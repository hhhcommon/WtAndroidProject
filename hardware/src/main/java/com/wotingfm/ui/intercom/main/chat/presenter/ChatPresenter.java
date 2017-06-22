package com.wotingfm.ui.intercom.main.chat.presenter;

import com.wotingfm.ui.intercom.main.chat.model.ChatModel;
import com.wotingfm.ui.intercom.main.chat.view.ChatFragment;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;
import java.util.List;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class ChatPresenter {

    private final ChatFragment activity;
    private final ChatModel model;
    private List<Contact.user> list;


    public ChatPresenter(ChatFragment activity) {
        this.activity = activity;
        this.model = new ChatModel();
    }

    public void getData() {
       list= model.getData();

        activity.updateUI(list);
    }

}
