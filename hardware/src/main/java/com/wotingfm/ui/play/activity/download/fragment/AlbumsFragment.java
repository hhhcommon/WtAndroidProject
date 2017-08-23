package com.wotingfm.ui.play.activity.download.fragment;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.woting.commonplat.utils.FileSizeUtil;
import com.woting.commonplat.widget.LoadFrameLayout;
import com.wotingfm.R;
import com.wotingfm.ui.adapter.downloadAdapter.AlbumsDownloadAdapter;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.ui.bean.SinglesDownload;
import com.wotingfm.common.database.DownloadHelper;
import com.wotingfm.ui.base.basefragment.BaseFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;


/**
 * Created by amine on 2017/6/14.
 */

public class AlbumsFragment extends BaseFragment {
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.loadLayout)
    LoadFrameLayout loadLayout;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_download_albums;
    }

    public static AlbumsFragment newInstance() {
        AlbumsFragment fragment = new AlbumsFragment();
        return fragment;
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
        hashMap.clear();
        hashMapList.clear();
        refresh();
    }

    public void refresh() {
        final DownloadHelper downloadHelper = new DownloadHelper(BSApplication.getInstance());
        if (downloadHelper != null) {
            final List<SinglesDownload> list = downloadHelper.findPlayHistoryList();
            if (list != null && !list.isEmpty()) {
                final List<SinglesDownload> listResult = new ArrayList<>();
                for (int i = 0, size = list.size(); i < size; i++) {
                    SinglesDownload s = list.get(i);
                    if (s.isDownloadOver == true) {
                        SinglesDownload so = getMapContent(s.album_id);
                        List<SinglesDownload> sos = getMapContentList(s.album_id);
                        if (so != null && so.album_id.equals(s.album_id)) {
                            so.count = s.count + 1;
                            so.albumSize = so.albumSize + s.albumSize;
                            hashMap.put(s.album_id, so);
                            sos.add(so);
                            hashMapList.put(s.album_id, sos);
                        } else {
                            listResult.add(s);
                            hashMap.put(s.album_id, s);
                            sos.add(s);
                            hashMapList.put(s.album_id, sos);
                        }
                    }
                }
                if (listResult != null && !listResult.isEmpty()) {
                    albumsDownloadAdapter = new AlbumsDownloadAdapter(getActivity(), listResult, hashMap, hashMapList, new AlbumsDownloadAdapter.DeleteClick() {
                        @Override
                        public void clickDelete(final List<SinglesDownload> singlesDownloads, final SinglesDownload s) {
                            new MaterialDialog.Builder(getActivity())
                                    .content("您确定删除此专辑吗?")
                                    .positiveText("确定")
                                    .negativeText("取消")
                                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                            dialog.dismiss();

                                        }
                                    })
                                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                            showLodingDialog();
                                            downloadHelper.deleteTable(s.id);
                                            for (int i = 0; i < singlesDownloads.size(); i++) {
                                                FileSizeUtil.delFile(singlesDownloads.get(i).single_file_url);
                                            }
                                            listResult.remove(s);
                                            albumsDownloadAdapter.notifyDataSetChanged();
                                            if (listResult.isEmpty()) {
                                                loadLayout.showEmptyView();
                                            }
                                            dissmisDialog();
                                            EventBus.getDefault().postSticky(s.id);
                                        }
                                    }).show();
                        }

                        @Override
                        public void click(List<SinglesDownload> singlesDownloads) {
                            startMain(singlesDownloads);
                        }
                    });
                    mRecyclerView.setAdapter(albumsDownloadAdapter);
                    loadLayout.showContentView();
                } else {
                    loadLayout.showEmptyView();
                }
            } else {
                loadLayout.showEmptyView();
            }

        } else {
            loadLayout.showEmptyView();
        }
    }

    private HashMap<String, SinglesDownload> hashMap = new HashMap<>();
    private HashMap<String, List<SinglesDownload>> hashMapList = new HashMap<>();

    public SinglesDownload getMapContent(String key) {
        if (hashMap == null && hashMap.isEmpty())
            return null;
        if (hashMap.containsKey(key)) {
            return hashMap.get(key);
        } else {
            return null;
        }
    }

    public List<SinglesDownload> getMapContentList(String key) {
        if (hashMapList == null && hashMapList.isEmpty())
            return new ArrayList<>();
        if (hashMapList.containsKey(key)) {
            return hashMapList.get(key);
        } else {
            return new ArrayList<>();
        }
    }

    private AlbumsDownloadAdapter albumsDownloadAdapter;
}
