package com.wotingfm.ui.play.find.radio.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.woting.commonplat.config.GlobalAddressConfig;
import com.woting.commonplat.constant.BroadcastConstants;
import com.woting.commonplat.widget.LoadFrameLayout;
import com.wotingfm.R;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.config.LocationInfo;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.ui.adapter.radioAdapter.ProvincesAdapter;
import com.wotingfm.ui.base.basefragment.BaseFragment;
import com.wotingfm.ui.bean.Provinces;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by amine on 2017/7/6.
 * 省市台
 */

public class ProvincesAndCitiesFragment extends BaseFragment {
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.loadLayout)
    LoadFrameLayout loadLayout;

    public static ProvincesAndCitiesFragment newInstance() {
        ProvincesAndCitiesFragment fragment = new ProvincesAndCitiesFragment();
        return fragment;
    }


    private LocationInfo locationInfo;
    private ProvincesAdapter mAdapter;
    private List<Provinces.DataBean.ProvincesBean> albumsBeanList = new ArrayList<>();
    private View headview;
    private TextView tvLocal;
    private HeaderAndFooterWrapper headerAndFooterWrapper;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_provinces_radio;
    }

    @Override
    public void initView() {
        setTitle("省市台");
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

    private MessageReceiver messageReceiver;

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

}
