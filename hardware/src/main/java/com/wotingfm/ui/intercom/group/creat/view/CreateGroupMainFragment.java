package com.wotingfm.ui.intercom.group.creat.view;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.wotingfm.R;
import com.wotingfm.common.utils.DialogUtils;
import com.wotingfm.ui.intercom.group.creat.presenter.CreateGroupMainPresenter;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;

/**
 * 创建群主页
 * 作者：xinLong on 2017/6/5 01:30
 * 邮箱：645700751@qq.com
 */
public class CreateGroupMainFragment extends Fragment implements View.OnClickListener {
    private View rootView;
    private ImageView img_url, img_password, img_shen;
    private EditText et_phoneNumber, et_password;
    private TextView tv_send;
    private CreateGroupMainPresenter presenter;
    private LinearLayout lin_chose;// 图片来源选择
    private Dialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_creatgroup, container, false);
            rootView.setOnClickListener(this);
            inItView();
            presenter = new CreateGroupMainPresenter(this);
        }
        return rootView;
    }

    private void inItView() {
        rootView.findViewById(R.id.head_left_btn).setOnClickListener(this);
        img_url = (ImageView) rootView.findViewById(R.id.img_url);          // 群头像
        img_url.setOnClickListener(this);
        img_password = (ImageView) rootView.findViewById(R.id.img_password);// 密码群
        img_password.setOnClickListener(this);
        img_shen = (ImageView) rootView.findViewById(R.id.img_shen);        // 审核群
        img_shen.setOnClickListener(this);

        et_phoneNumber = (EditText) rootView.findViewById(R.id.et_phoneNumber);// 群名
        et_password = (EditText) rootView.findViewById(R.id.et_password);      // 群密码
        tv_send = (TextView) rootView.findViewById(R.id.tv_send);              // 提交
        tv_send.setOnClickListener(this);

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
            case R.id.img_url:
                // 头像上传
                presenter.headViewShow();
                break;
            case R.id.img_password:
                presenter.setPassword();
                break;
            case R.id.img_shen:
                presenter.setSen();
                break;
            case R.id.tv_send:
                String name = et_phoneNumber.getText().toString().trim();
                String password = et_password.getText().toString().trim();
                presenter.send(name, password);
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
        }
    }

    // 图片上传界面的展示
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
     * 设置头像
     *
     * @param url
     */
    public void setImageUrl(String url) {
        // img_url
    }

    /**
     * 设置密码群样式
     */
    public void setViewM(boolean b) {
        if(b){
            img_password.setImageResource(R.mipmap.create_group_icon_selected_s);
        }else{
            img_password.setImageResource(R.mipmap.create_group_icon_selected_n);
        }


    }

    /**
     * 设置审核群样式
     */
    public void setViewS(boolean b) {
        if(b){
            img_shen.setImageResource(R.mipmap.create_group_icon_selected_s);
        }else{
            img_shen.setImageResource(R.mipmap.create_group_icon_selected_n);
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
        presenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
