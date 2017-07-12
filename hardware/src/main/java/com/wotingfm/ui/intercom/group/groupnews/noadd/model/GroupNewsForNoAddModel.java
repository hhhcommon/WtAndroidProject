package com.wotingfm.ui.intercom.group.groupnews.noadd.model;

import android.util.Log;

import com.google.gson.Gson;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.common.utils.GetTestData;
import com.wotingfm.ui.base.model.UserInfo;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;
import com.wotingfm.ui.user.login.model.Login;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 群组详情
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class GroupNewsForNoAddModel extends UserInfo {

    public List<Contact.user> getPersonList() {
        List<Contact.user> list = GetTestData.getFriendList();
        return list;
    }

    /**
     * 组建群成员界面
     *
     * @param list
     * @return
     */
    public List<Contact.user> assemblyDataForGroup(List<Contact.user> list) {
        // 组装数据
        List<Contact.user> g_list = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).is_admin()) {
                g_list.add(g_list.get(i));
            }
        }
        return g_list;
    }

    /**
     * 获取群组信息
     */
    public void loadNews(String id, final OnLoadInterface listener) {
        try {
            RetrofitUtils.getInstance().getGroupNews(id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Object>() {
                        @Override
                        public void call(Object o) {
                            try {
                                Log.e("获取群组信息返回数据", new Gson().toJson(o));
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
     * 获取群成员
     */
    public void loadNewsForGroupPerson(String id, final OnLoadInterface listener) {
        try {
            RetrofitUtils.getInstance().getGroupPerson(id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Object>() {
                        @Override
                        public void call(Object o) {
                            try {
                                Log.e("获取群组成员返回数据", new Gson().toJson(o));
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
}
