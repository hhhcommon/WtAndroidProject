package com.wotingfm.ui.user.login.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wotingfm.R;
import com.wotingfm.ui.base.baseactivity.BaseActivity;
import com.wotingfm.ui.user.login.presenter.LoginPresenter;
import com.wotingfm.ui.user.register.view.RegisterActivity;

/**
 * 作者：xinLong on 2017/6/4 19:45
 * 邮箱：645700751@qq.com
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_forgetPassWord, tv_login;
    private ImageView left, img_eye;
    private EditText et_phoneNumber, et_passWord;
    private LoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        inItView();
        inItListener();
        setEditListener();
        loginPresenter = new LoginPresenter(this);
        loginPresenter.setEye();
    }

    private void inItView() {
        img_eye = (ImageView) findViewById(R.id.img_eye);                         // 显示密码
        left = (ImageView) findViewById(R.id.head_left_btn);                      // 返回按钮
        TextView tv_center = (TextView) findViewById(R.id.tv_center);             // 标头
        tv_center.setText("登录");

        et_phoneNumber = (EditText) findViewById(R.id.et_phoneNumber);            // 手机号输入框
        et_passWord = (EditText) findViewById(R.id.et_passWord);                  // 密码输入框

        tv_login = (TextView) findViewById(R.id.tv_login);                        // 登录
        tv_forgetPassWord = (TextView) findViewById(R.id.tv_forgetPassWord);      // 忘记密码
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
                finish();
                break;
            case R.id.img_eye:
                loginPresenter.setEye();
                break;
            case R.id.tv_login:
                login();
                break;
            case R.id.tv_forgetPassWord:
                startActivity(new Intent(this, RegisterActivity.class));
                break;

        }
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
            et_passWord.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
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
}
