package com.wotingfm.ui.mine.security.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wotingfm.R;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.constant.StringConstant;
import com.wotingfm.ui.mine.security.password.view.ModifyPasswordFragment;
import com.wotingfm.ui.mine.security.phonenumber.view.ModifyPhoneNumberFragment;
import com.wotingfm.ui.mine.main.MineActivity;

/**
 * 账户安全
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class AccountSecurityFragment extends Fragment implements View.OnClickListener {
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_account_security, container, false);
            rootView.setOnClickListener(this);
            initView();
            initEvent();
        }
        return rootView;
    }

    // 初始化视图
    private void initView() {
        TextView textTitle = (TextView) rootView.findViewById(R.id.tv_center);// 标题
        textTitle.setText(getString(R.string.account_security));
        TextView text_phone_number = (TextView) rootView.findViewById(R.id.text_phone_number);// 手机号
        TextView text_password = (TextView) rootView.findViewById(R.id.text_password);        // 密码
        String num = BSApplication.SharedPreferences.getString(StringConstant.USER_PHONE_NUMBER, "******");
        String bb =num.substring(3,7);
        //字符串替换
        String cc = num.replace(bb,"****");
        text_phone_number.setText(cc);

    }

    // 初始化点击事件
    private void initEvent() {
        rootView.findViewById(R.id.head_left_btn).setOnClickListener(this);    // 返回
        rootView.findViewById(R.id.view_phone_number).setOnClickListener(this);// 修改手机号
        rootView.findViewById(R.id.view_password).setOnClickListener(this);    // 修改密码
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left_btn:// 返回
                MineActivity.close();
                break;
            case R.id.view_phone_number:// 修改手机号
                MineActivity.open(new ModifyPhoneNumberFragment());
                break;
            case R.id.view_password:// 修改密码
                MineActivity.open(new ModifyPasswordFragment());
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (rootView != null) {
            ((ViewGroup) rootView.getParent()).removeView(rootView);
        }
    }


}
