package com.wotingfm.ui.play.localaudio.locallist.view;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.woting.commonplat.widget.LoadFrameLayout;
import com.wotingfm.R;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.constant.BroadcastConstants;
import com.wotingfm.common.utils.DialogUtils;
import com.wotingfm.ui.bean.MessageEvent;
import com.wotingfm.ui.bean.SinglesDownload;
import com.wotingfm.ui.play.localaudio.locallist.adapter.LocalListAdapter;
import com.wotingfm.ui.play.localaudio.locallist.presenter.LocalListPresenter;
import com.wotingfm.ui.play.localaudio.model.FileInfo;
import com.wotingfm.ui.play.main.PlayerActivity;
import org.greenrobot.eventbus.EventBus;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 专辑列表
 * author：辛龙 (xinLong)
 * 2016/12/28 11:21
 * 邮箱：645700751@qq.com
 */

public class LocalListFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.loadLayout)
    LoadFrameLayout loadLayout;
    @BindView(R.id.tv_center)
    TextView tv_center;

    private View rootView;
    private LocalListPresenter presenter;
    private LocalListAdapter localListAdapter;
    private Dialog dialog;
    private ResultListener Listener;
    private MessageReceivers receiver;
    private List<FileInfo> src_list=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.activity_local_list, container, false);
            rootView.setOnClickListener(this);
            ButterKnife.bind(this, rootView);
            inItView();
            presenter = new LocalListPresenter(this);
            if (receiver == null) {
                receiver = new MessageReceivers();
                IntentFilter filters = new IntentFilter();
                filters.addAction(BroadcastConstants.ACTION_FINISHED);
                getActivity().registerReceiver(receiver, filters);
            }
        }
        return rootView;
    }

    // 设置界面
    private void inItView() {
        rootView.findViewById(R.id.head_left_btn).setOnClickListener(this);
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
     * 设置名称
     *
     * @param name
     */
    public void setName(String name) {
        if (!TextUtils.isEmpty(name)) {
            tv_center.setText(name);
        } else {
            tv_center.setText("列表");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left_btn:
                PlayerActivity.close();
                break;
        }
    }

    /**
     * 适配数据
     *
     * @param list
     */
    public void setData(List<FileInfo>  list) {
        src_list.clear();
        src_list.addAll(list);
        if (localListAdapter == null) {
            localListAdapter = new LocalListAdapter(getActivity(), src_list, new LocalListAdapter.localListClick() {
                @Override
                public void play(FileInfo singlesDownload) {
                    if (singlesDownload != null) {
//                        List<SinglesDownload> singlesDownloads = new ArrayList<>();
//                        singlesDownloads.add(singlesDownload);
//                        startMain(singlesDownloads);
                    }
                }

                @Override
                public void delete(FileInfo s) {
                    if (s != null) presenter.del(s);
                }
            });
            mRecyclerView.setAdapter(localListAdapter);
        } else {
            localListAdapter.notifyDataSetChanged();
        }
    }

    // 开始播放
    private void startMain(List<SinglesDownload> s) {
        GlobalStateConfig.activityA = "A";
        EventBus.getDefault().post(new MessageEvent("one"));
        EventBus.getDefault().post(new MessageEvent(s, 3));
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

    /**
     * 返回值设置
     *
     * @param b
     */
    public void setResult(boolean b) {
        Listener.resultListener(b);
    }

    /**
     * 回调结果值
     *
     * @param l
     */
    public void setResultListener(ResultListener l) {
        Listener = l;
    }

    public interface ResultListener {
        void resultListener(boolean b);
    }

    class MessageReceivers extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
         if(BroadcastConstants.ACTION_FINISHED.equals(intent.getAction())){
                presenter.getData();
            }
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
