package com.wotingfm.ui.play.activity.download.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.woting.commonplat.utils.FileSizeUtil;
import com.woting.commonplat.widget.LoadFrameLayout;
import com.wotingfm.R;
import com.wotingfm.common.adapter.downloadAdapter.ProgramDownloadAdapter;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.bean.SinglesDownload;
import com.wotingfm.common.database.DownloadHelper;
import com.wotingfm.ui.base.basefragment.BaseFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


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
                            startMain(singlesDownloads);
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
