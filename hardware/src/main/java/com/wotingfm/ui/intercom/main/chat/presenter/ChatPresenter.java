package com.wotingfm.ui.intercom.main.chat.presenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import com.woting.commonplat.config.GlobalNetWorkConfig;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.constant.BroadcastConstants;
import com.wotingfm.common.utils.CommonUtils;
import com.wotingfm.common.utils.ToastUtils;
import com.wotingfm.ui.intercom.group.groupnews.add.view.GroupNewsForAddFragment;
import com.wotingfm.ui.intercom.group.groupnews.noadd.view.GroupNewsForNoAddFragment;
import com.wotingfm.ui.intercom.main.chat.model.ChatModel;
import com.wotingfm.ui.intercom.main.chat.model.DBTalkHistory;
import com.wotingfm.ui.intercom.main.chat.model.TalkHistory;
import com.wotingfm.ui.intercom.main.chat.view.ChatFragment;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;
import com.wotingfm.ui.user.logo.LogoActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class ChatPresenter {

    private final ChatFragment activity;
    private final ChatModel model;
    private List<TalkHistory> list;
    public static TalkHistory data;// 此时当前的聊天对象，挂断后置为null
    private MessageReceiver Receiver;


    public ChatPresenter(ChatFragment activity) {
        this.activity = activity;
        this.model = new ChatModel(activity);
        setReceiver();
    }

    /**
     * 获取展示数据
     */
    public void getData() {
        if (GlobalNetWorkConfig.CURRENT_NETWORK_STATE_TYPE != -1) {
            if (CommonUtils.isLogin()) {
                list = model.getData();

                if (list != null && list.size() > 0) {
                    activity.isLoginView(0);
                    activity.updateUI(list);
                } else {
                    activity.isLoginView(1);
                }

            } else {
                activity.isLoginView(3);
            }
        } else {
            activity.isLoginView(2);
        }
    }

    /**
     * 点击对讲按钮的操作
     *
     * @param position
     */
    public void call(int position) {
        if (list != null && list.size() > 0) {
            TalkHistory _data = list.get(position);
            String type = _data.getTyPe().trim();
            if (type != null && !type.equals("") && type.equals("person")) {
                // 单对单呼叫
                if (data != null) {
                    // 此时有对讲状态
                    String _t = data.getTyPe().trim();
                    if (_t != null && !_t.equals("") && _t.equals("person")) {
                        // 此时的对讲状态是单对单
                        /**
                         * 此处需要弹出框提示以及呼叫流程
                         * 以下代码是呼叫成功后的代码调用
                         */

                        TalkHistory _d = list.remove(position);
                        activity.setPersonViewShow(_d);
                        model.add(model.assemblyData(_d));
                        list.add(0, data);
                        if (list != null && list.size() > 0) activity.updateUI(list);
                        data = _d;// 替换此时对讲对象
                    } else if (_t != null && !_t.equals("") && _t.equals("group")) {
                        // 此时的对讲状态是群组
                        /**
                         * 此处需要退出组对讲，然后进行呼叫流程
                         * 以下代码是呼叫成功后的代码调用
                         */
                        activity.setGroupViewClose();
                        TalkHistory _d = list.remove(position);
                        activity.setPersonViewShow(_d);
                        model.add(model.assemblyData(_d));
                        list.add(0, data);
                        if (list != null && list.size() > 0) activity.updateUI(list);
                        data = _d;// 替换此时对讲对象
                    }
                } else {
                    // 此时没有对讲状态
                    /**
                     * 此处需要呼叫流程
                     * 以下代码是呼叫成功后的代码调用
                     */
                    TalkHistory _d = list.remove(position);
                    activity.setPersonViewShow(_d);
                    model.add(model.assemblyData(_d));
                    if (list != null && list.size() > 0) activity.updateUI(list);
                    data = _d;// 替换此时对讲对象
                }

            } else if (type != null && !type.equals("") && type.equals("group")) {
                // 组对讲
                if (data != null) {
                    // 此时有对讲状态
                    String _t = data.getTyPe().trim();
                    if (_t != null && !_t.equals("") && _t.equals("person")) {
                        // 此时的对讲状态是单对单
                        /**
                         * 此处需要先挂断电话
                         * 然后入组，以下是入组成功的代码
                         */
                        activity.setPersonViewClose();
                        TalkHistory _d = list.remove(position);
                        activity.setGroupViewShow(_d);
                        model.add(model.assemblyData(_d));
                        list.add(0, data);
                        if (list != null && list.size() > 0) activity.updateUI(list);
                        data = _d;// 替换此时对讲对象
                    } else if (_t != null && !_t.equals("") && _t.equals("group")) {
                        // 此时的对讲状态是群组
                        /**
                         * 此时需要先退出上一个组，然后进入当前组
                         * 以下是入组成功的代码
                         */
                        TalkHistory _d = list.remove(position);
                        activity.setGroupViewShow(_d);
                        model.add(model.assemblyData(_d));
                        list.add(0, data);
                        if (list != null && list.size() > 0) activity.updateUI(list);
                        data = _d;// 替换此时对讲对象
                    }
                } else {
                    // 此时没有对讲状态
                    TalkHistory _d = list.remove(position);
                    activity.setGroupViewShow(_d);
                    model.add(model.assemblyData(_d));
                    if (list != null && list.size() > 0) activity.updateUI(list);
                    data = _d;// 替换此时对讲对象
                }
            } else {
                // 数据有问题
                ToastUtils.show_always(activity.getActivity(), "数据出错了，请您稍后再试！");
            }
        }
    }

    /**
     * 更改此时对讲状态为null
     */
    public void setNull() {
        if (list == null) list = new ArrayList<>();
        list.add(0, data);
        activity.updateUI(list);
        data = null;
    }

    /**
     * 删除数据
     *
     * @param position
     */
    public void del(int position) {
        if (list != null && list.size() > 0) {
            // 从数据库中删除该条数据
            String id = list.get(position).getID();
            if (id != null && id.trim().equals("")) {
                model.del(id);
            }
            // 界面更改
            list.remove(position);
            if (data != null) {
                activity.updateUI(list);
            } else {
                // 此时没有激活状态
                if (list != null && list.size() > 0) {
                    activity.isLoginView(0);
                    activity.updateUI(list);
                } else {
                    activity.isLoginView(1);
                }
            }
        }
    }

    /**
     * 界面跳转
     */
    public void jump(){
        GroupNewsForAddFragment fragment = new GroupNewsForAddFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", data.getID());
        fragment.setArguments(bundle);
        InterPhoneActivity.open(fragment);
    }

    /**
     * 异常按钮的操作
     *
     * @param type
     */
    public void tipClick(int type) {
        if (type == 3) {
            // 没有登录,打开登录界面
            activity.getActivity().startActivity(new Intent(activity.getActivity(), LogoActivity.class));
        }else if(type==1){
            getData();
        }
    }

    // 进入当前组
    private boolean enterGroup(String id) {

        return true;
    }

    // 退出当前组
    private boolean exitGroup(String id) {

        return true;
    }

    // 呼叫好友
    private boolean callPerson(String id) {

        return true;
    }

    // 挂断电话
    private boolean bsckPerson(String id) {

        return true;
    }

    // 设置广播接收器
    private void setReceiver() {
        if (Receiver == null) {
            Receiver = new MessageReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(BroadcastConstants.CANCEL);// 注销登录广播
            filter.addAction(BroadcastConstants.LOGIN);// 登录广播
            filter.addAction(BroadcastConstants.PERSON_CHANGE);// 群组或者好友信息更改后重新适配数据
            filter.addAction(BroadcastConstants.VIEW_GROUP_CLOSE);// 群组页面关闭广播
            filter.addAction(BroadcastConstants.VIEW_PERSON_CLOSE);// 好友界面关闭广播
            filter.addAction(BroadcastConstants.VIEW_INTER_PHONE_CHAT_OK);// 有新的对讲连接时，对讲界面数据更改
            activity.getActivity().registerReceiver(Receiver, filter);
        }
    }

    class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BroadcastConstants.CANCEL)) {
                // 设置未登录界面
                activity.isLoginView(3);
            } else if (action.equals(BroadcastConstants.LOGIN)) {
                // 登录后重新获取数据
                getData();
            } else if (action.equals(BroadcastConstants.PERSON_CHANGE)) {
                // 群组或者好友信息更改后重新适配数据
                getData();
            } else if (action.equals(BroadcastConstants.VIEW_GROUP_CLOSE)) {
                // 关闭群组对讲界面
                activity.setGroupViewClose();
            } else if (action.equals(BroadcastConstants.VIEW_PERSON_CLOSE)) {
                // 关闭好友对讲界面
                activity.setPersonViewClose();
            } else if (action.equals(BroadcastConstants.VIEW_INTER_PHONE_CHAT_OK)) {
                // 有新的对讲连接时，对讲界面数据更改
                setViewForOK();
            }
        }
    }

    // 界面更改：当有新的对讲连接成功时，接收到界面更改广播后进行更改
    private void setViewForOK() {
        activity.isLoginView(0);
        DBTalkHistory f = model.getForFirst();// 得到数据库第一条数据
        if (f != null) {
            String type = f.getTyPe().trim();
            if (type != null && !type.equals("") && type.equals("person")) {
                String id = f.getID().trim();
                if (id != null && !id.equals("")) {
                    Contact.user u = model.getUser(id);// 根据库表的第一条数据的id得到对应的通讯录里边的好友数据
                    TalkHistory _d = model.assemblyDataForPerson(u, f);// 根据两条对应数据组装展示数据
                    activity.setPersonViewShow(_d);
                    list = model.delForList(list, id);// 删除列表中重复数据
                    if (list != null && list.size() > 0) activity.updateUI(list);
                    data = _d;// 替换此时对讲对象
                } else {
                    Log.e("setViewForOK", "person的id类型为空");
                }
            } else if (type != null && !type.equals("") && type.equals("group")) {

                String id = f.getID().trim();
                if (id != null && !id.equals("")) {
                    Contact.group u = model.getGroup(id);// 根据库表的第一条数据的id得到对应的通讯录里边的群组数据
                    TalkHistory _d = model.assemblyDataForGroup(u, f);// 根据两条对应数据组装展示数据
                    activity.setGroupViewShow(_d);
                    list = model.delForList(list, id);// 删除列表中重复数据
                    if (list != null && list.size() > 0) activity.updateUI(list);
                    data = _d;// 替换此时对讲对象
                } else {
                    Log.e("setViewForOK", "group的id类型为空");
                }
            } else {
                Log.e("setViewForOK", "type类型为空");
            }
        }
    }

    // 界面销毁,注销广播
    public void destroy() {
        if (Receiver != null) {
            activity.getActivity().unregisterReceiver(Receiver);
            Receiver = null;
        }
    }
}
