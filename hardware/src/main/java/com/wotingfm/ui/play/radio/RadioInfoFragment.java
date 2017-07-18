package com.wotingfm.ui.play.radio;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.woting.commonplat.utils.DementionUtil;
import com.woting.commonplat.widget.GlideCircleTransform;
import com.woting.commonplat.widget.LoadFrameLayout;
import com.wotingfm.R;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.bean.AlbumInfo;
import com.wotingfm.common.bean.BaseResult;
import com.wotingfm.common.bean.Provinces;
import com.wotingfm.common.bean.Radio;
import com.wotingfm.common.bean.RadioInfo;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.common.utils.T;
import com.wotingfm.common.view.ObservableScrollView;
import com.wotingfm.ui.base.baseactivity.AppManager;
import com.wotingfm.ui.base.baseactivity.NoTitleBarBaseActivity;
import com.wotingfm.ui.base.basefragment.BaseFragment;
import com.wotingfm.ui.play.activity.albums.fragment.AlbumsInfoFragment;
import com.wotingfm.ui.play.activity.albums.fragment.ProgramInfoFragment;
import com.wotingfm.ui.play.activity.albums.fragment.SimilarInfoFragment;
import com.wotingfm.ui.play.radio.fragment.RadioInfoTodayFragment;
import com.wotingfm.ui.play.radio.fragment.RadioInfoTomorrowFragment;
import com.wotingfm.ui.play.radio.fragment.RadioInfoYesterdayFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.BlurTransformation;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.wotingfm.R.id.tvAlbumsInfo;
import static com.wotingfm.R.id.tvProgramInfo;
import static com.wotingfm.R.id.tvSimilarInfo;

/**
 * Created by amine on 2017/7/7.
 * 电台详情
 */

