package com.wotingfm.ui.play.look.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.woting.commonplat.widget.LoadFrameLayout;
import com.wotingfm.R;
import com.wotingfm.common.adapter.findHome.SelectedAdapter;
import com.wotingfm.common.bean.HomeBanners;
import com.wotingfm.common.bean.Player;
import com.wotingfm.common.bean.Selected;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.common.view.BannerView;
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

/**
 * Created by amine on 2017/6/14.
 * 发现精选
 */

public class SelectedFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.loadLayout)
    LoadFrameLayout loadLayout;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.id_swipe_ly)
    SwipeRefreshLayout mSwipeLayout;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_selected;
    }

    public static SelectedFragment newInstance() {
        SelectedFragment fragment = new SelectedFragment();
        return fragment;
    }

    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;

    @Override
    protected void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mSwipeLayout.setOnRefreshListener(this);
        SelectedAdapter selectedAdapter = new SelectedAdapter(getActivity(), datas, new SelectedAdapter.SelectedClickBase() {
            @Override
            public void click(Selected.DataBeanX.DataBean dataBean) {

            }

            @Override
            public void clickMore(Selected.DataBeanX dataBeanX) {

            }
        });
        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(selectedAdapter);
        mRecyclerView.setAdapter(mHeaderAndFooterWrapper);
        mSwipeLayout.setColorSchemeResources(R.color.app_basic, R.color.app_basic,
                R.color.app_basic, R.color.app_basic);
        mBannerView = new BannerView(getActivity());
        loadLayout.showLoadingView();
        loadLayout.findViewById(R.id.btnTryAgain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadLayout.showLoadingView();
                refresh();
                getBanners();
                getSelecteds();
            }
        });
        getBanners();
        getSelecteds();
    }

    private void refresh() {
    }

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

    private List<Selected.DataBeanX> datas = new ArrayList<>();
    private BannerView mBannerView;

    private void getBanners() {
        RetrofitUtils.getInstance().getHomeBanners("SELECTION")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<HomeBanners.DataBean.BannersBean>>() {
                    @Override
                    public void call(List<HomeBanners.DataBean.BannersBean> banners) {
                        if (banners != null && !banners.isEmpty()) {
                            mBannerView.setData(banners);
                            int screenWidth = getResources().getDisplayMetrics().widthPixels;
                            mBannerView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                    (int) (screenWidth / 5f * 2)));
                            mHeaderAndFooterWrapper.addHeaderView(mBannerView);
                            mBannerView.startTurning(5000);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }

    private void getSelecteds() {
        RetrofitUtils.getInstance().getSelecteds()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Selected.DataBeanX>>() {
                    @Override
                    public void call(List<Selected.DataBeanX> dataBeanXes) {
                        loadLayout.showContentView();
                        datas.clear();
                        datas.addAll(dataBeanXes);
                        mHeaderAndFooterWrapper.notifyDataSetChanged();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        loadLayout.showErrorView();
                    }
                });
    }

    @Override
    public void onRefresh() {
        getSelecteds();
    }
}
