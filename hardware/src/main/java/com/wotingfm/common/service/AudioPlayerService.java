//package com.wotingfm.common.service;
//
//import android.app.Service;
//import android.content.Intent;
//import android.media.MediaPlayer;
//import android.os.IBinder;
//import android.util.Log;
//
//import com.wotingfm.ui.bean.MessageEvent;
//
//import org.greenrobot.eventbus.EventBus;
//
//import java.io.IOException;
//
///**
// * 播放服务
// * 作者：xinlong on 2017/7/13 23:57
// * 邮箱：645700751@qq.com
// */
//public class AudioPlayerService extends Service {
//    private static AudioPlayerService context;
//    private static MediaPlayer mediaPlayer;
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        context = this;
//        init();
//    }
//
//    public void init() {
//        // 实例化mediaPlayer
//        mediaPlayer = new MediaPlayer();
//        mediaPlayer.setOnPreparedListener(mPreparedListener);
//        mediaPlayer.setOnCompletionListener(mCompletionListener);
//    }
//
//    public static void reset() {
//        mediaPlayer.reset();
//    }
//
//    public static void pause() {
//        if (mediaPlayer.isPlaying()) {
//            mediaPlayer.pause();
//        } else {
//            mediaPlayer.start();
//        }
//    }
//
//    /**
//     * 播放列表
//     */
//    private static void play(String path) {
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
//    MediaPlayer.OnPreparedListener mPreparedListener = new MediaPlayer.OnPreparedListener() {
//        public void onPrepared(MediaPlayer mp) {
//            Log.e("监听", "播放准备好");
//            EventBus.getDefault().post(new MessageEvent(2001));
//            mediaPlayer.start();
//        }
//    };
//
//    private MediaPlayer.OnCompletionListener mCompletionListener =
//            new MediaPlayer.OnCompletionListener() {
//                public void onCompletion(MediaPlayer mp) {
//                    Log.e("media", "onCompletion");
////                    if (mOnCompletionListener != null) {
////                        mOnCompletionListener.onCompletion(mp);
////                    }
//                }
//            };
//
//    public static boolean isPlaying() {
//        return mediaPlayer.isPlaying();
//    }
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        mediaPlayer.stop();
//        mediaPlayer.release();
//        mediaPlayer = null;
//    }
//
//    public static void start(int playerType) {
//        mediaPlayer.start();
//    }
//
//    public static void playPause() {
//        mediaPlayer.pause();
//    }
//
//    public static int getCurrentPosition(int playerType) {
//        return mediaPlayer.getCurrentPosition();
//    }
//
//    public static int getDuration(int playerType) {
//        return mediaPlayer.getDuration();
//    }
//
//    public static void play(int playerType, String url) {
//        reset();
//        play(url);
//    }
//}
