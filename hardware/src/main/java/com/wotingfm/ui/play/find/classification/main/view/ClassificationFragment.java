package com.wotingfm.ui.play.find.classification.main.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.woting.commonplat.widget.LoadFrameLayout;
import com.wotingfm.R;
import com.wotingfm.common.view.GridSpacingItemDecoration;
import com.wotingfm.ui.play.find.classification.main.adapter.ClassificationAdapter;
import com.wotingfm.ui.play.find.classification.main.model.Classification;
import com.wotingfm.ui.play.find.classification.main.presenter.ClassificationPresenter;
import com.wotingfm.ui.play.find.classification.minorclassification.view.MinorClassificationFragment;
import com.wotingfm.ui.play.find.main.view.LookListActivity;
import com.wotingfm.ui.play.main.PlayerActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 发现分类
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */

public class ClassificationFragment extends Fragment implements View.OnClickListener,SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.id_swipe_ly)
    SwipeRefreshLayout mSwipeLayout;
    @BindView(R.id.loadLayout)
    LoadFrameLayout loadLayout;

    private ClassificationPresenter presenter;
    private View rootView;

    public static ClassificationFragment newInstance() {
        ClassificationFragment fragment = new ClassificationFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_classification, container, false);
            rootView.setOnClickListener(this);
            ButterKnife.bind(this, rootView);
            inItView();
        }
        return rootView;
    }

    private ClassificationAdapter mAdapter;

    protected void inItView() {
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
            GridLayoutManager mgr =  new GridLayoutManager(getActivity(), 4, GridLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(mgr);
            GridSpacingItemDecoration gridSpacingItemDecoration = new GridSpacingItemDecoration(4, getActivity().getResources().getDimensionPixelSize(R.dimen.padding_middle_16), true);
            mRecyclerView.addItemDecoration(gridSpacingItemDecoration);
            mAdapter = new ClassificationAdapter(getActivity(), commons, new ClassificationAdapter.TagClick() {
                @Override
                public void click(Classification.DataBeanX dataBean) {
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

    private void openFragment(Fragment fragment) {
        if (getActivity() instanceof PlayerActivity) {
            PlayerActivity.open(fragment);
        } else if (getActivity() instanceof LookListActivity) {
            LookListActivity.open(fragment);
        }
    }

    @Override
    public void onClick(View v) {

    }
}
