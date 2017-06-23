package com.wotingfm.ui.intercom.person.personmessage.model;

import android.util.Log;
import com.google.gson.Gson;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.constant.StringConstant;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.ui.base.model.UserInfo;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 好友信息的数据中心
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class PersonMessageModel extends UserInfo {

    /**
     * 获取好友信息
     * @param listener 监听
     */
    public void loadNews( final OnLoadInterface listener) {
        String id= BSApplication.SharedPreferences.getString(StringConstant.USER_ID,"000");
        String token= BSApplication.SharedPreferences.getString(StringConstant.TOKEN,"000");

        RetrofitUtils.getInstance().getPersonNews(id, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        try {
                            Log.e("获取好友信息返回数据",new Gson().toJson(o));
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
