package com.wotingfm.ui.base.model;

import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;

import java.util.List;

/**
 * 作者：xinLong on 2017/5/31 15:13
 * 邮箱：645700751@qq.com
 */
public class CommonModel {

    /**
     * 判断该用户是否是自己好友
     *
     * @param id
     * @return
     */
    public boolean judgeFriends(String id) {
        boolean b = false;
        List<Contact.user> list = GlobalStateConfig.list_person;
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                String _id = list.get(i).getId();
                if (_id != null && !_id.equals("")) {
                    if (id.equals(_id)) {
                        b = true;
                        break;
                    }
                }
            }
        }
        return b;
    }

    /**
     * 判断该群组是不是自己所在的群组
     *
     * @param id
     * @return
     */
    public boolean judgeGroups(String id) {
        boolean b = false;
        List<Contact.group> list = GlobalStateConfig.list_group;
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                String _id = list.get(i).getId();
                if (_id != null && !_id.equals("")) {
                    if (id.equals(_id)) {
                        b = true;
                        break;
                    }
                }
            }
        }
        return b;
    }

}
