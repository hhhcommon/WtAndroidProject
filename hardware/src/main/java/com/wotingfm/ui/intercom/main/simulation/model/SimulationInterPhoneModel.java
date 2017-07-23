package com.wotingfm.ui.intercom.main.simulation.model;

import com.wotingfm.common.utils.FrequencyUtil;
import java.util.List;

/**
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class SimulationInterPhoneModel {

    public List<String> getData() {
        List<String> list = FrequencyUtil.getFrequencyList();
        return list;
    }

    /**
     * 获取对讲实际频道
     * @param c
     * @return
     */
    public String getChannel(String c) {
        String s="";
        if(c!=null&&!c.trim().equals("")&&c.trim().startsWith("CH")){
             s = c.substring(5,c.length());
        }
        return s;
    }

    /**
     * 判断备用频道是不是为空
     *
     * @param channel1
     * @param channel2
     * @return
     */
    public boolean checkData(String channel1, String channel2) {
        if ((channel1 != null && !channel1.trim().equals("") && !channel1.trim().equals("备用频道一"))
                || (channel2 != null && !channel2.trim().equals("") && !channel2.trim().equals("备用频道二"))) {
            return true;
        } else {
            return false;
        }
    }


}
