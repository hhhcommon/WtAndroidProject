package com.woting.ui.intercom.group.groupintroduce.view;

import android.app.Dialog;
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

import com.woting.R;
import com.woting.common.utils.DialogUtils;
import com.woting.ui.intercom.group.groupintroduce.presenter.EditGroupIntroducePresenter;
import com.woting.ui.intercom.main.view.InterPhoneActivity;

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
    private Dialog dialog;
    private ResultListener Listener;

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
     * 设置返回值监听
     * @param name
     */
    public void setResult(String name){
        Listener.resultListener(true,name);
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
        void resultListener(boolean type, String name);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("界面","执行销毁");
        presenter.destroy();
        presenter=null;
    }
}
