package com.wotingfm.ui.intercom.group.groupmumberadd.presenter;

import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.woting.commonplat.config.GlobalNetWorkConfig;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.utils.ToastUtils;
import com.wotingfm.ui.intercom.group.groupmumberadd.model.GroupNumberAddModel;
import com.wotingfm.ui.intercom.group.groupmumberadd.view.GroupNumberAddFragment;
import com.wotingfm.ui.intercom.group.groupmumberdel.model.GroupNumberDelModel;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;
import com.wotingfm.ui.intercom.person.personmessage.view.PersonMessageFragment;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class GroupNumberAddPresenter {

    private final GroupNumberAddFragment activity;
    private final GroupNumberAddModel model;
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
            }
        } else {
            ToastUtils.show_always(activity.getActivity(), "网络连接失败，请稍后再试！");
        }
    }

    /**
     * 获取数据
     */
    public List<Contact.user> getNews() {
        List<Contact.user> list = GlobalStateConfig.list_person;
        Bundle bundle = activity.getArguments();
        gid = bundle.getString("gid");// 群id
        return list;
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
     *
     * @param position
     */
    public void apply(int position) {
        if (GlobalNetWorkConfig.CURRENT_NETWORK_STATE_TYPE != -1) {
            if (GlobalStateConfig.test) {
                // 测试数据
                list.remove(position);
                activity.setView(list);
            } else {
                // 获取网络数据
                sendApply(position);
            }
        } else {
            ToastUtils.show_always(activity.getActivity(), "网络连接失败，请稍后再试！");
        }
    }

    // 发送申请请求
    private void sendApply(final int position) {
        activity.dialogShow();
        if (list != null && list.size() > 0) {
            String id = list.get(position).getId();
            if (id != null && !id.equals("")) {
                model.loadNewsForAdd(gid, id, new GroupNumberAddModel.OnLoadInterface() {
                    @Override
                    public void onSuccess(Object o) {
                        activity.dialogCancel();
                        dealSuccess(o, position);
                    }

                    @Override
                    public void onFailure(String msg) {
                        activity.dialogCancel();
                    }
                });
            }
        }
    }

    // 添加群成员=返回数据
    private void dealSuccess(Object o, int position) {
        try {
            String s = new Gson().toJson(o);
            JSONObject js = new JSONObject(s);
            int ret = js.getInt("ret");
            Log.e("ret", String.valueOf(ret));
            if (ret == 0) {
                list.remove(position);
                activity.setView(list);
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
}
