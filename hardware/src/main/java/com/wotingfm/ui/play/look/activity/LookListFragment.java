package com.wotingfm.ui.play.look.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.woting.commonplat.manager.VoiceRecognizer;
import com.woting.commonplat.utils.KeyboardChangeListener;
import com.wotingfm.R;
import com.wotingfm.common.adapter.MyAdapter;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.bean.ChannelsBean;
import com.wotingfm.common.bean.MessageEvent;
import com.wotingfm.common.bean.RadioInfo;
import com.wotingfm.common.bean.SinglesBase;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.constant.BroadcastConstants;
import com.wotingfm.common.utils.NetUtils;
import com.wotingfm.common.utils.T;
import com.wotingfm.ui.base.baseactivity.AppManager;
import com.wotingfm.ui.base.baseactivity.NoTitleBarBaseActivity;
import com.wotingfm.ui.base.basefragment.BaseFragment;
import com.wotingfm.ui.play.look.activity.serch.SerchFragment;
import com.wotingfm.ui.play.look.fragment.ClassificationFragment;
import com.wotingfm.ui.play.look.fragment.LiveFragment;
import com.wotingfm.ui.play.look.fragment.RadioStationFragment;
import com.wotingfm.ui.play.look.fragment.SelectedFragment;
import com.wotingfm.ui.play.radio.fragment.RadioInfoTodayFragment;
import com.wotingfm.ui.test.PlayerActivity;
import com.wotingfm.ui.test.PlayerFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by amine on 2017/6/21.
 * 发现列表
 */

