package com.wotingfm.ui.intercom.group.applygrouptype.model;

import android.util.Log;

import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.ui.base.model.UserInfo;
import com.wotingfm.ui.user.login.model.Login;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class ApplyGroupTypeModel extends UserInfo {

    /**
     * 进行数据交互
     * @param password
     * @param listener 监听
     */
    public void loadNews( String password,int type, final OnLoadInterface listener) {
        RetrofitUtils.getInstance().applyGroupType(password, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        try {
                            Log.e("更改加群方式返回数据",o.toString());
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