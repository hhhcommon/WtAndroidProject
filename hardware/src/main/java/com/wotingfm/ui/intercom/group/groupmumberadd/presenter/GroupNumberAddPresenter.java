package com.wotingfm.ui.intercom.group.groupmumberadd.presenter;

import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.woting.commonplat.config.GlobalNetWorkConfig;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.utils.ToastUtils;
import com.wotingfm.ui.intercom.group.groupmumberadd.model.GroupNumberAddModel;
import com.wotingfm.ui.intercom.group.groupmumberadd.view.GroupNumberAddFragment;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;
import com.wotingfm.ui.intercom.person.personmessage.view.PersonMessageFragment;

import org.json.JSONObject;

import java.util.List;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class GroupNumberAddPresenter {

    private GroupNumberAddFragment activity;
    private GroupNumberAddModel model;
    private List<Contact.user> list;
    private String gid;// 群id

    public GroupNumberAddPresenter(GroupNumberAddFragment activity) {
        this.activity = activity;
        this.model = new GroupNumberAddModel();
    }

    /**
     * 获取数据设置界面数据
     */
    public void getData() {
        List<Contact.user> _list = GlobalStateConfig.list_person;
        try {
            gid = activity.getArguments().getString("gid");// 群id
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<Contact.user> src_list = null;// 成员列表
        try {
            src_list = (List<Contact.user>) activity.getArguments().getSerializable("list");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (_list != null && _list.size() > 0 && src_list != null && src_list.size() > 0) {
            list = model.assemblyData(src_list, _list);
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

    /**
     * 查看用户详情
     *
     * @param position
     */
    public void showPersonNews(int position) {
        if (list != null && list.size() > 0) {
            Contact.user u = list.get(position);
            if (u != null) {
                String id = u.getId().trim();
                if (id != null && !id.equals("")) {
                    PersonMessageFragment fragment = new PersonMessageFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("type", "true");
                    bundle.putString("id", id);
                    fragment.setArguments(bundle);
                    InterPhoneActivity.open(fragment);
                }
            }
        }
    }

    /**
     * 添加群成员
     */
    public void apply() {
        if (GlobalNetWorkConfig.CURRENT_NETWORK_STATE_TYPE != -1) {
            if (GlobalStateConfig.test) {
                // 测试数据
                String s = model.getString(list);
                list.remove(s);
                activity.setView(list);
            } else {
                // 获取网络数据
                sendApply();
            }
        } else {
            ToastUtils.show_always(activity.getActivity(), "网络连接失败，请稍后再试！");
        }
    }

    // 发送申请请求
    private void sendApply() {
        if (list != null && list.size() > 0) {
            String s = model.getString(list);
            if (s != null && !s.equals("")) {
                activity.dialogShow();
                model.loadNewsForAdd(gid, s, new GroupNumberAddModel.OnLoadInterface() {
                    @Override
                    public void onSuccess(Object o) {
                        activity.dialogCancel();
                        dealSuccess(o);
                    }

                    @Override
                    public void onFailure(String msg) {
                        activity.dialogCancel();
                        ToastUtils.show_always(activity.getActivity(), "出错了，请您稍后再试！");
                    }
                });
            }
        }
    }

    // 添加群成员=返回数据
    private void dealSuccess(Object o) {
        try {
            String s = new Gson().toJson(o);
            JSONObject js = new JSONObject(s);
            int ret = js.getInt("ret");
            Log.e("ret", String.valueOf(ret));
            if (ret == 0) {
                ToastUtils.show_always(activity.getActivity(), "您的邀请已经发送！");
                InterPhoneActivity.close();
//                remove();
            } else {
                String msg = js.getString("msg");
                if (msg != null && !msg.trim().equals("")) {
                    ToastUtils.show_always(activity.getActivity(), msg);
                } else {
                    ToastUtils.show_always(activity.getActivity(), "出错了，请您稍后再试！");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.show_always(activity.getActivity(), "出错了，请您稍后再试！");
        }
    }

    /**
     * 点击按钮的数据更改
     *
     * @param position
     */
    public void changeData(int position) {
        boolean type = list.get(position).is_admin();
        if (type) {
            list.get(position).setIs_admin(false);
        } else {
            list.get(position).setIs_admin(true);
        }
        activity.setView(list);
    }

    // 移除邀请后的数据
    private void remove() {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).is_admin()) {
                list.remove(i);
                i--;
            }
        }
        activity.setView(list);
    }

    /**
     * 数据销毁
     */
    public void destroy() {
        model = null;
    }
}
