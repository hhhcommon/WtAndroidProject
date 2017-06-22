package com.wotingfm.ui.play.look.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.woting.commonplat.amine.ARecyclerView;
import com.woting.commonplat.widget.LoadFrameLayout;
import com.wotingfm.R;
import com.wotingfm.common.adapter.findHome.LiveListAdapter;
import com.wotingfm.common.adapter.findHome.RadioStationAdapter;
import com.wotingfm.common.bean.HomeBanners;
import com.wotingfm.common.bean.LiveBean;
import com.wotingfm.common.bean.Radiostation;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.common.view.BannerView;
import com.wotingfm.common.view.GridSpacingItemDecoration;
import com.wotingfm.ui.base.basefragment.BaseFragment;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.wotingfm.R.id.tvCountry;
import static com.wotingfm.R.id.tvLocal;
import static com.wotingfm.R.id.tvProvince;
import static com.wotingfm.ui.intercom.main.view.InterPhoneActivity.context;

/**
 * Created by amine on 2017/6/14.
 * 发现直播
 */

public class LiveFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.mRecyclerView)
    ARecyclerView mRecyclerView;
    @BindView(R.id.id_swipe_ly)
    SwipeRefreshLayout mSwipeLayout;
    @BindView(R.id.loadLayout)
    LoadFrameLayout loadLayout;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_live_list;
    }

    public static LiveFragment newInstance() {
        LiveFragment fragment = new LiveFragment();
        return fragment;
    }

    private View headview;
    private TextView tvTitle;

    @Override
    protected void initView() {
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mSwipeLayout.setOnRefreshListener(this);
        LiveListAdapter selectedAdapter = new LiveListAdapter(getActivity(), datas, new LiveListAdapter.LiveListClick() {
            @Override
            public void click(LiveBean.DataBean dataBean) {

            }
        });
        headview = LayoutInflater.from(getActivity()).inflate(R.layout.headview_live, mRecyclerView, false);
        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(selectedAdapter);
        mHeaderAndFooterWrapper.addHeaderView(headview);
        mRecyclerView.setAdapter(mHeaderAndFooterWrapper);
        mSwipeLayout.setColorSchemeResources(R.color.app_basic, R.color.app_basic,
                R.color.app_basic, R.color.app_basic);
        mBannerView = (BannerView) headview.findViewById(R.id.mBannerView);
        tvTitle = (TextView) headview.findViewById(R.id.tvTitle);
        loadLayout.showLoadingView();
        loadLayout.findViewById(R.id.btnTryAgain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadLayout.showLoadingView();
                // refresh();
                getBanners();
            }
        });
        getBanners();
    }

    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    private List<LiveBean.DataBean> datas = new ArrayList<>();
    private BannerView mBannerView;

    @Override
    public void onResume() {
//        setVideoResume();
        super.onResume();
        if (mBannerView != null)
            mBannerView.startTurning(5000);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mBannerView != null)
            mBannerView.stopTurning();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (mBannerView != null)
            //被hidden时未true 回到这个Fragment时返回false
            if (hidden) {
                mBannerView.stopTurning();
            } else {
//            setVideoResume();
                mBannerView.startTurning(5000);
            }

        super.onHiddenChanged(hidden);
    }

    private void getBanners() {
        RetrofitUtils.getInstance().getHomeBanners("LIVE")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<HomeBanners.DataBean.BannersBean>>() {
                    @Override
                    public void call(List<HomeBanners.DataBean.BannersBean> banners) {
                        loadLayout.showContentView();
                        mSwipeLayout.setRefreshing(false);
                        if (banners != null && !banners.isEmpty()) {
                            mBannerView.setData(banners);
                            mBannerView.setVisibility(View.VISIBLE);
                            int screenWidth = getResources().getDisplayMetrics().widthPixels;
                            mBannerView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                    (int) (screenWidth / 5f * 2)));
                            mBannerView.startTurning(5000);
                        } else {
                            mBannerView.setVisibility(View.GONE);
                        }
                        datas.clear();
                        LiveBean.DataBean r = new LiveBean.DataBean();
                        r.title1 = "北京新闻广播";
                        r.title2 = "正在直播：新闻新天下";
                        r.number = 1;
                        r.type = 1;
                        r.logo = "http://";
                        datas.add(r);
                        LiveBean.DataBean r1 = new LiveBean.DataBean();
                        r1.title1 = "北京新闻广播";
                        r1.title2 = "正在直播：新闻新天下";
                        r1.number = 1;
                        r1.type = 1;
                        r1.logo = "http://";
                        datas.add(r1);
                        LiveBean.DataBean r2 = new LiveBean.DataBean();
                        r2.title1 = "北京新闻广播2";
                        r2.title2 = "正在直播：新闻新天下";
                        r2.number = 2;
                        r2.type = 2;
                        r2.logo = "http://";
                        datas.add(r2);
                        LiveBean.DataBean r22 = new LiveBean.DataBean();
                        r22.title1 = "北京新闻广播2";
                        r22.title2 = "正在直播：新闻新天下";
                        r22.number = 2;
                        r22.type = 2;
                        r22.logo = "http://";
                        datas.add(r22);
                        mHeaderAndFooterWrapper.notifyDataSetChanged();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }

    @Override
    public void onRefresh() {
        getBanners();
    }
}
