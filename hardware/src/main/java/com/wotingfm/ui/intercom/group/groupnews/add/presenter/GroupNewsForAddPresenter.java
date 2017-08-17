package com.wotingfm.ui.intercom.group.groupnews.add.presenter;

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
import com.woting.commonplat.utils.JsonEncloseUtils;
import com.wotingfm.common.bean.MessageEvent;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.constant.BroadcastConstants;
import com.wotingfm.common.utils.BeanCloneUtil;
import com.wotingfm.common.utils.ToastUtils;
import com.wotingfm.ui.intercom.group.exitgroup.view.GroupExitFragment;
import com.wotingfm.ui.intercom.group.groupmanage.GroupManageFragment;
import com.wotingfm.ui.intercom.group.groupmumberadd.view.GroupNumberAddFragment;
import com.wotingfm.ui.intercom.group.groupmumberdel.view.GroupNumberDelFragment;
import com.wotingfm.ui.intercom.group.groupmumbershow.view.GroupNumberShowFragment;
import com.wotingfm.ui.intercom.group.groupnews.add.model.GroupNewsForAddModel;
import com.wotingfm.ui.intercom.group.groupnews.add.view.GroupNewsForAddFragment;
import com.wotingfm.ui.intercom.main.chat.model.TalkHistory;
import com.wotingfm.ui.intercom.main.chat.presenter.ChatPresenter;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;
import com.wotingfm.ui.intercom.main.simulation.view.SimulationInterPhoneFragment;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;
import com.wotingfm.ui.intercom.person.personmessage.view.PersonMessageFragment;
import com.wotingfm.ui.mine.qrcodes.EWMShowFragment;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class GroupNewsForAddPresenter {

    private GroupNewsForAddFragment activity;
    private GroupNewsForAddModel model;
    private String gid;
    private Contact.group g_news;
    private List<Contact.user> list;
    private boolean isAdmin = false;// 是否是管理员
    private boolean isOw = false;// 是否是群主
    private String channel1, channel2;
    private MessageReceiver Receiver;

    public GroupNewsForAddPresenter(GroupNewsForAddFragment activity) {
        this.activity = activity;
        this.model = new GroupNewsForAddModel(activity);
        setReceiver();
        try {
            gid = activity.getArguments().getString("id");
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
                activity.setViewForMy(true);
                String name = "朝阳钓鱼";
                String number = "518518";
                String address = "北京朝阳";
                String introduce = "这是一个钓鱼交流群";
                String channel1 = "CH100-100000";
                String channel2 = "CH100-100000";
                activity.setViewData("", name, number, address, introduce, channel1, channel2);
                list = model.getPersonList();// 获取群成员数据
                if (list != null && list.size() > 0) {
                    ArrayList<Contact.user> _list = model.assemblyDataForGroup(list, true);// 组装群成员展示数据
                    if (_list != null && _list.size() > 0) {
                        activity.setGridViewData(_list, list.size());
                    } else {
                        activity.setViewForNoGroupPerson();
                    }
                } else {
                    activity.setViewForNoGroupPerson();
                }
            } else {
                // 实际数据
                if (gid != null && !gid.equals("")) {
                    getNews();
                    getGroupPerson();
                } else {
                    ToastUtils.show_always(activity.getActivity(), "数据出错了，请稍后再试！");
                }
            }
        } else {
            ToastUtils.show_always(activity.getActivity(), "网络连接失败，请稍后再试！");
        }
    }

    /**
     * 获取群详情
     */
    public void getNews() {
        model.getGroupNews(gid, new GroupNewsForAddModel.OnLoadInterface() {
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
                g_news = new Gson().fromJson(group, new TypeToken<Contact.group>() {
                }.getType());
                if (g_news != null) {
                    // 处理数据
                    assemblyData(g_news);
                    activity.isLoginView(0);
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

        String channel = "";
        channel1 = "";
        channel2 = "";
        try {
            channel = g_news.getChannel();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (channel != null && !channel.equals("")) {
            if (channel.contains(",")) {
                String[] strArray = channel.split(","); //拆分字符为"," ,然后把结果交给数组strArray
                channel1 = strArray[0];
                channel2 = strArray[1];
            } else {
                channel1 = channel;
                channel2 = "";
            }
        }
        activity.setViewData(url, name, number, address, introduce, channel1, channel2);
    }

    /**
     * 获取群组成员
     */
    public void getGroupPerson() {
        activity.dialogShow();
        model.getGroupPerson(gid, new GroupNewsForAddModel.OnLoadInterface() {
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
            String s = new GsonBuilder().serializeNulls().create().toJson(o);
            JSONObject js = new JSONObject(s);
            int ret = js.getInt("ret");
            Log.e("获取群成员==ret", String.valueOf(ret));
            if (ret == 0) {
                String msg = js.getString("data");
                JSONTokener jsonParser = new JSONTokener(msg);
                JSONObject arg1 = (JSONObject) jsonParser.nextValue();
                String group = arg1.getString("users");
                // 群成员
                list = new Gson().fromJson(group, new TypeToken<List<Contact.user>>() {
                }.getType());
                if (list != null) {
                    // 处理数据
                    isAdmin = model.isAdmin(list);// 判断当前用户是否是管理员
                    isOw = model.isOw(list);// 判断当前用户是否是群主
                    setAdmin(isAdmin);
                    ArrayList<Contact.user> _list = model.assemblyDataForGroup(list, isAdmin);
                    if (_list != null && _list.size() > 0) {
                        activity.setGridViewData(_list, list.size());
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

    // 设置管理员
    private void setAdmin(boolean b) {
        if (b) {
            activity.setViewForMy(true);
        } else {
            activity.setViewForMy(false);
        }
    }

    /**
     * 跳转到群管理界面
     */
    public void jumpManager() {
        if (GlobalStateConfig.test) {
            String name = "朝阳钓鱼";
            String number = "518518";
            String address = "北京朝阳";
            String introduce = "这是一个钓鱼交流群";
            String channel1 = "CH100-100000";
            String channel2 = "CH100-100000";
            Contact.group group = new Contact.group();
            group.setTitle(name);
            group.setId(number);
            group.setLocation(address);
            group.setIntroduction(introduce);
            List<Contact.user> _list = BeanCloneUtil.cloneTo(list);
            GroupManageFragment fragment = new GroupManageFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("group", group);
            bundle.putSerializable("list", (Serializable) _list);
            fragment.setArguments(bundle);
            InterPhoneActivity.open(fragment);
        } else {
            if (g_news != null) {
                List<Contact.user> _list = BeanCloneUtil.cloneTo(list);
                GroupManageFragment fragment = new GroupManageFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("group", g_news);
                bundle.putBoolean("ow", isOw);
                bundle.putSerializable("list", (Serializable) _list);
                fragment.setArguments(bundle);
                InterPhoneActivity.open(fragment);

                fragment.setResultListener(new GroupManageFragment.ResultListener() {
                    @Override
                    public void resultListener(boolean type) {
                        if (type) {
                            getData();
                        }
                    }
                });
            } else {
                ToastUtils.show_always(activity.getActivity(), "数据出错了，请您稍后再试！");
            }
        }
    }

    /**
     * 跳转到群成员展示页
     */
    public void jumpGroupNumberShow() {
        if (list != null && list.size() > 0) {
            List<Contact.user> _list = BeanCloneUtil.cloneTo(list);
            GroupNumberShowFragment fragment = new GroupNumberShowFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("list", (Serializable) _list);
            fragment.setArguments(bundle);
            InterPhoneActivity.open(fragment);
        } else {
            ToastUtils.show_always(activity.getActivity(), "数据出错了，请您稍后再试！");
        }
    }

    /**
     * 跳转到模拟对讲界面
     *
     * @param type
     */
    public void jumpChannelSet(int type) {
        String c;
        if (type == 1) {
            c = channel1;
        } else {
            c = channel2;
        }
        SimulationInterPhoneFragment fragment = new SimulationInterPhoneFragment();
        Bundle bundle = new Bundle();
        bundle.putString("channel", c);
        fragment.setArguments(bundle);
        InterPhoneActivity.open(fragment);
    }

    /**
     * 退出该群
     */
    public void exit() {
        if (GlobalStateConfig.test) {
            activity.exitResult();// 设置返回监听
            InterPhoneActivity.close();
        } else {
            if (g_news != null) {
                // 判断是否是群主
                if (model.judgeMine(g_news)) {
                    //群主跳转到退出群组的界面
                    List<Contact.user> _list = BeanCloneUtil.cloneTo(list);
                    GroupExitFragment fragment = new GroupExitFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("group", g_news);
                    bundle.putSerializable("list", (Serializable) _list);
                    fragment.setArguments(bundle);
                    InterPhoneActivity.open(fragment);
                    fragment.setResultListener(new GroupExitFragment.ResultListener() {
                        @Override
                        public void resultListener(boolean type, int changeType) {
                            if (type) {
                                if (changeType == 0) {
                                    // 群已经解散
                                    InterPhoneActivity.close();
                                } else {
                                    // 群主已转让
                                    getData();
                                }
                            }
                        }
                    });
                } else {
                    activity.delDialogShow();
                }
            } else {
                ToastUtils.show_always(activity.getActivity(), "数据出错了，请您稍后再试！");
            }
        }
    }

    /**
     * 发送退出群组的请求
     */
    public void del() {
        if (GlobalNetWorkConfig.CURRENT_NETWORK_STATE_TYPE != -1) {
            activity.dialogShow();
            model.exitGroup(gid, new GroupNewsForAddModel.OnLoadInterface() {
                @Override
                public void onSuccess(Object o) {
                    activity.dialogCancel();
                    dealExitGroupSuccess(o);
                }

                @Override
                public void onFailure(String msg) {
                    activity.dialogCancel();
                }
            });
        } else {
            ToastUtils.show_always(activity.getActivity(), "网络连接失败，请稍后再试！");
        }
    }

    // 处理退出组返回的数据
    private void dealExitGroupSuccess(Object o) {
        try {
            String s = new Gson().toJson(o);
            JSONObject js = new JSONObject(s);
            int ret = js.getInt("ret");
            Log.e("退出群==ret", String.valueOf(ret));
            if (ret == 0) {
                activity.exitResult();// 设置返回监听
                activity.getActivity().sendBroadcast(new Intent(BroadcastConstants.GROUP_GET));// 更新群数据
                InterPhoneActivity.close();
            } else {
                ToastUtils.show_always(activity.getActivity(), "数据出错了，请您稍后再试！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.show_always(activity.getActivity(), "数据出错了，请您稍后再试！");
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

    /**
     * Grid的点击事件
     *
     * @param _list
     * @param position
     */
    public void setGridItemClick(List<Contact.user> _list, int position) {
        if (_list != null && _list.size() > 0) {
            int type = _list.get(position).getType();
            if (type == 1) {// 跳转到群成员界面，判断是否是自己好友
                String id = _list.get(position).getId().trim();
                if (id != null && !id.equals("")) {
                    if (model.judgeFriends(id)) {
                        PersonMessageFragment fragment = new PersonMessageFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("type", "true");
                        bundle.putString("id", id);
                        fragment.setArguments(bundle);
                        InterPhoneActivity.open(fragment);
                    } else {
                        PersonMessageFragment fragment = new PersonMessageFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("type", "false");
                        bundle.putString("id", id);
                        fragment.setArguments(bundle);
                        InterPhoneActivity.open(fragment);
                    }
                } else {
                    ToastUtils.show_always(activity.getActivity(), "数据出错了，请稍后再试！");
                }
            } else if (type == 2) {// 添加群成员
                List<Contact.user> list2 = BeanCloneUtil.cloneTo(list);
                GroupNumberAddFragment fragment = new GroupNumberAddFragment();
                Bundle bundle = new Bundle();
                bundle.putString("gid", gid);
                bundle.putSerializable("list", (Serializable) list2);// 成员列表
                fragment.setArguments(bundle);
                InterPhoneActivity.open(fragment);
            } else if (type == 3) {// 删除群成员
                String cid = "1";
                try {
                    cid = g_news.getOwner_id();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                List<Contact.user> list3 = BeanCloneUtil.cloneTo(list);
                GroupNumberDelFragment fragment = new GroupNumberDelFragment();
                Bundle bundle = new Bundle();
                bundle.putString("gid", gid);// 群id
                bundle.putString("id", cid);// 群所有者id

                bundle.putSerializable("list", (Serializable) list3);// 成员列表
                fragment.setArguments(bundle);
                InterPhoneActivity.open(fragment);
                fragment.setResultListener(new GroupNumberDelFragment.ResultListener() {
                    @Override
                    public void resultListener(boolean type) {
                        if (type) {
                            getData();
                        }
                    }
                });
            }
        } else {
            ToastUtils.show_always(activity.getActivity(), "数据出错了，请稍后再试！");
        }
    }

    /**
     * 跳转到二维码界面
     */
    public void jumpEWM() {
        if (g_news != null) {

            String image = "";
            String news = "";
            String name = "";
            try {
                image = g_news.getLogo_url();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                news = g_news.getIntroduction();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                name = g_news.getTitle();
            } catch (Exception e) {
                e.printStackTrace();
            }

            EWMShowFragment fragment = new EWMShowFragment();
            Bundle bundle = new Bundle();
            bundle.putString("from", "interPhone");// 路径来源
            bundle.putString("image", image);// 头像
            bundle.putString("news", news);// 简介
            bundle.putString("name", name);// 姓名

            Map<String, Object> map = new HashMap<>();
            map.put("type", "group");
            map.put("id", gid);
            String url = JsonEncloseUtils.jsonEnclose(map).toString();

            bundle.putString("uri", url);// 内容路径
            fragment.setArguments(bundle);
            InterPhoneActivity.open(fragment);
        }
    }

    /**
     * 开始对讲
     */
    public void interPhone() {
        if (gid != null && !gid.equals("")) {
            TalkHistory data = ChatPresenter.data;
            if (data != null) {
                // 此时有对讲状态
                String _t = data.getTyPe().trim();
                if (_t != null && !_t.equals("") && _t.equals("person")) {
                    // 此时的对讲状态是单对单
                    activity.confirmDialogShow(gid);
                } else if (_t != null && !_t.equals("") && _t.equals("group")) {
                    // 退出组，关闭对讲页面群组数据
                    activity.getActivity().sendBroadcast(new Intent(BroadcastConstants.VIEW_GROUP_CLOSE));
                    boolean et = enterGroup(gid);// 进入组
                    if (et) {
                        Log.e("信令控制", "进入组成功");
                        enterGroupOkData(gid);
                    } else {
                        Log.e("信令控制", "进入组失败");
                    }
                }
            } else {
                // 此时没有对讲状态,直接进入组
                boolean et = enterGroup(gid);
                if (et) {
                    Log.e("信令控制", "进入组成功");
                    enterGroupOkData(gid);
                } else {
                    Log.e("信令控制", "进入组失败");
                }
            }
        } else {
            ToastUtils.show_always(activity.getActivity(), "数据出错了，请您稍后再试！");
        }
    }

    /**
     * 同意挂断当前对讲后的操作
     */
    public void callOk(String groupId) {
        // 关闭对讲页面好友数据
        activity.getActivity().sendBroadcast(new Intent(BroadcastConstants.VIEW_PERSON_CLOSE));
        boolean et = enterGroup(groupId);// 进入组
        if (et) {
            Log.e("信令控制", "进入组成功");
            enterGroupOkData(groupId);
        } else {
            Log.e("信令控制", "进入组失败");
        }
    }

    // 进入组
    private boolean enterGroup(String groupId) {
        EventBus.getDefault().post(new MessageEvent("enterGroup&" + groupId));
        return true;
    }

    // 进入组成功后数据处理
    private void enterGroupOkData(String groupId) {
        model.del(groupId);// 删除跟本次id相关的数据
        model.add(model.assemblyData(g_news, GlobalStateConfig.ok, ""));// 把本次数据添加的数据库
        activity.getActivity().sendBroadcast(new Intent(BroadcastConstants.VIEW_INTER_PHONE));// 跳转到对讲主页
        activity.getActivity().sendBroadcast(new Intent(BroadcastConstants.VIEW_INTER_PHONE_CHAT_OK));// 对讲主页界面，数据更新
        activity.getActivity().sendBroadcast(new Intent(BroadcastConstants.VIEW_INTER_PHONE_CLOSE_ALL));
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
                if (gid != null && !gid.equals("")) {
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
        model = null;
    }
}
