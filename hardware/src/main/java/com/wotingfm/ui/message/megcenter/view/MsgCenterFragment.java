package com.wotingfm.ui.message.megcenter.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wotingfm.R;
import com.wotingfm.ui.message.group.view.MsgGroupFragment;
import com.wotingfm.ui.message.megcenter.presenter.MsgCenterPresenter;
import com.wotingfm.ui.message.notify.view.MsgNotifyFragment;
import com.wotingfm.ui.message.person.view.MsgPersonFragment;
import com.wotingfm.ui.mine.main.MineActivity;

/**
 * 消息中心
 * 作者：xinLong on 2017/6/5 01:30
 * 邮箱：645700751@qq.com
 */
public class MsgCenterFragment extends Fragment implements View.OnClickListener {
    private View rootView;
    private MsgCenterPresenter presenter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_msg_center, container, false);
            rootView.setOnClickListener(this);
            inItView();
            presenter = new MsgCenterPresenter(this);
        }
        return rootView;
    }

    // 设置界面
    private void inItView() {
        rootView.findViewById(R.id.head_left_btn).setOnClickListener(this);
        rootView.findViewById(R.id.re_notify).setOnClickListener(this);// 通知消息
        rootView.findViewById(R.id.re_person).setOnClickListener(this);// 好友消息
        rootView.findViewById(R.id.re_group).setOnClickListener(this);// 群组消息

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left_btn:
                MineActivity.close();
                break;
            case R.id.re_notify:
                MineActivity.open(new MsgNotifyFragment());
                break;
            case R.id.re_person:
                MineActivity.open(new MsgPersonFragment());
                break;
            case R.id.re_group:
                MineActivity.open(new MsgGroupFragment());
                break;
        }
    }


}
