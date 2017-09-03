package com.wotingfm.ui.play.localaudio.download.presenter;

import com.wotingfm.common.utils.CommonUtils;
import com.wotingfm.ui.play.localaudio.dao.FileInfoDao;
import com.wotingfm.ui.play.localaudio.download.view.DownloadingFragment;
import com.wotingfm.ui.play.localaudio.model.FileInfo;
import com.wotingfm.ui.play.localaudio.service.DownloadClient;

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

    public void getData() {
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
        activity.dialogCancel();
    }

    public void itemClick(FileInfo singlesDownload){
        if (singlesDownload.download_type.trim().equals("0")) {
					/*
					 * 点击该项目时，此时是未下载状态需要把下载中状态的数据变为暂停状态暂停状态的数据不需要改变
					 * 最后把该数据状态变为开始下载中状态
					 */
            for (int i = 0; i < listResult.size(); i++) {
                if (listResult.get(i).download_type.trim().equals("1")) {
                    listResult.get(i).download_type="2";
                    FID.upDataDownloadStatus(listResult.get(i).id, "2");
                    DownloadClient.workStop(listResult.get(i));
                }
            }
            getFileInfo(singlesDownload);
        } else if (singlesDownload.download_type.trim().equals("1")) {
            // 点击该项目时，此时该项目的状态是下载中 只需要把项目自己变为暂停状态即可
            singlesDownload.download_type="2";
            FID.upDataDownloadStatus(singlesDownload.id, "2");
            DownloadClient.workStop(singlesDownload);
            activity.setData(listResult);

        } else {
            // 点击该项目时，该项目为暂停状态 把其它的播放状态变为暂停状态 最后把自己状态变为下载中状态
            for (int i = 0; i < listResult.size(); i++) {
                if (listResult.get(i).download_type.trim().equals("1")) {
                    listResult.get(i).download_type="2";
                    FID.upDataDownloadStatus(listResult.get(i).id, "2");
                    DownloadClient.workStop(listResult.get(i));
                }
            }
            getFileInfo(singlesDownload);
        }
    }

    // 给 fileInfo 初值
    private void getFileInfo(FileInfo fileInfo) {
        fileInfo.download_type="1";
        FID.upDataDownloadStatus(fileInfo.id, "1");
        DownloadClient.workStart(fileInfo);
    }

    /**
     * 数据销毁
     */
    public void destroy() {
        FID.closeDB();
        FID = null;
    }

}
