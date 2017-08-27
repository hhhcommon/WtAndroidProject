package com.wotingfm.ui.play.localaudio.local.presenter;

import com.woting.commonplat.utils.FileSizeUtil;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.database.DownloadHelper;
import com.wotingfm.common.database.HistoryHelper;
import com.wotingfm.common.utils.T;
import com.wotingfm.ui.bean.Player;
import com.wotingfm.ui.bean.SinglesDownload;
import com.wotingfm.ui.play.localaudio.local.model.AlbumsModel;
import com.wotingfm.ui.play.localaudio.local.view.AlbumsFragment;
import com.wotingfm.ui.play.playhistory.view.PlayerHistoryFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class AlbumsPresenter {

    private AlbumsModel model;
    private AlbumsFragment activity;
    private DownloadHelper downloadHelper;
    private List<SinglesDownload> listResult;

    public AlbumsPresenter(AlbumsFragment activity) {
        this.activity = activity;
        this.model = new AlbumsModel();
        getData();
    }

    public void getData() {
        downloadHelper = new DownloadHelper(BSApplication.getInstance());
        if (downloadHelper != null) {
             List<SinglesDownload> list = downloadHelper.findPlayHistoryList();
            if (list != null && !list.isEmpty()) {
                listResult = new ArrayList<>();
                for (int i = 0, size = list.size(); i < size; i++) {
                    SinglesDownload s = list.get(i);
                    if (s.isDownloadOver) {
                        SinglesDownload so = model.getMapContent(s.album_id);
                        List<SinglesDownload> sos = model.getMapContentList(s.album_id);
                        if (so != null && so.album_id != null && so.album_id.equals(s.album_id)) {
                            so.count = s.count + 1;
                            so.albumSize = so.albumSize + s.albumSize;
                            model.mapPut(s.album_id, so);
                            sos.add(so);
                            model.mapListPut(s.album_id, sos);

                        } else {
                            listResult.add(s);
                            model.mapPut(s.album_id, s);
                            sos.add(s);
                            model.mapListPut(s.album_id, sos);
                        }
                    }
                }
                if (listResult != null && !listResult.isEmpty()) {
                    activity.setData(listResult, model.getMap(), model.getMapList());
                } else {
                    activity.showEmptyView();
                }
            } else {
                activity.showEmptyView();
            }
        } else {
            activity.showEmptyView();
        }
    }

    public void del(SinglesDownload s,List<SinglesDownload> singlesDownloads){
        activity.dialogShow();
        downloadHelper.deleteTable(s.id);
        for (int i = 0; i < singlesDownloads.size(); i++) {
            FileSizeUtil.delFile(singlesDownloads.get(i).single_file_url);
        }
        listResult.remove(s);
        if (listResult.isEmpty()) {
            activity.showEmptyView();
        }else{
            activity.setData(listResult, model.getMap(), model.getMapList());
        }
        activity. dialogCancel();
        EventBus.getDefault().postSticky(s.id);
    }

    /**
     * 数据销毁
     */
    public void destroy() {
        downloadHelper = null;
        model=null;
    }

}
