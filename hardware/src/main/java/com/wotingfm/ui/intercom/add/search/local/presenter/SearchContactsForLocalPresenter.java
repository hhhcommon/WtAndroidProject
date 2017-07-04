package com.wotingfm.ui.intercom.add.search.local.presenter;

import android.os.Bundle;

import com.wotingfm.common.utils.ToastUtils;
import com.wotingfm.ui.intercom.add.search.local.model.SearchContactsForLocalModel;
import com.wotingfm.ui.intercom.add.search.local.view.SearchContactsForLocalFragment;
import com.wotingfm.ui.intercom.add.search.net.view.SearchContactsForNetFragment;
import com.wotingfm.ui.intercom.group.groupnews.add.view.GroupNewsForAddFragment;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;
import com.wotingfm.ui.intercom.main.contacts.view.CharacterParser;
import com.wotingfm.ui.intercom.main.contacts.view.PinyinComparator;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;
import com.wotingfm.ui.intercom.person.personmessage.view.PersonMessageFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class SearchContactsForLocalPresenter {

    private final SearchContactsForLocalFragment activity;
    private final SearchContactsForLocalModel model;
    private List<Contact.group> srcList_G;// 原始群组数据
    private List<Contact.user> srcList_p;// 原始好友数据

    public SearchContactsForLocalPresenter(SearchContactsForLocalFragment activity) {
        this.activity = activity;
        this.model = new SearchContactsForLocalModel();
    }

    /**
     * 获取数据
     */
    public void getFriends() {
        srcList_p = model.filledData(model.getDataForPerson());
        srcList_G = model.getDataForGroup();
    }

    /**
     * 根据关键词调整展示数据
     *
     * @param s 为null或者“”
     */
    public void search(String s) {
        if (s != null && !s.trim().equals("")) {
            searchForData(s);
        } else {
            searchForNoData();
        }
    }

    // 此时没有搜索关键词的时候的数据
    private void searchForNoData() {
        activity.setViewOne();
    }

    // 此时有搜索关键词的时候的数据
    private void searchForData(String s) {
        if (srcList_G == null || srcList_G.size() == 0) {
            // 此时没有群组数据
            if (srcList_p == null || srcList_p.size() == 0) {
                // 此时没有好友》》》没有搜索数据
                activity.setView();
            } else {
                // 此时有好友》》》有搜索数据
                List<Contact.user> list = model.filterData(s, srcList_p);
                if (list.size() == 0) {
                    // 此时没有数据
                    activity.setView();
                } else {
                    // 此时个人有数据
                    activity.setViewForPerson(list);
                }
            }
        } else {
            List<Contact.group> groupList = new ArrayList<>();
            // 此时有群组数据
            for (int i = 0; i < srcList_G.size(); i++) {
                if (srcList_G.get(i).getTitle().contains(s)) {
                    groupList.add(srcList_G.get(i));
                }
            }
            if (groupList.size() == 0) {
                // 群组没有匹配数据
                if (srcList_p == null || srcList_p.size() == 0) {
                    // 此时没有好友数据
                    activity.setView();
                } else {
                    // 此时有好友数据
                    List<Contact.user> list = model.filterData(s, srcList_p);
                    if (list.size() == 0) {
                        // 此时没有数据
                        activity.setView();
                    } else {
                        // 此时个人有数据
                        activity.setViewForPerson(list);
                    }
                }
            } else {
                // 此时群组有数据
                if (srcList_p == null || srcList_p.size() == 0) {
                    // 此时群组有数据
                    activity.setViewForGroup(groupList);
                } else {
                    List<Contact.user> list = model.filterData(s, srcList_p);
                    if (list.size() == 0) {
                        // 此时群组有数据
                        activity.setViewForGroup(groupList);
                    } else {
                        // 此时群组。个人都有数据
                        activity.setViewForAll(list, groupList);
                    }
                }
            }
        }
    }


    /**
     * 跳转到好友详情
     *
     * @param position
     */
    public void jumpForPerson(List<Contact.user> person, int position) {
        String id = person.get(position).getId();
        if (id != null && !id.equals("")) {
            PersonMessageFragment fragment = new PersonMessageFragment();
            Bundle bundle = new Bundle();
            bundle.putString("id", id);
            bundle.putString("type", "true");
            fragment.setArguments(bundle);
            InterPhoneActivity.open(fragment);
        } else {
            ToastUtils.show_always(activity.getActivity(), "数据出错了，请稍后再试！");
        }
    }

    /**
     * 跳转到群组界面
     *
     * @param position
     */
    public void jumpForGroup(List<Contact.group> group, int position) {
        String id = group.get(position).getId();
        if (id != null && !id.equals("")) {
            GroupNewsForAddFragment fragment = new GroupNewsForAddFragment();
            Bundle bundle = new Bundle();
            bundle.putString("id", id);
            fragment.setArguments(bundle);
            InterPhoneActivity.open(fragment);
        } else {
            ToastUtils.show_always(activity.getActivity(), "数据出错了，请稍后再试！");
        }


    }

    /**
     * 呼叫，待对接
     *
     * @param position
     */
    public void call(int position) {

    }

    public void callOk(String id) {

    }

    /**
     * 对讲，待对接
     *
     * @param position
     */
    public void interPhone(int position) {

    }


}
