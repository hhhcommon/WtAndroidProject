package com.wotingfm.ui.intercom.group.groupapply;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.wotingfm.R;
import com.wotingfm.ui.intercom.group.groupapply.view.GroupApplyForNewsFragment;
import com.wotingfm.ui.intercom.group.groupapply.view.GroupApplyForPasswordFragment;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;

/**
 * 入组申请界面
 * 作者：xinLong on 2017/6/5 01:30
 * 邮箱：645700751@qq.com
 */
public class GroupApplyFragment extends Fragment implements View.OnClickListener {
    private View rootView;
    private String type, gid;//  0密码群，1审核群，2密码审核群
    private RelativeLayout re_news, re_password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_apply, container, false);
            rootView.setOnClickListener(this);
            inItView();
            getData();
            setView();
        }
        return rootView;
    }

    // 设置界面
    private void inItView() {
        rootView.findViewById(R.id.head_left_btn).setOnClickListener(this);
        re_news = (RelativeLayout) rootView.findViewById(R.id.re_news);
        re_news.setOnClickListener(this);
        re_password = (RelativeLayout) rootView.findViewById(R.id.re_password);
        re_password.setOnClickListener(this);
    }

    // 获取上级界面传递数据
    private void getData() {
        try {
            // 群id
            gid = this.getArguments().getString("gid");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            // 加群类型
            type = this.getArguments().getString("type");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 根据加群类型设置界面
    private void setView() {
        if (type != null && !type.equals("")) {
            if (type.equals("0")) {
                ViewShowNews();
            } else if (type.equals("1")) {
                ViewShowMM();
            } else if (type.equals("2")) {
                ViewShowAll();
            } else {
                ViewShowAll();
            }
        } else {
            ViewShowAll();
        }
    }

    // 设置审核申请样式
    private void ViewShowNews() {
        re_news.setVisibility(View.VISIBLE);
        re_password.setVisibility(View.GONE);
    }

    // 设置密码申请样式
    private void ViewShowMM() {
        re_news.setVisibility(View.GONE);
        re_password.setVisibility(View.VISIBLE);
    }

    // 设置全部申请样式
    private void ViewShowAll() {
        re_news.setVisibility(View.VISIBLE);
        re_password.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left_btn:
                InterPhoneActivity.close();
                break;
            case R.id.re_news:
                // 跳转到申请加群消息界面
                if (gid != null && !gid.equals("")) {
                    GroupApplyForNewsFragment fragment = new GroupApplyForNewsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("gid", gid);
                    fragment.setArguments(bundle);
                    InterPhoneActivity.open(fragment);
                }
                break;
            case R.id.re_password:
                // 跳转到密码加群消息界面
                if (gid != null && !gid.equals("")) {
                    GroupApplyForPasswordFragment fragment = new GroupApplyForPasswordFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("gid", gid);
                    fragment.setArguments(bundle);
                    InterPhoneActivity.open(fragment);
                }
                break;
        }
    }

}
