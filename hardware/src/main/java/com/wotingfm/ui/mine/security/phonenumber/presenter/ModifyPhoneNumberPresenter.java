package com.wotingfm.ui.mine.security.phonenumber.presenter;

import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.woting.commonplat.config.GlobalNetWorkConfig;
import com.wotingfm.common.utils.ToastUtils;
import com.wotingfm.ui.mine.main.MineActivity;
import com.wotingfm.ui.mine.security.phonenumber.model.ModifyPhoneNumberModel;
import com.wotingfm.ui.mine.security.phonenumber.view.ModifyPhoneNumberFragment;
import com.wotingfm.ui.user.logo.LogoActivity;
import com.wotingfm.ui.user.retrievepassword.model.RetrievePasswordModel;
import com.wotingfm.ui.user.retrievepassword.view.RetrievePassWordFragment;

import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class ModifyPhoneNumberPresenter {

    private final ModifyPhoneNumberFragment activity;
    private final ModifyPhoneNumberModel model;
    private CountDownTimer mCountDownTimer;


    public ModifyPhoneNumberPresenter(ModifyPhoneNumberFragment activity) {
        this.activity = activity;
        this.model = new ModifyPhoneNumberModel();
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

    /**
     * confirm
     *
     * @param news1
     * @param news2
     * @return true:可以 / false：不能
     */
    public void confirm(String news1, String news2, String yzm) {
        if (checkData(news1, news2, yzm)) {
            if (GlobalNetWorkConfig.CURRENT_NETWORK_STATE_TYPE != -1) {
                send(news1, news2, yzm);
            } else {
                ToastUtils.show_always(activity.getActivity(), "网络连接失败，请稍后再试！");
            }

        }
    }

    // 检查数据的正确性  检查通过则进行登录
    private boolean checkData(String news1, String news2, String yzm) {
        if (news1 == null || news1.trim().equals("")) {
            Toast.makeText(activity.getActivity(), "旧手机号码不能为空", Toast.LENGTH_LONG).show();
            return false;
        }
        if (yzm == null || yzm.trim().equals("")) {
            Toast.makeText(activity.getActivity(), "验证码不能为空", Toast.LENGTH_LONG).show();
            return false;
        }
        if (news2 == null || news2.trim().equals("")) {
            Toast.makeText(activity.getActivity(), "新手机号码不能为空", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }


    /**
     * 获取验证码
     */
    public void getYzm(String userName) {
        if(userName!=null&&!userName.equals("")){
            if (GlobalNetWorkConfig.CURRENT_NETWORK_STATE_TYPE != -1) {
                if (mCountDownTimer == null) {
                    timerDown();
                    getYzmData(userName);
                }
            } else {
                ToastUtils.show_always(activity.getActivity(), "网络连接失败，请稍后再试！");
            }
        }else{
            ToastUtils.show_always(activity.getActivity(),"手机号不能为空");
        }
    }

    // 发送修改手机号请求
    private void send(String news1, final String news2, String yzm) {
        model.loadNews(news1, news2, yzm, new ModifyPhoneNumberModel.OnLoadInterface() {
            @Override
            public void onSuccess(Object result) {
                activity.dialogCancel();
                dealSuccess(result,news2);
            }

            @Override
            public void onFailure(String msg) {
                activity.dialogCancel();
                ToastUtils.show_always(activity.getActivity(), "手机号修改失败，请稍后再试");
            }
        });
    }

    // 处理修改手机号返回数据
    private void dealSuccess(Object o,String num) {
        try {
            String s = new Gson().toJson(o);
            JSONObject js = new JSONObject(s);
            int ret = js.getInt("ret");
            Log.e("ret", String.valueOf(ret));
            if (ret == 0) {
                // 保存修改后的手机号到本地，进行同步
                model.savePhoneNumber(num);
                MineActivity.close();
                ToastUtils.show_always(activity.getActivity(), "手机号修改成功");
            } else {
                String msg = js.getString("msg");
                ToastUtils.show_always(activity.getActivity(), msg);
                ToastUtils.show_always(activity.getActivity(), "手机号修改失败，请稍后再试");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.show_always(activity.getActivity(), "手机号修改失败，请稍后再试");
        }
    }


    // 获取验证码的网络请求
    private void getYzmData(String userName) {
        activity.dialogShow();
        model.loadNewsForYzm(userName, new ModifyPhoneNumberModel.OnLoadInterface() {
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
            String s = new Gson().toJson(o);
            JSONObject js = new JSONObject(s);
            int ret = js.getInt("ret");
            Log.e("ret", String.valueOf(ret));
            if (ret == 0) {
                String msg = js.getString("data");
                JSONTokener jsonParser = new JSONTokener(msg);
                JSONObject arg1 = (JSONObject) jsonParser.nextValue();
                int code = arg1.getInt("code");
                ToastUtils.show_always(activity.getActivity(), String.valueOf(code));
                ToastUtils.show_always(activity.getActivity(), "获取验证码成功");
            } else {
                String msg = js.getString("msg");
                ToastUtils.show_always(activity.getActivity(), msg);
                ToastUtils.show_always(activity.getActivity(), "获取验证码失败，请稍后再试");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.show_always(activity.getActivity(), "获取验证码失败，请稍后再试");
        }
    }

    public void cancel() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
    }
}
