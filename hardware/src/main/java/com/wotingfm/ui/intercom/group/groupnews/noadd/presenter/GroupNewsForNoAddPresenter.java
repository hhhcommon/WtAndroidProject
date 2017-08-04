package com.wotingfm.ui.intercom.group.groupnews.noadd.presenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.woting.commonplat.config.GlobalNetWorkConfig;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.constant.BroadcastConstants;
import com.wotingfm.common.utils.BeanCloneUtil;
import com.wotingfm.ui.intercom.group.groupapply.GroupApplyFragment;
import com.wotingfm.ui.intercom.group.groupmumbershow.view.GroupNumberShowFragment;
import com.wotingfm.ui.intercom.group.groupnews.noadd.model.GroupNewsForNoAddModel;
import com.wotingfm.ui.intercom.group.groupnews.noadd.view.GroupNewsForNoAddFragment;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class GroupNewsForNoAddPresenter {

    private  GroupNewsForNoAddFragment activity;
    private  GroupNewsForNoAddModel model;
    private String id;
    private List<Contact.user> _list;
    private String access = "2";//  0密码群，1审核群，2密码审核群
    private MessageReceiver Receiver;

    public GroupNewsForNoAddPresenter(GroupNewsForNoAddFragment activity) {
        this.activity = activity;
        this.model = new GroupNewsForNoAddModel();
        setReceiver();
        try {
            id = activity.getArguments().getString("id");
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                activity.setViewData("", name, number, address, introduce);
                List<Contact.user> list = model.getPersonList();
                if (list != null && list.size() > 0) {
                    activity.setGridViewData(list);
                } else {
                    activity.setViewForNoGroupPerson();
                }
                activity.isLoginView(0);
            } else {
                // 实际数据
                if (id != null && !id.trim().equals("")) {
                    getNews();// 获取群详情
                    getGroupPerson();// 获取群成员
                }
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
            String s = new GsonBuilder().serializeNulls().create().toJson(o);
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
            } else {
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
        String url = "";
        try {
            url = g_news.getLogo_url();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String name = "";
        try {
            name = g_news.getTitle();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String number = "";
        try {
            number = g_news.getGroup_num();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String address = "暂未填写";
        try {
            address = g_news.getLocation();
            if (address.toString().equals("")) {
                address = "暂未填写";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String introduce = "暂无群介绍";
        try {
            introduce = g_news.getIntroduction();
            if (introduce == null || introduce.equals("")) {
                introduce = "暂无群介绍";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            access = g_news.getMember_access_mode();
        } catch (Exception e) {
            e.printStackTrace();
        }
        activity.setViewData(url, name, number, address, introduce);
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
                activity.setViewForNoGroupPerson();
            }
        });
    }

    // 处理群成员返回的数据
    private void dealGroupPersonSuccess(Object o) {
        try {
            String s = new GsonBuilder().serializeNulls().create().toJson(o);
            JSONObject js = new JSONObject(s);
            int ret = js.getInt("ret");
            Log.e("获取群成员==ret", String.valueOf(ret));
            if (ret == 0) {
                String msg = js.getString("data");
                JSONTokener jsonParser = new JSONTokener(msg);
                JSONObject arg1 = (JSONObject) jsonParser.nextValue();
                String users = arg1.getString("users");
                // 群成员
                List<Contact.user> list = new Gson().fromJson(users, new TypeToken<List<Contact.user>>() {
                }.getType());
                if (list != null) {
                    // 处理数据
                    _list = model.assemblyDataForGroup(list);
                    // 数据适配
                    if (_list != null && _list.size() > 0) {
                        activity.setGridViewData(_list);
                    } else {
                        activity.setViewForNoGroupPerson();
                    }
                } else {
                    // 设置数据出错界面
                    activity.setViewForNoGroupPerson();
                }
            } else {
                // 设置数据出错界面
                activity.setViewForNoGroupPerson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 设置数据出错界面
            activity.setViewForNoGroupPerson();
        }
    }

    /**
     * 跳转到成员列表界面
     */
    public void jump() {
        if (_list != null && _list.size() > 0) {
            List<Contact.user> list= BeanCloneUtil.cloneTo(_list);
            GroupNumberShowFragment fragment = new GroupNumberShowFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("list", (Serializable) list);
            fragment.setArguments(bundle);
            InterPhoneActivity.open(fragment);
        }
    }

    /**
     * 跳转到申请界面
     */
    public void apply() {
        if (id != null && !id.equals("")) {
            GroupApplyFragment fragment = new GroupApplyFragment();
            Bundle bundle = new Bundle();
            bundle.putString("gid", id);
            bundle.putString("type", access);
            fragment.setArguments(bundle);
            InterPhoneActivity.open(fragment);
        }
    }

    /**
     * 异常按钮点击
     *
     * @param type
     */
    public void tipClick(int type) {
        if (type == 4) {
            getData();
        }
    }

    // 设置广播接收器(群组信息更改)
    private void setReceiver() {
        if (Receiver == null) {
            Receiver = new MessageReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(BroadcastConstants.GROUP_GET);
            activity.getActivity().registerReceiver(Receiver, filter);
        }
    }

    class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BroadcastConstants.GROUP_GET)) {
                if (id != null && !id.equals("")) {
                    getNews();
                }
            }
        }
    }

    /**
     * 界面销毁,注销广播
     */
    public void destroy() {
        if (Receiver != null) {
            activity.getActivity().unregisterReceiver(Receiver);
            Receiver = null;
        }
        model=null;
    }
}
