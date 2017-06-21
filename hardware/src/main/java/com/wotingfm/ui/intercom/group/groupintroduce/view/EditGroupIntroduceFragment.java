package com.wotingfm.ui.intercom.group.groupintroduce.view;

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
import com.wotingfm.ui.intercom.group.groupintroduce.presenter.EditGroupIntroducePresenter;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;

/**
 * 编辑群介绍
 * 作者：xinLong on 2017/6/5 01:30
 * 邮箱：645700751@qq.com
 */
public class EditGroupIntroduceFragment extends Fragment implements View.OnClickListener{
    private View rootView;
    private EditGroupIntroducePresenter presenter;
    private TextView tv_number;
    private EditText et_news;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_editgroupintroduce, container, false);
            rootView.setOnClickListener(this);
            inItView();
            presenter = new EditGroupIntroducePresenter(this);
        }
        return rootView;
    }


    private void inItView() {
        rootView.findViewById(R.id.head_left_btn).setOnClickListener(this);
        rootView.findViewById(R.id.tv_send).setOnClickListener(this);
        tv_number = (TextView) rootView.findViewById(R.id.tv_number);// 计数
        et_news = (EditText) rootView.findViewById(R.id.et_news);// 信息
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
