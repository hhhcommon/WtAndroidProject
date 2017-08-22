package com.woting.ui.play.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.woting.commonplat.widget.LoadFrameLayout;
import com.woting.R;
import com.woting.common.adapter.PlayerSubscribleListAdapter;
import com.woting.common.bean.AlbumsBean;
import com.woting.common.net.RetrofitUtils;
import com.woting.common.utils.CommonUtils;
import com.woting.common.utils.T;
import com.woting.ui.base.basefragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by amine on 2017/6/9.
 * 我的订阅列表
 */

public class MeSubscribeListFragment extends BaseFragment {
    public static MeSubscribeListFragment newInstance() {
        MeSubscribeListFragment fragment = new MeSubscribeListFragment();
        return fragment;
    }

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.loadLayout)
    LoadFrameLayout loadLayout;
    private PlayerSubscribleListAdapter playerHistoryListAdapter;


    @Override
    protected int getLayoutResource() {
        return R.layout.activity_play_subscrible;
    }

    @Override
    public void initView() {
        setTitle("我的订阅");
        loadLayout.findViewById(R.id.btnTryAgain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadLayout.showLoadingView();
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        playerHistoryListAdapter = new PlayerSubscribleListAdapter(getActivity(), albumsBeens, new PlayerSubscribleListAdapter.PlayerHistoryClick() {
            @Override
            public void click(AlbumsBean singlesBean) {
                startMain(singlesBean.id);
            }

            @Override
            public void delete(AlbumsBean singlesBean) {
                showLodingDialog();
                unfollowPlayer(singlesBean);
            }
        });
        mRecyclerView.setAdapter(playerHistoryListAdapter);
        loadLayout.showLoadingView();
        getPlayerList(CommonUtils.getUserId());
    }

    private List<AlbumsBean> albumsBeens = new ArrayList<>();

    private void getPlayerList(String uid) {
        RetrofitUtils.getInstance().getSubscriptionsList(uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<AlbumsBean>>() {
                    @Override
                    public void call(List<AlbumsBean> albumsBeen) {
                        if (albumsBeen != null && !albumsBeen.isEmpty()) {
                            albumsBeens.addAll(albumsBeen);
                            loadLayout.showContentView();
                            playerHistoryListAdapter.notifyDataSetChanged();
                        } else {
                            loadLayout.showEmptyView();
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

    private void unfollowPlayer(final AlbumsBean albumsBean) {
        RetrofitUtils.getInstance().unSubscriptions(albumsBean.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object s) {
                        dissmisDialog();
                        albumsBeens.remove(albumsBean);
                        T.getInstance().showToast("取消成功");
                        playerHistoryListAdapter.notifyDataSetChanged();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        dissmisDialog();
                        T.getInstance().showToast("取消订阅失败");
                    }
                });
    }

}
