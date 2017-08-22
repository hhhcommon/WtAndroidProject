package com.woting.ui.intercom.group.groupmumberdel.presenter;

import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.woting.commonplat.config.GlobalNetWorkConfig;
import com.woting.common.application.BSApplication;
import com.woting.common.config.GlobalStateConfig;
import com.woting.common.constant.StringConstant;
import com.woting.common.utils.ToastUtils;
import com.woting.ui.intercom.group.groupmumberdel.model.GroupNumberDelModel;
import com.woting.ui.intercom.group.groupmumberdel.view.GroupNumberDelFragment;
import com.woting.ui.intercom.main.contacts.model.Contact;
import com.woting.ui.intercom.main.view.InterPhoneActivity;
import com.woting.ui.intercom.person.personmessage.view.PersonMessageFragment;

import org.json.JSONObject;

import java.util.List;

/**
 * 新的好友申请控制器
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class GroupNumberDelPresenter {

    private GroupNumberDelFragment activity;
    private GroupNumberDelModel model;
    private List<Contact.user> list;
    private String gid;// 群id
    private boolean change = false;// 上层界面是否更改

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
            } else {
                // 实际数据
                list = getNews();
            }
            if (list != null && list.size() > 0) {
                activity.updateUI(list);
                activity.isLoginView(0);
            } else {
                activity.isLoginView(1);
            }
        } else {
            activity.isLoginView(2);
        }
    }

    /**
     * 上层界面是否更改
     */
    public void isChange() {
        activity.setResult(change);
    }

    /**
     * 获取组装数据
     */
    public List<Contact.user> getNews() {
        String id = null;// 群主id
        try {
            id = activity.getArguments().getString("id");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            gid = activity.getArguments().getString("gid");// 群id
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
     * item的点击事件
     *
     * @param position
     */
    public void onClick(int position) {
        // 判断当前用户是否是自己好友
        boolean b = model.isFriend(list, position);
        String Id = null;
        try {
            Id = list.get(position).getId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (Id != null && !Id.equals("")) {
            String id = BSApplication.SharedPreferences.getString(StringConstant.USER_ID, "");
            if (!id.equals(Id)) {
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
        String s = list.get(position).getId();
        model.loadNewsForDel(gid, s, new GroupNumberDelModel.OnLoadInterface() {
            @Override
            public void onSuccess(Object o) {
                activity.dialogCancel();
                dealDelSuccess(o, position);
            }

            @Override
            public void onFailure(String msg) {
                activity.dialogCancel();
            }
        });
    }

    // 删除群成员=返回数据
    private void dealDelSuccess(Object o, int position) {
        try {
            String s = new Gson().toJson(o);
            JSONObject js = new JSONObject(s);
            int ret = js.getInt("ret");
            Log.e("ret", String.valueOf(ret));
            if (ret == 0) {
                change = true;
                list.remove(position);
                activity.updateUI(list);
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
     * 数据销毁
     */
    public void destroy() {
        model = null;
    }
}
