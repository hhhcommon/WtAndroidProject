package com.woting.commonplat.player.qiniu;

import android.content.Context;
import android.media.AudioManager;
import android.os.PowerManager;
import android.util.Log;

import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PLMediaPlayer;

import java.io.IOException;


/**
 * 作者：xinLong on 2017/5/23 14:43
 * 邮箱：645700751@qq.com
 */
public class QNPlayer {

    private Context context;
    private PLMediaPlayer mMediaPlayer;
    private AVOptions mAVOptions;
    private boolean mIsStopped = false;
    private String TAG = "大牛";

    /**
     * 代码构造函数
     *
     * @param contexts
     */
    public QNPlayer(Context contexts) {
        context = contexts.getApplicationContext();
        mAVOptions = new AVOptions();
        // the unit of timeout is ms
        mAVOptions.setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 10 * 1000);
        // 1 -> hw codec enable, 0 -> disable [recommended]
        int codec = 0;
        mAVOptions.setInteger(AVOptions.KEY_MEDIACODEC, codec);

        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        prepare();
    }

    /**
     * 开始播放
     * @param mAudioPath
     */
    public void onClickPlay(String mAudioPath) {
        if (mIsStopped) {
            prepare();
            try {
                mMediaPlayer.setDataSource(mAudioPath);
                mMediaPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            mMediaPlayer.start();
        }
    }

    /**
     * 暂停播放
     */
    public void onClickPause() {
        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
        }
    }

    /**
     * 继续播放
     */
    public void onClickResume() {
        if (mMediaPlayer != null) {
            mMediaPlayer.start();
        }
    }

    /**
     * 停止播放
     */
    public void onClickStop() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
        }
        mIsStopped = true;
    }

    /**
     * 将播放器指定到某个播放位置
     *
     * @param msec
     */
    public void seekTo(long msec) {
        if (mMediaPlayer != null) {
            mMediaPlayer.seekTo(msec);
        }
    }

    /**
     * 获取当前播放进度，单位为毫秒！
     *
     * @return
     */
    public int getCurrentPosition() {
        if (mMediaPlayer != null) {
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
        if (mMediaPlayer != null) {
            return (int) mMediaPlayer.getDuration();
        }

        return 0;
    }

    /**
     * 获取播放器当前的状态
     *
     * @return 播放器当前的状态
     */
    public boolean getCurrentPlayerState() {
        return mIsStopped;
    }

    /**
     * ,释放资源
     */
    public void release() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    private void prepare() {
        if (mMediaPlayer == null) {
            mMediaPlayer = new PLMediaPlayer(context, mAVOptions);
            mMediaPlayer.setDebugLoggingEnabled(true);
            mMediaPlayer.setLooping(false);
            mMediaPlayer.setOnPreparedListener(mOnPreparedListener);
            mMediaPlayer.setOnCompletionListener(mOnCompletionListener);
            mMediaPlayer.setOnErrorListener(mOnErrorListener);
            mMediaPlayer.setOnInfoListener(mOnInfoListener);
            mMediaPlayer.setOnBufferingUpdateListener(mOnBufferingUpdateListener);
            mMediaPlayer.setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK);
        }
    }

    private PLMediaPlayer.OnPreparedListener mOnPreparedListener = new PLMediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(PLMediaPlayer mp, int preparedTime) {
            Log.i(TAG, "On Prepared !");
            mMediaPlayer.start();
            mIsStopped = false;
        }
    };

    /**
     * 注册一个回调，以便在回放期间到达媒体文件的结束时调用。
     *
     * @param l 监听结果
     */
    public void setOnCompletionListener(PLMediaPlayer.OnCompletionListener l) {
        mOnCompletionListener = l;
    }

    private PLMediaPlayer.OnInfoListener mOnInfoListener = new PLMediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(PLMediaPlayer mp, int what, int extra) {
            Log.i(TAG, "OnInfo, what = " + what + ", extra = " + extra);
            switch (what) {
                case PLMediaPlayer.MEDIA_INFO_BUFFERING_START:
                    Log.e(TAG, "加载中");
                    break;
                case PLMediaPlayer.MEDIA_INFO_BUFFERING_END:
                case PLMediaPlayer.MEDIA_INFO_AUDIO_RENDERING_START:
                    Log.e(TAG, "加载完成");
                    break;
                default:
                    break;
            }
            return true;
        }
    };

    private PLMediaPlayer.OnBufferingUpdateListener mOnBufferingUpdateListener = new PLMediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(PLMediaPlayer mp, int percent) {
            Log.d(TAG, "onBufferingUpdate: " + percent + "%");
        }
    };

    /**
     * Listen the event of playing complete
     * For playing local file, it's called when reading the file EOF
     * For playing network stream, it's called when the buffered bytes played over
     * <p>
     * If setLooping(true) is called, the player will restart automatically
     * And ｀onCompletion｀ will not be called
     */
    private PLMediaPlayer.OnCompletionListener mOnCompletionListener = new PLMediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(PLMediaPlayer mp) {
            Log.d(TAG, "Play Completed !");

        }
    };

    private PLMediaPlayer.OnErrorListener mOnErrorListener = new PLMediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(PLMediaPlayer mp, int errorCode) {
            Log.e(TAG, "Error happened, errorCode = " + errorCode);
            switch (errorCode) {
                case PLMediaPlayer.ERROR_CODE_IO_ERROR:
                    /**
                     * SDK will do reconnecting automatically
                     */
                    Log.e(TAG, "IO Error !");
                    return false;
                case PLMediaPlayer.ERROR_CODE_OPEN_FAILED:
                    Log.e(TAG, "failed to open player !");
                    break;
                case PLMediaPlayer.ERROR_CODE_SEEK_FAILED:
                    Log.e(TAG, "failed to seek !");
                    break;
                default:
                    Log.e(TAG, "unknown error !");
                    break;
            }
            return true;
        }
    };

    /**
     * 在加载媒体文件时注册一个回调函数，并准备执行。
     *
     * @param l 监听结果
     */
    public void setOnPreparedListener(PLMediaPlayer.OnPreparedListener l) {
        mOnPreparedListener = l;
    }

    public void destroy() {
        release();
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.abandonAudioFocus(null);
    }

}
