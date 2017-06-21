package com.wotingfm.ui.intercom.group.groupnews.noadd.presenter;

import com.wotingfm.ui.intercom.group.groupnews.noadd.model.GroupNewsForNoAddModel;
import com.wotingfm.ui.intercom.group.groupnews.noadd.view.GroupNewsForNoAddFragment;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;
import com.wotingfm.ui.intercom.person.newfriend.model.NewFriendModel;
import com.wotingfm.ui.intercom.person.newfriend.view.NewFriendFragment;

import java.util.List;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class GroupNewsForNoAddPresenter {

    private final GroupNewsForNoAddFragment activity;
    private final GroupNewsForNoAddModel model;


    public GroupNewsForNoAddPresenter(GroupNewsForNoAddFragment activity) {
        this.activity = activity;
        this.model = new GroupNewsForNoAddModel();
    }

    /**
     * 获取数据设置界面数据
     */
    public void getData() {

        model.getData();
        String name = "朝阳钓鱼";
        String number = "518518";
        String address = "北京朝阳";
        String introduce = "这是一个钓鱼交流群";
        activity.setViewData(name, number, address, introduce);

        List<Contact.user> list = model.getPersonList();
        if (list != null && list.size() > 0) {
            activity.setGridViewData(list);
        } else {
            activity.setGridView();
        }
    }

}
