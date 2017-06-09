package com.wotingfm.ui.test.mine.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wotingfm.R;
import com.wotingfm.ui.test.mine.MineActivity;

/**
 * 个人中心主界面
 * Created by Administrator on 2017/6/7.
 */
public class MineFragment extends Fragment implements View.OnClickListener {
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_mine, container, false);
            rootView.setOnClickListener(this);

            initView();
            initEvent();
        }
        return rootView;
    }

    // 初始化视图
    private void initView() {

    }

    // 初始化点击事件
    private void initEvent() {
        rootView.findViewById(R.id.fm_set).setOnClickListener(this);// FM 设置
        rootView.findViewById(R.id.setting).setOnClickListener(this);// 设置
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (rootView != null) {
            ((ViewGroup) rootView.getParent()).removeView(rootView);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fm_set:// FM 设置
                MineActivity.open(new FMSetFragment());
                break;
            case R.id.setting:// 设置
                MineActivity.open(new SettingFragment());
                break;
        }
    }
}
