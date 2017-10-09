package com.wotingfm.ui.play.find.live.view;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.GsonBuilder;
import com.woting.commonplat.widget.GlideCircleTransform;
import com.wotingfm.R;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.live.LiveManger;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.common.utils.CommonUtils;
import com.wotingfm.common.utils.DialogUtils;
import com.wotingfm.common.utils.T;
import com.wotingfm.ui.base.basefragment.BaseFragment;
import com.wotingfm.ui.bean.TrailerInfo;
import com.wotingfm.ui.play.anchor.view.AnchorPersonalCenterFragment;
import com.wotingfm.ui.user.logo.LogoActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 预告详情
 */

public class TrailerInfoFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.tvTrailerTitle)
    TextView tvTrailerTitle;
    @BindView(R.id.tvTrailerContent)
    TextView tvTrailerContent;
    @BindView(R.id.tvTrailerNumber)
    TextView tvTrailerNumber;
    @BindView(R.id.tvTrailerTime)
    TextView tvTrailerTime;
    @BindView(R.id.tvTrailerSubmit)
    TextView tvTrailerSubmit;
    @BindView(R.id.ivPhoto)
    ImageView ivPhoto;
    @BindView(R.id.tvTrailerFollow)
    TextView tvTrailerFollow;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvFens)
    TextView tvFens;
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.relativeLable)
    RelativeLayout relativeLable;
    @BindView(R.id.img_bg)
    ImageView img_bg;
    @BindView(R.id.re_body)
    RelativeLayout re_body;

    private String id;
    private String userId;
    private View rootView;
    private Dialog dialog;

    public static TrailerInfoFragment newInstance(String id) {
        TrailerInfoFragment fragment = new TrailerInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.activity_trailer_info, container, false);
            rootView.setOnClickListener(this);
            ButterKnife.bind(this, rootView);
            initView();
        }
        return rootView;
    }

    public void initView() {
        Bundle bundle = getArguments();
        if (bundle != null) id = bundle.getString("id");
        userId = CommonUtils.getUserId();
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFragment();
            }
        });
        refresh();
    }

    private void refresh() {
        LiveManger.getInstance().trailerInfo(id, new LiveManger.TrailerInfoCallBack() {
            @Override
            public void trailerInfo(final TrailerInfo.DataBean.VoiceLiveBean voiceLiveBean) {
                Log.e("预告详情返回数据", new GsonBuilder().serializeNulls().create().toJson(voiceLiveBean));
                if (voiceLiveBean == null) {
                    re_body.setVisibility(View.GONE);
                } else {
                    Glide.with(BSApplication.getInstance()).load(voiceLiveBean.cover)// Glide
                            .transform(new GlideCircleTransform(BSApplication.getInstance()))
                            .error(R.mipmap.p)
                            .placeholder(R.mipmap.p)
                            .into(img_bg);
                    tvTrailerTitle.setText(voiceLiveBean.title);
                    tvTrailerContent.setText(voiceLiveBean.description);
                    if (voiceLiveBean.begin_at != null) {
                        SpannableString msp = new SpannableString("开始时间  " + voiceLiveBean.begin_at);
                        msp.setSpan(new ForegroundColorSpan(Color.parseColor("#fd8548")), 4, 6 + voiceLiveBean.begin_at.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        tvTrailerTime.setText(msp);
                    }
                    tvTrailerNumber.setText(voiceLiveBean.reserved_count + "人预约");
                    isDeleteReservations(voiceLiveBean.had_ended);
                    tvTrailerSubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (CommonUtils.isLogin() == false) {
                                LogoActivity.start(getActivity());
                                return;
                            }
                            if (voiceLiveBean.had_reserved == true) {
                                deleteReservations(voiceLiveBean);
                            } else {
                                reservations(voiceLiveBean);
                            }
                        }
                    });
                    if (voiceLiveBean.owner != null) {
                        relativeLable.setVisibility(View.VISIBLE);
                        isFollow(voiceLiveBean.owner.had_followed);
                        tvTrailerFollow.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (CommonUtils.isLogin() == false) {
                                    LogoActivity.start(getActivity());
                                    return;
                                }
                                if (voiceLiveBean.owner.had_followed == true) {
                                    unFollowAnchor(userId, voiceLiveBean);
                                } else {
                                    followAnchor(userId, voiceLiveBean);
                                }
                            }
                        });
                        Glide.with(BSApplication.getInstance()).load(voiceLiveBean.owner.avatar)// Glide
                                .transform(new GlideCircleTransform(BSApplication.getInstance()))
                                .error(R.mipmap.oval_defut_photo)
                                .placeholder(R.mipmap.oval_defut_photo)
                                .into(ivPhoto);
                        ivPhoto.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openFragment(AnchorPersonalCenterFragment.newInstance(voiceLiveBean.owner.id));
                            }
                        });
                        tvName.setText(voiceLiveBean.owner.nick_name);
                        tvFens.setText("粉丝  " + voiceLiveBean.owner.fans_count);
                    } else {
                        relativeLable.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    private void followAnchor(String uid, final TrailerInfo.DataBean.VoiceLiveBean sw) {
        dialogShow();
        RetrofitUtils.getInstance().followAnchor(uid, sw.owner.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object Object) {
                        dialogCancel();
                        sw.owner.fans_count = sw.owner.fans_count + 1;
                        tvFens.setText("粉丝  " + sw.owner.fans_count);
                        sw.owner.had_followed = true;
                        isFollow(true);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        T.getInstance().showToast("关注失败");
                        dialogCancel();
                    }
                });
    }

    private void unFollowAnchor(String uid, final TrailerInfo.DataBean.VoiceLiveBean sw) {
        dialogShow();
        RetrofitUtils.getInstance().unFollowAnchor(uid, sw.owner.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object s) {
                        dialogCancel();
                        sw.owner.fans_count = sw.owner.fans_count - 1;
                        tvFens.setText("粉丝  " + sw.owner.fans_count);
                        sw.owner.had_followed = false;
                        isFollow(false);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        T.getInstance().showToast("取消关注失败");
                        dialogCancel();
                    }
                });
    }

    private void reservations(final TrailerInfo.DataBean.VoiceLiveBean voiceLiveBean) {
        dialogShow();
        RetrofitUtils.getInstance().reservations(voiceLiveBean.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object Object) {
                        isDeleteReservations(true);
                        voiceLiveBean.reserved_count = voiceLiveBean.reserved_count + 1;
                        tvTrailerNumber.setText(voiceLiveBean.reserved_count + "人预约");
                        voiceLiveBean.had_reserved = true;
                        dialogCancel();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        T.getInstance().showToast("预约失败");
                        dialogCancel();
                    }
                });
    }

    private void deleteReservations(final TrailerInfo.DataBean.VoiceLiveBean voiceLiveBean) {
        dialogShow();
        RetrofitUtils.getInstance().deleteReservations(voiceLiveBean.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object s) {
                        isDeleteReservations(false);
                        voiceLiveBean.reserved_count = voiceLiveBean.reserved_count - 1;
                        voiceLiveBean.had_reserved = false;
                        tvTrailerNumber.setText(voiceLiveBean.reserved_count + "人预约");
                        dialogCancel();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        T.getInstance().showToast("取消预约失败");
                        dialogCancel();
                    }
                });
    }

    private void isFollow(boolean isfollow) {
        if (isfollow == true) {
            tvTrailerFollow.setText("已关注");
            tvTrailerFollow.setTextColor(Color.parseColor("#50ffffff"));
            tvTrailerFollow.setBackgroundResource(R.drawable.trailer_info_follow_n);
        } else {
            tvTrailerFollow.setText("关注");
            tvTrailerFollow.setTextColor(Color.parseColor("#ffffff"));
            tvTrailerFollow.setBackgroundResource(R.drawable.trailer_info_follow);
        }
    }

    private void isDeleteReservations(boolean isfollow) {
        if (isfollow == true) {
            tvTrailerSubmit.setText("已预约");
            tvTrailerSubmit.setTextColor(Color.parseColor("#50ffffff"));
            tvTrailerSubmit.setBackgroundResource(R.drawable.trailer_info_bg_y);
        } else {
            tvTrailerSubmit.setText("预约");
            tvTrailerSubmit.setBackgroundResource(R.drawable.trailer_info_bg);
            tvTrailerSubmit.setTextColor(Color.parseColor("#fd8548"));
        }
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
    public void onClick(View v) {

    }

}
