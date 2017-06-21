package com.wotingfm.ui.intercom.group.groupnews.add.presenter;

import com.wotingfm.ui.intercom.group.groupnews.add.model.GroupNewsForAddModel;
import com.wotingfm.ui.intercom.group.groupnews.add.view.GroupNewsForAddFragment;
import com.wotingfm.ui.intercom.group.groupnews.noadd.model.GroupNewsForNoAddModel;
import com.wotingfm.ui.intercom.group.groupnews.noadd.view.GroupNewsForNoAddFragment;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class GroupNewsForAddPresenter {

    private final GroupNewsForAddFragment activity;
    private final GroupNewsForAddModel model;


    public GroupNewsForAddPresenter(GroupNewsForAddFragment activity) {
        this.activity = activity;
        this.model = new GroupNewsForAddModel();
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
        String channel1 = "CH100-100000";
        String channel2 = "CH100-100000";
        activity.setViewData(name, number, address, introduce,channel1,channel2);

        List<Contact.user> list = model.getPersonList();
        if (list != null && list.size() > 0) {
            if(list.size()>3){
                List<Contact.user> _list = new ArrayList<>();
                for(int i=0;i<3;i++){
                    _list.add(list.get(i));
                }
                _list.add(getUser("小苹果","1",2));
                _list.add(getUser("小苹果","1",3));
                activity.setGridViewData(_list);
            }else{
                list.add(getUser("小苹果","1",2));
                list.add(getUser("小苹果","1",3));
                activity.setGridViewData(list);
            }
        } else {
            activity.setGridView();
        }
    }

    // 生成一条用户数据
    private static Contact.user getUser(String name, String id,int type) {
        Contact.user user = new Contact.user();
        user.setType(type);
        user.setName(name);
        user.setNickName(name);
        user.setId(id);
        return user;
    }

}
