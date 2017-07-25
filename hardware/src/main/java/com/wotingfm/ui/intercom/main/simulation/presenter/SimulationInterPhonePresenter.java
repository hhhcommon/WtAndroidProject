package com.wotingfm.ui.intercom.main.simulation.presenter;

import com.wotingfm.ui.intercom.main.simulation.model.SimulationInterPhoneModel;
import com.wotingfm.ui.intercom.main.simulation.view.SimulationInterPhoneFragment;

import java.util.List;

/**
 * 频道的处理中心
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class SimulationInterPhonePresenter {
    private final SimulationInterPhoneFragment activity;
    private final SimulationInterPhoneModel model;
    private List<String> list;


    public SimulationInterPhonePresenter(SimulationInterPhoneFragment activity) {
        this.activity = activity;
        this.model = new SimulationInterPhoneModel();
        getData();
    }

    private void getData() {
        String c=null;
        String channel;
        try {
            c= activity.getArguments().getString("channel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(c!=null&&!c.trim().equals("")&&c.startsWith("CH")){
            channel=model.getChannel(c);
            activity.setChannel(c);
        }else{
            channel= model.getChannel("CH01-409.7500");
            activity.setChannel(c);
        }
        activity.initEmp(channel);
        list = model.getData();
        activity.setDialog(list);
    }

    /**
     * 确定按钮的操作
     */
    public void ok(int channelIndex) {
        if (list != null && list.size() > 0) {
            String s = list.get(channelIndex);
            if (s != null && !s.trim().equals("")) {
                activity.setChannel(s);
            }
        }
    }

}
