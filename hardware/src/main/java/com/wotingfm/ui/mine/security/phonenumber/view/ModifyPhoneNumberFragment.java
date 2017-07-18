package com.wotingfm.ui.mine.security.phonenumber.view;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wotingfm.R;
import com.wotingfm.common.utils.DialogUtils;
import com.wotingfm.ui.mine.main.MineActivity;
import com.wotingfm.ui.mine.security.phonenumber.presenter.ModifyPhoneNumberPresenter;
import com.wotingfm.ui.user.logo.LogoActivity;
import com.wotingfm.ui.user.retrievepassword.presenter.RetrievePasswordPresenter;

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
    private ModifyPhoneNumberPresenter presenter;
    private Dialog dialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_modif_phone_number, container, false);
            inItView();
            inItListener();
            presenter = new ModifyPhoneNumberPresenter(this);
        }
        return rootView;
    }

    // 设置界面
    private void inItView() {
        TextView textTitle = (TextView) rootView.findViewById(R.id.tv_center);// 标题
        textTitle.setText(getString(R.string.phone_number));
        editOldPhone = (EditText) rootView.findViewById(R.id.edit_old_phone);// 旧手机号
        editVerificationCode = (EditText) rootView.findViewById(R.id.edit_verification_code);// 验证码
        editNewPhone = (EditText) rootView.findViewById(R.id.edit_new_phone);// 新手机号
        textSendVerificationCode = (TextView) rootView.findViewById(R.id.text_send_verification_code);// 发送验证码
    }

    // 设置监听
    private void inItListener() {
        rootView.findViewById(R.id.head_left_btn).setOnClickListener(this);// 返回
        rootView.findViewById(R.id.text_confirm).setOnClickListener(this);// 确定
        textSendVerificationCode.setOnClickListener(this);// 发送验证码
    }

    // 监听
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left_btn:
                MineActivity.close();
                break;
            case R.id.text_send_verification_code:
                String userName = editOldPhone.getText().toString().trim();
                presenter.getYzm(userName);
                break;
            case R.id.text_confirm:
                confirm();
                break;

        }
    }

    // confirm
    private void confirm() {
        String news1 = editOldPhone.getText().toString().trim();
        String news2 = editNewPhone.getText().toString().trim();
        String yzm = editVerificationCode.getText().toString().trim();
        presenter.confirm(news1, news2, yzm);
    }


    /**
     * 设置获取验证码的样式
     *
     * @param type       完成状态
     * @param timeString 文字展示内容
     */
    public void setYzmStyle(boolean type, String timeString) {
        if (type) {
            textSendVerificationCode.setText(timeString);
            textSendVerificationCode.setTextColor(this.getResources().getColor(R.color.gray_edit_hint_word));
        } else {
            textSendVerificationCode.setText(timeString);
            textSendVerificationCode.setTextColor(this.getResources().getColor(R.color.app_basic));
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
        presenter.cancel();
    }

}

