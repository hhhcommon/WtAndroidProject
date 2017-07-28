package com.wotingfm.ui.intercom.main.contacts.model;

import com.wotingfm.common.utils.CommonUtils;
import com.wotingfm.ui.intercom.main.chat.dao.SearchTalkHistoryDao;
import com.wotingfm.ui.intercom.main.chat.model.DBTalkHistory;
import com.wotingfm.ui.intercom.main.contacts.fragment.ContactsFragment;
import com.wotingfm.ui.intercom.main.contacts.view.CharacterParser;
import com.wotingfm.ui.intercom.main.contacts.view.PinyinComparator;

import java.util.Collections;
import java.util.List;

/**
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class ContactsModel {
    private final ContactsFragment activity;
    private SearchTalkHistoryDao dbDao;
    private final CharacterParser characterParser;
    private final PinyinComparator pinyinComparator;

    public ContactsModel(ContactsFragment activity) {
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
     * @param id
     */
    public void del(String id){
        if(dbDao==null){
            initDao();
            dbDao.deleteHistory(id);
        }else{
            dbDao.deleteHistory(id);
        }
    }

    /**
     * 在数据库中插入一条数据
     * @param h
     */
    public void add(DBTalkHistory h){
        if(dbDao==null){
            initDao();
            dbDao.addTalkHistory(h);
        }else{
            dbDao.addTalkHistory(h);
        }
    }

    /**
     * 获取数据库中的数据
     */
    public List<DBTalkHistory> get(){
        if(dbDao==null){
            initDao();
            List<DBTalkHistory> list = dbDao.queryHistory();// 数据库存储数据
            return list;
        }else{
            List<DBTalkHistory> list = dbDao.queryHistory();// 数据库存储数据
            return list;
        }
    }

    /**
     * 组装数据库数据
     * @param s
     * @return
     */
    public DBTalkHistory assemblyData(Contact.user s,String callType, String CallTypeM) {
        String id = s.getId();
        String type = "person";
        String addTime = Long.toString(System.currentTimeMillis());
        String bjUserId = CommonUtils.getUserId();
        DBTalkHistory h = new DBTalkHistory(bjUserId, type, id, addTime,callType,CallTypeM);
        return h;
    }

    /**
     * 组装数据
     * @param userList
     */
    public List<Contact.user> assemblyData(List<Contact.user> userList) {
        try {
            if (userList != null && userList.size() > 0) {
                // 根据 a - z 进行排序源数据
                List<Contact.user> srcList = filledData(userList);
                Collections.sort(srcList, pinyinComparator);
                return srcList;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *  为 ListView 填充数据
     * @param person
     * @return
     */
    private List<Contact.user> filledData(List<Contact.user> person) {
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
    }

}
