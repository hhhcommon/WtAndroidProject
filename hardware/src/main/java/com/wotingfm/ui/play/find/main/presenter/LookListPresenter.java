package com.wotingfm.ui.play.find.main.presenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.woting.commonplat.manager.VoiceRecognizer;
import com.wotingfm.ui.play.find.classification.main.view.ClassificationFragment;
import com.wotingfm.ui.play.find.live.LiveFragment;
import com.wotingfm.ui.play.find.main.view.LookListFragment;
import com.wotingfm.ui.play.find.radio.main.RadioStationFragment;
import com.wotingfm.ui.play.find.selected.view.SelectedFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class LookListPresenter {

    private LookListFragment activity;
    private VoiceRecognizer mVoiceRecognizer;
    private AudioManager audioMgr;
    private int stepVolume;// 调整后音量
    private int curVolume;// 当前音量
    private InputMethodManager imm;

    public LookListPresenter(LookListFragment activity) {
        this.activity = activity;
        setKeyboard();
        setVoiceManager();
        setAudioManager();
        setReceiver();
        getData();
    }

    // 初始化软键盘
    private void setKeyboard(){
        imm = (InputMethodManager) activity.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    /**
     * 打开软键盘
     * @param view
     */
    public void openKeyboard(final View view){
        Timer timer = new Timer(); //设置定时器
        timer.schedule(new TimerTask() {
            @Override
            public void run() { //弹出软键盘的代码
                imm.showSoftInput(view, InputMethodManager.RESULT_SHOWN);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        }, 100);
    }

    /**
     * 关闭软键盘
     * @param view
     */
    public void closeKeyboard(View view) {
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    // 初始化语音识别
    private void setVoiceManager(){
        mVoiceRecognizer = VoiceRecognizer.getInstance(activity.getActivity(), com.woting.commonplat.constant.BroadcastConstants.SEARCHVOICE);// 初始化语音搜索
    }

    /**
     * 开始语音识别
     */
    public void startVoice(){
        mVoiceRecognizer.startListen();
    }

    /**
     * 停止语音识别
     */
    public void stopVoice(){
        mVoiceRecognizer.stopListen();
    }

    // 组装数据
    private void getData() {
        List<String> type = new ArrayList<>();
        type.add("精选");
        type.add("分类");
        type.add("电台");
        type.add("直播");

        List<Fragment> mFragment = new ArrayList<>();
        mFragment.add(SelectedFragment.newInstance());
        mFragment.add(ClassificationFragment.newInstance());
        mFragment.add(RadioStationFragment.newInstance());
        mFragment.add(LiveFragment.newInstance());
        activity.setData(type, mFragment);// 设置数据
    }

    private void setAudioManager(){
        audioMgr = (AudioManager) activity.getActivity().getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = audioMgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);// 获取最大音乐音量
        stepVolume = maxVolume / 100;// 每次调整的音量大概为最大音量的1/100
    }

    /**
     * 设置静音
     */
    public void setAudioVoice(){
        curVolume = audioMgr.getStreamVolume(AudioManager.STREAM_MUSIC);
        audioMgr.setStreamVolume(AudioManager.STREAM_MUSIC, stepVolume, AudioManager.FLAG_PLAY_SOUND);
    }

    /**
     * 恢复声音
     */
    public void resumeAudioVoice(){
        audioMgr.setStreamVolume(AudioManager.STREAM_MUSIC, curVolume, AudioManager.FLAG_PLAY_SOUND);
    }

    private void setReceiver() {
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(com.woting.commonplat.constant.BroadcastConstants.SEARCHVOICE);
        activity.getActivity().registerReceiver(mBroadcastReceiver, mFilter);
    }

    // 广播接收器
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            if (intent.getAction().equals(com.woting.commonplat.constant.BroadcastConstants.SEARCHVOICE)) {
                final String str = intent.getStringExtra("VoiceContent");
                activity.search(str);
            }
        }
    };

    /**
     * 数据销毁
     */
    public void destroy() {
        if (mBroadcastReceiver != null )activity.getActivity().unregisterReceiver(mBroadcastReceiver);
        activity=null;
    }


}
