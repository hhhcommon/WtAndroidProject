package com.wotingfm.ui.play.look.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.woting.commonplat.widget.LoadFrameLayout;
import com.wotingfm.R;
import com.wotingfm.common.adapter.findHome.ClassificationAdapter;
import com.wotingfm.common.adapter.findHome.SelectedAdapter;
import com.wotingfm.common.bean.Classification;
import com.wotingfm.common.bean.Player;
import com.wotingfm.common.bean.SinglesBase;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.common.utils.T;
import com.wotingfm.common.view.BannerView;
import com.wotingfm.ui.base.basefragment.BaseFragment;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.wotingfm.R.id.relatiBottom;

/**
 * Created by amine on 2017/6/14.
 * 发现分类
 */

public class ClassificationFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.id_swipe_ly)
    SwipeRefreshLayout mSwipeLayout;
    @BindView(R.id.loadLayout)
    LoadFrameLayout loadLayout;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_classification;
    }

    public static ClassificationFragment newInstance() {
        ClassificationFragment fragment = new ClassificationFragment();
        return fragment;
    }

    private ClassificationAdapter mAdapter;

    @Override
    protected void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mSwipeLayout.setOnRefreshListener(this);
        mAdapter = new ClassificationAdapter(getActivity(), commons, new ClassificationAdapter.TagClickBase() {
            @Override
            public void click(Classification.DataBeanX.DataBean dataBean) {
                T.getInstance().showToast("class点击");
            }
        });
        mSwipeLayout.setColorSchemeResources(R.color.app_basic, R.color.app_basic,
                R.color.app_basic, R.color.app_basic);
        loadLayout.showLoadingView();
        mRecyclerView.setAdapter(mAdapter);
        loadLayout.findViewById(R.id.btnTryAgain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadLayout.showLoadingView();
                refresh();
            }
        });
        refresh();
    }

    private List<Classification.DataBeanX> commons = new ArrayList<>();

    private void refresh() {
        RetrofitUtils.getInstance().getClassifications()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Classification.DataBeanX>>() {
                    @Override
                    public void call(List<Classification.DataBeanX> datas) {
                        mSwipeLayout.setRefreshing(false);
                        if (datas != null && !datas.isEmpty()) {
                            commons.clear();
                            commons.addAll(datas);
                            mAdapter.notifyDataSetChanged();
                            loadLayout.showContentView();
                        } else {
                            loadLayout.showEmptyView();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mSwipeLayout.setRefreshing(false);
                        loadLayout.showErrorView();
                        throwable.printStackTrace();
                    }
                });
    }

    @Override
    public void onRefresh() {
        refresh();
    }
}
