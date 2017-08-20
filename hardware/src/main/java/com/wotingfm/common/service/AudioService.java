package com.wotingfm.common.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import com.wotingfm.R;

/**
 * 呼叫流程背景音乐
 * 作者：xinLong on 2017/8/20 18:26
 * 邮箱：645700751@qq.com
 */
public class AudioService extends Service  {
    // 实例化MediaPlayer对象
    MediaPlayer player;
    private final IBinder binder = new AudioBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void onCreate() {
        super.onCreate();
        // 从raw文件夹中获取一个应用自带的mp3文件
        player = MediaPlayer.create(this, R.raw.talkno);
        player.setLooping(true);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        if (!player.isPlaying()) {
            new MusicPlayThread().start();
        }
        else player.isPlaying();
        return START_STICKY;
    }

    public void onDestroy() {
        super.onDestroy();
        if (player.isPlaying()) {
            player.stop();
        }
        player.release();
    }

    // 为了和Activity交互，我们需要定义一个Binder对象
    public class AudioBinder extends Binder {
        // 返回Service对象
        public AudioService getService() {
            return AudioService.this;
        }
    }

//    //获取系统默认铃声的Uri
//    private Uri getSystemDefaultRingtoneUri() {
//        return RingtoneManager.getActualDefaultRingtoneUri(activity, RingtoneManager.TYPE_RINGTONE);
//    }

    private class MusicPlayThread extends Thread {
        public void run() {
            if (!player.isPlaying()) {
                player.start();
            }
        }
    }
}