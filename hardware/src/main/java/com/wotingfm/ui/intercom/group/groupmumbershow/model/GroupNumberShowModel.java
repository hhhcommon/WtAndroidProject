package com.wotingfm.ui.intercom.group.groupmumbershow.model;


import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.utils.GetTestData;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class GroupNumberShowModel {

    /**
     * 测试数据
     *
     * @return
     */
    public List<Contact.user> getData() {
        List<Contact.user> list = GetTestData.getFriendList();
        try {
            list.get(0).setType(1);
            list.get(1).setType(2);
            list.get(2).setType(2);
            list.get(3).setType(2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 判断是不是自己好友
     *
     * @param list
     * @param position
     * @return
     */
    public boolean isFriend(List<Contact.user> list, int position) {
        boolean b = false;
        if (list != null && list.size() > 0) {
            Contact.user u = list.get(position);
            if (u != null) {
                String id = u.getId();
                if (id != null && !id.trim().equals("")) {
                    List<Contact.user> _list = GlobalStateConfig.list_person;
                    if (_list != null && _list.size() > 0) {
                        for (int i = 0; i < _list.size(); i++) {
                            String _id = _list.get(i).getId();
                            if (_id.equals(id)) {
                                b = true;
                                break;
                            }
                        }
                    }
                }
            }
        }
        return b;
    }

    /**
     * 组装数据
     *
     * @param list
     */
    public List<Contact.user> assemblyData(List<Contact.user> list) {
        List<Contact.user> _list = new ArrayList<>();
        // 添加群主
        for (int i = 0; i < list.size(); i++) {
            boolean b = list.get(i).is_owner();
            if (b) {
                _list.add(list.get(i));
                break;
            }
        }

        // 添加管理员
        for (int i = 0; i < list.size(); i++) {
            boolean b = list.get(i).is_admin();
            if (b) {
                _list.add(list.get(i));
            }
        }

        // 添加成员
        for (int i = 0; i < list.size(); i++) {
            boolean b = list.get(i).is_admin();
            boolean b1 = list.get(i).is_owner();
            if (!b && !b1) {
                _list.add(list.get(i));
            }
        }
        return _list;
    }


}
