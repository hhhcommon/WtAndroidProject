package com.wotingfm.ui.intercom.add.search.net.view;

import android.app.Dialog;
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
import com.woting.commonplat.widget.TipView;
import com.wotingfm.R;
import com.wotingfm.common.utils.DialogUtils;
import com.wotingfm.ui.intercom.add.search.local.adapter.GroupsAdapter;
import com.wotingfm.ui.intercom.add.search.net.adapter.SearchContactsForGroupAdapter;
import com.wotingfm.ui.intercom.add.search.net.adapter.SearchContactsForUserAdapter;
import com.wotingfm.ui.intercom.add.search.net.presenter.SearchContactsForNetPresenter;
import com.wotingfm.ui.intercom.group.groupnews.add.view.GroupNewsForAddFragment;
import com.wotingfm.ui.intercom.group.groupnews.noadd.view.GroupNewsForNoAddFragment;
import com.wotingfm.ui.intercom.main.contacts.adapter.ContactsAdapter;
import com.wotingfm.ui.intercom.main.contacts.adapter.NoAdapter;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;
import com.wotingfm.ui.intercom.person.personmessage.view.PersonMessageFragment;

import java.util.List;

/**
 * 查询新的朋友
 * 作者：xinLong on 2017/6/5 01:30
 * 邮箱：645700751@qq.com
 */
