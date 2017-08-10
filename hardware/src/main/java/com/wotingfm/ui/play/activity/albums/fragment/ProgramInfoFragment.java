package com.wotingfm.ui.play.activity.albums.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.woting.commonplat.amine.ARecyclerView;
import com.woting.commonplat.amine.LoadMoreFooterView;
import com.woting.commonplat.amine.OnLoadMoreListener;
import com.woting.commonplat.amine.OnRefreshListener;
import com.woting.commonplat.widget.LoadFrameLayout;
import com.wotingfm.R;
import com.wotingfm.common.adapter.albumsAdapter.AlbumsInfoProgramAdapter;
import com.wotingfm.common.bean.Player;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.common.utils.DownloadUtils;
import com.wotingfm.common.utils.T;
import com.wotingfm.ui.base.basefragment.BaseFragment;
import com.wotingfm.ui.play.activity.AnchorPersonalCenterFragment;
import com.wotingfm.ui.play.activity.download.DownloadSelectFragment;
import com.wotingfm.ui.test.PlayerFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;

/**
 * Created by amine on 2017/6/14.
 * *专辑详情。节目fragment
 */

public class ProgramInfoFragment extends BaseFragment {
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.loadLayout)
    LoadFrameLayout loadLayout;
    @BindView(R.id.tvTotal)
    TextView tvTotal;
    @BindView(R.id.ivSequence)
    ImageView ivSequence;
    @BindView(R.id.ivDownload)
    ImageView ivDownload;
    @BindView(R.id.relativeLable)
    RelativeLayout relativeLable;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_program_info;
    }

    public static ProgramInfoFragment newInstance(String albumsID) {
        ProgramInfoFragment fragment = new ProgramInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString("albumsID", albumsID);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initView() {
        Bundle bundle = getArguments();
        if (bundle != null)
            albumsID = bundle.getString("albumsID");
        loadLayout.showLoadingView();
        loadLayout.findViewById(R.id.btnTryAgain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadLayout.showLoadingView();
                refresh();
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setNestedScrollingEnabled(false);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        albumsInfoProgramAdapter = new AlbumsInfoProgramAdapter(getActivity(), singlesBeanList);
        mRecyclerView.setAdapter(albumsInfoProgramAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //拿到最后一条的position
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int endCompletelyPosition = manager.findLastCompletelyVisibleItemPosition();
                if (albumsInfoProgramAdapter.getItemCount() > 10 && endCompletelyPosition == albumsInfoProgramAdapter.getItemCount() - 1) {
                    //执行加载更多的方法，无论是用接口还是别的方式都行
                    loadMore();
                }
            }
        });
        refresh();
        ivSequence.setOnClickListener(this);
        relativeLable.setOnClickListener(this);
        ivDownload.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.relativeLable:
                startMain(albumsID);
                break;
            case R.id.ivSequence:
                Collections.reverse(singlesBeanList);
                albumsInfoProgramAdapter.notifyDataSetChanged();
                T.getInstance().showToast("排序成功");
                break;
            case R.id.ivDownload:
                openFragment(DownloadSelectFragment.newInstance(albumsID));
                break;
        }
    }

    private int mPage;
    private String albumsID;

    private void refresh() {
        mPage = 1;
        RetrofitUtils.getInstance().getProgramList(albumsID, mPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Player>() {
                    @Override
                    public void call(Player albumsBeen) {
                        relativeLable.setVisibility(View.VISIBLE);
                        tvTotal.setText("(全部播放共" + albumsBeen.data.total_count + "集)");
                        if (albumsBeen != null && albumsBeen.data != null && albumsBeen.data.singles != null && !albumsBeen.data.singles.isEmpty()) {
                            mPage++;
                            singlesBeanList.clear();
                            singlesBeanList.addAll(albumsBeen.data.singles);
                            loadLayout.showContentView();
                            albumsInfoProgramAdapter.notifyDataSetChanged();
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

    private void loadMore() {
        RetrofitUtils.getInstance().getProgramList(albumsID, mPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Player>() {
                    @Override
                    public void call(Player albumsBeen) {
                        if (albumsBeen != null && albumsBeen.data != null && albumsBeen.data.singles != null && !albumsBeen.data.singles.isEmpty()) {
                            mPage++;
                            singlesBeanList.addAll(albumsBeen.data.singles);
                            albumsInfoProgramAdapter.notifyDataSetChanged();
                        } else {
                            T.getInstance().showToast("没有更多数据");
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


    private List<Player.DataBean.SinglesBean> singlesBeanList = new ArrayList<>();
    private AlbumsInfoProgramAdapter albumsInfoProgramAdapter;
}
