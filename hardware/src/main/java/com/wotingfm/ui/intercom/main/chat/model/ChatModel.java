package com.wotingfm.ui.intercom.main.chat.model;

import android.util.Log;

import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.constant.StringConstant;
import com.wotingfm.common.utils.CommonUtils;
import com.wotingfm.common.utils.GetTestData;
import com.wotingfm.ui.base.model.CommonModel;
import com.wotingfm.ui.intercom.main.chat.dao.SearchTalkHistoryDao;
import com.wotingfm.ui.intercom.main.chat.view.ChatFragment;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class ChatModel extends CommonModel {
    private final ChatFragment activity;
    private SearchTalkHistoryDao dbDao;

    public ChatModel(ChatFragment activity) {
        this.activity = activity;
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
     * 从list删除id重复数据
     *
     * @param id
     */
    public List<TalkHistory> delForList(List<TalkHistory> list, String id) {
        int j = 0;
        boolean t = false;
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                String _id = list.get(i).getID().trim();
                if (_id != null && !_id.equals("")) {
                    if (id.equals(_id)) {
                        j = i;
                        t = true;
                    }
                }
            }
            if (t) {
                list.remove(j);
                return list;
            } else {
                return list;
            }
        } else {
            return null;
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
     * 获取数据库中的数据
     */
    public List<DBTalkHistory> get() {
        if (dbDao == null) {
            initDao();
            List<DBTalkHistory> list = dbDao.queryHistory();// 数据库存储数据
            return list;
        } else {
            List<DBTalkHistory> list = dbDao.queryHistory();// 数据库存储数据
            return list;
        }
    }

    /**
     * 获取数据库中的第一条数据
     */
    public DBTalkHistory getForFirst() {
        if (dbDao == null) {
            initDao();
            List<DBTalkHistory> list = dbDao.queryHistory();// 数据库存储数据
            if (list != null && list.size() > 0) {
                DBTalkHistory s = list.remove(0);
                return s;
            } else {
                return null;
            }
        } else {
            List<DBTalkHistory> list = dbDao.queryHistory();// 数据库存储数据
            if (list != null && list.size() > 0) {
                DBTalkHistory s = list.remove(0);
                return s;
            } else {
                return null;
            }
        }
    }

    /**
     * 组装数据库数据
     *
     * @param s
     * @return
     */
    public DBTalkHistory assemblyData(TalkHistory s, String CallType, String CallTypeM) {
        String id = s.getID();
        String type = s.getTyPe();
        String addTime = Long.toString(System.currentTimeMillis());
        String bjUserId = CommonUtils.getUserId();
        DBTalkHistory h = new DBTalkHistory(bjUserId, type, id, addTime, CallType, CallTypeM);
        return h;
    }

    /**
     * 获取展示数据
     *
     * @return
     */
    public List<TalkHistory> getData() {
        if (GlobalStateConfig.test) {
            // 测试数据
            List<DBTalkHistory> list = get();// 数据库存储数据
            List<Contact.user> p_list = GetTestData.getFriendList();
            List<Contact.group> g_list = GetTestData.getGroupList();
            List<TalkHistory> data = new ArrayList<>();
            // 组装
            for (int i = 0; i < list.size(); i++) {
                String id = list.get(i).getID();
                if (id != null && !id.trim().equals("")) {
                    String type = list.get(i).getTyPe();
                    if (type != null && !type.trim().equals("") && type.equals("person")) {
                        // 是否是好友
                        for (int j = 0; j < p_list.size(); j++) {
                            String pid = p_list.get(j).getId();
                            if (pid != null && !pid.trim().equals("")) {
                                if (pid.equals(id)) {
                                    data.add(assemblyDataForPerson(p_list.get(j), list.get(i)));
                                    break;
                                }
                            }
                        }
                    } else if (type != null && !type.trim().equals("") && type.equals("group")) {
                        // 是否是群组
                        for (int j = 0; j < g_list.size(); j++) {
                            String gid = g_list.get(j).getId();
                            if (gid != null && !gid.trim().equals("")) {
                                if (gid.equals(id)) {
                                    data.add(assemblyDataForGroup(g_list.get(j), list.get(i)));
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            return data;
        } else {
            List<DBTalkHistory> list = get();// 数据库存储数据
            Log.e("222222", "list大小" + list.size());
            List<Contact.user> p_list = GlobalStateConfig.list_person;// 好友列表
            List<Contact.group> g_list = GlobalStateConfig.list_group;// 群组列表
            List<TalkHistory> data = new ArrayList<>();// 组装后的数据

            // 组装
            for (int i = 0; i < list.size(); i++) {
                String id = list.get(i).getID();
                if (id != null && !id.trim().equals("")) {
                    String type = list.get(i).getTyPe();
                    if (type != null && !type.trim().equals("") && type.equals("person")) {
                        // 是否是好友
                        if (p_list != null && p_list.size() > 0) {
                            for (int j = 0; j < p_list.size(); j++) {
                                String pid = p_list.get(j).getId();
                                if (pid != null && !pid.trim().equals("")) {
                                    if (pid.equals(id)) {
                                        data.add(assemblyDataForPerson(p_list.get(j), list.get(i)));
                                        break;
                                    }
                                }
                            }
                        }

                    } else if (type != null && !type.trim().equals("") && type.equals("group")) {
                        // 是否是群组
                        if (g_list != null && g_list.size() > 0) {
                            for (int j = 0; j < g_list.size(); j++) {
                                String gid = g_list.get(j).getId();
                                if (gid != null && !gid.trim().equals("")) {
                                    if (gid.equals(id)) {
                                        data.add(assemblyDataForGroup(g_list.get(j), list.get(i)));
                                        break;
                                    }
                                }
                            }
                        }

                    } else {
                        Log.e("222222", "type为空");
                    }
                } else {
                    Log.e("222222", "id为空");
                }
            }

            return data;
        }
    }

    /**
     * 组装好友数据
     *
     * @param s
     * @param h
     * @return
     */
    public TalkHistory assemblyDataForPerson(Contact.user s, DBTalkHistory h) {
        if (s != null) {
            if (h == null) {
                // 测试数据
                TalkHistory data = new TalkHistory();
                data.setName(s.getName());
                data.setID(s.getId());
                data.setTyPe("person");
                data.setURL(s.getAvatar());
                String addTime = Long.toString(System.currentTimeMillis());
                data.setAddTime(addTime);
                data.setCallType("ok");
                data.setCallTypeM("已接受");
                return data;
            } else {
                // 实际数据
                TalkHistory data = new TalkHistory();
                data.setName(s.getName());
                data.setID(s.getId());
                data.setTyPe("person");
                data.setURL(s.getAvatar());
                data.setAddTime(h.getAddTime());
                data.setCallType(h.getCallType());
                data.setCallTypeM(h.getCallTypeM());
                return data;
            }
        } else {
            return null;
        }
    }

    // 组装群组数据
    public TalkHistory assemblyDataForGroup(Contact.group s, DBTalkHistory h) {
        if (s != null) {
            if (h == null) {
                // 测试数据
                TalkHistory data = new TalkHistory();
                data.setName(s.getTitle());
                data.setID(s.getId());
                data.setTyPe("group");
                data.setURL(s.getLogo_url());
                String addTime = Long.toString(System.currentTimeMillis());
                data.setAddTime(addTime);
                data.setGroupNum(s.getMember_num());
                return data;
            } else {
                // 实际数据
                TalkHistory data = new TalkHistory();
                data.setName(s.getTitle());
                data.setID(s.getId());
                data.setTyPe("group");
                data.setURL(s.getLogo_url());
                data.setAddTime(h.getAddTime());
                data.setGroupNum(s.getMember_num());
                return data;
            }
        } else {
            return null;
        }
    }

    /**
     * 根据id得到用户数据
     *
     * @param id
     * @return
     */
    public Contact.user getUser(String id) {
        if (id != null && !id.trim().equals("")) {
            List<Contact.user> list = GlobalStateConfig.list_person;
            if (list != null && list.size() > 0) {
                Contact.user u = getU(list, id);
                return u;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    // 判断是否有好友数据
    private Contact.user getU(List<Contact.user> list, String id) {
        boolean t = false;
        int j = 0;
        for (int i = 0; i < list.size(); i++) {
            String _id = list.get(i).getId();
            if (_id != null && !_id.equals("")) {
                if (_id.equals(id)) {
                    t = true;
                    j = i;
                }
            }
        }
        if (t) {
            return list.get(j);
        } else {
            return null;
        }
    }

    /**
     * 根据id得到群组数据
     *
     * @param id
     * @return
     */
    public Contact.group getGroup(String id) {
        if (id != null && !id.trim().equals("")) {
            List<Contact.group> list = GlobalStateConfig.list_group;
            if (list != null && list.size() > 0) {
                Contact.group u = getG(list, id);
                return u;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    // 判断是否有群组数据
    private Contact.group getG(List<Contact.group> list, String id) {
        boolean t = false;
        int j = 0;
        for (int i = 0; i < list.size(); i++) {
            String _id = list.get(i).getId();
            if (_id != null && !_id.equals("")) {
                if (_id.equals(id)) {
                    t = true;
                    j = i;
                }
            }
        }
        if (t) {
            return list.get(j);
        } else {
            return null;
        }
    }

    /**
     * 判断是否是自己创建的群
     *
     * @param id
     * @return
     */
    public boolean judgeGroupCreate(String id) {
        boolean b = false;
        Contact.group group = null;
        List<Contact.group> list = GlobalStateConfig.list_group;
        String pid = BSApplication.SharedPreferences.getString(StringConstant.USER_ID, "");
        if (id != null && !id.equals("")) {
            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    String gid = list.get(i).getId();
                    if (gid != null && !gid.equals("")) {
                        if (gid.equals(id)) {
                            group = list.get(i);
                            break;
                        }
                    }
                }
            }
        }

        if (group != null) {
            String cid = group.getOwner_id();
            if (cid != null && !cid.equals("")) {
                if (cid.equals(pid)) {
                    b = true;
                }
            }
        }
        return b;
    }
}
