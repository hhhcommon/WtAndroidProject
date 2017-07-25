package com.wotingfm.ui.mine.myfocus.view;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.woting.commonplat.widget.TipView;
import com.wotingfm.R;
import com.wotingfm.common.utils.DialogUtils;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;
import com.wotingfm.ui.mine.main.MineActivity;
import com.wotingfm.ui.mine.myfocus.adapter.MyFocusAdapter;
import com.wotingfm.ui.mine.myfocus.model.Focus;
import com.wotingfm.ui.mine.myfocus.presenter.MyFocusPresenter;

import java.util.List;

/**
 * 我关注的列表
 * 作者：xinLong on 2017/6/5 01:30
 * 邮箱：645700751@qq.com
 */
public class MyFocusFragment extends Fragment implements View.OnClickListener {
    private View rootView;
    private ListView lv_focus;
    private MyFocusPresenter presenter;
    private MyFocusAdapter mAdapter;
    private Dialog dialog;
    private TipView tip_view;
    private Dialog dfDialog;
    private List<Focus> list;
    private int index=1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_myfocus, container, false);
            rootView.setOnClickListener(this);
            inItView();
            initDialog();
            presenter = new MyFocusPresenter(this);
            presenter.getData();
        }
        return rootView;
    }


    // 初始化界面
    private void inItView() {
        tip_view = (TipView) rootView.findViewById(R.id.tip_view);// 提示界面
        rootView.findViewById(R.id.head_left_btn).setOnClickListener(this);
        lv_focus = (ListView) rootView.findViewById(R.id.lv_focus);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left_btn:
                MineActivity.close();
                break;
            case R.id.tv_confirm:           // 确定
                presenter.del(list, index);
                dfDialogCancel();
                break;
            case R.id.tv_cancle:            // 取消
                dfDialogCancel();
                break;
        }
    }

    // 适配数据
    public void updateUI( List<Focus> list) {
        this.list=list;
        if (mAdapter == null) {
            mAdapter = new MyFocusAdapter(this.getActivity(), list);
            lv_focus.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
        mAdapter.setOnListener(new MyFocusAdapter.OnListener() {
            @Override
            public void del(int position) {
                index=position;
                dfDialogShow();
            }
        });
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
            lv_focus.setVisibility(View.VISIBLE);
            tip_view.setVisibility(View.GONE);
        } else if (type == 1) {
            // 已经登录，没有数据
            lv_focus.setVisibility(View.GONE);
            tip_view.setVisibility(View.VISIBLE);
            tip_view.setTipView(TipView.TipStatus.NO_DATA);
        } else if (type == 2) {
            // 没有网络
            lv_focus.setVisibility(View.GONE);
            tip_view.setVisibility(View.VISIBLE);
            tip_view.setTipView(TipView.TipStatus.NO_NET);
        } else if (type == 3) {
            // 没有登录
            lv_focus.setVisibility(View.GONE);
            tip_view.setVisibility(View.VISIBLE);
            tip_view.setTipView(TipView.TipStatus.NO_LOGIN);
        } else if (type == 4) {
            // 已经登录，数据加载失败
            lv_focus.setVisibility(View.GONE);
            tip_view.setVisibility(View.VISIBLE);
            tip_view.setTipView(TipView.TipStatus.IS_ERROR);
        }
    }

    // 初始化对话框
    private void initDialog() {
        // 清除缓存对话框
        View dialog1 = LayoutInflater.from(this.getActivity()).inflate(R.layout.dialog_exit_confirm, null);
        dialog1.findViewById(R.id.tv_confirm).setOnClickListener(this); // 清空
        dialog1.findViewById(R.id.tv_cancle).setOnClickListener(this);  // 取消
        TextView textTitle = (TextView) dialog1.findViewById(R.id.tv_title);
        textTitle.setText("是否取消关注该用户?");

        dfDialog = new Dialog(this.getActivity(), R.style.MyDialogs);
        dfDialog.setContentView(dialog1);
        dfDialog.setCanceledOnTouchOutside(false);
        dfDialog.getWindow().setBackgroundDrawableResource(R.color.transparent_background);
    }

    /**
     * 展示弹出框
     */
    public void dfDialogShow() {
        if (dfDialog != null) dfDialog.show();
    }

    /**
     * 取消弹出框
     */
    public void dfDialogCancel() {
        if (dfDialog != null) dfDialog.dismiss();
    }
}
