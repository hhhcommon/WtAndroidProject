package com.wotingfm.ui.user.login.presenter;

import android.widget.Toast;
import com.wotingfm.ui.user.login.model.Login;
import com.wotingfm.ui.user.login.model.LoginModel;
import com.wotingfm.ui.user.login.view.LoginFragment;

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
    private void send(String userName,String password) {
        model.loadNews(userName, password,  new LoginModel.OnLoadInterface  () {
            @Override
            public void onSuccess(Login result) {
//                loginView.removeDialog();
                dealLoginSuccess(result);
            }

            @Override
            public void onFailure(String msg) {
//                loginView.removeDialog();
//                ToastUtils.showVolleyError(loginView);
            }
        });
    }


    // 处理返回数据
    private void dealLoginSuccess(Login result) {
//        try {
//            String ReturnType = result.getString("ReturnType");
//            if (ReturnType != null && ReturnType.equals("1001")) {
//                try {
//                    JSONObject ui = (JSONObject) new JSONTokener(result.getString("UserInfo")).nextValue();
//                    if (ui != null) {
//                        // 保存用户数据
//                        model.saveUserInfo(ui);
//                        // 关闭当前界面
//                        activity.close();
//                    } else {
//                        ToastUtils.show_always(activity.getActivity(), "登录失败，请您稍后再试");
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    ToastUtils.show_always(activity.getActivity(), "登录失败，请您稍后再试");
//                }
//
//            }
//        } catch (Exception e1) {
//            e1.printStackTrace();
//        }
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
