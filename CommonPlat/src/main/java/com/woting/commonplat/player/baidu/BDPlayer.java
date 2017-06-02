package com.woting.commonplat.player.baidu;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.widget.FrameLayout;


import com.baidu.cloud.media.player.BDCloudMediaPlayer;
import com.baidu.cloud.media.player.IMediaPlayer;

import java.io.IOException;


/**
 * 作者：xinLong on 2017/5/23 14:43
 * 邮箱：645700751@qq.com
 */
public class BDPlayer extends FrameLayout {
    private Context mAppContext;
    private static final String TAG = "woTingVideo";
    private boolean isTryToPlaying = false;
    private PlayerState mCurrentState = PlayerState.STATE_IDLE;// 播放器当前的状态
    private Uri mUri; // 播放链接

    // 监听
    private BDCloudMediaPlayer mMediaPlayer = null;
    private IMediaPlayer.OnCompletionListener mOnCompletionListener;
    private IMediaPlayer.OnPreparedListener mOnPreparedListener;
    private IMediaPlayer.OnErrorListener mOnErrorListener;
    private IMediaPlayer.OnInfoListener mOnInfoListener;
    private OnPlayerStateListener mOnPlayerStateListener;
    private IMediaPlayer.OnBufferingUpdateListener mOnBufferingUpdateListener;
    private IMediaPlayer.OnSeekCompleteListener mOnSeekCompleteListener;

    // 播放器参数
    private int mCacheTimeInMilliSeconds = 0;                       // 播放缓冲时间
    private int mDecodeMode = BDCloudMediaPlayer.DECODE_AUTO;       // 解码模式
    private boolean mLogEnabled = false;                            // 是否打印日志，一般不打印
    private long mInitPlayPositionInMilliSec = 0;                   // 播放位置
    private int mWakeMode = 0;                                      // 唤醒模式????????
    private float mLeftVolume = -1f;                                // 左声道音量
    private float mRightVolume = -1f;                               // 右声道音量
    private boolean mUseApmDetect = false;                          // 是否 开启APM检测，开启时需要额外嵌入APM SDK。
    private int mMaxProbeTimeInMs = 0;                              // 最大探测时长??????
    private int mMaxProbeSizeInBytes = 0;                           // 大探测的数据大小??????
    private int mMaxCacheSizeInBytes = 0;                           // 最大缓存数据大小
    private boolean mLooping = true;                                // 是否循环播放
    private int mBufferSizeInBytes = 0;                             // 设置"加载中"触发时，需要缓冲多大的数据才结束

    /**
     * 代码构造函数
     *
     * @param context
     */
    public BDPlayer(Context context) {
        super(context);
        initVideoView(context);
    }

    private void initVideoView(Context context) {
        mAppContext = context.getApplicationContext();
        setCurrentState(PlayerState.STATE_IDLE);
    }

    // 播放状态枚举类型
    public enum PlayerState {
        STATE_ERROR(-1),
        STATE_IDLE(0),
        STATE_PREPARING(1),
        STATE_PREPARED(2),
        STATE_PLAYING(3),
        STATE_PAUSED(4),
        STATE_PLAYBACK_COMPLETED(5);
        private int code;

        private PlayerState(int oCode) {
            code = oCode;
        }
    }

    // 设置此时播放状态
    private void setCurrentState(PlayerState newState) {
        if (mCurrentState != newState) {
            mCurrentState = newState;
            if (mOnPlayerStateListener != null) {
                mOnPlayerStateListener.onPlayerStateChanged(mCurrentState);
            }
        }
    }

    // 此时播放状态的接口
    public interface OnPlayerStateListener {
        public void onPlayerStateChanged(final PlayerState nowState);
    }

    // the URI of the video.
    private void setVideoURI(Uri uri) {
        mUri = uri;
        openVideo();
    }

