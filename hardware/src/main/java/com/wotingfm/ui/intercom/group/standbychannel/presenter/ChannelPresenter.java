package com.wotingfm.ui.intercom.group.standbychannel.presenter;

import com.wotingfm.common.utils.FrequencyUtil;
import com.wotingfm.ui.intercom.group.standbychannel.model.ChannelModel;
import com.wotingfm.ui.intercom.group.standbychannel.view.StandbyChannelFragment;
import com.wotingfm.ui.intercom.person.newfriend.model.NewFriendModel;
import com.wotingfm.ui.intercom.person.newfriend.view.NewFriendFragment;

import java.util.List;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class ChannelPresenter {

    private final StandbyChannelFragment activity;
    private final ChannelModel model;
    private int type;
    private List<String> list;


    public ChannelPresenter(StandbyChannelFragment activity) {
        this.activity = activity;
        this.model = new ChannelModel();
    }

    /**
     * 获取数据
     */
    public void getData() {
        list = model.getData();
        activity.setDialog(list);
    }

    /**
     * 按钮点击事件
     *
     * @param type
     */
    public void setChannel(int type) {
        this.type = type;
        if (type == 1) {
            activity.showDialog(true);
        } else {
            activity.showDialog(true);
        }
    }

    /**
     * 完成按钮的操作
     * 界面来源不同
     */
    public void over() {

    }

    /**
     * 确定按钮的操作
     */
    public void ok(int channelIndex) {
        if (list != null && list.size() > 0) {
            String s = list.get(channelIndex);
            if (s != null && !s.trim().equals("")) {
                activity.setChannel(type, s);
            }
        }
    }
}
