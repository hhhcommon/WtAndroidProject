package com.wotingfm.ui.intercom.main.contacts.presenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.constant.BroadcastConstants;
import com.wotingfm.common.utils.CommonUtils;
import com.wotingfm.common.utils.ToastUtils;
import com.wotingfm.ui.intercom.main.chat.model.ChatModel;
import com.wotingfm.ui.intercom.main.chat.model.DBTalkHistory;
import com.wotingfm.ui.intercom.main.chat.model.TalkHistory;
import com.wotingfm.ui.intercom.main.chat.presenter.ChatPresenter;
import com.wotingfm.ui.intercom.main.contacts.fragment.ContactsFragment;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;
import com.wotingfm.ui.intercom.main.contacts.model.ContactsModel;
import com.wotingfm.ui.intercom.main.contacts.view.CharacterParser;
import com.wotingfm.ui.intercom.main.contacts.view.PinyinComparator;
import com.wotingfm.ui.intercom.main.view.InterPhoneFragment;
import com.wotingfm.ui.user.logo.LogoActivity;

import java.util.Collections;
import java.util.List;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class ContactsPresenter {

    private final ContactsFragment activity;
    private final ContactsModel model;
    private final CharacterParser characterParser;
    private final PinyinComparator pinyinComparator;
    private MessageReceiver Receiver;
    private List<Contact.user> list;

    public ContactsPresenter(ContactsFragment activity) {
        this.activity = activity;
        this.model = new ContactsModel(activity);

        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        setReceiver();
    }

    /**
     * 获取数据，数据适配
     */
    public void getData() {
        if (CommonUtils.isLogin()) {
            getFriends();
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
            // 跳转到登录界面
            activity.getActivity().startActivity(new Intent(activity.getActivity(), LogoActivity.class));
        } else if (type == 4) {
            // 重新获取数据
            getData();
        }
    }

    /**
     * 发送网络请求,获取好友
     */
    public void getFriends() {
        list = GlobalStateConfig.list_person;
        if (list != null && list.size() > 0) {
            assemblyData(list);
            activity.isLoginView(0);
            Log.e("1111","list大小=="+list.size());
        } else {
            activity.setData();
            activity.isLoginView(0);
            Log.e("1111","list为空");
        }
    }

    /**
     * 呼叫
     *
     * @param position
     */
    public void call(int position) {
        if (ChatPresenter.data != null) {
            // 此时有对讲状态
            String _t = ChatPresenter.data.getTyPe().trim();
            if (_t != null && !_t.equals("") && _t.equals("person")) {
                // 此时的对讲状态是单对单
                /**
                 * 此处需要弹出框提示以及呼叫流程
                 * 以下代码是呼叫成功后的代码调用
                 */
                // 关闭对讲页面好友数据
                activity.getActivity().sendBroadcast(new Intent(BroadcastConstants.VIEW_PERSON_CLOSE));
                Contact.user u = list.get(position);
                if (u != null) {
                    DBTalkHistory l = model.assemblyData(u);
                    String id =l.getID().trim();
                    if(id!=null&&!id.equals("")){
                        model.del(id);
                    }
                    model.add(l);
                    activity.getActivity().sendBroadcast(new Intent(BroadcastConstants.VIEW_INTER_PHONE));// 跳转到对讲主页
                    activity.getActivity().sendBroadcast(new Intent(BroadcastConstants.VIEW_INTER_PHONE_CHAT_OK));// 对讲主页界面，数据更新
                } else {
                    ToastUtils.show_always(activity.getActivity(), "数据出错了，请稍后再试！");
                }
            } else if (_t != null && !_t.equals("") && _t.equals("group")) {
                // 此时的对讲状态是群组
                // 关闭对讲页面群组数据
                activity.getActivity().sendBroadcast(new Intent(BroadcastConstants.VIEW_GROUP_CLOSE));
                /**
                 * 此处需要退出组对讲，然后进行呼叫流程
                 * 以下代码是呼叫成功后的代码调用
                 */
                Contact.user u = list.get(position);
                if (u != null) {
                    DBTalkHistory l = model.assemblyData(u);
                    String id =l.getID().trim();
                    if(id!=null&&!id.equals("")){
                        model.del(id);
                    }
                    model.add(l);
                    activity.getActivity().sendBroadcast(new Intent(BroadcastConstants.VIEW_INTER_PHONE));// 跳转到对讲主页
                    activity.getActivity().sendBroadcast(new Intent(BroadcastConstants.VIEW_INTER_PHONE_CHAT_OK));// 对讲主页界面，数据更新
                } else {
                    ToastUtils.show_always(activity.getActivity(), "数据出错了，请稍后再试！");
                }

            }
        } else {
            // 此时没有对讲状态
            /**
             * 此处需要呼叫流程
             * 以下代码是呼叫成功后的代码调用
             */
            //  添加到数据库
            Contact.user u = list.get(position);
            if (u != null) {
                DBTalkHistory l = model.assemblyData(u);
                String id =l.getID().trim();
                if(id!=null&&!id.equals("")){
                    model.del(id);
                }
                model.add(l);
                activity.getActivity().sendBroadcast(new Intent(BroadcastConstants.VIEW_INTER_PHONE));// 跳转到对讲主页
                activity.getActivity().sendBroadcast(new Intent(BroadcastConstants.VIEW_INTER_PHONE_CHAT_OK));// 对讲主页界面，数据更新
            } else {
                ToastUtils.show_always(activity.getActivity(), "数据出错了，请稍后再试！");
            }
        }
    }

    // 组装数据
    private void assemblyData(List<Contact.user> userList) {
        try {
            if (userList != null && userList.size() > 0) {
                // 根据 a - z 进行排序源数据
                List<Contact.user> srcList = filledData(userList);
                Collections.sort(srcList, pinyinComparator);
                activity.setData(srcList);
            } else {
                activity.setData();
            }
        } catch (Exception e) {
            e.printStackTrace();
            activity.isLoginView(4);
        }
    }

    // 为 ListView 填充数据
    private List<Contact.user> filledData(List<Contact.user> person) {
        for (int i = 0; i < person.size(); i++) {
            person.get(i).setName(person.get(i).getName());
            // 汉字转换成拼音
            String pinyin = characterParser.getSelling(person.get(i).getName());
            String sortString = pinyin.substring(0, 1).toUpperCase();
            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                person.get(i).setSortLetters(sortString.toUpperCase());
            } else {
                person.get(i).setSortLetters("#");
            }
        }
        return person;
    }

    // 设置广播接收器
    private void setReceiver() {
        if (Receiver == null) {
            Receiver = new MessageReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(BroadcastConstants.CANCEL);
            filter.addAction(BroadcastConstants.LOGIN);
            filter.addAction(BroadcastConstants.PERSON_CHANGE);
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
                getData();
            } else if (action.equals(BroadcastConstants.PERSON_CHANGE)) {
                Log.e("1111","广播接收");
                getData();
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