    // 打开播放器
    private void openVideo() {
        if (mUri == null) {
            // not ready for playback just yet, will try again later
            return;
        }
        // we shouldn't clear the target state, because somebody might have
        // called start() previously
        release(false);
        AudioManager am = (AudioManager) mAppContext.getSystemService(Context.AUDIO_SERVICE);
        am.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        try {
            mMediaPlayer = createPlayer();
            mMediaPlayer.setOnPreparedListener(mPreparedListener);
            mMediaPlayer.setOnCompletionListener(mCompletionListener);
            mMediaPlayer.setOnErrorListener(mErrorListener);
            mMediaPlayer.setOnInfoListener(mInfoListener);
            mMediaPlayer.setOnBufferingUpdateListener(mBufferingUpdateListener);
            mMediaPlayer.setOnSeekCompleteListener(mSeekCompleteListener);
            mMediaPlayer.setDataSource(mAppContext, mUri);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setScreenOnWhilePlaying(true);
            mMediaPlayer.prepareAsync();

            // we don't set the target state here either, but preserve the
            // target state that was there before.
            setCurrentState(PlayerState.STATE_PREPARING);
            // attachMediaController();
        } catch (IOException ex) {
            Log.e(TAG, "Unable to open content: " + mUri, ex);
            setCurrentState(PlayerState.STATE_ERROR);
            // mTargetState = STATE_ERROR;
            isTryToPlaying = false;
            mErrorListener.onError(mMediaPlayer, MediaPlayer.MEDIA_ERROR_UNKNOWN, 0);
        } catch (IllegalArgumentException ex) {
            Log.e(TAG, "Unable to open content: " + mUri, ex);
            setCurrentState(PlayerState.STATE_ERROR);
            // mTargetState = STATE_ERROR;
            isTryToPlaying = false;
            mErrorListener.onError(mMediaPlayer, MediaPlayer.MEDIA_ERROR_UNKNOWN, 0);
        } finally {
            // REMOVED: mPendingSubtitleTracks.clear();
        }
    }

    public BDCloudMediaPlayer createPlayer() {
        BDCloudMediaPlayer bdCloudMediaPlayer = new BDCloudMediaPlayer(mAppContext);

        bdCloudMediaPlayer.setLogEnabled(mLogEnabled); // 打开播放器日志输出
        bdCloudMediaPlayer.setDecodeMode(mDecodeMode); // 设置解码模式
        if (mInitPlayPositionInMilliSec > -1) {
            bdCloudMediaPlayer.setInitPlayPosition(mInitPlayPositionInMilliSec); // 设置初始播放位置
            mInitPlayPositionInMilliSec = -1; // 置为-1，防止影响下个播放源
        }
        if (mWakeMode > 0) {
            bdCloudMediaPlayer.setWakeMode(mAppContext, mWakeMode);
        }
        if (mLeftVolume > -1 && mRightVolume > -1) {
            bdCloudMediaPlayer.setVolume(mLeftVolume, mRightVolume);
        }
        if (mUseApmDetect) {
            bdCloudMediaPlayer.setUseApmDetect(mUseApmDetect); // 开启APM检测，开启时需要额外嵌入APM SDK。
        }
        if (mCacheTimeInMilliSeconds > 0) {
            bdCloudMediaPlayer.setBufferTimeInMs(mCacheTimeInMilliSeconds); // 设置『加载中』的最长缓冲时间
        }

        if (mMaxProbeTimeInMs > 0) {
            bdCloudMediaPlayer.setMaxProbeTime(mMaxProbeTimeInMs);
        }
        if (mMaxProbeSizeInBytes > 0) {
            bdCloudMediaPlayer.setMaxProbeSize(mMaxProbeSizeInBytes);
        }
        if (mMaxCacheSizeInBytes > 0) {
            bdCloudMediaPlayer.setMaxCacheSizeInBytes(mMaxCacheSizeInBytes);
        }
        if (mLooping) {
            bdCloudMediaPlayer.setLooping(mLooping);
        }
        if (mBufferSizeInBytes > 0) {
            bdCloudMediaPlayer.setBufferSizeInBytes(mBufferSizeInBytes);
        }

        return bdCloudMediaPlayer;
    }

    IMediaPlayer.OnPreparedListener mPreparedListener = new IMediaPlayer.OnPreparedListener() {
        public void onPrepared(IMediaPlayer mp) {
            Log.e(TAG, "onPrepared");
            setCurrentState(PlayerState.STATE_PREPARED);
            if (mOnPreparedListener != null) {
                mOnPreparedListener.onPrepared(mMediaPlayer);
            }
            if (isTryToPlaying) {
                start();
            }
        }
    };

