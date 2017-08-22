package com.woting.common.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.woting.commonplat.manager.VoiceRecognizer;
import com.woting.R;
import com.woting.common.constant.BroadcastConstants;
import com.woting.common.utils.NetUtils;

//搜索dialog
public class VideoDialog extends Dialog {

    private Activity activity;
    private VoiceRecognizer mVoiceRecognizer;
    private FrameLayout fragmentVideo;
    private TextView tvTitle, tvContent;
    private int stepVolume;
    private AudioManager audioMgr;
    protected int curVolume;

    public VideoDialog(@NonNull Activity context) {
        super(context, R.style.BottomDialog);
        this.activity = context;
        setContentView(R.layout.video_dialog);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.BOTTOM);

        setCanceledOnTouchOutside(true);

        mVoiceRecognizer = VoiceRecognizer.getInstance(context, BroadcastConstants.SEARCH_VOICE);// 初始化语音搜索
        fragmentVideo = (FrameLayout) findViewById(R.id.fragmentVideo);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvContent = (TextView) findViewById(R.id.tvContent);
        audioMgr = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = audioMgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);// 获取最大音乐音量
        stepVolume = maxVolume / 100;// 每次调整的音量大概为最大音量的1/100
        fragmentVideo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!NetUtils.isNetworkAvailable(activity)) return true;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        curVolume = audioMgr.getStreamVolume(AudioManager.STREAM_MUSIC);
                        audioMgr.setStreamVolume(AudioManager.STREAM_MUSIC, stepVolume, AudioManager.FLAG_PLAY_SOUND);
                        mVoiceRecognizer.startListen();
                        tvTitle.setText("识别中");
                        tvTitle.setTextColor(Color.parseColor("#cccccd"));
                        break;
                    case MotionEvent.ACTION_UP:
                        audioMgr.setStreamVolume(AudioManager.STREAM_MUSIC, curVolume, AudioManager.FLAG_PLAY_SOUND);
                        mVoiceRecognizer.stopListen();
                        tvTitle.setTextColor(Color.parseColor("#16181a"));
                        tvTitle.setText("请按住讲话");
                        break;
                }
                return true;
            }
        });

    }


}
