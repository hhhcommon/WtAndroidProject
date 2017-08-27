package com.wotingfm.ui.play.localaudio.download.presenter;

import com.woting.commonplat.utils.FileSizeUtil;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.database.DownloadHelper;
import com.wotingfm.ui.bean.SinglesDownload;
import com.wotingfm.ui.play.localaudio.download.view.DownloadingFragment;
import org.greenrobot.eventbus.EventBus;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class DownloadingPresenter {

    private DownloadHelper downloadHelper;
    private DownloadingFragment activity;
    private List<SinglesDownload> listResult = new ArrayList<>();

    public DownloadingPresenter(DownloadingFragment activity) {
        this.activity = activity;
        downloadHelper = new DownloadHelper(BSApplication.getInstance());
        getData();
    }

    private void getData() {
        downloadHelper = new DownloadHelper(BSApplication.getInstance());
        if (downloadHelper != null) {
            final List<SinglesDownload> list = downloadHelper.findPlayHistoryList();
            if (list != null && !list.isEmpty()) {
                for (int i = 0, size = list.size(); i < size; i++) {
                    if (list.get(i).isDownloadOver == false)
                        listResult.add(list.get(i));
                }
                if (listResult.isEmpty()) {
                    activity.showEmptyView();
                } else {
                    activity.setData(listResult);
                    activity.showContentView();
                }
            } else {
                activity.showEmptyView();
            }
        } else {
            activity.showEmptyView();
        }
    }

    public void del(SinglesDownload s) {
        activity.dialogShow();
        downloadHelper.deleteTable(s.id);
        FileSizeUtil.delFile(s.single_file_url);
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
        downloadHelper = null;
    }

}