    private IMediaPlayer.OnCompletionListener mCompletionListener =
            new IMediaPlayer.OnCompletionListener() {
                public void onCompletion(IMediaPlayer mp) {
                    Log.e(TAG, "onCompletion");
                    setCurrentState(PlayerState.STATE_PLAYBACK_COMPLETED);
                    isTryToPlaying = false;
                    if (mOnCompletionListener != null) {
                        mOnCompletionListener.onCompletion(mMediaPlayer);
                    }
                }
            };

    private IMediaPlayer.OnInfoListener mInfoListener =
            new IMediaPlayer.OnInfoListener() {
                public boolean onInfo(IMediaPlayer mp, int arg1, int arg2) {
                    Log.e(TAG, "onInfo: arg1=" + arg1 + "; arg2=" + arg2);
                    if (mOnInfoListener != null) {
                        mOnInfoListener.onInfo(mp, arg1, arg2);
                    }
                    switch (arg1) {
                        case IMediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:
                            Log.d(TAG, "MEDIA_INFO_VIDEO_TRACK_LAGGING:");
                            break;
                        case IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                            Log.d(TAG, "MEDIA_INFO_VIDEO_RENDERING_START:");
                            break;
                        case IMediaPlayer.MEDIA_INFO_BUFFERING_START:
                            Log.d(TAG, "MEDIA_INFO_BUFFERING_START:");
                            break;
                        case IMediaPlayer.MEDIA_INFO_BUFFERING_END:
                            Log.d(TAG, "MEDIA_INFO_BUFFERING_END:");
                            break;
                        case IMediaPlayer.MEDIA_INFO_NETWORK_BANDWIDTH:
                            Log.d(TAG, "MEDIA_INFO_NETWORK_BANDWIDTH: " + arg2);
                            break;
                        case IMediaPlayer.MEDIA_INFO_BAD_INTERLEAVING:
                            Log.d(TAG, "MEDIA_INFO_BAD_INTERLEAVING:");
                            break;
                        case IMediaPlayer.MEDIA_INFO_NOT_SEEKABLE:
                            Log.d(TAG, "MEDIA_INFO_NOT_SEEKABLE:");
                            break;
                        case IMediaPlayer.MEDIA_INFO_METADATA_UPDATE:
                            Log.d(TAG, "MEDIA_INFO_METADATA_UPDATE:");
                            break;
                        case IMediaPlayer.MEDIA_INFO_UNSUPPORTED_SUBTITLE:
                            Log.d(TAG, "MEDIA_INFO_UNSUPPORTED_SUBTITLE:");
                            break;
                        case IMediaPlayer.MEDIA_INFO_SUBTITLE_TIMED_OUT:
                            Log.d(TAG, "MEDIA_INFO_SUBTITLE_TIMED_OUT:");
                            break;
                        case IMediaPlayer.MEDIA_INFO_VIDEO_ROTATION_CHANGED:
                            Log.d(TAG, "MEDIA_INFO_VIDEO_ROTATION_CHANGED: " + arg2);
                            break;
                        case IMediaPlayer.MEDIA_INFO_AUDIO_RENDERING_START:
                            Log.d(TAG, "MEDIA_INFO_AUDIO_RENDERING_START:");
                            break;
                    }
                    return true;
                }
            };

    private IMediaPlayer.OnErrorListener mErrorListener =
            new IMediaPlayer.OnErrorListener() {
                public boolean onError(IMediaPlayer mp, int framework_err, int impl_err) {
                    Log.e(TAG, "onError: " + framework_err + "," + impl_err);
                    setCurrentState(PlayerState.STATE_ERROR);
                    isTryToPlaying = false;
                    /* If an error handler has been supplied, use it and finish. */
                    if (mOnErrorListener != null) {
                        if (mOnErrorListener.onError(mMediaPlayer, framework_err, impl_err)) {
                            return true;
                        }
                    }
                    return true;
                }
            };

