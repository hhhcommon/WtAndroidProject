package com.wotingfm.ui.play.album.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.woting.commonplat.widget.LoadFrameLayout;
import com.wotingfm.R;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.ui.adapter.albumsAdapter.AlbumsInfoProgramAdapter;
import com.wotingfm.ui.base.basefragment.BaseFragment;
import com.wotingfm.ui.bean.MessageEvent;
import com.wotingfm.ui.bean.Player;
import com.wotingfm.ui.bean.SinglesBase;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;
import com.wotingfm.ui.mine.main.MineActivity;
import com.wotingfm.ui.play.album.view.download.view.DownloadSelectFragment;
import com.wotingfm.ui.play.find.main.view.LookListActivity;
import com.wotingfm.ui.play.main.PlayerActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * *专辑详情。节目fragment
 */

public class ProgramInfoFragment extends BaseFragment implements View.OnClickListener {
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
    LinearLayout relativeLable;
    private View rootView;

    private int mPage;
    private String albumsID;
    private List<Player.DataBean.SinglesBean> singlesBeanList = new ArrayList<>();
    private AlbumsInfoProgramAdapter albumsInfoProgramAdapter;
    private boolean isLoadingData = false;
    private boolean isCo=false;// 排序按钮标志

    public static ProgramInfoFragment newInstance(String albumsID) {
        ProgramInfoFragment fragment = new ProgramInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString("albumsID", albumsID);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_program_info, container, false);
            rootView.setOnClickListener(this);
            ButterKnife.bind(this, rootView);
            EventBus.getDefault().register(this);
            inItView();
        }
        return rootView;
    }

    protected void inItView() {
        Bundle bundle = getArguments();
        if (bundle != null) albumsID = bundle.getString("albumsID");
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
        albumsInfoProgramAdapter = new AlbumsInfoProgramAdapter(getActivity(), singlesBeanList, new AlbumsInfoProgramAdapter.AlbumsInfoClick() {
            @Override
            public void player(Player.DataBean.SinglesBean albumsBean, int postion) {
                closeFragment();
                startMain(albumsBean);
            }
        });
        mRecyclerView.setAdapter(albumsInfoProgramAdapter);
        refresh();
        ivSequence.setOnClickListener(this);
        tvTotal.setOnClickListener(this);
        ivDownload.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvTotal:
                startMain(albumsID);
                break;
            case R.id.ivSequence:
                Collections.reverse(singlesBeanList);
                albumsInfoProgramAdapter.notifyDataSetChanged();
                if(isCo){
                    ivSequence.setImageResource(R.mipmap.detail_icon_sequence);
                    isCo=false;
                }else{
                    ivSequence.setImageResource(R.mipmap.detail_icon_reverse);
                    isCo=true;
                }
                break;
            case R.id.ivDownload:
                DownloadSelectFragment fragment = DownloadSelectFragment.newInstance(albumsID);
                openFragment(fragment);
                break;
        }
    }

    private void startMain(String albumsId) {
        GlobalStateConfig.activityA = "A";
        EventBus.getDefault().post(new MessageEvent("one"));
        EventBus.getDefault().post(new MessageEvent("stop&" + albumsId));
    }

    private void startMain(SinglesBase singlesBase) {
        GlobalStateConfig.activityA = "A";
        EventBus.getDefault().post(new MessageEvent("one"));
        EventBus.getDefault().post(new MessageEvent(singlesBase, 2));
    }

    private void refresh() {
        mPage = 1;
        RetrofitUtils.getInstance().getProgramList(albumsID, mPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Player>() {
                    @Override
                    public void call(Player albumsBeen) {
                        relativeLable.setVisibility(View.VISIBLE);
                        tvTotal.setText("全部播放(共" + albumsBeen.data.total_count + "集)");
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
                        }
                        isLoadingData = false;
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (singlesBeanList != null && singlesBeanList.size() > 0) {
                            loadLayout.showContentView();
                        } else {
                            loadLayout.showErrorView();
                        }
                        throwable.printStackTrace();
                        isLoadingData = false;
                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEventBase(MessageEvent messageEvent) {
        boolean type = messageEvent.getIsBottom();
        if (type) {
            if (!isLoadingData) {
                isLoadingData = true;
                loadMore();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
