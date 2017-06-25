package com.wotingfm.ui.intercom.person.newfriend.model;

import android.util.Log;

import com.google.gson.Gson;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.constant.StringConstant;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.ui.base.model.UserInfo;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;
import com.wotingfm.ui.user.login.model.Login;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 新的好友申请请求
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class NewFriendModel extends UserInfo {

    /**
     * 新的好友申请请求
     * @param listener 监听
     */
    public void loadNews( final OnLoadInterface listener) {
        String id= BSApplication.SharedPreferences.getString(StringConstant.USER_ID,"000");
        String token= BSApplication.SharedPreferences.getString(StringConstant.TOKEN,"000");
        RetrofitUtils.getInstance().newFriend(id, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        try {
                            Log.e("新的好友申请请求返回数据",new Gson().toJson(o));
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
     * 新的好友申请==删除
     * @param listener 监听
     */
    public void loadNewsForDel( String id,final OnLoadInterface listener) {
        String token= BSApplication.SharedPreferences.getString(StringConstant.TOKEN,"000");
        RetrofitUtils.getInstance().newFriendDel(id, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        try {
                            Log.e("新的好友申请删除==返回数据",new Gson().toJson(o));
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
     * 新的好友申请==同意
     * @param listener 监听
     */
    public void loadNewsForApply( String id,final OnLoadInterface listener) {
        String token= BSApplication.SharedPreferences.getString(StringConstant.TOKEN,"000");
        RetrofitUtils.getInstance().newFriendApply(id, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        try {
                            Log.e("新的好友申请同意==返回数据",new Gson().toJson(o));
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
     * 新的好友申请==拒绝
     * @param listener 监听
     */
    public void loadNewsForRefuse(String id, final OnLoadInterface listener) {
        String token= BSApplication.SharedPreferences.getString(StringConstant.TOKEN,"000");
        RetrofitUtils.getInstance().newFriendRefuse(id, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        try {
                            Log.e("新的好友申请同意==返回数据",new Gson().toJson(o));
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


    public static List<NewFriend> getFriendList() {
        List<NewFriend> srcList_p = new ArrayList<>();
        srcList_p.add(getUser("小苹果","1"));
        srcList_p.add(getUser("大美丽","2"));
        srcList_p.add(getUser("放羊佬","3"));
        srcList_p.add(getUser("阿富汗","4"));
        srcList_p.add(getUser("奔驰","5"));
        srcList_p.add(getUser("冲天一怒为红颜","6"));
        srcList_p.add(getUser("东风拖拉机","7"));
        srcList_p.add(getUser("易中天","8"));
        srcList_p.add(getUser("京东大哥","9"));
        srcList_p.add(getUser("海尔兄弟","10"));
        srcList_p.add(getUser("卡夫卡","11"));
        srcList_p.add(getUser("刘德华","12"));
        srcList_p.add(getUser("面条爱上包子","13"));
        srcList_p.add(getUser("我的天呀","14"));

        return srcList_p;
    }

    // 生成一条用户数据
    private static NewFriend getUser(String name, String id) {
        NewFriend user = new NewFriend();
        user.setName(name);
        user.setId(id);
        return user;
    }

    public interface OnLoadInterface {
        void onSuccess(Object o);

        void onFailure(String msg);
    }

}
