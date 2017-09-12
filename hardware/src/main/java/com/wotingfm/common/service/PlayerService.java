package com.wotingfm.common.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.baidu.cloud.media.player.IMediaPlayer;
import com.pili.pldroid.player.PLMediaPlayer;
import com.woting.commonplat.player.baidu.BDPlayer;
import com.woting.commonplat.player.qiniu.QNPlayer;
import com.wotingfm.ui.bean.MessageEvent;
import com.wotingfm.ui.play.localaudio.service.DownloadClient;

import org.greenrobot.eventbus.EventBus;

/**
 * 播放服务
 * 作者：xinlong on 2017/7/13 23:57
 * 邮箱：645700751@qq.com
 */
public class PlayerService extends Service {
    private static PlayerService context;
    private static QNPlayer nnPlayer;
    private static BDPlayer bdPlayer;
    private DownloadClient downloadClient;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        initBD();
        initNN();
        initDO();
        setListener();
    }

    private void initDO() {
        downloadClient = new DownloadClient(this);
    }

    private void initNN() {
        if (nnPlayer == null) nnPlayer = new QNPlayer(context);
    }

    private void initBD() {
        if (bdPlayer == null) bdPlayer = new BDPlayer(context);
    }

    private void setListener() {
        bdPlayer.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer iMediaPlayer) {
                Log.e("百度监听", "播放准备好");
                EventBus.getDefault().post(new MessageEvent(1001));
            }
        });
        bdPlayer.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IMediaPlayer iMediaPlayer) {
                Log.e("百度监听", "播放完成");
                EventBus.getDefault().post(new MessageEvent(1002));
            }
        });
        nnPlayer.setOnPreparedListener(new PLMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(PLMediaPlayer mp, int preparedTime) {
                Log.e("大牛监听", "播放准备好");
                EventBus.getDefault().post(new MessageEvent(2001));
            }
        });
        nnPlayer.setOnCompletionListener(new PLMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(PLMediaPlayer mp) {
                Log.e("大牛监听", "播放完成");
                EventBus.getDefault().post(new MessageEvent(2002));
            }
        });
    }

    public static void play(int playerType,String url) {
        if (playerType == 1) {
            bdPlayer.setVideoPath(url);
            bdPlayer.start();
        } else {
            nnPlayer.onClickPlay(url);
        }
    }

    public static void playPause() {
//        if (bdPlayer.getCurrentPlayerState() == BDPlayer.PlayerState.STATE_PLAYING)
            bdPlayer.pause();

//        if (nnPlayer.getCurrentPlayerState() == PlayerState.PLAYING)
            nnPlayer.onClickPause();
    }

    public static void start(int playerType) {
        if (playerType == 1) {
            bdPlayer.start();
        } else {
            nnPlayer.onClickResume();
        }
    }

    public static Object getCurrentPlayerState(int playerType) {
        if (playerType == 1) {
            return bdPlayer.getCurrentPlayerState();
        } else {
            return nnPlayer.getCurrentPlayerState();
        }
    }

    public static int getCurrentPosition(int playerType) {
        if (playerType == 1) {
            return bdPlayer.getCurrentPosition();
        } else {
            return nnPlayer.getCurrentPosition();
        }
    }

    public static int getDuration(int playerType) {
        if (playerType == 1) {
            return bdPlayer.getDuration();
        } else {
            return nnPlayer.getDuration();
        }
    }

    public static void release(int playerType) {
        if (playerType == 1) {
            bdPlayer.stopPlayback();
        } else {
            nnPlayer.onClickStop();
        }
    }

    public static void seekTo(int playerType,long progress) {
        if (playerType == 1) {
            bdPlayer.seekTo(progress);
        } else {
            nnPlayer.seekTo(progress);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        downloadClient.unregister();
        bdPlayer.destroy();
        nnPlayer.destroy();
    }
}
