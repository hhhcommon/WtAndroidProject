package com.wotingfm.ui.play.find.radio.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.woting.commonplat.amine.ARecyclerView;
import com.woting.commonplat.amine.LoadMoreFooterView;
import com.woting.commonplat.amine.OnLoadMoreListener;
import com.woting.commonplat.amine.OnRefreshListener;
import com.woting.commonplat.config.GlobalAddressConfig;
import com.woting.commonplat.constant.BroadcastConstants;
import com.wotingfm.R;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.config.LocationInfo;
import com.wotingfm.common.constant.StringConstant;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.ui.adapter.radioAdapter.RadioAdapter;
import com.wotingfm.ui.bean.ChannelsBean;
import com.wotingfm.ui.bean.MessageEvent;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;
import com.wotingfm.ui.mine.main.MineActivity;
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
    @BindView(R.id.lin_bg)
    LinearLayout lin_bg;
    @BindView(R.id.lin_load)
    LinearLayout lin_load;

    private View headview;
    private TextView tvLocal;
    private View rootView;
    private LoadMoreFooterView loadMoreFooterView;

    private RadioAdapter mAdapter;
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;

    private MessageReceiver messageReceiver;

    private List<ChannelsBean> albumsBeanList = new ArrayList<>();
    private LocationInfo locationInfo;
    private int mPage;
    private String refreshType="local_areas";;

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

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        headview = LayoutInflater.from(getActivity()).inflate(R.layout.local_headview, null);
        tvLocal = (TextView) headview.findViewById(R.id.tvLocal);
        tvLocal.setOnClickListener(this);
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
        lin_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lin_bg.setVisibility(View.GONE);
                lin_load.setVisibility(View.VISIBLE);
                refresh();
            }
        });
        if (messageReceiver == null) {
            messageReceiver = new MessageReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(BroadcastConstants.CITY_CHANGE);
            getActivity().registerReceiver(messageReceiver, filter);
        }
        locationInfo = new LocationInfo(BSApplication.getInstance());
    }

    private void refresh() {
        mPage = 1;
        RetrofitUtils.getInstance().getChannelsRadioLocation(refreshType, GlobalAddressConfig.AdCode, mPage)
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
                            lin_bg.setVisibility(View.GONE);
                            lin_load.setVisibility(View.GONE);
                            mHeaderAndFooterWrapper.notifyDataSetChanged();
                        } else {
                            if (albumsBeanList != null && albumsBeanList.size() > 0) {
                                lin_bg.setVisibility(View.GONE);
                                lin_load.setVisibility(View.GONE);
                            } else {
                                lin_bg.setVisibility(View.VISIBLE);
                                lin_load.setVisibility(View.GONE);
                            }
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (albumsBeanList != null && albumsBeanList.size() > 0) {
                            lin_bg.setVisibility(View.GONE);
                            lin_load.setVisibility(View.VISIBLE);
                        } else {
                            lin_bg.setVisibility(View.VISIBLE);
                            lin_load.setVisibility(View.GONE);
                        }
                        throwable.printStackTrace();
                    }
                });
    }

    private void loadMore() {
        RetrofitUtils.getInstance().getChannelsRadioLocation(refreshType, GlobalAddressConfig.AdCode, mPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<ChannelsBean>>() {
                    @Override
                    public void call(List<ChannelsBean> albumsBeen) {
                        mRecyclerView.setRefreshing(false);
                        if (albumsBeen != null && !albumsBeen.isEmpty()) {
                            mPage++;
                            albumsBeanList.addAll(albumsBeen);
                            mHeaderAndFooterWrapper.notifyDataSetChanged();
                            loadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                        } else {
                            loadMoreFooterView.setStatus(LoadMoreFooterView.Status.THE_END);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (albumsBeanList != null && albumsBeanList.size() > 0) {
                            lin_bg.setVisibility(View.GONE);
                            lin_load.setVisibility(View.VISIBLE);
                        } else {
                            lin_bg.setVisibility(View.VISIBLE);
                            lin_load.setVisibility(View.GONE);
                        }
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
            case R.id.tvLocal:
                //  Fragment fragment=ProvincesAndCitiesListRadioFragment.newInstance(GlobalAddressConfig.CityName, GlobalAddressConfig.AdCode);
                ProvincesAndCitiesFragment fragment = ProvincesAndCitiesFragment.newInstance(1);
                openFragment(fragment);
                fragment.setResultListener(new ProvincesAndCitiesFragment.ResultListener() {
                    @Override
                    public void resultListener(boolean type) {
                        if (type) {
                            lin_bg.setVisibility(View.GONE);
                            lin_load.setVisibility(View.VISIBLE);
                            refreshType="provinces";
                            tvLocal.setText(GlobalAddressConfig.CityName);
                            if (locationInfo != null) locationInfo.stopLocation();
                            refresh();
                        }
                    }
                });
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
     *
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

    class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BroadcastConstants.CITY_CHANGE)) {
                String isLocation = BSApplication.SharedPreferences.getString(StringConstant.IS_LOCATION, "false");
                if (!TextUtils.isEmpty(isLocation) && isLocation.trim().equals("true")) {
                    if (GlobalAddressConfig.CityName != null) {
                        tvLocal.setText(GlobalAddressConfig.CityName);
                        refresh();
                    } else {
                        tvLocal.setText("定位失败");
                    }
                } else {
                    tvLocal.setText("定位失败");
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locationInfo != null) locationInfo.stopLocation();
        if (messageReceiver != null && getActivity() != null)
            getActivity().unregisterReceiver(messageReceiver);
    }
}
