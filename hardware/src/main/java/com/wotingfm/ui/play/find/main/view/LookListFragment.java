package com.wotingfm.ui.play.find.main.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.woting.commonplat.manager.PhoneMsgManager;
import com.woting.commonplat.widget.WaveLineView;
import com.wotingfm.R;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.service.PlayerService;
import com.wotingfm.common.utils.NetUtils;
import com.wotingfm.common.utils.T;
import com.wotingfm.ui.bean.MessageEvent;
import com.wotingfm.ui.play.find.main.adapter.MyAdapter;
import com.wotingfm.ui.play.find.main.presenter.LookListPresenter;
import com.wotingfm.ui.play.search.main.view.SearchFragment;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Field;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 发现功能界面
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class LookListFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.ivClose)// 界面关闭
            ImageView ivClose;
    @BindView(R.id.tabs)// 分类栏目
            TabLayout tabLayout;
    @BindView(R.id.ivVoice)// 语音搜索
            ImageView ivVoice;
    @BindView(R.id.viewPager)// fragment适配器
            ViewPager viewPager;
    @BindView(R.id.layout_main)
    RelativeLayout layout_main;
    @BindView(R.id.re_headView)
    RelativeLayout re_headView;

    private MyAdapter mAdapter;
    private Dialog voiceDialog;
    private LookListPresenter presenter;
    private TextView tvTitle, tvContent;
    private WaveLineView waveLineView;
    private LinearLayout lin_line_bg;
    private FrameLayout fragmentVideo;
    private View rootView;
    private int type = 1;// 默认是暂停状态

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.activity_look_list, container, false);
            rootView.setOnClickListener(this);
            ButterKnife.bind(this, rootView);
            setListener();
            presenter = new LookListPresenter(this);
            DDialog();
            EDialog();
        }
        return rootView;
    }

    // 设置监听
    private void setListener() {
        ivClose.setOnClickListener(this);
        ivVoice.setOnClickListener(this);
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
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                try {
                    //拿到tabLayout的mTabStrip属性
                    Field mTabStripField = tabLayout.getClass().getDeclaredField("mTabStrip");
                    mTabStripField.setAccessible(true);

                    LinearLayout mTabStrip = (LinearLayout) mTabStripField.get(tabLayout);
                    int dp10;
                    if (PhoneMsgManager.ScreenWidth >= 700) {
                        dp10 = dp2px(getContext(), 15);
                    } else {
                        dp10 = dp2px(getContext(), 10);
                    }

                    for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                        View tabView = mTabStrip.getChildAt(i);

                        //拿到tabView的mTextView属性
                        Field mTextViewField = tabView.getClass().getDeclaredField("mTextView");
                        mTextViewField.setAccessible(true);

                        TextView mTextView = (TextView) mTextViewField.get(tabView);

                        tabView.setPadding(0, 0, 0, 0);

                        //因为我想要的效果是   字多宽线就多宽，所以测量mTextView的宽度
                        int width = 0;
                        width = mTextView.getWidth();
                        if (width == 0) {
                            mTextView.measure(0, 0);
                            width = mTextView.getMeasuredWidth();
                        }

                        //设置tab左右间距为10dp  注意这里不能使用Padding 因为源码中线的宽度是根据 tabView的宽度来设置的
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                        params.width = width;
                        params.leftMargin = dp10;
                        params.rightMargin = dp10;
                        tabView.setLayoutParams(params);

                        tabView.invalidate();

                    }
                    re_headView.setVisibility(View.VISIBLE);

                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
//                setIndicator(tabs,20,20);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivClose:
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
        final View dialog = LayoutInflater.from(this.getActivity()).inflate(R.layout.dialog_video, null);
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
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        EditDialog.dialogShow();
                    }
                }, 300);
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
                        type = 1;
                        if (PlayerService.isPlaying(1)) {
                            type = 0;
                            EventBus.getDefault().post(new MessageEvent("PAUSE"));
                        }
                        tvTitle.setText("识别中...");
                        tvTitle.setTextColor(Color.parseColor("#cccccd"));
                        lin_line_bg.setVisibility(View.GONE);
                        tvContent.setVisibility(View.GONE);
                        waveLineView.setVisibility(View.VISIBLE);
                        waveLineView.startAnim();
                        presenter.setAudioVoice();
                        presenter.startVoice();
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
                        if (type == 0) {
                            EventBus.getDefault().post(new MessageEvent("START"));
                        }
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

    // 输入框选择框
    private void EDialog() {
        EditDialog.showSecurityCodeInputDialog(this.getActivity(), new EditDialog.DialogOnClickListener() {

            @Override
            public void searchClick(String str) {
                if (TextUtils.isEmpty(str)) {
                    T.getInstance().showToast("请输入搜索内容");
                    return;
                }
                openFragment(SearchFragment.newInstance(str, 0));
            }

            @Override
            public void backClick() {
                EditDialog.dialogDismiss();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        voiceDialog.show();
                    }
                }, 300);
            }
        });
    }

    public void search(final String str) {
        tvContent.setVisibility(View.VISIBLE);
        lin_line_bg.setVisibility(View.GONE);
        tvContent.setText("正在为您查找: " + str);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                voiceDialog.dismiss();
                tvContent.setText("");
                tvContent.setVisibility(View.GONE);
                lin_line_bg.setVisibility(View.VISIBLE);
                openFragment(SearchFragment.newInstance(str.trim(), 0));
            }
        }, 1000);
    }

    private void openFragment(Fragment fragment) {
        LookListActivity.open(fragment);
    }

    /**
     * dpתpx
     *
     * @param context
     * @param dp
     * @return
     */
    public static int dp2px(Context context, float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp, context.getResources().getDisplayMetrics());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (waveLineView != null) waveLineView.release();
        presenter.destroy();
        presenter = null;
    }

}
