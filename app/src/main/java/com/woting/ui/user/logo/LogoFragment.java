package com.woting.ui.user.logo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.woting.R;
import com.woting.ui.user.login.view.LoginFragment;
import com.woting.ui.user.register.view.RegisterFragment;

/**
 * 用户主页
 * 作者：xinLong on 2017/6/4 19:48
 * 邮箱：645700751@qq.com
 */
public class LogoFragment extends Fragment implements View.OnClickListener {

    private ImageView img_close;
    private TextView tv_register, tv_login;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.activity_logo, container, false);
            rootView.setOnClickListener(this);
            inItView();
            inItListener();
        }
        return rootView;
    }

    // 设置界面
    private void inItView() {
        img_close = (ImageView) rootView.findViewById(R.id.img_close);  // 关闭
        tv_register = (TextView) rootView.findViewById(R.id.tv_register);  // 注册
        tv_login = (TextView) rootView.findViewById(R.id.tv_login);        // 登录
    }

    // 设置监听
    private void inItListener() {
        img_close.setOnClickListener(this);
        tv_register.setOnClickListener(this);
        tv_login.setOnClickListener(this);
    }

    // 监听
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_close:
                LogoActivity.closeActivity();
                break;
            case R.id.tv_register:
                LogoActivity.open(new RegisterFragment());
                break;
            case R.id.tv_login:
                LogoActivity.open(new LoginFragment());
                break;
        }
    }

}
