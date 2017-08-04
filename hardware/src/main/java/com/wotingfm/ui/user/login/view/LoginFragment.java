package com.wotingfm.ui.user.login.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.wotingfm.R;
import com.wotingfm.common.utils.DialogUtils;
import com.wotingfm.ui.user.login.presenter.LoginPresenter;
import com.wotingfm.ui.user.logo.LogoActivity;
import com.wotingfm.ui.user.retrievepassword.view.RetrievePassWordFragment;

/**
 * 登录界面
 * 作者：xinLong on 2017/6/4 19:45
 * 邮箱：645700751@qq.com
 */
public class LoginFragment extends Fragment implements View.OnClickListener {
    private TextView tv_forgetPassWord, tv_login;
    private ImageView left, img_eye;
    private EditText et_phoneNumber, et_passWord;
    private LoginPresenter loginPresenter;
    private View rootView;
    private Dialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_login, container, false);
            rootView.setOnClickListener(this);
            inItView();
            inItListener();
            setEditListener();
            loginPresenter = new LoginPresenter(this);
            loginPresenter.setEye();
        }
        return rootView;
    }

    private void inItView() {
        img_eye = (ImageView) rootView.findViewById(R.id.img_eye);                         // 显示密码
        left = (ImageView) rootView.findViewById(R.id.head_left_btn);                      // 返回按钮
        TextView tv_center = (TextView)rootView. findViewById(R.id.tv_center);             // 标头
        tv_center.setText("登录");

        et_phoneNumber = (EditText)rootView. findViewById(R.id.et_phoneNumber);            // 手机号输入框
        et_phoneNumber.requestFocus();
        InputMethodManager imm = (InputMethodManager) et_phoneNumber.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
        et_passWord = (EditText) rootView.findViewById(R.id.et_passWord);                  // 密码输入框

        tv_login = (TextView)rootView. findViewById(R.id.tv_login);                        // 登录
        tv_forgetPassWord = (TextView) rootView.findViewById(R.id.tv_forgetPassWord);      // 忘记密码
    }

    // 设置监听
    private void inItListener() {
        img_eye.setOnClickListener(this);
        left.setOnClickListener(this);
        tv_forgetPassWord.setOnClickListener(this);
        tv_login.setOnClickListener(this);
    }

    // 监听
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left_btn:
                close();
                break;
            case R.id.img_eye:
                loginPresenter.setEye();
                break;
            case R.id.tv_login:
                login();
                break;
            case R.id.tv_forgetPassWord:
                LogoActivity.open(new RetrievePassWordFragment());
                break;

        }
    }

    /**
     * 关闭当前界面
     */
    public void close(){
        LogoActivity.close();
    }

    // 输入框的监听
    private void setEditListener() {
        // 用户名的监听
        et_phoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String userName = et_phoneNumber.getText().toString().trim();
                String password = et_passWord.getText().toString().trim();
                loginPresenter.getBtView(userName, password);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //  密码的监听
        et_passWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String userName = et_phoneNumber.getText().toString().trim();
                String password = et_passWord.getText().toString().trim();
                loginPresenter.getBtView(userName, password);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    // 登录
    private void login() {
        String userName = et_phoneNumber.getText().toString().trim();
        String password = et_passWord.getText().toString().trim();
        loginPresenter.login(userName, password);
    }

    /**
     * 设置密码显示的样式
     *
     * @param type true显示明文状态，false显示密码状态
     */
    public void setEyeShow(boolean type) {
        if (type) {
            img_eye.setImageResource(R.mipmap.login_icon_password_look);
            et_passWord.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            img_eye.setImageResource(R.mipmap.login_icon_password_unlook);
            et_passWord.setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
    }

    /**
     * 设置登录按钮的样式
     *
     * @param type true输入完成状态，false未输入完成状态
     */
    public void setLoginBackground(boolean type) {
        if (type) {
            tv_login.setBackgroundResource(R.drawable.background_login_tvlogin_on);
        } else {
            tv_login.setBackgroundResource(R.drawable.background_login_tvlogin_off);
        }
    }

    /**
     * 展示弹出框
     */
    public void dialogShow() {
        dialog = DialogUtils.Dialog(this.getActivity());
    }

    /**
     * 取消弹出框
     */
    public void dialogCancel() {
        if (dialog != null) dialog.dismiss();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        loginPresenter.destroy();
        loginPresenter=null;
    }
}
