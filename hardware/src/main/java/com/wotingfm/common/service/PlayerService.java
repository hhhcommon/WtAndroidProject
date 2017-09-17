package com.wotingfm.common.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.kingsoft.media.httpcache.KSYProxyService;
import com.kingsoft.media.httpcache.OnCacheStatusListener;
import com.kingsoft.media.httpcache.OnErrorListener;
import com.kingsoft.media.httpcache.stats.OnLogEventListener;
import com.nostra13.universalimageloader.utils.L;
import com.woting.commonplat.ksy.KSYProxy;
import com.woting.commonplat.utils.ResourceUtil;
import com.wotingfm.ui.bean.MessageEvent;
import com.wotingfm.ui.play.localaudio.service.DownloadClient;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * 播放服务
 * 作者：xinlong on 2017/7/13 23:57
 * 邮箱：645700751@qq.com
 */
public class PlayerService extends Service {
    private static int secondProgress;
    private DownloadClient downloadClient;
    private static android.media.MediaPlayer mediaPlayer;
    private static Handler mHandler = new Handler();
    private static int n = 0;// 测试计数
    //    private static Intent it = new Intent(BroadcastConstants.PLAY_TIME_CHANGE);
    private static PlayerService context;
    private static KSYProxyService proxy;
    private static String mDataSource;
    private static int playerType;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        initCache();
        initMM();
        initDO();
        new UploadDataThread().start();// 定量上传数据线程
    }

    // 初始化播放缓存
    private void initCache() {
        proxy = KSYProxy.getKSYProxy(context);
        File file = new File(ResourceUtil.getLocalUrlForKsy());// 设置缓存目录
        if (!file.exists()) if (!file.mkdirs()) L.w("TAG", "KSYProxy MkDir Error");
        proxy.setCacheRoot(file);
        proxy.setMaxCacheSize(5000 * 1024 * 1024);// 缓存大小 5000MB
        proxy.startServer();

    }

    // 初始化下载数据库
    private void initDO() {
        downloadClient = new DownloadClient(this);
    }

    // 初始化播放器
    private void initMM() {
        mediaPlayer = new android.media.MediaPlayer();
        mediaPlayer.setOnPreparedListener(mPreparedListener);
        mediaPlayer.setOnCompletionListener(mCompletionListener);
        mediaPlayer.setOnErrorListener(mErrorListener);
    }

    android.media.MediaPlayer.OnPreparedListener mPreparedListener = new android.media.MediaPlayer.OnPreparedListener() {
        public void onPrepared(android.media.MediaPlayer mp) {
            Log.e("监听", "播放准备好");
            EventBus.getDefault().post(new MessageEvent(2001));
            mHandler.postDelayed(mUpdatePlayTimeRunnable, 1000);
            mediaPlayer.start();
        }
    };

    android.media.MediaPlayer.OnErrorListener mErrorListener = new android.media.MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            Log.e("监听", "播放出错");
            return false;
        }
    };
    android.media.MediaPlayer.OnCompletionListener mCompletionListener =
            new android.media.MediaPlayer.OnCompletionListener() {
                public void onCompletion(android.media.MediaPlayer mp) {
                    Log.e("监听", "播放完成");
                    EventBus.getDefault().post(new MessageEvent(2002));
                }
            };

    /**
     * 设置播放地址
     *
     * @param url 播放地址
     */
    public static void play(int playerType, String url) {
        String srcUrl;
        context.playerType=playerType;
        if (playerType == 1) {
            Log.e("播放地址", "" + url);
            if (!isCacheFinish(url)) {// 判断是否已经缓存过  没有则开始缓存
                Log.e("isCache","false");
                proxy.registerCacheStatusListener(mCacheStatusListener, url);
            } else {
                Log.e("isCache","true");
                secondProgress = -1;
                EventBus.getDefault().post(new MessageEvent(2004, secondProgress));
            }
            String _url = getCacheUrl(url);
            Log.e("缓存地址", "" + _url);
            srcUrl = _url;
        } else {
            srcUrl = url;
        }
        if (mUpdatePlayTimeRunnable != null) mHandler.removeCallbacks(mUpdatePlayTimeRunnable);
        mediaPlayer.reset();
        play(srcUrl);
    }

    // 判断是否已经缓存完成
    private static boolean isCacheFinish(String url) {
        HashMap<String, File> cacheMap = proxy.getCachedFileList();
        File cacheFile = cacheMap.get(url);
        return cacheFile != null && cacheFile.length() > 0;
    }

    //获取缓存路径
    private static String getCacheUrl(String url) {
        mDataSource = url;
        String urls = proxy.getProxyUrl(url);
        proxy.registerErrorListener(mOnErrorListener);
        proxy.registerLogEventListener(mOnLogListener);
        return urls;
    }

    private static OnCacheStatusListener mCacheStatusListener = new OnCacheStatusListener() {
        @Override
        public void OnCacheStatus(String url, long sourceLength, int percentsAvailable) {
            Log.d("cache", String.format("OnCacheStatus listener percents: %d, sourceLength: %d, url: %s",
                    percentsAvailable, sourceLength, url));
//        cachelength = (int)length*percentsAvailable/100;
//        Log.d("cachetest","cached length: "+cachelength);
//        this.cachePercents = percentsAvailable;
//        mPlayerSeekbar
            secondProgress=percentsAvailable;
            EventBus.getDefault().post(new MessageEvent(2004, secondProgress));
        }
    };

    private static OnErrorListener mOnErrorListener = new OnErrorListener() {
        @Override
        public void OnError(int errCode) {
            Log.d("cache", "onError listener " + errCode);
        }
    };

    private static OnLogEventListener mOnLogListener = new OnLogEventListener() {
        @Override
        public void onLogEvent(String s) {
            Log.d("cache", "stat log: " + s);
        }
    };

    /**
     * 是否正在播放
     *
     * @return
     */
    public static boolean isPlaying(int playerTyp) {
        return mediaPlayer.isPlaying();
    }

    /**
     * 播放列表
     */
    private static void play(String path) {
        try {
            // 设置文件路径
            mediaPlayer.setDataSource(path);
            // 异步准备
            mediaPlayer.prepareAsync();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 暂停播放
     */
    public static void playPause() {
        if(playerType==1)proxy.stopPreDownload(mDataSource);
        mediaPlayer.pause();
        if (mUpdatePlayTimeRunnable != null) mHandler.removeCallbacks(mUpdatePlayTimeRunnable);
    }

    /**
     * 继续播放
     *
     * @param playerType
     */
    public static void start(int playerType) {
        try {
            if(playerType==1)proxy.startPreDownload(mDataSource);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
        mHandler.postDelayed(mUpdatePlayTimeRunnable, 1000);
    }

    /**
     * 获取总长
     *
     * @param playerType
     * @return
     */
    public static int getCurrentPosition(int playerType) {
        return mediaPlayer.getCurrentPosition();
    }

    /**
     * 获取当前时长
     *
     * @param playerType
     * @return
     */
    public static int getDuration(int playerType) {
        return mediaPlayer.getDuration();
    }

    public void onStop() {
        mediaPlayer.stop();
    }

    /**
     * 设置进度
     *
     * @param playerType
     * @param progress
     */
    public static void seekTo(int playerType, long progress) {
        mediaPlayer.seekTo((int) progress);
    }

    // 更新时间
    private static Runnable mUpdatePlayTimeRunnable = new Runnable() {
        @Override
        public void run() {
            Log.e("时间", "更新" + n++);
            EventBus.getDefault().post(new MessageEvent(2003));
            mHandler.postDelayed(this, 1000);
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        downloadClient.unregister();
        if (mUpdatePlayTimeRunnable != null) {
            mHandler.removeCallbacks(mUpdatePlayTimeRunnable);
            mUpdatePlayTimeRunnable = null;
        }
        mHandler = null;
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
        if (proxy != null) {
            proxy.unregisterCacheStatusListener(mCacheStatusListener, mDataSource);
            proxy.unregisterErrorListener(mOnErrorListener);
            proxy.unregisterLogEventListener(mOnLogListener);
        }
    }

}
