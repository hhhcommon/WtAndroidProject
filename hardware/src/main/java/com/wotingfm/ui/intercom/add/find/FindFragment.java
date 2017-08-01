package com.wotingfm.ui.intercom.add.find;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wotingfm.R;
import com.wotingfm.ui.intercom.add.search.net.view.SearchContactsForNetFragment;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;
import com.wotingfm.ui.intercom.scanning.activity.CaptureFragment;

/**
 * 查找群组与好友（已完成）
 * 作者：xinLong on 2017/6/5 01:30
 * 邮箱：645700751@qq.com
 */
public class FindFragment extends Fragment implements View.OnClickListener {
    private View rootView;
    private String fromType = "group";// 界面跳转来源 添加群组=group，添加好友=friend

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            fromType = getArguments().getString("type");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_find, container, false);
            rootView.setOnClickListener(this);
            initViews();// 设置界面
        }
        return rootView;
    }

    // 初始化视图
    private void initViews() {
        rootView.findViewById(R.id.lin_search).setOnClickListener(this);
        rootView.findViewById(R.id.re_sao).setOnClickListener(this);
        rootView.findViewById(R.id.head_left_btn).setOnClickListener(this);
        TextView tv_center = (TextView) rootView.findViewById(R.id.tv_center);
        // 根据界面来源设置界面展示
        if (fromType != null && !fromType.trim().equals("") && fromType.equals("friend")) {
            tv_center.setText("添加好友");
        } else {
            tv_center.setText("加入群组");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left_btn:
                InterPhoneActivity.close();
                break;
            case R.id.re_sao:
                InterPhoneActivity.open(new CaptureFragment());
                break;
            case R.id.lin_search:
                // 根据界面来源设置界面展示
                if (fromType != null && !fromType.trim().equals("") && fromType.equals("friend")) {
                    SearchContactsForNetFragment fragment = new SearchContactsForNetFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("type", "friend");
                    fragment.setArguments(bundle);
                    InterPhoneActivity.open(fragment);
                } else {
                    SearchContactsForNetFragment fragment = new SearchContactsForNetFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("type", "group");
                    fragment.setArguments(bundle);
                    InterPhoneActivity.open(fragment);
                }
                break;
        }
    }

}
