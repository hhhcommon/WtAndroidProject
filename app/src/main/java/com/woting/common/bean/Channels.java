package com.woting.common.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by amine on 2017/6/23.
 */

public class Channels implements Serializable {

    /**
     * data : {"channels":[{"id":"cn3_1","logo":"http://www.wotingfm.com:908/CM//dataCenter/group04/8c5d15f2e090467aa9b7e74ac95e85cd.100_100.png","title":"宝贝"},{"id":"cn3_10","logo":"http://www.wotingfm.com:908/CM//dataCenter/group04/8c5d15f2e090467aa9b7e74ac95e85cd.100_100.png","title":"推荐"},{"id":"cn3_11","logo":"http://www.wotingfm.com:908/CM//dataCenter/group04/8c5d15f2e090467aa9b7e74ac95e85cd.100_100.png","title":"英文"},{"id":"cn3_12","logo":"http://www.wotingfm.com:908/CM//dataCenter/group04/8c5d15f2e090467aa9b7e74ac95e85cd.100_100.png","title":"知识"},{"id":"cn3_13","logo":"http://www.wotingfm.com:908/CM//dataCenter/group04/8c5d15f2e090467aa9b7e74ac95e85cd.100_100.png","title":"综合"},{"id":"cn3_2","logo":"http://www.wotingfm.com:908/CM//dataCenter/group04/8c5d15f2e090467aa9b7e74ac95e85cd.100_100.png","title":"动漫"},{"id":"cn3_3","logo":"http://www.wotingfm.com:908/CM//dataCenter/group04/8c5d15f2e090467aa9b7e74ac95e85cd.100_100.png","title":"儿歌"},{"id":"cn3_4","logo":"http://www.wotingfm.com:908/CM//dataCenter/group04/8c5d15f2e090467aa9b7e74ac95e85cd.100_100.png","title":"父母"},{"id":"cn3_5","logo":"http://www.wotingfm.com:908/CM//dataCenter/group04/8c5d15f2e090467aa9b7e74ac95e85cd.100_100.png","title":"故事"},{"id":"cn3_6","logo":"http://www.wotingfm.com:908/CM//dataCenter/group04/8c5d15f2e090467aa9b7e74ac95e85cd.100_100.png","title":"国学"},{"id":"cn3_7","logo":"http://www.wotingfm.com:908/CM//dataCenter/group04/8c5d15f2e090467aa9b7e74ac95e85cd.100_100.png","title":"精品"},{"id":"cn3_8","logo":"http://www.wotingfm.com:908/CM//dataCenter/group04/8c5d15f2e090467aa9b7e74ac95e85cd.100_100.png","title":"名著"},{"id":"cn3_9","logo":"http://www.wotingfm.com:908/CM//dataCenter/group04/8c5d15f2e090467aa9b7e74ac95e85cd.100_100.png","title":"其他"}]}
     * msg : success
     * ret : 0
     */

    public DataBean data;
    public String msg;
    public int ret;


    public static class DataBean implements Serializable {
        public List<ChannelsBean> channels;
    }
}
