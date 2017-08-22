package com.woting.ui.mine.message.notify.view;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.woting.commonplat.widget.TipView;
import com.woting.R;
import com.woting.common.utils.DialogUtils;
import com.woting.ui.mine.message.notify.adapter.NotifyMsgAdapter;
import com.woting.ui.mine.message.notify.model.Msg;
import com.woting.ui.mine.message.notify.presenter.MsgNotifyPresenter;
import com.woting.ui.play.look.activity.LookListActivity;

import java.util.List;

/**
 * 消息中心
 * 作者：xinLong on 2017/6/5 01:30
 * 邮箱：645700751@qq.com
 */
public class MsgNotifyFragment extends Fragment implements NotifyMsgAdapter.IonSlidingViewClickListener, View.OnClickListener {
    private View rootView;
    private MsgNotifyPresenter presenter;
    private RecyclerView mRecyclerView;
    private TipView tip_view;
    private Dialog dialog;
    private NotifyMsgAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_msg, container, false);
            rootView.setOnClickListener(this);
            inItView();
            presenter = new MsgNotifyPresenter(this);
        }
        return rootView;
    }

    // 设置界面
    private void inItView() {
        tip_view = (TipView) rootView.findViewById(R.id.tip_view);// 提示界面
        rootView.findViewById(R.id.head_left_btn).setOnClickListener(this);
        TextView tv_center = (TextView) rootView.findViewById(R.id.tv_center);
        tv_center.setText("消息中心");
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left_btn:
                LookListActivity.close();
                break;
        }
    }

    // 适配数据
    public void updateUI(List<Msg> list) {
        if (mAdapter == null) {
            mAdapter = new NotifyMsgAdapter(this.getActivity(), list);
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        } else {
            mAdapter.changeData(list);
        }
        mAdapter.setOnSlidListener(this);
    }

    /**
     * 同意按钮的操作
     *
     * @param view
     * @param position
     */
    @Override
    public void onItemClick(View view, int position) {
        presenter.apply(position);
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
    public void onDestroy() {
        super.onDestroy();
        presenter.destroy();
        presenter = null;
    }
}
