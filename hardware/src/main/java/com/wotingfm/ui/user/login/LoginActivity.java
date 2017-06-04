package com.wotingfm.ui.user.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.wotingfm.R;
import com.wotingfm.ui.base.baseactivity.BaseActivity;
import com.wotingfm.ui.main.view.MainActivity;
import com.wotingfm.ui.user.register.RegisterActivity;

/**
 * 作者：xinLong on 2017/6/4 19:45
 * 邮箱：645700751@qq.com
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_forgetPassWord, tv_login;
    private ImageView left, img_eye;
    private EditText et_phoneNumber, et_passWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        inItView();
        inItListener();
    }

    private void inItView() {
        img_eye = (ImageView) findViewById(R.id.img_eye);                         // 显示密码
        left = (ImageView) findViewById(R.id.head_left_btn);                      // 返回按钮

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
                break;
            case R.id.tv_login:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.tv_forgetPassWord:
                startActivity(new Intent(this, RegisterActivity.class));
                break;

        }
    }
}
