package com.wotingfm.ui.user.register;

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
public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_yzm, tv_confirm;
    private ImageView left, img_eye;
    private EditText et_phoneNumber, et_passWord,et_yzm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        inItView();
        inItListener();
    }

    // 设置界面
    private void inItView() {
        img_eye = (ImageView) findViewById(R.id.img_eye);                 // 密码显示
        left = (ImageView) findViewById(R.id.head_left_btn);              // 删除按钮

        et_phoneNumber = (EditText) findViewById(R.id.et_phoneNumber);    // 手机号输入框
        et_yzm = (EditText) findViewById(R.id.et_yzm);                    // 验证码输入框
        et_passWord = (EditText) findViewById(R.id.et_passWord);          // 密码输入框

        tv_yzm= (TextView) findViewById(R.id.tv_yzm);                     // 获取验证码
        tv_confirm = (TextView) findViewById(R.id.tv_confirm);            // 注册
    }

    // 设置监听
    private void inItListener() {
        img_eye.setOnClickListener(this);
        left.setOnClickListener(this);
        tv_yzm.setOnClickListener(this);
        tv_confirm.setOnClickListener(this);
    }

    // 监听
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left_btn:
                finish();
                break;
            case R.id.tv_yzm:
                break;
            case R.id.img_eye:
                break;
            case R.id.tv_confirm:
                break;

        }
    }
}
