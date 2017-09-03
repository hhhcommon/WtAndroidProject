package com.wotingfm.ui.play.localaudio.locallist.presenter;

import android.os.Bundle;
import android.text.TextUtils;

import com.wotingfm.common.utils.CommonUtils;
import com.wotingfm.ui.play.localaudio.dao.FileInfoDao;
import com.wotingfm.ui.play.localaudio.locallist.view.LocalListFragment;
import com.wotingfm.ui.play.localaudio.model.FileInfo;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class LocalListPresenter {

    private FileInfo album;
    private FileInfoDao FID;
    private LocalListFragment activity;
    private List<FileInfo> list;

    public LocalListPresenter(LocalListFragment activity) {
        this.activity = activity;
        FID = new FileInfoDao(activity.getActivity());
        Bundle bundle = activity.getArguments();
        if (bundle != null) {
            album = (FileInfo) bundle.getSerializable("album");
            getData();
            String name = bundle.getString("name");
            if (!TextUtils.isEmpty(name)) {
                activity.setName(name);
            } else {
                activity.setName("列表");
            }
        } else {
            activity.showEmptyView();
        }
    }

    public void getData() {
        list = FID.queryAlbumInfo(album.album_id, CommonUtils.getUserId());
        if (list != null && !list.isEmpty()) {
            activity.setData(list);
            activity.showContentView();
        } else {
            activity.showEmptyView();
        }
    }

    public void del(FileInfo s) {
        activity.dialogShow();
        FID.deleteFileInfo(s.id, CommonUtils.getUserId());
        list.remove(s);
        if (list.isEmpty()) {
            activity.showEmptyView();
        } else {
            activity.setData(list);
        }
        EventBus.getDefault().postSticky(s.id);
        activity.dialogCancel();
        activity.setResult(true);
    }

    /**
     * 数据销毁
     */
    public void destroy() {
        FID.closeDB();
        FID = null;
    }

}
