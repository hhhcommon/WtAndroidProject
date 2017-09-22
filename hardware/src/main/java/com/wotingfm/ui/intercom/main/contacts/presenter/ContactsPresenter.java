package com.wotingfm.ui.intercom.main.contacts.presenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.constant.BroadcastConstants;
import com.wotingfm.common.constant.IntegerConstant;
import com.wotingfm.common.manager.InterPhoneControl;
import com.wotingfm.common.utils.CommonUtils;
import com.wotingfm.common.utils.ToastUtils;
import com.wotingfm.ui.intercom.alert.call.view.CallAlertActivity;
import com.wotingfm.ui.intercom.main.chat.presenter.ChatPresenter;
import com.wotingfm.ui.intercom.main.contacts.fragment.ContactsFragment;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;
import com.wotingfm.ui.intercom.main.contacts.model.ContactsModel;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;
import com.wotingfm.ui.intercom.person.personmessage.view.PersonMessageFragment;
import com.wotingfm.ui.user.logo.LogoActivity;

import java.util.List;

/**
 * 通讯录业务处理中心
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class ContactsPresenter {

    private final ContactsFragment activity;
    private final ContactsModel model;
    private MessageReceiver Receiver;
    private List<Contact.user> list;
    private int position;

    public ContactsPresenter(ContactsFragment activity) {
        this.activity = activity;
        this.model = new ContactsModel(activity);
        getData();    // 获取数据
        setReceiver();// 设置广播接收器
    }

    // 获取数据，数据适配
    private void getData() {
        if (CommonUtils.isLogin()) {
            getFriends();// 获取数据
            getRedData();// 设置小红点
        } else {
            activity.isLoginView(3);
        }
    }

    /**
     * 提示事件的点击事件
     *
     * @param type
     */
    public void tipClick(int type) {
        if (type == 3) {
            activity.getActivity().startActivity(new Intent(activity.getActivity(), LogoActivity.class)); // 跳转到登录界面
        } else if (type == 4) {
            getData();// 重新获取数据
        }
    }

    /**
     * 发送网络请求,获取好友
     */
    public void getFriends() {
        list = GlobalStateConfig.list_person;
        if (list != null && list.size() > 0) {
            List<Contact.user> srcList = model.assemblyData(list);
            if (srcList != null && srcList.size() > 0) {
                activity.setData(srcList);
            } else {
                activity.setData();
            }
        } else {
            activity.setData();
        }
        activity.isLoginView(0);
    }

    /**
     * 呼叫
     *
     * @param position
     */
    public void call(int position) {
        this.position = position;
        if (ChatPresenter.data != null) {
            // 此时有对讲状态
            String _t = ChatPresenter.data.getTyPe().trim();
            if (_t != null && !_t.equals("") && _t.equals("person")) {// 此时的对讲状态是单对单
                activity.dialogShow(position);  // 弹出选择界面
            } else if (_t != null && !_t.equals("") && _t.equals("group")) {// 此时的对讲状态是群组
                // 退出组，关闭对讲页面群组数据
                activity.getActivity().sendBroadcast(new Intent(BroadcastConstants.VIEW_GROUP_CLOSE));
                callPerson(position);// 进行呼叫

            }
        } else {
            // 此时没有对讲状态
             callPerson(position);// 进行呼叫

        }
    }

    // 进行呼叫
    private void callPerson(int position) {
        final String id = list.get(position).getId();
        final String acc_id = list.get(position).getAcc_id();
        if (id != null && !id.equals("")) {
             InterPhoneControl.call(acc_id, new InterPhoneControl.Listener() {
                @Override
                public void type(boolean b) {
                    if (b) {
                        Intent intent = new Intent(activity.getActivity(), CallAlertActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("id", id);
                        bundle.putString("fromType", "contacts");
                        intent.putExtras(bundle);
                        activity.startActivity(intent);
                    } else {
                        ToastUtils.show_always(activity.getActivity(), "呼叫失败，请稍后再试！");
                    }
                }
            });// 发送呼叫请求
        } else {
            ToastUtils.show_always(activity.getActivity(), "数据出错了，请稍后再试！");
        }
    }

    /**
     * 同意挂断当前对讲后的操作
     *
     * @param position
     */
    public void callOk(int position) {
        // 挂断当前会话,关闭对讲页面好友数据
        activity.getActivity().sendBroadcast(new Intent(BroadcastConstants.VIEW_PERSON_CLOSE));
       callPerson(position);// 进行呼叫

    }

    // 呼叫成功后操作
    private void pushCallOk() {
        model.del(list.get(position).getId());// 删除跟本次id相关的数据
        model.add(model.assemblyData(list.get(position), GlobalStateConfig.ok, ""));// 把本次数据添加的数据库
        activity.getActivity().sendBroadcast(new Intent(BroadcastConstants.VIEW_INTER_PHONE));// 跳转到对讲主页
        activity.getActivity().sendBroadcast(new Intent(BroadcastConstants.VIEW_INTER_PHONE_CHAT_OK));// 对讲主页界面，数据更新
    }

    /**
     * 设置小红点样式
     */
    private void getRedData() {
        int point_person = BSApplication.SharedPreferences.getInt(IntegerConstant.RED_POINT_PERSON, 0);
        activity.setRedView("person", point_person);
//        int point_group = BSApplication.SharedPreferences.getInt(IntegerConstant.RED_POINT_GROUP, 0);
//        activity.setRedView("group", point_group);
    }

    /**
     * 跳转到好友详情界面
     *
     * @param position
     */
    public void jump(final int position) {
        String id = list.get(position).getId();
        if (id != null && !id.equals("")) {
            PersonMessageFragment fragment = new PersonMessageFragment();
            Bundle bundle = new Bundle();
            bundle.putString("type", "true");// true是好友界面，false非好友界面
            bundle.putString("id", id);// 好友的id
            fragment.setArguments(bundle);
            InterPhoneActivity.open(fragment);
            fragment.setResultListener(new PersonMessageFragment.ResultListener() {
                @Override
                public void resultListener(boolean type, String name) {
                    if (type) {
                        if (name != null & !name.equals("")) {
                            changeData(position, name);
                        }
                    }
                }
            });
        } else {
            ToastUtils.show_always(activity.getActivity(), "数据出错了，请稍后再试");
        }

    }

    // 修改备注的返回监听
    private void changeData(int pos, String name) {
        list.get(pos).setAlias_name(name);
        activity.setData(list);
    }

    // 设置广播接收器
    private void setReceiver() {
        if (Receiver == null) {
            Receiver = new MessageReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(BroadcastConstants.CANCEL);       // 注销的监听
            filter.addAction(BroadcastConstants.LOGIN);        // 登录的监听
            filter.addAction(BroadcastConstants.PERSON_CHANGE);// 好友更改广播
            filter.addAction(BroadcastConstants.PUSH_CALL_SEND);// 单对单呼叫成功
            filter.addAction(BroadcastConstants.VIEW_INTER_PHONE_POINT_CHANGE);// 小红点更改的广播
            activity.getActivity().registerReceiver(Receiver, filter);
        }
    }

    class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BroadcastConstants.CANCEL)) {              // 注销的监听
                // 设置未登录界面
                activity.isLoginView(3);
            } else if (action.equals(BroadcastConstants.LOGIN)) {        // 登录的监听
                getData();
            } else if (action.equals(BroadcastConstants.PERSON_CHANGE)) {// 好友更改广播
                getData();
            } else if (action.equals(BroadcastConstants.VIEW_INTER_PHONE_POINT_CHANGE)) {
                // 小红点更改的广播
                getRedData();
            } else if (action.equals(BroadcastConstants.PUSH_CALL_SEND)) {// 单对单呼叫成功
               String type= intent.getStringExtra("fromType");
                if(type!=null&&!type.trim().equals("")&&type.trim().equals("contacts")){
                    pushCallOk();
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
    }

}