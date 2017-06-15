package com.wotingfm.ui.intercom.main.chat.view;

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

import com.wotingfm.R;
import com.wotingfm.common.utils.GetTestData;
import com.wotingfm.ui.intercom.main.chat.adapter.ChatAdapter;
import com.wotingfm.ui.intercom.main.chat.presenter.ChatPresenter;
import com.wotingfm.ui.intercom.person.newfriend.adapter.NewFriendAdapter;
import com.wotingfm.ui.intercom.person.newfriend.presenter.NewFriendPresenter;

/**
 * 对讲机-获取联系列表，包括群组跟个人
 *
 * @author 辛龙
 *         2016年1月18日
 */
public class ChatFragment extends Fragment implements ChatAdapter.IonSlidingViewClickListener, View.OnClickListener{

    private View rootView;
    private ChatPresenter presenter;
    private FragmentActivity context;
    private RecyclerView mRecyclerView;
    private ChatAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_chat, container, false);
            rootView.setOnClickListener(this);
            inItView();
            presenter = new ChatPresenter(this);
            presenter.getData();
        }
        return rootView;
    }

    private void inItView() {
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    // 临时数据测试
    public void updateUI() {
        if (mAdapter == null) {
            mAdapter = new ChatAdapter(context, GetTestData.getFriendList());
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

    @Override
    public void onClick(View v) {

    }
}
