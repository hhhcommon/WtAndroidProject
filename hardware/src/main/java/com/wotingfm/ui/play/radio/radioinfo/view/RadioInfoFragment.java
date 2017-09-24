package com.wotingfm.ui.play.radio.radioinfo.view;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.woting.commonplat.utils.DementionUtil;
import com.woting.commonplat.widget.LoadFrameLayout;
import com.wotingfm.R;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.common.utils.DialogUtils;
import com.wotingfm.common.utils.GlideUtils;
import com.wotingfm.common.view.myscrollview.ObservableScrollView;
import com.wotingfm.common.view.viewpager.CustomViewPager;
import com.wotingfm.ui.base.baseadapter.MyFragmentPagerAdapter;
import com.wotingfm.ui.base.basefragment.BaseFragment;
import com.wotingfm.ui.base.baseinterface.ScrollViewListener;
import com.wotingfm.ui.bean.RadioInfo;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;
import com.wotingfm.ui.mine.main.MineActivity;
import com.wotingfm.ui.play.find.main.view.LookListActivity;
import com.wotingfm.ui.play.main.PlayerActivity;
import com.wotingfm.ui.play.radio.fragment.view.RadioInfoTodayFragment;
import com.wotingfm.ui.play.radio.fragment.view.RadioInfoTomorrowFragment;
import com.wotingfm.ui.play.radio.fragment.view.RadioInfoYesterdayFragment;
import com.wotingfm.ui.play.radio.radioinfo.presenter.RadioInfoPresenter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.BlurTransformation;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 电台详情
 */

public class RadioInfoFragment extends BaseFragment implements View.OnClickListener, ScrollViewListener {
    @BindView(R.id.ivPhotoBg)
    ImageView ivPhotoBg;
    @BindView(R.id.ivPhoto)
    ImageView ivPhoto;
//    @BindView(R.id.tvFollow)
//    TextView tvFollow;
    @BindView(R.id.tvYesterday)
    TextView tvYesterday;
    @BindView(R.id.tvToday)
    TextView tvToday;
    @BindView(R.id.tvTomorrow)
    TextView tvTomorrow;
    @BindView(R.id.mObservableScrollView)
    ObservableScrollView mObservableScrollView;
    @BindView(R.id.loadLayout)
    LoadFrameLayout loadLayout;
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.mRelativeLayout)
    RelativeLayout mRelativeLayout;
    @BindView(R.id.viewpager)
    CustomViewPager mPager;

    private View rootView;
    private int height;
    private RadioInfoPresenter presenter;
    private Dialog dialog;

    public static RadioInfoFragment newInstance(String rId, String title) {
        RadioInfoFragment fragment = new RadioInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("rId", rId);
        bundle.putSerializable("title", title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.activity_radio_info, container, false);
            rootView.setOnClickListener(this);
            ButterKnife.bind(this, rootView);
            inItView();
            presenter = new RadioInfoPresenter(this);
        }
        return rootView;
    }

    public void inItView() {
        height = DementionUtil.dip2px(getActivity(), 210);
        loadLayout.findViewById(R.id.btnTryAgain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadLayout.showLoadingView();
                refresh();
            }
        });
        loadLayout.showLoadingView();
        ivBack.setOnClickListener(this);
