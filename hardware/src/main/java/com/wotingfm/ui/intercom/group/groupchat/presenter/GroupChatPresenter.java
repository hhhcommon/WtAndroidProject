package com.wotingfm.ui.intercom.group.groupchat.presenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.constant.BroadcastConstants;
import com.wotingfm.common.constant.StringConstant;
import com.wotingfm.common.utils.ToastUtils;
import com.wotingfm.ui.intercom.alert.call.view.CallAlertActivity;
import com.wotingfm.ui.intercom.group.groupchat.model.GroupChat;
import com.wotingfm.ui.intercom.group.groupchat.model.GroupChatModel;
import com.wotingfm.ui.intercom.group.groupchat.view.GroupChatFragment;
import com.wotingfm.ui.intercom.group.groupnews.add.view.GroupNewsForAddFragment;
import com.wotingfm.ui.intercom.group.groupnews.noadd.view.GroupNewsForNoAddFragment;
import com.wotingfm.ui.intercom.main.chat.model.DBTalkHistory;
import com.wotingfm.ui.intercom.main.chat.model.TalkHistory;
import com.wotingfm.ui.intercom.main.chat.presenter.ChatPresenter;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;
import com.wotingfm.ui.intercom.person.personnote.view.EditPersonNoteFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 群聊控制中心
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class GroupChatPresenter {

    private final GroupChatFragment activity;
    private final GroupChatModel model;
    private List<GroupChat> list;
    private MessageReceiver Receiver;

    public GroupChatPresenter(GroupChatFragment activity) {
        this.activity = activity;
        this.model = new GroupChatModel(activity);
        setReceiver();
        getData();    // 组装数据
    }

    // 组装数据
    private void getData() {
        if (GlobalStateConfig.test) {
            // 测试代码
            list = model.getTestData();
            activity.setView(list);
            activity.isLoginView(0);
        } else {
            list = model.assemblyData();
            if (list != null && list.size() > 0) {
                activity.setView(list);
                activity.isLoginView(0);
            } else {
                activity.isLoginView(1);
            }
        }
    }

    /**
     * 界面跳转
     *
     * @param g 父数据id
     * @param p 子数据id
     */
    public void jump(final int g, final int p) {
        String gid = "";
        try {
            gid = list.get(g).getPerson().get(p).getId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (g == 0) {
            GroupNewsForAddFragment fragment = new GroupNewsForAddFragment();
            Bundle bundle = new Bundle();
            bundle.putString("id", gid);
            bundle.putString("type", "true");
            fragment.setArguments(bundle);
            InterPhoneActivity.open(fragment);
            fragment.setResultListener(new GroupNewsForAddFragment.ResultListener() {
                @Override
                public void resultListener(boolean type) {
                    if (type) {
                        list.get(g).getPerson().remove(p);
                        activity.setView(list);
                    }
                }
            });
        } else {
            GroupNewsForAddFragment fragment = new GroupNewsForAddFragment();
            Bundle bundle = new Bundle();
            bundle.putString("id", gid);
            bundle.putString("type", "false");
            fragment.setArguments(bundle);
            InterPhoneActivity.open(fragment);
            fragment.setResultListener(new GroupNewsForAddFragment.ResultListener() {
                @Override
                public void resultListener(boolean type) {
                    if (type) {
                        list.get(g).getPerson().remove(p);
                        activity.setView(list);
                    }
                }
            });
        }
    }

    /**
     * 提示事件的点击事件
     *
     * @param type
     */
    public void tipClick(int type) {
        if (type == 1) {
            getData();
        }
    }

    /**
     * 对讲按钮
     *
     * @param groupPosition
     * @param childPosition
     */
    public void interPhone(int groupPosition, int childPosition) {
        String groupId = "";
        try {
            groupId = list.get(groupPosition).getPerson().get(childPosition).getId();// 点击按钮的群组id
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (groupId != null && !groupId.equals("")) {
            TalkHistory data = ChatPresenter.data;
            if (data != null) {
                // 此时有对讲状态
                String _t = data.getTyPe().trim();
                if (_t != null && !_t.equals("") && _t.equals("person")) {
                    // 此时的对讲状态是单对单
                    activity.dialogShow(groupId);
                } else if (_t != null && !_t.equals("") && _t.equals("group")) {
                    // 此时的对讲状态是群组
                    takeOverGroup();// 退出组
                    enterGroup(groupId);// 进入组
                }
            } else {
                // 此时没有对讲状态,直接进入组
                enterGroup(groupId);
            }
        } else {
            ToastUtils.show_always(activity.getActivity(), "数据出错了，请您稍后再试！");
        }
    }

    /**
     * 同意挂断当前对讲后的操作
     */
    public void callOk(String groupId) {
        // 退出个人对讲
        talkOver();
        // 关闭对讲页面好友数据
        activity.getActivity().sendBroadcast(new Intent(BroadcastConstants.VIEW_PERSON_CLOSE));
        // 进入组
        enterGroup(groupId);
    }

    // 进入组
    private void enterGroup(String groupId) {
        InterPhoneActivity.closeAll();
        activity.getActivity().sendBroadcast(new Intent(BroadcastConstants.VIEW_INTER_PHONE));
    }

    // 退出组
    private void takeOverGroup() {
        if (ChatPresenter.data != null && ChatPresenter.data.getID() != null) {
            // 退出组
            String id = ChatPresenter.data.getID();
        }
    }

    // 退出个人对讲
    private void talkOver() {
        // 挂断当前会话
        if (ChatPresenter.data != null && ChatPresenter.data.getID() != null) {
            ChatPresenter.data.getID();
        }
    }

    // 设置广播接收器
    private void setReceiver() {
        if (Receiver == null) {
            Receiver = new MessageReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(BroadcastConstants.GROUP_CHANGE);// 好友更改广播
            activity.getActivity().registerReceiver(Receiver, filter);
        }
    }

    class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BroadcastConstants.GROUP_CHANGE)) {
                getData();
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
