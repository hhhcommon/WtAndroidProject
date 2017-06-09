package com.wotingfm.ui.user.register.presenter;

import android.os.CountDownTimer;
import android.widget.Toast;

import com.wotingfm.ui.user.login.model.Login;
import com.wotingfm.ui.user.login.model.LoginModel;
import com.wotingfm.ui.user.register.model.RegisterModel;
import com.wotingfm.ui.user.register.view.RegisterFragment;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class RegisterPresenter {

    private final RegisterFragment activity;
    private final RegisterModel model;
    private boolean eyeShow = false;
    private CountDownTimer mCountDownTimer;


    public RegisterPresenter(RegisterFragment activity) {
        this.activity = activity;
        this.model = new RegisterModel();
    }

    // 再次获取验证码时间
    private void timerDown() {
        mCountDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String timeString = millisUntilFinished / 1000 + "s";
                activity.setYzmStyle(true, timeString);
            }

            @Override
            public void onFinish() {
                mCountDownTimer.cancel();
                mCountDownTimer = null;
                String timeString = "获取验证码";
                activity.setYzmStyle(false, timeString);
            }
        }.start();
    }

    // 检查数据的正确性  检查通过则进行登录
    private boolean checkData(String userName, String password, String yzm) {
        if (userName == null || userName.trim().equals("")) {
            Toast.makeText(activity.getActivity(), "登录账号不能为空", Toast.LENGTH_LONG).show();
            return false;
        }
        if (yzm == null || yzm.trim().equals("")) {
            Toast.makeText(activity.getActivity(), "验证码不能为空", Toast.LENGTH_LONG).show();
            return false;
        }
        if (password == null || password.trim().equals("")) {
            Toast.makeText(activity.getActivity(), "密码不能为空", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    // 获取网络数据
    private void getYzmData(String userName) {
        model.loadNewsForYzm(userName, new RegisterModel.OnLoadInterface() {
            @Override
            public void onSuccess(Login result) {
//                loginView.removeDialog();
                dealGetYzmSuccess(result);
            }

            @Override
            public void onFailure(String msg) {
//                loginView.removeDialog();
//                ToastUtils.showVolleyError(loginView);
            }
        });
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * register
     *
     * @param userName 用户名
     * @param password 密码
     * @return true:可以登录 / false：不能登录
     */
    public void register(String userName, String password, String yzm) {
        if (checkData(userName, password, yzm)) {
            send(userName, password, yzm);
        }
    }

    /**
     * 获取验证码
     */
    public void getYzm(String userName) {
        if (mCountDownTimer == null) {
            timerDown();
            getYzmData(userName);
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
     * @param yzm      验证码
     */
    public void getBtView(String userName, String password, String yzm) {
        boolean bt;
        if (userName != null && !userName.trim().equals("")) {
            if (yzm != null && !yzm.trim().equals("")) {
                if (password != null && !password.trim().equals("") && password.length() > 5) {
                    bt = true;
                } else {
                    bt = false;
                }
            } else {
                bt = false;
            }
        } else {
            bt = false;
        }
        activity.setRegisterBackground(bt);
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////
    // 发送网络请求
    private void send(String userName, String password, String yzm) {
        model.loadNews(userName, password, yzm, new RegisterModel.OnLoadInterface() {
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


    // 处理返回数据
    private void dealGetYzmSuccess(Login result) {
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
}
