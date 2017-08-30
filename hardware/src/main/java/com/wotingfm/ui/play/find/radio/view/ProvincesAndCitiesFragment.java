package com.wotingfm.ui.play.find.radio.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.woting.commonplat.config.GlobalAddressConfig;
import com.woting.commonplat.constant.BroadcastConstants;
import com.woting.commonplat.widget.LoadFrameLayout;
import com.wotingfm.R;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.config.LocationInfo;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.ui.adapter.radioAdapter.ProvincesAdapter;
import com.wotingfm.ui.base.basefragment.BaseFragment;
import com.wotingfm.ui.bean.ChannelsBean;
import com.wotingfm.ui.bean.MessageEvent;
import com.wotingfm.ui.bean.Provinces;
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
 * 省市列表
 */

public class ProvincesAndCitiesFragment extends Fragment implements View.OnClickListener{

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.loadLayout)
    LoadFrameLayout loadLayout;

    private LocationInfo locationInfo;
    private ProvincesAdapter mAdapter;
    private List<Provinces.DataBean.ProvincesBean> albumsBeanList = new ArrayList<>();
    private View headview;
    private TextView tvLocal;
    private HeaderAndFooterWrapper headerAndFooterWrapper;
    private MessageReceiver messageReceiver;
    private View rootView;


    public static ProvincesAndCitiesFragment newInstance() {
        ProvincesAndCitiesFragment fragment = new ProvincesAndCitiesFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.activity_provinces_radio, container, false);
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
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new ProvincesAdapter(getActivity(), albumsBeanList, new ProvincesAdapter.ProvincesClick() {
            @Override
            public void clickAlbums(Provinces.DataBean.ProvincesBean singlesBean) {
                openFragment(ProvincesAndCitiesListRadioFragment.newInstance(singlesBean.title, singlesBean.id));
            }
        });
        headerAndFooterWrapper = new HeaderAndFooterWrapper(mAdapter);
        headerAndFooterWrapper.addHeaderView(headview);
        mRecyclerView.setAdapter(headerAndFooterWrapper);
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
        refresh();
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
                            openFragment(ProvincesAndCitiesListRadioFragment.newInstance(GlobalAddressConfig.CityName, GlobalAddressConfig.AdCode));
                        }
                    });
                }
            }
        }
    }

    private void refresh() {
        RetrofitUtils.getInstance().getProvinces()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Provinces.DataBean.ProvincesBean>>() {
                    @Override
                    public void call(List<Provinces.DataBean.ProvincesBean> albumsBeen) {
                        if (albumsBeen != null && !albumsBeen.isEmpty()) {
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
                        loadLayout.showErrorView();
                        throwable.printStackTrace();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left_btn:
                closeFragment();
                break;
        }
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locationInfo != null)
            locationInfo.stopLocation();
        if (messageReceiver != null && getActivity() != null)
            getActivity().unregisterReceiver(messageReceiver);
    }
}
