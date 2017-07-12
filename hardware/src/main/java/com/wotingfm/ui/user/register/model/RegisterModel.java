package com.wotingfm.ui.user.register.model;

import android.util.Log;

import com.google.gson.GsonBuilder;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.ui.base.model.UserInfo;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 注册数据处理中心
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class RegisterModel extends UserInfo {
//" {ret=0.0, msg=success,
// retdata={token=2Ncsfsf, user={id=5963231d5ce8e, mainPhoneNum=13200000000, password=123456, net_ease_token=5b0ecb83a0cae2de681769ae84079983, fans_count=0.0, idols_count=0.0, acc_id=1000005963231d5ce8e, fans=[], idols=[]}}}\n"
    /**
     * 提交注册数据
     *
     * @param userName
     * @param password
     * @param yzm
     * @param listener 监听
     */
    public void loadNews(String userName, String password,String yzm, final OnLoadInterface listener) {
        try {
            RetrofitUtils.getInstance().register(userName, password,yzm)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Object>() {
                        @Override
                        public void call(Object o) {
                            try {
                                Log.e("注册返回数据",new GsonBuilder().serializeNulls().create().toJson(o));
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
     * 获取数据参数
     * @param userName
     * @param password
     * @param yzm
     * @return
     */
    public boolean getBtViewType(String userName, String password, String yzm) {
        boolean bt;
        if (userName != null && !userName.trim().equals("")) {
            if (yzm != null && !yzm.trim().equals("")&&yzm.length()>3) {
                if (password != null && !password.trim().equals("") && password.length() > 5) {
                    bt = true;
                } else {
                    bt = false;
                }
            } else {
                bt = false;
            }
        } else {
            bt = false;
        }
        return bt;
    }


    public interface OnLoadInterface {
        void onSuccess(Object o);
        void onFailure(String msg);
    }

}
