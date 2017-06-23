package com.wotingfm.ui.play.look.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.woting.commonplat.widget.LoadFrameLayout;
import com.wotingfm.R;
import com.wotingfm.common.adapter.findHome.RadioStationAdapter;
import com.wotingfm.common.adapter.findHome.SelectedAdapter;
import com.wotingfm.common.bean.HomeBanners;
import com.wotingfm.common.bean.Radiostation;
import com.wotingfm.common.bean.Selected;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.common.view.BannerView;
import com.wotingfm.ui.base.basefragment.BaseFragment;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.wotingfm.R.id.loadLayout;
import static com.wotingfm.R.id.mRecyclerView;

/**
 * Created by amine on 2017/6/14.
 * 发现电台
 */

public class RadioStationFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.id_swipe_ly)
    SwipeRefreshLayout mSwipeLayout;
    @BindView(R.id.loadLayout)
    LoadFrameLayout loadLayout;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_selected;
    }

    public static RadioStationFragment newInstance() {
        RadioStationFragment fragment = new RadioStationFragment();
        return fragment;
    }

    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    private View headview;

    @Override
    protected void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mSwipeLayout.setOnRefreshListener(this);
        RadioStationAdapter selectedAdapter = new RadioStationAdapter(getActivity(), datas, new RadioStationAdapter.RadioStationClick() {
            @Override
            public void click(Radiostation.DataBean dataBean) {

            }
        });
        headview = LayoutInflater.from(getActivity()).inflate(R.layout.headview_radiostation, mRecyclerView, false);
        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(selectedAdapter);
        mHeaderAndFooterWrapper.addHeaderView(headview);
        mRecyclerView.setAdapter(mHeaderAndFooterWrapper);
        mSwipeLayout.setColorSchemeResources(R.color.app_basic, R.color.app_basic,
                R.color.app_basic, R.color.app_basic);
        mBannerView = (BannerView) headview.findViewById(R.id.mBannerView);
        tvLocal = (TextView) headview.findViewById(R.id.tvLocal);
        tvCountry = (TextView) headview.findViewById(R.id.tvCountry);
        tvProvince = (TextView) headview.findViewById(R.id.tvProvince);
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
        //refresh();
        tvLocal.setOnClickListener(this);
        tvCountry.setOnClickListener(this);
        tvProvince.setOnClickListener(this);
    }

    private TextView tvLocal, tvCountry, tvProvince;


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

    private List<Radiostation.DataBean> datas = new ArrayList<>();
    private BannerView mBannerView;

    private void getBanners() {
        RetrofitUtils.getInstance().getHomeBanners("RADIO")
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
                        Radiostation.DataBean r = new Radiostation.DataBean();
                        r.title1 = "北京新闻广播";
                        r.title2 = "正在直播：新闻新天下";
                        r.title3 = 1;
                        r.logo = "http://";
                        datas.add(r);
                        Radiostation.DataBean r2 = new Radiostation.DataBean();
                        r2.title1 = "北京新闻广播2";
                        r2.title2 = "正在直播：新闻新天下";
                        r2.title3 = 2;
                        r2.logo = "http://";
                        datas.add(r2);
                        mHeaderAndFooterWrapper.notifyDataSetChanged();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }

    private void refresh() {
       /* RetrofitUtils.getInstance().getSelecteds()
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
                });*/
    }

    @Override
    public void onRefresh() {
        getBanners();
    }

}