package com.wotingfm.ui.intercom.group.groupmumberdel.view;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.woting.commonplat.widget.TipView;
import com.wotingfm.R;
import com.wotingfm.common.utils.DialogUtils;
import com.wotingfm.ui.intercom.group.groupmumberdel.adapter.GroupNumberDelAdapter;
import com.wotingfm.ui.intercom.group.groupmumberdel.presenter.GroupNumberDelPresenter;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;
import com.wotingfm.ui.intercom.person.newfriend.adapter.NewFriendAdapter;
import com.wotingfm.ui.intercom.person.newfriend.model.NewFriend;
import com.wotingfm.ui.intercom.person.newfriend.presenter.NewFriendPresenter;

import java.util.List;

/**
 * 删除组成员
 * 作者：xinLong on 2017/6/5 01:30
 * 邮箱：645700751@qq.com
 */
public class GroupNumberDelFragment extends Fragment implements GroupNumberDelAdapter.IonSlidingViewClickListener,  View.OnClickListener{
    private View rootView;
    private RecyclerView mRecyclerView;
    private GroupNumberDelPresenter presenter;
    private GroupNumberDelAdapter mAdapter;
    private Dialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_groupmumbersdel, container, false);
            rootView.setOnClickListener(this);
            inItView();
            presenter = new GroupNumberDelPresenter(this);
            presenter.getData();
        }
        return rootView;
    }


    // 初始化界面
    private void inItView() {
        rootView.findViewById(R.id.head_left_btn).setOnClickListener(this);
        TextView tv_center = (TextView) rootView.findViewById(R.id.tv_center);
        tv_center.setText("删除群成员");

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left_btn:
                InterPhoneActivity.close();
                break;
        }
    }

    // 适配数据
    public void updateUI(List<Contact.user> list) {
        if (mAdapter == null) {
            mAdapter = new GroupNumberDelAdapter(this.getActivity(), list);
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        } else {
            mAdapter.notifyDataSetChanged();
        }
        mAdapter.setOnSlidListener(this);
    }

    /**
     * 删除按钮的操作
     * @param view
     * @param position
     */
    @Override
    public void onDeleteBtnClick(View view, int position) {
        presenter.del(position);
    }

    /**
     * item的点击事件
     * @param view
     * @param position
     */
    @Override
    public void onAdapterClick(View view, int position) {
        presenter.onClick(position);
    }

    /**
     * 展示弹出框
     */
    public void dialogShow() {
        dialog = DialogUtils.Dialog(this.getActivity());
    }

    /**
     * 取消弹出框
     */
    public void dialogCancel() {
        if (dialog != null) dialog.dismiss();
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
