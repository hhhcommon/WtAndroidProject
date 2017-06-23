package com.wotingfm.ui.intercom.main.contacts.presenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.constant.BroadcastConstants;
import com.wotingfm.common.utils.CommonUtils;
import com.wotingfm.ui.intercom.main.contacts.fragment.ContactsFragment;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;
import com.wotingfm.ui.intercom.main.contacts.view.CharacterParser;
import com.wotingfm.ui.intercom.main.contacts.view.PinyinComparator;
import com.wotingfm.ui.user.logo.LogoActivity;
import java.util.Collections;
import java.util.List;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class ContactsPresenter {

    private final ContactsFragment activity;
    private final CharacterParser characterParser;
    private final PinyinComparator pinyinComparator;
    private MessageReceiver Receiver;


    public ContactsPresenter(ContactsFragment activity) {
        this.activity = activity;
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
        List<Contact.user> list = GlobalStateConfig.list_person;
        if (list != null && list.size() > 0) {
            assemblyData(list);
            activity.isLoginView(0);
        } else {
            activity.setData();
            activity.isLoginView(0);
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
            person.get(i).setName(person.get(i).getNickName());
            // 汉字转换成拼音
            String pinyin = characterParser.getSelling(person.get(i).getNickName());
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
            }else if (action.equals(BroadcastConstants.PERSON_CHANGE)) {
                getData();
            }
        }
    }

}
