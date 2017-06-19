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
import com.wotingfm.ui.mine.MineActivity;

/**
 * 修改手机号
 * Created by Administrator on 2017/6/16.
 */
public class ModifyPhoneNumberFragment extends Fragment implements View.OnClickListener {
    private View rootView;
    private TextView textSendVerificationCode;

    private EditText editOldPhone;
    private EditText editVerificationCode;
    private EditText editNewPhone;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_modif_phone_number, container, false);
            rootView.setOnClickListener(this);

            initView();
            initEvent();
        }
        return rootView;
    }

    // 初始化视图
    private void initView() {
        TextView textTitle = (TextView) rootView.findViewById(R.id.tv_center);// 标题
        textTitle.setText(getString(R.string.phone_number));

        editOldPhone = (EditText) rootView.findViewById(R.id.edit_old_phone);// 旧手机号
        editVerificationCode = (EditText) rootView.findViewById(R.id.edit_verification_code);// 验证码
        editNewPhone = (EditText) rootView.findViewById(R.id.edit_new_phone);// 新手机号

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
        String oldPhone = editOldPhone.getText().toString();
        if (oldPhone.equals("")) {
            Log.w("TAG", "没有输入旧手机号");
            return ;
        }

        String verificationCode = editVerificationCode.getText().toString();
        if (verificationCode.equals("")) {
            Log.w("TAG", "没有输入验证码");
            return ;
        }

        String newPhone = editNewPhone.getText().toString();
        if (newPhone.equals("")) {
            Log.w("TAG", "没有输入新手机号");
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