//        tvFollow.setOnClickListener(this);
        tvYesterday.setOnClickListener(this);
        tvToday.setOnClickListener(this);
        tvTomorrow.setOnClickListener(this);
        mObservableScrollView.setScrollViewListener(this);
    }


    public void initFragment(RadioInfo.DataBean s) {
        ArrayList<Fragment> fragmentList = new ArrayList<>();
        Fragment radioYesterdayFragment = RadioInfoYesterdayFragment.newInstance(s);
        Fragment radioTodayFragment = RadioInfoTodayFragment.newInstance(s);
        Fragment radioTomorrowFragment = RadioInfoTomorrowFragment.newInstance(s);

        fragmentList.add(radioYesterdayFragment);
        fragmentList.add(radioTodayFragment);
        fragmentList.add(radioTomorrowFragment);
        mPager.setAdapter(new MyFragmentPagerAdapter(getChildFragmentManager(), fragmentList));
        mPager.setOnPageChangeListener(new MyOnPageChangeListener());// 页面变化时的监听器
        setTextColor(1);
    }

    // ViewPager 监听事件
    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageSelected(int arg0) {
            setTextColor(arg0);
        }
    }

    private void setResultData(final RadioInfo.DataBean s) {
        Glide.with(getActivity())
                .load(s.channel.image_url)
                .placeholder(R.mipmap.oval_defut_other)
                .error(R.mipmap.oval_defut_other)
                .crossFade(1000)
                .bitmapTransform(new BlurTransformation(getActivity(), 23, 4))  // “23”：设置模糊度(在0.0到25.0之间)，默认”25";"4":图片缩放比例,默认“1”。
                .into(ivPhotoBg);
        String url=null;
        try {
            url=s.channel.image_url;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (url != null && !url.equals("") && url.startsWith("http")) {
            GlideUtils.loadImageViewRoundCorners(url, ivPhoto, 150, 150);
        } else {
            GlideUtils.loadImageViewRoundCorners(R.mipmap.icon_avatar_d, ivPhoto, 60, 60);
        }
//        if (s.channel.had_subscribed == true) {
//            tvFollow.setText("已订阅(" + s.channel.subscribed_count + ")");
//            tvFollow.setTextColor(Color.parseColor("#50ffffff"));
//            Drawable rightDrawable = getResources().getDrawable(R.mipmap.icon_subscription_s);
//            rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(), rightDrawable.getMinimumHeight());
//            tvFollow.setCompoundDrawables(rightDrawable, null, null, null);
//        } else {
//            tvFollow.setText("订阅(" + s.channel.subscribed_count + ")");
//            tvFollow.setTextColor(Color.parseColor("#ffffff"));
//            Drawable rightDrawable = getResources().getDrawable(R.mipmap.icon_subscription_n);
//            rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(), rightDrawable.getMinimumHeight());
//            tvFollow.setCompoundDrawables(rightDrawable, null, null, null);
//        }

    }


    /**
     * @param code 切换的下标
     */
    private void setTextColor(int code) {
        tvYesterday.setTextColor(Color.parseColor("#16181a"));
        tvToday.setTextColor(Color.parseColor("#16181a"));
        tvTomorrow.setTextColor(Color.parseColor("#16181a"));
        switch (code) {
            case 0:
                tvYesterday.setTextColor(Color.parseColor("#fd8548"));
                break;
            case 1:
                tvToday.setTextColor(Color.parseColor("#fd8548"));
                break;
            case 2:
                tvTomorrow.setTextColor(Color.parseColor("#fd8548"));
                break;
        }
        mPager.setCurrentItem(code);
    }

    public void refresh() {
        RetrofitUtils.getInstance().getRadioInfo(presenter.getRid())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<RadioInfo.DataBean>() {
                    @Override
                    public void call(RadioInfo.DataBean s) {
                        initFragment(s);
                        setResultData(s);
                        loadLayout.showContentView();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        loadLayout.showErrorView();
                        throwable.printStackTrace();
                    }
                });
    }

//    private void subscriptionsRadio() {
//        dialogShow();
//        RetrofitUtils.getInstance().subscriptionsRadio(presenter.getRid())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<BaseResult>() {
//                    @Override
//                    public void call(BaseResult baseResult) {
//                        if (baseResult != null && baseResult.ret == 0) {
//                            if (albumInfoBase != null && albumInfoBase.channel != null) {
//                                albumInfoBase.channel.subscribed_count = albumInfoBase.channel.subscribed_count + 1;
//                                albumInfoBase.channel.had_subscribed = true;
//                                setResultData(albumInfoBase);
//                            }
//                            T.getInstance().showToast("订阅成功");
//                        } else {
//                            if (baseResult != null)
//                                T.getInstance().showToast(baseResult.msg);
//                            else
//                                T.getInstance().showToast("订阅失败");
//                        }
//                        dialogCancel();
//                    }
//                }, new Action1<Throwable>() {
//                    @Override
//                    public void call(Throwable throwable) {
//                        T.getInstance().showToast("订阅失败");
//                        dialogCancel();
//                    }
//                });
//    }

