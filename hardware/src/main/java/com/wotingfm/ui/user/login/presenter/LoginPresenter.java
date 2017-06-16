package com.wotingfm.ui.user.login.presenter;

import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wotingfm.common.utils.ToastUtils;
import com.wotingfm.ui.user.login.model.LoginModel;
import com.wotingfm.ui.user.login.view.LoginFragment;
import com.wotingfm.ui.user.logo.LogoActivity;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.List;

/**
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
            send(userName, password);
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
        model.loadNews(userName, password, new LoginModel.OnLoadInterface() {
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
        try {
            String s = new Gson().toJson(o);
            JSONObject js = new JSONObject(s);
            int ret = js.getInt("ret");
            Log.e("ret", String.valueOf(ret));
            if (ret == 0) {
                String msg = js.getString("data");
                JSONTokener jsonParser = new JSONTokener(msg);
                JSONObject arg1 = (JSONObject) jsonParser.nextValue();
                String token = arg1.getString("token");

                JSONObject ui = (JSONObject) new JSONTokener(arg1.getString("user")).nextValue();
                if (ui != null) {
                    // 保存用户数据
                    model.saveUserInfo(ui);
                }
                ToastUtils.show_always(activity.getActivity(), token);
                ToastUtils.show_always(activity.getActivity(), "登录成功");
                LogoActivity.closeActivity();
            } else {
                String msg = js.getString("msg");
                ToastUtils.show_always(activity.getActivity(), msg);
                ToastUtils.show_always(activity.getActivity(), "登录失败，请稍后再试");
            }
        } catch (Exception e) {
            e.printStackTrace();
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
