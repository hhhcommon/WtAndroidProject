package com.wotingfm.ui.play.playhistory.presenter;

import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.database.HistoryHelper;
import com.wotingfm.common.utils.T;
import com.wotingfm.ui.bean.Player;
import com.wotingfm.ui.play.find.classification.model.Classification;
import com.wotingfm.ui.play.find.classification.model.ClassificationModel;
import com.wotingfm.ui.play.find.classification.view.ClassificationFragment;
import com.wotingfm.ui.play.playhistory.adapter.PlayerHistoryListAdapter;
import com.wotingfm.ui.play.playhistory.view.PlayerHistoryFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class PlayHistoryPresenter {

    private PlayerHistoryFragment activity;
    private HistoryHelper historyHelper;
    private List<Player.DataBean.SinglesBean> list;

    public PlayHistoryPresenter(PlayerHistoryFragment activity) {
        this.activity = activity;
        getData();
    }

    private void getData() {
         historyHelper = new HistoryHelper(BSApplication.getInstance());
        if (historyHelper != null) {
             list = historyHelper.findPlayHistoryList();
            if (list != null && !list.isEmpty()) {
                activity.setData(list);
                activity.showContentView();
            } else {
                activity.showEmptyView();
            }
        } else {
            activity.showEmptyView();
        }
    }

    public void del(Player.DataBean.SinglesBean singlesBean, String id){
        T.getInstance().showToast("删除成功");
        list.remove(singlesBean);
        historyHelper.deleteTable(id);
    }

    /**
     * 数据销毁
     */
    public void destroy() {
        historyHelper = null;
    }

}
