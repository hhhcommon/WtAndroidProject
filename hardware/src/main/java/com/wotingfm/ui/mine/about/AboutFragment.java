package com.wotingfm.ui.mine.about;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.woting.commonplat.manager.PhoneMsgManager;
import com.wotingfm.R;
import com.wotingfm.ui.mine.main.MineActivity;

/**
 * 关于
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class AboutFragment extends Fragment implements View.OnClickListener {
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_about, container, false);
            rootView.setOnClickListener(this);
            initView();
        }
        return rootView;
    }

    // 初始化视图
    private void initView() {
        rootView.findViewById(R.id.head_left_btn).setOnClickListener(this);      // 返回
        TextView textVersion = (TextView) rootView.findViewById(R.id.tv_verson); // 版本号
        textVersion.setText("版本号："+PhoneMsgManager.appVersionName);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left_btn:// 返回
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
