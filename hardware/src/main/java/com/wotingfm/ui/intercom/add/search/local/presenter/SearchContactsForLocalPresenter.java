package com.wotingfm.ui.intercom.add.search.local.presenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import com.wotingfm.common.bean.MessageEvent;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.constant.BroadcastConstants;
import com.wotingfm.common.service.InterPhoneControl;
import com.wotingfm.common.utils.ToastUtils;
import com.wotingfm.ui.intercom.add.search.local.model.SearchContactsForLocalModel;
import com.wotingfm.ui.intercom.add.search.local.view.SearchContactsForLocalFragment;
import com.wotingfm.ui.intercom.alert.call.view.CallAlertActivity;
import com.wotingfm.ui.intercom.group.groupnews.add.view.GroupNewsForAddFragment;
import com.wotingfm.ui.intercom.main.chat.presenter.ChatPresenter;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;
import com.wotingfm.ui.intercom.person.personmessage.view.PersonMessageFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class SearchContactsForLocalPresenter {

    private SearchContactsForLocalFragment activity;
    private SearchContactsForLocalModel model;
    private MessageReceiver Receiver;
    private List<Contact.group> srcList_G;// 原始群组数据
    private List<Contact.user> srcList_p;// 原始好友数据
    private int callType = 1;// 呼叫类型 1好友，2群组
    private Contact.user user;

    public SearchContactsForLocalPresenter(SearchContactsForLocalFragment activity) {
        this.activity = activity;
        this.model = new SearchContactsForLocalModel(activity);
        setReceiver();// 广播
    }

    /**
     * 获取数据
     */
    public void getFriends() {
        srcList_p = model.filledData(model.getDataForPerson());
        srcList_G = model.getDataForGroup();
    }

    /**
     * 根据关键词调整展示数据
     *
     * @param s 为null或者“”
     */
    public void search(String s) {
        if (s != null && !s.trim().equals("")) {
            searchForData(s);
        } else {
            searchForNoData();
        }
    }

    // 此时没有搜索关键词的时候的数据
    private void searchForNoData() {
        activity.setViewOne();
    }

    // 此时有搜索关键词的时候的数据
    private void searchForData(String s) {
        if (srcList_G == null || srcList_G.size() == 0) {
            // 此时没有群组数据
            if (srcList_p == null || srcList_p.size() == 0) {
                // 此时没有好友》》》没有搜索数据
                activity.setView();
            } else {
                // 此时有好友》》》有搜索数据
                List<Contact.user> list = model.filterData(s, srcList_p);
                if (list.size() == 0) {
                    // 此时没有数据
                    activity.setView();
                } else {
                    // 此时个人有数据
                    activity.setViewForPerson(list);
                }
            }
        } else {
            List<Contact.group> groupList = new ArrayList<>();
            // 此时有群组数据
            for (int i = 0; i < srcList_G.size(); i++) {
                if (srcList_G.get(i).getTitle().contains(s)) {
                    groupList.add(srcList_G.get(i));
                }
            }
            if (groupList.size() == 0) {
                // 群组没有匹配数据
                if (srcList_p == null || srcList_p.size() == 0) {
                    // 此时没有好友数据
                    activity.setView();
                } else {
                    // 此时有好友数据
                    List<Contact.user> list = model.filterData(s, srcList_p);
                    if (list.size() == 0) {
                        // 此时没有数据
                        activity.setView();
                    } else {
                        // 此时个人有数据
                        activity.setViewForPerson(list);
                    }
                }
            } else {
                // 此时群组有数据
                if (srcList_p == null || srcList_p.size() == 0) {
                    // 此时群组有数据
                    activity.setViewForGroup(groupList);
                } else {
                    List<Contact.user> list = model.filterData(s, srcList_p);
                    if (list.size() == 0) {
                        // 此时群组有数据
                        activity.setViewForGroup(groupList);
                    } else {
                        // 此时群组。个人都有数据
                        activity.setViewForAll(list, groupList);
                    }
                }
            }
        }
    }

    /**
     * 跳转到好友详情
     *
     * @param position
     */
    public void jumpForPerson(List<Contact.user> person, int position) {
        String id = person.get(position).getId();
        if (id != null && !id.equals("")) {
            PersonMessageFragment fragment = new PersonMessageFragment();
            Bundle bundle = new Bundle();
            bundle.putString("id", id);
            bundle.putString("type", "true");
            fragment.setArguments(bundle);
            InterPhoneActivity.open(fragment);
        } else {
            ToastUtils.show_always(activity.getActivity(), "数据出错了，请稍后再试！");
        }
    }

    /**
     * 跳转到群组界面
     *
     * @param position
     */
    public void jumpForGroup(List<Contact.group> group, int position) {
        String id = group.get(position).getId();
        if (id != null && !id.equals("")) {
            GroupNewsForAddFragment fragment = new GroupNewsForAddFragment();
            Bundle bundle = new Bundle();
            bundle.putString("id", id);
            fragment.setArguments(bundle);
            InterPhoneActivity.open(fragment);
        } else {
            ToastUtils.show_always(activity.getActivity(), "数据出错了，请稍后再试！");
        }
    }

    /**
     * 呼叫
     *
     * @param position
     */
    public void call(List<Contact.user> person, int position) {
        String id = person.get(position).getId();
        if (id == null || id.trim().equals("")) {
            return;
        }
        if (ChatPresenter.data != null) {
            // 此时有对讲状态
            String _t = ChatPresenter.data.getTyPe().trim();
            if (_t != null && !_t.equals("") && _t.equals("person")) {// 此时的对讲状态是单对单
                activity.dialogShow(person.get(position), null, 1);// 弹出选择界面
            } else if (_t != null && !_t.equals("") && _t.equals("group")) {// 此时的对讲状态是群组
                // 退出组,关闭对讲页面群组数据
                activity.getActivity().sendBroadcast(new Intent(BroadcastConstants.VIEW_GROUP_CLOSE));
                boolean tok = talkOk(id, person.get(position).getAcc_id());// 进行呼叫
                if (tok) {
                    Log.e("信令控制", "呼叫成功");
                    callType = 1;
                    user = person.get(position);
                } else {
                    Log.e("信令控制", "呼叫失败");
                }
            }
        } else {
            // 此时没有对讲状态
            boolean to = talkOk(id, person.get(position).getAcc_id());// 进行呼叫
            if (to) {
                Log.e("信令控制", "呼叫成功");
                callType = 1;
                user = person.get(position);
            } else {
                Log.e("信令控制", "呼叫失败");
            }
        }
    }

    /**
     * 对讲，待对接
     *
     * @param position
     */
    public void interPhone(List<Contact.group> group, int position) {
        String id = group.get(position).getId();
        if (id == null || id.trim().equals("")) {
            return;
        }
        if (ChatPresenter.data != null) {
            // 此时有对讲状态
            String _t = ChatPresenter.data.getTyPe().trim();
            if (_t != null && !_t.equals("") && _t.equals("person")) {// 此时的对讲状态是单对单
                activity.dialogShow(null, group.get(position), 2);// 弹出选择界面
            } else if (_t != null && !_t.equals("") && _t.equals("group")) {// 此时的对讲状态是群组
                // 退出组,关闭对讲页面群组数据
                activity.getActivity().sendBroadcast(new Intent(BroadcastConstants.VIEW_GROUP_CLOSE));
                boolean et = enterGroup(id);// 进入组
                if (et) {
                    Log.e("信令控制", "进入组成功");
                    enterOkData(true, null, group.get(position));
                } else {
                    Log.e("信令控制", "进入组失败");
                }
            }
        } else {
            // 此时没有对讲状态,直接进入组
            boolean et = enterGroup(id);// 进入组
            if (et) {
                Log.e("信令控制", "进入组成功");
                enterOkData(true, null, group.get(position));
            } else {
                Log.e("信令控制", "进入组失败");
            }
        }
    }

    /**
     * 进行呼叫
     */
    public void callOk(Contact.user user, Contact.group group, int type) {
        if (type == 1) {
            // 挂断当前会话,关闭对讲页面好友数据
            activity.getActivity().sendBroadcast(new Intent(BroadcastConstants.VIEW_PERSON_CLOSE));
            boolean tok = talkOk(user.getId(), user.getAcc_id());// 进行呼叫
            if (tok) {
                Log.e("信令控制", "呼叫成功");
                callType = 1;
                this.user = user;
            } else {
                Log.e("信令控制", "呼叫失败");
            }
        } else {
            // 挂断当前会话,关闭对讲页面好友数据
            activity.getActivity().sendBroadcast(new Intent(BroadcastConstants.VIEW_PERSON_CLOSE));
            boolean et = enterGroup(group.getId());// 进入组
            if (et) {
                Log.e("信令控制", "进入组成功");
                enterOkData(true, null, group);
            } else {
                Log.e("信令控制", "进入组失败");
            }
        }
    }

    // 进入组
    private boolean enterGroup(String groupId) {
        EventBus.getDefault().post(new MessageEvent("enterGroup&" + groupId));
        return true;
    }

    // 开始个人对讲
    private boolean talkOk(String id, String accId) {
        if (id != null && !id.equals("")) {
            boolean b = InterPhoneControl.call(accId);// 发送呼叫请求
            if (b) {
                Intent intent = new Intent(activity.getActivity(), CallAlertActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", id);
                bundle.putString("fromType", "searchLocal");
                bundle.putString("roomId", accId);
                intent.putExtras(bundle);
                activity.startActivity(intent);
                return true;
            } else {
                ToastUtils.show_always(activity.getActivity(), "呼叫失败，请稍后再试！");
                return false;
            }
        } else {
            ToastUtils.show_always(activity.getActivity(), "数据出错了，请稍后再试！");
            return false;
        }
    }

    // 进入组成功后数据处理
    private void enterOkData(boolean b, Contact.user user, Contact.group group) {
        if (b) {
            // 好友
            model.del(user.getId());// 删除跟本次id相关的数据
            model.add(model.assemblyPersonData(user, GlobalStateConfig.ok, ""));// 把本次数据添加的数据库
        } else {
            // 群组
            model.del(group.getId());// 删除跟本次id相关的数据
            model.add(model.assemblyGroupData(group, GlobalStateConfig.ok, ""));// 把本次数据添加的数据库
        }
        activity.getActivity().sendBroadcast(new Intent(BroadcastConstants.VIEW_INTER_PHONE));// 跳转到对讲主页
        activity.getActivity().sendBroadcast(new Intent(BroadcastConstants.VIEW_INTER_PHONE_CHAT_OK));// 对讲主页界面，数据更新
        activity.getActivity().sendBroadcast(new Intent(BroadcastConstants.VIEW_INTER_PHONE_CLOSE_ALL));
    }

    // 设置广播接收器
    private void setReceiver() {
        if (Receiver == null) {
            Receiver = new MessageReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(BroadcastConstants.PUSH_CALL_SEND);// 单对单呼叫成功
            activity.getActivity().registerReceiver(Receiver, filter);
        }
    }

    class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BroadcastConstants.PUSH_CALL_SEND)) {// 单对单呼叫成功
                String type = intent.getStringExtra("fromType");
                if (type != null && !type.trim().equals("") && type.trim().equals("searchLocal")) {
                    if (callType == 1) {
                        enterOkData(true, user, null);
                    }
                }
            }
        }
    }

    /**
     * 数据销毁
     */
    public void destroy() {
        if (Receiver != null) {
            activity.getActivity().unregisterReceiver(Receiver);
            Receiver = null;
        }
        model = null;
    }
}
