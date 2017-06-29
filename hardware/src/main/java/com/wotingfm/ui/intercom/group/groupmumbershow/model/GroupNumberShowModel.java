package com.wotingfm.ui.intercom.group.groupmumbershow.model;


import com.wotingfm.common.utils.GetTestData;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;
import java.util.List;

/**
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class GroupNumberShowModel  {

    /**
     * 测试数据
     * @return
     */
    public List<Contact.user> getData() {
        List<Contact.user> list = GetTestData.getFriendList();
        return list;
    }

}
