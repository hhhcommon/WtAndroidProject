package com.wotingfm.ui.intercom.person.newfriend.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wotingfm.R;
import com.wotingfm.common.utils.GetTestData;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;
import com.wotingfm.ui.intercom.person.newfriend.adapter.NewFriendAdapter;
import com.wotingfm.ui.intercom.person.newfriend.presenter.NewFriendPresenter;

/**
 * 新的朋友
 * 作者：xinLong on 2017/6/5 01:30
 * 邮箱：645700751@qq.com
 */
public class NewFriendFragment extends Fragment implements NewFriendAdapter.IonSlidingViewClickListener, View.OnClickListener {
    private View rootView;
    private RecyclerView mRecyclerView;
    private NewFriendAdapter mAdapter;
    private FragmentActivity context;
    private NewFriendPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_newfriend, container, false);
        inItView();
        presenter = new NewFriendPresenter(this);
        presenter.getData();
        return rootView;
    }


    // 初始化界面
    private void inItView() {
        rootView.findViewById(R.id.head_left_btn).setOnClickListener(this);
        TextView tv_center = (TextView) rootView.findViewById(R.id.tv_center);
        tv_center.setText("新的朋友");

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left_btn:
                InterPhoneActivity.close();
                break;
        }
    }

    // 临时数据测试
    public void updateUI() {
        if (mAdapter == null) {
            mAdapter = new NewFriendAdapter(context, GetTestData.getFriendList());
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        } else {
            mAdapter.notifyDataSetChanged();
        }

        mAdapter.setOnSlidListener(this);
    }

    @Override
    public void onItemClick(View view, int position) {
    }

    @Override
    public void onDeleteBtnClick(View view, int position) {
        mAdapter.removeData(position);
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
