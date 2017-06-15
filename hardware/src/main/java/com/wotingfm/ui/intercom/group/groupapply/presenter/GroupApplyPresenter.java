package com.wotingfm.ui.intercom.group.groupapply.presenter;

import com.wotingfm.ui.intercom.group.groupapply.view.GroupApplyFragment;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class GroupApplyPresenter {

    private final GroupApplyFragment activity;


    public GroupApplyPresenter(GroupApplyFragment activity) {
        this.activity = activity;
    }

    /**
     * 发送申请
     * @param s
     */
    public void send(String s) {
    }

    /**
     * 字数更改之后变化
     * @param src
     */
    public void textChange(String src) {

        activity.setTextViewChange("");
    }
}
