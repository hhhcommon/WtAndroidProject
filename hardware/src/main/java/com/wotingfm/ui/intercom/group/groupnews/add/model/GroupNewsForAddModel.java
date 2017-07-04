package com.wotingfm.ui.intercom.group.groupnews.add.model;

import android.util.Log;

import com.google.gson.Gson;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.constant.StringConstant;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.common.utils.GetTestData;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;

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
        RetrofitUtils.getInstance().getGroupPerson(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        try {
                            Log.e("创建群组返回数据", new Gson().toJson(o));
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
    }

    /**
     * 获取群详情
     *
     * @param id
     * @param listener
     */
    public void getGroupNews(String id, final OnLoadInterface listener) {
        RetrofitUtils.getInstance().getGroupNews(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        try {
                            Log.e("获取群详情返回数据", new Gson().toJson(o));
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
    }

    /**
     * 退出群组
     *
     * @param listener
     */
    public void exitGroup(String Gid, final OnLoadInterface listener) {
        String Pid = BSApplication.SharedPreferences.getString(StringConstant.USER_ID, "");
        RetrofitUtils.getInstance().exitGroup(Gid, Pid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        try {
                            Log.e("退出群组返回数据", new Gson().toJson(o));
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
    }

    public interface OnLoadInterface {
        void onSuccess(Object o);

        void onFailure(String msg);
    }

    /**
     * 判断该群是否是自己创建的群
     *
     * @param id
     */
    public boolean judgeMine(String id) {
        String _id = BSApplication.SharedPreferences.getString(StringConstant.USER_ID, "");
        if (id.equals(_id)) {
            return true;
        } else {
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
     * 判断当前用户是都是群管理员
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
                        b = list.get(i).is_admin();
                        break;
                    }
                }
            }
        }
        return b;
    }
}