public class SearchContactsForNetFragment extends Fragment implements View.OnClickListener, TipView.TipViewClick {
    private View rootView;
    private FragmentActivity context;
    private LinearLayout lin_pos, lin_search;
    private ListView lv_pos, listView;
    private TextView tv_clear;
    private EditText et_search;
    private ImageView img_search;
    private Dialog dialog;
    private TipView tip_view;
    private int type;
    private SearchContactsForNetPresenter presenter;
    private SearchContactsForGroupAdapter searchContactsForGroupAdapter;
    private SearchContactsForUserAdapter searchContactsForUserAdapter;
    private SearchContactsForGroupAdapter gAdapter;
    private SearchContactsForUserAdapter pAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_search_net, container, false);
            rootView.setOnClickListener(this);
            initViews();// 设置界面
            setEditListener();
            presenter = new SearchContactsForNetPresenter(this);
            presenter.getRecommendedData();// 获取推荐数据
        }
        return rootView;
    }

    // 初始化视图
    private void initViews() {
        tip_view = (TipView) rootView.findViewById(R.id.tip_view);// 提示界面
        tip_view.setTipClick(this);
        lin_pos = (LinearLayout) rootView.findViewById(R.id.lin_pos);
        lv_pos = (ListView) rootView.findViewById(R.id.lv_pos);
        lin_search = (LinearLayout) rootView.findViewById(R.id.lin_search);
        listView = (ListView) rootView.findViewById(R.id.lv_search);
        tv_clear = (TextView) rootView.findViewById(R.id.tv_clear);
        tv_clear.setOnClickListener(this);
        et_search = (EditText) rootView.findViewById(R.id.et_search);
        img_search = (ImageView) rootView.findViewById(R.id.img_search);
    }

    // 根据输入框输入值的改变来过滤搜索
    private void setEditListener() {
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String search_name = s.toString();
                if (search_name.trim().equals("")) {
                    presenter.getRecommendedData();
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

    @Override
    public void onTipViewClick() {
        presenter.tipClick(type);
    }

    // 设置可能认识的人有数据的界面（个人）
    public void setViewForPosPerson(List<Contact.user> list) {
        if (searchContactsForUserAdapter == null) {
            searchContactsForUserAdapter = new SearchContactsForUserAdapter(context, list);
            lv_pos.setAdapter(searchContactsForUserAdapter);
        } else {
            searchContactsForUserAdapter.ChangeDate(list);
        }
        setPosPersonListViewListener(list);
    }

    // 设置可能认识的人有数据的界面（群组）
    public void setViewForPosGroup(List<Contact.group> list) {
        if (searchContactsForGroupAdapter == null) {
            searchContactsForGroupAdapter = new SearchContactsForGroupAdapter(context, list);
            lv_pos.setAdapter(searchContactsForGroupAdapter);
        } else {
            searchContactsForGroupAdapter.ChangeDate(list);
        }
        setPosGroupListViewListener(list);
    }

    // 设置个人有数据
    public void setViewForPerson(List<Contact.user> person) {
        if (pAdapter == null) {
            pAdapter = new SearchContactsForUserAdapter(context, person);
            listView.setAdapter(pAdapter);
        } else {
            pAdapter.ChangeDate(person);
        }
        setListViewListener(person);

    }

    // 设置群组有数据
    public void setViewForGroup(List<Contact.group> group) {
        if (gAdapter == null) {
            gAdapter = new SearchContactsForGroupAdapter(context, group);
            listView.setAdapter(gAdapter);
        } else {
            gAdapter.ChangeDate(group);
        }
        setGroupListViewListener(group);
    }

    // listView 的监听==推荐的好友
    private void setPosPersonListViewListener(final List<Contact.user> person) {
        lv_pos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 跳转到好友信息界面
                PersonMessageFragment fragment = new PersonMessageFragment();
                Bundle bundle = new Bundle();
                bundle.putString("type", "false");
                bundle.putString("id", person.get(position).getId());
                fragment.setArguments(bundle);
                InterPhoneActivity.open(fragment);
            }
        });
    }

    // 组 listView 监听==推荐的群组
    private void setPosGroupListViewListener(final List<Contact.group> group) {
        lv_pos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 跳转到群组详情页面
                GroupNewsForNoAddFragment fragment = new GroupNewsForNoAddFragment();
                Bundle bundle = new Bundle();
                bundle.putString("id", group.get(position).getId());
                fragment.setArguments(bundle);
                InterPhoneActivity.open(fragment);
            }
        });
    }

    // listView 的监听==搜索到的好友
    private void setListViewListener(final List<Contact.user> person) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 跳转到好友信息界面
                PersonMessageFragment fragment = new PersonMessageFragment();
                Bundle bundle = new Bundle();
                bundle.putString("type", "false");
                bundle.putString("id", person.get(position).getId());
                fragment.setArguments(bundle);
                InterPhoneActivity.open(fragment);
            }
        });

    }

    // 组 listView 监听==搜索到的群组
    private void setGroupListViewListener(final List<Contact.group> group) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 跳转到群组详情页面
                GroupNewsForNoAddFragment fragment = new GroupNewsForNoAddFragment();
                Bundle bundle = new Bundle();
                bundle.putString("id", group.get(position).getId());
                fragment.setArguments(bundle);
                InterPhoneActivity.open(fragment);
            }
        });
    }


    /**
     * 是否登录，是否有数据
     *
     * @param type 登录后数据类型
     *             -1 推荐有数据
     *             0 搜索有数据
     *             NO_DATA,没有数据 1
     *             NO_NET,没有网络 2
     *             NO_LOGIN,没有登录 3
     *             IS_ERROR,加载错误 4
     */
    public void isLoginView(int type) {
        this.type = type;
        if (type == -1) {
            // 已经登录，推荐有数据
            lin_pos.setVisibility(View.VISIBLE);
            lin_search.setVisibility(View.GONE);
            tip_view.setVisibility(View.GONE);
        } else if (type == 0) {
            // 已经登录，搜索有数据
            lin_pos.setVisibility(View.GONE);
            lin_search.setVisibility(View.VISIBLE);
            tip_view.setVisibility(View.GONE);
        } else if (type == 1) {
            // 已经登录，没有数据
            lin_pos.setVisibility(View.GONE);
            lin_search.setVisibility(View.GONE);
            tip_view.setVisibility(View.VISIBLE);
            tip_view.setTipView(TipView.TipStatus.NO_DATA, "搜索词不要太逆天呦");
        } else if (type == 2) {
            // 没有网络
            lin_pos.setVisibility(View.GONE);
            lin_search.setVisibility(View.GONE);
            tip_view.setVisibility(View.VISIBLE);
            tip_view.setTipView(TipView.TipStatus.NO_NET);
        } else if (type == 3) {
            // 没有登录
            lin_pos.setVisibility(View.GONE);
            lin_search.setVisibility(View.GONE);
            tip_view.setVisibility(View.VISIBLE);
            tip_view.setTipView(TipView.TipStatus.NO_LOGIN);
        } else if (type == 4) {
            // 已经登录，数据加载失败
            lin_pos.setVisibility(View.GONE);
            lin_search.setVisibility(View.GONE);
            tip_view.setVisibility(View.VISIBLE);
            tip_view.setTipView(TipView.TipStatus.IS_ERROR);
        }
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_clear:
                InterPhoneActivity.close();
                break;
        }
    }
}
