package com.wotingfm.ui.mine.fm.presenter;

import com.wotingfm.ui.mine.fm.model.FMInfo;
import com.wotingfm.ui.mine.fm.model.FMSetModel;
import com.wotingfm.ui.mine.fm.view.FMSetFragment;

import java.util.List;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class FMSetPresenter {

    private final FMSetFragment activity;
    private final FMSetModel model;


    public FMSetPresenter(FMSetFragment activity) {
        this.activity = activity;
        this.model = new FMSetModel();
    }

    /**
     * 设置界面数据
     */
    public void setView() {
        List<FMInfo> list = model.getTestData();
        if (list != null && list.size() > 0) {
            activity.setViewData(list);
        }
    }


}
