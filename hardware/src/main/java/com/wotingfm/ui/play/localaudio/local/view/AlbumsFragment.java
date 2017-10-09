package com.wotingfm.ui.play.localaudio.local.view;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.woting.commonplat.widget.LoadFrameLayout;
import com.wotingfm.R;
import com.wotingfm.common.constant.BroadcastConstants;
import com.wotingfm.common.utils.DialogUtils;
import com.wotingfm.ui.base.basefragment.BaseFragment;
import com.wotingfm.ui.play.localaudio.download.view.DownloadingFragment;
import com.wotingfm.ui.play.localaudio.local.adapter.AlbumsDownloadAdapter;
import com.wotingfm.ui.play.localaudio.local.presenter.AlbumsPresenter;
import com.wotingfm.ui.play.localaudio.locallist.view.LocalListFragment;
import com.wotingfm.ui.play.localaudio.model.FileInfo;
import com.wotingfm.ui.play.main.PlayerActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 本地音频
 * author：辛龙 (xinLong)
 * 2016/12/28 11:21
 * 邮箱：645700751@qq.com
 */

public class AlbumsFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.loadLayout)
    LoadFrameLayout loadLayout;
    @BindView(R.id.tv_down)
    TextView tv_down;
    @BindView(R.id.head_left_btn)
    ImageView head_left_btn;

    private View rootView;
    private AlbumsPresenter presenter;
    private AlbumsDownloadAdapter albumsDownloadAdapter;
    private Dialog LDialog;
    private Dialog dialog;
    private FileInfo s;
    private MessageReceivers receiver;
    private List<FileInfo> src_list=new ArrayList<>();

    public static AlbumsFragment newInstance() {
        AlbumsFragment fragment = new AlbumsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_download_albums, container, false);
            rootView.setOnClickListener(this);
            ButterKnife.bind(this, rootView);
            inItView();
            presenter = new AlbumsPresenter(this);
            initDialogL();
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
        head_left_btn.setOnClickListener(this);
        tv_down.setOnClickListener(this);
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
     * 数据适配
     *
     * @param listResult
     */
    public void setData(List<FileInfo> listResult) {
        src_list.clear();
        src_list.addAll(listResult);
        if (albumsDownloadAdapter == null) {
            albumsDownloadAdapter = new AlbumsDownloadAdapter(getActivity(), src_list, new AlbumsDownloadAdapter.DeleteClick() {
                @Override
                public void clickDelete(FileInfo ss) {
                    s = ss;
                    LDialog.show();
                }

                @Override
                public void play(FileInfo s) {
                    startMain(s.id);
                }

                @Override
                public void click(FileInfo s) {
                    LocalListFragment fragment = new LocalListFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("album", s);
                    fragment.setArguments(bundle);
                    PlayerActivity.open(fragment);
                    fragment.setResultListener(new LocalListFragment.ResultListener() {
                        @Override
                        public void resultListener(boolean b) {
                            if (b) {
                                presenter.getData();
                            }
                        }
                    });
                }
            });
            mRecyclerView.setAdapter(albumsDownloadAdapter);
        } else {
            albumsDownloadAdapter.notifyDataSetChanged();
        }
        loadLayout.showContentView();
    }

    // 初始化对话框
    private void initDialogL() {
        // 退出登录对话框
        View dialog1 = LayoutInflater.from(this.getActivity()).inflate(R.layout.dialog_talk_person_del, null);
        dialog1.findViewById(R.id.tv_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LDialog.dismiss();
                presenter.del(s);

            }
        }); // 确定
        dialog1.findViewById(R.id.tv_cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LDialog.dismiss();
            }
        });  // 取消
        TextView textTitle = (TextView) dialog1.findViewById(R.id.tv_title);
        textTitle.setText("您确定删除此专辑吗?");

        LDialog = new Dialog(this.getActivity(), R.style.MyDialogs);
        LDialog.setContentView(dialog1);
        LDialog.setCanceledOnTouchOutside(false);
        LDialog.getWindow().setBackgroundDrawableResource(R.color.transparent_background);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left_btn:
                PlayerActivity.close();
                break;
            case R.id.tv_down:
                PlayerActivity.open(DownloadingFragment.newInstance());
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
