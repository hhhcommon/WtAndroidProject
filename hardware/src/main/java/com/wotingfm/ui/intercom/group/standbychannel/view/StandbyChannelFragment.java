package com.wotingfm.ui.intercom.group.standbychannel.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wotingfm.R;
import com.wotingfm.common.utils.DialogUtils;
import com.wotingfm.common.utils.FrequencyUtil;
import com.wotingfm.common.view.pickview.LoopView;
import com.wotingfm.common.view.pickview.OnItemSelectedListener;
import com.wotingfm.ui.intercom.group.standbychannel.presenter.ChannelPresenter;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;

import java.util.List;

/**
 * 设置备用频道界面
 * 作者：xinLong on 2017/6/5 01:30
 * 邮箱：645700751@qq.com
 */
public class StandbyChannelFragment extends Fragment implements View.OnClickListener {
    private View rootView;
    private FragmentActivity context;
    private Dialog CDialog;
    public int channelIndex=1;
    private ChannelPresenter presenter;
    private TextView tv_channel1,tv_channel2;
    private Dialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_groupsetchannel, container, false);
            rootView.setOnClickListener(this);
            inItView();
            presenter = new ChannelPresenter(this);
            presenter.getData();
        }
        return rootView;
    }

    // 设置界面
    private void inItView() {
        rootView.findViewById(R.id.lin_channel1).setOnClickListener(this);
        rootView.findViewById(R.id.lin_channel2).setOnClickListener(this);
        rootView.findViewById(R.id.tv_send).setOnClickListener(this);
        rootView.findViewById(R.id.head_left_btn).setOnClickListener(this);
        tv_channel1 = (TextView) rootView.findViewById(R.id.tv_channel1);
        tv_channel2 = (TextView) rootView.findViewById(R.id.tv_channel2);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.head_left_btn:
                presenter.jump();
                break;
            case R.id.lin_channel1:
                presenter.setChannel(1);
                break;
            case R.id.lin_channel2:
                presenter.setChannel(2);
                break;
            case R.id.tv_send:
                presenter.over();
                break;
            case R.id.tv_quxiao:
                showDialog(false);
                break;
            case R.id.tv_ok:
                presenter.ok(channelIndex);
                showDialog(false);
                break;
        }
    }

    /**
     * 频率选择框
     *
     * @param tempList
     */
    public void setDialog(List<String> tempList) {
        View dialog = LayoutInflater.from(context).inflate(R.layout.dialog_channel, null);
        LoopView pickChannel = (LoopView) dialog.findViewById(R.id.pick_channel);
        dialog.findViewById(R.id.tv_quxiao).setOnClickListener(this);
        dialog.findViewById(R.id.tv_ok).setOnClickListener(this);

        // 设置字体样式
        pickChannel.setTextSize(15,17);
        pickChannel.setItems(tempList);
        pickChannel.setInitPosition(10);

        pickChannel.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                channelIndex = index;
            }
        });

        CDialog = new Dialog(context, R.style.MyDialog);
        CDialog.setContentView(dialog);
        CDialog.setCanceledOnTouchOutside(true);

        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        ViewGroup.LayoutParams params = dialog.getLayoutParams();
        params.width = screenWidth;
        dialog.setLayoutParams(params);

        Window window = CDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.inOutStyle);
        window.setBackgroundDrawableResource(R.color.transparent_40_black);
    }

    /**
     * dialog的展示情况
     * @param b true=展示/false=不展示
     */
    public void showDialog(boolean b){
        if(b){
            CDialog.show();
        }else{
            if (CDialog.isShowing()) {
                CDialog.dismiss();
            }
        }
    }

    /**
     * 频道的tv展示
     * @param type 1=channel1,2=channel2
     * @param s
     */
    public void setChannel(int type,String s){
        if(type==1){
            tv_channel1.setText(s);
        }else{
            tv_channel2.setText(s);
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


}
