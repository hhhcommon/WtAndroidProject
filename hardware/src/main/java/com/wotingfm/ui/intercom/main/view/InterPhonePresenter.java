package com.wotingfm.ui.intercom.main.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.woting.commonplat.config.GlobalNetWorkConfig;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.constant.BroadcastConstants;
import com.wotingfm.common.utils.CommonUtils;
import com.wotingfm.common.utils.GetTestData;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.List;

/**
 * 获取好友以及群组列表
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class InterPhonePresenter {

    private final InterPhoneFragment activity;
    private final InterPhoneModel model;
    private MessageReceiver Receiver;

    public InterPhonePresenter(InterPhoneFragment activity) {
        this.activity = activity;
        this.model = new InterPhoneModel();
        getData();    // 获取数据，数据适配
        setReceiver();// 设置广播接收器
    }

    // 获取数据，数据适配
    private void getData() {
        if (CommonUtils.isLogin()) {
            if (GlobalNetWorkConfig.CURRENT_NETWORK_STATE_TYPE != -1) {
                if (GlobalStateConfig.test) {
                    // 测试数据
                    GlobalStateConfig.list_person = GetTestData.getFriendList();
                    GlobalStateConfig.list_group = GetTestData.getGroupList();
                } else {
                    getUser();//  获取好友数据
                    getGroup();// 获取群组数据
                }
            }
        }
    }

    // 获取好友数据
    private void getUser() {
        model.loadNewsForUser(new InterPhoneModel.OnLoadInterface() {
            @Override
            public void onSuccess(Object o) {
                dealUserSuccess(o);
            }

            @Override
            public void onFailure(String msg) {
            }
        });
    }

    // 获取群组数据
    private void getGroup() {
        model.loadNewsForGroup(new InterPhoneModel.OnLoadInterface() {
            @Override
            public void onSuccess(Object o) {
                dealGroupSuccess(o);
            }

            @Override
            public void onFailure(String msg) {
            }
        });
    }

    // 处理返回好友的数据
    private void dealUserSuccess(Object o) {
        try {
            String s = new Gson().toJson(o);
            JSONObject js = new JSONObject(s);
            int ret = js.getInt("ret");
            Log.e("获取好友列表==ret", String.valueOf(ret));
            if (ret == 0) {
                String msg = js.getString("data");
                JSONTokener jsonParser = new JSONTokener(msg);
                JSONObject arg1 = (JSONObject) jsonParser.nextValue();
                String friends = arg1.getString("friends");
                // 好友列表
                List<Contact.user> list = new Gson().fromJson(friends, new TypeToken<List<Contact.user>>() {
                }.getType());
                if (list != null && list.size() > 0) {
                    GlobalStateConfig.list_person = list;
                    // 发送好友数据更改广播通知所有界面
                    activity.getActivity().sendBroadcast(new Intent(BroadcastConstants.PERSON_CHANGE));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 处理返回组的数据
    private void dealGroupSuccess(Object o) {
        try {
            String s = new Gson().toJson(o);
            JSONObject js = new JSONObject(s);
            int ret = js.getInt("ret");
            Log.e("获取群组列表==ret", String.valueOf(ret));
            if (ret == 0) {
                String msg = js.getString("data");
                JSONTokener jsonParser = new JSONTokener(msg);
                JSONObject arg1 = (JSONObject) jsonParser.nextValue();
                String groups = arg1.getString("chat_groups");
                // 好友列表
                List<Contact.group> list = new Gson().fromJson(groups, new TypeToken<List<Contact.group>>() {
                }.getType());
                if (list != null && list.size() > 0) {
                    GlobalStateConfig.list_group = list;
                    // 发送群组数据更改广播通知所有界面
                    activity.getActivity().sendBroadcast(new Intent(BroadcastConstants.GROUP_CHANGE));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 设置广播接收器
    private void setReceiver() {
        if (Receiver == null) {
            Receiver = new MessageReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(BroadcastConstants.LOGIN);           // 登录成功GROUP_CHANGE
            filter.addAction(BroadcastConstants.GROUP_GET);       // 更新群列表
            filter.addAction(BroadcastConstants.PERSON_GET);      // 更新好友
            filter.addAction(BroadcastConstants.VIEW_INTER_PHONE);// 更改对讲页面viewPage的展示界面
            activity.getActivity().registerReceiver(Receiver, filter);
        }
    }

    class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BroadcastConstants.LOGIN)) {
                getData();// 接收到账户更改后重新获取数据
            } else if (action.equals(BroadcastConstants.VIEW_INTER_PHONE)) {
                activity.change(0);// 更改为对讲主页
            } else if (action.equals(BroadcastConstants.GROUP_GET)) {
                getGroup();// 重新获取群组
            } else if (action.equals(BroadcastConstants.PERSON_GET)) {
                getUser();// 重新获取好友
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
    }
}
