package com.wotingfm.ui.play.look.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Rect;
import android.media.AudioManager;
import android.os.Build;
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
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.woting.commonplat.manager.VoiceRecognizer;
import com.woting.commonplat.utils.SequenceUUID;
import com.wotingfm.R;
import com.wotingfm.common.adapter.MyAdapter;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.bean.MessageEvent;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.utils.NetUtils;
import com.wotingfm.common.utils.T;
import com.wotingfm.ui.base.baseactivity.NoTitleBarBaseActivity;
import com.wotingfm.ui.base.basefragment.BaseFragment;
import com.wotingfm.ui.play.look.activity.serch.SerchFragment;
import com.wotingfm.ui.play.look.fragment.ClassificationFragment;
import com.wotingfm.ui.play.look.fragment.LiveFragment;
import com.wotingfm.ui.play.look.fragment.RadioStationFragment;
import com.wotingfm.ui.play.look.fragment.SelectedFragment;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.wotingfm.R.layout.activity_look_list;

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
    @BindView(R.id.layout_main)
    RelativeLayout layout_main;
    @BindView(R.id.tvSubmitSerch)
    TextView tvSubmitSerch;
    public static LookListFragment context;

    private List<Fragment> mFragment = new ArrayList<>();
    private MyAdapter mAdapter;


    /**
     * EditText获取焦点并显示软键盘
     */
    public void showSoftInputFromWindow() {
        etSearchlike.setFocusable(true);
        etSearchlike.setFocusableInTouchMode(true);
        etSearchlike.requestFocus();
        //   InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(-30, InputMethodManager.HIDE_NOT_ALWAYS);
        isOne = false;
        relatLable.setVisibility(View.VISIBLE);
    }

    // 状态栏的高度
    private int statusBarHeight;
    // 软键盘的高度
    private int keyboardHeight;
    // 软键盘的显示状态
    private boolean isShowKeyboard;
    private boolean isOne = true;
    private ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {

        @Override
        public void onGlobalLayout() {
            // 应用可以显示的区域。此处包括应用占用的区域，
            // 以及ActionBar和状态栏，但不含设备底部的虚拟按键。
            Rect r = new Rect();
            layout_main.getWindowVisibleDisplayFrame(r);

            // 屏幕高度。这个高度不含虚拟按键的高度
            int screenHeight = layout_main.getRootView().getHeight();

            int heightDiff = screenHeight - (r.bottom - r.top);

            // 在不显示软键盘时，heightDiff等于状态栏的高度
            // 在显示软键盘时，heightDiff会变大，等于软键盘加状态栏的高度。
            // 所以heightDiff大于状态栏高度时表示软键盘出现了，
            // 这时可算出软键盘的高度，即heightDiff减去状态栏的高度
            if (keyboardHeight == 0 && heightDiff > statusBarHeight) {
                keyboardHeight = heightDiff - statusBarHeight;
            }

            if (isShowKeyboard) {
                // 如果软键盘是弹出的状态，并且heightDiff小于等于状态栏高度，
                // 说明这时软键盘已经收起
                if (heightDiff <= statusBarHeight) {
                    isShowKeyboard = false;
                    relatLable.setVisibility(View.GONE);
                }
            } else {
                // 如果软键盘是收起的状态，并且heightDiff大于状态栏高度，
                // 说明这时软键盘已经弹出
                if (heightDiff > statusBarHeight && isOne == false) {
                    isShowKeyboard = true;
                    relatLable.setVisibility(View.VISIBLE);
                }
            }
        }
    };

    @Override
    public void initView() {
        context = this;
        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        statusBarHeight = getStatusBarHeight(BSApplication.getInstance());

        layout_main.getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListener);
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
        // viewPager.setOffscreenPageLimit(1);
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
                        closeKeyboard(etSearchlike);
                        openFragment(SerchFragment.newInstance(content, 0));
                        //  openFragment(SerchFragment.newInstance(content, 0));
                        etSearchlike.setText("");
                    }
                }
                return false;
            }
        });
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
                        closeKeyboard(etSearchlike);
                        openFragment(SerchFragment.newInstance(str.trim(), 0));
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
                closeKeyboard(etSearchlike);
                openFragment(SerchFragment.newInstance(content, 0));
                etSearchlike.setText("");
                break;
            case R.id.ivBack:
            case R.id.ivClose:
                closeKeyboard(etSearchlike);
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

    private InputMethodManager imm;

    public void closeKeyboard(View view) {

        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
                            EventBus.getDefault().post(new MessageEvent("pause"));
                            tvTitle.setTextColor(Color.parseColor("#cccccd"));
                            break;
                        case MotionEvent.ACTION_UP:
                            audioMgr.setStreamVolume(AudioManager.STREAM_MUSIC, curVolume, AudioManager.FLAG_PLAY_SOUND);
                            mVoiceRecognizer.stopListen();
                            tvTitle.setTextColor(Color.parseColor("#16181a"));
                            tvTitle.setText("点击切换文字搜索");
                            EventBus.getDefault().post(new MessageEvent("start"));
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
// 获取状态栏高度
    public static int getStatusBarHeight(Context context) {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return context.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (layout_main != null)
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                layout_main.getViewTreeObserver().removeGlobalOnLayoutListener(globalLayoutListener);
            } else {
                layout_main.getViewTreeObserver().removeOnGlobalLayoutListener(globalLayoutListener);
            }
        if (mBroadcastReceiver != null && getActivity() != null)
            getActivity().unregisterReceiver(mBroadcastReceiver
            );
    }

}
