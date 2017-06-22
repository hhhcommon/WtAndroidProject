package com.wotingfm.ui.intercom.group.groupnews.add.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wotingfm.R;
import com.wotingfm.common.utils.ToastUtils;
import com.wotingfm.ui.intercom.group.groupmanage.GroupManageFragment;
import com.wotingfm.ui.intercom.group.groupnews.add.adapter.GroupNewsPersonForAddAdapter;
import com.wotingfm.ui.intercom.group.groupnews.add.presenter.GroupNewsForAddPresenter;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;
import com.wotingfm.ui.intercom.main.simulation.SimulationInterPhoneFragment;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;

import java.util.List;

/**
 * 已加入的组详情界面
 * 作者：xinLong on 2017/6/5 01:30
 * 邮箱：645700751@qq.com
 */
public class GroupNewsForAddFragment extends Fragment implements View.OnClickListener {
    private View rootView;
    private GroupNewsForAddPresenter presenter;
    private TextView tv_groupName, tv_groupNumber, tv_address, tv_groupIntroduce, tv_number, tv_channel1, tv_channel2;
    private RelativeLayout re_groupIntroduce, re_groupNumber, re_groupEwm, re_channel1, re_channel2, re_groupManager;
    private GridView gridView;
    private GroupNewsPersonForAddAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_groupnewsadd, container, false);
            rootView.setOnClickListener(this);

            inItView();
            presenter = new GroupNewsForAddPresenter(this);
            presenter.getData();
        }
        return rootView;
    }

    private void inItView() {
        rootView.findViewById(R.id.head_left_btn).setOnClickListener(this);//
        rootView.findViewById(R.id.img_other).setOnClickListener(this);//

        tv_groupName = (TextView) rootView.findViewById(R.id.tv_groupName);// 群名称
        tv_groupNumber = (TextView) rootView.findViewById(R.id.tv_groupNumber);// 群号
        tv_address = (TextView) rootView.findViewById(R.id.tv_address);// 地址
        re_groupEwm = (RelativeLayout) rootView.findViewById(R.id.re_groupEwm);// 群二维码
        re_groupEwm.setOnClickListener(this);
        tv_groupIntroduce = (TextView) rootView.findViewById(R.id.tv_groupIntroduce);// 群介绍
        re_groupIntroduce = (RelativeLayout) rootView.findViewById(R.id.re_groupIntroduce);// 群介绍按钮（不需要）
        re_groupIntroduce.setOnClickListener(this);
        re_groupManager = (RelativeLayout) rootView.findViewById(R.id.re_groupManager);// 管理群
        re_groupManager.setOnClickListener(this);

        tv_number = (TextView) rootView.findViewById(R.id.tv_number);// 成员数
        re_groupNumber = (RelativeLayout) rootView.findViewById(R.id.re_groupNumber);// 成员数按钮
        re_groupNumber.setOnClickListener(this);
        gridView = (GridView) rootView.findViewById(R.id.gridView);// 成员展示
        tv_channel1 = (TextView) rootView.findViewById(R.id.tv_channel1);// 对讲频道1
        re_channel1 = (RelativeLayout) rootView.findViewById(R.id.re_channel1);
        tv_channel2 = (TextView) rootView.findViewById(R.id.tv_channel2);// 对讲频道2
        re_channel2 = (RelativeLayout) rootView.findViewById(R.id.re_channel2);
        rootView.findViewById(R.id.lin_send).setOnClickListener(this);// 开始对讲
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left_btn:
                InterPhoneActivity.close();
                break;
            case R.id.img_other:
                break;
            case R.id.re_groupEwm:
                break;
            case R.id.re_groupManager:
                InterPhoneActivity.open(new GroupManageFragment());
                break;
            case R.id.lin_send:
                ToastUtils.show_always(this.getActivity(), "开始对讲");
                break;
            case R.id.re_channel1:
                SimulationInterPhoneFragment fragment = new SimulationInterPhoneFragment();
                Bundle bundle = new Bundle();
                bundle.putString("channel", "00000");
                fragment.setArguments(bundle);
                InterPhoneActivity.open(fragment);
                break;
            case R.id.re_channel2:
                SimulationInterPhoneFragment fragment1 = new SimulationInterPhoneFragment();
                Bundle bundle1 = new Bundle();
                bundle1.putString("channel", "00000");
                fragment1.setArguments(bundle1);
                InterPhoneActivity.open(fragment1);
                break;
        }
    }

    /**
     * 设置界面数据
     *
     * @param name
     * @param number
     * @param address
     * @param introduce
     */
    public void setViewData(String name, String number, String address, String introduce, String channel1, String channel2) {
        tv_groupName.setText(name);// 群名称
        tv_groupNumber.setText(number);// 群号
        tv_address.setText(address);// 地址
        tv_groupIntroduce.setText(introduce);// 群介绍
        tv_channel1.setText(channel1);// 对讲频道1
        tv_channel2.setText(channel2);//对讲频道2
    }

    /**
     * 设置成员展示
     *
     * @param list
     */
    public void setGridViewData(List<Contact.user> list) {
        if (adapter == null) {
            adapter = new GroupNewsPersonForAddAdapter(this.getActivity(), list);
            gridView.setAdapter(adapter);
        } else {
            adapter.changeData(list);
        }
        tv_number.setText("（" + String.valueOf(list.size()) + "）");// 成员数
    }

    /**
     * 隐藏成员展示
     */
    public void setGridView() {
        gridView.setVisibility(View.GONE);
        tv_number.setText("（0）");// 成员数
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
