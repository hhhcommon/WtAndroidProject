package com.woting.ui.user.retrievepassword.presenter;

import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.woting.commonplat.config.GlobalNetWorkConfig;
import com.woting.common.utils.ToastUtils;
import com.woting.ui.user.logo.LogoActivity;
import com.woting.ui.user.retrievepassword.model.RetrievePasswordModel;
import com.woting.ui.user.retrievepassword.view.RetrievePassWordFragment;

import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class RetrievePasswordPresenter {

    private RetrievePassWordFragment activity;
    private RetrievePasswordModel model;
    private boolean eyeShow = false;
    private CountDownTimer mCountDownTimer;


    public RetrievePasswordPresenter(RetrievePassWordFragment activity) {
        this.activity = activity;
        this.model = new RetrievePasswordModel();
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
        activity.setConfirmBackground(bt);
    }

    /**
     * confirm
     *
     * @param userName 用户名
     * @param password 密码
     * @return true:可以登录 / false：不能登录
     */
    public void confirm(String userName, String password, String yzm) {
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

    // 发送忘记密码请求
    private void send(String userName, String password, String yzm) {
        model.loadNews(userName, password, yzm, new RetrievePasswordModel.OnLoadInterface() {
            @Override
            public void onSuccess(Object result) {
                activity.dialogCancel();
                dealSuccess(result);
            }

            @Override
            public void onFailure(String msg) {
                activity.dialogCancel();
                ToastUtils.show_always(activity.getActivity(), "修改密码失败，请稍后再试");
            }
        });
    }

    // 处理忘记密码返回数据
    private void dealSuccess(Object o) {
        try {
            String s = new GsonBuilder().serializeNulls().create().toJson(o);
            JSONObject js = new JSONObject(s);
            int ret = js.getInt("ret");
            Log.e("ret", String.valueOf(ret));
            if (ret == 0) {
                LogoActivity.close();
                ToastUtils.show_always(activity.getActivity(), "修改密码成功");
            } else {
                String msg = js.getString("msg");
                if (msg != null && !msg.trim().equals("")) {
                    Log.e("修改密码失败返回数据", msg);
                    ToastUtils.show_always(activity.getActivity(), msg);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.show_always(activity.getActivity(), "修改密码失败，请稍后再试");
        }
    }


    // 获取验证码的网络请求
    private void getYzmData(String userName) {
        activity.dialogShow();
        model.loadNewsForYzm(userName, new RetrievePasswordModel.OnLoadInterface() {
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
                    Log.e("获取验证码失败返回数据", msg);
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
