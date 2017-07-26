package com.wotingfm.ui.intercom.group.groupchat.view;

import android.app.Dialog;
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
public class GroupChatFragment extends Fragment implements View.OnClickListener, TipView.TipViewClick {
    private View rootView;
    private ExpandableListView spListView;
    private GroupChatPresenter presenter;
    private GroupChatAdapter adapter;
    private TipView tip_view;
    private int type;
    private Dialog confirmDialog;
    private String id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_mychat, container, false);
            rootView.setOnClickListener(this);
            inItView();
            Dialog();
            presenter = new GroupChatPresenter(this);
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
                if (parent.isGroupExpanded(groupPosition)) {
                    parent.collapseGroup(groupPosition);
                } else {
                    //第二个参数false表示展开时是否触发默认滚动动画
                    parent.expandGroup(groupPosition, false);
                }
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
                presenter.jump(groupPosition, childPosition);
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

    /**
     * 适配数据
     * @param list
     */
    public void setView(List<GroupChat> list) {
        if (adapter == null) {
            adapter = new GroupChatAdapter(this.getActivity(), list);
            spListView.setAdapter(adapter);
        } else {
            adapter.changeData(list);
        }
        for (int i = 0; i < list.size(); i++) {
            spListView.expandGroup(i);
        }
        onItemClick();
    }

    /**
     * 点击适配器
     */
    public void onItemClick() {
        adapter.setClickListener(new GroupChatAdapter.clickListener() {
            @Override
            public void onItemClick(int groupPosition, int childPosition) {
                presenter.interPhone(groupPosition, childPosition);
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
        this.type = type;
        if (type == 0) {
            // 已经登录，并且有数据
            spListView.setVisibility(View.VISIBLE);
            tip_view.setVisibility(View.GONE);
        } else if (type == 1) {
            // 已经登录，没有数据
            spListView.setVisibility(View.GONE);
            tip_view.setVisibility(View.VISIBLE);
            tip_view.setTipView(TipView.TipStatus.NO_DATA, "您还没有加入的群组哟\n快去创建吧");
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
                confirmDialog.dismiss();
            }
        });

        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.callOk(id);
                confirmDialog.dismiss();
            }
        });
    }

    /**
     * 展示弹出框
     */
    public void dialogShow(String id) {
        this.id = id;
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
        presenter.destroy();
    }
}
