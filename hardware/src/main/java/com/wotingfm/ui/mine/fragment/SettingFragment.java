package com.wotingfm.ui.mine.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wotingfm.R;
import com.wotingfm.ui.mine.MineActivity;

/**
 * 设置
 * Created by Administrator on 2017/6/9.
 */
public class SettingFragment extends Fragment implements View.OnClickListener {
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_setting, container, false);
            rootView.setOnClickListener(this);

            initView();
            initEvent();
        }
        return rootView;
    }

    // 初始化视图
    private void initView() {
        TextView textTitle = (TextView) rootView.findViewById(R.id.tv_center);// 标题
        textTitle.setText(getString(R.string.setting));
    }

    // 初始化点击事件
    private void initEvent() {
        rootView.findViewById(R.id.head_left_btn).setOnClickListener(this);// 返回

        rootView.findViewById(R.id.view_personal_info).setOnClickListener(this);// 个人信息
        rootView.findViewById(R.id.view_my_concern).setOnClickListener(this);// 我关注的
        rootView.findViewById(R.id.view_preference_setting).setOnClickListener(this);// 偏好设置
        rootView.findViewById(R.id.view_message_settings).setOnClickListener(this);// 消息设置
        rootView.findViewById(R.id.view_clear_cache).setOnClickListener(this);// 清理缓存
        rootView.findViewById(R.id.view_account_security).setOnClickListener(this);// 账号安全
        rootView.findViewById(R.id.view_feedback).setOnClickListener(this);// 意见反馈
        rootView.findViewById(R.id.view_about).setOnClickListener(this);// 关于
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
            case R.id.head_left_btn:// 返回
                MineActivity.close();
                break;
            case R.id.view_personal_info:// 个人信息
                MineActivity.open(new PersonalInfoFragment());
                break;
            case R.id.view_my_concern:// 我关注的

                break;
            case R.id.view_preference_setting:// 偏好设置

                break;
            case R.id.view_message_settings:// 消息设置
                MineActivity.open(new MessageSettingFragment());
                break;
            case R.id.view_clear_cache:// 清理缓存

                break;
            case R.id.view_account_security:// 账号安全

                break;
            case R.id.view_feedback:// 意见反馈
                MineActivity.open(new FeedbackFragment());
                break;
            case R.id.view_about:// 关于

                break;
        }
    }
}
