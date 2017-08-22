package com.woting.ui.intercom.group.groupnews.add.model;

import android.util.Log;

import com.google.gson.GsonBuilder;
import com.woting.common.application.BSApplication;
import com.woting.common.config.GlobalStateConfig;
import com.woting.common.constant.StringConstant;
import com.woting.common.net.RetrofitUtils;
import com.woting.common.utils.CommonUtils;
import com.woting.common.utils.GetTestData;
import com.woting.ui.intercom.group.groupnews.add.view.GroupNewsForAddFragment;
import com.woting.ui.intercom.main.chat.dao.SearchTalkHistoryDao;
import com.woting.ui.intercom.main.chat.model.DBTalkHistory;
import com.woting.ui.intercom.main.contacts.model.Contact;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class GroupNewsForAddModel {
    private final GroupNewsForAddFragment activity;
    private SearchTalkHistoryDao dbDao;

    public GroupNewsForAddModel(GroupNewsForAddFragment activity) {
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
    public DBTalkHistory assemblyData(Contact.group s,String callType, String CallTypeM) {
        String id = s.getId();
        String type = "group";
        String addTime = Long.toString(System.currentTimeMillis());
        String bjUserId = CommonUtils.getUserId();
        DBTalkHistory h = new DBTalkHistory(bjUserId, type, id, addTime,callType,CallTypeM,s.getRoomId());
        return h;
    }
    
    public List<Contact.user> getPersonList() {
        List<Contact.user> list = GetTestData.getFriendList();
        return list;
    }

    /**
     * 生成一条用户数据
     *
     * @param name
     * @param id
     * @param type
     * @return
     */
    public Contact.user getUser(String name, String id, int type) {
        Contact.user user = new Contact.user();
        user.setType(type);
        user.setName(name);
        user.setId(id);
        return user;
    }

    /**
     * 获取群成员
     *
     * @param id       群id
     * @param listener 监听
     */
    public void getGroupPerson(String id, final OnLoadInterface listener) {
        try {
            RetrofitUtils.getInstance().getGroupPerson(id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Object>() {
                        @Override
                        public void call(Object o) {
                            try {
                                Log.e("获取群成员返回数据", new GsonBuilder().serializeNulls().create().toJson(o));
                                //填充UI
                                listener.onSuccess(o);
                            } catch (Exception e) {
                                e.printStackTrace();
                                listener.onFailure("");
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            throwable.printStackTrace();
                            listener.onFailure("");
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            listener.onFailure("");
        }
    }

    /**
     * 获取群详情
     *
     * @param id
     * @param listener
     */
    public void getGroupNews(String id, final OnLoadInterface listener) {
        try {
            RetrofitUtils.getInstance().getGroupNews(id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Object>() {
                        @Override
                        public void call(Object o) {
                            try {
                                Log.e("获取群详情返回数据", new GsonBuilder().serializeNulls().create().toJson(o));
                                //填充UI
                                listener.onSuccess(o);
                            } catch (Exception e) {
                                e.printStackTrace();
                                listener.onFailure("");
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            throwable.printStackTrace();
                            listener.onFailure("");
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            listener.onFailure("");
        }
    }

    /**
     * 退出群组
     *
     * @param listener
     */
    public void exitGroup(String Gid, final OnLoadInterface listener) {
        String Pid = BSApplication.SharedPreferences.getString(StringConstant.USER_ID, "");
        try {
            RetrofitUtils.getInstance().exitGroup(Gid, Pid)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Object>() {
                        @Override
                        public void call(Object o) {
                            try {
                                Log.e("退出群组返回数据", new GsonBuilder().serializeNulls().create().toJson(o));
                                //填充UI
                                listener.onSuccess(o);
                            } catch (Exception e) {
                                e.printStackTrace();
                                listener.onFailure("");
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            throwable.printStackTrace();
                            listener.onFailure("");
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            listener.onFailure("");
        }
    }

    public interface OnLoadInterface {
        void onSuccess(Object o);

        void onFailure(String msg);
    }

    /**
     * 判断该群是否是群主
     *
     * @param group
     */
    public boolean judgeMine(Contact.group group) {
        if(group!=null){
            String id= null;
            try {
                id = group.getOwner_id();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(id!=null&&!id.trim().equals("")){
                String _id = BSApplication.SharedPreferences.getString(StringConstant.USER_ID, "");
                if (id.equals(_id)) {
                    return true;
                } else {
                    return false;
                }
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

    /**
     * 组装群成员展示数据
     *
     * @param list
     * @param b    是否是群管理员
     * @return
     */
    public ArrayList<Contact.user> assemblyDataForGroup(List<Contact.user> list, boolean b) {

        // 组装数据
        if (b) {
            if (list != null && list.size() > 0) {
                for (int j = 0; j < list.size(); j++) {
                    list.get(j).setType(1);
                }

                ArrayList<Contact.user> g_list = new ArrayList<>();
                if (list.size() > 3) {
                    for (int i = 0; i < 3; i++) {
                        g_list.add(list.get(i));
                    }
                    g_list.add(getUser("小苹果", "1", 2));
                    g_list.add(getUser("小苹果", "1", 3));
                    return g_list;
                } else {
                    for (int i = 0; i < list.size(); i++) {
                        g_list.add(list.get(i));
                    }
                    g_list.add(getUser("小苹果", "1", 2));
                    g_list.add(getUser("小苹果", "1", 3));
                    return g_list;
                }
            } else {
                return null;
            }
        } else {
            if (list != null && list.size() > 0) {
                for (int j = 0; j < list.size(); j++) {
                    list.get(j).setType(1);
                }

                ArrayList<Contact.user> g_list = new ArrayList<>();
                if (list.size() > 4) {
                    for (int i = 0; i < 4; i++) {
                        g_list.add(list.get(i));
                    }
                    g_list.add(getUser("小苹果", "1", 2));
                    return g_list;
                } else {
                    for (int i = 0; i < list.size(); i++) {
                        g_list.add(list.get(i));
                    }
                    g_list.add(getUser("小苹果", "1", 2));
                    return g_list;
                }
            } else {
                return null;
            }
        }
    }

    /**
     * 判断该用户是否是自己好友
     *
     * @param id
     * @return
     */
    public boolean judgeFriends(String id) {
        boolean b = false;
        List<Contact.user> list = GlobalStateConfig.list_person;
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                String _id = list.get(i).getId();
                if (_id != null && !_id.equals("")) {
                    if (id.equals(_id)) {
                        b = true;
                        break;
                    }
                }
            }
        }
        return b;
    }

    /**
     * 判断当前用户是不是群管理员
     *
     * @param list
     * @return
     */
    public boolean isAdmin(List<Contact.user> list) {
        boolean b = false;
        String _id = BSApplication.SharedPreferences.getString(StringConstant.USER_ID, "");
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                String id = list.get(i).getId();
                if (id != null && !id.equals("")) {
                    if (id.equals(_id)) {
                        boolean  b1 = list.get(i).is_admin();
                        boolean b2 = list.get(i).is_owner();
                        if(b1||b2){
                            b = true;
                            break;
                        }else{
                            b = false;
                        }
                    }
                }
            }
        }
        return b;
    }

    /**
     * 判断当前用户是不是群主
     *
     * @param list
     * @return
     */
    public boolean isOw(List<Contact.user> list) {
        boolean b = false;
        String _id = BSApplication.SharedPreferences.getString(StringConstant.USER_ID, "");
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                String id = list.get(i).getId();
                if (id != null && !id.equals("")) {
                    if (id.equals(_id)) {
                        boolean  b1 = list.get(i).is_owner();
                        if(b1){
                            b = true;
                            break;
                        }else{
                            b = false;
                        }
                    }
                }
            }
        }
        return b;
    }
}
