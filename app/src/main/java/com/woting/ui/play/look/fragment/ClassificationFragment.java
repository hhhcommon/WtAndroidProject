package com.woting.ui.play.look.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.woting.commonplat.widget.LoadFrameLayout;
import com.woting.R;
import com.woting.common.adapter.findHome.ClassificationAdapter;
import com.woting.common.bean.Classification;
import com.woting.common.net.RetrofitUtils;
import com.woting.ui.base.basefragment.BaseFragment;
import com.woting.ui.play.look.activity.classification.fragment.MinorClassificationFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

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
                openFragment(MinorClassificationFragment.newInstance(dataBean.id, dataBean.title));
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
