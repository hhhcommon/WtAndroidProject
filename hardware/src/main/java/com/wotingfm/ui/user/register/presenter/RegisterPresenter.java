package com.wotingfm.ui.user.register.presenter;

import android.content.Intent;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.woting.commonplat.config.GlobalNetWorkConfig;
import com.woting.commonplat.nim.DemoCache;
import com.woting.commonplat.nim.im.ui.dialog.DialogMaker;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.live.preference.Preferences;
import com.wotingfm.common.live.preference.UserPreferences;
import com.wotingfm.common.constant.BroadcastConstants;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.common.utils.ToastUtils;
import com.wotingfm.ui.user.information.view.InformationFragment;
import com.wotingfm.ui.user.logo.LogoActivity;
import com.wotingfm.ui.user.register.model.RegisterModel;
import com.wotingfm.ui.user.register.view.RegisterFragment;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * 注册处理器
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class RegisterPresenter {

    private RegisterFragment activity;
    private RegisterModel model;
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
        if (userName != null &&! userName.trim().equals("")&&userName.trim().length()==11) {

        }else{
            Toast.makeText(activity.getActivity(), "手机号格式不正确", Toast.LENGTH_LONG).show();
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

    /**
     * register
     *
     * @param userName 用户名
     * @param password 密码
     * @return true:可以登录 / false：不能登录
     */
    public void register(String userName, String password, String yzm) {
        if (checkData(userName, password, yzm)) {
            if (GlobalNetWorkConfig.CURRENT_NETWORK_STATE_TYPE != -1) {
                send(userName, password, yzm);
            } else {
                ToastUtils.show_always(activity.getActivity(), "网络连接失败，请稍后再试！");
            }
        }
    }

    /**
     * 获取验证码
     */
    public void getYzm(String userName) {
        if (userName != null && !userName.equals("")&&userName.trim().length()==11) {
            if (GlobalNetWorkConfig.CURRENT_NETWORK_STATE_TYPE != -1) {
                if (mCountDownTimer == null) {
                    timerDown();
                    getYzmData(userName);
                }
            } else {
                ToastUtils.show_always(activity.getActivity(), "网络连接失败，请稍后再试！");
            }
        } else {
            ToastUtils.show_always(activity.getActivity(), "手机号格式不正确");
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
        boolean bt = model.getBtViewType(userName, password, yzm);
        activity.setRegisterBackground(bt);
    }

    // 发送注册账号请求
    private void send(String userName, String password, String yzm) {
        activity.dialogShow();
        model.loadNews(userName, password, yzm, new RegisterModel.OnLoadInterface() {
            @Override
            public void onSuccess(Object o) {
                activity.dialogCancel();
                dealRegisterSuccess(o);
            }

            @Override
            public void onFailure(String msg) {
                activity.dialogCancel();
                ToastUtils.show_always(activity.getActivity(), "注册失败，请稍后再试");

            }
        });
    }

    // 处理注册返回数据
    private void dealRegisterSuccess(Object o) {
        try {
            String s = new GsonBuilder().serializeNulls().create().toJson(o);
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
                    loginYx(ui.optString("acc_id"), ui.optString("net_ease_token"));
                    ToastUtils.show_always(activity.getActivity(), "注册成功");
                }

            } else {
                String msg = js.getString("msg");
                if (msg != null && !msg.trim().equals("")) {
                    ToastUtils.show_always(activity.getActivity(), msg);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.show_always(activity.getActivity(), "注册失败，请稍后再试");
        }
    }

    private AbortableFuture<LoginInfo> loginRequest;

    private void onLoginDone() {
        loginRequest = null;
        DialogMaker.dismissProgressDialog();
    }

    private void saveLoginInfo(final String account, final String token) {
        Preferences.saveUserAccount(account);
        Preferences.saveUserToken(token);
    }

    private void loginYx(final String account, final String token) {
        // 登录
        loginRequest = NIMClient.getService(AuthService.class).login(new LoginInfo(account, token));
        loginRequest.setCallback(new RequestCallback<LoginInfo>() {
            @Override
            public void onSuccess(LoginInfo param) {

                onLoginDone();
                DemoCache.setAccount(account);
                saveLoginInfo(account, token);

                // 初始化消息提醒
                NIMClient.toggleNotification(UserPreferences.getNotificationToggle());

                // 初始化免打扰
                NIMClient.updateStatusBarNotificationConfig(UserPreferences.getStatusConfig());
                // 发送登录广播通知所有界面
                RetrofitUtils.INSTANCE = null;

                // 发送登录广播通知所有界面
                activity.getActivity().sendBroadcast(new Intent(BroadcastConstants.LOGIN));
                jump();// 跳转到信息完善界面

            }

            @Override
            public void onFailed(int code) {
                onLoginDone();
                if (code == 302 || code == 404) {
                    Toast.makeText(BSApplication.getInstance(), "登录失败", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(BSApplication.getInstance(), "登录失败: " + code, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onException(Throwable exception) {
                onLoginDone();
            }
        });
    }

    // 跳转到信息完善界面
    private void jump() {
        LogoActivity.open(new InformationFragment());
    }

    // 获取验证码的网络请求
    private void getYzmData(String userName) {
        activity.dialogShow();
        model.loadNewsForYzm(userName, new RegisterModel.OnLoadInterface() {
            @Override
            public void onSuccess(Object result) {
                activity.dialogCancel();
                dealGetYzmSuccess(result);
            }

            @Override
            public void onFailure(String msg) {
                activity.dialogCancel();
                ToastUtils.show_always(activity.getActivity(), "获取验证码失败，请稍后再试1");
            }
        });
    }

    // 处理户获取验证码返回数据
    private void dealGetYzmSuccess(Object o) {
        try {
            String s = new GsonBuilder().serializeNulls().create().toJson(o);
            JSONObject js = new JSONObject(s);
            int ret = js.getInt("ret");
            Log.e("ret", String.valueOf(ret));
            if (ret == 0) {
                String msg = js.getString("data");
                JSONTokener jsonParser = new JSONTokener(msg);
                JSONObject arg1 = (JSONObject) jsonParser.nextValue();
                int code = arg1.getInt("code");
                ToastUtils.show_always(activity.getActivity(), String.valueOf(code));
            } else {
                String msg = js.getString("msg");
                if (msg != null && !msg.trim().equals("")) {
                    Log.e("登录失败返回数据", msg);
                    ToastUtils.show_always(activity.getActivity(), msg);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.show_always(activity.getActivity(), "获取验证码失败，请稍后再试");
        }
    }

    /**
     * 关闭定时器
     */
    public void cancel() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
    }

    /**
     * 数据销毁
     */
    public void destroy() {
        model = null;
    }
}
