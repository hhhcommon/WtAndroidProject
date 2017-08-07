package com.wotingfm.ui.mine.wifi.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import com.wotingfm.R;
import com.wotingfm.ui.mine.main.MineActivity;
import com.wotingfm.ui.mine.wifi.presenter.ConfigWIFIPresenter;


/**
 * 连接无线网络配置
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class ConfigWIFIFragment extends Fragment implements View.OnClickListener {
    private View rootView;
    private EditText editPsw;
    private ConfigWIFIPresenter presenter;
    private TextView textTitle, tv_signal, tv_encryption;
    private ResultListener Listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_config_wi_fi, container, false);
            rootView.setOnClickListener(this);
            init();
            presenter = new ConfigWIFIPresenter(this);
        }
        return rootView;
    }

    // 初始化
    private void init() {
        rootView.findViewById(R.id.head_left_btn).setOnClickListener(this);
        textTitle = (TextView) rootView.findViewById(R.id.tv_center);         // 标题
        tv_signal = (TextView) rootView.findViewById(R.id.tv_signal);         // 信号强度
        tv_encryption = (TextView) rootView.findViewById(R.id.tv_encryption); // 加密类型
        editPsw = (EditText) rootView.findViewById(R.id.edit_psw);            // 输入 密码
        rootView.findViewById(R.id.btn_cancel).setOnClickListener(this);      // 取消
        rootView.findViewById(R.id.btn_confirm).setOnClickListener(this);     // 连接
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left_btn:// 返回
                MineActivity.close();
                break;
            case R.id.btn_cancel:// 取消连接
                MineActivity.close();
                break;
            case R.id.btn_confirm:// 连接
                String res = editPsw.getText().toString().trim();
                presenter.link(res);
                break;
        }
    }

    /**
     * 适配界面数据
     * @param wiFiName
     * @param signal
     * @param encryption
     */
    public void setView(String wiFiName,String signal,String encryption) {
        textTitle.setText(wiFiName);// 设置标题
        tv_signal.setText(signal);// 信号强度
        tv_encryption.setText(encryption);// 加密类型
    }

    /**
     * 返回值设置
     *
     * @param type
     */
    public void setResult(boolean type) {
        Listener.resultListener(type);
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
        void resultListener(boolean type);
    }
}
