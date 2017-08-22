package com.woting.ui.play.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.woting.commonplat.widget.LoadFrameLayout;
import com.woting.R;
import com.woting.common.adapter.PlayerHistoryListAdapter;
import com.woting.common.application.BSApplication;
import com.woting.common.bean.Player;
import com.woting.common.database.HistoryHelper;
import com.woting.common.utils.T;
import com.woting.ui.base.basefragment.BaseFragment;

import java.util.List;

import butterknife.BindView;

/**
 * Created by amine on 2017/6/9.
 * f播放历史
 */

public class PlayerHistoryFragment extends BaseFragment {
    public static PlayerHistoryFragment newInstance() {
        PlayerHistoryFragment fragment = new PlayerHistoryFragment();
        return fragment;
    }

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.loadLayout)
    LoadFrameLayout loadLayout;
    private PlayerHistoryListAdapter playerHistoryListAdapter;


    @Override
    protected int getLayoutResource() {
        return R.layout.activity_play_history;
    }

    @Override
    public void initView() {
        setTitle("播放历史");
        loadLayout.showLoadingView();
        loadLayout.findViewById(R.id.btnTryAgain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadLayout.showLoadingView();
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
/*        mRecyclerView.addItemDecoration(new RecyclerViewDivider(
                this, LinearLayoutManager.VERTICAL, 1, R.color.color_efefef));*/
        final HistoryHelper historyHelper = new HistoryHelper(BSApplication.getInstance());
        if (historyHelper != null) {
            final List<Player.DataBean.SinglesBean> list = historyHelper.findPlayHistoryList();
            if (list != null && !list.isEmpty()) {
                playerHistoryListAdapter = new PlayerHistoryListAdapter(getActivity(), list, new PlayerHistoryListAdapter.PlayerHistoryClick() {
                    @Override
                    public void click(Player.DataBean.SinglesBean singlesBean) {
                        startMain(singlesBean);
                    }

                    @Override
                    public void delete(Player.DataBean.SinglesBean singlesBean) {
                        T.getInstance().showToast("删除成功");
                        list.remove(singlesBean);
                        historyHelper.deleteTable(singlesBean.id);
                        playerHistoryListAdapter.notifyDataSetChanged();
                    }
                });
                mRecyclerView.setAdapter(playerHistoryListAdapter);
                loadLayout.showContentView();
            } else {
                loadLayout.showEmptyView();
            }
        } else {
            loadLayout.showEmptyView();
        }
    }

}
