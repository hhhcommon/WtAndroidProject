package com.wotingfm.ui.user.login.presenter;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.woting.commonplat.config.GlobalNetWorkConfig;
import com.wotingfm.common.constant.BroadcastConstants;
import com.wotingfm.common.utils.ToastUtils;
import com.wotingfm.ui.user.login.model.LoginModel;
import com.wotingfm.ui.user.login.view.LoginFragment;
import com.wotingfm.ui.user.logo.LogoActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.List;

/**
 * 登录界面处理器
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class LoginPresenter {

    private final LoginFragment activity;
    private final LoginModel model;
    private boolean eyeShow = false;

    public LoginPresenter(LoginFragment activity) {
        this.activity = activity;
        this.model = new LoginModel();
    }

    /**
     * 本地登录
     *
     * @param userName 用户名
     * @param password 密码
     * @return true:可以登录 / false：不能登录
     */
    public void login(String userName, String password) {
        if (checkData(userName, password)) {
            if (GlobalNetWorkConfig.CURRENT_NETWORK_STATE_TYPE != -1) {
                send(userName, password);
            } else {
                ToastUtils.show_always(activity.getActivity(), "网络连接失败，请稍后再试！");
            }
        }
    }

    /**
     * 密码明文显示
     * 默认为密码
     */
    public void setEye() {
        if (eyeShow) {
            activity.setEyeShow(eyeShow);
            eyeShow = false;
        } else {
            activity.setEyeShow(eyeShow);
            eyeShow = true;
        }
    }

    /**
     * 更改按钮背景色
     *
     * @param userName 用户名
     * @param password 密码
     */
    public void getBtView(String userName, String password) {
        boolean bt;
        if (userName != null && !userName.trim().equals("")) {
            if (password != null && !password.trim().equals("") && password.length() > 5) {
                bt = true;
            } else {
                bt = false;
            }
        } else {
            bt = false;
        }
        activity.setLoginBackground(bt);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // 发送网络请求
    private void send(String userName, String password) {
        activity.dialogShow();
        model.loadNews(userName, password, new LoginModel.OnLoadInterface() {
            @Override
            public void onSuccess(Object o) {
                activity.dialogCancel();
                dealSuccess(o);
            }

            @Override
            public void onFailure(String msg) {
                activity.dialogCancel();
                ToastUtils.show_always(activity.getActivity(), "登录失败，请稍后再试");
            }
        });
    }

    // 处理返回数据
    private void dealSuccess(Object o) {
        try {
            Gson g = new GsonBuilder().serializeNulls().create();
            String s = g.toJson(o);
            JSONObject js = new JSONObject(s);
            int ret = js.getInt("ret");
            Log.e("ret", String.valueOf(ret));
            if (ret == 0) {
                String msg = js.getString("data");
                JSONTokener jsonParser = new JSONTokener(msg);
                JSONObject arg1 = (JSONObject) jsonParser.nextValue();
                try {
                    String token = arg1.getString("token");
                    // 保存后台获取到的token
                    if (token != null && !token.trim().equals("")) {
                        model.saveToken(token);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JSONObject ui = (JSONObject) new JSONTokener(arg1.getString("user")).nextValue();
                // 保存用户数据
                if (ui != null) {
                    model.saveUserInfo(ui);
                }
                ToastUtils.show_always(activity.getActivity(), "登录成功");
                // 发送登录广播通知所有界面
                activity.getActivity().sendBroadcast(new Intent(BroadcastConstants.LOGIN));
                LogoActivity.closeActivity();
            } else {
                String msg = js.getString("msg");
                if (msg != null && !msg.trim().equals("")) {
                    Log.e("登录失败返回数据", msg);
                    ToastUtils.show_always(activity.getActivity(), msg);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.show_always(activity.getActivity(), "登录失败，请稍后再试");
        }
    }

    /*
     * 检查数据的正确性  检查通过则进行登录
     *
     * @param userName 用户名
     * @param password 密码
     * @return true:可以登录 / false：不能登录
     */
    private boolean checkData(String userName, String password) {
        if (userName == null || userName.trim().equals("")) {
            Toast.makeText(activity.getActivity(), "登录账号不能为空", Toast.LENGTH_LONG).show();
            return false;
        }
        if (password == null || password.trim().equals("")) {
            Toast.makeText(activity.getActivity(), "密码不能为空", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}
