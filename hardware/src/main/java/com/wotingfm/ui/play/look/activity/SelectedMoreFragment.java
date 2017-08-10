package com.wotingfm.ui.play.look.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.woting.commonplat.amine.ARecyclerView;
import com.woting.commonplat.amine.LoadMoreFooterView;
import com.woting.commonplat.amine.OnLoadMoreListener;
import com.woting.commonplat.amine.OnRefreshListener;
import com.woting.commonplat.widget.LoadFrameLayout;
import com.wotingfm.R;
import com.wotingfm.common.adapter.albumsAdapter.AlbumsAdapter;
import com.wotingfm.common.adapter.findHome.ItemSelected1Adapter;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.bean.AlbumsBean;
import com.wotingfm.common.bean.Player;
import com.wotingfm.common.bean.Selected;
import com.wotingfm.common.bean.SelectedMore;
import com.wotingfm.common.bean.Subscrible;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.ui.base.baseactivity.BaseToolBarActivity;
import com.wotingfm.ui.base.basefragment.BaseFragment;
import com.wotingfm.ui.play.activity.AnchorPersonalCenterFragment;
import com.wotingfm.ui.play.radio.ProvincesAndCitiesListRadioFragment;
import com.wotingfm.ui.test.PlayerActivity;
import com.wotingfm.ui.test.PlayerFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static android.R.attr.type;

/**
 * Created by amine on 2017/6/22.
 * 精选，每日  列表，
 */

public class SelectedMoreFragment extends BaseFragment implements OnLoadMoreListener, OnRefreshListener {

    @BindView(R.id.mRecyclerView)
    ARecyclerView mRecyclerView;
    @BindView(R.id.loadLayout)
    LoadFrameLayout loadLayout;

    public static SelectedMoreFragment newInstance(String type, String title) {
        SelectedMoreFragment fragment = new SelectedMoreFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }


    private String type, title;
    private LoadMoreFooterView loadMoreFooterView;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_selected;
    }

    @Override
    public void initView() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            title = bundle.getString("title");
            type = bundle.getString("type");
            setTitle(title + "台");
            setTitle(title);
            GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            loadMoreFooterView = (LoadMoreFooterView) mRecyclerView.getLoadMoreFooterView();
            mRecyclerView.setOnLoadMoreListener(this);
            mRecyclerView.setOnRefreshListener(this);
            mRecyclerView.setLayoutManager(layoutManager);
            mAdapter = new ItemSelected1Adapter(getActivity(), datas, new ItemSelected1Adapter.SelectedClick() {
                @Override
                public void click(AlbumsBean dataBean) {
                    startMain(dataBean.id);
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

    private ItemSelected1Adapter mAdapter;
    private List<AlbumsBean> datas = new ArrayList<>();
    private int mPage;

    private void refresh() {
        mPage = 1;
        RetrofitUtils.getInstance().getSelectedsMore(mPage, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<AlbumsBean>>() {
                    @Override
                    public void call(List<AlbumsBean> albumsBeen) {
                        mRecyclerView.setRefreshing(false);
                        if (albumsBeen != null && !albumsBeen.isEmpty()) {
                            mPage++;
                            datas.clear();
                            datas.addAll(albumsBeen);
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
        RetrofitUtils.getInstance().getSelectedsMore(mPage, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<AlbumsBean>>() {
                    @Override
                    public void call(List<AlbumsBean> albumsBeen) {
                        mRecyclerView.setRefreshing(false);
                        if (albumsBeen != null && !albumsBeen.isEmpty()) {
                            mPage++;
                            datas.addAll(albumsBeen);
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
