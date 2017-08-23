package com.woting.ui.play.activity.albums;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.woting.commonplat.amine.ARecyclerView;
import com.woting.commonplat.amine.LoadMoreFooterView;
import com.woting.commonplat.amine.OnLoadMoreListener;
import com.woting.commonplat.amine.OnRefreshListener;
import com.woting.commonplat.widget.LoadFrameLayout;
import com.woting.R;
import com.woting.common.adapter.albumsAdapter.AlbumsAdapter;
import com.woting.common.bean.AlbumsBean;
import com.woting.common.net.RetrofitUtils;
import com.woting.ui.base.basefragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 个人专辑列表
 * Created by amine on 2017/6/13.
 */

public class AlbumsListMeFragment extends BaseFragment implements OnLoadMoreListener, OnRefreshListener {
    @BindView(R.id.mRecyclerView)
    ARecyclerView mRecyclerView;
    @BindView(R.id.loadLayout)
    LoadFrameLayout loadLayout;

    private AlbumsAdapter mAdapter;

    public static AlbumsListMeFragment newInstance(String uid, String title) {
        AlbumsListMeFragment fragment = new AlbumsListMeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("uid", uid);
        bundle.putString("title", title);
        fragment.setArguments(bundle);
        return fragment;
    }


    private LoadMoreFooterView loadMoreFooterView;
    private List<AlbumsBean> albumsBeanList = new ArrayList<>();

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_albums_list;
    }

    @Override
    public void initView() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            user_id = bundle.getString("uid");
            setTitle(bundle.getString("title"));
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            loadMoreFooterView = (LoadMoreFooterView) mRecyclerView.getLoadMoreFooterView();
            mRecyclerView.setOnLoadMoreListener(this);
            mRecyclerView.setOnRefreshListener(this);
            mRecyclerView.setLayoutManager(layoutManager);
            mAdapter = new AlbumsAdapter(getActivity(), albumsBeanList);
            mAdapter.setPlayerClick(new AlbumsAdapter.PlayerClick() {
                @Override
                public void clickAlbums(AlbumsBean singlesBean) {
                    startMain(singlesBean.id);
                   // PlayerActivity.start(getActivity(), singlesBean.id);
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
    private String user_id;

    private void refresh() {
        mPage = 1;
        RetrofitUtils.getInstance().getAlbumsList(user_id, mPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<AlbumsBean>>() {
                    @Override
                    public void call(List<AlbumsBean> albumsBeen) {
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
        RetrofitUtils.getInstance().getAlbumsList(user_id, mPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<AlbumsBean>>() {
                    @Override
                    public void call(List<AlbumsBean> albumsBeen) {
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