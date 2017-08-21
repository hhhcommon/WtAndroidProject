package com.wotingfm.ui.play.look.activity.serch.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.woting.commonplat.amine.ARecyclerView;
import com.woting.commonplat.amine.LoadMoreFooterView;
import com.woting.commonplat.amine.OnLoadMoreListener;
import com.woting.commonplat.amine.OnRefreshListener;
import com.woting.commonplat.widget.LoadFrameLayout;
import com.wotingfm.R;
import com.wotingfm.common.adapter.albumsAdapter.AlbumsAdapter;
import com.wotingfm.common.bean.AlbumsBean;
import com.wotingfm.common.bean.SerchList;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.ui.base.basefragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by amine on 2017/6/14.
 * 专辑列表
 * <p>
 * 筛选的
 */

public class AlbumsListFragment extends BaseFragment implements OnLoadMoreListener, OnRefreshListener {
    @BindView(R.id.mRecyclerView)
    ARecyclerView mRecyclerView;
    @BindView(R.id.loadLayout)
    LoadFrameLayout loadLayout;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_albums_list;
    }

    public static AlbumsListFragment newInstance(String q) {
        AlbumsListFragment fragment = new AlbumsListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("q", q);
        fragment.setArguments(bundle);
        return fragment;
    }

    private LoadMoreFooterView loadMoreFooterView;
    private String q;

    @Override
    protected void initView() {
        Bundle bundle = getArguments();
        if (bundle != null)
            q = bundle.getString("q");
        loadLayout.showLoadingView();
        loadMoreFooterView = (LoadMoreFooterView) mRecyclerView.getLoadMoreFooterView();
        mRecyclerView.setOnLoadMoreListener(this);
        mRecyclerView.setOnRefreshListener(this);
        loadLayout.findViewById(R.id.btnTryAgain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadLayout.showLoadingView();
                refresh(q);
            }
        });
        mAdapter = new AlbumsAdapter(getActivity(), albumsBeanList);
        mAdapter.setPlayerClick(new AlbumsAdapter.PlayerClick() {
            @Override
            public void clickAlbums(AlbumsBean singlesBean) {
                hideSoftKeyboard();
                startMain(singlesBean.id);
            }
        });
        mRecyclerView.setIAdapter(mAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        refresh(q);
    }

    private int mPage;
    private AlbumsAdapter mAdapter;
    private List<AlbumsBean> albumsBeanList = new ArrayList<>();

    public void refresh(String q) {
        mPage = 1;
        this.q=q;
        RetrofitUtils.getInstance().serchList("albums", q, mPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<SerchList>() {
                    @Override
                    public void call(SerchList serchList) {
                        mRecyclerView.setRefreshing(false);
                        if (serchList != null && serchList.ret == 0 && serchList.data != null && serchList.data.albums != null && !serchList.data.albums.isEmpty()) {
                            mPage++;
                            albumsBeanList.clear();
                            albumsBeanList.addAll(serchList.data.albums);
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
        RetrofitUtils.getInstance().serchList("albums", q, mPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<SerchList>() {
                    @Override
                    public void call(SerchList serchList) {
                        mRecyclerView.setRefreshing(false);
                        if (serchList != null && serchList.ret == 0 && serchList.data != null && serchList.data.albums != null && !serchList.data.albums.isEmpty()) {
                            mPage++;
                            albumsBeanList.addAll(serchList.data.albums);
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
        refresh(q);
    }
}
