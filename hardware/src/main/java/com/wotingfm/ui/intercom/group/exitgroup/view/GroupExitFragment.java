package com.wotingfm.ui.intercom.group.exitgroup.view;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wotingfm.R;
import com.wotingfm.common.utils.DialogUtils;
import com.wotingfm.ui.intercom.group.exitgroup.presenter.GroupExitPresenter;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;

/**
 * 群主解散群组管理界面
 * 作者：xinLong on 2017/6/5 01:30
 * 邮箱：645700751@qq.com
 */
public class GroupExitFragment extends Fragment implements View.OnClickListener {
    private View rootView;
    private RelativeLayout re_exit, re_transfer;
    private GroupExitPresenter presenter;
    private ResultListener Listener;
    private Dialog dialog;
    private Dialog exitDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_group_exit, container, false);
            rootView.setOnClickListener(this);
            inItView();
            initDialog();
            presenter = new GroupExitPresenter(this);
        }
        return rootView;
    }

    // 设置界面
    private void inItView() {
        rootView.findViewById(R.id.head_left_btn).setOnClickListener(this);
        re_exit = (RelativeLayout) rootView.findViewById(R.id.re_exit);
        re_exit.setOnClickListener(this);
        re_transfer = (RelativeLayout) rootView.findViewById(R.id.re_transfer);
        re_transfer.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left_btn:
                InterPhoneActivity.close();
                break;
            case R.id.re_exit:
                exitDialog.show();
                break;
            case R.id.re_transfer:
                // 跳转到转让群主界面
                presenter.transfer();
                break;
            case R.id.tv_cancle:
                exitDialog.dismiss();
                break;
            case R.id.tv_confirm:
                // 解散群
                presenter.exit();
                exitDialog.dismiss();
                break;
        }
    }

    // 初始化对话框
    private void initDialog() {
        // 解散群组对话框
        View dialog1 = LayoutInflater.from(this.getActivity()).inflate(R.layout.dialog_talk_person_del, null);
        dialog1.findViewById(R.id.tv_confirm).setOnClickListener(this); // 清空
        dialog1.findViewById(R.id.tv_cancle).setOnClickListener(this);  // 取消
        TextView textTitle = (TextView) dialog1.findViewById(R.id.tv_title);
        textTitle.setText("解散后你将失去和群友的联系哦，你确定要解散群吗？");

        exitDialog = new Dialog(this.getActivity(), R.style.MyDialogs);
        exitDialog.setContentView(dialog1);
        exitDialog.setCanceledOnTouchOutside(false);
        exitDialog.getWindow().setBackgroundDrawableResource(R.color.transparent_background);
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
     * 返回值设置
     *
     * @param type
     * @param changeType 更改类型 0:解散   1:更新数据
     */
    public void setResult(boolean type, int changeType) {
        Listener.resultListener(type, changeType);
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
        void resultListener(boolean type, int changeType);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("界面", "执行销毁");
        presenter.destroy();
        presenter = null;
    }
}