    private IMediaPlayer.OnBufferingUpdateListener mBufferingUpdateListener =
            new IMediaPlayer.OnBufferingUpdateListener() {
                public void onBufferingUpdate(IMediaPlayer mp, int percent) {
                    Log.e(TAG, "onBufferingUpdate: percent=" + percent);
                    if (mOnBufferingUpdateListener != null) {
                        mOnBufferingUpdateListener.onBufferingUpdate(mp, percent);
                    }
                }
            };

    private IMediaPlayer.OnSeekCompleteListener mSeekCompleteListener = new IMediaPlayer.OnSeekCompleteListener() {

        @Override
        public void onSeekComplete(IMediaPlayer mp) {
            Log.e(TAG, "onSeekComplete");
            if (mOnSeekCompleteListener != null) {
                mOnSeekCompleteListener.onSeekComplete(mp);
            }
        }
    };

    // 内部实现方法//////////////////////////////////////////////////////////////////////////////////

    private boolean isInPlaybackState() {
        return (mMediaPlayer != null &&
                mCurrentState != PlayerState.STATE_ERROR &&
                mCurrentState != PlayerState.STATE_IDLE &&
                mCurrentState != PlayerState.STATE_PREPARING);
    }

    // release the media player in any state
    private void release(boolean cleartargetstate) {
        if (mMediaPlayer != null) {
            // comment this line: may anr
            // mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
            setCurrentState(PlayerState.STATE_IDLE);
            if (cleartargetstate) {
                isTryToPlaying = false;
            }
            AudioManager am = (AudioManager) mAppContext.getSystemService(Context.AUDIO_SERVICE);
            am.abandonAudioFocus(null);
        }
    }

    // 监听接口/////////////////////////////////////////////////////////////////////////////////////

    /**
     * 播放状态的监听
     *
     * @param l 监听结果
     */
    public void setOnPlayerStateListener(OnPlayerStateListener l) {
        mOnPlayerStateListener = l;
    }

    /**
     * 在加载媒体文件时注册一个回调函数，并准备执行。
     *
     * @param l 监听结果
     */
    public void setOnPreparedListener(IMediaPlayer.OnPreparedListener l) {
        mOnPreparedListener = l;
    }

    /**
     * 注册一个回调，以便在回放期间到达媒体文件的结束时调用。
     *
     * @param l 监听结果
     */
    public void setOnCompletionListener(IMediaPlayer.OnCompletionListener l) {
        mOnCompletionListener = l;
    }

    /**
     * 当在回放或设置过程中出现错误时，注册一个回调函数。如果没有指定侦听器，或者如果侦听器返回false,VideoView将通知用户任何错误。
     *
     * @param l 监听结果
     */
    public void setOnErrorListener(IMediaPlayer.OnErrorListener l) {
        mOnErrorListener = l;
    }

    /**
     * 当在回放或设置过程中发生信息事件时，注册一个回调函数。
     *
     * @param l 监听结果
     */
    public void setOnInfoListener(IMediaPlayer.OnInfoListener l) {
        mOnInfoListener = l;
    }

    /**
     * 缓冲更新的监听
     *
     * @param l 监听结果
     */
    public void setOnBufferingUpdateListener(IMediaPlayer.OnBufferingUpdateListener l) {
        mOnBufferingUpdateListener = l;
    }

    /**
     * 完成的监听
     *
     * @param l 监听结果
     */
    public void setOnSeekCompleteListener(IMediaPlayer.OnSeekCompleteListener l) {
        mOnSeekCompleteListener = l;
    }

    // 播放控制/////////////////////////////////////////////////////////////////////////////////////

    /**
     * 开始或继续播放
     * <p/>
     * complete状态时不支持拖动，因为该函数重新prepareAsync了。
     * 为何complete需要重新prepare：有些情况下，直播中断会返回complete，此时需要重新prepareAsync.
     */
    public void start() {
        if (mMediaPlayer != null && (mCurrentState == PlayerState.STATE_ERROR)
                || mCurrentState == PlayerState.STATE_PLAYBACK_COMPLETED) {

            // if your link is not live link, you can comment the following if block
            if (mCurrentState == PlayerState.STATE_PLAYBACK_COMPLETED) {
                // complete --> stop --> prepareAsync
                mMediaPlayer.stop();
            }

            mMediaPlayer.prepareAsync(); // will start() in onPrepared, because isTryToPlaying = true
            setCurrentState(PlayerState.STATE_PREPARING);
        } else if (isInPlaybackState()) {
            mMediaPlayer.start();
            setCurrentState(PlayerState.STATE_PLAYING);
        }
        isTryToPlaying = true;
    }

