package com.wotingfm.ui.intercom.group.groupchat.presenter;

import android.os.Bundle;

import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.constant.StringConstant;
import com.wotingfm.ui.intercom.group.groupchat.model.GroupChat;
import com.wotingfm.ui.intercom.group.groupchat.model.GroupChatModel;
import com.wotingfm.ui.intercom.group.groupchat.view.GroupChatFragment;
import com.wotingfm.ui.intercom.group.groupnews.noadd.view.GroupNewsForNoAddFragment;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;

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
    public GroupChatPresenter(GroupChatFragment activity) {
        this.activity = activity;
        this.model = new GroupChatModel();
    }

    // 数据组装
    private List<GroupChat> assemblyData(List<Contact.group> list) {
        String id = BSApplication.SharedPreferences.getString(StringConstant.USER_ID, "");
        ArrayList<Contact.group> srcList_G = new ArrayList<>();// 我加入的群
        ArrayList<Contact.group> srcList_O = new ArrayList<>();// 我创建的群
        if (id != null && !id.equals("")) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getCreator_id()!=null&&!list.get(i).getCreator_id().equals("")&&list.get(i).getCreator_id().trim().equals(id)) {
                    srcList_O.add(list.get(i));
                } else {
                    srcList_G.add(list.get(i));
                }
            }

            List<GroupChat> g_list = new ArrayList<>();
            GroupChat GC = new GroupChat();
            GC.setGroupNumber(String.valueOf(srcList_O.size()));
            GC.setName("我创建的群");
            GC.setPerson(srcList_O);
            g_list.add(GC);

            GroupChat GC1 = new GroupChat();
            GC1.setGroupNumber(String.valueOf(srcList_G.size()));
            GC1.setName("我加入的群");
            GC1.setPerson(srcList_G);
            g_list.add(GC1);

            return g_list;
        } else {
            return null;
        }
    }

    /**
     * 组装数据
     */
    public void getData() {
        if (GlobalStateConfig.test) {
            // 测试代码
            list = model.getTestData();
            activity.setView(list);
            activity.isLoginView(0);
        } else {
            if (GlobalStateConfig.list_group != null && GlobalStateConfig.list_group.size() > 0) {
                list = assemblyData(GlobalStateConfig.list_group);
                if (list != null && list.size() > 0) {
                    activity.setView(list);
                    activity.isLoginView(0);
                } else {
                    activity.isLoginView(1);
                }
            } else {
                activity.isLoginView(1);
            }
        }
    }

    /**
     * 界面跳转
     *
     * @param g
     * @param p
     */
    public void jump(int g, int p) {
        GroupNewsForNoAddFragment fragment = new GroupNewsForNoAddFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", list.get(g).getPerson().get(p).getId());
        fragment.setArguments(bundle);
        InterPhoneActivity.open(fragment);
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

}
