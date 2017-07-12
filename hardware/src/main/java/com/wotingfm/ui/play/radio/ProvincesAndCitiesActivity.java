package com.wotingfm.ui.play.radio;

import android.app.Activity;
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
import com.wotingfm.common.adapter.radioAdapter.ProvincesAdapter;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.bean.Provinces;
import com.wotingfm.common.config.LocationInfo;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.ui.base.baseactivity.BaseToolBarActivity;
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

public class ProvincesAndCitiesActivity extends BaseToolBarActivity {
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.loadLayout)
    LoadFrameLayout loadLayout;

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, ProvincesAndCitiesActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_provinces_radio;
    }


    private LocationInfo locationInfo;
    private ProvincesAdapter mAdapter;
    private List<Provinces.DataBean.ProvincesBean> albumsBeanList = new ArrayList<>();
    private View headview;
    private TextView tvLocal;
    private HeaderAndFooterWrapper headerAndFooterWrapper;

    @Override
    public void initView() {
        setTitle("省市台");
        locationInfo = new LocationInfo(getApplicationContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        headview = LayoutInflater.from(this).inflate(R.layout.local_headview, null);
        tvLocal = (TextView) headview.findViewById(R.id.tvLocal);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new ProvincesAdapter(this, albumsBeanList, new ProvincesAdapter.ProvincesClick() {
            @Override
            public void clickAlbums(Provinces.DataBean.ProvincesBean singlesBean) {
                ProvincesAndCitiesListRadioActivity.start(ProvincesAndCitiesActivity.this, singlesBean.title);
            }
        });
        headerAndFooterWrapper = new HeaderAndFooterWrapper(mAdapter);
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
            context.registerReceiver(messageReceiver, filter);
        }
        refresh();
    }

    private MessageReceiver messageReceiver;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationInfo != null)
            locationInfo.stopLocation();
        if (messageReceiver != null)
            unregisterReceiver(messageReceiver);
    }


    class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BroadcastConstants.CITY_CHANGE)) {
                if (GlobalAddressConfig.CityName != null)
                    tvLocal.setText(GlobalAddressConfig.CityName);
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
