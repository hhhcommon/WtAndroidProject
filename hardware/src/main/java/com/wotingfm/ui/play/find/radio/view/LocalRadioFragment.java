package com.wotingfm.ui.play.find.radio.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.config.LocationInfo;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.ui.adapter.radioAdapter.RadioAdapter;
import com.wotingfm.ui.base.basefragment.BaseFragment;
import com.wotingfm.ui.bean.ChannelsBean;
import com.wotingfm.ui.bean.MessageEvent;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;
import com.wotingfm.ui.mine.main.MineActivity;
import com.wotingfm.ui.play.anchor.view.AlbumsListMeFragment;
import com.wotingfm.ui.play.find.main.view.LookListActivity;
import com.wotingfm.ui.play.main.PlayerActivity;
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
 * 本地台
 */

public class LocalRadioFragment extends Fragment implements View.OnClickListener, OnLoadMoreListener, OnRefreshListener {
    @BindView(R.id.mRecyclerView)
    ARecyclerView mRecyclerView;
    @BindView(R.id.loadLayout)
    LoadFrameLayout loadLayout;

    private LocationInfo locationInfo;
    private LoadMoreFooterView loadMoreFooterView;
    private RadioAdapter mAdapter;
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    private List<ChannelsBean> albumsBeanList = new ArrayList<>();
    private View headview;
    private TextView tvLocal;
    private View rootView;
    private MessageReceiver messageReceiver;
    private int mPage;

    public static LocalRadioFragment newInstance() {
        LocalRadioFragment fragment = new LocalRadioFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.activity_local_radio, container, false);
            rootView.setOnClickListener(this);
            ButterKnife.bind(this, rootView);
            inItView();
        }
        return rootView;
    }

    public void inItView() {
        rootView.findViewById(R.id.head_left_btn).setOnClickListener(this);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locationInfo != null)
            locationInfo.stopLocation();
        if (messageReceiver != null && getActivity() != null)
            getActivity().unregisterReceiver(messageReceiver);
    }

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
                            Fragment fragment=ProvincesAndCitiesListRadioFragment.newInstance(GlobalAddressConfig.CityName, GlobalAddressConfig.AdCode);
                            openFragment(fragment);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left_btn:
                closeFragment();
                break;
        }
    }

    private void startMain(ChannelsBean channelsBean) {
        GlobalStateConfig.activityA = "A";
        EventBus.getDefault().post(new MessageEvent("one"));
        EventBus.getDefault().post(new MessageEvent(channelsBean, 1));
    }

    // 关闭页面
    private void closeFragment() {
        if (getActivity() instanceof PlayerActivity) {
            PlayerActivity.close();
        } else if (getActivity() instanceof MineActivity) {
            MineActivity.close();
        } else if (getActivity() instanceof LookListActivity) {
            LookListActivity.close();
        } else if (getActivity() instanceof InterPhoneActivity) {
            InterPhoneActivity.close();
        }
    }

    /**
     * 关闭界面
     * @param fragment
     */
    public void openFragment(Fragment fragment) {
        if (getActivity() instanceof PlayerActivity) {
            PlayerActivity.open(fragment);
        } else if (getActivity() instanceof MineActivity) {
            MineActivity.open(fragment);
        } else if (getActivity() instanceof InterPhoneActivity) {
            InterPhoneActivity.open(fragment);
        } else if (getActivity() instanceof LookListActivity) {
            LookListActivity.open(fragment);
        }
    }
}
