package com.wotingfm.ui.intercom.main.simulation.view;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.woting.commonplat.manager.PhoneMsgManager;
import com.wotingfm.R;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.service.SimulationService;
import com.wotingfm.common.view.pickview.LoopView;
import com.wotingfm.common.view.pickview.OnItemSelectedListener;
import com.wotingfm.ui.intercom.main.simulation.presenter.SimulationInterPhonePresenter;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;
import java.util.List;

/**
 * author：辛龙 (xinLong)
 * 2017/2/15 17:58
 * 邮箱：645700751@qq.com
 * 模拟对讲
 */
public class SimulationInterPhoneFragment extends Fragment implements View.OnClickListener {

    private TextView tv_text, tv_number, tv_set;
    private View rootView;
    private Dialog CDialog;
    private int channelIndex = 10;
    private SimulationInterPhonePresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_simulation, container, false);
            rootView.setOnClickListener(this);
            setView();   // 设置界面
            presenter = new SimulationInterPhonePresenter(this);        }
        return rootView;
    }

    /**
     * 初始化模拟对讲
     */
    public void initEmp(final String channel) {
//        SimulationService.onOpenDevice(true);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                SimulationService.setFrequence(channel);
//            }
//        }, 100);
    }

    // 初始化视图
    private void setView() {
        int height;
        if (PhoneMsgManager.ScreenWidth <= 480) {
            height = 180;
        } else if (480 < PhoneMsgManager.ScreenWidth && PhoneMsgManager.ScreenWidth <= 720) {
            height = 220;
        } else {
            height = 240;
        }

        rootView.findViewById(R.id.img_close).setOnClickListener(this);                                        // 退出
        tv_set = (TextView) rootView.findViewById(R.id.tv_set);
        tv_set.setOnClickListener(this);                                                                     // 频率设置
        LinearLayout lin_center = (LinearLayout) rootView.findViewById(R.id.lin_center);
        ViewGroup.LayoutParams para = lin_center.getLayoutParams();//获取按钮的布局
        para.height = ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height, getResources().getDisplayMetrics()));
//        para.height=height;
        lin_center.setLayoutParams(para); //设置修改后的布局。
        tv_text = (TextView) rootView.findViewById(R.id.tv_text);                                            // 文字说明
        tv_number = (TextView) rootView.findViewById(R.id.tv_number);                                        // 频率号

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_close:
                // 退出
                InterPhoneActivity.close();
                break;
            case R.id.tv_set:                                        // 频率设置
                showDialog(true);
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
     */
    public void setDialog(List<String> list) {
        GlobalStateConfig.LoopViewW = new Double(PhoneMsgManager.ScreenWidth / 2.2).intValue();
        ;
        View dialog = LayoutInflater.from(this.getActivity()).inflate(R.layout.dialog_channel, null);
        LoopView pickChannel = (LoopView) dialog.findViewById(R.id.pick_channel);
        dialog.findViewById(R.id.tv_quxiao).setOnClickListener(this);
        dialog.findViewById(R.id.tv_ok).setOnClickListener(this);

        // 设置字体样式
        pickChannel.setTextSize(15, 17);
        pickChannel.setItems(list);
        pickChannel.setInitPosition(10);

        pickChannel.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                channelIndex = index;
            }
        });

        CDialog = new Dialog(this.getActivity(), R.style.MyDialog);
        CDialog.setContentView(dialog);
        CDialog.setCanceledOnTouchOutside(true);

        DisplayMetrics dm = new DisplayMetrics();
        this.getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
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
     * 频道的tv展示
     * @param s
     */
    public void setChannel( String s) {
        tv_text.setText("当前频道");
        tv_number.setText(s);
    }

    /**
     * dialog的展示情况
     *
     * @param b true=展示/false=不展示
     */
    public void showDialog(boolean b) {
        if (b) {
            CDialog.show();
        } else {
            if (CDialog.isShowing()) {
                CDialog.dismiss();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        SimulationService.onOpenDevice(false);
        presenter.destroy();
        presenter=null;
    }

    /**
     * 开机连接频道
     * @param channel
     */
    public void start(String channel) {
//        SimulationService.setFrequence(channel);
    }
}
