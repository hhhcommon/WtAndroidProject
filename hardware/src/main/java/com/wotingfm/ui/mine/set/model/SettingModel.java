package com.wotingfm.ui.mine.set.model;

import android.util.Log;

import com.google.gson.Gson;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.ui.base.model.UserInfo;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 设置界面数据中心
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class SettingModel extends UserInfo {

    /**
     * 注销登录
     * @param listener 监听
     */
    public void loadNews( final OnLoadInterface listener) {
        String s="";
        RetrofitUtils.getInstance().cancel(s)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        try {
                               Log.e("注销登录返回数据",new Gson().toJson(o));
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
     * 保存注销后数据
     */
    public void cancel(){
        unRegisterLogin();
    }

    public interface OnLoadInterface {
        void onSuccess(Object o);
        void onFailure(String msg);
    }

}
