package com.wotingfm.ui.intercom.group.setmanager.view;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.wotingfm.R;
import com.wotingfm.common.utils.DialogUtils;
import com.wotingfm.ui.intercom.group.applygrouptype.view.ApplyGroupTypeFragment;
import com.wotingfm.ui.intercom.group.editgroupmessage.view.EditGroupMessageFragment;
import com.wotingfm.ui.intercom.group.setmanager.adapter.SetManagerAdapter;
import com.wotingfm.ui.intercom.group.setmanager.presenter.SetManagerPresenter;
import com.wotingfm.ui.intercom.group.standbychannel.view.StandbyChannelFragment;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;

import java.util.List;

/**
 * 设置管理员界面
 * 作者：xinLong on 2017/6/5 01:30
 * 邮箱：645700751@qq.com
 */
public class SetManagerFragment extends Fragment implements View.OnClickListener {
    private View rootView;
    private SetManagerPresenter presenter;
    private ListView listView;
    private SetManagerAdapter adapter;
    private Dialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_groupsetmanager, container, false);
            rootView.setOnClickListener(this);
            inItView();
            presenter = new SetManagerPresenter(this);
            presenter.getData();
        }
        return rootView;
    }

    private void inItView() {
        rootView.findViewById(R.id.head_left_btn).setOnClickListener(this);
        rootView.findViewById(R.id.tv_send).setOnClickListener(this);
        listView = (ListView) rootView.findViewById(R.id.lv_manager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left_btn:
                InterPhoneActivity.close();
                break;
            case R.id.tv_send:
                presenter.send();
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
            adapter = new SetManagerAdapter(this.getActivity(), list);
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
                presenter.changeData(position);
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
