package com.woting.commonplat.nim.inject;

import android.app.Activity;

import com.netease.nimlib.sdk.msg.attachment.MsgAttachmentParser;
import com.woting.commonplat.nim.entertainment.activity.IdentifyActivity;
import com.woting.commonplat.nim.entertainment.helper.ChatRoomMemberCache;
import com.woting.commonplat.nim.entertainment.helper.GiftCache;
import com.woting.commonplat.nim.entertainment.module.CustomAttachParser;


/**
 * Created by huangjun on 2016/3/15.
 */
public class FlavorDependent implements IFlavorDependent{
    @Override
    public String getFlavorName() {
        return "entertainment";
    }

    @Override
    public Class<? extends Activity> getMainClass() {
        return IdentifyActivity.class;
    }

    @Override
    public MsgAttachmentParser getMsgAttachmentParser() {
        return new CustomAttachParser();
    }

    @Override
    public void onLogout() {
        ChatRoomMemberCache.getInstance().clear();
        GiftCache.getInstance().clear();
    }

    public static FlavorDependent getInstance() {
        return InstanceHolder.instance;
    }

    public static class InstanceHolder {
        public final static FlavorDependent instance = new FlavorDependent();
    }

    @Override
    public void onApplicationCreate() {

    }
}
