package com.wotingfm.ui.mine.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.wotingfm.R;
import com.wotingfm.ui.mine.main.MineActivity;

/**
 * 修改密码
 * Created by Administrator on 2017/6/16.
 */
public class ModifyPasswordFragment extends Fragment implements View.OnClickListener {
    private View rootView;
    private TextView textSendVerificationCode;// 发送验证码

    private EditText editPhoneNumber;// 手机号
    private EditText editVerificationCode;// 验证码
    private EditText editNewPassword;// 新密码

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_modif_password, container, false);
            rootView.setOnClickListener(this);

            initView();
            initEvent();
        }
        return rootView;
    }

    // 初始化视图
    private void initView() {
        TextView textTitle = (TextView) rootView.findViewById(R.id.tv_center);// 标题
        textTitle.setText(getString(R.string.password));

        editPhoneNumber = (EditText) rootView.findViewById(R.id.edit_phone_number);// 手机号
        editVerificationCode = (EditText) rootView.findViewById(R.id.edit_verification_code);// 验证码
        editNewPassword = (EditText) rootView.findViewById(R.id.edit_new_password);// 新密码

        textSendVerificationCode = (TextView) rootView.findViewById(R.id.text_send_verification_code);// 发送验证码
    }

    // 初始化点击事件
    private void initEvent() {
        rootView.findViewById(R.id.head_left_btn).setOnClickListener(this);// 返回
        rootView.findViewById(R.id.text_confirm).setOnClickListener(this);// 确定

        textSendVerificationCode.setOnClickListener(this);// 发送验证码
    }

    // 验证手机号、验证码的正确性
    private void verification() {
        // 手机号
        String oldPhone = editPhoneNumber.getText().toString();
        if (oldPhone.equals("")) {
            Log.w("TAG", "没有输入手机号");
            return ;
        }

        // 验证码
        String verificationCode = editVerificationCode.getText().toString();
        if (verificationCode.equals("")) {
            Log.w("TAG", "没有输入验证码");
            return ;
        }

        // 新密码
        String newPhone = editNewPassword.getText().toString();
        if (newPhone.equals("")) {
            Log.w("TAG", "没有输入新密码");
            return ;
        }

        Log.i("TAG", "验证通过");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left_btn:// 返回
                MineActivity.close();
                break;
            case R.id.text_confirm:// 确定
                verification();
                break;
            case R.id.text_send_verification_code:// 发送验证码

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
