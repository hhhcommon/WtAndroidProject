package com.wotingfm.ui.user.retrievepassword.model;


import android.util.Log;

import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.ui.base.baseinterface.OnLoadInterface;
import com.wotingfm.ui.base.model.CommonModel;
import com.wotingfm.ui.base.model.UserInfo;
import com.wotingfm.ui.user.login.model.Login;
import com.wotingfm.ui.user.retrievepassword.view.RetrievePassWordFragment;

import org.json.JSONObject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class RetrievePasswordModel extends UserInfo {

    /**
     * 进行数据交互
     *
     * @param userName
     * @param listener 监听
     */
    public void loadNewsForYzm(String userName, final OnLoadInterface listener) {
        RetrofitUtils.getInstance().registerForYzm(userName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        try {
                            Log.e("获取验证码返回数据",o.toString());
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
     * 进行数据交互
     *
     * @param userName
     * @param password
     * @param yzm
     * @param listener 监听
     */
    public void loadNews(String userName, String password, String yzm, final OnLoadInterface listener) {
        RetrofitUtils.getInstance().resetPasswords(userName, password, yzm)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        try {
                            Log.e("忘记密码返回数据",o.toString());
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
