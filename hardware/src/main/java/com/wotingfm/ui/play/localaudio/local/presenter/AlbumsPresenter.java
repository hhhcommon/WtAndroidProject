package com.wotingfm.ui.play.localaudio.local.presenter;

import com.wotingfm.common.utils.CommonUtils;
import com.wotingfm.ui.play.localaudio.dao.FileInfoDao;
import com.wotingfm.ui.play.localaudio.local.view.AlbumsFragment;
import com.wotingfm.ui.play.localaudio.model.FileInfo;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class AlbumsPresenter {

    private FileInfoDao FID;
    private AlbumsFragment activity;
    private List<FileInfo> listResult;

    public AlbumsPresenter(AlbumsFragment activity) {
        this.activity = activity;
        FID = new FileInfoDao(activity.getActivity());
        getData();
    }

    public void getData() {
        List<FileInfo> f = FID.queryFileInfo("true", CommonUtils.getUserId());
        if (f.size() > 0) {
            listResult = FID.GroupFileInfoAll(CommonUtils.getUserId());
            if (listResult != null && !listResult.isEmpty()) {
                activity.setData(listResult);
            } else {
                activity.showEmptyView();
            }
        } else {
            activity.showEmptyView();
        }
    }

    public void del(FileInfo s) {
        activity.dialogShow();
        FID.deleteSequ(s.album_id, CommonUtils.getUserId());
        listResult.remove(s);
        if (listResult.isEmpty()) {
            activity.showEmptyView();
        } else {
            activity.setData(listResult);
        }
        activity.dialogCancel();
    }

    /**
     * 数据销毁
     */
    public void destroy() {
        FID.closeDB();
        FID = null;
    }

}
