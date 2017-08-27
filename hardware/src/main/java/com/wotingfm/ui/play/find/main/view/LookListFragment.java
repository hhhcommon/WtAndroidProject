package com.wotingfm.ui.play.find.main.view;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.woting.commonplat.widget.WaveLineView;
import com.wotingfm.R;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.utils.NetUtils;
import com.wotingfm.common.utils.T;
import com.wotingfm.ui.play.find.main.adapter.MyAdapter;
import com.wotingfm.ui.base.basefragment.BaseFragment;
import com.wotingfm.ui.bean.MessageEvent;
import com.wotingfm.ui.play.find.main.presenter.LookListPresenter;
import com.wotingfm.ui.play.search.main.SerchFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;

/**
 * 发现功能界面
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class LookListFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.ivClose)// 界面关闭
            ImageView ivClose;
    @BindView(R.id.tabs)// 分类栏目
            TabLayout tabLayout;
    @BindView(R.id.ivVoice)// 语音搜索
            ImageView ivVoice;
    @BindView(R.id.viewPager)// fragment适配器
            ViewPager viewPager;
    @BindView(R.id.ivBack)// 语音搜索返回
            ImageView ivBack;
    @BindView(R.id.et_searchlike)// 语音搜索输入框
            EditText etSearchlike;
    @BindView(R.id.relatLable)// 语音搜索框
            RelativeLayout relatLable;
    @BindView(R.id.layout_main)
    RelativeLayout layout_main;
    @BindView(R.id.tvSubmitSerch)// 开始搜索按钮
            TextView tvSubmitSerch;


    private MyAdapter mAdapter;
    private Dialog voiceDialog;
    private LookListPresenter presenter;
    private TextView tvTitle, tvContent;
    private WaveLineView waveLineView;
    private LinearLayout lin_line_bg;
    private FrameLayout fragmentVideo;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_look_list;
    }

    @Override
    public void initView() {
        setListener();
        presenter = new LookListPresenter(this);
        DDialog();
    }

    // 设置监听
    private void setListener() {
        ivClose.setOnClickListener(this);
        tvSubmitSerch.setOnClickListener(this);
        ivVoice.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        // 不是在我们点击EditText的时候触发，也不是在我们对EditText进行编辑时触发，而是在我们编辑完之后点击软键盘上的回车键才会触发
        etSearchlike.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int arg1, KeyEvent event) {
                if (arg1 == EditorInfo.IME_ACTION_SEARCH) {
                    String content = etSearchlike.getText().toString().trim();
                    if (!TextUtils.isEmpty(content)) {
                        presenter.closeKeyboard(etSearchlike);
                        openFragment(SerchFragment.newInstance(content, 0));
                        etSearchlike.setText("");
                    }
                }
                return false;
            }
        });
    }

    /**
     * 数据适配
     *
     * @param type
     * @param list
     */
    public void setData(List<String> type, List<Fragment> list) {
        mAdapter = new MyAdapter(getChildFragmentManager(), type, list);
        viewPager.setAdapter(mAdapter);
        viewPager.setOffscreenPageLimit(1);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSubmitSerch:// 开始搜索
                String content = etSearchlike.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    T.getInstance().showToast("请输入搜索内容");
                    return;
                }
                presenter.closeKeyboard(etSearchlike);
                openFragment(SerchFragment.newInstance(content, 0));
                etSearchlike.setText("");
                break;
            case R.id.ivBack:
                relatLable.setVisibility(View.GONE);
                presenter.closeKeyboard(etSearchlike);
                break;
            case R.id.ivClose:
                presenter.closeKeyboard(etSearchlike);
                GlobalStateConfig.mineFromType = 0;
                GlobalStateConfig.activityA = "A";
                EventBus.getDefault().post(new MessageEvent("one"));
                break;
            case R.id.ivVoice:
                voiceDialog.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        waveLineView.setVisibility(View.GONE);
                        lin_line_bg.setVisibility(View.VISIBLE);
                    }
                }, 100);

                break;
        }
    }

    // 选择框
    private void DDialog() {
        final View dialog = LayoutInflater.from(this.getActivity()).inflate(R.layout.video_dialog, null);
        fragmentVideo = (FrameLayout) dialog.findViewById(R.id.fragmentVideo);
        lin_line_bg = (LinearLayout) dialog.findViewById(R.id.lin_line_bg);
        waveLineView = (WaveLineView) dialog.findViewById(R.id.waveLineView);
        waveLineView.setZOrderOnTop(true);
        tvTitle = (TextView) dialog.findViewById(R.id.tvTitle);
        tvContent = (TextView) dialog.findViewById(R.id.tvContent);
        tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voiceDialog.dismiss();
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
                        tvTitle.setText("识别中...");
                        tvTitle.setTextColor(Color.parseColor("#cccccd"));
                        lin_line_bg.setVisibility(View.GONE);
                        tvContent.setVisibility(View.GONE);
                        waveLineView.setVisibility(View.VISIBLE);
                        waveLineView.startAnim();
                        presenter.setAudioVoice();
                        presenter.startVoice();
                        EventBus.getDefault().post(new MessageEvent("pause"));
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        presenter.stopVoice();
                        presenter.resumeAudioVoice();
                        waveLineView.stopAnim();
                        waveLineView.setVisibility(View.GONE);
                        lin_line_bg.setVisibility(View.VISIBLE);
                        tvTitle.setTextColor(Color.parseColor("#16181a"));
                        tvTitle.setText("点击切换文字搜索");
                        EventBus.getDefault().post(new MessageEvent("start"));
                        break;
                }
                return true;
            }
        });

        voiceDialog = new Dialog(this.getActivity(), R.style.MyDialogs);
        voiceDialog.setContentView(dialog);
        voiceDialog.setCanceledOnTouchOutside(true);

        DisplayMetrics dm = new DisplayMetrics();
        this.getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        ViewGroup.LayoutParams params = dialog.getLayoutParams();
        params.width = screenWidth;
        dialog.setLayoutParams(params);

        Window window = voiceDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.inOutStyle);
        window.setBackgroundDrawableResource(R.color.transparent_40_black);
    }

    /**
     * EditText获取焦点并显示软键盘
     */
    public void showSoftInputFromWindow() {
        etSearchlike.setFocusable(true);
        etSearchlike.setFocusableInTouchMode(true);
        etSearchlike.requestFocus();
        relatLable.setVisibility(View.VISIBLE);
        presenter.openKeyboard(etSearchlike);
    }

    public void search(final String str) {
        tvContent.setVisibility(View.VISIBLE);
        lin_line_bg.setVisibility(View.GONE);
        tvContent.setText("正在为您查找: " + str);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                voiceDialog.dismiss();
                etSearchlike.setText("");
                tvContent.setText("");
                tvContent.setVisibility(View.GONE);
                lin_line_bg.setVisibility(View.VISIBLE);
                presenter.closeKeyboard(etSearchlike);
                openFragment(SerchFragment.newInstance(str.trim(), 0));
            }
        }, 1000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (waveLineView != null) waveLineView.release();
        presenter.destroy();
        presenter = null;
    }

}
