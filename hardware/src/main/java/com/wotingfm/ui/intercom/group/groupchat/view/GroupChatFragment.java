package com.wotingfm.ui.intercom.group.groupchat.view;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.woting.commonplat.widget.TipView;
import com.wotingfm.R;
import com.wotingfm.common.utils.DialogUtils;
import com.wotingfm.ui.intercom.group.groupchat.adapter.GroupChatAdapter;
import com.wotingfm.ui.intercom.group.groupchat.model.GroupChat;
import com.wotingfm.ui.intercom.group.groupchat.presenter.GroupChatPresenter;
import com.wotingfm.ui.intercom.group.groupnews.noadd.view.GroupNewsForNoAddFragment;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;

import java.util.List;

/**
 * 群聊，包含我创建的群组
 * 作者：xinLong on 2017/6/5 01:30
 * 邮箱：645700751@qq.com
 */
public class GroupChatFragment extends Fragment implements View.OnClickListener,TipView.TipViewClick {
    private View rootView;
    private ExpandableListView spListView;
    private GroupChatPresenter presenter;
    private FragmentActivity context;
    private GroupChatAdapter adapter;
    private TipView tip_view;
    private int type;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_mychat, container, false);
            rootView.setOnClickListener(this);
            inItView();
            presenter = new GroupChatPresenter(this);
            presenter.getData();
        }
        return rootView;
    }

    // 设置界面
    private void inItView() {
        tip_view = (TipView) rootView.findViewById(R.id.tip_view);// 提示界面
        rootView.findViewById(R.id.head_left_btn).setOnClickListener(this);
        TextView tv_center = (TextView) rootView.findViewById(R.id.tv_center);
        tv_center.setText("群聊");
        spListView = (ExpandableListView) rootView.findViewById(R.id.expandableListView);
        spListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });

        spListView.setGroupIndicator(null);
        spListView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        setListener();
    }

    private void setListener() {
        spListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                InterPhoneActivity.open(new GroupNewsForNoAddFragment());
                return false;
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
            case R.id.head_left_btn:
                InterPhoneActivity.close();
                break;
        }
    }

    public void setView(List<GroupChat> list){
        adapter = new GroupChatAdapter(context, list);
        spListView.setAdapter(adapter);

        for (int i = 0; i < list.size(); i++) {
            spListView.expandGroup(i);
        }
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
            spListView.setVisibility(View.VISIBLE);
            tip_view.setVisibility(View.GONE);
        } else if (type == 1) {
            // 已经登录，没有数据
            spListView.setVisibility(View.GONE);
            tip_view.setVisibility(View.VISIBLE);
            tip_view.setTipView(TipView.TipStatus.NO_DATA, "您还没有聊天对象哟\n快去找好友们聊天吧");
        } else if (type == 2) {
            // 没有网络
            spListView.setVisibility(View.GONE);
            tip_view.setVisibility(View.VISIBLE);
            tip_view.setTipView(TipView.TipStatus.NO_NET);
        } else if (type == 3) {
            // 没有登录
            spListView.setVisibility(View.GONE);
            tip_view.setVisibility(View.VISIBLE);
            tip_view.setTipView(TipView.TipStatus.NO_LOGIN);
        } else if (type == 4) {
            // 已经登录，数据加载失败
            spListView.setVisibility(View.GONE);
            tip_view.setVisibility(View.VISIBLE);
            tip_view.setTipView(TipView.TipStatus.IS_ERROR);
        }
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
