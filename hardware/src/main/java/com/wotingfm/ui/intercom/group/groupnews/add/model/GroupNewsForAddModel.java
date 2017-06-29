package com.wotingfm.ui.intercom.group.groupnews.add.model;

import android.util.Log;

import com.google.gson.Gson;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.common.utils.GetTestData;
import com.wotingfm.ui.base.model.UserInfo;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;
import com.wotingfm.ui.user.login.model.Login;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class GroupNewsForAddModel extends UserInfo {

    public List<Contact.user> getPersonList() {
        List<Contact.user> list = GetTestData.getFriendList();
        return list;
    }

    /**
     * 生成一条用户数据
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
     * @param id 群id
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

    public interface OnLoadInterface {
        void onSuccess(Object o);
        void onFailure(String msg);
    }

}
