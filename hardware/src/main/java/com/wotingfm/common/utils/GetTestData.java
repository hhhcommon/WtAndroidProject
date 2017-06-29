package com.wotingfm.common.utils;

import com.wotingfm.ui.intercom.main.contacts.model.Contact;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：xinLong on 2017/6/12 14:39
 * 邮箱：645700751@qq.com
 */
public class GetTestData {

    public static List<Contact.group>  getGroupList() {
        List<Contact.group> srcList_G = new ArrayList<>();
        srcList_G.add(getGroup("兔子群-1","1"));
        srcList_G.add(getGroup("野鸡大学群-2","2"));
        srcList_G.add(getGroup("三国群-3","3"));
        srcList_G.add(getGroup("相亲交友群-4","4"));
        srcList_G.add(getGroup("一路一带群-5","5"));
        srcList_G.add(getGroup("丝绸之路群-6","6"));
        srcList_G.add(getGroup("辽宁航母群-7","7"));
        srcList_G.add(getGroup("G20峰会群-8","8"));
        srcList_G.add(getGroup("我听郊游群-9","9"));
        srcList_G.add(getGroup("临时群-10","10"));
        srcList_G.add(getGroup("临时群-11","11"));
        srcList_G.add(getGroup("临时群-12","12"));

        return  srcList_G;
    }

    public static List<Contact.user>  getFriendList() {
        List<Contact.user> srcList_p = new ArrayList<>();
        srcList_p.add(getUser("小苹果","1"));
        srcList_p.add(getUser("大美丽","2"));
        srcList_p.add(getUser("放羊佬","3"));
        srcList_p.add(getUser("阿富汗","4"));
        srcList_p.add(getUser("奔驰","5"));
        srcList_p.add(getUser("冲天一怒为红颜","6"));
        srcList_p.add(getUser("东风拖拉机","7"));
        srcList_p.add(getUser("易中天","8"));
        srcList_p.add(getUser("京东大哥","9"));
        srcList_p.add(getUser("海尔兄弟","10"));
        srcList_p.add(getUser("卡夫卡","11"));
        srcList_p.add(getUser("刘德华","12"));
        srcList_p.add(getUser("面条爱上包子","13"));
        srcList_p.add(getUser("我的天呀","14"));

        return srcList_p;
    }

    // 生成一条用户数据
    private static Contact.user getUser(String name, String id) {
        Contact.user user = new Contact.user();
        user.setName(name);
        user.setId(id);
        return user;
    }

    // 生成一条组数据
    private static Contact.group getGroup(String name, String id) {
        Contact.group group = new Contact.group();
        group.setTitle(name);
        group.setId(id);
        return group;
    }
}
