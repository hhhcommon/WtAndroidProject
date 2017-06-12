package com.wotingfm.ui.intercom.add.search.local.presenter;

import com.wotingfm.ui.intercom.add.search.local.model.SearchContactsForLocalModel;
import com.wotingfm.ui.intercom.add.search.local.view.SearchContactsForLocalFragment;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;
import com.wotingfm.ui.intercom.main.contacts.view.CharacterParser;
import com.wotingfm.ui.intercom.main.contacts.view.PinyinComparator;

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
    private final CharacterParser characterParser;
    private final PinyinComparator pinyinComparator;
    private List<Contact.group> srcList_G;// 原始群组数据
    private List<Contact.user> srcList_p ;// 原始好友数据



    public SearchContactsForLocalPresenter(SearchContactsForLocalFragment activity) {
        this.activity = activity;
        this.model = new SearchContactsForLocalModel();

        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
    }

    /**
     * 获取数据
     */
    public void getFriends() {
        srcList_p= model.getDataForPerson();
        srcList_G=  model.getDataForGroup();
    }

    /**
     * 根据关键词调整展示数据
     * @param s 为null或者“”
     */
    public void search(String s){
        if(s!=null&&!s.trim().equals("")){
            searchForData(s);
        }else{
            searchForNoData();
        }
    }

    // 此时没有搜索关键词的时候的数据
    private void searchForNoData(){
        activity.setViewForOnce(srcList_p,srcList_G);
    }

    // 此时有搜索关键词的时候的数据
    private void searchForData(String s){
        if (srcList_G == null || srcList_G.size() == 0) {
            // 此时没有群组数据
            if (srcList_p == null || srcList_p.size() == 0) {
                // 此时没有好友》》》没有搜索数据
                activity.setView();
            } else {
                // 此时有好友》》》有搜索数据
                List<Contact.user> list = filterData(s);
                if (list.size() == 0) {
                    // 此时没有数据
                    activity.setView();
                } else {
                    // 此时个人有数据
                    activity.setViewForPerson(list);
                }
            }
        } else {
            List<Contact.group> groupList=new ArrayList<>() ;
            // 此时有群组数据
            for (int i = 0; i < srcList_G.size(); i++) {
                if (srcList_G.get(i).getName().contains(s)) {
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
                    List<Contact.user>  list = filterData(s);
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
                    List<Contact.user> list = filterData(s);
                    if (list.size() == 0) {
                        // 此时群组有数据
                        activity.setViewForGroup(groupList);
                    } else {
                        // 此时群组。个人都有数据
                        activity.setViewForAll(list,groupList);
                    }
                }
            }
        }
    }

    // 为 ListView 填充数据
    private List<Contact.user>  filledData(List<Contact.user> person) {
        for (int i = 0; i < person.size(); i++) {
            person.get(i).setName(person.get(i).getNickName());
            // 汉字转换成拼音
            String pinyin = characterParser.getSelling(person.get(i).getNickName());
            String sortString = pinyin.substring(0, 1).toUpperCase();
            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                person.get(i).setSortLetters(sortString.toUpperCase());
            } else {
                person.get(i).setSortLetters("#");
            }
        }
        return person;
    }

    // 根据输入框中的值来过滤数据并更新 ListView
    private List<Contact.user> filterData(String filterStr) {
        List<Contact.user> filterDateList = new ArrayList<>();
        filterDateList.clear();
        for (Contact.user sortModel : srcList_p) {
            String name = sortModel.getName();
            if (name.contains(filterStr) || characterParser.getSelling(name).startsWith(filterStr)) {
                filterDateList.add(sortModel);
            }
        }
        // 根据 a - z 进行排序
        Collections.sort(filterDateList, pinyinComparator);
        return filterDateList;
    }
}
