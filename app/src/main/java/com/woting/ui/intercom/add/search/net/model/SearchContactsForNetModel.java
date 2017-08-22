package com.woting.ui.intercom.add.search.net.model;

import android.util.Log;

import com.google.gson.GsonBuilder;
import com.woting.common.application.BSApplication;
import com.woting.common.config.GlobalStateConfig;
import com.woting.common.constant.StringConstant;
import com.woting.common.net.RetrofitUtils;
import com.woting.common.utils.GetTestData;
import com.woting.ui.base.model.CommonModel;
import com.woting.ui.intercom.main.contacts.model.Contact;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class SearchContactsForNetModel extends CommonModel {

    /**
     * 获取好友
     */
    public List<Contact.user> getDataForPerson() {
        List<Contact.user> srcList_p = GetTestData.getFriendList();
        return srcList_p;
    }

    /**
     * 获取群组
     */
    public List<Contact.group> getDataForGroup() {
        List<Contact.group> srcList_G = GetTestData.getGroupList();
        return srcList_G;
    }

    /**
     * 去掉重复数据
     *
     * @return
     */
    public List<Contact.group> assemblyDataForGroup(List<Contact.group> list) {
        List<Contact.group> srcList_G = GlobalStateConfig.list_group;
        if (srcList_G != null && srcList_G.size() > 0) {
            for (int i = 0; i < srcList_G.size(); i++) {
                String id = srcList_G.get(i).getId().trim();
                if (id != null && !id.equals("")) {
                    for (int j = 0; j < list.size(); j++) {
                        String _id = list.get(j).getId().trim();
                        if (_id != null && !_id.equals("") && _id.equals(id)) {
                            list.remove(j);
                            j--;
                        }
                    }
                }
            }
            return list;
        } else {
            return list;
        }
    }

    /**
     * 去掉重复数据(已经是好友的以及自己)
     */
    public List<Contact.user> assemblyDataForPerson(List<Contact.user> list) {
        List<Contact.user> srcList_p = GlobalStateConfig.list_person;
        String uid=BSApplication.SharedPreferences.getString(StringConstant.USER_ID,"");
        if(uid!=null&&!uid.trim().equals("")){
            for (int j = 0; j < list.size(); j++) {
                String _id = list.get(j).getId().trim();
                if (_id != null && !_id.equals("") && _id.equals(uid)) {
                    list.remove(j);
                    j--;
                }
            }
        }

        if (srcList_p != null && srcList_p.size() > 0) {
            for (int i = 0; i < srcList_p.size(); i++) {
                String id = srcList_p.get(i).getId().trim();
                if (id != null && !id.equals("")) {
                    for (int j = 0; j < list.size(); j++) {
                        String _id = list.get(j).getId().trim();
                        if (_id != null && !_id.equals("") && _id.equals(id)) {
                            list.remove(j);
                            j--;
                        }
                    }
                }
            }
            return list;
        } else {
            return list;
        }
    }

    /**
     * 设置搜索数据（群组）
     * @param s
     * @param srcList_g
     * @return
     */
    public  List<Contact.group> searchForGroupData(String s, List<Contact.group> srcList_g) {
        List<Contact.group>  list=new ArrayList<>();
        for(int i=0;i<srcList_g.size();i++){
            if(srcList_g.get(i).getTitle().contains(s)){
                list.add(srcList_g.get(i));
            }
        }
        return list;
    }

    /**
     * 设置搜索数据（个人）
     * @param s
     * @param srcList_p
     * @return
     */
    public  List<Contact.user> searchForPersonData(String s, List<Contact.user> srcList_p) {
        List<Contact.user>  list=new ArrayList<>();
        for(int i=0;i<srcList_p.size();i++){
            if(srcList_p.get(i).getName().contains(s)){
                list.add(srcList_p.get(i));
            }
        }
        return list;
    }

    /**
     * 获取推荐好友
     */
    public void loadNewsForRecommendPerson(String type, final OnLoadInterface listener) {
        String id = BSApplication.SharedPreferences.getString(StringConstant.USER_ID, "");
        try {
            RetrofitUtils.getInstance().getRecommendPerson(id, type)
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
     * 获取推荐群组
     */
    public void loadNewsForRecommendGroup(String type, final OnLoadInterface listener) {
        String id = BSApplication.SharedPreferences.getString(StringConstant.USER_ID, "");
        try {
            RetrofitUtils.getInstance().getRecommendGroup(id, type)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Object>() {
                        @Override
                        public void call(Object o) {
                            try {
                                Log.e("好友列表返回数据",new GsonBuilder().serializeNulls().create().toJson(o));
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
     * 获取搜索好友
     */
    public void loadNewsForSearchPerson(String s, final OnLoadInterface listener) {
        try {
            RetrofitUtils.getInstance().getSearchPerson(s)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Object>() {
                        @Override
                        public void call(Object o) {
                            try {
                                Log.e("搜索好友列表返回数据",new GsonBuilder().serializeNulls().create().toJson(o));
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
     * 获取搜索群组
     */
    public void loadNewsForSearchGroup(String s, final OnLoadInterface listener) {
        try {
            RetrofitUtils.getInstance().getSearchGroup(s)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Object>() {
                        @Override
                        public void call(Object o) {
                            try {
                                Log.e("搜索群组列表返回数据", new GsonBuilder().serializeNulls().create().toJson(o));
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
