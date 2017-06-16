package com.wotingfm.ui.user.retrievepassword.presenter;

import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Toast;

import com.wotingfm.common.utils.ToastUtils;
import com.wotingfm.ui.user.login.model.Login;
import com.wotingfm.ui.user.register.model.RegisterModel;
import com.wotingfm.ui.user.retrievepassword.model.RetrievePasswordModel;
import com.wotingfm.ui.user.retrievepassword.view.RetrievePassWordFragment;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class RetrievePasswordPresenter {

    private final RetrievePassWordFragment activity;
    private final RetrievePasswordModel model;
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
   private void getYzmData(String userName){
       model.loadNewsForYzm(userName, new RetrievePasswordModel.OnLoadInterface() {
           @Override
           public void onSuccess(Object o) {
//                loginView.removeDialog();
               dealGetYzmSuccess(o);
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
     * confirm
     *
     * @param userName 用户名
     * @param password 密码
     * @return true:可以登录 / false：不能登录
     */
    public void confirm(String userName, String password, String yzm) {
        if (checkData(userName, password, yzm)) {
            send(userName, password, yzm);
        }
    }

    /**
     * 获取验证码
     */
    public void getYzm(String userName) {
        if(userName!=null&&!userName.equals("")){
            if (mCountDownTimer == null) {
                timerDown();
                getYzmData(userName);
            }
        }else{
            ToastUtils.show_always(activity.getActivity(),"手机号不能为空");
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
     * @param yzm 验证码
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
        activity.setConfirmBackground(bt);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // 发送网络请求
    private void send(String userName, String password, String yzm) {
        model.loadNews(userName, password, yzm, new RetrievePasswordModel.OnLoadInterface() {
            @Override
            public void onSuccess(Object result) {
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
    private void dealLoginSuccess(Object o) {
        try {
            JSONObject js=new JSONObject(o.toString());
            String ret= js.getString("ret");
            Log.e("ret",ret.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 处理返回数据
    private void dealGetYzmSuccess(Object o) {
        try {
            JSONObject js=new JSONObject(o.toString());
            String ret= js.getString("ret");
            Log.e("ret",ret.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
