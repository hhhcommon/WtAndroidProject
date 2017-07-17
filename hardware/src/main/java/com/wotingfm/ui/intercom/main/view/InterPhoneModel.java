package com.wotingfm.ui.intercom.main.view;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.constant.StringConstant;
import com.wotingfm.common.net.RetrofitUtils;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 获取好友的数据中心
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class InterPhoneModel {

    /**
     * 获取好友
     */
    public void loadNewsForUser(final OnLoadInterface listener) {
        String id = BSApplication.SharedPreferences.getString(StringConstant.USER_ID, "");
        String token = BSApplication.SharedPreferences.getString(StringConstant.TOKEN, "");
        try {
            RetrofitUtils.getInstance().getFriends(id, token)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Object>() {
                        @Override
                        public void call(Object o) {
                            try {
                                Log.e("好友列表返回数据", new GsonBuilder().serializeNulls().create().toJson(o));
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
     * 获取群组
     */
    public void loadNewsForGroup(final OnLoadInterface listener) {
        String id = BSApplication.SharedPreferences.getString(StringConstant.USER_ID, "");
        String token = BSApplication.SharedPreferences.getString(StringConstant.TOKEN, "");
        try {
            RetrofitUtils.getInstance().getGroups(id, token)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Object>() {
                        @Override
                        public void call(Object o) {
                            try {
                                Log.e("群组列表返回数据", new GsonBuilder().serializeNulls().create().toJson(o));
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
