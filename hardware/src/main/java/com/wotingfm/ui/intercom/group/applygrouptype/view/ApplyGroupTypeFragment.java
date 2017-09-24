package com.wotingfm.ui.intercom.group.applygrouptype.view;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.wotingfm.R;
import com.wotingfm.common.utils.DialogUtils;
import com.wotingfm.ui.base.basefragment.BaseFragment;
import com.wotingfm.ui.intercom.group.applygrouptype.presenter.ApplyGroupTypePresenter;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;

/**
 * 加群方式
 * 作者：xinLong on 2017/6/5 01:30
 * 邮箱：645700751@qq.com
 */
public class ApplyGroupTypeFragment extends BaseFragment implements View.OnClickListener {
    private View rootView;
    private ApplyGroupTypePresenter presenter;
    private ImageView img_password, img_shen;
    private EditText et_password;
    private ResultListener Listener;
    private Dialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_applygrouptype, container, false);
            rootView.setOnClickListener(this);
            inItView();
            presenter = new ApplyGroupTypePresenter(this);
            presenter.setView();
        }
        return rootView;
    }

    // 设置界面
    private void inItView() {
        rootView.findViewById(R.id.head_left_btn).setOnClickListener(this);
        rootView.findViewById(R.id.tv_ok).setOnClickListener(this);
        img_password = (ImageView) rootView.findViewById(R.id.img_password);
        img_password.setOnClickListener(this);
        et_password = (EditText) rootView.findViewById(R.id.et_password);
        img_shen = (ImageView) rootView.findViewById(R.id.img_shen);
        img_shen.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left_btn:
                InterPhoneActivity.close();
                break;
            case R.id.tv_ok:// 确定
                String m = et_password.getText().toString();
                presenter.ok(m);
                break;
            case R.id.img_password:// 密码群
                presenter.password();
                break;
            case R.id.img_shen:// 审核群
                presenter.shen();
                break;
        }
    }


    /**
     * 返回值设置
     *
     * @param type
     * @param password
     */
    public void setResult(int type, String password) {
        Listener.resultListener(type, password);
    }

    /**
     * 设置密码群样式
     *
     * @param b
     */
    public void setPasswordView(boolean b) {
        if (b) {
            img_password.setImageResource(R.mipmap.create_group_icon_selected_s);
        } else {
            img_password.setImageResource(R.mipmap.create_group_icon_selected_n);
        }
    }

    /**
     * 设置密码群密码
     *
     * @param s
     */
    public void setPasswordViewNews(String s) {
        et_password.setText(s);
    }

    /**
     * 设置审核群样式
     *
     * @param b
     */
    public void setShenView(boolean b) {
        if (b) {
            img_shen.setImageResource(R.mipmap.create_group_icon_selected_s);
        } else {
            img_shen.setImageResource(R.mipmap.create_group_icon_selected_n);
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

    /**
     * 回调结果值
     *
     * @param l
     */
    public void setResultListener(ResultListener l) {
        Listener = l;
    }

    public interface ResultListener {
        void resultListener(int type, String password);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("界面","执行销毁");
        presenter.destroy();
        presenter=null;
    }
}
