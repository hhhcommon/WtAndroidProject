package com.wotingfm.ui.mine.message.megcenter.presenter;

import com.wotingfm.ui.mine.message.megcenter.model.MsgCenterModel;
import com.wotingfm.ui.mine.message.megcenter.view.MsgCenterFragment;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class MsgCenterPresenter {

    private final MsgCenterFragment activity;
    private final MsgCenterModel model;

    public MsgCenterPresenter(MsgCenterFragment activity) {
        this.activity = activity;
        this.model = new MsgCenterModel();
    }



}
