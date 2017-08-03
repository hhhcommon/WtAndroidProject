package com.wotingfm.ui.intercom.group.groupapply.view;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.wotingfm.R;
import com.wotingfm.common.utils.DialogUtils;
import com.wotingfm.ui.intercom.group.groupapply.presenter.GroupApplyForPasswordPresenter;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;

/**
 * 入组申请界面
 * 作者：xinLong on 2017/6/5 01:30
 * 邮箱：645700751@qq.com
 */
public class GroupApplyForPasswordFragment extends Fragment implements View.OnClickListener {
    private View rootView;
    private EditText et_news;
    private GroupApplyForPasswordPresenter presenter;
    private Dialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_apply_password, container, false);
            rootView.setOnClickListener(this);
            inItView();
            presenter = new GroupApplyForPasswordPresenter(this);
        }
        return rootView;
    }

    private void inItView() {
        rootView.findViewById(R.id.head_left_btn).setOnClickListener(this);
        rootView.findViewById(R.id.tv_send).setOnClickListener(this);
        et_news = (EditText) rootView.findViewById(R.id.et_news);// 申请信息
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left_btn:
                InterPhoneActivity.close();
                break;
            case R.id.tv_send:
                String s = et_news.getText().toString().trim();
                presenter.send(s);
                break;
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
        Log.e("界面","执行销毁");
        presenter.destroy();
        presenter=null;
    }
}
