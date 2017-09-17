//package com.wotingfm.common.player.media;
//
//import android.content.Context;
//import android.media.MediaPlayer;
//import android.util.Log;
//
//import java.io.IOException;
//
///**
// * 作者：xinLong on 2017/5/23 14:43
// * 邮箱：645700751@qq.com
// */
//public class MyMediaPlayer {
//    private Context mAppContext;
//    private  android.media.MediaPlayer mediaPlayer;
//    private android.media.MediaPlayer.OnCompletionListener OnCompletionListener;
//    private android.media.MediaPlayer.OnPreparedListener OnPreparedListener;
//    /**
//     * 代码构造函数
//     *
//     * @param context
//     */
//    public MyMediaPlayer(Context context) {
//        initVideo(context);
//    }
//
//    private void initVideo(Context context) {
//        mediaPlayer = new android.media.MediaPlayer();
//        mediaPlayer.setOnPreparedListener(mPreparedListener);
//        mediaPlayer.setOnCompletionListener(mCompletionListener);
//        mediaPlayer.setOnErrorListener(mErrorListener);
//    }
//
//    android.media.MediaPlayer.OnPreparedListener mPreparedListener = new android.media.MediaPlayer.OnPreparedListener() {
//        public void onPrepared(android.media.MediaPlayer mp) {
//            Log.e("media", "onPrepared");
//            if (OnPreparedListener != null) {
//                OnPreparedListener.onPrepared(mp);
//            }
//            mediaPlayer.start();
//
//        }
//    };
//
//    android.media.MediaPlayer.OnErrorListener mErrorListener = new android.media.MediaPlayer.OnErrorListener() {
//
//        @Override
//        public boolean onError(MediaPlayer mp, int what, int extra) {
//            return false;
//        }
//    };
//    android.media.MediaPlayer.OnCompletionListener mCompletionListener =
//            new android.media.MediaPlayer.OnCompletionListener() {
//                public void onCompletion(android.media.MediaPlayer mp) {
//                    Log.e("media", "onCompletion");
//                    if (OnCompletionListener != null) {
//                        OnCompletionListener.onCompletion(mp);
//                    }
//                }
//            };
//
//    /**
//     * 设置播放地址
//     *
//     * @param path 播放地址
//     */
//    public void setVideoPath(String path) {
//        Log.e("此时播放地址===", path + "");
//        mediaPlayer.reset();
//        play(path);
//    }
//
//    /**
//     * 是否正在播放
//     *
//     * @return
//     */
//    public boolean isPlaying() {
//        return  mediaPlayer.isPlaying();
//    }
//
//    /**
//     * 播放列表
//     */
//    private  void play(String path) {
//        try {
//            // 设置文件路径
//            mediaPlayer.setDataSource(path);
//            // 异步准备
//            mediaPlayer.prepareAsync();
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
//        } catch (IllegalStateException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void destroy(){
//        mediaPlayer.stop();
//        mediaPlayer.release();
//        mediaPlayer = null;
//    }
//
//    public void pause() {
//        mediaPlayer.pause();
//    }
//
//    public void start() {
//        mediaPlayer.start();
//    }
//
//    public int getCurrentPosition() {
//       return mediaPlayer.getCurrentPosition();
//    }
//
//    public int getDuration() {
//        return mediaPlayer.getDuration();
//    }
//
//    public void onStop() {
//        mediaPlayer.stop();
//    }
//
//    public void seekTo(int progress) {
//        mediaPlayer.seekTo(progress);
//    }
//
//    /**
//     * 注册一个回调，以便在回放期间到达媒体文件的结束时调用。
//     *
//     * @param l 监听结果
//     */
//    public void setOnCompletionListener(android.media.MediaPlayer.OnCompletionListener l) {
//        OnCompletionListener = l;
//    }
//
//    /**
//     * 在加载媒体文件时注册一个回调函数，并准备执行。
//     *
//     * @param l 监听结果
//     */
//    public void setOnPreparedListener(android.media.MediaPlayer.OnPreparedListener l) {
//        OnPreparedListener = l;
//    }
//}
