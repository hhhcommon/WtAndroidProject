package com.woting.ui.mine.security.phonenumber.model;


import android.util.Log;

import com.google.gson.GsonBuilder;
import com.woting.common.net.RetrofitUtils;
import com.woting.ui.base.model.UserInfo;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class ModifyPhoneNumberModel extends UserInfo {

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
     * 修改手机号
     *
     * @param news1
     * @param news2
     * @param yzm
     * @param listener 监听
     */
    public void loadNews(String news1, String news2, String yzm, final OnLoadInterface listener) {
        try {
            RetrofitUtils.getInstance().resetPhoneNumber(news1, news2, yzm)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Object>() {
                        @Override
                        public void call(Object o) {
                            try {
                                Log.e("修改手机号返回数据",new GsonBuilder().serializeNulls().create().toJson(o));
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
