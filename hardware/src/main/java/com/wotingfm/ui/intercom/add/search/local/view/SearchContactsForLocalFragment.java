package com.wotingfm.ui.intercom.add.search.local.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.woting.commonplat.widget.HeightListView;
import com.wotingfm.R;
import com.wotingfm.ui.intercom.add.search.local.adapter.GroupsAdapter;
import com.wotingfm.ui.intercom.add.search.local.presenter.SearchContactsForLocalPresenter;
import com.wotingfm.ui.intercom.main.contacts.adapter.ContactsAdapter;
import com.wotingfm.ui.intercom.main.contacts.adapter.NoAdapter;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;

import java.util.List;


/**
 * 查询本地好友
 * 作者：xinLong on 2017/6/5 01:30
 * 邮箱：645700751@qq.com
 */
public class SearchContactsForLocalFragment extends Fragment implements View.OnClickListener {
    private View rootView;
    private FragmentActivity context;
    private ListView listViewG, listViewP;
    private View headView;
    private TextView tv_clear, tv_newsP, tv_newsG, tv_group_bg;
    private EditText et_search;
    private ImageView img_search;
    private SearchContactsForLocalPresenter presenter;
    private GroupsAdapter gAdapter;
    private ContactsAdapter pAdapter;
    private LinearLayout lin_background;
    private TextView tv_ts;// 提示性界面，临时使用，需替换
    private Dialog confirmDialog;
    private int type;
    private Contact.user user;
    private Contact.group group;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_search_local, container, false);
            rootView.setOnClickListener(this);
            initViews();// 设置界面
            setEditListener();
            setViewOne();
            Dialog();
            presenter = new SearchContactsForLocalPresenter(this);
            presenter.getFriends();
        }
        return rootView;
    }

    // 初始化视图
    private void initViews() {
        lin_background = (LinearLayout) rootView.findViewById(R.id.lin_background);
        tv_ts = (TextView) rootView.findViewById(R.id.tv_ts);
        listViewG = (ListView) rootView.findViewById(R.id.listView);

        tv_clear = (TextView) rootView.findViewById(R.id.tv_clear);
        tv_clear.setOnClickListener(this);
        et_search = (EditText) rootView.findViewById(R.id.et_search);
        et_search.requestFocus();
        InputMethodManager imm = (InputMethodManager) et_search.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
        img_search = (ImageView) rootView.findViewById(R.id.img_search);

        headView = LayoutInflater.from(context).inflate(R.layout.headview_search_local, null);// 头部 view
        tv_newsP = (TextView) headView.findViewById(R.id.tv_newsP);
        tv_group_bg = (TextView) headView.findViewById(R.id.tv_group_bg);
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
        listViewG.setVisibility(View.GONE);
    }

    // 此时没有数据
    public void setView() {
        lin_background.setVisibility(View.VISIBLE);
        tv_ts.setText("暂没有数据");
        listViewG.setVisibility(View.GONE);
    }

    // 此时个人有数据
    public void setViewForPerson(List<Contact.user> person) {
        tv_newsP.setVisibility(View.VISIBLE);
        tv_newsG.setVisibility(View.GONE);
        lin_background.setVisibility(View.GONE);
        tv_group_bg.setVisibility(View.GONE);
        listViewP.setVisibility(View.VISIBLE);
        listViewG.setVisibility(View.VISIBLE);
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
        tv_newsP.setVisibility(View.GONE);
        tv_newsG.setVisibility(View.VISIBLE);
        lin_background.setVisibility(View.GONE);
        tv_group_bg.setVisibility(View.GONE);
        listViewP.setVisibility(View.GONE);
        listViewG.setVisibility(View.VISIBLE);
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
        tv_newsP.setVisibility(View.VISIBLE);
        tv_newsG.setVisibility(View.VISIBLE);
        lin_background.setVisibility(View.GONE);
        tv_group_bg.setVisibility(View.VISIBLE);
        listViewP.setVisibility(View.VISIBLE);
        listViewG.setVisibility(View.VISIBLE);
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
    private void setListViewListener(final List<Contact.user> person) {
        pAdapter.setOnListener(new ContactsAdapter.OnListener() {
            @Override
            public void add(int position) {
                presenter.call(person, position);
            }
        });

        listViewP.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 跳转到好友信息界面
                presenter.jumpForPerson(person, position);
            }
        });
    }

    // 组 listView 监听
    private void setGroupListViewListener(final List<Contact.group> group) {
        gAdapter.setOnListener(new GroupsAdapter.OnListener() {
            @Override
            public void add(int position) {
                presenter.interPhone(group, position);
            }
        });

        listViewG.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 跳转到群组详情页面,需要-1
                presenter.jumpForGroup(group, position - 1);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_clear:
                InputMethodManager m = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                m .hideSoftInputFromWindow(et_search.getWindowToken(), 0);//比如EditView
                InterPhoneActivity.close();
                break;
        }
    }

    // 呼叫弹出框
    private void Dialog() {
        final View dialog1 = LayoutInflater.from(this.getActivity()).inflate(R.layout.dialog_talk_person_del, null);
        TextView tv_cancel = (TextView) dialog1.findViewById(R.id.tv_cancle);
        TextView tv_confirm = (TextView) dialog1.findViewById(R.id.tv_confirm);
        confirmDialog = new Dialog(this.getActivity(), R.style.MyDialogs);
        confirmDialog.setContentView(dialog1);
        confirmDialog.setCanceledOnTouchOutside(true);
        confirmDialog.getWindow().setBackgroundDrawableResource(R.color.transparent_background);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCancel();
            }
        });

        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.callOk(user, group, type);
                dialogCancel();
            }
        });
    }

    /**
     * 展示弹出框
     */
    public void dialogShow(Contact.user user, Contact.group group, int type) {
        this.user = user;
        this.group = group;
        this.type = type;
        confirmDialog.show();
    }

    /**
     * 取消弹出框
     */
    public void dialogCancel() {
        confirmDialog.dismiss();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("界面", "执行销毁");
        presenter.destroy();
        presenter = null;
    }

}
