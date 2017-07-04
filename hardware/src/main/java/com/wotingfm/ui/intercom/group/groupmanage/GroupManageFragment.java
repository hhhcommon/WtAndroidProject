package com.wotingfm.ui.intercom.group.groupmanage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wotingfm.R;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.utils.ToastUtils;
import com.wotingfm.ui.intercom.group.applygrouptype.view.ApplyGroupTypeFragment;
import com.wotingfm.ui.intercom.group.editgroupmessage.view.EditGroupMessageFragment;
import com.wotingfm.ui.intercom.group.setmanager.view.SetManagerFragment;
import com.wotingfm.ui.intercom.group.standbychannel.view.StandbyChannelFragment;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;

import java.io.Serializable;
import java.util.List;

/**
 * 组管理界面
 * 作者：xinLong on 2017/6/5 01:30
 * 邮箱：645700751@qq.com
 */
public class GroupManageFragment extends Fragment implements View.OnClickListener {
    private View rootView;
    private Contact.group group;
    private List<Contact.user> list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_groupmanager, container, false);
            rootView.setOnClickListener(this);
            getData();
            inItView();
        }
        return rootView;
    }

    private void getData() {
        try {
            group = (Contact.group)  this.getArguments().getSerializable("group");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            list = (List<Contact.user>)  this.getArguments().getSerializable("list");// 成员列表
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            case R.id.re_groupNews:// 编辑资料
                if (group != null) {
                    EditGroupMessageFragment fragment = new EditGroupMessageFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("group", group);
                    fragment.setArguments(bundle);
                    InterPhoneActivity.open(fragment);
                } else {
                    ToastUtils.show_always(this.getActivity(), "数据出错了，请您稍后再试！");
                }
                break;
            case R.id.re_groupManager:// 设置管理员
                if (GlobalStateConfig.test) {
                    // 测试数据
                    InterPhoneActivity.open(new SetManagerFragment());
                } else {
                    // 实际数据
                    if (group != null && list != null && list.size() > 0&&group.getCreator_id()!=null&&group.getId()!=null) {
                        SetManagerFragment fragment = new SetManagerFragment();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("list", (Serializable) list);
                        bundle.putString("id", group.getCreator_id());
                        bundle.putString("gid", group.getId());
                        fragment.setArguments(bundle);
                        InterPhoneActivity.open(fragment);
                    } else {
                        ToastUtils.show_always(this.getActivity(), "数据出错了，请您稍后再试！");
                    }
                }
                break;
            case R.id.re_channel:// 设置备用频道
                if (GlobalStateConfig.test) {
                    StandbyChannelFragment fragment = new StandbyChannelFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("fromType", "message");
                    bundle.putString("groupId", "000");
                    bundle.putString("channel1", "");
                    bundle.putString("channel2", "");
                    fragment.setArguments(bundle);
                    InterPhoneActivity.open(fragment);
                } else {
                    if (group != null&&group.getId()!=null) {
                        StandbyChannelFragment fragment = new StandbyChannelFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("fromType", "message");
                        bundle.putString("groupId", group.getId());
                        bundle.putString("channel1", "");
                        bundle.putString("channel2", "");
                        fragment.setArguments(bundle);
                        InterPhoneActivity.open(fragment);
                    } else {
                        ToastUtils.show_always(this.getActivity(), "数据出错了，请您稍后再试！");
                    }
                }
                break;
            case R.id.re_applyGroupType:// 加群方式
                InterPhoneActivity.open(new ApplyGroupTypeFragment());
                break;
            case R.id.re_changeGroup:// 转让群
                ToastUtils.show_always(this.getActivity(), "转让群");
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
