package com.wotingfm.ui.intercom.add.search.net.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.woting.commonplat.widget.HeightListView;
import com.wotingfm.R;
import com.wotingfm.ui.intercom.add.search.local.adapter.GroupsAdapter;
import com.wotingfm.ui.intercom.add.search.net.adapter.SearchContactsAdapter;
import com.wotingfm.ui.intercom.add.search.net.presenter.SearchContactsForNetPresenter;
import com.wotingfm.ui.intercom.group.groupnews.GroupNewsForAddFragment;
import com.wotingfm.ui.intercom.main.contacts.adapter.ContactsAdapter;
import com.wotingfm.ui.intercom.main.contacts.adapter.NoAdapter;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;
import com.wotingfm.ui.intercom.person.personmessage.PersonMessageFragment;

import java.util.List;

/**
 * 查询新的朋友
 * 作者：xinLong on 2017/6/5 01:30
 * 邮箱：645700751@qq.com
 */
public class SearchContactsForNetFragment extends Fragment implements View.OnClickListener{
    private View rootView;
    private FragmentActivity context;
    private LinearLayout lin_pos,lin_search;
    private ListView lv_pos,listViewG,listViewP;
    private TextView tv_clear,tv_newsP,tv_newsG;
    private EditText et_search;
    private ImageView img_search;
    private SearchContactsForNetPresenter presenter;
    private ContactsAdapter pAdapter;
    private GroupsAdapter gAdapter;
    private LinearLayout lin_background;
    private TextView tv_ts;// 提示性界面，临时使用，需替换
    private SearchContactsAdapter searchContactsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_search_net, container, false);
        rootView.setOnClickListener(this);
        initViews();// 设置界面
        setEditListener();
        presenter = new SearchContactsForNetPresenter(this);
        presenter.getFriends();
        return rootView;
    }

    // 初始化视图
    private void initViews() {
        lin_background = (LinearLayout) rootView.findViewById(R.id.lin_background);
        tv_ts = (TextView) rootView.findViewById(R.id.tv_ts);

        lin_pos = (LinearLayout) rootView.findViewById(R.id.lin_pos);
        lv_pos = (ListView) rootView.findViewById(R.id.lv_pos);

        lin_search = (LinearLayout) rootView.findViewById(R.id.lin_search);
        listViewG = (ListView) rootView.findViewById(R.id.lv_search);

        tv_clear = (TextView) rootView.findViewById(R.id.tv_clear);
        tv_clear.setOnClickListener(this);
        et_search = (EditText) rootView.findViewById(R.id.et_search);
        img_search = (ImageView) rootView.findViewById(R.id.img_search);

        View headView = LayoutInflater.from(context).inflate(R.layout.headview_search_local, null);// 头部 view
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

    // 第一次进入时候的界面展示
    public void setViewOne() {
        lin_background.setVisibility(View.VISIBLE);
        tv_ts.setText("请输入您想要搜索的关键词");
        lin_pos.setVisibility(View.GONE);
        lin_search.setVisibility(View.GONE);
    }

    // 此时没有数据
    public void setView() {
        lin_background.setVisibility(View.VISIBLE);
        tv_ts.setText("暂没有数据");
        lin_pos.setVisibility(View.GONE);
        lin_search.setVisibility(View.GONE);
    }

    // 设置可能认识的人没有数据的界面
    public void setViewForPosNoData() {
        lin_background.setVisibility(View.VISIBLE);
        tv_ts.setText("请输入您想要搜索的关键词");
        lin_pos.setVisibility(View.GONE);
        lin_search.setVisibility(View.GONE);
    }

    // 设置可能认识的人有数据的界面
    public void setViewForPos(List<Contact.user> list) {
        lin_background.setVisibility(View.GONE);
        lin_pos.setVisibility(View.VISIBLE);
        lin_search.setVisibility(View.GONE);
        if (searchContactsAdapter == null) {
            searchContactsAdapter = new SearchContactsAdapter(context, list);
            lv_pos.setAdapter(searchContactsAdapter);
        } else {
            searchContactsAdapter.ChangeDate(list);
        }
    }

    // 此时个人有数据
    public void setViewForPerson(List<Contact.user> person) {
        lin_background.setVisibility(View.GONE);
        lin_pos.setVisibility(View.GONE);
        lin_search.setVisibility(View.VISIBLE);
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
        lin_background.setVisibility(View.GONE);
        lin_pos.setVisibility(View.GONE);
        lin_search.setVisibility(View.VISIBLE);
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
        lin_background.setVisibility(View.GONE);
        lin_pos.setVisibility(View.GONE);
        lin_search.setVisibility(View.VISIBLE);
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
        switch (v.getId()){
            case R.id.tv_clear:
                InterPhoneActivity.close();
                break;
        }
    }
}