public class RadioInfoFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.ivPhotoBg)
    ImageView ivPhotoBg;
    @BindView(R.id.ivPhoto)
    ImageView ivPhoto;
    @BindView(R.id.tvFollow)
    TextView tvFollow;
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


    public static RadioInfoFragment newInstance(String rId, String title) {
        RadioInfoFragment fragment = new RadioInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("rId", rId);
        bundle.putSerializable("title", title);
        fragment.setArguments(bundle);
        return fragment;
    }

    private String rid;
    private int height;

    @Override
    public void initView() {
        height = DementionUtil.dip2px(getActivity(), 210);
        loadLayout.findViewById(R.id.btnTryAgain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadLayout.showLoadingView();
                refresh();
            }
        });
        loadLayout.showLoadingView();
        Bundle bundle = getArguments();
        if (bundle != null) {
            final String title = bundle.getString("title");
            tvTitle.setText(title + "电视台");
            rid = bundle.getString("rId");
            ivBack.setOnClickListener(this);
            tvYesterday.setOnClickListener(this);
            tvToday.setOnClickListener(this);
            tvTomorrow.setOnClickListener(this);
            mObservableScrollView.setScrollViewListener(new ObservableScrollView.ScrollViewListener() {
                @Override
                public void onScrollChanged(ObservableScrollView scrollView, int x, int dy, int oldx, int oldy) {
                    if (dy <= 0) {   //设置标题的背景颜色
                        mRelativeLayout.setBackgroundColor(Color.argb((int) 0, 255, 255, 255));
                        tvTitle.setTextColor(Color.argb((int) 0, 22, 24, 26));
                    } else if (dy > 0 && dy <= height) { //滑动距离小于banner图的高度时，设置背景和字体颜色颜色透明度渐变
                        float scale = (float) dy / height;
                        float alpha = (255 * scale);
                        mRelativeLayout.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
                        tvTitle.setTextColor(Color.argb((int) alpha, 22, 24, 26));
                    } else {
                        mRelativeLayout.setBackgroundColor(Color.argb((int) 255, 255, 255, 255));
                        tvTitle.setTextColor(Color.argb((int) 255, 22, 24, 26));
                    }
                }
            });
            refresh();
        }
    }


    private void refresh() {
        RetrofitUtils.getInstance().getRadioInfo(rid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<RadioInfo.DataBean>() {
                    @Override
                    public void call(RadioInfo.DataBean s) {
                        List<String> type = new ArrayList<>();
                        type.add("昨天");
                        type.add("今天");
                        type.add("明天");
                        initFragment(s);
                        albumInfoBase = s;
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

    private void setResultData(final RadioInfo.DataBean s) {
        Glide.with(getActivity())
                .load(s.channel.image_url)
                .placeholder(R.mipmap.oval_defut_other)
                .error(R.mipmap.oval_defut_other)
                .crossFade(1000)
                .bitmapTransform(new BlurTransformation(getActivity(), 23, 4))  // “23”：设置模糊度(在0.0到25.0之间)，默认”25";"4":图片缩放比例,默认“1”。
                .into(ivPhotoBg);
        Glide.with(BSApplication.getInstance()).load(s.channel.image_url)// Glide
                .error(R.mipmap.oval_defut_other)
                .placeholder(R.mipmap.oval_defut_other)
                .into(ivPhoto);
        if (s.channel.had_subscribed == true) {
            tvFollow.setText("已订阅(" + s.channel.subscribed_count + ")");
            tvFollow.setTextColor(Color.parseColor("#50ffffff"));
            Drawable rightDrawable = getResources().getDrawable(R.mipmap.icon_subscription_s);
            rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(), rightDrawable.getMinimumHeight());
            tvFollow.setCompoundDrawables(rightDrawable, null, null, null);
        } else {
            tvFollow.setText("订阅(" + s.channel.subscribed_count + ")");
            tvFollow.setTextColor(Color.parseColor("#ffffff"));
            Drawable rightDrawable = getResources().getDrawable(R.mipmap.icon_subscription_n);
            rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(), rightDrawable.getMinimumHeight());
            tvFollow.setCompoundDrawables(rightDrawable, null, null, null);
        }
        tvFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (s.channel.had_subscribed == true) {
                    deleteSubscriptionsRadio();
                } else {
                    subscriptionsRadio();
                }
            }
        });
    }

    private RadioInfoYesterdayFragment radioYesterdayFragment;
    private RadioInfoTodayFragment radioTodayFragment;
    private RadioInfoTomorrowFragment radioTomorrowFragment;

    private void initFragment(RadioInfo.DataBean s) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        radioYesterdayFragment = RadioInfoYesterdayFragment.newInstance(s);
        radioTodayFragment = RadioInfoTodayFragment.newInstance(s);
        radioTomorrowFragment = RadioInfoTomorrowFragment.newInstance(s);
        transaction.add(R.id.fl_body, radioYesterdayFragment, "radioYesterdayFragment");
        transaction.add(R.id.fl_body, radioTodayFragment, "radioTodayFragment");
        transaction.add(R.id.fl_body, radioTomorrowFragment, "radioTomorrowFragment");
        transaction.commit();
        SwitchTo(0);
    }

    private void SwitchTo(int position) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        switch (position) {
            case 0:
                transaction.hide(radioTodayFragment);
                transaction.hide(radioTomorrowFragment);
                transaction.show(radioYesterdayFragment);
                transaction.commitAllowingStateLoss();
                break;

            case 1:
                transaction.hide(radioYesterdayFragment);
                transaction.hide(radioTomorrowFragment);
                transaction.show(radioTodayFragment);
                transaction.commitAllowingStateLoss();
                break;
            case 2:
                transaction.hide(radioYesterdayFragment);
                transaction.hide(radioTodayFragment);
                transaction.show(radioTomorrowFragment);
                transaction.commitAllowingStateLoss();
                break;
            default:
                break;
        }
    }


    /**
     * @param textViewBase 需要变颜色文本
     * @param code         切换的下标
     */
    private void setTextColor(TextView textViewBase, int code) {
        tvTomorrow.setTextColor(Color.parseColor("#16181a"));
        tvYesterday.setTextColor(Color.parseColor("#16181a"));
        tvToday.setTextColor(Color.parseColor("#16181a"));
        textViewBase.setTextColor(Color.parseColor("#fd8548"));
        SwitchTo(code);
    }

    private void subscriptionsRadio() {
        showLodingDialog();
        RetrofitUtils.getInstance().subscriptionsRadio(rid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BaseResult>() {
                    @Override
                    public void call(BaseResult baseResult) {
                        if (baseResult != null && baseResult.ret == 0) {
                            if (albumInfoBase != null && albumInfoBase.channel != null) {
                                albumInfoBase.channel.subscribed_count = albumInfoBase.channel.subscribed_count + 1;
                                albumInfoBase.channel.had_subscribed = true;
                                setResultData(albumInfoBase);
                            }
                            T.getInstance().showToast("订阅成功");
                        } else {
                            if (baseResult != null)
                                T.getInstance().showToast(baseResult.msg);
                            else
                                T.getInstance().showToast("订阅失败");
                        }
                        dissmisDialog();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        T.getInstance().showToast("订阅失败");
                        dissmisDialog();
                    }
                });
    }

    private RadioInfo.DataBean albumInfoBase;

    private void deleteSubscriptionsRadio() {
        showLodingDialog();
        RetrofitUtils.getInstance().deleteSubscriptionsRadio(rid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BaseResult>() {
                    @Override
                    public void call(BaseResult baseResult) {
                        if (baseResult != null && baseResult.ret == 0) {
                            if (albumInfoBase != null && albumInfoBase.channel != null) {
                                albumInfoBase.channel.subscribed_count = albumInfoBase.channel.subscribed_count - 1;
                                albumInfoBase.channel.had_subscribed = false;
                                setResultData(albumInfoBase);
                            }
                            T.getInstance().showToast("取消订阅成功");
                        } else {
                            if (baseResult != null)
                                T.getInstance().showToast(baseResult.msg);
                            else
                                T.getInstance().showToast("取消订阅失败");
                        }
                        dissmisDialog();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        T.getInstance().showToast("取消订阅失败");
                        dissmisDialog();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                closeFragment();
                break;
            case R.id.tvYesterday:
                setTextColor(tvYesterday, 0);
                break;
            case R.id.tvToday:
                setTextColor(tvToday, 1);
                break;
            case R.id.tvTomorrow:
                setTextColor(tvTomorrow, 2);
                break;
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_radio_info;
    }


}
