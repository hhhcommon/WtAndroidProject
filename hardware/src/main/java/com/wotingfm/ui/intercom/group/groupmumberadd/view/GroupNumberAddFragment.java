package com.wotingfm.ui.intercom.group.groupmumberadd.view;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.wotingfm.R;
import com.wotingfm.common.utils.DialogUtils;
import com.wotingfm.ui.intercom.group.groupmumberadd.adapter.GroupNumberAddAdapter;
import com.wotingfm.ui.intercom.group.groupmumberadd.presenter.GroupNumberAddPresenter;
import com.wotingfm.ui.intercom.group.groupmumbershow.adapter.GroupNumberShowAdapter;
import com.wotingfm.ui.intercom.group.groupmumbershow.presenter.GroupNumberShowPresenter;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;

import java.util.List;

/**
 * 添加组成员
 * 作者：xinLong on 2017/6/5 01:30
 * 邮箱：645700751@qq.com
 */
public class GroupNumberAddFragment extends Fragment implements View.OnClickListener {
    private View rootView;
    private ListView listView;
    private GroupNumberAddPresenter presenter;
    private GroupNumberAddAdapter adapter;
    private Dialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_groupmumbers, container, false);
            rootView.setOnClickListener(this);
            inItView();
            presenter = new GroupNumberAddPresenter(this);
            presenter.getData();
        }
        return rootView;
    }

    // 设置界面
    private void inItView() {
        rootView.findViewById(R.id.head_left_btn).setOnClickListener(this);
        TextView tv_center = (TextView) rootView.findViewById(R.id.tv_center);
        tv_center.setText("添加群成员");
        listView = (ListView) rootView.findViewById(R.id.lv);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left_btn:
                InterPhoneActivity.close();
                break;
        }
    }

    /**
     * 设置界面
     *
     * @param list
     */
    public void setView(List<Contact.user> list) {
        if (adapter == null) {
            adapter = new GroupNumberAddAdapter(this.getActivity(), list);
            listView.setAdapter(adapter);
        } else {
            adapter.ChangeDate(list);
        }
        setOnItemClickListener();
    }

    private void setOnItemClickListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                presenter.showPersonNews(position);
            }
        });

        adapter.setOnListener(new GroupNumberAddAdapter.OnListener() {
            @Override
            public void apply(int position) {
                presenter.apply(position);
            }
        });
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

}
