package com.wotingfm.ui.intercom.group.editgroupmessage.view;

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
import com.wotingfm.common.utils.GlideUtils;
import com.wotingfm.ui.intercom.group.editgroupmessage.presenter.EditGroupMessagePresenter;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;

/**
 * 编辑资料
 * 作者：xinLong on 2017/6/5 01:30
 * 邮箱：645700751@qq.com
 */
public class EditGroupMessageFragment extends Fragment implements View.OnClickListener {
    private View rootView;
    private EditGroupMessagePresenter presenter;
    private LinearLayout lin_chose;
    private ImageView image_headView;
    private TextView tv_groupName, tv_groupIntroduce_name, tv_groupAddress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_editgroupmessage, container, false);
            rootView.setOnClickListener(this);
            inItView();
            presenter = new EditGroupMessagePresenter(this);
            presenter.getData();
        }
        return rootView;
    }

    // 初始化界面
    private void inItView() {
        rootView.findViewById(R.id.head_left_btn).setOnClickListener(this);

        rootView.findViewById(R.id.re_headView).setOnClickListener(this);
        image_headView = (ImageView) rootView.findViewById(R.id.image_headView); // 图片

        rootView.findViewById(R.id.re_groupName).setOnClickListener(this);
        tv_groupName = (TextView) rootView.findViewById(R.id.tv_groupName); // 群名称

        rootView.findViewById(R.id.re_groupAddress).setOnClickListener(this);
        tv_groupAddress = (TextView) rootView.findViewById(R.id.tv_groupAddress); // 群地址

        rootView.findViewById(R.id.re_groupIntroduce).setOnClickListener(this);
        tv_groupIntroduce_name = (TextView) rootView.findViewById(R.id.tv_groupIntroduce_name); // 群介绍

        lin_chose = (LinearLayout) rootView.findViewById(R.id.lin_chose); // 图片选择
        lin_chose.setOnClickListener(this);
        rootView.findViewById(R.id.tv_paizhao).setOnClickListener(this);  // 拍照
        rootView.findViewById(R.id.tv_xiangce).setOnClickListener(this);  // 相册
        rootView.findViewById(R.id.tv_quxiao).setOnClickListener(this);   // 取消
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left_btn:
                InterPhoneActivity.close();
                break;
            case R.id.tv_paizhao:
                presenter.camera();
                presenter.headViewShow();
                break;
            case R.id.tv_xiangce:
                presenter.photoAlbum();
                presenter.headViewShow();
                break;
            case R.id.tv_quxiao:
                presenter.headViewShow();
                break;
            case R.id.re_headView:
                presenter.headViewShow();  // 头像上传
                break;
            case R.id.re_groupName:
                presenter.setGroupName();  // 设置群名称
                break;
            case R.id.re_groupAddress:
                presenter.setGroupAddress();  // 设置群地址
                break;
            case R.id.re_groupIntroduce:
                presenter.setGroupIntroduce();  // 设置群介绍
                break;
        }
    }

    /**
     * 图片上传界面的展示
     *
     * @param type
     */
    public void imageShow(boolean type) {
        if (type) {
            Animation mAnimation = AnimationUtils.loadAnimation(this.getActivity(), R.anim.wt_slide_in_from_bottom);
            lin_chose.setAnimation(mAnimation);
            lin_chose.setVisibility(View.VISIBLE);
        } else {
            Animation mAnimation = AnimationUtils.loadAnimation(this.getActivity(), R.anim.wt_slide_out_from_bottom);
            lin_chose.setAnimation(mAnimation);
            lin_chose.setVisibility(View.GONE);
        }
    }

    /**
     * 设置图片
     *
     * @param url
     */
    public void setViewForImage(String url) {
        if (url != null && !url.trim().equals("")) {
            GlideUtils.loadImageViewSize(this.getActivity(), url, 60, 60, image_headView, true);
        }else{

        }
    }

    /**
     * 设置组名称
     *
     * @param s
     */
    public void setViewForGroupName(String s) {
        if (s != null && !s.trim().equals("")) {
            tv_groupName.setText(s);
        } else {
            tv_groupName.setText("");
        }
    }

    /**
     * 设置组地址
     *
     * @param s
     */
    public void setViewGroupAddress(String s) {
        if (s != null && !s.trim().equals("")) {
            tv_groupAddress.setText(s);
        } else {
            tv_groupAddress.setText("");
        }
    }

    /**
     * 设置组介绍
     *
     * @param s
     */
    public void setViewForGroupIntroduce(String s) {
        if (s != null && !s.trim().equals("")) {
            tv_groupIntroduce_name.setText(s);
        } else {
            tv_groupIntroduce_name.setText("");
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
