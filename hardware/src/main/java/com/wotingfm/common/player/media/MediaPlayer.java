//package com.wotingfm.common.player.media;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.net.Uri;
//import android.os.Handler;
//import android.os.Message;
//import android.util.Log;
//
//import com.videolan.libvlc.EventHandler;
//import com.videolan.libvlc.LibVLC;
//import com.videolan.vlc.util.VLCInstance;
//
//
///**
// * 作者：xinLong on 2017/5/23 14:43
// * 邮箱：645700751@qq.com
// */
//public class MediaPlayer {
//    private Context mAppContext;
//    private LibVLC mVlc;
//
//    /**
//     * 代码构造函数
//     *
//     * @param context
//     */
//    public MediaPlayer(Context context) {
//        initVideo(context);
//    }
//
//    private void initVideo(Context context) {
//        if(mVlc != null) return ;
//        try {
//            mVlc = VLCInstance.getLibVlcInstance();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        EventHandler em = EventHandler.getInstance();
//        em.addHandler(mVlcHandler);
//    }
//
//    // VLC
//    @SuppressLint("HandlerLeak")
//    private Handler mVlcHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            if (msg == null || msg.getData() == null) return ;
//            switch (msg.getData().getInt("event")) {
//                case EventHandler.MediaPlayerEncounteredError:// 播放出现错误播下一首
//                    break;
//                case EventHandler.MediaPlayerEndReached:// 播放完成播下一首
//
//                    break;
//            }
//        }
//    };
//
//
//    /**
//     * 获取当前的mediaplayer，可能为null
//     *
//     * @return 返回当前的player对象，可能为null
//     */
//    public IMediaPlayer getCurrentMediaPlayer() {
//        return mMediaPlayerBase;
//    }
//
//    /**
//     * 设置播放地址
//     *
//     * @param path 播放地址
//     */
//    public void setVideoPath(String path) {
//        Log.e("此时播放地址===", path + "");
//        setVideoURI(Uri.parse(path));
//    }
//
//    /**
//     * 是否正在播放
//     *
//     * @return
//     */
//    public boolean isPlaying() {
//        return  mVlc.isPlaying();
//    }
//
//    /**
//     * 将播放器指定到某个播放位置
//     *
//     * @param msec
//     */
//    public void seekTo(long msec) {
//        if (isInPlaybackState()) {
//            mVlc.see(msec);
//        }
//    }
//
//    /**
//     * 获取当前播放进度，单位为毫秒！
//     *
//     * @return
//     */
//    public int getCurrentPosition() {
//        if (isInPlaybackState()) {
//            return (int) mMediaPlayerBase.getCurrentPosition();
//        }
//        return 0;
//    }
//
//    /**
//     * 获得视频时长，单位为毫秒！
//     *
//     * @return
//     */
//    public int getDuration() {
//        if (isInPlaybackState()) {
//            return (int) mMediaPlayerBase.getDuration();
//        }
//
//        return 0;
//    }
//
//    /**
//     * 获取多码率的字串数组
//     * 每个String的格式为
//     *
//     * @return
//     */
//    public String[] getVariantInfo() {
//        if (mMediaPlayerBase != null) {
//            return mMediaPlayerBase.getVariantInfo();
//        }
//        return null;
//    }
//
//    /**
//     * 获取当前网络内容下载速率
//     *
//     * @return
//     */
//    public long getDownloadSpeed() {
//        if (mMediaPlayerBase != null) {
//            return mMediaPlayerBase.getDownloadSpeed();
//
//        }
//        return 0L;
//    }
//
//    /**
//     * 获取当前正在播的节目
//     *
//     * @return
//     */
//    public String getCurrentPlayingUrl() {
//        if (this.mUri != null) {
//            return this.mUri.toString();
//        }
//        return null;
//    }
//
//    /**
//     * 获取播放器当前的状态
//     *
//     * @return 播放器当前的状态
//     */
//    public PlayerState getCurrentPlayerState() {
//        return mCurrentState;
//    }
//
//
//    /**
//     * 设置"加载中"触发时，缓存多长时间的数据才结束
//     * * 注意：若mMediaPlayerBase为null时，实际上会在createPlayer()中设置，这是mCacheTimeInMilliSeconds成员在此赋值的原因；
//     *
//     * @param milliSeconds
//     */
//    public void setBufferTimeInMs(int milliSeconds) {
//        this.mCacheTimeInMilliSeconds = milliSeconds;
//        if (mMediaPlayerBase != null) {
//            mMediaPlayerBase.setBufferTimeInMs(mCacheTimeInMilliSeconds);
//        }
//    }
//
//    /**
//     * 设置"加载中"触发时，需要缓冲多大的数据才结束
//     *
//     * @param sizeInBytes
//     */
//    public void setBufferSizeInBytes(int sizeInBytes) {
//        this.mBufferSizeInBytes = sizeInBytes;
//        if (mMediaPlayerBase != null) {
//            mMediaPlayerBase.setBufferSizeInBytes(mBufferSizeInBytes);
//        }
//    }
//
//    /**
//     * 设置是否循环播放
//     *
//     * @param isLoop
//     */
//    public void setLooping(boolean isLoop) {
//        this.mLooping = isLoop;
//        if (mMediaPlayerBase != null) {
//            mMediaPlayerBase.setLooping(mLooping);
//        }
//    }
//
//    /**
//     * 设置最大缓存数据大小
//     *
//     * @param sizeInBytes
//     */
//    public void setMaxCacheSizeInBytes(int sizeInBytes) {
//        mMaxCacheSizeInBytes = sizeInBytes;
//        if (mMediaPlayerBase != null) {
//            mMediaPlayerBase.setMaxCacheSizeInBytes(mMaxCacheSizeInBytes);
//        }
//    }
//
//    /**
//     * 设置最大探测的数据大小
//     *
//     * @param maxProbeSizeInBytes
//     */
//    public void setMaxProbeSize(int maxProbeSizeInBytes) {
//        mMaxProbeSizeInBytes = maxProbeSizeInBytes;
//        if (mMediaPlayerBase != null) {
//            mMediaPlayerBase.setMaxProbeSize(mMaxProbeSizeInBytes);
//        }
//    }
//
//    /**
//     * 设置最大探测时长
//     * 类似于老接口 setFirstBufferingTime
//     *
//     * @param maxProbeTimeInMs
//     */
//    public void setMaxProbeTime(int maxProbeTimeInMs) {
//        mMaxProbeTimeInMs = maxProbeTimeInMs;
//        if (mMediaPlayerBase != null) {
//            mMediaPlayerBase.setMaxProbeTime(mMaxProbeTimeInMs);
//        }
//    }
//
//    /**
//     * 设置是否开启APM探测；如果开启，需要额外嵌入APM SDK！
//     *
//     * @param useApmDetect
//     */
//    public void setUseApmDetect(boolean useApmDetect) {
//        mUseApmDetect = useApmDetect;
//        if (mMediaPlayerBase != null) {
//            mMediaPlayerBase.setUseApmDetect(mUseApmDetect);
//        }
//    }
//
//    /**
//     * 设置左右声道的音量
//     *
//     * @param leftVolume
//     * @param rightVolume
//     */
//    public void setVolume(float leftVolume, float rightVolume) {
//        mLeftVolume = leftVolume;
//        mRightVolume = rightVolume;
//        if (mMediaPlayerBase != null) {
//            mMediaPlayerBase.setVolume(mLeftVolume, mRightVolume);
//        }
//    }
//
//    /**
//     * 设置唤醒Mode
//     *
//     * @param context
//     * @param mode
//     */
//    public void setWakeMode(Context context, int mode) {
//        mWakeMode = mode;
//        if (mMediaPlayerBase != null) {
//            mMediaPlayerBase.setWakeMode(context, mWakeMode);
//        }
//    }
//
//    /**
//     * 设置初始播放位置
//     * 注意：若mMediaPlayerBase为null时，实际上会在createPlayer()中设置，这是mInitPlayPositionInMilliSec成员在此赋值的原因；
//     *
//     * @param milliSeconds
//     */
//    public void setInitPlayPosition(long milliSeconds) {
//        mInitPlayPositionInMilliSec = milliSeconds;
//        if (mMediaPlayerBase != null) {
//            mMediaPlayerBase.setInitPlayPosition(mInitPlayPositionInMilliSec);
//            // 设置给mediaplayer后重置为-1，防止影响新播放源的播放
//            mInitPlayPositionInMilliSec = -1;
//        }
//    }
//
//    /**
//     * 设置是否显示日志
//     * 注意：若mMediaPlayerBase为null时，实际上会在createPlayer()中设置，这是mLogEnable成员在此赋值的原因；
//     *
//     * @param enabled
//     */
//    public void setLogEnabled(boolean enabled) {
//        mLogEnabled = enabled;
//        if (mMediaPlayerBase != null) {
//            mMediaPlayerBase.setLogEnabled(mLogEnabled);
//        }
//    }
//
//    /**
//     * 设置解码模式
//     * 注意：若mMediaPlayerBase为null时，实际上会在createPlayer()中设置，这是mDecodeMode成员在此赋值的原因；
//     *
//     * @param decodeMode DECODE_AUTO(优先硬解，找不到硬解解码器则软解)或DECODE_SW(软解)
//     */
//    public void setDecodeMode(int decodeMode) {
//        mDecodeMode = decodeMode;
//        if (mMediaPlayerBase != null) {
//            mMediaPlayerBase.setDecodeMode(mDecodeMode);
//        }
//    }
//
//
//    public void destroy(){
//        release();
//    }
//}
