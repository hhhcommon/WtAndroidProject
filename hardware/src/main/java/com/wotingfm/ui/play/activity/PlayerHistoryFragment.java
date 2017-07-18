package com.wotingfm.ui.play.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.woting.commonplat.widget.LoadFrameLayout;
import com.wotingfm.R;
import com.wotingfm.common.adapter.PlayerHistoryListAdapter;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.bean.Player;
import com.wotingfm.common.config.DbConfig;
import com.wotingfm.common.database.HistoryHelper;
import com.wotingfm.common.utils.T;
import com.wotingfm.common.view.RecyclerViewDivider;
import com.wotingfm.ui.base.baseactivity.BaseToolBarActivity;
import com.wotingfm.ui.base.basefragment.BaseFragment;
import com.wotingfm.ui.test.PlayerActivity;
import com.wotingfm.ui.test.PlayerFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static android.R.id.list;
import static android.app.Activity.RESULT_OK;

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
                        openFragment(PlayerFragment.newInstance(singlesBean));
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
