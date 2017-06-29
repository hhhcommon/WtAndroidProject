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
                try {
                    list.get(0).setType(1);
                    list.get(1).setType(2);
                    list.get(2).setType(2);
                    list.get(3).setType(2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                // 实际数据
                list = getNews();
            }
            if (list != null && list.size() > 0) {
                activity.setView(list);
            }
        } else {
            ToastUtils.show_always(activity.getActivity(), "网络连接失败，请稍后再试！");
        }
    }

    /**
     * 获取组装数据
     */
    public List<Contact.user> getNews() {
        Bundle bundle = activity.getArguments();
        String id = bundle.getString("id");// 群创建者id
        List<Contact.user> list = (List<Contact.user>) bundle.getSerializable("list");// 成员列表
        List<Contact.user> _list = new ArrayList<>();
        // 有群主
        if (id != null && !id.trim().equals("")) {
            // 添加群主
            for (int i = 0; i < list.size(); i++) {
                boolean b = list.get(i).is_admin();
                if (b) {
                    String _id = list.get(i).getId();
                    if (_id != null && !_id.trim().equals("")) {
                        if (id.equals(_id)) {
                            list.get(i).setType(1);
                            _list.add(list.get(i));
                        }
                    }
                }
            }

            // 添加管理员
            for (int i = 0; i < list.size(); i++) {
                boolean b = list.get(i).is_admin();
                if (b) {
                    String _id = list.get(i).getId();
                    if (_id != null && !_id.trim().equals("")) {
                        if (!id.equals(_id)) {
                            list.get(i).setType(2);
                            _list.add(list.get(i));
                        }
                    }
                }
            }

            // 添加成员
            for (int i = 0; i < list.size(); i++) {
                boolean b = list.get(i).is_admin();
                if (!b) {

                    list.get(i).setType(3);
                    _list.add(list.get(i));
                }
            }
        } else {
            // 添加管理员
            for (int i = 0; i < list.size(); i++) {
                boolean b = list.get(i).is_admin();
                if (b) {
                    list.get(i).setType(2);
                    _list.add(list.get(i));
                }
            }
            // 添加成员
            for (int i = 0; i < list.size(); i++) {
                boolean b = list.get(i).is_admin();
                if (!b) {

                    list.get(i).setType(3);
                    _list.add(list.get(i));
                }
            }
        }

        return _list;
    }

    /**
     * 查看用户详情
     *
     * @param position
     */
    public void showPersonNews(int position) {
        // 判断当前用户是否是自己好友
        boolean b = false;
        String gId = "";
        if (list != null && list.size() > 0) {
            Contact.user u = list.get(position);
            if (u != null) {
                String id = u.getId();
                gId = id;
                if (id != null && !id.trim().equals("")) {
                    List<Contact.user> _list = GlobalStateConfig.list_person;
                    if (_list != null && _list.size() > 0) {
                        for (int i = 0; i < _list.size(); i++) {
                            String _id = _list.get(i).getId();
                            if (_id.equals(id)) {
                                b = true;
                                break;
                            }
                        }
                    }
                }
            }
        }
        if (gId != null && !gId.equals("")) {
            if (b) {
                // 是自己好友
                PersonMessageFragment fragment = new PersonMessageFragment();
                Bundle bundle = new Bundle();
                bundle.putString("type", "true");
                bundle.putString("id", gId);
                fragment.setArguments(bundle);
                InterPhoneActivity.open(fragment);
            } else {
                // 不是自己好友
                PersonMessageFragment fragment = new PersonMessageFragment();
                Bundle bundle = new Bundle();
                bundle.putString("type", "false");
                bundle.putString("id", gId);
                fragment.setArguments(bundle);
                InterPhoneActivity.open(fragment);
            }
        } else {
            ToastUtils.show_always(activity.getActivity(), "数据出错了，请稍后再试！");
        }
    }
}
