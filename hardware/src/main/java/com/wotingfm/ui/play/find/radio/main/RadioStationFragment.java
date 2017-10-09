package com.wotingfm.ui.play.find.radio.main;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.GsonBuilder;
import com.woting.commonplat.amine.ARecyclerView;
import com.woting.commonplat.amine.LoadMoreFooterView;
import com.woting.commonplat.amine.OnLoadMoreListener;
import com.woting.commonplat.amine.OnRefreshListener;
import com.woting.commonplat.widget.LoadFrameLayout;
import com.wotingfm.R;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.common.utils.ToastUtils;
import com.wotingfm.ui.adapter.findHome.RadioStationAdapter;
import com.wotingfm.ui.base.basefragment.BaseFragment;
import com.wotingfm.ui.bean.ChannelsBean;
import com.wotingfm.ui.play.find.radio.view.CountryRadioFragment;
import com.wotingfm.ui.play.find.radio.view.LocalRadioFragment;
import com.wotingfm.ui.play.find.radio.view.ProvincesAndCitiesFragment;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

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

public class RadioStationFragment extends BaseFragment implements View.OnClickListener, OnLoadMoreListener, OnRefreshListener {

    @BindView(R.id.mRecyclerView)
    ARecyclerView mRecyclerView;

    @BindView(R.id.loadLayout)
    LoadFrameLayout loadLayout;
    private View rootView;

    private List<ChannelsBean> datas = new ArrayList<>();
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    private View headview;
    private RadioStationAdapter selectedAdapter;
    private LoadMoreFooterView loadMoreFooterView;

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

    protected void inItView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        loadMoreFooterView = (LoadMoreFooterView) mRecyclerView.getLoadMoreFooterView();
        mRecyclerView.setOnLoadMoreListener(this);
        mRecyclerView.setOnRefreshListener(this);
        mRecyclerView.setLayoutManager(layoutManager);
        selectedAdapter = new RadioStationAdapter(getActivity(), datas, new RadioStationAdapter.RadioStationClick() {
            @Override
            public void click(ChannelsBean dataBean) {
                startMain(dataBean);
                // RadioInfoActivity.start(getActivity(), dataBean.title, dataBean.id);
            }
        });
        headview = LayoutInflater.from(getActivity()).inflate(R.layout.headview_radiostation, mRecyclerView, false);
        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(selectedAdapter);
        mHeaderAndFooterWrapper.addHeaderView(headview);
        mRecyclerView.setIAdapter(mHeaderAndFooterWrapper);
        headview.findViewById(R.id.lin_Local).setOnClickListener(this);
        headview.findViewById(R.id.lin_Country).setOnClickListener(this);
        headview.findViewById(R.id.lin_Province).setOnClickListener(this);
        loadLayout.showLoadingView();
        loadLayout.findViewById(R.id.btnTryAgain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadLayout.showLoadingView();
                refresh();
            }
        });
        refresh();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lin_Local:
                openFragment(LocalRadioFragment.newInstance());
                break;
            case R.id.lin_Country:
                openFragment(CountryRadioFragment.newInstance());
                break;
            case R.id.lin_Province:
                openFragment(ProvincesAndCitiesFragment.newInstance(2));
                break;
//            case R.id.tvTitle:
//                openFragment(RadioMoreFragment.newInstance());
//                break;
        }
    }

    @Override
    public void onRefresh() {
        refresh();
    }

    @Override
    public void onLoadMore(View loadMoreView) {
        if (loadMoreFooterView.canLoadMore() && selectedAdapter.getItemCount() > 0) {
            loadMoreFooterView.setStatus(LoadMoreFooterView.Status.LOADING);
            loadMore();
        }
    }

    private void refresh() {
        RetrofitUtils.getInstance().getChannelsRadioHots("part")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<ChannelsBean>>() {
                    @Override
                    public void call(List<ChannelsBean> dataBeanXes) {
                        Log.e("获取电台返回数据",new GsonBuilder().serializeNulls().create().toJson(dataBeanXes));
                        mRecyclerView.setRefreshing(false);
                        if (dataBeanXes != null && dataBeanXes.size() > 0) {
                            if (datas != null) {
                                datas.addAll(0, dataBeanXes);
                                mHeaderAndFooterWrapper.notifyDataSetChanged();
                            } else {
                                datas = new ArrayList<>();
                                datas.addAll(0, dataBeanXes);
                                mHeaderAndFooterWrapper.notifyDataSetChanged();
                            }
                            loadLayout.showContentView();
                        } else {
                            if (datas != null && datas.size() > 0) {
                                loadLayout.showContentView();
                                ToastUtils.show_always(BSApplication.getInstance(), "没有新的推荐了！");
                            } else {
                                loadLayout.showEmptyView();
                            }
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        //                        mRecyclerView.setRefreshing(false);
                        if (datas != null && datas.size() > 0) {
                            loadLayout.showContentView();
                        } else {
                            loadLayout.showErrorView();
                        }
                    }
                });
    }


    private void loadMore() {
        RetrofitUtils.getInstance().getChannelsRadioHots("part")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<ChannelsBean>>() {
                    @Override
                    public void call(List<ChannelsBean> dataBeanXes) {
                        //                        mRecyclerView.setRefreshing(false);
                        if (dataBeanXes != null && !dataBeanXes.isEmpty()) {
                            datas.addAll(dataBeanXes);
                            mHeaderAndFooterWrapper.notifyDataSetChanged();
                            loadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                        } else {
                            loadMoreFooterView.setStatus(LoadMoreFooterView.Status.THE_END);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        if (datas != null && datas.size() > 0) {
                            loadLayout.showContentView();
                        } else {
                            loadLayout.showErrorView();
                        }
                        loadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                    }
                });
    }
}
