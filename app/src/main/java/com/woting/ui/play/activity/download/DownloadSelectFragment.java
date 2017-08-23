package com.woting.ui.play.activity.download;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.woting.commonplat.amine.ARecyclerView;
import com.woting.commonplat.amine.LoadMoreFooterView;
import com.woting.commonplat.amine.OnLoadMoreListener;
import com.woting.commonplat.amine.OnRefreshListener;
import com.woting.commonplat.widget.LoadFrameLayout;
import com.woting.R;
import com.woting.common.adapter.downloadAdapter.DownloadSelectAdapter;
import com.woting.common.bean.Player;
import com.woting.common.net.RetrofitUtils;
import com.woting.common.utils.DownloadUtils;
import com.woting.common.utils.T;
import com.woting.ui.base.basefragment.BaseFragment;
import com.woting.ui.play.activity.ReportsPlayerFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by amine on 2017/6/20.
 * 节目下载选择页面
 */

public class DownloadSelectFragment extends BaseFragment implements View.OnClickListener, OnLoadMoreListener, OnRefreshListener {
    @BindView(R.id.tvCancel)
    TextView tvCancel;
    @BindView(R.id.tvDownload)
    TextView tvDownload;
    @BindView(R.id.mRecyclerView)
    ARecyclerView mRecyclerView;
    @BindView(R.id.loadLayout)
    LoadFrameLayout loadLayout;
    @BindView(R.id.ivSelect)
    ImageView ivSelect;
    @BindView(R.id.largeLabelSelect)
    LinearLayout largeLabelSelect;


    private DownloadSelectAdapter downloadSelectAdapter;

    public static ReportsPlayerFragment newInstance(String albumsID) {
        ReportsPlayerFragment fragment = new ReportsPlayerFragment();
        Bundle bundle = new Bundle();
        bundle.putString("albumsID", albumsID);
        fragment.setArguments(bundle);
        return fragment;
    }


    private LoadMoreFooterView loadMoreFooterView;
    private String albumsID;

    @Override
    public void initView() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            albumsID = bundle.getString("albumsID");
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setNestedScrollingEnabled(false);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(layoutManager);
            loadMoreFooterView = (LoadMoreFooterView) mRecyclerView.getLoadMoreFooterView();
            downloadSelectAdapter = new DownloadSelectAdapter(getActivity(), singlesBeanList);
            downloadSelectAdapter.setPlayerClick(new DownloadSelectAdapter.AlbumsInfoClick() {
                @Override
                public void player(Player.DataBean.SinglesBean albumsBean, boolean isSelect, int postion) {
                    if (isSelect == true) {
                        singlesBeanListSelect.add(albumsBean);
                    } else {
                        singlesBeanListSelect.remove(albumsBean);
                    }
                }
            });
            mRecyclerView.setIAdapter(downloadSelectAdapter);
            mRecyclerView.setOnRefreshListener(this);
            mRecyclerView.setOnLoadMoreListener(this);
            largeLabelSelect.setOnClickListener(this);
            tvCancel.setOnClickListener(this);
            tvDownload.setOnClickListener(this);
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
        RetrofitUtils.getInstance().getProgramList(albumsID, mPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Player>() {
                    @Override
                    public void call(Player albumsBeen) {
                        mRecyclerView.setRefreshing(false);
                        if (albumsBeen != null && albumsBeen.data != null && albumsBeen.data.singles != null && !albumsBeen.data.singles.isEmpty()) {
                            mPage++;
                            singlesBeanList.clear();
                            singlesBeanList.addAll(albumsBeen.data.singles);
                            loadLayout.showContentView();
                            downloadSelectAdapter.setMore(isSelect);
                            largeLabelSelect.setVisibility(View.VISIBLE);
                            tvDownload.setVisibility(View.VISIBLE);
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
        RetrofitUtils.getInstance().getProgramList(albumsID, mPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Player>() {
                    @Override
                    public void call(Player albumsBeen) {
                        mRecyclerView.setRefreshing(false);
                        if (albumsBeen != null && albumsBeen.data != null && albumsBeen.data.singles != null && !albumsBeen.data.singles.isEmpty()) {
                            mPage++;
                            singlesBeanList.addAll(albumsBeen.data.singles);
                            downloadSelectAdapter.setMore(isSelect);
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


    private List<Player.DataBean.SinglesBean> singlesBeanList = new ArrayList<>();

    @Override
    public void onLoadMore(View loadMoreView) {
        if (loadMoreFooterView.canLoadMore() && downloadSelectAdapter.getItemCount() > 0) {
            loadMoreFooterView.setStatus(LoadMoreFooterView.Status.LOADING);
            loadMore();
        }
    }

    //控制选择的集合
    private List<Player.DataBean.SinglesBean> singlesBeanListSelect = new ArrayList<>();

    @Override
    public void onRefresh() {
        refresh();
    }

    private boolean isSelect = false;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvDownload:
                if (singlesBeanListSelect.isEmpty()) {
                    T.getInstance().showToast("请选择要下载的节目");
                    return;
                }
                T.getInstance().showToast("开始下载");
                for (int w = 0, size = singlesBeanListSelect.size(); w < size; w++) {
                    DownloadUtils.downloadManger(singlesBeanListSelect.get(w));
                }
                closeFragment();
                break;
            case R.id.tvCancel:
                closeFragment();
                break;
            case R.id.largeLabelSelect:
                for (int i = 0, size = singlesBeanList.size(); i < size; i++) {
                    Player.DataBean.SinglesBean s = singlesBeanList.get(i);
                    s.isSelect = !isSelect;
                    singlesBeanList.set(i, s);
                }
                if (isSelect == true) {
                    singlesBeanListSelect.clear();
                    singlesBeanListSelect.addAll(singlesBeanList);
                    ivSelect.setImageResource(R.mipmap.icon_select_n);
                    isSelect = false;
                } else {
                    singlesBeanListSelect.clear();
                    isSelect = true;
                    ivSelect.setImageResource(R.mipmap.icon_select_s);
                }
                downloadSelectAdapter.setMore(isSelect);
                break;
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_download_select;
    }
}