package com.wotingfm.ui.play.look.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.woting.commonplat.amine.ARecyclerView;
import com.woting.commonplat.amine.LoadMoreFooterView;
import com.woting.commonplat.amine.OnLoadMoreListener;
import com.woting.commonplat.amine.OnRefreshListener;
import com.woting.commonplat.widget.LoadFrameLayout;
import com.wotingfm.R;
import com.wotingfm.common.adapter.findHome.RadioStationAdapter;
import com.wotingfm.common.bean.ChannelsBean;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.ui.base.basefragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by amine on 2017/6/22.
 * 精选，每日  列表，
 */

public class RadioMoreFragment extends BaseFragment implements OnLoadMoreListener, OnRefreshListener {

    @BindView(R.id.mRecyclerView)
    ARecyclerView mRecyclerView;
    @BindView(R.id.loadLayout)
    LoadFrameLayout loadLayout;

    public static RadioMoreFragment newInstance() {
        RadioMoreFragment fragment = new RadioMoreFragment();
        return fragment;
    }

    private LoadMoreFooterView loadMoreFooterView;
    private RadioStationAdapter selectedAdapter;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_radio_more;
    }

    @Override
    public void initView() {
        setTitle("热门电台");
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        loadMoreFooterView = (LoadMoreFooterView) mRecyclerView.getLoadMoreFooterView();
        mRecyclerView.setOnLoadMoreListener(this);
        mRecyclerView.setOnRefreshListener(this);
        mRecyclerView.setLayoutManager(layoutManager);
        selectedAdapter = new RadioStationAdapter(getActivity(), datas, new RadioStationAdapter.RadioStationClick() {
            @Override
            public void click(ChannelsBean dataBean) {
                startMain(dataBean);
            }
        });
        mRecyclerView.setIAdapter(selectedAdapter);
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

    private List<ChannelsBean> datas = new ArrayList<>();
    private int mPage;

    private void refresh() {
        mPage = 1;
        RetrofitUtils.getInstance().getChannelsRadioList("all", mPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<ChannelsBean>>() {
                    @Override
                    public void call(List<ChannelsBean> dataBeanXes) {
                        mRecyclerView.setRefreshing(false);
                        loadLayout.showContentView();
                        datas.clear();
                        datas.addAll(dataBeanXes);
                        selectedAdapter.notifyDataSetChanged();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        loadLayout.showErrorView();
                    }
                });
    }

    private void loadMore() {
        RetrofitUtils.getInstance().getChannelsRadioList("all", mPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<ChannelsBean>>() {
                    @Override
                    public void call(List<ChannelsBean> dataBeanXes) {
                        mRecyclerView.setRefreshing(false);
                        if (dataBeanXes != null && !dataBeanXes.isEmpty()) {
                            mPage++;
                            datas.addAll(dataBeanXes);
                            selectedAdapter.notifyDataSetChanged();
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
        if (loadMoreFooterView.canLoadMore() && selectedAdapter.getItemCount() > 0) {
            loadMoreFooterView.setStatus(LoadMoreFooterView.Status.LOADING);
            loadMore();
        }
    }

    @Override
    public void onRefresh() {
        refresh();
    }
}
