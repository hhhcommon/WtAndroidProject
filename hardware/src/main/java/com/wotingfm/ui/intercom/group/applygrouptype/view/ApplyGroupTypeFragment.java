package com.wotingfm.ui.intercom.group.applygrouptype.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.wotingfm.R;
import com.wotingfm.ui.intercom.group.applygrouptype.presenter.ApplyGroupTypePresenter;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;

/**
 * 加群方式(待定)
 * 作者：xinLong on 2017/6/5 01:30
 * 邮箱：645700751@qq.com
 */
public class ApplyGroupTypeFragment extends Fragment implements View.OnClickListener {
    private View rootView;
    private ApplyGroupTypePresenter presenter;
    private ImageView img_password,img_shen;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_applygrouptype, container, false);
            rootView.setOnClickListener(this);
            inItView();
            presenter = new ApplyGroupTypePresenter(this);
            presenter.setView();
        }
        return rootView;
    }

    // 设置界面
    private void inItView() {
        rootView.findViewById(R.id.head_left_btn).setOnClickListener(this);
        rootView.findViewById(R.id.tv_ok).setOnClickListener(this);
        img_password=(ImageView)rootView.findViewById(R.id.img_password);
        img_password.setOnClickListener(this);
        img_shen=(ImageView)rootView.findViewById(R.id.img_shen);
        img_shen.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left_btn:
                InterPhoneActivity.close();
                break;
            case R.id.tv_ok:// 确定
                presenter.ok();
                break;
            case R.id.img_password:// 密码群
                presenter.password();
                break;
            case R.id.img_shen:// 审核群
                presenter.shen();
                break;
        }
    }

    /**
     * 设置密码群样式
     * @param b
     */
    public void setPasswordView(boolean b){
        if(b){
            img_password.setImageResource(R.mipmap.create_group_icon_selected_s);
        }else{
            img_password.setImageResource(R.mipmap.create_group_icon_selected_n);
        }
    }

    /**
     * 设置审核群样式
     * @param b
     */
    public void setShenView(boolean b){
        if(b){
            img_shen.setImageResource(R.mipmap.create_group_icon_selected_s);
        }else{
            img_shen.setImageResource(R.mipmap.create_group_icon_selected_n);
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
