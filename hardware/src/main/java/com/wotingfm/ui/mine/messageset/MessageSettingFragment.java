package com.wotingfm.ui.mine.messageset;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wotingfm.R;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.constant.StringConstant;
import com.wotingfm.common.utils.StatusBarUtil;
import com.wotingfm.ui.base.basefragment.BaseFragment;
import com.wotingfm.ui.mine.main.MineActivity;

/**
 * 消息设置
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class MessageSettingFragment extends BaseFragment implements View.OnClickListener {
    private View rootView;
    private ImageView image_msg_set;
    private boolean b = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_message_setting, container, false);
            rootView.setOnClickListener(this);
            initView();
            setView();
        }
        return rootView;
    }

    // 初始化视图
    private void initView() {
        rootView.findViewById(R.id.head_left_btn).setOnClickListener(this);
        TextView textTitle = (TextView) rootView.findViewById(R.id.tv_center);// 标题
        textTitle.setText(getString(R.string.message_settings));
        rootView.findViewById(R.id.msg_set).setOnClickListener(this);
        image_msg_set = (ImageView) rootView.findViewById(R.id.image_msg_set);// 是否开启按钮
    }

    /**
     * 根据数据设置界面
     */
    private void setView() {
        String p = BSApplication.SharedPreferences.getString(StringConstant.PUSH_MSG_SET, "true");
        if (p != null && !p.trim().equals("")) {
            if (p.equals("true")) {
                b = true;
            } else {
                b = false;
            }
        } else {
            b = false;
        }
        setViewForImg(b);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.msg_set:
                msgSet();
                break;
            case R.id.head_left_btn:
                closeFragment();
                break;

        }
    }

    /**
     * 设置
     */
    private void msgSet() {
        if (b) {
            setViewForImg(false);
            b = false;
            save("false");
        } else {
            setViewForImg(true);
            b = true;
            save("true");
        }
    }

    /**
     * 保存设置
     *
     * @param type
     */
    private void save(String type) {
        SharedPreferences.Editor edit = BSApplication.SharedPreferences.edit();
        edit.putString(StringConstant.PUSH_MSG_SET, type);
        if (!edit.commit()) {
            Log.e("commit", "数据 commit 失败!");
        }
    }

    /**
     * 设置图片样式
     *
     * @param b
     */
    private void setViewForImg(boolean b) {
        if (b) {
            image_msg_set.setImageResource(R.mipmap.on_switch);
        } else {
            image_msg_set.setImageResource(R.mipmap.off_switch);
        }
    }

}
