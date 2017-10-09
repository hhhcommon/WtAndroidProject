package com.wotingfm.ui.mine.bluetooth.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.wotingfm.R;
import com.wotingfm.ui.base.basefragment.BaseFragment;
import com.wotingfm.ui.mine.bluetooth.presenter.EditBlueToothPresenter;

/**
 * 设置蓝牙名称
 * 作者：xinLong on 2017/6/5 01:30
 * 邮箱：645700751@qq.com
 */
public class EditBlueToothNameFragment extends BaseFragment implements View.OnClickListener {
    private View rootView;
    private EditBlueToothPresenter presenter;
    private EditText et_news;
    private ResultListener Listener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_editbluetoothname, container, false);
            rootView.setOnClickListener(this);
            inItView();
            presenter = new EditBlueToothPresenter(this);
        }
        return rootView;
    }

    // 设置界面
    private void inItView() {
        rootView.findViewById(R.id.head_left_btn).setOnClickListener(this);
        rootView.findViewById(R.id.tv_send).setOnClickListener(this);
        et_news = (EditText) rootView.findViewById(R.id.et_news);// 信息
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left_btn:
                closeFragment();
                break;
            case R.id.tv_send:
                String s = et_news.getText().toString().trim();
                presenter.save(s);
                break;
        }
    }

    /**
     * 界面适配-蓝牙名称
     * @param s
     */
    public void setName(String s){
        et_news.setText(s);
    }

    /**
     * 返回值设置
     *
     * @param type
     * @param name
     */
    public void setResult(boolean type, String name) {
        Listener.resultListener(type, name);
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
        presenter=null;
    }

}
