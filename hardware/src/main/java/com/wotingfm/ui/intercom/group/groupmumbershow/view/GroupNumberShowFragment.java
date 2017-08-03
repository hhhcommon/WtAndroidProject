package com.wotingfm.ui.intercom.group.groupmumbershow.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.woting.commonplat.widget.TipView;
import com.wotingfm.R;
import com.wotingfm.ui.intercom.group.groupmumbershow.adapter.GroupNumberShowAdapter;
import com.wotingfm.ui.intercom.group.groupmumbershow.presenter.GroupNumberShowPresenter;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;

import java.util.List;

/**
 * 展示组成员
 * 作者：xinLong on 2017/6/5 01:30
 * 邮箱：645700751@qq.com
 */
public class GroupNumberShowFragment extends Fragment implements View.OnClickListener {
    private View rootView;
    private ListView listView;
    private GroupNumberShowAdapter adapter;
    private GroupNumberShowPresenter presenter;
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
            presenter = new GroupNumberShowPresenter(this);
            presenter.getData();
        }
        return rootView;
    }

    // 设置界面
    private void inItView() {
        rootView.findViewById(R.id.head_left_btn).setOnClickListener(this);
        tip_view = (TipView) rootView.findViewById(R.id.tip_view);// 提示界面
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
            adapter = new GroupNumberShowAdapter(this.getActivity(), list);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("界面","执行销毁");
        presenter.destroy();
        presenter=null;
    }
}
