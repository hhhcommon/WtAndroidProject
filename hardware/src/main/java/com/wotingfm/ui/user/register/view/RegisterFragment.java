package com.wotingfm.ui.user.register.view;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wotingfm.R;
import com.wotingfm.common.utils.DialogUtils;
import com.wotingfm.ui.user.logo.LogoActivity;
import com.wotingfm.ui.user.register.presenter.RegisterPresenter;

/**
 * 注册界面
 * 作者：xinLong on 2017/6/4 19:45
 * 邮箱：645700751@qq.com
 */
public class RegisterFragment extends Fragment implements View.OnClickListener {
    private TextView tv_yzm, tv_confirm;
    private ImageView left, img_eye;
    private EditText et_phoneNumber, et_passWord, et_yzm;
    private RegisterPresenter registerPresenter;
    private View rootView;
    private Dialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_register, container, false);
            inItView();
            setEditListener();
            registerPresenter = new RegisterPresenter(this);
            registerPresenter.setEye();
        }
        return rootView;
    }

    // 设置界面
    private void inItView() {

        left = (ImageView) rootView.findViewById(R.id.head_left_btn);              // 删除按钮
        left.setOnClickListener(this);
        TextView tv_center = (TextView) rootView.findViewById(R.id.tv_center);     // 标头
        tv_center.setText("注册");

        img_eye = (ImageView) rootView.findViewById(R.id.img_eye);                 // 密码显示
        img_eye.setOnClickListener(this);

        et_phoneNumber = (EditText) rootView.findViewById(R.id.et_phoneNumber);    // 手机号输入框
        et_yzm = (EditText) rootView.findViewById(R.id.et_yzm);                    // 验证码输入框
        et_passWord = (EditText) rootView.findViewById(R.id.et_passWord);          // 密码输入框

        tv_yzm = (TextView) rootView.findViewById(R.id.tv_yzm);                    // 获取验证码
        tv_yzm.setOnClickListener(this);
        tv_confirm = (TextView) rootView.findViewById(R.id.tv_confirm);            // 注册
        tv_confirm.setOnClickListener(this);

    }

    // 监听
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left_btn:
                LogoActivity.close();
                break;
            case R.id.tv_yzm:
                String userName = et_phoneNumber.getText().toString().trim();
                registerPresenter.getYzm(userName);
                break;
            case R.id.img_eye:
                registerPresenter.setEye();
                break;
            case R.id.tv_confirm:
                register();
                break;
        }
    }

    // 输入框的监听
    private void setEditListener() {
        // 用户名的监听
        et_phoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String userName = et_phoneNumber.getText().toString().trim();
                String password = et_passWord.getText().toString().trim();
                String yzm = et_yzm.getText().toString().trim();
                registerPresenter.getBtView(userName, password, yzm);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //  密码的监听
        et_passWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String userName = et_phoneNumber.getText().toString().trim();
                String password = et_passWord.getText().toString().trim();
                String yzm = et_yzm.getText().toString().trim();
                registerPresenter.getBtView(userName, password, yzm);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //  验证码的监听
        et_yzm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String userName = et_phoneNumber.getText().toString().trim();
                String password = et_passWord.getText().toString().trim();
                String yzm = et_yzm.getText().toString().trim();
                registerPresenter.getBtView(userName, password, yzm);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    // 登录
    private void register() {
        String userName = et_phoneNumber.getText().toString().trim();
        String password = et_passWord.getText().toString().trim();
        String yzm = et_yzm.getText().toString().trim();
        registerPresenter.register(userName, password, yzm);
    }

    /**
     * 设置密码显示的样式
     *
     * @param type true显示明文状态，false显示密码状态
     */
    public void setEyeShow(boolean type) {
        if (type) {
            img_eye.setImageResource(R.mipmap.login_icon_password_look);
            et_passWord.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            img_eye.setImageResource(R.mipmap.login_icon_password_unlook);
            et_passWord.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
    }

    /**
     * 设置登录按钮的样式
     *
     * @param type true输入完成状态，false未输入完成状态
     */
    public void setRegisterBackground(boolean type) {
        if (type) {
            tv_confirm.setBackgroundResource(R.drawable.background_login_tvlogin_on);
        } else {
            tv_confirm.setBackgroundResource(R.drawable.background_login_tvlogin_off);
        }
    }

    /**
     * 设置获取验证码的样式
     *
     * @param type       完成状态
     * @param timeString 文字展示内容
     */
    public void setYzmStyle(boolean type, String timeString) {
        if (type) {
            tv_yzm.setText(timeString);
            tv_yzm.setTextColor(this.getResources().getColor(R.color.gray_edit_hint_word));
        } else {
            tv_yzm.setText(timeString);
            tv_yzm.setTextColor(this.getResources().getColor(R.color.app_basic));
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
        registerPresenter.cancel();
    }
}
