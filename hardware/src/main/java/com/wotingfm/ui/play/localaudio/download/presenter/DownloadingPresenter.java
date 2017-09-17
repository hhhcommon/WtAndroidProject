package com.wotingfm.ui.play.localaudio.download.presenter;

import com.wotingfm.common.utils.CommonUtils;
import com.wotingfm.ui.play.localaudio.dao.FileInfoDao;
import com.wotingfm.ui.play.localaudio.download.view.DownloadingFragment;
import com.wotingfm.ui.play.localaudio.model.FileInfo;
import com.wotingfm.ui.play.localaudio.service.DownloadClient;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class DownloadingPresenter {

    private DownloadingFragment activity;
    private FileInfoDao FID;
    private List<FileInfo> list =new ArrayList<>();

    public DownloadingPresenter(DownloadingFragment activity) {
        this.activity = activity;
        FID = new FileInfoDao(activity.getActivity());
        getData();
    }

    public void getData() {
        List<FileInfo>  listResult = FID.queryFileInfo("false", CommonUtils.getUserId());// 查询表中未完成的任务
        list.clear();
        list.addAll(listResult);
        if (list != null && !list.isEmpty()) {
            activity.setData(list);
            activity.showContentView();
        }else{
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
        activity.dialogCancel();
    }

    public void itemClick(FileInfo singlesDownload){
        if (singlesDownload.download_type.trim().equals("0")) {
					/*
					 * 点击该项目时，此时是未下载状态需要把下载中状态的数据变为暂停状态暂停状态的数据不需要改变
					 * 最后把该数据状态变为开始下载中状态
					 */
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).download_type.trim().equals("1")) {
                    list.get(i).download_type="2";
                    FID.upDataDownloadStatus(list.get(i).id, "2");
                    DownloadClient.workStop(list.get(i));
                }
            }
            getFileInfo(singlesDownload);
        } else if (singlesDownload.download_type.trim().equals("1")) {
            // 点击该项目时，此时该项目的状态是下载中 只需要把项目自己变为暂停状态即可
            singlesDownload.download_type="2";
            FID.upDataDownloadStatus(singlesDownload.id, "2");
            DownloadClient.workStop(singlesDownload);
            activity.setData(list);

        } else {
            // 点击该项目时，该项目为暂停状态 把其它的播放状态变为暂停状态 最后把自己状态变为下载中状态
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).download_type.trim().equals("1")) {
                    list.get(i).download_type="2";
                    FID.upDataDownloadStatus(list.get(i).id, "2");
                    DownloadClient.workStop(list.get(i));
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
