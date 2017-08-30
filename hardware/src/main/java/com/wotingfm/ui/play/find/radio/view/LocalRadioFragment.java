package com.wotingfm.ui.play.find.radio.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.woting.commonplat.amine.ARecyclerView;
import com.woting.commonplat.amine.LoadMoreFooterView;
import com.woting.commonplat.amine.OnLoadMoreListener;
import com.woting.commonplat.amine.OnRefreshListener;
import com.woting.commonplat.config.GlobalAddressConfig;
import com.woting.commonplat.constant.BroadcastConstants;
import com.woting.commonplat.widget.LoadFrameLayout;
import com.wotingfm.R;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.config.LocationInfo;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.ui.adapter.radioAdapter.RadioAdapter;
import com.wotingfm.ui.base.basefragment.BaseFragment;
import com.wotingfm.ui.bean.ChannelsBean;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by amine on 2017/7/6.
 * 本地台
 */

public class LocalRadioFragment extends BaseFragment implements OnLoadMoreListener, OnRefreshListener {
    @BindView(R.id.mRecyclerView)
    ARecyclerView mRecyclerView;
    @BindView(R.id.loadLayout)
    LoadFrameLayout loadLayout;

    public static LocalRadioFragment newInstance() {
        LocalRadioFragment fragment = new LocalRadioFragment();
        return fragment;
    }

    private LocationInfo locationInfo;
    private LoadMoreFooterView loadMoreFooterView;
    private RadioAdapter mAdapter;
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    private List<ChannelsBean> albumsBeanList = new ArrayList<>();
    private View headview;
    private TextView tvLocal;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_local_radio;
    }

    @Override
    public void initView() {
        setTitle("本地台");
        locationInfo = new LocationInfo(BSApplication.getInstance());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        headview = LayoutInflater.from(getActivity()).inflate(R.layout.local_headview, null);
        tvLocal = (TextView) headview.findViewById(R.id.tvLocal);
        loadMoreFooterView = (LoadMoreFooterView) mRecyclerView.getLoadMoreFooterView();
        mRecyclerView.setOnLoadMoreListener(this);
        mRecyclerView.setOnRefreshListener(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new RadioAdapter(getActivity(), albumsBeanList, new RadioAdapter.RadioClick() {
            @Override
            public void clickAlbums(ChannelsBean singlesBean) {
                // RadioInfoActivity.start(LocalRadioActivity.this, singlesBean.title, singlesBean.id);
                startMain(singlesBean);
            }
        });
        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(mAdapter);
        mHeaderAndFooterWrapper.addHeaderView(headview);
        mRecyclerView.setIAdapter(mHeaderAndFooterWrapper);
        loadLayout.findViewById(R.id.btnTryAgain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadLayout.showLoadingView();
                refresh();
            }
        });
        loadLayout.showLoadingView();
        if (messageReceiver == null) {
            messageReceiver = new MessageReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(BroadcastConstants.CITY_CHANGE);
            getActivity().registerReceiver(messageReceiver, filter);
        }

    }

    private MessageReceiver messageReceiver;

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locationInfo != null)
            locationInfo.stopLocation();
        if (messageReceiver != null && getActivity() != null)
            getActivity().unregisterReceiver(messageReceiver);
    }

    private int mPage;


    class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BroadcastConstants.CITY_CHANGE)) {
                if (GlobalAddressConfig.CityName != null) {
                    tvLocal.setText(GlobalAddressConfig.CityName);
                    tvLocal.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openFragment(ProvincesAndCitiesListRadioFragment.newInstance(GlobalAddressConfig.CityName, GlobalAddressConfig.AdCode));
                        }
                    });
                    refresh();
                }
            }
        }

    }

    private void refresh() {
        mPage = 1;
        RetrofitUtils.getInstance().getChannelsRadioLocation("local_areas", GlobalAddressConfig.AdCode, mPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<ChannelsBean>>() {
                    @Override
                    public void call(List<ChannelsBean> albumsBeen) {
                        mRecyclerView.setRefreshing(false);
                        if (albumsBeen != null && !albumsBeen.isEmpty()) {
                            mPage++;
                            albumsBeanList.clear();
                            albumsBeanList.addAll(albumsBeen);
                            loadLayout.showContentView();
                            mAdapter.notifyDataSetChanged();
                        } else {
                            loadLayout.showContentView();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        loadLayout.showContentView();
                        throwable.printStackTrace();
                    }
                });

    }

    private void loadMore() {
        RetrofitUtils.getInstance().getChannelsRadioLocation("local_areas", GlobalAddressConfig.AdCode, mPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<ChannelsBean>>() {
                    @Override
                    public void call(List<ChannelsBean> albumsBeen) {
                        mRecyclerView.setRefreshing(false);
                        if (albumsBeen != null && !albumsBeen.isEmpty()) {
                            mPage++;
                            albumsBeanList.addAll(albumsBeen);
                            mAdapter.notifyDataSetChanged();
                            loadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                        } else {
                            loadMoreFooterView.setStatus(LoadMoreFooterView.Status.THE_END);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        loadLayout.showErrorView();
                        throwable.printStackTrace();
                    }
                });
    }

    @Override
    public void onLoadMore(View loadMoreView) {
        if (loadMoreFooterView.canLoadMore() && mAdapter.getItemCount() > 0) {
            loadMoreFooterView.setStatus(LoadMoreFooterView.Status.LOADING);
            loadMore();
        }
    }

    @Override
    public void onRefresh() {
        refresh();
    }
}
