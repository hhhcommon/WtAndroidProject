package com.wotingfm.ui.play.activity.download.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.woting.commonplat.utils.FileSizeUtil;
import com.woting.commonplat.widget.LoadFrameLayout;
import com.wotingfm.R;
import com.wotingfm.common.adapter.PlayerHistoryListAdapter;
import com.wotingfm.common.adapter.downloadAdapter.AlbumsDownloadAdapter;
import com.wotingfm.common.adapter.downloadAdapter.ProgramDownloadAdapter;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.bean.Player;
import com.wotingfm.common.bean.SinglesDownload;
import com.wotingfm.common.database.DownloadHelper;
import com.wotingfm.common.database.HistoryHelper;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.common.utils.T;
import com.wotingfm.ui.base.basefragment.BaseFragment;
import com.wotingfm.ui.test.PlayerFragment;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;
import static android.media.CamcorderProfile.get;
import static com.loc.e.e;
import static com.loc.e.i;
import static com.loc.e.l;

/**
 * Created by amine on 2017/6/14.
 */

public class ProgramFragment extends BaseFragment {
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.loadLayout)
    LoadFrameLayout loadLayout;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_download_program;
    }

    public static ProgramFragment newInstance() {
        ProgramFragment fragment = new ProgramFragment();
        return fragment;
    }

    public void refresh() {
        final DownloadHelper downloadHelper = new DownloadHelper(BSApplication.getInstance());
        if (downloadHelper != null) {
            final List<SinglesDownload> list = downloadHelper.findPlayHistoryList();
            if (list != null && !list.isEmpty()) {
                final List<SinglesDownload> listResult = new ArrayList<>();
                for (int i = 0, size = list.size(); i < size; i++) {
                    if (list.get(i).isDownloadOver == true)
                        listResult.add(list.get(i));
                }
                programDownloadAdapter = new ProgramDownloadAdapter(getActivity(), listResult, new ProgramDownloadAdapter.DeleteClick() {
                    @Override
                    public void clickDelete(final SinglesDownload s) {
                        showLodingDialog();
                        downloadHelper.deleteTable(s.id);
                        FileSizeUtil.delFile(s.single_file_url);
                        listResult.remove(s);
                        programDownloadAdapter.notifyDataSetChanged();
                        if (listResult.isEmpty()) {
                            loadLayout.showEmptyView();
                        }
                        EventBus.getDefault().postSticky(s.id);
                        dissmisDialog();
                    }

                    @Override
                    public void click(SinglesDownload singlesDownload) {
                        if(getActivity()!=null) {
                            List<SinglesDownload> singlesDownloads = new ArrayList<>();
                            singlesDownloads.add(singlesDownload);
                            openFragment(PlayerFragment.newInstance(singlesDownloads));
                        }
                    }
                });
                mRecyclerView.setAdapter(programDownloadAdapter);
                if (listResult.isEmpty()) {
                    loadLayout.showEmptyView();
                } else {
                    loadLayout.showContentView();
                }
            } else {
                loadLayout.showEmptyView();
            }
        } else {
            loadLayout.showEmptyView();
        }

    }

    @Override
    protected void initView() {
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
        refresh();
    }


    private ProgramDownloadAdapter programDownloadAdapter;
}
