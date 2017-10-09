package com.wotingfm.ui.bean;

import java.io.Serializable;

public class ChannelsBean implements Serializable {
    public String id;
    public String title;
    public String desc;
    public String image_url;
    public String radio_url;
    public String listen_count;
    public PlayBill play_bill;
    public boolean had_liked;
}
