package com.wotingfm.ui.play.playhistory.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.woting.commonplat.widget.LoadFrameLayout;
import com.wotingfm.R;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.ui.bean.MessageEvent;
import com.wotingfm.ui.bean.Player;
import com.wotingfm.ui.bean.SinglesBase;
import com.wotingfm.ui.play.main.PlayerActivity;
import com.wotingfm.ui.play.playhistory.adapter.PlayerHistoryListAdapter;
import com.wotingfm.ui.play.playhistory.presenter.PlayHistoryPresenter;
import org.greenrobot.eventbus.EventBus;
import java.util.List;


/**
 * 播放历史
 * author：辛龙 (xinLong)
 * 2016/12/28 11:21
 * 邮箱：645700751@qq.com
 */

public class PlayerHistoryFragment extends Fragment implements View.OnClickListener{

    private View rootView;
    private PlayHistoryPresenter presenter;
    private PlayerHistoryListAdapter playerHistoryListAdapter;
    private RecyclerView mRecyclerView;
    private LoadFrameLayout loadLayout;

    public static PlayerHistoryFragment newInstance() {
        PlayerHistoryFragment fragment = new PlayerHistoryFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.activity_play_history, container, false);
            rootView.setOnClickListener(this);
            inItView();
            presenter = new PlayHistoryPresenter(this);
        }
        return rootView;
    }

    // 设置界面
    private void inItView() {

        rootView.findViewById(R.id.head_left_btn).setOnClickListener(this);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.mRecyclerView);
        loadLayout = (LoadFrameLayout) rootView.findViewById(R.id.loadLayout);

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
    }

    public void setData(List<Player.DataBean.SinglesBean> list ){
        playerHistoryListAdapter = new PlayerHistoryListAdapter(getActivity(), list, new PlayerHistoryListAdapter.PlayerHistoryClick() {
            @Override
            public void click(Player.DataBean.SinglesBean singlesBean) {
                startMain(singlesBean);
            }

            @Override
            public void delete(Player.DataBean.SinglesBean singlesBean) {
                presenter.del(singlesBean,singlesBean.id);
                playerHistoryListAdapter.notifyDataSetChanged();
            }
        });
        mRecyclerView.setAdapter(playerHistoryListAdapter);
    }

    public void showContentView(){
        loadLayout.showContentView();
    }

    public void showEmptyView(){
        loadLayout.showEmptyView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left_btn:
                PlayerActivity.close();
                break;}
    }

    public void startMain(SinglesBase singlesBase) {
        GlobalStateConfig.activityA = "A";
        EventBus.getDefault().post(new MessageEvent("one"));
        EventBus.getDefault().post(new MessageEvent(singlesBase, 2));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("界面","执行销毁");
        presenter.destroy();
        presenter=null;
    }
}
