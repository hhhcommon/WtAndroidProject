package com.wotingfm.ui.user.retrievepassword;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wotingfm.R;
import com.wotingfm.ui.base.baseactivity.BaseActivity;

/**
 * 作者：xinLong on 2017/6/4 19:45
 * 邮箱：645700751@qq.com
 */
public class RetrievePassWordActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_confirm, tv_yzm;
    private ImageView left, img_eye;
    private EditText et_phoneNumber, et_newPassWord,et_yzm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve_password);
        inItView();
        inItListener();
    }

    // 设置界面
    private void inItView() {
        img_eye = (ImageView) findViewById(R.id.img_eye);                // 密码显示
        left = (ImageView) findViewById(R.id.head_left_btn);             // 返回按钮

        et_phoneNumber = (EditText) findViewById(R.id.et_phoneNumber);   // 手机号输入框
        et_yzm = (EditText) findViewById(R.id.et_yzm);                   // 验证码输入框
        et_newPassWord = (EditText) findViewById(R.id.et_newPassWord);   // 新密码输入框

        tv_yzm = (TextView) findViewById(R.id.tv_yzm);                   // 获取验证码
        tv_confirm = (TextView) findViewById(R.id.tv_confirm);           // 确定
    }

    // 设置监听
    private void inItListener() {
        img_eye.setOnClickListener(this);
        left.setOnClickListener(this);
        tv_confirm.setOnClickListener(this);
        tv_yzm.setOnClickListener(this);
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
            case R.id.tv_yzm:
                break;
            case R.id.tv_confirm:
                break;

        }
    }
}
