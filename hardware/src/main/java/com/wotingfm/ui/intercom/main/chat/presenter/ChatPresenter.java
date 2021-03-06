package com.wotingfm.ui.intercom.main.chat.presenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.netease.nimlib.sdk.avchat.AVChatManager;
import com.woting.commonplat.config.GlobalNetWorkConfig;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.constant.BroadcastConstants;
import com.wotingfm.common.manager.InterPhoneControl;
import com.wotingfm.common.utils.CommonUtils;
import com.wotingfm.common.utils.ToastUtils;
import com.wotingfm.ui.bean.MessageEvent;
import com.wotingfm.ui.intercom.alert.call.view.CallAlertActivity;
import com.wotingfm.ui.intercom.group.groupnews.add.view.GroupNewsForAddFragment;
import com.wotingfm.ui.intercom.main.chat.model.ChatModel;
import com.wotingfm.ui.intercom.main.chat.model.DBTalkHistory;
import com.wotingfm.ui.intercom.main.chat.model.TalkHistory;
import com.wotingfm.ui.intercom.main.chat.view.ChatFragment;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;
import com.wotingfm.ui.intercom.person.personmessage.view.PersonMessageFragment;
import com.wotingfm.ui.user.logo.LogoActivity;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;
import org.json.JSONTokener;

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
    private int position;

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
     * 删除数据
     *
     * @param position
     */
    public void del(int position) {
        if (list != null && list.size() > 0) {
            // 从数据库中删除该条数据
            String id = list.get(position).getID();
            if (id != null && !id.trim().equals("")) {
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
     * 点击对讲按钮的操作
     *
     * @param position
     */
    public void call(int position) {
        this.position = position;
        if (list != null && list.size() > 0) {
            TalkHistory _data = list.get(position);// 按钮点击数据
            String type = _data.getTyPe().trim();// 当前按钮点击的对讲类型
            if (type != null && !type.equals("") && type.equals("person")) {
                // 单对单呼叫
                if (data != null) {
                    // 此时有对讲状态
                    String _t = data.getTyPe().trim();
                    if (_t != null && !_t.equals("") && _t.equals("person")) {
                        // 此时的对讲状态是单对单
                        String n_id = _data.getID();// 本次id
                        String acc_id = _data.getACC_ID();// 本次acc_id
                        activity.dialogShow(n_id, 1, acc_id);
                    } else if (_t != null && !_t.equals("") && _t.equals("group")) {
                        // 退出组，关闭对讲页面群组数据
                        activity.getActivity().sendBroadcast(new Intent(BroadcastConstants.VIEW_GROUP_CLOSE));
                        // 此时的对讲状态是群组
                        String n_id = _data.getID();
                        callPerson(n_id, _data.getACC_ID());// 呼叫好友
                    }
                } else {
                    // 此时没有对讲状态
                    String n_id = _data.getID();
                    callPerson(n_id, _data.getACC_ID());// 呼叫好友
                }
            } else if (type != null && !type.equals("") && type.equals("group")) {
                // 组对讲
                if (data != null) {
                    // 此时有对讲状态
                    String _t = data.getTyPe().trim();
                    if (_t != null && !_t.equals("") && _t.equals("person")) {
                        // 此时的对讲状态是单对单
                        String n_id = _data.getID();// 本次id
                        String acc_id = _data.getACC_ID();// 本次acc_id
                        activity.dialogShow(n_id, 2, acc_id);
                    } else if (_t != null && !_t.equals("") && _t.equals("group")) {
                        // 此时的对讲状态是群组
                        activity.getActivity().sendBroadcast(new Intent(BroadcastConstants.VIEW_GROUP_CLOSE));
                        String n_id = _data.getID();
                        boolean cp = enterGroup(n_id);// 进入组
                        if (cp) {
                            Log.e("信令控制", "进入组成功");
                            enterGroupOkData();
                        } else {
                            Log.e("信令控制", "进入组失败");
                        }
                    }
                } else {
                    // 此时没有对讲状态
                    String n_id = _data.getID();
                    boolean cp = enterGroup(n_id);// 进入组
                    if (cp) {
                        Log.e("信令控制", "进入组成功");
                        enterGroupOkData();
                    } else {
                        Log.e("信令控制", "进入组失败");
                    }
                }
            } else {
                // 数据有问题
                ToastUtils.show_always(activity.getActivity(), "数据出错了，请您稍后再试！");
            }
        }
    }

    /**
     * 弹框按钮成功
     *
     * @param new_id
     * @param type
     */
    public void callOk(String new_id, int type, String accId) {
        if (type == 1) {
            talkOver();// 挂断当前个人对讲
            activity.setPersonViewClose();
            callPerson(new_id, accId);  // 呼叫好友
        } else {
            // 挂断当前会话,关闭对讲页面好友数据
            activity.getActivity().sendBroadcast(new Intent(BroadcastConstants.VIEW_PERSON_CLOSE));
            boolean cp = enterGroup(new_id);// 进入群组
            if (cp) {
                Log.e("信令控制", "进入组成功");
                enterGroupOkData();
            } else {
                Log.e("信令控制", "进入组失败");
            }
        }
    }

    // 进入当前组
    private boolean enterGroup(String id) {
        EventBus.getDefault().post(new MessageEvent("enterGroup&" + id));
        return true;
    }

    // 挂断当前个人对讲
    public void talkOver() {
        if (ChatPresenter.data != null && ChatPresenter.data.getID() != null) {
            InterPhoneControl.over();// 结束通话
        }
    }

    // 退出组对讲
    public void talkOverGroup() {
        if (ChatPresenter.data != null && ChatPresenter.data.getID() != null) {
            InterPhoneControl.quitRoomGroup(ChatPresenter.data.getID(), new InterPhoneControl.Listener() {
                @Override
                public void type(boolean b) {
                    if (b) {
                        Log.e("退出组对讲", "退出组成功");
                    } else {
                        Log.e("退出组对讲", "退出组失败");
                    }

                }
            });
        }
    }

    // 呼叫好友
    private void callPerson(final String id, final String accId) {
        if (id != null && !id.equals("")) {
            InterPhoneControl.call(accId, new InterPhoneControl.Listener() {
                @Override
                public void type(boolean b) {
                    if (b) {
                        Intent intent = new Intent(activity.getActivity(), CallAlertActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("id", id);
                        bundle.putString("fromType", "chat");
                        bundle.putString("roomId", accId);
                        intent.putExtras(bundle);
                        activity.startActivity(intent);

                    } else {
                        ToastUtils.show_always(activity.getActivity(), "呼叫失败，请稍后再试！");
                    }
                }
            });
            // 发送呼叫请求
        } else {
            ToastUtils.show_always(activity.getActivity(), "数据出错了，请稍后再试！");
        }
    }

    // 进入组成功后数据处理
    private void enterGroupOkData() {
        model.del(list.get(position).getID());// 删除跟本次id相关的数据
        model.add(model.assemblyData(list.get(position), GlobalStateConfig.ok, ""));// 把本次数据添加的数据库
        activity.getActivity().sendBroadcast(new Intent(BroadcastConstants.VIEW_INTER_PHONE_CHAT_OK));// 对讲主页界面，数据更新
    }

    /**
     * item按钮的操作
     *
     * @param position
     */
    public void itemClick(int position) {
        if (list != null && list.size() > 0) {
            TalkHistory _data = list.get(position);
            String type = _data.getTyPe().trim();
            if (type != null && !type.equals("") && type.equals("person")) {
                String id = list.get(position).getID();
                if (id != null && !id.trim().equals("")) {
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
                    // 数据有问题
                    ToastUtils.show_always(activity.getActivity(), "数据出错了，请您稍后再试！");
                }
            } else if (type != null && !type.equals("") && type.equals("group")) {
                String id = list.get(position).getID();
                if (id != null && !id.trim().equals("")) {
                    if (model.judgeGroupCreate(id)) {
                        GroupNewsForAddFragment fragment = new GroupNewsForAddFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("id", id);
                        bundle.putString("type", "true");
                        fragment.setArguments(bundle);
                        InterPhoneActivity.open(fragment);
                    } else {
                        GroupNewsForAddFragment fragment = new GroupNewsForAddFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("id", id);
                        bundle.putString("type", "false");
                        fragment.setArguments(bundle);
                        InterPhoneActivity.open(fragment);
                    }
                } else {
                    // 数据有问题
                    ToastUtils.show_always(activity.getActivity(), "数据出错了，请您稍后再试！");
                }
            } else {
                // 数据有问题
                ToastUtils.show_always(activity.getActivity(), "数据出错了，请您稍后再试！");
            }
        }
    }

    /**
     * 界面跳转(此时群肯定是自己所在的群)
     * 判断该群是不是自己创建的
     */
    public void jumpGroup() {
        if (model.judgeGroupCreate(data.getID())) {
            GroupNewsForAddFragment fragment = new GroupNewsForAddFragment();
            Bundle bundle = new Bundle();
            bundle.putString("id", data.getID());
            bundle.putString("type", "true");
            fragment.setArguments(bundle);
            InterPhoneActivity.open(fragment);
        } else {
            GroupNewsForAddFragment fragment = new GroupNewsForAddFragment();
            Bundle bundle = new Bundle();
            bundle.putString("id", data.getID());
            bundle.putString("type", "false");
            fragment.setArguments(bundle);
            InterPhoneActivity.open(fragment);
        }
    }

    /**
     * 界面跳转
     * 判断是不是自己好友
     */
    public void jumpPerson() {
        String id = data.getID();
        if (id != null && !id.trim().equals("")) {
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
        }
    }

    /**
     * 更改此时对讲状态为null
     */
    public void setNull() {
        if (list == null) list = new ArrayList<>();
        if (data != null) list.add(0, data);
        activity.updateUI(list);
        data = null;
        position++;
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
        } else if (type == 1) {
            getData();
        }
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
            filter.addAction(BroadcastConstants.PUSH_CALL_SEND);// 单对单呼叫成功
            filter.addAction(BroadcastConstants.VIEW_INTER_PHONE_CHAT_OK);// 有新的对讲连接时，对讲界面数据更改
            filter.addAction(BroadcastConstants.PUSH_CHAT_CLOSE);// 无人在说话
            filter.addAction(BroadcastConstants.PUSH_CHAT_OPEN);// 有人在说话
            filter.addAction(BroadcastConstants.PUSH_CHAT_GROUP_NUM);// // 群成员在线人数变化
            filter.addAction(BroadcastConstants.GROUP_USER_CHANGE);// // 群成员变化
            activity.getActivity().registerReceiver(Receiver, filter);
        }
    }

    class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case BroadcastConstants.CANCEL:// 设置未登录界面
                    activity.isLoginView(3);
                    break;
                case BroadcastConstants.LOGIN:// 登录后重新获取数据
                    getData();
                    break;
                case BroadcastConstants.PERSON_CHANGE:// 群组或者好友信息更改后重新适配数据
                    getData();
                    break;
                case BroadcastConstants.VIEW_GROUP_CLOSE:// 关闭群组对讲界面
                    talkOverGroup();// 退出对讲组
                    activity.setGroupViewClose();
                    break;
                case BroadcastConstants.VIEW_PERSON_CLOSE:// 关闭好友对讲界面
                    talkOver();// 挂断当前个人对讲
                    activity.setPersonViewClose();
                    break;
                case BroadcastConstants.VIEW_INTER_PHONE_CHAT_OK:// 有新的对讲连接时，对讲界面数据更改
                    AVChatManager.getInstance().muteLocalAudio(true);// 关闭音频
                    toggleSpeaker(true);
                    setViewForOK();
                    break;
                case BroadcastConstants.PUSH_CALL_SEND:// 单对单呼叫成功
                    String type = intent.getStringExtra("fromType");
                    if (type != null && !type.trim().equals("") && type.trim().equals("chat")) {
                        enterGroupOkData();
                    }
                    break;
                case BroadcastConstants.PUSH_CHAT_OPEN:// 有人在说话
                    String name = intent.getStringExtra("name");
                    String url = intent.getStringExtra("url");
                    setViewChatOpen(name, url);
                    break;
                case BroadcastConstants.PUSH_CHAT_CLOSE:// 无人在说话
                    setViewChatClose();
                    break;
                case BroadcastConstants.PUSH_CHAT_GROUP_NUM:// 群成员在线人数
                    String num = intent.getStringExtra("num");
                    activity.setGroupViewNumIn(num);
                    break;
                case BroadcastConstants.GROUP_USER_CHANGE:// 群成员
                    if (data != null) {// 此时有对讲状态
                        String _t = data.getTyPe().trim();
                        if (_t != null && !_t.equals("") && _t.equals("group")) {
                            String id = data.getID().trim();
                            getGroupPerson(id);
                        }
                    }
                    break;
            }
        }
    }

    // 进入组后获取群成员、接收到群成员变化的消息后获取群成员
    private void getGroupPerson(String id) {
        model.getGroupPerson(id, new ChatModel.OnLoadInterface() {
            @Override
            public void mun(Object o) {
                dealGroupPersonSuccess(o);
            }
        });
    }

    // 更改当前说话人界面类型
    private void setViewChatOpen(String name, String url) {
        if (data != null) {// 此时有对讲状态
            String _t = data.getTyPe().trim();
            if (_t != null && !_t.equals("") && _t.equals("person")) {
                activity.setPersonViewTalk(name, url);
            } else if (_t != null && !_t.equals("") && _t.equals("group")) {
                activity.setGroupViewTalk(name, url);
            }
        }
    }

    // 更改当前说话人界面类型
    private void setViewChatClose() {
        if (data != null) {// 此时有对讲状态
            String _t = data.getTyPe().trim();
            if (_t != null && !_t.equals("") && _t.equals("person")) {
                activity.setPersonViewTalkClose();
            } else if (_t != null && !_t.equals("") && _t.equals("group")) {
                activity.setGroupViewTalkClose();
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
                    if (list != null && list.size() > 0)
                        list = model.delForList(list, id);// 删除列表中重复数据
                    if (list != null && list.size() > 0) activity.updateUI(list);
                    data = _d;// 替换此时对讲对象
                } else {
                    Log.e("setViewForOK", "person的id类型为空");
                }
            } else if (type != null && !type.equals("") && type.equals("group")) {

                String id = f.getID().trim();
                if (id != null && !id.equals("")) {
                    getGroupPerson(id);
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

    // 处理群成员返回的数据
    private void dealGroupPersonSuccess(Object o) {
        try {
            String s = new GsonBuilder().serializeNulls().create().toJson(o);
            JSONObject js = new JSONObject(s);
            int ret = js.getInt("ret");
            if (ret == 0) {
                String msg = js.getString("data");
                JSONTokener jsonParser = new JSONTokener(msg);
                JSONObject arg1 = (JSONObject) jsonParser.nextValue();
                String group = arg1.getString("users");
                // 群成员
                GlobalStateConfig.list_group_user = new Gson().fromJson(group, new TypeToken<List<Contact.user>>() {
                }.getType());
                activity.setGroupViewNum(GlobalStateConfig.list_group_user.size() + "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置扬声器的开关
     *
     * @param b
     */
    public void toggleSpeaker(boolean b) {
        boolean type = AVChatManager.getInstance().speakerEnabled();
        ToastUtils.show_always(activity.getActivity(), "此时扬声器的开关" + type);
        if (b) {
            if (!type) {
                AVChatManager.getInstance().setSpeaker(!AVChatManager.getInstance().speakerEnabled());// 设置扬声器是否开启
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
