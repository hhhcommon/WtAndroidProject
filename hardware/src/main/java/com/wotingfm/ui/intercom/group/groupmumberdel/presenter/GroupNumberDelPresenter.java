package com.wotingfm.ui.intercom.group.groupmumberdel.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.woting.commonplat.config.GlobalNetWorkConfig;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.utils.ToastUtils;
import com.wotingfm.ui.intercom.group.groupmumberdel.model.GroupNumberDelModel;
import com.wotingfm.ui.intercom.group.groupmumberdel.view.GroupNumberDelFragment;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;
import com.wotingfm.ui.intercom.person.newfriend.model.NewFriend;
import com.wotingfm.ui.intercom.person.newfriend.model.NewFriendModel;
import com.wotingfm.ui.intercom.person.personmessage.view.PersonMessageFragment;
import com.wotingfm.ui.user.logo.LogoActivity;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

/**
 * 新的好友申请控制器
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class GroupNumberDelPresenter {

    private final GroupNumberDelFragment activity;
    private final GroupNumberDelModel model;
    private List<Contact.user> list;
    private String gid;// 群id

    public GroupNumberDelPresenter(GroupNumberDelFragment activity) {
        this.activity = activity;
        this.model = new GroupNumberDelModel();
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
                activity.updateUI(list);
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
        gid = bundle.getString("gid");// 群id
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
     * item的点击事件
     *
     * @param position
     */
    public void onClick(int position) {
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
    /**
     * 删除该条数据
     *
     * @param position
     */
    public void del(int position) {
        if (GlobalNetWorkConfig.CURRENT_NETWORK_STATE_TYPE != -1) {
            if (GlobalStateConfig.test) {
                // 测试数据==同意
                list.remove(position);
                activity.updateUI(list);
            } else {
                // 获取网络数据
                sendDel(position);
            }
        } else {
            ToastUtils.show_always(activity.getActivity(), "网络连接失败，请稍后再试！");
        }
    }

    // 删除群成员==删除
    private void sendDel(final int position) {
        activity.dialogShow();
        String s=list.get(position).getId();
        model.loadNewsForDel(gid,s,new GroupNumberDelModel.OnLoadInterface() {
            @Override
            public void onSuccess(Object o) {
                activity.dialogCancel();
                dealDelSuccess(o,position);
            }

            @Override
            public void onFailure(String msg) {
                activity.dialogCancel();
            }
        });
    }

    // 删除群成员=返回数据
    private void dealDelSuccess(Object o,int position) {
        try {
            String s = new Gson().toJson(o);
            JSONObject js = new JSONObject(s);
            int ret = js.getInt("ret");
            Log.e("ret", String.valueOf(ret));
            if (ret == 0) {
                list.remove(position);
                activity.updateUI(list);
            } else {
                String msg = js.getString("msg");
                if (msg != null && !msg.trim().equals("")) {
                    ToastUtils.show_always(activity.getActivity(), msg);
                }else{
                    ToastUtils.show_always(activity.getActivity(), "出错了，请您稍后再试！");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.show_always(activity.getActivity(), "出错了，请您稍后再试！");
        }
    }

}
