package com.wotingfm.ui.play.search.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.ui.adapter.albumsAdapter.AlbumsAdapter;
import com.wotingfm.ui.base.basefragment.BaseFragment;
import com.wotingfm.ui.bean.AlbumsBean;
import com.wotingfm.ui.bean.MessageEvent;
import com.wotingfm.ui.bean.SerchList;
import com.wotingfm.ui.mine.main.MineActivity;
import com.wotingfm.ui.play.album.main.view.AlbumsInfoMainFragment;
import com.wotingfm.ui.play.find.main.view.LookListActivity;
import com.wotingfm.ui.play.main.PlayerActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 专辑列表
 */

public class AlbumsListFragment extends BaseFragment implements View.OnClickListener, OnLoadMoreListener, OnRefreshListener {
    @BindView(R.id.mRecyclerView)
    ARecyclerView mRecyclerView;
    @BindView(R.id.loadLayout)
    LoadFrameLayout loadLayout;
    private View rootView;

    private LoadMoreFooterView loadMoreFooterView;
    private String q;

    public static AlbumsListFragment newInstance(String q) {
        AlbumsListFragment fragment = new AlbumsListFragment();
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
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        loadMoreFooterView = (LoadMoreFooterView) mRecyclerView.getLoadMoreFooterView();
        mRecyclerView.setOnLoadMoreListener(this);
        mRecyclerView.setOnRefreshListener(this);
        mRecyclerView.setLayoutManager(layoutManager);
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
                openFragment(AlbumsInfoMainFragment.newInstance(singlesBean.id));
            }
            @Override
            public void play(AlbumsBean singlesBean) {
//                hideSoftKeyboard();
                startMain(singlesBean.id);
            }
        });
        mRecyclerView.setIAdapter(mAdapter);

        refresh(q);
    }

    private int mPage;
    private AlbumsAdapter mAdapter;
    private List<AlbumsBean> albumsBeanList = new ArrayList<>();

    public void refresh(String q) {
        Log.e("执行搜索：", "专辑" + q);
        mPage = 1;
        this.q=q;
        RetrofitUtils.getInstance().serchList("albums", q, mPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<SerchList>() {
                    @Override
                    public void call(SerchList serchList) {
                        try {
                            Log.e("搜索专辑返回数据", new GsonBuilder().serializeNulls().create().toJson(serchList));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        mRecyclerView.setRefreshing(false);
                        if (serchList != null && serchList.ret == 0 && serchList.data != null && serchList.data.albums != null && !serchList.data.albums.isEmpty()) {
                            mPage++;
                            albumsBeanList.clear();
                            albumsBeanList.addAll(serchList.data.albums);
                            loadLayout.showContentView();
                            mAdapter.notifyDataSetChanged();
                        } else {
                            if(albumsBeanList!=null&&albumsBeanList.size()>0){
                                loadLayout.showContentView();
                            }else{
                                loadLayout.showEmptyView();
                            }
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if(albumsBeanList!=null&&albumsBeanList.size()>0){
                            loadLayout.showContentView();
                        }else{
                            loadLayout.showEmptyView();
                        }
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
                            if(albumsBeanList!=null&&albumsBeanList.size()>0){
                                loadLayout.showContentView();
                            }else{
                                loadLayout.showEmptyView();
                            }
                            loadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if(albumsBeanList!=null&&albumsBeanList.size()>0){
                            loadLayout.showContentView();
                        }else{
                            loadLayout.showEmptyView();
                        }
                        loadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
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

    public void startMain(String albumsId) {
        GlobalStateConfig.activityA = "A";
        EventBus.getDefault().post(new MessageEvent("one"));
        EventBus.getDefault().post(new MessageEvent("stop&" + albumsId));
    }

    @Override
    public void onRefresh() {
        refresh(q);
    }

    @Override
    public void onClick(View v) {

    }
}
