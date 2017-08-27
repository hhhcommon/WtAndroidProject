package com.wotingfm.ui.intercom.main.chat.view;

import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.woting.commonplat.widget.TipView;
import com.wotingfm.R;
import com.wotingfm.common.utils.GlideUtils;
import com.wotingfm.common.view.waveline.WaveLineView;
import com.wotingfm.common.view.waveview.WaveView;
import com.wotingfm.ui.intercom.main.chat.adapter.ChatAdapter;
import com.wotingfm.ui.intercom.main.chat.model.TalkHistory;
import com.wotingfm.ui.intercom.main.chat.presenter.ChatPresenter;

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
    private ImageView img_url_group, img_person_group,img_url_person,img_person_person;
    private RelativeLayout re_group, re_person,re_back;
    private TextView tv_groupName, tv_groupNum, tv_person_group, tv_person_person,tv_group_talked,tv_person_name,tv_person_talked,tv_line,tv_view_bg;
    private LinearLayout lin_back,lin_group_talking,lin_person_talking;
    private TipView tip_view;
    private Dialog confirmDialog;
    private int type;
    private String new_id;
    private int callType;
    private String accId;
    private WaveView WaveView_group,WaveView_person;
    private WaveLineView waveLineView;

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
        lin_back = (LinearLayout) rootView.findViewById(R.id.lin_back);//全局界面
        tv_line = (TextView) rootView.findViewById(R.id.tv_line);//
        tv_line.setVisibility(View.GONE);
        re_back = (RelativeLayout) rootView.findViewById(R.id.re_back);//
        re_back.setVisibility(View.GONE);

        // 组的界面
        re_group = (RelativeLayout) rootView.findViewById(R.id.re_group);// 组的全局界面
        re_group.setVisibility(View.GONE);
        waveLineView = (WaveLineView) rootView.findViewById(R.id.waveLineView);
        tv_view_bg= (TextView) rootView.findViewById(R.id.tv_view_bg);//

        img_url_group = (ImageView) rootView.findViewById(R.id.img_url_group);// 组的头像
        img_url_group.setOnClickListener(this);// 监听，跳转到群详情
        tv_groupName = (TextView) rootView.findViewById(R.id.tv_groupName);// 组名称
        tv_groupNum = (TextView) rootView.findViewById(R.id.tv_groupNum);
        rootView.findViewById(R.id.img_close_group).setOnClickListener(this);// 退出组对讲按钮
        tv_group_talked = (TextView) rootView.findViewById(R.id.tv_group_talked);// 无人说话提示界面
        lin_group_talking = (LinearLayout) rootView.findViewById(R.id.lin_group_talking);//有人说话提示界面
        tv_person_group = (TextView) rootView.findViewById(R.id.tv_person_group);// 群组说话人名称
        img_person_group = (ImageView) rootView.findViewById(R.id.img_person_group);// 群组说话人头像
        WaveView_group = (WaveView) rootView.findViewById(R.id.WaveView_group);// 群组说话人水波纹

        WaveView_group.setDuration(2000);
        WaveView_group.setInitialRadius(62f);
        WaveView_group.setMaxRadius(180f);
        WaveView_group.setStyle(Paint.Style.FILL);
        WaveView_group.setColor(Color.parseColor("#3CDCAF"));
        WaveView_group.setInterpolator(new LinearOutSlowInInterpolator());
        WaveView_group.start();

        // 用户的界面
        re_person = (RelativeLayout) rootView.findViewById(R.id.re_person);
        re_person.setVisibility(View.GONE);
        img_url_person = (ImageView) rootView.findViewById(R.id.img_url_person);// 好友的头像
        img_url_person.setOnClickListener(this);// 监听，跳转到个人详情
        tv_person_name = (TextView) rootView.findViewById(R.id.tv_person_name);// 好友名称
        rootView.findViewById(R.id.img_close_person).setOnClickListener(this);// // 退出个人对讲按钮
        tv_person_talked = (TextView) rootView.findViewById(R.id.tv_person_talked);// 无人说话提示界面
        lin_person_talking = (LinearLayout) rootView.findViewById(R.id.lin_person_talking);//有人说话提示界面
        tv_person_person = (TextView) rootView.findViewById(R.id.tv_person_person);// 说话人名称
        img_person_person = (ImageView) rootView.findViewById(R.id.img_person_person);// 说话人头像
        WaveView_person = (WaveView) rootView.findViewById(R.id.WaveView_person);// 单人对讲水波纹
        WaveView_person.setDuration(2000);
        WaveView_person.setInitialRadius(62f);
        WaveView_person.setMaxRadius(200f);
        WaveView_person.setStyle(Paint.Style.FILL);
        WaveView_person.setColor(Color.parseColor("#3CDCAF"));
        WaveView_person.setInterpolator(new LinearOutSlowInInterpolator());

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
                presenter.jumpGroup();
                break;
            case R.id.img_url_person:
                presenter.jumpPerson();
                break;
            case R.id.img_close_group:
                presenter.talkOverGroup();
                setGroupViewClose();
                break;
            case R.id.img_close_person:
                presenter.talkOver();
                setPersonViewClose();// 关闭好友对讲界面
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
        tv_line.setVisibility(View.VISIBLE);
        re_back.setVisibility(View.VISIBLE);
        re_person.setVisibility(View.VISIBLE);
        String p_name = h.getName();
        if (p_name == null || p_name.trim().equals("")) {
            p_name = "未知";
        }
        tv_person_name.setText(p_name);// 好友名称
        if (h.getURL() != null && !h.getURL().equals("") &&h.getURL().startsWith("http")) {// 好友头像
            GlideUtils.loadImageViewRound(h.getURL(), img_url_person, 150, 150);
        } else {
            GlideUtils.loadImageViewRound(R.mipmap.icon_avatar_d, img_url_person, 60, 60);
        }
        tv_person_talked.setVisibility(View.VISIBLE);
        lin_person_talking.setVisibility(View.GONE);
    }

    /**
     * 设置好友界面--关闭
     */
    public void setPersonViewClose() {
        tv_line.setVisibility(View.GONE);
        re_back.setVisibility(View.GONE);
        re_person.setVisibility(View.GONE);
        tv_person_talked.setVisibility(View.VISIBLE);
        lin_person_talking.setVisibility(View.GONE);
        WaveView_person.stop();
        waveLineView.stopAnim();
        presenter.setNull();
    }

    /**
     * 设置好友界面--有人说话
     */
    public void setPersonViewTalk(String name,String url) {
        tv_person_talked.setVisibility(View.GONE);
        lin_person_talking.setVisibility(View.VISIBLE);
        tv_person_person.setText(name);// 说话人名称
        if (url!= null && !url.equals("") &&url.startsWith("http")) {// 说话人头像
            GlideUtils.loadImageViewRound(url, img_person_person, 150, 150);
        } else {
            GlideUtils.loadImageViewRound(R.mipmap.icon_avatar_d, img_person_person, 60, 60);
        }
        WaveView_person.start();
        tv_view_bg.setVisibility(View.GONE);
        waveLineView.startAnim();
    }

    /**
     * 设置好友界面--有人说话完毕
     */
    public void setPersonViewTalkClose() {
        tv_person_talked.setVisibility(View.VISIBLE);
        lin_person_talking.setVisibility(View.GONE);
        WaveView_person.stop();
        waveLineView.stopAnim();
    }

    /**
     * 设置群组界面展示
     *
     * @param h
     */
    public void setGroupViewShow(TalkHistory h) {
        tv_line.setVisibility(View.VISIBLE);
        re_back.setVisibility(View.VISIBLE);
        re_group.setVisibility(View.VISIBLE);
        String g_name = h.getName();
        if (g_name == null || g_name.trim().equals("")) {
            g_name = "未知";
        }
        tv_groupName.setText(g_name);// 群名称
        setGroupViewNum(h.getGroupNum());
        if (h.getURL() != null && !h.getURL().equals("") &&h.getURL().startsWith("http")) {// 群头像
            GlideUtils.loadImageViewRoundCorners(h.getURL(), img_url_group, 150, 150);
        } else {
            GlideUtils.loadImageViewRoundCorners(R.mipmap.icon_avatar_d, img_url_group, 60, 60);
        }
        tv_group_talked.setVisibility(View.VISIBLE);
        lin_group_talking.setVisibility(View.GONE);
    }

    /**
     * 设置群组界面关闭
     */
    public void setGroupViewClose() {
        tv_line.setVisibility(View.GONE);
        re_back.setVisibility(View.GONE);
        re_group.setVisibility(View.GONE);
        tv_group_talked.setVisibility(View.VISIBLE);
        lin_group_talking.setVisibility(View.GONE);
        WaveView_group.stop();
        waveLineView.stopAnim();
        presenter.setNull();
    }

    /**
     * 设置群组界面--有人说话
     */
    public void setGroupViewTalk(String name,String url) {
        tv_group_talked.setVisibility(View.GONE);
        lin_group_talking.setVisibility(View.VISIBLE);
        tv_person_group.setText(name);// 说话人名称
        if (url!= null && !url.equals("") &&url.startsWith("http")) {// 说话人头像
            GlideUtils.loadImageViewRound(url, img_person_group, 150, 150);
        } else {
            GlideUtils.loadImageViewRound(R.mipmap.icon_avatar_d, img_person_group, 60, 60);
        }
        WaveView_group.start();
        tv_view_bg.setVisibility(View.GONE);
        waveLineView.startAnim();
    }

    /**
     * 设置群组界面--有人说话完毕
     */
    public void setGroupViewTalkClose() {
        tv_group_talked.setVisibility(View.VISIBLE);
        lin_group_talking.setVisibility(View.GONE);
        WaveView_group.stop();
        waveLineView.stopAnim();
    }

    /**
     * 设置群组成员数
     */
    public void setGroupViewNum(String num) {
        tv_groupNum.setText("("+num+")人");
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
            setGroupViewClose();
            setPersonViewClose();
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
    public void dialogShow( String new_id, int type,String acc_id ) {
        this.new_id = new_id;
        this.callType = type;
        this.accId = acc_id;
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
        waveLineView.release();
    }
    @Override
    public void onResume() {
        super.onResume();
        waveLineView.onResume();
        Log.e("聊天页面","onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        waveLineView.onPause();
        Log.e("聊天页面","onPause");
    }

}
