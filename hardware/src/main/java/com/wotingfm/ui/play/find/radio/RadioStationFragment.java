package com.wotingfm.ui.play.find.radio;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.woting.commonplat.widget.LoadFrameLayout;
import com.wotingfm.R;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.common.view.BannerView;
import com.wotingfm.ui.adapter.findHome.RadioStationAdapter;
import com.wotingfm.ui.bean.ChannelsBean;
import com.wotingfm.ui.bean.HomeBanners;
import com.wotingfm.ui.bean.MessageEvent;
import com.wotingfm.ui.play.find.main.view.LookListActivity;
import com.wotingfm.ui.play.main.PlayerActivity;
import com.wotingfm.ui.play.radio.CountryRadioFragment;
import com.wotingfm.ui.play.radio.LocalRadioFragment;
import com.wotingfm.ui.play.radio.ProvincesAndCitiesFragment;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by amine on 2017/6/14.
 * 发现电台
 */

public class RadioStationFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.id_swipe_ly)
    SwipeRefreshLayout mSwipeLayout;
    @BindView(R.id.loadLayout)
    LoadFrameLayout loadLayout;
    private View rootView;

    public static RadioStationFragment newInstance() {
        RadioStationFragment fragment = new RadioStationFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_selected, container, false);
            rootView.setOnClickListener(this);
            ButterKnife.bind(this, rootView);
            inItView();
        }
        return rootView;
    }

    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    private View headview;

    protected void inItView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mSwipeLayout.setOnRefreshListener(this);
        RadioStationAdapter selectedAdapter = new RadioStationAdapter(getActivity(), datas, new RadioStationAdapter.RadioStationClick() {
            @Override
            public void click(ChannelsBean dataBean) {
                startMain(dataBean);
                // RadioInfoActivity.start(getActivity(), dataBean.title, dataBean.id);
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
        tvTitle = (TextView) headview.findViewById(R.id.tvTitle);
        loadLayout.showLoadingView();
        loadLayout.findViewById(R.id.btnTryAgain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadLayout.showLoadingView();
                refresh();
                getBanners();
            }
        });
        getBanners();
        refresh();
        tvLocal.setOnClickListener(this);
        tvCountry.setOnClickListener(this);
        tvProvince.setOnClickListener(this);
        tvTitle.setOnClickListener(this);
    }

    private TextView tvLocal, tvCountry, tvProvince, tvTitle;


    @Override
    public void onResume() {
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

    private List<ChannelsBean> datas = new ArrayList<>();
    private BannerView mBannerView;

    private void getBanners() {
        RetrofitUtils.getInstance().getHomeBanners("RADIO")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<HomeBanners.DataBean.BannersBean>>() {
                    @Override
                    public void call(List<HomeBanners.DataBean.BannersBean> banners) {
                        loadLayout.showContentView();
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
        RetrofitUtils.getInstance().getChannelsRadioHots("part")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<ChannelsBean>>() {
                    @Override
                    public void call(List<ChannelsBean> dataBeanXes) {
                        mSwipeLayout.setRefreshing(false);
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvLocal:
                openFragment(LocalRadioFragment.newInstance());
                break;
            case R.id.tvCountry:
                openFragment(CountryRadioFragment.newInstance());
                break;
            case R.id.tvProvince:
                openFragment(ProvincesAndCitiesFragment.newInstance());
                break;
            case R.id.tvTitle:
                openFragment(RadioMoreFragment.newInstance());
                break;
        }
    }

    public void startMain(ChannelsBean channelsBean) {
        GlobalStateConfig.activityA = "A";
        EventBus.getDefault().post(new MessageEvent("one"));
        EventBus.getDefault().post(new MessageEvent(channelsBean, 1));
    }

    private void openFragment(Fragment fragment) {
        if (getActivity() instanceof PlayerActivity) {
            PlayerActivity.open(fragment);
        } else if (getActivity() instanceof LookListActivity) {
            LookListActivity.open(fragment);
        }
    }

    @Override
    public void onRefresh() {
        refresh();
    }

}
