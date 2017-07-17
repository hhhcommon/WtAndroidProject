package com.wotingfm.ui.mine.security.password.model;


import android.util.Log;

import com.google.gson.GsonBuilder;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.ui.base.model.UserInfo;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class ModifyPasswordModel extends UserInfo {

    /**
     * 获取验证码
     *
     * @param userName
     * @param listener 监听
     */
    public void loadNewsForYzm(String userName, final OnLoadInterface listener) {
        try {
            RetrofitUtils.getInstance().registerForYzm(userName)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Object>() {
                        @Override
                        public void call(Object o) {
                            try {
                                Log.e("获取验证码返回数据",new GsonBuilder().serializeNulls().create().toJson(o));
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
     * 修改密码
     *
     * @param userName
     * @param password
     * @param yzm
     * @param listener 监听
     */
    public void loadNews(String userName, String password, String yzm, final OnLoadInterface listener) {
        try {
            RetrofitUtils.getInstance().resetPasswords(userName, password, yzm)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Object>() {
                        @Override
                        public void call(Object o) {
                            try {
                                Log.e("获取修改密码返回数据",new GsonBuilder().serializeNulls().create().toJson(o));
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
