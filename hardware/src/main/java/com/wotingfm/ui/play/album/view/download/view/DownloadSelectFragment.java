package com.wotingfm.ui.play.album.view.download.view;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
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
import com.wotingfm.common.utils.DialogUtils;
import com.wotingfm.ui.base.basefragment.BaseFragment;
import com.wotingfm.ui.play.album.view.download.adapter.DownloadSelectAdapter;
import com.wotingfm.ui.bean.Player;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;
import com.wotingfm.ui.mine.main.MineActivity;
import com.wotingfm.ui.play.album.view.download.presenter.DownloadSelectPresenter;
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

public class DownloadSelectFragment extends BaseFragment implements View.OnClickListener, OnLoadMoreListener, OnRefreshListener {
    @BindView(R.id.tvCancel)
    TextView tvCancel;
    @BindView(R.id.tvDownload)
    TextView tvDownload;
    @BindView(R.id.tv_news)
    TextView tv_news;
    @BindView(R.id.mRecyclerView)
    ARecyclerView mRecyclerView;
    @BindView(R.id.loadLayout)
    LoadFrameLayout loadLayout;
    @BindView(R.id.ivSelect)
    ImageView ivSelect;
    @BindView(R.id.largeLabelSelect)
    LinearLayout largeLabelSelect;

    private LoadMoreFooterView loadMoreFooterView;
    private DownloadSelectAdapter downloadSelectAdapter;
    private View rootView;
    private Dialog dialog;
    private int mPage;
    private DownloadSelectPresenter presenter;
    private boolean isSelect = false;
    private List<Player.DataBean.SinglesBean> singlesBeanList = new ArrayList<>();
    private List<Player.DataBean.SinglesBean> list;

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
            presenter = new DownloadSelectPresenter(this);
            loadLayout.findViewById(R.id.btnTryAgain).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showLoadingView();
                    refresh(presenter.getId());
                }
            });
        }
        return rootView;
    }

    public void inItView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setNestedScrollingEnabled(false);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        loadMoreFooterView = (LoadMoreFooterView) mRecyclerView.getLoadMoreFooterView();
        downloadSelectAdapter = new DownloadSelectAdapter(getActivity(), singlesBeanList);
        downloadSelectAdapter.setPlayerClick(new DownloadSelectAdapter.AlbumsInfoClick() {
            @Override
            public void itemClick(int position) {
                if (singlesBeanList.get(position - 2).isSelect) {
                    singlesBeanList.get(position - 2).isSelect = false;
                } else {
                    singlesBeanList.get(position - 2).isSelect = true;
                }

                downloadSelectAdapter.notifyDataSetChanged();
            }
        });
        mRecyclerView.setIAdapter(downloadSelectAdapter);
//        mRecyclerView.setOnRefreshListener(this);
        mRecyclerView.setRefreshEnabled(false);
        mRecyclerView.setRefreshing(false);
        mRecyclerView.setOnLoadMoreListener(this);
        largeLabelSelect.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
        tvDownload.setOnClickListener(this);
    }


    public void refresh(String albumsID) {
        mPage = 1;
        RetrofitUtils.getInstance().getProgramList(albumsID, mPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Player>() {
                    @Override
                    public void call(Player albumsBeen) {
                        showRecycleView(1);
                        if (albumsBeen != null && albumsBeen.data != null && albumsBeen.data.singles != null && !albumsBeen.data.singles.isEmpty()) {
                            mPage++;
                            singlesBeanList.clear();
                            list = albumsBeen.data.singles;
                            singlesBeanList.addAll(list);
                            assembleData();
                            setData();
                            showContentView();
                        } else {
                            showEmptyView();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (singlesBeanList != null && singlesBeanList.size() > 0) {
                            showContentView();
                        } else {
                            showErrorView();
                        }
                        throwable.printStackTrace();
                    }
                });

    }

    // 组装数据
    private void assembleData() {
        // 先设置为未选中
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                list.get(i).isSelect = false;
            }
        }
        // 判断是否下载
        if (presenter.getDList() != null && presenter.getDList().size() > 0) {
            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    String id = list.get(i).id;
                    if (!TextUtils.isEmpty(id)) {
                        for (int j = 0; j < presenter.getDList().size(); j++) {
                            String _id = presenter.getDList().get(j).id;
                            if (!TextUtils.isEmpty(_id)) {
                                if (id.trim().equals(_id)) {
                                    list.get(i).isDownloadOver = true;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void loadMore() {
        RetrofitUtils.getInstance().getProgramList(presenter.getId(), mPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Player>() {
                    @Override
                    public void call(Player albumsBeen) {
                        showRecycleView(1);
                        if (albumsBeen != null && albumsBeen.data != null && albumsBeen.data.singles != null && !albumsBeen.data.singles.isEmpty()) {
                            mPage++;
                            list = albumsBeen.data.singles;
                            singlesBeanList.addAll(list);
                            assembleData();
                            setData();
                            showRecycleView(2);
                        } else {
                            showRecycleView(3);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (singlesBeanList != null && singlesBeanList.size() > 0) {
                            showContentView();
                        } else {
                            showErrorView();
                        }
                        showRecycleView(2);
                        throwable.printStackTrace();
                    }
                });
    }

    /**
     * 设置数据
     */
    public void setData() {
        downloadSelectAdapter.notifyDataSetChanged();
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
        refresh(presenter.getId());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvDownload:
                boolean b = presenter.downLoad(singlesBeanList);
                if (b) closeFragment();
                break;
            case R.id.tvCancel:
                closeFragment();
                break;
            case R.id.largeLabelSelect:
                // 全选，取消全选
                select();
                break;
        }
    }

    private void select() {
        if (isSelect) {
            // 当前是全选状态
            isSelect = false;
            for (int i = 0; i < singlesBeanList.size(); i++) {
                singlesBeanList.get(i).isSelect = false;
            }
            setData();
            ivSelect.setImageResource(R.mipmap.icon_select_n);
            tv_news.setText("取消全选");
        } else {
            // 当前不是全选状态
            isSelect = true;
            for (int i = 0; i < singlesBeanList.size(); i++) {
                singlesBeanList.get(i).isSelect = true;
            }
            setData();
            ivSelect.setImageResource(R.mipmap.icon_select_s);
            tv_news.setText("全选本页");
        }
    }



    public void showContentView() {
        loadLayout.showContentView();
    }

    public void showEmptyView() {
        loadLayout.showEmptyView();
    }

    public void showLoadingView() {
        loadLayout.showLoadingView();
    }

    public void showErrorView() {
        loadLayout.showErrorView();
    }


    public void showRecycleView(int type) {
        if (type == 1) {
            mRecyclerView.setRefreshing(false);
        } else if (type == 2) {
            loadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
        } else if (type == 3) {
            loadMoreFooterView.setStatus(LoadMoreFooterView.Status.THE_END);
        }
    }


    /**
     * 展示弹出框
     */
    public void dialogShow() {
        dialog = DialogUtils.Dialog(this.getActivity());
    }

    /**
     * 取消弹出框
     */
    public void dialogCancel() {
        if (dialog != null) dialog.dismiss();
    }

}