//    private void deleteSubscriptionsRadio() {
//        dialogShow();
//        RetrofitUtils.getInstance().deleteSubscriptionsRadio(presenter.getRid())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<BaseResult>() {
//                    @Override
//                    public void call(BaseResult baseResult) {
//                        if (baseResult != null && baseResult.ret == 0) {
//                            if (albumInfoBase != null && albumInfoBase.channel != null) {
//                                albumInfoBase.channel.subscribed_count = albumInfoBase.channel.subscribed_count - 1;
//                                albumInfoBase.channel.had_subscribed = false;
//                                setResultData(albumInfoBase);
//                            }
//                            T.getInstance().showToast("取消订阅成功");
//                        } else {
//                            if (baseResult != null)
//                                T.getInstance().showToast(baseResult.msg);
//                            else
//                                T.getInstance().showToast("取消订阅失败");
//                        }
//                        dialogCancel();
//                    }
//                }, new Action1<Throwable>() {
//                    @Override
//                    public void call(Throwable throwable) {
//                        T.getInstance().showToast("取消订阅失败");
//                        dialogCancel();
//                    }
//                });
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                closeFragment();
                break;
            case R.id.tvYesterday:
                setTextColor(0);
                break;
            case R.id.tvToday:
                setTextColor(1);
                break;
            case R.id.tvTomorrow:
                setTextColor(2);
                break;
//            case R.id.tvFollow:
//                if (albumInfoBase.channel.had_subscribed == true) {
//                    deleteSubscriptionsRadio();
//                } else {
//                    subscriptionsRadio();
//                }
//                break;
        }
    }

    public void setTitle(String title) {
        tvTitle.setText(title + "");
    }

    @Override
    public void onScrollChanged(ObservableScrollView scrollView, int x, int dy, int oldx, int oldy) {
        if (dy <= 0) {   //设置标题的背景颜色
            mRelativeLayout.setBackgroundColor(Color.argb((int) 0, 255, 255, 255));
            tvTitle.setTextColor(Color.argb((int) 0, 22, 24, 26));
            ivBack.setImageResource(R.drawable.icon_mine_img_close);
        } else if (dy > 0 && dy <= height) { //滑动距离小于banner图的高度时，设置背景和字体颜色颜色透明度渐变
            float scale = (float) dy / height;
            float alpha = (255 * scale);
            mRelativeLayout.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
            tvTitle.setTextColor(Color.argb((int) alpha, 22, 24, 26));
            ivBack.setImageResource(R.drawable.icon_mine_img_close);
        } else {
            mRelativeLayout.setBackgroundColor(Color.argb((int) 255, 255, 255, 255));
            tvTitle.setTextColor(Color.argb((int) 255, 22, 24, 26));
            ivBack.setImageResource(R.drawable.icon_back_black);
        }
    }


    public void showContentView() {
        loadLayout.showContentView();
    }

    public void showEmptyView() {
        loadLayout.showEmptyView();
    }

    public void showLoadingView() {
        loadLayout.showLoadingView();
    }

    public void showErrorView() {
        loadLayout.showErrorView();
    }

    /**
     * 展示弹出框
     */
    public void dialogShow() {
        dialog = DialogUtils.Dialog(this.getActivity());
    }

    /**
     * 取消弹出框
     */
    public void dialogCancel() {
        if (dialog != null) dialog.dismiss();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.destroy();
        presenter = null;
    }
}
