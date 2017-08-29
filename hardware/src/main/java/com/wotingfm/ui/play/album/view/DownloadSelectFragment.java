package com.wotingfm.ui.play.album.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.woting.commonplat.amine.ARecyclerView;
import com.woting.commonplat.amine.LoadMoreFooterView;
import com.woting.commonplat.amine.OnLoadMoreListener;
import com.woting.commonplat.amine.OnRefreshListener;
import com.woting.commonplat.widget.LoadFrameLayout;
import com.wotingfm.R;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.common.utils.DownloadUtils;
import com.wotingfm.common.utils.T;
import com.wotingfm.ui.adapter.downloadAdapter.DownloadSelectAdapter;
import com.wotingfm.ui.bean.Player;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;
import com.wotingfm.ui.mine.main.MineActivity;
import com.wotingfm.ui.play.find.main.view.LookListActivity;
import com.wotingfm.ui.play.main.PlayerActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by amine on 2017/6/20.
 * 节目下载选择页面
 */

public class DownloadSelectFragment extends Fragment implements View.OnClickListener, OnLoadMoreListener, OnRefreshListener {
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

    private LoadMoreFooterView loadMoreFooterView;
    private String albumsID;
    private DownloadSelectAdapter downloadSelectAdapter;
    private View rootView;
    private int mPage;
    private boolean isSelect = false;
    private List<Player.DataBean.SinglesBean> singlesBeanList = new ArrayList<>();
    //控制选择的集合
    private List<Player.DataBean.SinglesBean> singlesBeanListSelect = new ArrayList<>();

    public static DownloadSelectFragment newInstance(String albumsID) {
        DownloadSelectFragment fragment = new DownloadSelectFragment();
        Bundle bundle = new Bundle();
        bundle.putString("albumsID", albumsID);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.activity_download_select, container, false);
            rootView.setOnClickListener(this);
            ButterKnife.bind(this, rootView);
            inItView();
        }
        return rootView;
    }

    public void inItView() {
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

    @Override
    public void onLoadMore(View loadMoreView) {
        if (loadMoreFooterView.canLoadMore() && downloadSelectAdapter.getItemCount() > 0) {
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

    /**
     * 关闭界面
     */
    public void closeFragment() {
        if (this.getActivity() instanceof PlayerActivity) {
            PlayerActivity.close();
        } else if (this.getActivity() instanceof MineActivity) {
            MineActivity.close();
        } else if (this.getActivity() instanceof InterPhoneActivity) {
            InterPhoneActivity.close();
        } else if (this.getActivity() instanceof LookListActivity) {
            LookListActivity.close();
        }
    }

}
