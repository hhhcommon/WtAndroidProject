package com.wotingfm.ui.intercom.person.personmessage.view;

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

import com.wotingfm.R;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;
import com.wotingfm.ui.intercom.person.personmessage.presenter.PersonMessagePresenter;

/**
 * 用户详情，区分好友与非好友
 * 作者：xinLong on 2017/6/5 01:30
 * 邮箱：645700751@qq.com
 */
public class PersonMessageFragment extends Fragment implements View.OnClickListener {
    private View rootView;
    private TextView tv_send, tv_name, tv_introduce, tv_number, tv_address, tv_sign, tv_del;
    private ImageView img_call;
    private LinearLayout lin_note;
    private PersonMessagePresenter presenter;
    private LinearLayout lin_chose;
    private boolean type;

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
        rootView.findViewById(R.id.img_more).setOnClickListener(this);        // 更多
        rootView.findViewById(R.id.head_left_btn).setOnClickListener(this);   // 返回钮
        rootView.findViewById(R.id.lin_send).setOnClickListener(this);        // 黄色按钮
        tv_send = (TextView) rootView.findViewById(R.id.tv_send);             // 添加好友展示信息
        img_call = (ImageView) rootView.findViewById(R.id.img_call);          // 呼叫展示图片
        lin_note = (LinearLayout) rootView.findViewById(R.id.lin_note);       // 备注
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left_btn:
                close();
                break;
            case R.id.img_more:
                presenter.headViewShow();
                break;
            case R.id.lin_note:
                // 设置备注
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
        this.type=b;
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
