package com.wotingfm.ui.intercom.add.search.local.model;

import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.utils.CommonUtils;
import com.wotingfm.ui.intercom.add.search.local.view.SearchContactsForLocalFragment;
import com.wotingfm.ui.intercom.group.groupchat.view.GroupChatFragment;
import com.wotingfm.ui.intercom.main.chat.dao.SearchTalkHistoryDao;
import com.wotingfm.ui.intercom.main.chat.model.DBTalkHistory;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;
import com.wotingfm.ui.intercom.main.contacts.view.CharacterParser;
import com.wotingfm.ui.intercom.main.contacts.view.PinyinComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class SearchContactsForLocalModel {
    private final CharacterParser characterParser;
    private final PinyinComparator pinyinComparator;
    private final SearchContactsForLocalFragment activity;
    private SearchTalkHistoryDao dbDao;

    public SearchContactsForLocalModel(SearchContactsForLocalFragment activity) {
        this.activity = activity;
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        initDao();      // 初始化数据库
    }

    // 初始化数据库
    private void initDao() {
        dbDao = new SearchTalkHistoryDao(activity.getActivity());
    }

    /**
     * 从数据库中删除一条数据
     *
     * @param id
     */
    public void del(String id) {
        if (dbDao == null) {
            initDao();
            dbDao.deleteHistory(id);
        } else {
            dbDao.deleteHistory(id);
        }
    }

    /**
     * 在数据库中插入一条数据
     *
     * @param h
     */
    public void add(DBTalkHistory h) {
        if (dbDao == null) {
            initDao();
            dbDao.addTalkHistory(h);
        } else {
            dbDao.addTalkHistory(h);
        }
    }

    /**
     * 组装数据库数据
     *
     * @param s
     * @return
     */
    public DBTalkHistory assemblyGroupData(Contact.group s, String callType, String CallTypeM) {
        String id = s.getId();
        String type = "group";
        String addTime = Long.toString(System.currentTimeMillis());
        String bjUserId = CommonUtils.getUserId();
        DBTalkHistory h = new DBTalkHistory(bjUserId, type, id, addTime, callType, CallTypeM, s.getRoomId());
        return h;
    }

    /**
     * 组装数据库数据
     *
     * @param s
     * @return
     */
    public DBTalkHistory assemblyPersonData(Contact.user s, String callType, String CallTypeM) {
        String id = s.getId();
        String type = "person";
        String addTime = Long.toString(System.currentTimeMillis());
        String bjUserId = CommonUtils.getUserId();
        DBTalkHistory h = new DBTalkHistory(bjUserId, type, id, addTime, callType, CallTypeM, s.getAcc_id());
        return h;
    }

    /**
     * 获取好友
     */
    public List<Contact.user> getDataForPerson() {
        List<Contact.user> srcList_p = GlobalStateConfig.list_person;
        return srcList_p;
    }

    /**
     * 获取群组
     */
    public List<Contact.group> getDataForGroup() {
        List<Contact.group> srcList_G = GlobalStateConfig.list_group;
        return srcList_G;
    }

    /**
     * 为 ListView 填充数据
     *
     * @param person
     * @return
     */
    public List<Contact.user> filledData(List<Contact.user> person) {
        if (person != null && person.size() > 0) {
            for (int i = 0; i < person.size(); i++) {
                person.get(i).setName(person.get(i).getName());
                // 汉字转换成拼音
                String pinyin = characterParser.getSelling(person.get(i).getName());
                String sortString = pinyin.substring(0, 1).toUpperCase();
                // 正则表达式，判断首字母是否是英文字母
                if (sortString.matches("[A-Z]")) {
                    person.get(i).setSortLetters(sortString.toUpperCase());
                } else {
                    person.get(i).setSortLetters("#");
                }
            }
            return person;
        } else {
            return null;
        }
    }

    /**
     * 根据输入框中的值来过滤数据
     *
     * @param filterStr
     * @param srcList_p
     * @return
     */
    public List<Contact.user> filterData(String filterStr, List<Contact.user> srcList_p) {
        List<Contact.user> filterDateList = new ArrayList<>();
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
