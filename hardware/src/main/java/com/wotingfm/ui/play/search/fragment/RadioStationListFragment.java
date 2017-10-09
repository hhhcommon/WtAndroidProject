package com.wotingfm.ui.play.search.fragment;

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
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.ui.adapter.findHome.RadioStationAdapter;
import com.wotingfm.ui.base.basefragment.BaseFragment;
import com.wotingfm.ui.bean.ChannelsBean;
import com.wotingfm.ui.bean.SerchList;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 电台
 */

public class RadioStationListFragment extends BaseFragment implements View.OnClickListener, OnLoadMoreListener, OnRefreshListener {

    @BindView(R.id.mRecyclerView)
    ARecyclerView mRecyclerView;
    @BindView(R.id.loadLayout)
    LoadFrameLayout loadLayout;

    private LoadMoreFooterView loadMoreFooterView;
    private String q;
    private View rootView;
    private int mPage;
    private RadioStationAdapter mAdapter;
    private List<ChannelsBean> albumsBeanList = new ArrayList<>();

    public static RadioStationListFragment newInstance(String q) {
        RadioStationListFragment fragment = new RadioStationListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("q", q);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_albums_list, container, false);
            rootView.setOnClickListener(this);
            ButterKnife.bind(this, rootView);
            inItView();
        }
        return rootView;
    }

    protected void inItView() {
        Bundle bundle = getArguments();
        if (bundle != null)
            q = bundle.getString("q");
        loadLayout.showLoadingView();
        loadMoreFooterView = (LoadMoreFooterView) mRecyclerView.getLoadMoreFooterView();
        mRecyclerView.setOnLoadMoreListener(this);
        mRecyclerView.setOnRefreshListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        loadLayout.findViewById(R.id.btnTryAgain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadLayout.showLoadingView();
                refresh(q);
            }
        });
        mAdapter = new RadioStationAdapter(getActivity(), albumsBeanList, new RadioStationAdapter.RadioStationClick() {
            @Override
            public void click(ChannelsBean dataBean) {
//                hideSoftKeyboard();
                startMain(dataBean);
                // RadioInfoActivity.start(getActivity(), dataBean.title, dataBean.id);
            }
        });
        mRecyclerView.setIAdapter(mAdapter);
        refresh(q);
    }

    public void refresh(String q) {
        Log.e("执行搜索：", "电台" + q);
        mPage = 1;
        this.q = q;
        RetrofitUtils.getInstance().serchList("radios", q, mPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<SerchList>() {
                    @Override
                    public void call(SerchList serchList) {
                        try {
                            Log.e("搜索电台返回数据", new GsonBuilder().serializeNulls().create().toJson(serchList));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        mRecyclerView.setRefreshing(false);
                        if (serchList != null && serchList.ret == 0 && serchList.data != null && serchList.data.radios != null && !serchList.data.radios.isEmpty()) {
                            mPage++;
                            albumsBeanList.clear();
                            albumsBeanList.addAll(serchList.data.radios);
                            loadLayout.showContentView();
                            mAdapter.notifyDataSetChanged();
                        } else {
                            if (albumsBeanList != null && albumsBeanList.size() > 0) {
                                loadLayout.showContentView();
                            } else {
                                loadLayout.showEmptyView();
                            }
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (albumsBeanList != null && albumsBeanList.size() > 0) {
                            loadLayout.showContentView();
                        } else {
                            loadLayout.showEmptyView();
                        }
                        throwable.printStackTrace();
                    }
                });

    }

    private void loadMore() {
        RetrofitUtils.getInstance().serchList("radios", q, mPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<SerchList>() {
                    @Override
                    public void call(SerchList serchList) {
                        mRecyclerView.setRefreshing(false);
                        if (serchList != null && serchList.ret == 0 && serchList.data != null && serchList.data.radios != null && !serchList.data.radios.isEmpty()) {
                            mPage++;
                            albumsBeanList.addAll(serchList.data.radios);
                            mAdapter.notifyDataSetChanged();
                            loadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                        } else {
                            if (albumsBeanList != null && albumsBeanList.size() > 0) {
                                loadLayout.showContentView();
                            } else {
                                loadLayout.showEmptyView();
                            }
                            loadMoreFooterView.setStatus(LoadMoreFooterView.Status.THE_END);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (albumsBeanList != null && albumsBeanList.size() > 0) {
                            loadLayout.showContentView();
                        } else {
                            loadLayout.showEmptyView();
                        }
                        loadMoreFooterView.setStatus(LoadMoreFooterView.Status.THE_END);
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
        refresh(q);
    }

    @Override
    public void onClick(View v) {

    }

}
