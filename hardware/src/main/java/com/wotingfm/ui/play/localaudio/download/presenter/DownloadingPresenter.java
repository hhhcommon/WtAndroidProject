package com.wotingfm.ui.play.localaudio.download.presenter;

import com.wotingfm.common.utils.CommonUtils;
import com.wotingfm.ui.play.localaudio.dao.FileInfoDao;
import com.wotingfm.ui.play.localaudio.download.view.DownloadingFragment;
import com.wotingfm.ui.play.localaudio.model.FileInfo;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class DownloadingPresenter {

    private DownloadingFragment activity;
    private List<FileInfo> listResult ;
    private FileInfoDao FID;

    public DownloadingPresenter(DownloadingFragment activity) {
        this.activity = activity;
        FID = new FileInfoDao(activity.getActivity());
        getData();
    }

    private void getData() {
        listResult = FID.queryFileInfo("false", CommonUtils.getUserId());// 查询表中未完成的任务
        if (listResult != null && !listResult.isEmpty()) {
            activity.setData(listResult);
            activity.showContentView();
        }else{
            activity.showEmptyView();
        }
    }

    public void del(FileInfo s) {
        activity.dialogShow();
        FID.deleteFileInfo(s.id, CommonUtils.getUserId());
        listResult.remove(s);
        if (listResult.isEmpty()) {
            activity.showEmptyView();
        } else {
            activity.setData(listResult);
        }
        EventBus.getDefault().postSticky(s.id);
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
