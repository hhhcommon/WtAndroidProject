package com.wotingfm.ui.intercom.person.personmessage.view;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.woting.commonplat.widget.TipView;
import com.wotingfm.R;
import com.wotingfm.common.utils.DialogUtils;
import com.wotingfm.ui.intercom.add.search.net.view.SearchContactsForNetFragment;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;
import com.wotingfm.ui.intercom.person.personmessage.presenter.PersonMessagePresenter;
import com.wotingfm.ui.intercom.person.personnote.view.EditPersonNoteFragment;

/**
 * 用户详情，区分好友与非好友
 * 作者：xinLong on 2017/6/5 01:30
 * 邮箱：645700751@qq.com
 */
public class PersonMessageFragment extends Fragment implements View.OnClickListener, TipView.TipViewClick {
    private View rootView;
    private TextView tv_send, tv_name, tv_introduce, tv_number, tv_address, tv_sign, tv_del;
    private ImageView img_call,img_background;
    private LinearLayout lin_note,lin_news;
    private PersonMessagePresenter presenter;
    private LinearLayout lin_chose;
    private Dialog dialog;
    private int type;
    private TipView tip_view;
    private boolean b;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_person_news, container, false);
            rootView.setOnClickListener(this);
            initViews();// 设置界面
            presenter = new PersonMessagePresenter(this);
            presenter.getData();
        }
        return rootView;
    }

    // 初始化视图
    private void initViews() {
        lin_news = (LinearLayout) rootView.findViewById(R.id.lin_news);       //
        img_background = (ImageView) rootView.findViewById(R.id.img_background);
        tip_view = (TipView) rootView.findViewById(R.id.tip_view);// 提示界面
        tip_view.setTipClick(this);

        rootView.findViewById(R.id.img_more).setOnClickListener(this);        // 更多
        rootView.findViewById(R.id.head_left_btn).setOnClickListener(this);   // 返回钮
        rootView.findViewById(R.id.lin_send).setOnClickListener(this);        // 黄色按钮
        tv_send = (TextView) rootView.findViewById(R.id.tv_send);             // 添加好友展示信息
        img_call = (ImageView) rootView.findViewById(R.id.img_call);          // 呼叫展示图片
        lin_note = (LinearLayout) rootView.findViewById(R.id.lin_note);       // 备注
        lin_note.setOnClickListener(this);
        tv_name = (TextView) rootView.findViewById(R.id.tv_name);             // 姓名
        tv_introduce = (TextView) rootView.findViewById(R.id.tv_introduce);   // 介绍
        tv_number = (TextView) rootView.findViewById(R.id.tv_number);         // 听号
        tv_address = (TextView) rootView.findViewById(R.id.tv_address);       // 地址
        tv_sign = (TextView) rootView.findViewById(R.id.tv_sign);             // 签名

        lin_chose = (LinearLayout) rootView.findViewById(R.id.lin_chose);     // 图片选择
        lin_chose.setOnClickListener(this);
        rootView.findViewById(R.id.tv_jubao).setOnClickListener(this);        // 举报
        tv_del = (TextView) rootView.findViewById(R.id.tv_del);               // 删除好友
        tv_del.setOnClickListener(this);
        rootView.findViewById(R.id.tv_quxiao).setOnClickListener(this);       // 取消
    }

    @Override
    public void onTipViewClick() {
        presenter.tipClick(type);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left_btn:
                close();
                break;
            case R.id.img_more:
                presenter.headViewShow();
                break;
            case R.id.lin_note: // 设置备注
                InterPhoneActivity.open(new EditPersonNoteFragment());
                break;
            case R.id.tv_quxiao:
                presenter.headViewShow();
                break;
            case R.id.tv_jubao:
                // 此处需要弹出提示框进行选择
                // 然后进行相应处理
                presenter.reportFriend();
                presenter.headViewShow();
                break;
            case R.id.tv_del:
                // 此处需要弹出提示框进行选择
                // 然后进行相应处理
                presenter.delFriend();
                presenter.headViewShow();
                break;
            case R.id.lin_send:
                if(b){

                }else{
                    presenter.apply();
                }
                break;


        }
    }

    /**
     * 图片上传界面的展示
     * @param type true 展示/false 不展示
     */
    public void imageShow(boolean type) {
        if (type) {
            Animation mAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.wt_slide_in_from_bottom);
            lin_chose.setAnimation(mAnimation);
            lin_chose.setVisibility(View.VISIBLE);
        } else {
            Animation mAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.wt_slide_out_from_bottom);
            lin_chose.setAnimation(mAnimation);
            lin_chose.setVisibility(View.GONE);
        }
    }

    /**
     * 根据好友状态设置界面展示
     * @param b true 好友/false 非好友
     */
    public void setView(boolean b) {
        this.b=b;
        if (b) {
            // 是好友
            tv_send.setVisibility(View.GONE);
            img_call.setVisibility(View.VISIBLE);
            lin_note.setVisibility(View.VISIBLE);
            tv_del.setVisibility(View.VISIBLE);
        } else {
            // 不是好友
            tv_send.setVisibility(View.VISIBLE);
            img_call.setVisibility(View.GONE);
            lin_note.setVisibility(View.GONE);
            tv_del.setVisibility(View.GONE);
        }
    }

    /**
     * 设置界面展示数据
     * @param name 姓名
     * @param introduce 介绍
     * @param number 听号
     * @param address 地址
     * @param sign 签名
     */
    public void setViewData(String name,String introduce,String number,String address,String sign){
        tv_name.setText(name);             // 姓名
        tv_introduce.setText(introduce);   // 介绍
        tv_number.setText(number);         // 听号
        tv_address.setText(address);       // 地址
        tv_sign.setText(sign);             // 签名
    }

    /**
     * 关闭当前界面
     */
    public void close() {
        InterPhoneActivity.close();
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
            lin_news .setVisibility(View.VISIBLE);
            img_background.setVisibility(View.VISIBLE);
            tip_view.setVisibility(View.GONE);
        } else if (type == 1) {
            // 已经登录，没有数据
            lin_news .setVisibility(View.GONE);
            img_background.setVisibility(View.GONE);
            tip_view.setVisibility(View.VISIBLE);
            tip_view.setTipView(TipView.TipStatus.NO_DATA);
        } else if (type == 2) {
            // 没有网络
            lin_news .setVisibility(View.GONE);
            img_background.setVisibility(View.GONE);
            tip_view.setVisibility(View.VISIBLE);
            tip_view.setTipView(TipView.TipStatus.NO_NET);
        } else if (type == 3) {
            // 没有登录
            lin_news .setVisibility(View.GONE);
            img_background.setVisibility(View.GONE);
            tip_view.setVisibility(View.VISIBLE);
            tip_view.setTipView(TipView.TipStatus.NO_LOGIN);
        } else if (type == 4) {
            // 已经登录，数据加载失败
            lin_news .setVisibility(View.GONE);
            img_background.setVisibility(View.GONE);
            tip_view.setVisibility(View.VISIBLE);
            tip_view.setTipView(TipView.TipStatus.IS_ERROR);
        }
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
