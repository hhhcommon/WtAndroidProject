package com.wotingfm.ui.intercom.group.setmanager.model;

import android.util.Log;

import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.common.utils.GetTestData;
import com.wotingfm.ui.base.model.UserInfo;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class SetManagerModel {

    /**
     * 测试数据
     * @return
     */
    public List<Contact.user> getData() {
        List<Contact.user> data = GetTestData.getFriendList();
        return data;
    }

    /**
     * 设置管理员
     *
     * @param s
     * @param listener 监听
     */
    public void loadNews(String id, String s, final OnLoadInterface listener) {
        RetrofitUtils.getInstance().setManager(id, s)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        try {
                            Log.e("设置管理员返回数据", o.toString());
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
