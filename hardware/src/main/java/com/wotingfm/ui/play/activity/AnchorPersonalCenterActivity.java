package com.wotingfm.ui.play.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.woting.commonplat.widget.GlideCircleTransform;
import com.woting.commonplat.widget.LoadFrameLayout;
import com.wotingfm.R;
import com.wotingfm.common.adapter.AnchorPersonalCenterInfoAdapter;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.bean.AnchorInfo;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.common.utils.T;
import com.wotingfm.ui.base.baseactivity.NoTitleBarBaseActivity;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.loc.e.d;
import static com.loc.e.i;
import static com.wotingfm.R.id.edContent;

/**
 * Created by amine on 2017/6/12.
 * 主播个人中心
 */

public class AnchorPersonalCenterActivity extends NoTitleBarBaseActivity implements View.OnClickListener {
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.ivMore)
    ImageView ivMore;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.loadLayout)
    LoadFrameLayout loadLayout;

    public static void start(Activity activity, String uid) {
        Intent intent = new Intent(activity, AnchorPersonalCenterActivity.class);
        intent.putExtra("uid", uid);
        activity.startActivityForResult(intent, 8080);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_anchor_personal_center;
    }

    private AnchorPersonalCenterInfoAdapter anchorPersonalCenterInfoAdapter;
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;

    @Override
    public void initView() {
        ivMore.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        anchorPersonalCenterInfoAdapter = new AnchorPersonalCenterInfoAdapter(this, dataBeanXes);
        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(anchorPersonalCenterInfoAdapter);
        View headview = LayoutInflater.from(this).inflate(R.layout.header_anchor_personal, mRecyclerView, false);
        ivPhotoBg = (ImageView) headview.findViewById(R.id.ivPhotoBg);
        ivPhoto = (ImageView) headview.findViewById(R.id.ivPhoto);
        tvFensNub = (TextView) headview.findViewById(R.id.tvFensNub);
        tvName = (TextView) headview.findViewById(R.id.tvName);
        tvFollowNub = (TextView) headview.findViewById(R.id.tvFollowNub);
        tvFollow = (TextView) headview.findViewById(R.id.tvFollow);
        tvContent = (TextView) headview.findViewById(R.id.tvContent);
        mHeaderAndFooterWrapper.addHeaderView(headview);
        mRecyclerView.setAdapter(mHeaderAndFooterWrapper);
        reportsPlayer(getIntent().getStringExtra("uid"));
    }

    private List<AnchorInfo.DataBeanXX.UserBean.DataBeanX> dataBeanXes = new ArrayList<>();


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.ivMore:
                finish();
                break;
        }
    }

    private void reportsPlayer(String uid) {
        loadLayout.showLoadingView();
        RetrofitUtils.getInstance().getAnchorInfo(uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<AnchorInfo>() {
                    @Override
                    public void call(AnchorInfo s) {
                        if (s == null) {
                            loadLayout.showErrorView();
                        } else {
                            if (s.data != null && s.data.user != null) {
                                loadLayout.showContentView();
                                setHeadViewData(s);
                                dataBeanXes.addAll(s.data.user.data);
                                mHeaderAndFooterWrapper.notifyDataSetChanged();
                            } else {
                                loadLayout.showErrorView();
                            }
                        }
                    }


                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        loadLayout.showErrorView();
                    }
                });
    }

    //headview
    private ImageView ivPhotoBg, ivPhoto;
    private TextView tvName, tvFensNub, tvFollowNub, tvFollow, tvContent;

    private void setHeadViewData(AnchorInfo s) {
        Glide.with(BSApplication.getInstance()).load(s.data.user.avatar)// Glide
                .transform(new GlideCircleTransform(BSApplication.getInstance()))
                .into(ivPhotoBg);
        Glide.with(BSApplication.getInstance()).load(s.data.user.avatar)// Glide
                .transform(new GlideCircleTransform(BSApplication.getInstance()))
                .into(ivPhoto);
        tvName.setText(s.data.user.name);
        tvFensNub.setText("粉丝" + s.data.user.fans_count);
        tvFollowNub.setText("关注" + s.data.user.idols_count);
        if (TextUtils.isEmpty(s.data.user.signature)) {
            tvContent.setVisibility(View.GONE);
        } else {
            tvContent.setVisibility(View.VISIBLE);
            tvContent.setText(s.data.user.signature);
        }
    }
}
