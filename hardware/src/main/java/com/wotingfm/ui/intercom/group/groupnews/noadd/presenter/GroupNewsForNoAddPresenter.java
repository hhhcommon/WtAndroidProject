package com.wotingfm.ui.intercom.group.groupnews.noadd.presenter;

import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.woting.commonplat.config.GlobalNetWorkConfig;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.ui.intercom.group.groupapply.view.GroupApplyFragment;
import com.wotingfm.ui.intercom.group.groupmumbershow.view.GroupNumberShowFragment;
import com.wotingfm.ui.intercom.group.groupnews.noadd.model.GroupNewsForNoAddModel;
import com.wotingfm.ui.intercom.group.groupnews.noadd.view.GroupNewsForNoAddFragment;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;
import com.wotingfm.ui.intercom.person.personapply.view.PersonApplyFragment;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class GroupNewsForNoAddPresenter {

    private final GroupNewsForNoAddFragment activity;
    private final GroupNewsForNoAddModel model;
    private final String id;
    private ArrayList<Contact.user> g_list;// 全部管理员

    public GroupNewsForNoAddPresenter(GroupNewsForNoAddFragment activity) {
        this.activity = activity;
        this.model = new GroupNewsForNoAddModel();
        Bundle bundle = activity.getArguments();
        id = bundle.getString("id");
    }

    /**
     * 获取数据设置界面数据
     */
    public void getData() {
        if (GlobalNetWorkConfig.CURRENT_NETWORK_STATE_TYPE != -1) {
            if (GlobalStateConfig.test) {
                // 测试数据
                String name = "朝阳钓鱼";
                String number = "518518";
                String address = "北京朝阳";
                String introduce = "这是一个钓鱼交流群";
                activity.setViewData(name, number, address, introduce);
                List<Contact.user> list = model.getPersonList();
                if (list != null && list.size() > 0) {
                    activity.setGridViewData(list);
                } else {
                    activity.setViewForNoGroupPerson();
                }
                activity.isLoginView(0);
            } else {
                // 实际数据
                getNews();// 获取群详情
                getGroupPerson();// 获取群成员
            }
        } else {
            activity.isLoginView(2);
        }
    }

    /**
     * 获取群详情
     */
    public void getNews() {
        model.loadNews(id, new GroupNewsForNoAddModel.OnLoadInterface() {
            @Override
            public void onSuccess(Object o) {
                dealSuccess(o);
            }

            @Override
            public void onFailure(String msg) {
            }
        });
    }

    // 处理返回的数据
    private void dealSuccess(Object o) {
        try {
            String s = new Gson().toJson(o);
            JSONObject js = new JSONObject(s);
            int ret = js.getInt("ret");
            Log.e("获取群详情==ret", String.valueOf(ret));
            if (ret == 0) {
                String msg = js.getString("data");
                JSONTokener jsonParser = new JSONTokener(msg);
                JSONObject arg1 = (JSONObject) jsonParser.nextValue();
                String group = arg1.getString("chat_group");
                // 群详情
                Contact.group g_news = new Gson().fromJson(group, new TypeToken<Contact.group>() {
                }.getType());
                if (g_news != null) {
                    // 处理数据
                    assemblyData(g_news);
                } else {
                    // 设置数据出错界面
                    activity.isLoginView(4);
                }
            }else{
                // 设置数据出错界面
                activity.isLoginView(4);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 设置数据出错界面
            activity.isLoginView(4);
        }
    }

    // 组装数据
    private void assemblyData(Contact.group g_news) {
        String name = "";
        try {
            name = g_news.getTitle();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String number = "";
        try {
            number = g_news.getId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String address = "";
        try {
            address = g_news.getLocation();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String introduce = "";
        try {
            introduce = g_news.getIntroduction();
        } catch (Exception e) {
            e.printStackTrace();
        }
        activity.setViewData(name, number, address, introduce);
        activity.isLoginView(0);
    }


    /**
     * 获取群组成员
     */
    public void getGroupPerson() {
        activity.dialogShow();
        model.loadNewsForGroupPerson(id, new GroupNewsForNoAddModel.OnLoadInterface() {
            @Override
            public void onSuccess(Object o) {
                activity.dialogCancel();
                dealGroupPersonSuccess(o);
            }

            @Override
            public void onFailure(String msg) {
                activity.dialogCancel();
            }
        });
    }

    // 处理群成员返回的数据
    private void dealGroupPersonSuccess(Object o) {
        try {
            String s = new Gson().toJson(o);
            JSONObject js = new JSONObject(s);
            int ret = js.getInt("ret");
            Log.e("获取群成员==ret", String.valueOf(ret));
            if (ret == 0) {
                String msg = js.getString("data");
                JSONTokener jsonParser = new JSONTokener(msg);
                JSONObject arg1 = (JSONObject) jsonParser.nextValue();
                String group = arg1.getString("chat_group");
                // 群成员
                List<Contact.user> list = new Gson().fromJson(group, new TypeToken<List<Contact.user>>() {
                }.getType());
                if (list != null) {
                    // 处理数据
                    assemblyDataForGroup(list);
                } else {
                    // 设置数据出错界面
                    activity.setViewForNoGroupPerson();
                }
            }else{
                // 设置数据出错界面
                activity.setViewForNoGroupPerson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 设置数据出错界面
            activity.setViewForNoGroupPerson();
        }
    }

    // 组建群成员界面
    private void assemblyDataForGroup(List<Contact.user> list) {
        // 组装数据
         g_list = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).is_admin()) {
                g_list.add(g_list.get(i));
            }
        }
        // 数据适配
        if (g_list != null && g_list.size() > 0) {
            if (g_list.size() > 5) {
                ArrayList<Contact.user> _list = new ArrayList<>();
                for (int i = 0; i < 5; i++) {
                    _list.add(g_list.get(i));
                }
                activity.setGridViewData(_list);
            } else {
                activity.setGridViewData(g_list);
            }
        } else {
            activity.setViewForNoGroupPerson();
        }
    }

     /**
     * 跳转到成员列表界面
     */
    public void  jump(){
        GroupNumberShowFragment fragment = new GroupNumberShowFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("list", g_list);
        fragment.setArguments(bundle);
        InterPhoneActivity.open(fragment);
    }
    /**
     * 跳转到成员列表界面
     */
    public void apply(){
        GroupApplyFragment fragment = new GroupApplyFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        fragment.setArguments(bundle);
        InterPhoneActivity.open(fragment);
    }



    /**
     * 异常按钮点击
     *
     * @param type
     */
    public void tipClick(int type) {
        if(type==4){
            getData();
        }
    }
}
