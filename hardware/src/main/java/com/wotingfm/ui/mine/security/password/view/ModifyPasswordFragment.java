package com.wotingfm.ui.mine.security.password.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import com.wotingfm.R;
import com.wotingfm.common.utils.DialogUtils;
import com.wotingfm.ui.base.basefragment.BaseFragment;
import com.wotingfm.ui.mine.main.MineActivity;
import com.wotingfm.ui.mine.security.password.presenter.ModifyPasswordPresenter;
import com.wotingfm.ui.user.logo.LogoActivity;

/**
 * 修改密码
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class ModifyPasswordFragment extends BaseFragment implements View.OnClickListener {
    private View rootView;
    private TextView textSendVerificationCode;// 发送验证码
    private EditText editPhoneNumber;// 手机号
    private EditText editVerificationCode;// 验证码
    private EditText editNewPassword;// 新密码
    private ModifyPasswordPresenter presenter;
    private Dialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_modif_password, container, false);
            rootView.setOnClickListener(this);

            initView();
            initEvent();
            presenter = new ModifyPasswordPresenter(this);
        }
        return rootView;
    }

    // 初始化视图
    private void initView() {
        TextView textTitle = (TextView) rootView.findViewById(R.id.tv_center);// 标题
        textTitle.setText(getString(R.string.password));

        editPhoneNumber = (EditText) rootView.findViewById(R.id.edit_phone_number);// 手机号
        editPhoneNumber.requestFocus();
        InputMethodManager imm = (InputMethodManager) editPhoneNumber.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
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

    // 监听
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left_btn:
                closeFragment();
                break;
            case R.id.text_send_verification_code:
                String userName = editPhoneNumber.getText().toString().trim();
                presenter.getYzm(userName);
                break;
            case R.id.text_confirm:
                confirm();
                break;

        }
    }

    // confirm
    private void confirm() {
        String news1 = editPhoneNumber.getText().toString().trim();
        String news2 = editNewPassword.getText().toString().trim();
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
        presenter.destroy();
        presenter=null;
    }
}
