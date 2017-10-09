package com.wotingfm.ui.play.album.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.woting.commonplat.widget.LoadFrameLayout;
import com.wotingfm.R;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.ui.base.basefragment.BaseFragment;
import com.wotingfm.ui.bean.AlbumsBean;
import com.wotingfm.ui.bean.MessageEvent;
import com.wotingfm.ui.play.album.main.view.AlbumsInfoMainFragment;
import com.wotingfm.ui.play.album.view.adapter.AlbumsAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by amine on 2017/6/14.
 * *专辑详情。，相似fragment
 */

public class SimilarInfoFragment extends BaseFragment implements View.OnClickListener{
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.loadLayout)
    LoadFrameLayout loadLayout;
    private View rootView;

    public static SimilarInfoFragment newInstance(String albumsID) {
        SimilarInfoFragment fragment = new SimilarInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString("albumsID", albumsID);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_similar_albums, container, false);
            rootView.setOnClickListener(this);
            ButterKnife.bind(this, rootView);
            initView();
        }
        return rootView;
    }

    private String albumsID;


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
                openFragment(AlbumsInfoMainFragment.newInstance(singlesBean.id));
            }
            @Override
            public void play(AlbumsBean singlesBean) {
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

    @Override
    public void onClick(View v) {

    }

    public void startMain(String albumsId) {
//        GlobalStateConfig.activityA = "A";
        EventBus.getDefault().post(new MessageEvent("one"));
        EventBus.getDefault().post(new MessageEvent("stop&" + albumsId));
    }

}
