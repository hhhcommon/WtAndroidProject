package com.wotingfm.ui.intercom.group.groupchat.presenter;

import com.wotingfm.common.utils.GetTestData;
import com.wotingfm.ui.intercom.group.groupchat.model.GroupChat;
import com.wotingfm.ui.intercom.group.groupchat.model.GroupChatModel;
import com.wotingfm.ui.intercom.group.groupchat.view.GroupChatFragment;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;
import com.wotingfm.ui.intercom.person.newfriend.model.NewFriendModel;
import com.wotingfm.ui.intercom.person.newfriend.view.NewFriendFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class GroupChatPresenter {

    private final GroupChatFragment activity;
    private final GroupChatModel model;

    public GroupChatPresenter(GroupChatFragment activity) {
        this.activity = activity;
        this.model = new GroupChatModel();
    }

    public void getData() {
        activity.setView(model.getData());
    }

}
