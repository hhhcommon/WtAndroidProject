package com.wotingfm.ui.intercom.person.newfriend.presenter;

import com.wotingfm.ui.intercom.person.newfriend.model.NewFriendModel;
import com.wotingfm.ui.intercom.person.newfriend.view.NewFriendFragment;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class NewFriendPresenter {

    private final NewFriendFragment activity;
    private final NewFriendModel model;


    public NewFriendPresenter(NewFriendFragment activity) {
        this.activity = activity;
        this.model = new NewFriendModel();
    }

    public void getData() {
        activity.updateUI();
    }

}
