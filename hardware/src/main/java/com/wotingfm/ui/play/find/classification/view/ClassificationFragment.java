package com.wotingfm.ui.play.find.classification.view;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.woting.commonplat.widget.LoadFrameLayout;
import com.wotingfm.R;
import com.wotingfm.ui.play.find.classification.adapter.ClassificationAdapter;
import com.wotingfm.ui.base.basefragment.BaseFragment;
import com.wotingfm.ui.play.find.classification.model.Classification;
import com.wotingfm.ui.play.find.classification.presenter.ClassificationPresenter;
import com.wotingfm.ui.play.look.activity.classification.fragment.MinorClassificationFragment;

import java.util.List;

import butterknife.BindView;

/**
 * 发现分类
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */

public class ClassificationFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.id_swipe_ly)
    SwipeRefreshLayout mSwipeLayout;
    @BindView(R.id.loadLayout)
    LoadFrameLayout loadLayout;

    private ClassificationPresenter presenter;

    public static ClassificationFragment newInstance() {
        ClassificationFragment fragment = new ClassificationFragment();
        return fragment;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_classification;
    }

    private ClassificationAdapter mAdapter;

    @Override
    protected void initView() {

        showView();
        presenter = new ClassificationPresenter(this);
        setListener();
    }

    // 界面适配
    private void showView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mSwipeLayout.setColorSchemeResources(R.color.app_basic, R.color.app_basic, R.color.app_basic, R.color.app_basic);
        // 使用下拉刷新
        loadLayout.showLoadingView();
    }

    //设置监听
    private void setListener() {
        mSwipeLayout.setOnRefreshListener(this);// 设置下拉刷新监听
        // 下拉刷新动画以及事件
        loadLayout.findViewById(R.id.btnTryAgain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadLayout.showLoadingView();
                presenter.refresh();
            }
        });
    }

    public void setData(List<Classification.DataBeanX> commons) {
        if(mAdapter==null){
            mAdapter = new ClassificationAdapter(getActivity(), commons, new ClassificationAdapter.TagClickBase() {
                @Override
                public void click(Classification.DataBeanX.DataBean dataBean) {
                    openFragment(MinorClassificationFragment.newInstance(dataBean.id, dataBean.title));
                }
            });
            mRecyclerView.setAdapter(mAdapter);
        }else{
            mAdapter.notifyDataSetChanged();
        }
    }

    public void refreshCancel() {
        mSwipeLayout.setRefreshing(false);
    }

    public void showErrorView() {
        loadLayout.showErrorView();
    }

    public void showContentView() {
        loadLayout.showContentView();
    }

    public void showEmptyView() {
        loadLayout.showEmptyView();
    }

    /**
     * 下拉刷新监听
     */
    @Override
    public void onRefresh() {
        presenter.refresh();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.destroy();
        presenter=null;
    }
}
