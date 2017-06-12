package com.wotingfm.ui.user.retrievepassword.view;

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
import com.wotingfm.ui.user.logo.LogoActivity;
import com.wotingfm.ui.user.retrievepassword.presenter.RetrievePasswordPresenter;

/**
 * 作者：xinLong on 2017/6/4 19:45
 * 邮箱：645700751@qq.com
 */
public class RetrievePassWordFragment extends Fragment implements View.OnClickListener {
    private TextView tv_confirm, tv_yzm;
    private ImageView left, img_eye;
    private EditText et_phoneNumber, et_newPassWord,et_yzm;
    private RetrievePasswordPresenter presenter;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_retrieve_password, container, false);
            inItView();
            inItListener();
            setEditListener();
            presenter= new RetrievePasswordPresenter(this);
            presenter.setEye();// 设置密码
        }
        return rootView;
    }

    // 设置界面
    private void inItView() {
        img_eye = (ImageView) rootView.findViewById(R.id.img_eye);                // 密码显示
        left = (ImageView) rootView.findViewById(R.id.head_left_btn);             // 返回按钮
        TextView tv_center = (TextView) rootView.findViewById(R.id.tv_center);    // 标头
        tv_center.setText("找回密码");

        et_phoneNumber = (EditText) rootView.findViewById(R.id.et_phoneNumber);   // 手机号输入框
        et_yzm = (EditText) rootView.findViewById(R.id.et_yzm);                   // 验证码输入框
        et_newPassWord = (EditText) rootView.findViewById(R.id.et_newPassWord);   // 新密码输入框

        tv_yzm = (TextView)rootView. findViewById(R.id.tv_yzm);                   // 获取验证码
        tv_confirm = (TextView) rootView.findViewById(R.id.tv_confirm);           // 确定
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
                LogoActivity.close();
                break;
            case R.id.img_eye:
                presenter.setEye();
                break;
            case R.id.tv_yzm:
                presenter.getYzm();
                break;
            case R.id.tv_confirm:
                confirm();
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
                String password = et_newPassWord.getText().toString().trim();
                String yzm = et_yzm.getText().toString().trim();
                presenter.getBtView(userName, password,yzm);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //  密码的监听
        et_newPassWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String userName = et_phoneNumber.getText().toString().trim();
                String password = et_newPassWord.getText().toString().trim();
                String yzm = et_yzm.getText().toString().trim();
                presenter.getBtView(userName, password,yzm);
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
                String password = et_newPassWord.getText().toString().trim();
                String yzm = et_yzm.getText().toString().trim();
                presenter.getBtView(userName, password,yzm);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    // confirm
    private void confirm() {
        String userName = et_phoneNumber.getText().toString().trim();
        String password = et_newPassWord.getText().toString().trim();
        String yzm = et_yzm.getText().toString().trim();
        presenter.confirm(userName, password,yzm);
    }

    /**
     * 设置密码显示的样式
     *
     * @param type true显示明文状态，false显示密码状态
     */
    public void setEyeShow(boolean type) {
        if (type) {
            img_eye.setImageResource(R.mipmap.login_icon_password_look);
            et_newPassWord.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            img_eye.setImageResource(R.mipmap.login_icon_password_unlook);
            et_newPassWord.setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
    }

    /**
     * 设置登录按钮的样式
     *
     * @param type true输入完成状态，false未输入完成状态
     */
    public void setConfirmBackground(boolean type) {
        if (type) {
            tv_confirm.setBackgroundResource(R.drawable.background_login_tvlogin_on);
        } else {
            tv_confirm.setBackgroundResource(R.drawable.background_login_tvlogin_off);
        }
    }

    /**
     * 设置获取验证码的样式
     * @param type 完成状态
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
}
