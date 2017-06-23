package com.wotingfm.ui.mine.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.wotingfm.R;
import com.wotingfm.common.constant.StringConstant;
import com.wotingfm.ui.mine.main.MineActivity;

/**
 * 连接无线网络配置
 * Created by Administrator on 2017/6/15.
 */
public class ConfigWiFiFragment extends Fragment implements View.OnClickListener {
    private View rootView;

    private EditText editPsw;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_config_wi_fi, container, false);
            rootView.setOnClickListener(this);

            init();
        }
        return rootView;
    }

    // 初始化
    private void init() {
        String wiFiName = getArguments().getString(StringConstant.WIFI_NAME);

        TextView textTitle = (TextView) rootView.findViewById(R.id.tv_center);// 标题
        if (wiFiName != null && !wiFiName.trim().equals("")) {
            textTitle.setText(wiFiName);// 设置标题
        } else {
            textTitle.setText(getString(R.string.connect));// 设置标题
        }

        rootView.findViewById(R.id.head_left_btn).setOnClickListener(this);
        editPsw = (EditText) rootView.findViewById(R.id.edit_psw);// 输入 密码

        rootView.findViewById(R.id.btn_cancel).setOnClickListener(this);// 取消
        rootView.findViewById(R.id.btn_confirm).setOnClickListener(this);// 连接
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

                Fragment targetFragment = getTargetFragment();
                ((WIFIFragment) targetFragment).setAddCardResult(res);

                MineActivity.close();
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (rootView != null) {
            ((ViewGroup) rootView.getParent()).removeView(rootView);
        }
    }
}
