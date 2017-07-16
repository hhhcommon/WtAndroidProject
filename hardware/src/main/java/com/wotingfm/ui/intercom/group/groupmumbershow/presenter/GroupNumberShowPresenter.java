package com.wotingfm.ui.intercom.group.groupmumbershow.presenter;

import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.woting.commonplat.config.GlobalNetWorkConfig;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.utils.L;
import com.wotingfm.common.utils.ToastUtils;
import com.wotingfm.ui.intercom.group.groupmumbershow.model.GroupNumberShowModel;
import com.wotingfm.ui.intercom.group.groupmumbershow.view.GroupNumberShowFragment;
import com.wotingfm.ui.intercom.group.groupnews.add.model.GroupNewsForAddModel;
import com.wotingfm.ui.intercom.group.groupnews.add.view.GroupNewsForAddFragment;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;
import com.wotingfm.ui.intercom.person.personmessage.view.PersonMessageFragment;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class GroupNumberShowPresenter {

    private final GroupNumberShowFragment activity;
    private final GroupNumberShowModel model;
    private List<Contact.user> list;


    public GroupNumberShowPresenter(GroupNumberShowFragment activity) {
        this.activity = activity;
        this.model = new GroupNumberShowModel();
    }

    /**
     * 获取数据设置界面数据
     */
    public void getData() {
        if (GlobalNetWorkConfig.CURRENT_NETWORK_STATE_TYPE != -1) {
            if (GlobalStateConfig.test) {
                // 测试数据
                list = model.getData();
            } else {
                // 实际数据
                list = getNews();
            }
            if (list != null && list.size() > 0) {
                activity.setView(list);
                activity.isLoginView(0);
            }else{
                activity.isLoginView(1);
            }
        } else {
            activity.isLoginView(2);
        }
    }

    /**
     * 获取组装数据
     */
    public List<Contact.user> getNews() {
        String id = null;// 群创建者id
        try {
            id = activity.getArguments().getString("id");
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Contact.user> list = null;// 成员列表
        try {
            list = (List<Contact.user>) activity.getArguments().getSerializable("list");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list != null && list.size() > 0) {
            List<Contact.user> _list = model.assemblyData(list, id);
            return _list;
        } else {
            return null;
        }
    }

    /**
     * 查看用户详情
     *
     * @param position
     */
    public void showPersonNews(int position) {
        // 判断当前用户是否是自己好友
        boolean b = model.isFriend(list, position);
        String Id = null;
        try {
            Id = list.get(position).getId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (Id != null && !Id.equals("")) {
            if (b) {
                // 是自己好友
                PersonMessageFragment fragment = new PersonMessageFragment();
                Bundle bundle = new Bundle();
                bundle.putString("type", "true");
                bundle.putString("id", Id);
                fragment.setArguments(bundle);
                InterPhoneActivity.open(fragment);
            } else {
                // 不是自己好友
                PersonMessageFragment fragment = new PersonMessageFragment();
                Bundle bundle = new Bundle();
                bundle.putString("type", "false");
                bundle.putString("id", Id);
                fragment.setArguments(bundle);
                InterPhoneActivity.open(fragment);
            }
        } else {
            ToastUtils.show_always(activity.getActivity(), "数据出错了，请稍后再试！");
        }
    }
}
