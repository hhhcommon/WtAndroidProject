package com.wotingfm.ui.mine.editusermessage.view;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wotingfm.R;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.constant.StringConstant;
import com.wotingfm.common.utils.DialogUtils;
import com.wotingfm.ui.mine.editusermessage.presenter.EditUserPresenter;
import com.wotingfm.ui.mine.main.MineActivity;

/**
 * 用户信息修改
 * 作者：xinLong on 2017/6/5 01:30
 * 邮箱：645700751@qq.com
 */
public class EditUserFragment extends Fragment implements View.OnClickListener {
    private View rootView;
    private EditUserPresenter presenter;
    private Dialog dialog;
    private ResultListener Listener;
    private TextView tv_center, tv_number;
    private EditText et_name, et_introduce;
    private RelativeLayout re_introduce;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_edituser, container, false);
            rootView.setOnClickListener(this);
            inItView();
            presenter = new EditUserPresenter(this);
            setListener();
        }
        return rootView;
    }

    private void setListener() {
        et_introduce.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String src = et_introduce.getText().toString().trim();
                presenter.textChange(src);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    // 设置界面
    private void inItView() {
        rootView.findViewById(R.id.head_left_btn).setOnClickListener(this);
        rootView.findViewById(R.id.tv_send).setOnClickListener(this);
        tv_center = (TextView) rootView.findViewById(R.id.tv_center);// 信息
        et_name = (EditText) rootView.findViewById(R.id.et_name);// 名称
        re_introduce = (RelativeLayout) rootView.findViewById(R.id.re_introduce);// 介绍
        et_introduce = (EditText) rootView.findViewById(R.id.et_introduce);// 介绍
        tv_number = (TextView) rootView.findViewById(R.id.tv_number);// 计数
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left_btn:
                MineActivity.close();
                break;
            case R.id.tv_send:
                String s1 = et_name.getText().toString().trim();// 名称
                String s2 = et_introduce.getText().toString().trim();// 介绍
                presenter.send(s1, s2);
                break;
        }
    }

    /**
     * 根据上级界面传递的数据进行界面展示
     *
     * @param type
     */
    public void setView(int type) {
        if (type == 1) {
            tv_center.setText("昵称");
            et_name.setText(BSApplication.SharedPreferences.getString(StringConstant.NICK_NAME, ""));
            et_name.setVisibility(View.VISIBLE);
            re_introduce.setVisibility(View.GONE);
        } else if (type == 2) {
            tv_center.setText("简介");
            et_introduce.setText(BSApplication.SharedPreferences.getString(StringConstant.USER_SIGN, ""));
            et_name.setVisibility(View.GONE);
            re_introduce.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 字数计数的数据展示
     *
     * @param s
     */
    public void setTextViewChange(String s) {
        tv_number.setText(s);
    }

    /**
     * 返回值设置
     *
     * @param b
     * @param name
     */
    public void setResult(boolean b, String name) {
        Listener.resultListener(b, name);
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

    /**
     * 回调结果值
     *
     * @param l
     */
    public void setResultListener(ResultListener l) {
        Listener = l;
    }

    public interface ResultListener {
        void resultListener(boolean b, String name);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.destroy();
        presenter = null;
    }
}
