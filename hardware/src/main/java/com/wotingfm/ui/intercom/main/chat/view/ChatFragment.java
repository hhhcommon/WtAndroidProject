package com.wotingfm.ui.intercom.main.chat.view;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.woting.commonplat.widget.TipView;
import com.wotingfm.R;
import com.wotingfm.common.bean.MessageEvent;
import com.wotingfm.common.service.InterPhoneControl;
import com.wotingfm.common.view.RippleImageView;
import com.wotingfm.ui.intercom.main.chat.adapter.ChatAdapter;
import com.wotingfm.ui.intercom.main.chat.model.TalkHistory;
import com.wotingfm.ui.intercom.main.chat.presenter.ChatPresenter;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * 对讲机-获取联系列表，包括群组跟个人
 *
 * @author 辛龙
 *         2016年1月18日
 */
public class ChatFragment extends Fragment implements ChatAdapter.IonSlidingViewClickListener, View.OnClickListener, TipView.TipViewClick {

    private View rootView;
    private ChatPresenter presenter;
    private RecyclerView mRecyclerView;
    private ChatAdapter mAdapter;
    private ImageView img_url_group, img_person_group;
    private RelativeLayout re_group, re_person;
    private TextView tv_groupName_group, tv_news_group, tv_person_group, tv_person_person;
    private RippleImageView rippleImageView_person, rippleImageView_group;
    private LinearLayout lin_back;
    private TipView tip_view;
    private Dialog confirmDialog;
    private int type;
    private String new_id;
    private int callType;
    private String accId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_chat, container, false);
            rootView.setOnClickListener(this);
            inItView();
            Dialog();
            isLoginView(-1);
            presenter = new ChatPresenter(this);
            presenter.getData();
        }
        return rootView;
    }

    private void inItView() {
        tip_view = (TipView) rootView.findViewById(R.id.tip_view);// 提示界面
        tip_view.setTipClick(this);
        lin_back = (LinearLayout) rootView.findViewById(R.id.lin_back);
        // 组的界面
        re_group = (RelativeLayout) rootView.findViewById(R.id.re_group);
        re_group.setVisibility(View.GONE);
        rootView.findViewById(R.id.img_close_group).setOnClickListener(this);
        img_url_group = (ImageView) rootView.findViewById(R.id.img_url_group);
        img_url_group.setOnClickListener(this);
        tv_groupName_group = (TextView) rootView.findViewById(R.id.tv_groupName_group);
        tv_news_group = (TextView) rootView.findViewById(R.id.tv_news_group);
        tv_person_group = (TextView) rootView.findViewById(R.id.tv_person_group);
        img_person_group = (ImageView) rootView.findViewById(R.id.img_url_group);
        rippleImageView_group = (RippleImageView) rootView.findViewById(R.id.rippleImageView);

        // 用户的界面
        re_person = (RelativeLayout) rootView.findViewById(R.id.re_person);
        re_person.setVisibility(View.GONE);
        rootView.findViewById(R.id.img_close_person).setOnClickListener(this);
        tv_person_person = (TextView) rootView.findViewById(R.id.tv_person_person);
        rippleImageView_person = (RippleImageView) rootView.findViewById(R.id.rippleImageView_person);

        // 列表
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
    }

    @Override
    public void onTipViewClick() {
        presenter.tipClick(type);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_url_group:
                presenter.jump();
                break;
            case R.id.img_close_group:
                setGroupViewClose();
                EventBus.getDefault().post(new MessageEvent("exitGroup&" + ChatPresenter.data.getID()));
                break;
            case R.id.img_close_person:
                setPersonViewClose();
                InterPhoneControl.over(accId);// 结束通话
                break;
        }
    }

    /**
     * 数据适配
     *
     * @param list
     */
    public void updateUI(List<TalkHistory> list) {
        if (mAdapter == null) {
            mAdapter = new ChatAdapter(this.getActivity(), list);
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        } else {
            mAdapter.ChangeData(list);
        }
        mAdapter.setOnSlidListener(this);
    }



    /**
     * 设置好友界面--展示
     *
     * @param h
     */
    public void setPersonViewShow(TalkHistory h) {
        re_person.setVisibility(View.VISIBLE);
        tv_person_person.setText(h.getName());
        accId = h.getACC_ID();
    }

    /**
     * 设置好友界面--关闭
     */
    public void setPersonViewClose() {
        re_person.setVisibility(View.GONE);
        rippleImageView_person.stopWaveAnimation();
        presenter.setNull();
    }

    /**
     * 设置好友界面--有人说话
     */
    public void setPersonViewTalk(String name) {
        rippleImageView_person.startWaveAnimation();// 开始动画
    }

    /**
     * 设置好友界面--有人说话完毕
     */
    public void setPersonViewTalkClose() {
        rippleImageView_person.stopWaveAnimation();// 结束动画
    }

    /**
     * 设置群组界面展示
     *
     * @param h
     */
    public void setGroupViewShow(TalkHistory h) {
        re_group.setVisibility(View.VISIBLE);
        img_url_group.setImageResource(R.mipmap.test);// 群头像
        String g_name = h.getName();
        if (g_name == null || g_name.trim().equals("")) {
            g_name = "未知";
        }
        tv_groupName_group.setText(g_name);// 群名称
    }

    /**
     * 设置群组界面关闭
     */
    public void setGroupViewClose() {
        re_group.setVisibility(View.GONE);
        presenter.setNull();
    }

    /**
     * 设置群组界面--有人说话
     */
    public void setGroupViewTalk() {
        rippleImageView_group.startWaveAnimation();//开始动画
    }

    /**
     * 设置群组界面--有人说话完毕
     */
    public void setGroupViewTalkClose() {
        rippleImageView_group.stopWaveAnimation();// 结束动画
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
        if (type == -1) {
            // 默认界面
            lin_back.setVisibility(View.GONE);
            tip_view.setVisibility(View.GONE);
        } else if (type == 0) {
            // 已经登录，并且有数据
            lin_back.setVisibility(View.VISIBLE);
            tip_view.setVisibility(View.GONE);
        } else if (type == 1) {
            // 已经登录，没有数据
            lin_back.setVisibility(View.GONE);
            tip_view.setVisibility(View.VISIBLE);
            tip_view.setTipView(TipView.TipStatus.NO_DATA, "您还没有聊天对象哟\n快去找好友们聊天吧");
        } else if (type == 2) {
            // 没有网络
            lin_back.setVisibility(View.GONE);
            tip_view.setVisibility(View.VISIBLE);
            tip_view.setTipView(TipView.TipStatus.NO_NET);
        } else if (type == 3) {
            // 没有登录
            lin_back.setVisibility(View.GONE);
            tip_view.setVisibility(View.VISIBLE);
            tip_view.setTipView(TipView.TipStatus.NO_LOGIN);
        } else if (type == 4) {
            // 已经登录，数据加载失败
            lin_back.setVisibility(View.GONE);
            tip_view.setVisibility(View.VISIBLE);
            tip_view.setTipView(TipView.TipStatus.IS_ERROR);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        presenter.call(position);
    }

    @Override
    public void onClick(View view, int position) {
        presenter.itemClick(position);
    }

    /**
     * 删除数据
     *
     * @param view
     * @param position
     */
    @Override
    public void onDeleteBtnClick(View view, int position) {
        presenter.del(position);
    }

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
                dialogCancel();
                presenter.callOk( new_id, callType, accId);

            }
        });
    }

    /**
     * 展示弹出框
     */
    public void dialogShow( String new_id, int type) {
        this.new_id = new_id;
        this.callType = type;
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
