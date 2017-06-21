package com.wotingfm.ui.intercom.group.applygrouptype.presenter;

import android.widget.Toast;

import com.wotingfm.ui.intercom.group.applygrouptype.model.ApplyGroupTypeModel;
import com.wotingfm.ui.intercom.group.applygrouptype.view.ApplyGroupTypeFragment;
import com.wotingfm.ui.intercom.group.creat.model.CreateGroupMainModel;
import com.wotingfm.ui.intercom.group.standbychannel.view.StandbyChannelFragment;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;
import com.wotingfm.ui.user.login.model.Login;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class ApplyGroupTypePresenter {

    private final ApplyGroupTypeFragment activity;
    private final ApplyGroupTypeModel model;
    private int viewType = 1; // 1公开群，2密码群，3审核群

    public ApplyGroupTypePresenter(ApplyGroupTypeFragment activity) {
        this.activity = activity;
        this.model = new ApplyGroupTypeModel();
    }

    /**
     * 提交数据
     */
    public void ok() {
        if (checkData()) {
            String password = "";
            int type = 1;
            send(password, type);
        }
    }

    /**
     * 设置初始界面样式
     */
    public void setView() {
        activity.setPasswordView(false);
        activity.setShenView(false);
    }

    /**
     * 设置审核群
     */
    public void shen() {
        if (viewType == 1) {
            activity.setPasswordView(false);
            activity.setShenView(true);
            viewType = 3;
        } else if (viewType == 2) {
            activity.setPasswordView(false);
            activity.setShenView(true);
            viewType = 3;
        } else if (viewType == 3) {
            activity.setPasswordView(false);
            activity.setShenView(false);
            viewType = 1;
        }
    }

    /**
     * 设置密码群
     */
    public void password() {
        if (viewType == 1) {
            activity.setPasswordView(true);
            activity.setShenView(false);
            viewType = 2;
        } else if (viewType == 2) {
            activity.setPasswordView(false);
            activity.setShenView(false);
            viewType = 1;
        } else if (viewType == 3) {
            activity.setPasswordView(true);
            activity.setShenView(false);
            viewType = 2;
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // 检查数据的正确性  检查通过则进行登录
    private boolean checkData() {
        Toast.makeText(activity.getActivity(), "测试", Toast.LENGTH_LONG).show();
        return true;
    }

    // 发送网络请求
    private void send(String password, int type) {
        model.loadNews(password, type, new ApplyGroupTypeModel.OnLoadInterface() {
            @Override
            public void onSuccess(Object o) {
//                loginView.removeDialog();
                dealLoginSuccess(o);
            }

            @Override
            public void onFailure(String msg) {
//                loginView.removeDialog();
//                ToastUtils.showVolleyError(loginView);
            }
        });
    }

    // 处理返回数据
    private void dealLoginSuccess(Object o) {

    }

}