public class LookListFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.ivClose)
    ImageView ivClose;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.ivVoice)
    ImageView ivVoice;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.et_searchlike)
    EditText etSearchlike;
    @BindView(R.id.relatLable)
    RelativeLayout relatLable;
    @BindView(R.id.tvSubmitSerch)
    TextView tvSubmitSerch;

    public static LookListFragment newInstance(int code) {
        LookListFragment fragment = new LookListFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("code", code);
        fragment.setArguments(bundle);
        return fragment;
    }

    private List<Fragment> mFragment = new ArrayList<>();
    private MyAdapter mAdapter;


    /**
     * EditText获取焦点并显示软键盘
     */
    public void showSoftInputFromWindow() {
        etSearchlike.setFocusable(true);
        etSearchlike.setFocusableInTouchMode(true);
        etSearchlike.requestFocus();
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public void initView() {
        new KeyboardChangeListener(getActivity()).setKeyBoardListener(new KeyboardChangeListener.KeyBoardListener() {
            @Override
            public void onKeyboardChange(boolean isShow, int keyboardHeight) {
                relatLable.setVisibility(isShow == true ? View.VISIBLE : View.GONE);
            }
        });
        ivClose.setOnClickListener(this);
        tvSubmitSerch.setOnClickListener(this);
        ivVoice.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        List<String> type = new ArrayList<>();
        type.add("精选");
        type.add("分类");
        type.add("电台");
        type.add("直播");
        mFragment.clear();
        mFragment.add(SelectedFragment.newInstance());
        mFragment.add(ClassificationFragment.newInstance());
        mFragment.add(RadioStationFragment.newInstance());
        mFragment.add(LiveFragment.newInstance());
        mAdapter = new MyAdapter(getChildFragmentManager(), type, mFragment);
        viewPager.setAdapter(mAdapter);
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);

        mVoiceRecognizer = VoiceRecognizer.getInstance(getActivity(), com.woting.commonplat.constant.BroadcastConstants.SEARCHVOICE);// 初始化语音搜索
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(com.woting.commonplat.constant.BroadcastConstants.SEARCHVOICE);
        getActivity().registerReceiver(mBroadcastReceiver, mFilter);
        etSearchlike.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int arg1, KeyEvent event) {
                if (arg1 == EditorInfo.IME_ACTION_SEARCH) {
                    String content = etSearchlike.getText().toString().trim();
                    if (!TextUtils.isEmpty(content)) {
                        openFragment(SerchFragment.newInstance(content,0));
                        SerchCode = viewPager.getCurrentItem();
                        etSearchlike.setText("");
                    }
                }
                return false;
            }
        });
        Bundle bundle = getArguments();
        if (bundle != null) {
            viewPager.setCurrentItem(bundle.getInt("code", 0));
        }
    }

    // 广播接收器
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            if (intent.getAction().equals(com.woting.commonplat.constant.BroadcastConstants.SEARCHVOICE)) {
                final String str = intent.getStringExtra("VoiceContent");
                tvContent.setText("正在为您查找: " + str);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (videoDialog != null) videoDialog.dismiss();
                        etSearchlike.setText("");
                        SerchCode = viewPager.getCurrentItem();
                        openFragment(SerchFragment.newInstance(str.trim(),0));
                    }
                }, 1000);
                if (NetUtils.isNetworkAvailable(context)) {
                    if (!str.trim().equals("")) {
                        tvContent.setText(str.trim());
                        tvContent.setVisibility(View.VISIBLE);

                    }
                }
            }
        }
    };
    private VideoDialog videoDialog;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSubmitSerch:
                String content = etSearchlike.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    T.getInstance().showToast("请输入搜索内容");
                    return;
                }
                hideSoftKeyboard();
                openFragment(SerchFragment.newInstance(content,0));
                SerchCode = viewPager.getCurrentItem();
                etSearchlike.setText("");
                break;
            case R.id.ivBack:
            case R.id.ivClose:
                closeFragment();
                GlobalStateConfig.mineFromType = 0;
                GlobalStateConfig.activityA = "A";
                EventBus.getDefault().post(new MessageEvent("one"));
                break;
            case R.id.ivVoice:
                if (videoDialog == null) {
                    videoDialog = new VideoDialog(getActivity());
                }
                videoDialog.show();
                break;
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_look_list;
    }

    private TextView tvTitle, tvContent;
    private VoiceRecognizer mVoiceRecognizer;

    public class VideoDialog extends Dialog {

        private FrameLayout fragmentVideo;
        private int stepVolume;
        private AudioManager audioMgr;
        protected int curVolume;

        public VideoDialog(@NonNull Activity context) {
            super(context, R.style.BottomDialog);
            setContentView(R.layout.video_dialog);
            getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            getWindow().setGravity(Gravity.BOTTOM);

            setCanceledOnTouchOutside(true);

            fragmentVideo = (FrameLayout) findViewById(R.id.fragmentVideo);
            tvTitle = (TextView) findViewById(R.id.tvTitle);
            tvContent = (TextView) findViewById(R.id.tvContent);
            audioMgr = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            int maxVolume = audioMgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);// 获取最大音乐音量
            stepVolume = maxVolume / 100;// 每次调整的音量大概为最大音量的1/100
            tvTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    showSoftInputFromWindow();
                }
            });
            fragmentVideo.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (!NetUtils.isNetworkAvailable(BSApplication.getInstance())) {
                        T.getInstance().showToast("网络异常");
                        return true;
                    }
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            curVolume = audioMgr.getStreamVolume(AudioManager.STREAM_MUSIC);
                            audioMgr.setStreamVolume(AudioManager.STREAM_MUSIC, stepVolume, AudioManager.FLAG_PLAY_SOUND);
                            mVoiceRecognizer.startListen();
                            tvTitle.setText("识别中...");
                            EventBus.getDefault().postSticky("pause");
                            tvTitle.setTextColor(Color.parseColor("#cccccd"));
                            break;
                        case MotionEvent.ACTION_UP:
                            audioMgr.setStreamVolume(AudioManager.STREAM_MUSIC, curVolume, AudioManager.FLAG_PLAY_SOUND);
                            mVoiceRecognizer.stopListen();
                            tvTitle.setTextColor(Color.parseColor("#16181a"));
                            tvTitle.setText("点击切换文字搜索");
                            EventBus.getDefault().postSticky("start");
                            break;
                    }
                    return true;
                }
            });

        }

    }

/*    @Override
    public void onBackPressed() {
        super.onBackPressed();
        GlobalStateConfig.mineFromType = 0;
        GlobalStateConfig.activityA = "A";
        AppManager.getAppManager().finishActivity(this);
        EventBus.getDefault().post(new MessageEvent("one"));
    }*/

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mBroadcastReceiver != null && getActivity() != null)
            getActivity().unregisterReceiver(mBroadcastReceiver
            );
    }
}
