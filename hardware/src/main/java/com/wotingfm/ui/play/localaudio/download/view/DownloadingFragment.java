package com.wotingfm.ui.play.localaudio.download.view;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.woting.commonplat.utils.FileSizeUtil;
import com.woting.commonplat.widget.LoadFrameLayout;
import com.wotingfm.R;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.database.DownloadHelper;
import com.wotingfm.common.utils.DialogUtils;
import com.wotingfm.ui.play.localaudio.download.adapter.DownloadingDownloadAdapter;
import com.wotingfm.ui.bean.MessageEvent;
import com.wotingfm.ui.bean.SinglesDownload;
import com.wotingfm.ui.play.localaudio.download.presenter.DownloadingPresenter;
import com.wotingfm.ui.play.localaudio.locallist.presenter.LocalListPresenter;
import com.wotingfm.ui.play.main.PlayerActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 下载中
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */

public class DownloadingFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.loadLayout)
    LoadFrameLayout loadLayout;

    private View rootView;
    private DownloadingDownloadAdapter downloadingDownloadAdapter;
    private Dialog dialog;
    private DownloadingPresenter presenter;

    public static DownloadingFragment newInstance() {
        DownloadingFragment fragment = new DownloadingFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_download_downloading, container, false);
            rootView.setOnClickListener(this);
            ButterKnife.bind(this, rootView);
            inItView();
            presenter = new DownloadingPresenter(this);
        }
        return rootView;
    }

    private void inItView() {
        rootView.findViewById(R.id.head_left_btn).setOnClickListener(this);
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

    /**
     * 适配数据
     *
     * @param list
     */
    public void setData(List<SinglesDownload> list) {
        if(downloadingDownloadAdapter==null){
            downloadingDownloadAdapter = new DownloadingDownloadAdapter(getActivity(), list, new DownloadingDownloadAdapter.DeleteClick() {
                @Override
                public void clickDelete(final SinglesDownload s) {
                  if(s!=null)  presenter.del(s);
                }

                @Override
                public void click(SinglesDownload singlesDownload) {
                    if (getActivity() != null) {
                        List<SinglesDownload> singlesDownloads = new ArrayList<>();
                        singlesDownloads.add(singlesDownload);
                        startMain(singlesDownloads);
                    }
                }
            });
            mRecyclerView.setAdapter(downloadingDownloadAdapter);
        }else{
            downloadingDownloadAdapter.notifyDataSetChanged();
        }
    }

    private void startMain(List<SinglesDownload> singlesDownloadsd) {
        GlobalStateConfig.activityA = "A";
        EventBus.getDefault().post(new MessageEvent("one"));
        EventBus.getDefault().post(new MessageEvent(singlesDownloadsd, 3));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left_btn:
                PlayerActivity.close();
                break;
        }
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
        presenter=null;
    }
}
