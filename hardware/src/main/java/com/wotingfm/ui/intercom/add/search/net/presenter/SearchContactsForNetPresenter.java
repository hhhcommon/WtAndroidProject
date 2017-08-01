package com.wotingfm.ui.intercom.add.search.net.presenter;

import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.woting.commonplat.config.GlobalNetWorkConfig;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.utils.ToastUtils;
import com.wotingfm.ui.intercom.add.search.net.model.SearchContactsForNetModel;
import com.wotingfm.ui.intercom.add.search.net.view.SearchContactsForNetFragment;
import com.wotingfm.ui.intercom.group.groupnews.add.view.GroupNewsForAddFragment;
import com.wotingfm.ui.intercom.group.groupnews.noadd.view.GroupNewsForNoAddFragment;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;
import com.wotingfm.ui.intercom.person.personmessage.view.PersonMessageFragment;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索好友以及群组
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class SearchContactsForNetPresenter {

    private final SearchContactsForNetFragment activity;
    private final SearchContactsForNetModel model;
    private String fromType = "group";// 界面跳转来源 添加群组=group，添加好友=friend


    public SearchContactsForNetPresenter(SearchContactsForNetFragment activity) {
        this.activity = activity;
        this.model = new SearchContactsForNetModel();
        getData();
    }

    // 获取界面来源
    private void getData() {
        try {
            fromType = activity.getArguments().getString("type");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取数据===推荐的人
     */
    public void getRecommendedData() {
        if (GlobalNetWorkConfig.CURRENT_NETWORK_STATE_TYPE != -1) {
//            if (true) {
//                // 测试数据
//                if (fromType.trim().equals("group")) {
//                    List<Contact.group> srcList_G = model.getDataForGroup();
//                    activity.setViewForPosGroup(srcList_G);
//                } else {
//                    List<Contact.user> srcList_p = model.getDataForPerson();
//                    activity.setViewForPosPerson(srcList_p);
//                }
//                activity.isLoginView(-1);
//            } else {
//                // 实际数据
//                if (fromType.trim().equals("group")) {
//                    // 获取推荐的群组
//                    getRecommendedDataForGroup();
//                } else {
//                    // 获取推荐的好友
//                        getRecommendedDataForPerson();
//
//                }
//            }
            activity.isLoginView(-1);
        } else {
            activity.isLoginView(2);
        }
    }

    /**
     * 获取推荐的好友
     */
//    public void getRecommendedDataForPerson() {
//        activity.dialogShow();
//        String type = "";
//        model.loadNewsForRecommendPerson(type, new SearchContactsForNetModel.OnLoadInterface() {
//            @Override
//            public void onSuccess(Object o) {
//                activity.dialogCancel();
//                dealUserSuccess(o);
//            }
//
//            @Override
//            public void onFailure(String msg) {
//                activity.dialogCancel();
//                activity.isLoginView(4);
//            }
//        });
//
//    }

    /**
     * 获取推荐的群组
     */
//    public void getRecommendedDataForGroup() {
//        activity.dialogShow();
//        String type = "";
//        model.loadNewsForRecommendGroup(type, new SearchContactsForNetModel.OnLoadInterface() {
//            @Override
//            public void onSuccess(Object o) {
//                activity.dialogCancel();
//                dealGroupSuccess(o);
//            }
//
//            @Override
//            public void onFailure(String msg) {
//                activity.dialogCancel();
//                activity.isLoginView(4);
//            }
//        });
//    }

    // 处理返回好友的数据
//    private void dealUserSuccess(Object o) {
//        try {
//            String s = new GsonBuilder().serializeNulls().create().toJson(o);
//            JSONObject js = new JSONObject(s);
//            int ret = js.getInt("ret");
//            Log.e("获取推荐好友列表==ret", String.valueOf(ret));
//            if (ret == 0) {
//                String msg = js.getString("data");
//                JSONTokener jsonParser = new JSONTokener(msg);
//                JSONObject arg1 = (JSONObject) jsonParser.nextValue();
//                String friends = arg1.getString("friends");
//                // 好友列表
//                List<Contact.user> list = new Gson().fromJson(friends, new TypeToken<List<Contact.user>>() {
//                }.getType());
//                if (list != null && list.size() > 0) {
//                    activity.setViewForPosPerson(list);
//                    activity.isLoginView(-1);
//                } else {
//                    activity.isLoginView(1);
//                }
//            } else {
//                activity.isLoginView(4);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            activity.isLoginView(4);
//        }
//    }

    // 处理返回组的数据
//    private void dealGroupSuccess(Object o) {
//        try {
//            String s = new GsonBuilder().serializeNulls().create().toJson(o);
//            JSONObject js = new JSONObject(s);
//            int ret = js.getInt("ret");
//            Log.e("获取推荐群组列表==ret", String.valueOf(ret));
//            if (ret == 0) {
//                String msg = js.getString("data");
//                JSONTokener jsonParser = new JSONTokener(msg);
//                JSONObject arg1 = (JSONObject) jsonParser.nextValue();
//                String groups = arg1.getString("chat_groups");
//                // 好友列表
//                List<Contact.group> list = new Gson().fromJson(groups, new TypeToken<List<Contact.group>>() {
//                }.getType());
//                if (list != null && list.size() > 0) {
//                    activity.setViewForPosGroup(list);
//                    activity.isLoginView(-1);
//                } else {
//                    activity.isLoginView(1);
//                }
//            } else {
//                activity.isLoginView(4);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            activity.isLoginView(4);
//        }
//    }

    /**
     * 根据关键词调整展示数据
     *
     * @param s 为null或者“”
     */
    public void search(String s) {
        if (GlobalNetWorkConfig.CURRENT_NETWORK_STATE_TYPE != -1) {
            if (GlobalStateConfig.test) {
                // 测试数据
                if (fromType.trim().equals("group")) {
                    List<Contact.group> srcList_G = model.getDataForGroup();
                    if (srcList_G != null && srcList_G.size() > 0) {
                        if (s != null && !s.trim().equals("")) {
                            List<Contact.group> list = model.searchForGroupData(s, srcList_G);
                            if (list != null && list.size() > 0) {
                                activity.setViewForGroup(list);
                                activity.isLoginView(0);
                            } else {
                                activity.isLoginView(1);
                            }
                        } else {
                            activity.isLoginView(1);
                        }
                    } else {
                        activity.isLoginView(1);
                    }
                } else {
                    List<Contact.user> srcList_p = model.getDataForPerson();
                    if (srcList_p != null && srcList_p.size() > 0) {
                        if (s != null && !s.trim().equals("")) {
                            List<Contact.user> list = model.searchForPersonData(s, srcList_p);
                            if (list != null && list.size() > 0) {
                                activity.setViewForPerson(list);
                                activity.isLoginView(0);
                            } else {
                                activity.isLoginView(1);
                            }
                        } else {
                            activity.isLoginView(1);
                        }
                    } else {
                        activity.isLoginView(1);
                    }
                }
            } else {
                // 实际数据
                if (fromType.trim().equals("group")) {
                    // 获取搜索的群组
                    getSearchDataForGroup(s);
                } else {
                    // 获取搜索的好友
                    getSearchDataForPerson(s);
                }
            }
        } else {
            activity.isLoginView(2);
        }
    }

    /**
     * 跳转到好友信息界面
     * @param id
     */
    public void jumpPerson(String id){
        if(model.judgeFriends(id)){
            PersonMessageFragment fragment = new PersonMessageFragment();
            Bundle bundle = new Bundle();
            bundle.putString("type", "true");
            bundle.putString("id",id );
            fragment.setArguments(bundle);
            InterPhoneActivity.open(fragment);
        }else{
            PersonMessageFragment fragment = new PersonMessageFragment();
            Bundle bundle = new Bundle();
            bundle.putString("type", "false");
            bundle.putString("id", id);
            fragment.setArguments(bundle);
            InterPhoneActivity.open(fragment);
        }
    }

    /**
     * 跳转到群组界面
     * @param id
     */
    public void jumpGroup(String id){
        if(model.judgeGroups(id)){
            GroupNewsForAddFragment fragment = new GroupNewsForAddFragment();
            Bundle bundle = new Bundle();
            bundle.putString("id",id);
            bundle.putString("type", "true");
            fragment.setArguments(bundle);
            InterPhoneActivity.open(fragment);
        }else{
            GroupNewsForNoAddFragment fragment = new GroupNewsForNoAddFragment();
            Bundle bundle = new Bundle();
            bundle.putString("id", id);
            fragment.setArguments(bundle);
            InterPhoneActivity.open(fragment);
        }
    }


    /**
     * 获取搜索的好友
     */
    public void getSearchDataForPerson(final String s) {
//        activity.dialogShow();
        model.loadNewsForSearchPerson(s, new SearchContactsForNetModel.OnLoadInterface() {
            @Override
            public void onSuccess(Object o) {
//                activity.dialogCancel();
                dealSearchUserSuccess(o, s);
            }

            @Override
            public void onFailure(String msg) {
//                activity.dialogCancel();
                activity.isLoginView(4);
            }
        });
    }

    /**
     * 获取搜索的群组
     */
    public void getSearchDataForGroup(final String s) {
//        activity.dialogShow();
        model.loadNewsForSearchGroup(s, new SearchContactsForNetModel.OnLoadInterface() {
            @Override
            public void onSuccess(Object o) {
//                activity.dialogCancel();
                dealSearchGroupSuccess(o, s);
            }

            @Override
            public void onFailure(String msg) {
//                activity.dialogCancel();
                activity.isLoginView(4);
            }
        });
    }

    // 处理搜索返回好友的数据
    private void dealSearchUserSuccess(Object o, String str) {
        try {
            String s = new GsonBuilder().serializeNulls().create().toJson(o);
            JSONObject js = new JSONObject(s);
            int ret = js.getInt("ret");
            Log.e("获取搜索好友列表==ret", String.valueOf(ret));
            if (ret == 0) {
                String msg = js.getString("data");
//                JSONTokener jsonParser = new JSONTokener(msg);
//                JSONObject arg1 = (JSONObject) jsonParser.nextValue();
//                String friends = arg1.getString("users");
                // 好友列表
                List<Contact.user> list = new Gson().fromJson(msg, new TypeToken<List<Contact.user>>() {
                }.getType());
                if (list != null && list.size() > 0) {
                    List<Contact.user> _list = model.assemblyDataForPerson(list);
                    if (_list != null && _list.size() > 0) {
                        activity.setViewForPerson(_list);
                        activity.isLoginView(0);
                    } else {
                        activity.isLoginView(1);
                    }
                } else {
                    activity.isLoginView(1);
                }
            } else {
                activity.isLoginView(4);
            }
        } catch (Exception e) {
            e.printStackTrace();
            activity.isLoginView(4);
        }
    }

    // 处理搜索返回组的数据
    private void dealSearchGroupSuccess(Object o, String str) {
        try {
            String s = new GsonBuilder().serializeNulls().create().toJson(o);
            JSONObject js = new JSONObject(s);
            int ret = js.getInt("ret");
            Log.e("获取搜索群组列表==ret", String.valueOf(ret));
            if (ret == 0) {
                String msg = js.getString("data");
//                JSONTokener jsonParser = new JSONTokener(msg);
//                JSONObject arg1 = (JSONObject) jsonParser.nextValue();
//                String groups = arg1.getString("chat_groups");
                // 好友列表
                List<Contact.group> list = new Gson().fromJson(msg, new TypeToken<List<Contact.group>>() {
                }.getType());
                if (list != null && list.size() > 0) {
                    List<Contact.group> _list = model.assemblyDataForGroup(list);
                    if (_list != null && _list.size() > 0) {
                        activity.setViewForGroup(_list);
                        activity.isLoginView(0);
                    } else {
                        activity.isLoginView(1);
                    }
                } else {
                    activity.isLoginView(1);
                }
            } else {
                activity.isLoginView(4);
            }
        } catch (Exception e) {
            e.printStackTrace();
            activity.isLoginView(4);
        }
    }


    /**
     * 异常按钮的点击事件
     *
     * @param type
     */
    public void tipClick(int type) {
        // 此处不需要处理
    }
}
