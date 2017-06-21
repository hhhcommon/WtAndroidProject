package com.wotingfm.ui.intercom.group.groupmanage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wotingfm.R;
import com.wotingfm.ui.intercom.group.applygrouptype.view.ApplyGroupTypeFragment;
import com.wotingfm.ui.intercom.group.editgroupmessage.view.EditGroupMessageFragment;
import com.wotingfm.ui.intercom.group.standbychannel.view.StandbyChannelFragment;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;

/**
 * 组管理界面
 * 作者：xinLong on 2017/6/5 01:30
 * 邮箱：645700751@qq.com
 */
public class GroupManageFragment extends Fragment implements View.OnClickListener {
    private View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_groupmanager, container, false);
            rootView.setOnClickListener(this);
            inItView();
        }
        return rootView;
    }

    private void inItView() {
        rootView.findViewById(R.id.head_left_btn).setOnClickListener(this);
        rootView.findViewById(R.id.re_groupNews).setOnClickListener(this);// 编辑资料
        rootView.findViewById(R.id.re_groupManager).setOnClickListener(this);// 设置管理员
        rootView.findViewById(R.id.re_channel).setOnClickListener(this);// 设置备用频道
        rootView.findViewById(R.id.re_applyGroupType).setOnClickListener(this);// 设置管理员
        rootView.findViewById(R.id.re_changeGroup).setOnClickListener(this);// 转让群

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left_btn:
                InterPhoneActivity.close();
                break;
            case R.id.re_groupNews:
                InterPhoneActivity.open(new EditGroupMessageFragment());
                break;
            case R.id.re_groupManager:
                break;
            case R.id.re_channel:
                InterPhoneActivity.open(new StandbyChannelFragment());
                break;
            case R.id.re_applyGroupType:
                InterPhoneActivity.open(new ApplyGroupTypeFragment());
                break;
            case R.id.re_changeGroup:
                break;
        }
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
