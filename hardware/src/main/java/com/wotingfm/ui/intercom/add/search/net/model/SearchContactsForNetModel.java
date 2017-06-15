package com.wotingfm.ui.intercom.add.search.net.model;

import com.wotingfm.common.utils.GetTestData;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class SearchContactsForNetModel {

    /**
     * 获取好友
     */
    public  List<Contact.user> getDataForPerson() {
        List<Contact.user> srcList_p= GetTestData.getFriendList();
        return srcList_p;
    }

    /**
     * 获取群组
     */
    public List<Contact.group> getDataForGroup() {
        List<Contact.group>  srcList_G=GetTestData.getGroupList() ;
        return srcList_G;
    }

}
