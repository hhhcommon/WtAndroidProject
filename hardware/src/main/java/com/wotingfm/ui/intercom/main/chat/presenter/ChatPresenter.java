package com.wotingfm.ui.intercom.main.chat.presenter;

import com.wotingfm.ui.intercom.main.chat.model.ChatModel;
import com.wotingfm.ui.intercom.main.chat.view.ChatFragment;
import com.wotingfm.ui.intercom.person.newfriend.model.NewFriendModel;
import com.wotingfm.ui.intercom.person.newfriend.view.NewFriendFragment;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class ChatPresenter {

    private final ChatFragment activity;
    private final ChatModel model;


    public ChatPresenter(ChatFragment activity) {
        this.activity = activity;
        this.model = new ChatModel();
    }

    public void getData() {
        activity.updateUI();
    }

}
