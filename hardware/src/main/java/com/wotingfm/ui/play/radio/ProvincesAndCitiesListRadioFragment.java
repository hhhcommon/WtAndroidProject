package com.wotingfm.ui.play.radio;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.woting.commonplat.amine.ARecyclerView;
import com.woting.commonplat.amine.LoadMoreFooterView;
import com.woting.commonplat.amine.OnLoadMoreListener;
import com.woting.commonplat.amine.OnRefreshListener;
import com.woting.commonplat.widget.LoadFrameLayout;
import com.wotingfm.R;
import com.wotingfm.common.adapter.radioAdapter.RadioAdapter;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.bean.ChannelsBean;
import com.wotingfm.common.bean.Radio;
import com.wotingfm.common.bean.RadioInfo;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.ui.base.baseactivity.BaseToolBarActivity;
import com.wotingfm.ui.base.basefragment.BaseFragment;
import com.wotingfm.ui.play.radio.fragment.RadioInfoTodayFragment;
import com.wotingfm.ui.test.PlayerActivity;
import com.wotingfm.ui.test.PlayerFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by amine on 2017/7/6.
 * 国家台
 */

public class ProvincesAndCitiesListRadioFragment extends BaseFragment implements OnLoadMoreListener, OnRefreshListener {
    @BindView(R.id.mRecyclerView)
    ARecyclerView mRecyclerView;
    @BindView(R.id.loadLayout)
    LoadFrameLayout loadLayout;


    public static ProvincesAndCitiesListRadioFragment newInstance(String title) {
        ProvincesAndCitiesListRadioFragment fragment = new ProvincesAndCitiesListRadioFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("title", title);
        fragment.setArguments(bundle);
        return fragment;
    }

    private LoadMoreFooterView loadMoreFooterView;
    private RadioAdapter mAdapter;
    private List<ChannelsBean> albumsBeanList = new ArrayList<>();

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_provinces_list_radio;
    }

    @Override
    public void initView() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            final String title = bundle.getString("title");
            setTitle(title + "台");
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            loadMoreFooterView = (LoadMoreFooterView) mRecyclerView.getLoadMoreFooterView();
            mRecyclerView.setOnLoadMoreListener(this);
            mRecyclerView.setOnRefreshListener(this);
            mRecyclerView.setLayoutManager(layoutManager);
            mAdapter = new RadioAdapter(getActivity(), albumsBeanList, new RadioAdapter.RadioClick() {
                @Override
                public void clickAlbums(ChannelsBean singlesBean) {
                    // RadioInfoActivity.start(ProvincesAndCitiesListRadioActivity.this, title, singlesBean.id);
                    openFragment(PlayerFragment.newInstance(singlesBean));
                }
            });
            mRecyclerView.setIAdapter(mAdapter);
            loadLayout.findViewById(R.id.btnTryAgain).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadLayout.showLoadingView();
                    refresh();
                }
            });
            loadLayout.showLoadingView();
            refresh();
        }
    }

    private int mPage;


    private void refresh() {
        mPage = 1;
        RetrofitUtils.getInstance().getChannelsRadio("provinces", mPage)
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
                            loadLayout.showEmptyView();
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

    private void loadMore() {
        RetrofitUtils.getInstance().getChannelsRadio("provinces", mPage)
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
