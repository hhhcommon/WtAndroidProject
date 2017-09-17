package com.wotingfm.ui.play.localaudio.download.view;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.woting.commonplat.widget.LoadFrameLayout;
import com.wotingfm.R;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.constant.BroadcastConstants;
import com.wotingfm.common.utils.DialogUtils;
import com.wotingfm.ui.bean.MessageEvent;
import com.wotingfm.ui.bean.SinglesDownload;
import com.wotingfm.ui.play.localaudio.download.adapter.DownloadAdapter;
import com.wotingfm.ui.play.localaudio.download.presenter.DownloadingPresenter;
import com.wotingfm.ui.play.localaudio.model.FileInfo;
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
    ListView mRecyclerView;
    @BindView(R.id.loadLayout)
    LoadFrameLayout loadLayout;

    private View rootView;
    private DownloadAdapter downloadingDownloadAdapter;
    private Dialog dialog;
    private DownloadingPresenter presenter;
    private MessageReceivers receiver;

    private List<FileInfo> src_list = new ArrayList<>();

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
            if (receiver == null) {
                receiver = new MessageReceivers();
                IntentFilter filters = new IntentFilter();
                filters.addAction(BroadcastConstants.ACTION_UPDATE);
                filters.addAction(BroadcastConstants.ACTION_FINISHED);
                getActivity().registerReceiver(receiver, filters);
            }
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
    }

    /**
     * 适配数据
     *
     * @param list
     */
    public void setData(List<FileInfo> list) {
        src_list.clear();
        if(src_list!=null)src_list.addAll(list);
        if (downloadingDownloadAdapter == null) {
            downloadingDownloadAdapter = new DownloadAdapter(getActivity(), src_list);
            mRecyclerView.setAdapter(downloadingDownloadAdapter);
        } else {
            downloadingDownloadAdapter.notifyDataSetChanged();
        }
        setAdapterListener();
    }

    private void setAdapterListener() {
        downloadingDownloadAdapter.setOnListener(new DownloadAdapter.DeleteClick() {
            @Override
            public void clickDelete(FileInfo s) {
                if (s != null) presenter.del(s);

            }

            @Override
            public void click(FileInfo s) {
                presenter.itemClick(s);
            }
        });
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

    class MessageReceivers extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (BroadcastConstants.ACTION_UPDATE.equals(intent.getAction())) {
                int start = intent.getIntExtra("start", 0);
                int end = intent.getIntExtra("end", 0);
                String id = intent.getStringExtra("id");
//                if (downloadingDownloadAdapter != null) {
//                    downloadingDownloadAdapter.updateProgress(id, start, end);
//                }

                int _id = 0;
                for (int i = 0; i < src_list.size(); i++) {
                    if (src_list.get(i).id.trim().equals(id)) {
                        _id = i;
                        break;
                    }
                }
                Log.e("_id",""+_id);

                if (src_list != null && src_list.size() != 0) {
                    FileInfo fileInfo = src_list.get(_id);
                    fileInfo.finished = String.valueOf((start / end));
                    fileInfo.start = start;
                    fileInfo.end = end;
                    Log.e("adapter===", "" + start);
                    Log.e("adapter===", "" + end);
                }
                updateView(_id);
            } else if (BroadcastConstants.ACTION_FINISHED.equals(intent.getAction())) {
                presenter.getData();
            }
        }
    }

    private void updateView(int itemIndex) {
        //得到第一个可显示控件的位置，
        int visiblePosition = mRecyclerView.getFirstVisiblePosition();
        //只有当要更新的view在可见的位置时才更新，不可见时，跳过不更新
        if (itemIndex - visiblePosition >= 0) {
            //得到要更新的item的view
            View view = mRecyclerView.getChildAt(itemIndex - visiblePosition);
            //调用adapter更新界面
            downloadingDownloadAdapter.updateView(view, itemIndex);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            getActivity().unregisterReceiver(receiver);
            receiver = null;
        }
        presenter.destroy();
        presenter = null;
    }
}
