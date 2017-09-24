package com.wotingfm.ui.play.mysubscribelist.view;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.woting.commonplat.widget.LoadFrameLayout;
import com.wotingfm.R;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.utils.DialogUtils;
import com.wotingfm.ui.base.basefragment.BaseFragment;
import com.wotingfm.ui.bean.AlbumsBean;
import com.wotingfm.ui.bean.MessageEvent;
import com.wotingfm.ui.mine.main.MineActivity;
import com.wotingfm.ui.play.album.main.view.AlbumsInfoMainFragment;
import com.wotingfm.ui.play.find.main.view.LookListActivity;
import com.wotingfm.ui.play.main.PlayerActivity;
import com.wotingfm.ui.play.mysubscribelist.adapter.PlayerSubscribeListAdapter;
import com.wotingfm.ui.play.mysubscribelist.presenter.MySubscribeListPresenter;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 我的订阅列表
 * author：辛龙 (xinLong)
 * 2016/12/28 11:21
 * 邮箱：645700751@qq.com
 */

public class MeSubscribeListFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.loadLayout)
    LoadFrameLayout loadLayout;

    private View rootView;
    private MySubscribeListPresenter presenter;
    private PlayerSubscribeListAdapter playerHistoryListAdapter;
    private Dialog dialog;
    private FragmentActivity context;

    public static MeSubscribeListFragment newInstance() {
        MeSubscribeListFragment fragment = new MeSubscribeListFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.activity_play_subscrible, container, false);
            rootView.setOnClickListener(this);
            context=this.getActivity();
            ButterKnife.bind(this, rootView);
            inItView();
            presenter = new MySubscribeListPresenter(this);
        }
        return rootView;
    }

    // 设置界面
    private void inItView() {
        rootView.findViewById(R.id.head_left_btn).setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left_btn:
              closeFragment();
                break;
        }
    }

    /**
     * 适配数据
     *
     * @param list
     */
    public void setData(List<AlbumsBean> list) {
        if (playerHistoryListAdapter == null) {
            playerHistoryListAdapter = new PlayerSubscribeListAdapter(getActivity(), list, new PlayerSubscribeListAdapter.PlayerHistoryClick() {
                @Override
                public void click(AlbumsBean singlesBean) {
                    if (singlesBean != null && singlesBean.id != null) {
                        openFragment(AlbumsInfoMainFragment.newInstance(singlesBean.id));
                    }

                }
                @Override
                public void play(AlbumsBean singlesBean) {
                    if (singlesBean != null && singlesBean.id != null) startMain(singlesBean.id);
                }
                @Override
                public void delete(AlbumsBean singlesBean) {
                    if (singlesBean != null) presenter.unFollowPlayer(singlesBean);

                }
            });
            mRecyclerView.setAdapter(playerHistoryListAdapter);
        } else {
            playerHistoryListAdapter.notifyDataSetChanged();
        }
    }

    // 开始播放
    private void startMain(String albumsId) {
        GlobalStateConfig.activityA = "A";
        EventBus.getDefault().post(new MessageEvent("one"));
        EventBus.getDefault().post(new MessageEvent("stop&" + albumsId));
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.destroy();
        presenter = null;
    }
}
