package com.woting.ui.intercom.group.groupmumberdel.view;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.woting.commonplat.widget.TipView;
import com.woting.R;
import com.woting.common.utils.DialogUtils;
import com.woting.ui.intercom.group.groupmumberdel.adapter.GroupNumberDelAdapter;
import com.woting.ui.intercom.group.groupmumberdel.presenter.GroupNumberDelPresenter;
import com.woting.ui.intercom.main.contacts.model.Contact;
import com.woting.ui.intercom.main.view.InterPhoneActivity;

import java.util.List;

/**
 * 删除组成员
 * 作者：xinLong on 2017/6/5 01:30
 * 邮箱：645700751@qq.com
 */
public class GroupNumberDelFragment extends Fragment implements GroupNumberDelAdapter.IonSlidingViewClickListener, View.OnClickListener {
    private View rootView;
    private RecyclerView mRecyclerView;
    private GroupNumberDelPresenter presenter;
    private GroupNumberDelAdapter mAdapter;
    private Dialog dialog;
    private TipView tip_view;
    private ResultListener Listener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_groupmumbersdel, container, false);
            rootView.setOnClickListener(this);
            inItView();
            presenter = new GroupNumberDelPresenter(this);
            presenter.getData();
        }
        return rootView;
    }


    // 初始化界面
    private void inItView() {
        tip_view = (TipView) rootView.findViewById(R.id.tip_view);// 提示界面

        rootView.findViewById(R.id.head_left_btn).setOnClickListener(this);
        TextView tv_center = (TextView) rootView.findViewById(R.id.tv_center);
        tv_center.setText("删除群成员");

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left_btn:
                presenter.isChange();
                InterPhoneActivity.close();
                break;
        }
    }

    // 适配数据
    public void updateUI(List<Contact.user> list) {
        if (mAdapter == null) {
            mAdapter = new GroupNumberDelAdapter(this.getActivity(), list);
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        } else {
            mAdapter.notifyDataSetChanged();
        }
        mAdapter.setOnSlidListener(this);
    }

    /**
     * 删除按钮的操作
     *
     * @param view
     * @param position
     */
    @Override
    public void onDeleteBtnClick(View view, int position) {
        presenter.del(position);
    }

    /**
     * item的点击事件
     *
     * @param view
     * @param position
     */
    @Override
    public void onAdapterClick(View view, int position) {
        presenter.onClick(position);
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
        if (type == 0) {
            // 已经登录，并且有数据
            mRecyclerView.setVisibility(View.VISIBLE);
            tip_view.setVisibility(View.GONE);
        } else if (type == 1) {
            // 已经登录，没有数据
            mRecyclerView.setVisibility(View.GONE);
            tip_view.setVisibility(View.VISIBLE);
            tip_view.setTipView(TipView.TipStatus.NO_DATA);
        } else if (type == 2) {
            // 没有网络
            mRecyclerView.setVisibility(View.GONE);
            tip_view.setVisibility(View.VISIBLE);
            tip_view.setTipView(TipView.TipStatus.NO_NET);
        } else if (type == 3) {
            // 没有登录
            mRecyclerView.setVisibility(View.GONE);
            tip_view.setVisibility(View.VISIBLE);
            tip_view.setTipView(TipView.TipStatus.NO_LOGIN);
        } else if (type == 4) {
            // 已经登录，数据加载失败
            mRecyclerView.setVisibility(View.GONE);
            tip_view.setVisibility(View.VISIBLE);
            tip_view.setTipView(TipView.TipStatus.IS_ERROR);
        }
    }

    /**
     * 返回值设置
     *
     * @param type
     */
    public void setResult(boolean type) {
        Listener.resultListener(type);
    }

    /**
     * 回调结果值
     *
     * @param l
     */
    public void setResultListener(ResultListener l) {
        Listener = l;
    }

    public interface ResultListener {
        void resultListener(boolean type);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("界面","执行销毁");
        presenter.destroy();
        presenter=null;
    }
}
