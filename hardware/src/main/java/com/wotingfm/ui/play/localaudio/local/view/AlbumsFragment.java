package com.wotingfm.ui.play.localaudio.local.view;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.woting.commonplat.widget.LoadFrameLayout;
import com.wotingfm.R;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.utils.BeanCloneUtil;
import com.wotingfm.common.utils.DialogUtils;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;
import com.wotingfm.ui.play.localaudio.download.view.DownloadingFragment;
import com.wotingfm.ui.play.localaudio.local.adapter.AlbumsDownloadAdapter;
import com.wotingfm.ui.bean.MessageEvent;
import com.wotingfm.ui.bean.SinglesDownload;
import com.wotingfm.ui.play.localaudio.local.presenter.AlbumsPresenter;
import com.wotingfm.ui.play.localaudio.locallist.view.LocalListFragment;
import com.wotingfm.ui.play.main.PlayerActivity;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 本地音频
 * author：辛龙 (xinLong)
 * 2016/12/28 11:21
 * 邮箱：645700751@qq.com
 */

public class AlbumsFragment extends Fragment implements View.OnClickListener {

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
    private List<SinglesDownload> singlesDownloads;
    private SinglesDownload s;

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
     * @param hashMap
     * @param hashMapList
     */
    public void setData(List<SinglesDownload> listResult, HashMap<String, SinglesDownload> hashMap, HashMap<String, List<SinglesDownload>> hashMapList) {
        if (albumsDownloadAdapter == null) {
            albumsDownloadAdapter = new AlbumsDownloadAdapter(getActivity(), listResult, hashMap, hashMapList, new AlbumsDownloadAdapter.DeleteClick() {
                @Override
                public void clickDelete(List<SinglesDownload> singlesDownloadss, SinglesDownload ss) {
                    singlesDownloads = singlesDownloadss;
                    s = ss;
                    LDialog.show();
                }

                @Override
                public void play(List<SinglesDownload> singlesDownloads) {
                    startMain(singlesDownloads);
                }

                @Override
                public void click(List<SinglesDownload> singlesDownloads, String name) {
                    LocalListFragment fragment = new LocalListFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("list", (Serializable) singlesDownloads);
                    bundle.putString("name", name);
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
                presenter.del(s, singlesDownloads);

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

    // 开始播放
    public void startMain(List<SinglesDownload> s) {
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.destroy();
        presenter = null;
    }
}
