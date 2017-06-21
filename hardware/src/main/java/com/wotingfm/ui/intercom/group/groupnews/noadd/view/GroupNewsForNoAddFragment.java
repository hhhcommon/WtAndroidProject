package com.wotingfm.ui.intercom.group.groupnews.noadd.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wotingfm.R;
import com.wotingfm.ui.intercom.group.groupapply.view.GroupApplyFragment;
import com.wotingfm.ui.intercom.group.groupnews.noadd.adapter.GroupNewsPersonForNoAddAdapter;
import com.wotingfm.ui.intercom.group.groupnews.noadd.presenter.GroupNewsForNoAddPresenter;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;

import java.util.List;

/**
 * 未加入的组详情界面
 * 作者：xinLong on 2017/6/5 01:30
 * 邮箱：645700751@qq.com
 */
public class GroupNewsForNoAddFragment extends Fragment implements View.OnClickListener {
    private View rootView;
    private TextView tv_groupName, tv_groupNumber, tv_address, tv_groupIntroduce, tv_number, tv_send;
    private RelativeLayout re_groupIntroduce, re_groupNumber;
    private GridView gridView;
    private GroupNewsForNoAddPresenter presenter;
    private GroupNewsPersonForNoAddAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_groupnews, container, false);
            rootView.setOnClickListener(this);

            Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
            toolbar.inflateMenu(R.menu.menu_groupnews);
            toolbar.setTitle("xinlong");
            toolbar.setTitleTextColor(this.getResources().getColor(R.color.app_basic));
            InterPhoneActivity.context.setSupportActionBar(toolbar);

            inItView();
            presenter = new GroupNewsForNoAddPresenter(this);
            presenter.getData();
        }
        return rootView;
    }

    private void inItView() {
        tv_groupName = (TextView) rootView.findViewById(R.id.tv_groupName);// 群名称
        tv_groupNumber = (TextView) rootView.findViewById(R.id.tv_groupNumber);// 群号
        tv_address = (TextView) rootView.findViewById(R.id.tv_address);// 地址
        tv_groupIntroduce = (TextView) rootView.findViewById(R.id.tv_groupIntroduce);// 群介绍
        re_groupIntroduce = (RelativeLayout) rootView.findViewById(R.id.re_groupIntroduce);// 群介绍按钮(不需要)
        re_groupIntroduce.setOnClickListener(this);
        tv_number = (TextView) rootView.findViewById(R.id.tv_number);// 成员数
        re_groupNumber = (RelativeLayout) rootView.findViewById(R.id.re_groupNumber);// 成员数按钮
        re_groupNumber.setOnClickListener(this);
        gridView = (GridView) rootView.findViewById(R.id.gridView);// 成员展示
        tv_send = (TextView) rootView.findViewById(R.id.tv_send);// 加入群
        tv_send.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left_btn:
                break;
            case R.id.tv_send:
                InterPhoneActivity.open(new GroupApplyFragment());
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
    public void setViewData(String name, String number, String address, String introduce) {
        tv_groupName.setText(name);// 群名称
        tv_groupNumber.setText(number);// 群号
        tv_address.setText(address);// 地址
        tv_groupIntroduce.setText(introduce);// 群介绍
    }

    /**
     * 设置成员展示
     *
     * @param list
     */
    public void setGridViewData(List<Contact.user> list) {
        if (adapter == null) {
            adapter = new GroupNewsPersonForNoAddAdapter(this.getActivity(), list);
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