    /**
     * 暂停播放
     */
    public void pause() {
        if (isInPlaybackState()) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
                setCurrentState(PlayerState.STATE_PAUSED);
            }
        }
        isTryToPlaying = false;
    }

    /**
     * 停止播放并释放资源
     * 如果仅想停止播放，请调用pause()
     */
    public void stopPlayback() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
            setCurrentState(PlayerState.STATE_IDLE);
            isTryToPlaying = false;
            AudioManager am = (AudioManager) mAppContext.getSystemService(Context.AUDIO_SERVICE);
            am.abandonAudioFocus(null);
        }
    }

    /**
     * 释放后不能再使用该BDCloudVideoView对象
     */
    public void release() {
        // 释放播放器player
        release(true);
    }

    // 外部调用方法//////////////////////////////////////////////////////////////////////////////////

    /**
     * 设置ak
     *
     * @param ak
     */
    public static void setAK(String ak) {
        BDCloudMediaPlayer.setAK(ak);
    }

    /**
     * 获取当前的mediaplayer，可能为null
     *
     * @return 返回当前的player对象，可能为null
     */
    public IMediaPlayer getCurrentMediaPlayer() {
        return mMediaPlayer;
    }

    /**
     * 设置播放地址
     *
     * @param path 播放地址
     */
    public void setVideoPath(String path) {
        Log.e("此时播放地址===", path + "");
        setVideoURI(Uri.parse(path));
    }

    /**
     * 是否正在播放
     *
     * @return
     */
    public boolean isPlaying() {
        return isInPlaybackState() && mMediaPlayer.isPlaying();
    }

    /**
     * 将播放器指定到某个播放位置
     *
     * @param msec
     */
    public void seekTo(int msec) {
        if (isInPlaybackState()) {
            mMediaPlayer.seekTo(msec);
        }
    }

    /**
     * 获取当前播放进度，单位为毫秒！
     *
     * @return
     */
    public int getCurrentPosition() {
        if (isInPlaybackState()) {
            return (int) mMediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    /**
     * 获得视频时长，单位为毫秒！
     *
     * @return
     */
    public int getDuration() {
        if (isInPlaybackState()) {
            return (int) mMediaPlayer.getDuration();
        }

        return 0;
    }

    /**
     * 获取多码率的字串数组
     * 每个String的格式为
     *
     * @return
     */
    public String[] getVariantInfo() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.getVariantInfo();
        }
        return null;
    }

    /**
     * 获取当前网络内容下载速率
     *
     * @return
     */
    public long getDownloadSpeed() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.getDownloadSpeed();

        }
        return 0L;
    }

    /**
     * 获取当前正在播的节目
     *
     * @return
     */
    public String getCurrentPlayingUrl() {
        if (this.mUri != null) {
            return this.mUri.toString();
        }
        return null;
    }

    /**
     * 获取播放器当前的状态
     *
     * @return 播放器当前的状态
     */
    public PlayerState getCurrentPlayerState() {
        return mCurrentState;
    }


    /**
     * 设置"加载中"触发时，缓存多长时间的数据才结束
     * * 注意：若mMediaPlayer为null时，实际上会在createPlayer()中设置，这是mCacheTimeInMilliSeconds成员在此赋值的原因；
     *
     * @param milliSeconds
     */
    public void setBufferTimeInMs(int milliSeconds) {
        this.mCacheTimeInMilliSeconds = milliSeconds;
        if (mMediaPlayer != null) {
            mMediaPlayer.setBufferTimeInMs(mCacheTimeInMilliSeconds);
        }
    }

    /**
     * 设置"加载中"触发时，需要缓冲多大的数据才结束
     *
     * @param sizeInBytes
     */
    public void setBufferSizeInBytes(int sizeInBytes) {
        this.mBufferSizeInBytes = sizeInBytes;
        if (mMediaPlayer != null) {
            mMediaPlayer.setBufferSizeInBytes(mBufferSizeInBytes);
        }
    }

    /**
     * 设置是否循环播放
     *
     * @param isLoop
     */
    public void setLooping(boolean isLoop) {
        this.mLooping = isLoop;
        if (mMediaPlayer != null) {
            mMediaPlayer.setLooping(mLooping);
        }
    }

    /**
     * 设置最大缓存数据大小
     *
     * @param sizeInBytes
     */
    public void setMaxCacheSizeInBytes(int sizeInBytes) {
        mMaxCacheSizeInBytes = sizeInBytes;
        if (mMediaPlayer != null) {
            mMediaPlayer.setMaxCacheSizeInBytes(mMaxCacheSizeInBytes);
        }
    }

    /**
     * 设置最大探测的数据大小
     *
     * @param maxProbeSizeInBytes
     */
    public void setMaxProbeSize(int maxProbeSizeInBytes) {
        mMaxProbeSizeInBytes = maxProbeSizeInBytes;
        if (mMediaPlayer != null) {
            mMediaPlayer.setMaxProbeSize(mMaxProbeSizeInBytes);
        }
    }

    /**
     * 设置最大探测时长
     * 类似于老接口 setFirstBufferingTime
     *
     * @param maxProbeTimeInMs
     */
    public void setMaxProbeTime(int maxProbeTimeInMs) {
        mMaxProbeTimeInMs = maxProbeTimeInMs;
        if (mMediaPlayer != null) {
            mMediaPlayer.setMaxProbeTime(mMaxProbeTimeInMs);
        }
    }

    /**
     * 设置是否开启APM探测；如果开启，需要额外嵌入APM SDK！
     *
     * @param useApmDetect
     */
    public void setUseApmDetect(boolean useApmDetect) {
        mUseApmDetect = useApmDetect;
        if (mMediaPlayer != null) {
            mMediaPlayer.setUseApmDetect(mUseApmDetect);
        }
    }

    /**
     * 设置左右声道的音量
     *
     * @param leftVolume
     * @param rightVolume
     */
    public void setVolume(float leftVolume, float rightVolume) {
        mLeftVolume = leftVolume;
        mRightVolume = rightVolume;
        if (mMediaPlayer != null) {
            mMediaPlayer.setVolume(mLeftVolume, mRightVolume);
        }
    }

    /**
     * 设置唤醒Mode
     *
     * @param context
     * @param mode
     */
    public void setWakeMode(Context context, int mode) {
        mWakeMode = mode;
        if (mMediaPlayer != null) {
            mMediaPlayer.setWakeMode(context, mWakeMode);
        }
    }

    /**
     * 设置初始播放位置
     * 注意：若mMediaPlayer为null时，实际上会在createPlayer()中设置，这是mInitPlayPositionInMilliSec成员在此赋值的原因；
     *
     * @param milliSeconds
     */
    public void setInitPlayPosition(long milliSeconds) {
        mInitPlayPositionInMilliSec = milliSeconds;
        if (mMediaPlayer != null) {
            mMediaPlayer.setInitPlayPosition(mInitPlayPositionInMilliSec);
            // 设置给mediaplayer后重置为-1，防止影响新播放源的播放
            mInitPlayPositionInMilliSec = -1;
        }
    }

    /**
     * 设置是否显示日志
     * 注意：若mMediaPlayer为null时，实际上会在createPlayer()中设置，这是mLogEnable成员在此赋值的原因；
     *
     * @param enabled
     */
    public void setLogEnabled(boolean enabled) {
        mLogEnabled = enabled;
        if (mMediaPlayer != null) {
            mMediaPlayer.setLogEnabled(mLogEnabled);
        }
    }

    /**
     * 设置解码模式
     * 注意：若mMediaPlayer为null时，实际上会在createPlayer()中设置，这是mDecodeMode成员在此赋值的原因；
     *
     * @param decodeMode DECODE_AUTO(优先硬解，找不到硬解解码器则软解)或DECODE_SW(软解)
     */
    public void setDecodeMode(int decodeMode) {
        mDecodeMode = decodeMode;
        if (mMediaPlayer != null) {
            mMediaPlayer.setDecodeMode(mDecodeMode);
        }
    }
}
