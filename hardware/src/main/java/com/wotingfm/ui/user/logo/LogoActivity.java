package com.wotingfm.ui.user.logo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.wotingfm.R;
import com.wotingfm.ui.base.baseactivity.BaseActivity;
import com.wotingfm.ui.user.login.view.LoginActivity;
import com.wotingfm.ui.user.register.view.RegisterActivity;

/**
 * 作者：xinLong on 2017/6/4 19:48
 * 邮箱：645700751@qq.com
 */
public class LogoActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rl_close;
    private TextView tv_register, tv_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);
        inItView();
        inItListener();
    }

    // 设置界面
    private void inItView() {
        rl_close = (RelativeLayout) findViewById(R.id.rl_close);  // 关闭
        tv_register = (TextView) findViewById(R.id.tv_register);  // 注册
        tv_login = (TextView) findViewById(R.id.tv_login);        // 登录
    }

    // 设置监听
    private void inItListener() {
        rl_close.setOnClickListener(this);
        tv_register.setOnClickListener(this);
        tv_login.setOnClickListener(this);
    }

    // 监听
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_close:
                finish();
                break;
            case R.id.tv_register:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.tv_login:
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }
    }

}
