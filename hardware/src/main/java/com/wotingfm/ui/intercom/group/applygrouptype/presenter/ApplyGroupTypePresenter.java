package com.wotingfm.ui.intercom.group.applygrouptype.presenter;

import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.woting.commonplat.config.GlobalNetWorkConfig;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.utils.ToastUtils;
import com.wotingfm.ui.intercom.group.applygrouptype.model.ApplyGroupTypeModel;
import com.wotingfm.ui.intercom.group.applygrouptype.view.ApplyGroupTypeFragment;
import com.wotingfm.ui.intercom.group.creat.model.CreateGroupMainModel;
import com.wotingfm.ui.intercom.group.standbychannel.view.StandbyChannelFragment;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;
import com.wotingfm.ui.user.login.model.Login;

import org.json.JSONObject;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class ApplyGroupTypePresenter {

    private final ApplyGroupTypeFragment activity;
    private final ApplyGroupTypeModel model;
    private boolean b1 = false;// 密码群 选择状态
    private boolean b2 = false;// 审核群 选择状态
    private String password, type,groupId;
    private Contact.group group;

    public ApplyGroupTypePresenter(ApplyGroupTypeFragment activity) {
        this.activity = activity;
        this.model = new ApplyGroupTypeModel();
        getData();
    }

    // 获取上级界面传递数据
    private void getData() {
        if(GlobalStateConfig.test){
            groupId="000";
            password="123456";
            type="2";
        }else{
            try {
                group = (Contact.group)activity.getArguments().getSerializable("group");
                groupId=group.getId();
                password=group.getPassword();
                type=group.getMember_access_mode();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 提交数据
     */
    public void ok(String mm) {
        if (checkData(mm)) {
            if (GlobalNetWorkConfig.CURRENT_NETWORK_STATE_TYPE != -1) {
                if (GlobalStateConfig.test) {
                    // 测试数据
                    ToastUtils.show_always(activity.getActivity(), "修改成功");
                    activity.setResult(2, "123456");
                    InterPhoneActivity.close();
                } else {
                    // 实际数据
                    send(mm);
                }
            } else {
                ToastUtils.show_always(activity.getActivity(), "网络连接失败，请稍后再试！");
            }
        }
    }

    /**
     * 设置初始界面样式
     * 0密码群，1审核群，3密码审核群
     */
    public void setView() {
        if (type != null && !type.equals("")) {
            if (type.equals("0")) {
                if (password != null && !password.equals("")) {
                    activity.setPasswordViewNews(password);
                }
                activity.setPasswordView(true);
                activity.setShenView(false);
            } else if (type.equals("1")) {
                activity.setPasswordView(false);
                activity.setShenView(true);
            } else if (type.equals("2")) {
                if (password != null && !password.equals("")) {
                    activity.setPasswordViewNews(password);
                }
                activity.setPasswordView(true);
                activity.setShenView(true);
            } else {
                activity.setPasswordView(false);
                activity.setShenView(false);
            }
        } else {
            activity.setPasswordView(false);
            activity.setShenView(false);
        }

    }

    /**
     * 设置审核群
     */
    public void shen() {
        if (b2) {
            activity.setShenView(false);
            b2 = false;
        } else {
            activity.setShenView(true);
            b2 = true;
        }
    }

    /**
     * 设置密码群
     */
    public void password() {
        if (b1) {
            activity.setPasswordView(false);
            b1 = false;
        } else {
            activity.setPasswordView(true);
            b1 = true;
        }
    }

    // 检查数据的正确性  检查通过则进行登录
    private boolean checkData(String mm) {
        if (b1) {
            if (mm == null || mm.trim().equals("")) {
                Toast.makeText(activity.getActivity(), "进群密码不能为空", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        if (b1 && b2) {
            Toast.makeText(activity.getActivity(), "请选择加群方式", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    // 发送网络请求
    private void send(final String mm) {
        int type = 0;
        if (b1) {
            type = 0;
        }
        if (b2) {
            type = 1;
        }
        if (b1 && b2) {
            type = 2;
        }
        final int FType = type;
        activity.dialogShow();
        model.loadNews(mm, type, new ApplyGroupTypeModel.OnLoadInterface() {
            @Override
            public void onSuccess(Object o) {
                activity.dialogCancel();
                dealLoginSuccess(o, mm, FType);
            }

            @Override
            public void onFailure(String msg) {
                activity.dialogCancel();
                ToastUtils.show_always(activity.getActivity(), "修改失败，请稍后再试");
            }
        });
    }

    // 处理返回数据
    private void dealLoginSuccess(Object o, String mm, int FType) {
        try {
            String s = new Gson().toJson(o);
            JSONObject js = new JSONObject(s);
            int ret = js.getInt("ret");
            Log.e("修改入群方式=ret", String.valueOf(ret));
            if (ret == 0) {
                if (FType == 0) {
                    activity.setResult(0, mm);
                } else if (FType == 1) {
                    activity.setResult(1, "");
                } else if (FType == 2) {
                    activity.setResult(2, mm);
                }

                ToastUtils.show_always(activity.getActivity(), "修改成功");
                InterPhoneActivity.close();
            } else {
                ToastUtils.show_always(activity.getActivity(), "修改失败，请稍后再试！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 设置数据出错界面
            ToastUtils.show_always(activity.getActivity(), "修改失败，请稍后再试！");
        }

    }

}
