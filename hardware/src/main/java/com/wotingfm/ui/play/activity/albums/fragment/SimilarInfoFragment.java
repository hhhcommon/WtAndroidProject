package com.wotingfm.ui.play.activity.albums.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.woting.commonplat.utils.FileSizeUtil;
import com.woting.commonplat.widget.LoadFrameLayout;
import com.wotingfm.R;
import com.wotingfm.common.adapter.albumsAdapter.AlbumsAdapter;
import com.wotingfm.common.adapter.downloadAdapter.AlbumsDownloadAdapter;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.bean.AlbumInfo;
import com.wotingfm.common.bean.AlbumsBean;
import com.wotingfm.common.bean.SinglesDownload;
import com.wotingfm.common.bean.Subscrible;
import com.wotingfm.common.database.DownloadHelper;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.ui.base.basefragment.BaseFragment;
import com.wotingfm.ui.play.activity.AnchorPersonalCenterFragment;
import com.wotingfm.ui.test.PlayerFragment;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;

/**
 * Created by amine on 2017/6/14.
 * *专辑详情。，相似fragment
 */

public class SimilarInfoFragment extends BaseFragment {
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.loadLayout)
    LoadFrameLayout loadLayout;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_similar_albums;
    }

    public static SimilarInfoFragment newInstance(String albumsID) {
        SimilarInfoFragment fragment = new SimilarInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString("albumsID", albumsID);
        fragment.setArguments(bundle);
        return fragment;
    }

    private String albumsID;


    @Override
    protected void initView() {
        Bundle savedInstanceState = getArguments();
        albumsID = savedInstanceState.getString("albumsID");
        loadLayout.showLoadingView();
        loadLayout.findViewById(R.id.btnTryAgain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadLayout.showLoadingView();
                refresh();
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        albumsAdapter = new AlbumsAdapter(getActivity(), albumsBeens);
        albumsAdapter.setPlayerClick(new AlbumsAdapter.PlayerClick() {
            @Override
            public void clickAlbums(AlbumsBean singlesBean) {
                startMain(albumsID);
            }
        });
        mRecyclerView.setAdapter(albumsAdapter);
        refresh();
    }

    public void refresh() {
        RetrofitUtils.getInstance().getSimilarsList(albumsID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<AlbumsBean>>() {
                    @Override
                    public void call(List<AlbumsBean> albumsBeen) {
                        if (albumsBeen != null && !albumsBeen.isEmpty()) {
                            albumsBeens.clear();
                            albumsBeens.addAll(albumsBeen);
                            loadLayout.showContentView();
                            albumsAdapter.notifyDataSetChanged();
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

    private List<AlbumsBean> albumsBeens = new ArrayList<>();
    private AlbumsAdapter albumsAdapter;
}
