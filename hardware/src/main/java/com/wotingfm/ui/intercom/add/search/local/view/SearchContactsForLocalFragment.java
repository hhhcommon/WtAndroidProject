package com.wotingfm.ui.intercom.add.search.local.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.woting.commonplat.widget.HeightListView;
import com.wotingfm.R;
import com.wotingfm.ui.base.model.UserInfo;
import com.wotingfm.ui.intercom.add.search.local.adapter.GroupsAdapter;
import com.wotingfm.ui.intercom.add.search.local.presenter.SearchContactsForLocalPresenter;
import com.wotingfm.ui.intercom.group.groupnews.GroupNewsForAddFragment;
import com.wotingfm.ui.intercom.main.chat.fragment.ChatFragment;
import com.wotingfm.ui.intercom.main.contacts.adapter.ContactsAdapter;
import com.wotingfm.ui.intercom.main.contacts.adapter.NoAdapter;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;
import com.wotingfm.ui.intercom.person.personmessage.PersonMessageFragment;

import java.util.List;


/**
 * 查询本地好友
 * 作者：xinLong on 2017/6/5 01:30
 * 邮箱：645700751@qq.com
 */
public class SearchContactsForLocalFragment extends Fragment {
    private View rootView;
    private FragmentActivity context;
    private ListView listViewG, listViewP;
    private View headView;
    private TextView tv_clear, tv_newsP, tv_newsG;
    private EditText et_search;
    private ImageView img_search;
    private SearchContactsForLocalPresenter presenter;
    private GroupsAdapter gAdapter;
    private ContactsAdapter pAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_search_local, container, false);
            initViews();// 设置界面
            setEditListener();
            presenter = new SearchContactsForLocalPresenter(this);
            presenter.getFriends();
        }
        return rootView;
    }

    // 初始化视图
    private void initViews() {
        listViewG = (ListView) rootView.findViewById(R.id.listView);

        tv_clear = (TextView) rootView.findViewById(R.id.tv_clear);
        et_search = (EditText) rootView.findViewById(R.id.et_search);
        img_search = (ImageView) rootView.findViewById(R.id.img_search);

        headView = LayoutInflater.from(context).inflate(R.layout.headview_search_local, null);// 头部 view
        tv_newsP = (TextView) headView.findViewById(R.id.tv_newsP);
        tv_newsG = (TextView) headView.findViewById(R.id.tv_newsG);
        listViewP = (ListView) headView.findViewById(R.id.head_listView);

        listViewG.addHeaderView(headView);// 添加头部 view
    }

    // 根据输入框输入值的改变来过滤搜索
    private void setEditListener() {
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String search_name = s.toString();
                if (search_name.trim().equals("")) {
                    presenter.search("");
                } else {// 关键词不为空
                    presenter.search(search_name);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    // 此时没有数据
    public void setView() {

    }

    // 此时个人有数据
    public void setViewForPerson(List<Contact.user> person) {
        if (pAdapter == null) {
            pAdapter = new ContactsAdapter(context, person);
            listViewP.setAdapter(pAdapter);
        } else {
            pAdapter.ChangeDate(person);
        }
        new HeightListView(context).setListViewHeightBasedOnChildren(listViewP);
        setListViewListener(person);
        NoAdapter adapters = new NoAdapter(context);
        listViewG.setAdapter(adapters);

    }

    // 此时群组有数据
    public void setViewForGroup(List<Contact.group> group) {
        if (gAdapter == null) {
            gAdapter = new GroupsAdapter(context, group);
            listViewG.setAdapter(gAdapter);
        } else {
            gAdapter.ChangeDate(group);
        }
        setGroupListViewListener(group);
    }

    // 此时群组、个人都有数据
    public void setViewForAll(List<Contact.user> person, List<Contact.group> group) {
        if (pAdapter == null) {
            pAdapter = new ContactsAdapter(context, person);
            listViewP.setAdapter(pAdapter);
        } else {
            pAdapter.ChangeDate(person);
        }
        new HeightListView(context).setListViewHeightBasedOnChildren(listViewP);
        setListViewListener(person);
        gAdapter = new GroupsAdapter(context, group);
        listViewG.setAdapter(gAdapter);
        setGroupListViewListener(group);
    }

    // 原始数据界面
    public void setViewForOnce(List<Contact.user> person, List<Contact.group> group) {

        if (person != null && person.size() != 0) {
            if (pAdapter == null) {
                pAdapter = new ContactsAdapter(context, person);
                listViewP.setAdapter(pAdapter);
            } else {
                pAdapter.ChangeDate(person);
            }
            new HeightListView(context).setListViewHeightBasedOnChildren(listViewP);
            setListViewListener(person);
            listViewP.setVisibility(View.VISIBLE);
        } else {
            listViewP.setVisibility(View.GONE);
        }
        if (group == null || group.size() == 0) {
            NoAdapter adapters = new NoAdapter(context);
            listViewG.setAdapter(adapters);
        } else {
            gAdapter = new GroupsAdapter(context, group);
            listViewG.setAdapter(gAdapter);
            setGroupListViewListener(group);

        }
    }

    // listView 的监听
    private void setListViewListener(List<Contact.user> person) {
        pAdapter.setOnListener(new ContactsAdapter.OnListener() {
            @Override
            public void add(int position) {
//                call(id);
            }
        });

        listViewP.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 跳转到好友信息界面
                InterPhoneActivity.open(new PersonMessageFragment());
            }
        });

    }

    // 组 listView 监听
    private void setGroupListViewListener(List<Contact.group> group) {
        gAdapter.setOnListener(new GroupsAdapter.OnListener() {
            @Override
            public void add(int position) {
            }
        });

        listViewG.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 跳转到群组详情页面
                InterPhoneActivity.open(new GroupNewsForAddFragment());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (rootView != null) {
            ((ViewGroup) rootView.getParent()).removeView(rootView);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
