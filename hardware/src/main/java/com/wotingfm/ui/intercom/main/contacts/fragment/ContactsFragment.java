package com.wotingfm.ui.intercom.main.contacts.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wotingfm.R;
import com.wotingfm.ui.intercom.add.search.local.view.SearchContactsForLocalFragment;
import com.wotingfm.ui.intercom.group.groupchat.view.GroupChatFragment;
import com.wotingfm.ui.intercom.main.contacts.adapter.ContactsAdapter;
import com.wotingfm.ui.intercom.main.contacts.adapter.NoAdapter;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;
import com.wotingfm.ui.intercom.main.contacts.presenter.ContactsPresenter;
import com.wotingfm.ui.intercom.main.contacts.view.SideBar;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;
import com.wotingfm.ui.intercom.person.newfriend.view.NewFriendFragment;
import com.wotingfm.ui.intercom.person.personmessage.view.PersonMessageFragment;

import java.util.List;

/**
 * 最新联系人排序
 *
 * @author 辛龙
 *         2016年5月12日
 */
public class ContactsFragment extends Fragment implements View.OnClickListener {
    private View rootView;
    private FragmentActivity context;
    private ListView listView;
    private View headView;
    private TextView tv_search, tv_newGroupNum, tv_newFriendNum;
    private RelativeLayout re_newFriend, re_newGroup;
    private ContactsPresenter presenter;
    private List<Contact.user> srcList_p;
    private ContactsAdapter adapter;
    private SideBar sideBar;
    private TextView tvDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_contacts, container, false);
            initViews();// 设置界面
            setEditListener();
            presenter = new ContactsPresenter(this);
            presenter.getFriends();// 获取数据
        }
        return rootView;
    }

    // 初始化视图
    private void initViews() {
        listView = (ListView) rootView.findViewById(R.id.listView);
        sideBar = (SideBar) rootView.findViewById(R.id.sidrbar);
        tvDialog = (TextView) rootView.findViewById(R.id.dialog);
        sideBar.setTextView(tvDialog);

        headView = LayoutInflater.from(context).inflate(R.layout.headview_contacts, null);// 头部 view
        tv_search = (TextView) headView.findViewById(R.id.tv_search);
        re_newFriend = (RelativeLayout) headView.findViewById(R.id.re_newFriend);
        tv_newFriendNum = (TextView) headView.findViewById(R.id.tv_newFriendNum);
        re_newGroup = (RelativeLayout) headView.findViewById(R.id.re_newGroup);
        tv_newGroupNum = (TextView) headView.findViewById(R.id.tv_newGroupNum);

        listView.addHeaderView(headView);// 添加头部 view
    }

    // 设置监听
    private void setEditListener() {
        tv_search.setOnClickListener(this);
        re_newFriend.setOnClickListener(this);
        re_newGroup.setOnClickListener(this);
    }

    // 设置右侧触摸监听
    private void setTouchListener() {
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    listView.setSelection(position);
                }
            }
        });
    }

    // 设置右侧触摸监听
    private void setClickListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 此处数据+1
                PersonMessageFragment fragment = new PersonMessageFragment();
                Bundle bundle = new Bundle();
                bundle.putString("type", "true");
                fragment.setArguments(bundle);
                InterPhoneActivity.open(fragment);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_search:
                InterPhoneActivity.open(new SearchContactsForLocalFragment());
                break;
            case R.id.re_newFriend:
                InterPhoneActivity.open(new NewFriendFragment());
                break;
            case R.id.re_newGroup:
                InterPhoneActivity.open(new GroupChatFragment());
                break;
        }
    }

    // 有参方法 设置界面
    public void setData(List<Contact.user> list) {
        this.srcList_p = list;
        if (adapter != null) {
            adapter.ChangeDate(srcList_p);
        } else {
            adapter = new ContactsAdapter(context, srcList_p);
            listView.setAdapter(adapter);
        }
        setTouchListener();
        setClickListener();
    }

    // 无参方法 设置界面
    public void setData() {
        NoAdapter adapters = new NoAdapter(context);
        listView.setAdapter(adapters);
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