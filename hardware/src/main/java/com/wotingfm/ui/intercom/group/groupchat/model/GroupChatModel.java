package com.wotingfm.ui.intercom.group.groupchat.model;

import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.constant.StringConstant;
import com.wotingfm.common.utils.CommonUtils;
import com.wotingfm.ui.base.model.UserInfo;
import com.wotingfm.ui.intercom.group.groupchat.view.GroupChatFragment;
import com.wotingfm.ui.intercom.main.chat.dao.SearchTalkHistoryDao;
import com.wotingfm.ui.intercom.main.chat.model.DBTalkHistory;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class GroupChatModel extends UserInfo {
    private final GroupChatFragment activity;
    private SearchTalkHistoryDao dbDao;

    public GroupChatModel(GroupChatFragment activity) {
        this.activity = activity;
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
     * 组装数据库数据
     * @param s
     * @return
     */
    public DBTalkHistory assemblyData(Contact.user s) {
        String id = s.getId();
        String type = "person";
        String addTime = Long.toString(System.currentTimeMillis());
        String bjUserId = CommonUtils.getUserId();
        DBTalkHistory h = new DBTalkHistory(bjUserId, type, id, addTime);
        return h;
    }

    /**
     * 组装测试展示数据
     *
     * @return
     */
    public List<GroupChat> getTestData() {
        List<GroupChat> list = new ArrayList<>();
        GroupChat GC = new GroupChat();
        GC.setGroupNumber("12");
        GC.setName("我创建的群");
        GC.setPerson(getGroupList());
        list.add(GC);
        GroupChat GC1 = new GroupChat();
        GC1.setGroupNumber("12");
        GC1.setName("我加入的群");
        GC1.setPerson(getGroupList());
        list.add(GC1);
        return list;
    }

    private ArrayList<Contact.group> getGroupList() {
        ArrayList<Contact.group> srcList_G = new ArrayList<>();
        srcList_G.add(getGroup("兔子群-1", "1"));
        srcList_G.add(getGroup("野鸡大学群-2", "2"));
        srcList_G.add(getGroup("三国群-3", "3"));
        srcList_G.add(getGroup("相亲交友群-4", "4"));
        srcList_G.add(getGroup("一路一带群-5", "5"));
        srcList_G.add(getGroup("丝绸之路群-6", "6"));
        srcList_G.add(getGroup("辽宁航母群-7", "7"));
        srcList_G.add(getGroup("G20峰会群-8", "8"));
        srcList_G.add(getGroup("我听郊游群-9", "9"));
        srcList_G.add(getGroup("临时群-10", "10"));
        srcList_G.add(getGroup("临时群-11", "11"));
        srcList_G.add(getGroup("临时群-12", "12"));

        return srcList_G;
    }

    // 生成一条组数据
    private Contact.group getGroup(String name, String id) {
        Contact.group group = new Contact.group();
        group.setTitle(name);
        group.setId(id);
        return group;
    }

    /**
     * 组装实际数据
     *
     * @return
     */
    public List<GroupChat> assemblyData() {
        List<Contact.group> list = GlobalStateConfig.list_group;
        if (list != null && list.size() > 0) {
            String id = BSApplication.SharedPreferences.getString(StringConstant.USER_ID, "");
            ArrayList<Contact.group> srcList_G = new ArrayList<>();// 我加入的群
            ArrayList<Contact.group> srcList_O = new ArrayList<>();// 我创建的群
            if (id != null && !id.equals("")) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getOwner_id() != null && !list.get(i).getOwner_id().equals("") && list.get(i).getOwner_id().trim().equals(id)) {
                        srcList_O.add(list.get(i));
                    } else {
                        srcList_G.add(list.get(i));
                    }
                }

                List<GroupChat> g_list = new ArrayList<>();

                // 我创建的群
                if (srcList_O != null && srcList_O.size() > 0) {
                    GroupChat GC = new GroupChat();
                    GC.setGroupNumber(String.valueOf(srcList_O.size()));
                    GC.setName("我创建的群");
                    GC.setPerson(srcList_O);
                    g_list.add(GC);

                }
                // 我加入的群
                if (srcList_G != null && srcList_G.size() > 0) {
                    GroupChat GC1 = new GroupChat();
                    GC1.setGroupNumber(String.valueOf(srcList_G.size()));
                    GC1.setName("我加入的群");
                    GC1.setPerson(srcList_G);
                    g_list.add(GC1);
                }

                if (g_list != null && g_list.size() > 0) {
                    return g_list;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

}
