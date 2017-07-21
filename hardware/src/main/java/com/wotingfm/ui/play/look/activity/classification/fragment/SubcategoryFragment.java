package com.wotingfm.ui.play.look.activity.classification.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.woting.commonplat.amine.ARecyclerView;
import com.woting.commonplat.amine.LoadMoreFooterView;
import com.woting.commonplat.amine.OnLoadMoreListener;
import com.woting.commonplat.amine.OnRefreshListener;
import com.woting.commonplat.widget.LoadFrameLayout;
import com.wotingfm.R;
import com.wotingfm.common.adapter.albumsAdapter.AlbumsAdapter;
import com.wotingfm.common.adapter.downloadAdapter.DownloadSelectAdapter;
import com.wotingfm.common.adapter.findHome.ClassificationAdapter;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.bean.AlbumInfo;
import com.wotingfm.common.bean.AlbumsBean;
import com.wotingfm.common.bean.Channels;
import com.wotingfm.common.bean.ChannelsBean;
import com.wotingfm.common.bean.Classification;
import com.wotingfm.common.bean.Player;
import com.wotingfm.common.bean.Subscrible;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.common.utils.T;
import com.wotingfm.ui.base.basefragment.BaseFragment;
import com.wotingfm.ui.play.activity.AnchorPersonalCenterFragment;
import com.wotingfm.ui.play.activity.albums.fragment.AlbumsInfoFragment;
import com.wotingfm.ui.test.PlayerActivity;
import com.wotingfm.ui.test.PlayerFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;
import static com.wotingfm.R.id.largeLabelSelect;
import static com.wotingfm.R.id.tvCancel;
import static com.wotingfm.R.id.tvDownload;

/**
 * Created by amine on 2017/6/14.
 * 发现分类
 * 子分类
 */

public class SubcategoryFragment extends BaseFragment implements OnLoadMoreListener, OnRefreshListener {


    @BindView(R.id.mRecyclerView)
    ARecyclerView mRecyclerView;
    @BindView(R.id.loadLayout)
    LoadFrameLayout loadLayout;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_subcategory;
    }

    public static SubcategoryFragment newInstance(ChannelsBean albumInfo,String id, String title) {
        SubcategoryFragment fragment = new SubcategoryFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("albumInfo", albumInfo);
        bundle.putString("title", title);
        bundle.putString("id", id);
        fragment.setArguments(bundle);
        return fragment;
    }


    private LoadMoreFooterView loadMoreFooterView;
    private List<AlbumsBean> albumsBeanList = new ArrayList<>();
    private AlbumsAdapter mAdapter;
    private ChannelsBean albumInfo;

    @Override
    public void initView() {
        final Bundle bundle = getArguments();
        if (bundle != null)
            albumInfo = (ChannelsBean) bundle.getSerializable("albumInfo");
        if (albumInfo == null)
            return;
        final String id  = bundle.getString("id");
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
                BSApplication.IS_RESULT = true;
                BSApplication.isIS_BACK = true;
                openFragment(PlayerFragment.newInstanceSerch(singlesBean.id,id, bundle.getString("title")));
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
        refresh();
    }

    private int mPage;

    private void refresh() {
        mPage = 1;
        RetrofitUtils.getInstance().getChannelsAlbums(albumInfo.id, mPage)
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
        RetrofitUtils.getInstance().getChannelsAlbums(albumInfo.id, mPage)
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
