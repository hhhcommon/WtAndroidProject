package com.wotingfm.ui.intercom.add.search.net.model;

import android.util.Log;

import com.google.gson.Gson;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.constant.StringConstant;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.common.utils.GetTestData;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class SearchContactsForNetModel {

    /**
     * 获取好友
     */
    public  List<Contact.user> getDataForPerson() {
        List<Contact.user> srcList_p= GetTestData.getFriendList();
        return srcList_p;
    }

    /**
     * 获取群组
     */
    public List<Contact.group> getDataForGroup() {
        List<Contact.group>  srcList_G=GetTestData.getGroupList() ;
        return srcList_G;
    }


    /**
     * 获取推荐好友
     */
    public void loadNewsForRecommendPerson(String type,final OnLoadInterface listener) {
        String id= BSApplication.SharedPreferences.getString(StringConstant.USER_ID,"");
        RetrofitUtils.getInstance().getRecommendPerson(id,type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        try {
                            Log.e("好友列表返回数据",new Gson().toJson(o));
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
     * 获取推荐群组
     */
    public void loadNewsForRecommendGroup(String type,final OnLoadInterface listener) {
        String id= BSApplication.SharedPreferences.getString(StringConstant.USER_ID,"");
        RetrofitUtils.getInstance().getRecommendGroup(id,type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        try {
                            Log.e("好友列表返回数据",new Gson().toJson(o));
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
     * 获取搜索好友
     */
    public void loadNewsForSearchPerson(String s,final OnLoadInterface listener) {
        RetrofitUtils.getInstance().getSearchPerson(s)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        try {
                            Log.e("搜索好友列表返回数据",new Gson().toJson(o));
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
     * 获取搜索群组
     */
    public void loadNewsForSearchGroup(String s,final OnLoadInterface listener) {
        RetrofitUtils.getInstance().getSearchGroup(s)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        try {
                            Log.e("搜索群组列表返回数据",new Gson().toJson(o));
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
