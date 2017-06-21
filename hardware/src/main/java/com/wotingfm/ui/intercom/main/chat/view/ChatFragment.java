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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wotingfm.R;
import com.wotingfm.ui.intercom.group.groupnews.add.view.GroupNewsForAddFragment;
import com.wotingfm.ui.intercom.main.chat.adapter.ChatAdapter;
import com.wotingfm.ui.intercom.main.chat.presenter.ChatPresenter;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;
import com.wotingfm.ui.intercom.person.personmessage.view.PersonMessageFragment;

import java.util.List;

/**
 * 对讲机-获取联系列表，包括群组跟个人
 *
 * @author 辛龙
 *         2016年1月18日
 */
public class ChatFragment extends Fragment implements ChatAdapter.IonSlidingViewClickListener, View.OnClickListener {

    private View rootView;
    private ChatPresenter presenter;
    private FragmentActivity context;
    private RecyclerView mRecyclerView;
    private ChatAdapter mAdapter;
    private ImageView img_url_group,img_person_group;
    private RelativeLayout re_group;
    private TextView tv_groupName_group,tv_news_group,tv_person_group;

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
        // 组的界面
        re_group = (RelativeLayout) rootView.findViewById(R.id.re_group);
        rootView.findViewById(R.id.img_close_group).setOnClickListener(this);
        img_url_group = (ImageView) rootView.findViewById(R.id.img_url_group);
        img_url_group.setOnClickListener(this);
        tv_groupName_group= (TextView) rootView.findViewById(R.id.tv_groupName_group);
        tv_news_group= (TextView) rootView.findViewById(R.id.tv_news_group);
        tv_person_group= (TextView) rootView.findViewById(R.id.tv_person_group);
        img_person_group= (ImageView) rootView.findViewById(R.id.img_url_group);

        // 用户的界面

        // 列表
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_url_group:
                InterPhoneActivity.open(new GroupNewsForAddFragment());
                break;
            case R.id.img_close_group:
                re_group.setVisibility(View.GONE);
                break;
        }
    }

    // 临时数据测试
    public void updateUI(List<Contact.user> list) {
        if (mAdapter == null) {
            mAdapter = new ChatAdapter(context, list);
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
