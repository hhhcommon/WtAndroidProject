package com.wotingfm.ui.base.basefragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.woting.commonplat.widget.LoadingDialog;
import com.wotingfm.common.utils.ProgressDialogUtils;

import butterknife.ButterKnife;

/**
 * Created by amine on 2017/6/14.
 */

public abstract class BaseFragment extends Fragment implements View.OnClickListener {
    protected View rootView;

    @Override
    public void onClick(View view) {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null)
            rootView = inflater.inflate(getLayoutResource(), container, false);
        ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    private LoadingDialog mLdDialog;

    public void showLodingDialog() {
        mLdDialog = ProgressDialogUtils.instance(getActivity()).getLoadingDialog();
        mLdDialog.show();
    }

    public void dissmisDialog() {
        if (mLdDialog != null)
            mLdDialog.dismiss();
    }

    //获取布局文件
    protected abstract int getLayoutResource();

    //初始化view
    protected abstract void initView();


    /**
     * 通过Class跳转界面
     **/
    public void startActivity(Class<?> cls) {
        startActivity(cls, null);
    }

    /**
     * 通过Class跳转界面
     **/
    public void startActivityForResult(Class<?> cls, int requestCode) {
        startActivityForResult(cls, null, requestCode);
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void startActivityForResult(Class<?> cls, Bundle bundle,
                                       int requestCode) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }
}
