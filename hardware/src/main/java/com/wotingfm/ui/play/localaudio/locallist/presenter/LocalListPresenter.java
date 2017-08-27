package com.wotingfm.ui.play.localaudio.locallist.presenter;

import android.os.Bundle;
import android.text.TextUtils;

import com.woting.commonplat.utils.FileSizeUtil;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.database.DownloadHelper;
import com.wotingfm.common.database.HistoryHelper;
import com.wotingfm.common.utils.T;
import com.wotingfm.ui.bean.Player;
import com.wotingfm.ui.bean.SinglesDownload;
import com.wotingfm.ui.play.localaudio.locallist.view.LocalListFragment;
import com.wotingfm.ui.play.playhistory.view.PlayerHistoryFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class LocalListPresenter {

    private  DownloadHelper downloadHelper;
    private LocalListFragment activity;
    private List<SinglesDownload> list;

    public LocalListPresenter(LocalListFragment activity) {
        this.activity = activity;
        downloadHelper = new DownloadHelper(BSApplication.getInstance());
        getData();
    }

    private void getData() {
        Bundle bundle = activity.getArguments();
        if (bundle != null) {
             list = (List<SinglesDownload>)bundle.getSerializable("list");
            if (list != null && !list.isEmpty()) {
                activity.setData(list);
                activity.showContentView();
            } else {
                activity.showEmptyView();
            }

            String name=bundle.getString("name");
            if(!TextUtils.isEmpty(name)){
                activity.setName(name);
            }else{
                activity.setName("列表");
            }
        }else{
            activity.showEmptyView();
        }
    }

    public void del(SinglesDownload s){
        activity.dialogShow();
        downloadHelper.deleteTable(s.id);
        FileSizeUtil.delFile(s.single_file_url);
        list.remove(s);

        if (list.isEmpty()) {
            activity.showEmptyView();
        }else{
            activity.setData(list);
        }
        EventBus.getDefault().postSticky(s.id);
        activity. dialogCancel();
        activity.setResult(true);
    }

    /**
     * 数据销毁
     */
    public void destroy() {
        downloadHelper = null;
    }

}
