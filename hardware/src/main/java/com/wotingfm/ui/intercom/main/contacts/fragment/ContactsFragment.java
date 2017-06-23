package com.wotingfm.ui.intercom.main.contacts.fragment;

import android.app.Dialog;
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

import com.woting.commonplat.widget.TipView;
import com.wotingfm.R;
import com.wotingfm.common.utils.DialogUtils;
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
public class ContactsFragment extends Fragment implements View.OnClickListener,TipView.TipViewClick {
    private View rootView;
    private FragmentActivity context;
    private ListView listView;
    private View headView;
    private TextView tv_search, tv_newGroupNum, tv_newFriendNum;
    private RelativeLayout re_newFriend, re_newGroup, re_view;
    private ContactsPresenter presenter;
    private ContactsAdapter adapter;
    private SideBar sideBar;
    private TextView tvDialog;
    private TipView tip_view;
    private int type;
    private Dialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_contacts, container, false);
            rootView.setOnClickListener(this);
            initViews();// 设置界面
            setEditListener();
            presenter = new ContactsPresenter(this);
            presenter.getData();// 获取数据
        }
        return rootView;
    }

    // 初始化视图
    private void initViews() {
        tip_view = (TipView) rootView.findViewById(R.id.tip_view);// 提示界面
        re_view = (RelativeLayout) rootView.findViewById(R.id.re_view);// 有数据的界面

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
        tip_view.setTipClick(this);
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
    public void onTipViewClick() {
        presenter.tipClick(type);
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
        if (adapter != null) {
            adapter.ChangeDate(list);
        } else {
            adapter = new ContactsAdapter(context, list);
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

    /**
     * 是否登录，是否有数据
     *
     * @param type 登录后数据类型
     *             0 正常有数据
     *             NO_DATA,没有数据 1
     *             NO_NET,没有网络 2
     *             NO_LOGIN,没有登录 3
     *             IS_ERROR,加载错误 4
     */
    public void isLoginView(int type) {
        this.type=type;
        if (type == 0) {
            // 已经登录，并且有数据
            re_view.setVisibility(View.VISIBLE);
            tip_view.setVisibility(View.GONE);
        } else if (type == 1) {
            // 已经登录，没有数据
            re_view.setVisibility(View.GONE);
            tip_view.setVisibility(View.VISIBLE);
            tip_view.setTipView(TipView.TipStatus.NO_DATA, "您还没有聊天对象哟\n快去找好友们聊天吧");
        } else if (type == 2) {
            // 没有网络
            re_view.setVisibility(View.GONE);
            tip_view.setVisibility(View.VISIBLE);
            tip_view.setTipView(TipView.TipStatus.NO_NET);
        } else if (type == 3) {
            // 没有登录
            re_view.setVisibility(View.GONE);
            tip_view.setVisibility(View.VISIBLE);
            tip_view.setTipView(TipView.TipStatus.NO_LOGIN);
        } else if (type == 4) {
            // 已经登录，数据加载失败
            re_view.setVisibility(View.GONE);
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


}