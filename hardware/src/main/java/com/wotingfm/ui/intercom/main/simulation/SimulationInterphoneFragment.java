package com.wotingfm.ui.intercom.main.simulation;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.wotingfm.R;
import com.wotingfm.common.service.SimulationService;
import com.wotingfm.common.utils.FrequencyUtil;

import java.util.List;

/**
 * author：辛龙 (xinLong)
 * 2017/2/15 17:58
 * 邮箱：645700751@qq.com
 * 模拟对讲
 */
public class SimulationInterphoneFragment extends Fragment implements View.OnClickListener {

    private TextView tv_text, tv_number, tv_set;
    private List<String> list;
    private FragmentActivity context;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_simulation, container, false);
            rootView.setOnClickListener(this);
            context = getActivity();
            list = FrequencyUtil.getFrequencyList();
            setView();                                     // 设置界面
            initEmp();                                     // 初始化模拟对讲
        }
        return rootView;
    }

    // 初始化模拟对讲
    private void initEmp() {
        SimulationService.onOpenDevice(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SimulationService.setFrequence("");
            }
        },100);
        }

    // 初始化视图
    private void setView() {
        rootView.findViewById(R.id.img_close).setOnClickListener(this);                                        // 退出
        tv_set = (TextView) rootView.findViewById(R.id.tv_set);
        tv_set.setOnClickListener(this);                                                                     // 频率设置

        tv_text = (TextView) rootView.findViewById(R.id.tv_text);                                            // 文字说明
        tv_number = (TextView) rootView.findViewById(R.id.tv_number);                                        // 频率号

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_close:                                       // 退出
                break;
            case R.id.tv_set:                                        // 频率设置
                SimulationService.setFrequence("");
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SimulationService.onOpenDevice(false);
    }

}
