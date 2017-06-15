package com.wotingfm.ui.intercom.group.groupapply.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.wotingfm.R;
import com.wotingfm.ui.intercom.group.groupapply.presenter.GroupApplyPresenter;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;

/**
 * 入组申请界面
 * 作者：xinLong on 2017/6/5 01:30
 * 邮箱：645700751@qq.com
 */
public class GroupApplyFragment extends Fragment implements View.OnClickListener {
    private View rootView;
    private EditText et_news;
    private TextView tv_number;
    private GroupApplyPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_apply, container, false);
            inItView();
            presenter = new GroupApplyPresenter(this);
        }
        return rootView;
    }

    private void inItView() {
        rootView.findViewById(R.id.head_left_btn).setOnClickListener(this);
        rootView.findViewById(R.id.tv_send).setOnClickListener(this);
        tv_number = (TextView) rootView.findViewById(R.id.tv_number);// 计数
        et_news = (EditText) rootView.findViewById(R.id.et_news);// 申请信息
        et_news.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String src = et_news.getText().toString().trim();
                presenter.textChange(src);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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
     * 字数计数的数据展示
     *
     * @param s
     */
    public void setTextViewChange(String s) {
        tv_number.setText(s);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
