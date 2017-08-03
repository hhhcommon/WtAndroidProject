package com.wotingfm.ui.intercom.group.groupmumberadd.view;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.woting.commonplat.widget.TipView;
import com.wotingfm.R;
import com.wotingfm.common.utils.DialogUtils;
import com.wotingfm.ui.intercom.group.groupmumberadd.adapter.GroupNumberAddAdapter;
import com.wotingfm.ui.intercom.group.groupmumberadd.presenter.GroupNumberAddPresenter;
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
    private TipView tip_view;
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
        tip_view = (TipView) rootView.findViewById(R.id.tip_view);// 提示界面

        rootView.findViewById(R.id.head_left_btn).setOnClickListener(this);
        TextView tv_send = (TextView) rootView.findViewById(R.id.tv_send);
        tv_send.setVisibility(View.VISIBLE);
        tv_send.setOnClickListener(this);
        TextView tv_center = (TextView) rootView.findViewById(R.id.tv_center);
        tv_center.setText("邀请新成员");
        listView = (ListView) rootView.findViewById(R.id.lv);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left_btn:
                InterPhoneActivity.close();
                break;
            case R.id.tv_send:
                presenter.apply();
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
        if (type == 0) {
            // 已经登录，并且有数据
            listView.setVisibility(View.VISIBLE);
            tip_view.setVisibility(View.GONE);
        } else if (type == 1) {
            // 已经登录，没有数据
            listView.setVisibility(View.GONE);
            tip_view.setVisibility(View.VISIBLE);
            tip_view.setTipView(TipView.TipStatus.NO_DATA);
        } else if (type == 2) {
            // 没有网络
            listView.setVisibility(View.GONE);
            tip_view.setVisibility(View.VISIBLE);
            tip_view.setTipView(TipView.TipStatus.NO_NET);
        } else if (type == 3) {
            // 没有登录
            listView.setVisibility(View.GONE);
            tip_view.setVisibility(View.VISIBLE);
            tip_view.setTipView(TipView.TipStatus.NO_LOGIN);
        } else if (type == 4) {
            // 已经登录，数据加载失败
            listView.setVisibility(View.GONE);
            tip_view.setVisibility(View.VISIBLE);
            tip_view.setTipView(TipView.TipStatus.IS_ERROR);
        }
    }
}
