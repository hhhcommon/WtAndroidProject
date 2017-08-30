package com.wotingfm.ui.play.find.classification.minorclassification;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.woting.commonplat.amine.ARecyclerView;
import com.woting.commonplat.amine.LoadMoreFooterView;
import com.woting.commonplat.amine.OnLoadMoreListener;
import com.woting.commonplat.amine.OnRefreshListener;
import com.woting.commonplat.widget.LoadFrameLayout;
import com.wotingfm.R;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.ui.adapter.albumsAdapter.AlbumsAdapter;
import com.wotingfm.ui.bean.AlbumsBean;
import com.wotingfm.ui.bean.ChannelsBean;
import com.wotingfm.ui.bean.MessageEvent;
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
 * 发现分类
 * 子分类
 */

public class SubcategoryFragment extends Fragment implements View.OnClickListener, OnLoadMoreListener, OnRefreshListener {

    @BindView(R.id.mRecyclerView)
    ARecyclerView mRecyclerView;
    @BindView(R.id.loadLayout)
    LoadFrameLayout loadLayout;

    private View rootView;
    private LoadMoreFooterView loadMoreFooterView;
    private List<AlbumsBean> albumsBeanList = new ArrayList<>();
    private AlbumsAdapter mAdapter;
    private ChannelsBean albumInfo;
    private int mPage;

    public static SubcategoryFragment newInstance(ChannelsBean albumInfo, String id, String title) {
        SubcategoryFragment fragment = new SubcategoryFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("albumInfo", albumInfo);
        bundle.putString("title", title);
        bundle.putString("id", id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_subcategory, container, false);
            rootView.setOnClickListener(this);
            ButterKnife.bind(this, rootView);
            inItView();
        }
        return rootView;
    }

    public void inItView() {
        final Bundle bundle = getArguments();
        if (bundle != null)
            albumInfo = (ChannelsBean) bundle.getSerializable("albumInfo");
        if (albumInfo == null)
            return;
        final String id = bundle.getString("id");
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
                if (getActivity() instanceof PlayerActivity) {
                    PlayerActivity.open(AlbumsInfoMainFragment.newInstance(singlesBean.id));
                } else if (getActivity() instanceof MineActivity) {
                    MineActivity.open(AlbumsInfoMainFragment.newInstance(singlesBean.id));
                } else if (getActivity() instanceof LookListActivity) {
                    LookListActivity.open(AlbumsInfoMainFragment.newInstance(singlesBean.id));
                }
            }

            @Override
            public void play(AlbumsBean singlesBean) {
                startMain(singlesBean.id);
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

    @Override
    public void onClick(View v) {

    }

    public void startMain(String albumsId) {
        GlobalStateConfig.activityA = "A";
        EventBus.getDefault().post(new MessageEvent("one"));
        EventBus.getDefault().post(new MessageEvent("stop&" + albumsId));
    }
}
