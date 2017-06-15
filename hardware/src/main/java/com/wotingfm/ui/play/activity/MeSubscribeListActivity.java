package com.wotingfm.ui.play.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.woting.commonplat.widget.LoadFrameLayout;
import com.wotingfm.R;
import com.wotingfm.common.adapter.PlayerHistoryListAdapter;
import com.wotingfm.common.adapter.PlayerSubscribleListAdapter;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.bean.Player;
import com.wotingfm.common.bean.Subscrible;
import com.wotingfm.common.database.HistoryHelper;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.common.utils.T;
import com.wotingfm.ui.base.baseactivity.BaseToolBarActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static android.R.id.list;
import static com.wotingfm.R.id.relatiBottom;

/**
 * Created by amine on 2017/6/9.
 * 我的订阅列表
 */

public class MeSubscribeListActivity extends BaseToolBarActivity {
    public static void start(Context activity) {
        Intent intent = new Intent(activity, MeSubscribeListActivity.class);
        activity.startActivity(intent);
    }

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.loadLayout)
    LoadFrameLayout loadLayout;
    private PlayerSubscribleListAdapter playerHistoryListAdapter;

    @Override
    public int getLayoutId() {
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
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        playerHistoryListAdapter = new PlayerSubscribleListAdapter(this, albumsBeens, new PlayerSubscribleListAdapter.PlayerHistoryClick() {
            @Override
            public void click(Subscrible.DataBean.AlbumsBean singlesBean) {
                T.getInstance().equals("点击订阅列表");
            }

            @Override
            public void delete(Subscrible.DataBean.AlbumsBean singlesBean) {
                showLodingDialog();
                unfollowPlayer(singlesBean);
            }
        });
        mRecyclerView.setAdapter(playerHistoryListAdapter);
        loadLayout.showLoadingView();
        //请求数据，测试userid
        getPlayerList(RetrofitUtils.TEST_USERID);
    }

    private List<Subscrible.DataBean.AlbumsBean> albumsBeens = new ArrayList<>();

    private void getPlayerList(String uid) {
        RetrofitUtils.getInstance().getSubscriptionsList(uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Subscrible.DataBean.AlbumsBean>>() {
                    @Override
                    public void call(List<Subscrible.DataBean.AlbumsBean> albumsBeen) {
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

    private void unfollowPlayer(final Subscrible.DataBean.AlbumsBean albumsBean) {
        RetrofitUtils.getInstance().unSubscriptions(albumsBean.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object s) {
                        dissmisDialog();
                        albumsBeens.remove(albumsBean);
                        T.getInstance().equals("取消成功");
                        playerHistoryListAdapter.notifyDataSetChanged();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        dissmisDialog();
                        T.getInstance().equals("取消订阅失败");
                    }
                });
    }

}