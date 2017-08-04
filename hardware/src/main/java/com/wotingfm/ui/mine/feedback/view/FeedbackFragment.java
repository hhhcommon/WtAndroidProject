package com.wotingfm.ui.mine.feedback.view;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.wotingfm.R;
import com.wotingfm.common.utils.DialogUtils;
import com.wotingfm.ui.mine.feedback.presenter.FeedbackPresenter;
import com.wotingfm.ui.mine.main.MineActivity;

/**
 * 意见反馈
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class FeedbackFragment extends Fragment implements View.OnClickListener {
    private View rootView;
    private EditText edit_information, edit_feedback;
    private FeedbackPresenter presenter;
    private Dialog dialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_feedback, container, false);
            rootView.setOnClickListener(this);
            initView();
            presenter = new FeedbackPresenter(this);
        }
        return rootView;
    }

    // 初始化视图
    private void initView() {
        rootView.findViewById(R.id.head_left_btn).setOnClickListener(this);      // 返回
        rootView.findViewById(R.id.tv_commit).setOnClickListener(this);          // 提交
        edit_information = (EditText) rootView.findViewById(R.id.edit_information); // 联系方式
        edit_feedback = (EditText) rootView.findViewById(R.id.edit_feedback);       // 意见
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left_btn:// 返回
                MineActivity.close();
                break;
            case R.id.tv_commit:    // 提交
                String information = edit_information.getText().toString(); // 联系方式
                String feedback = edit_feedback.getText().toString();       // 意见
                presenter.send(information, feedback);
                break;
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
