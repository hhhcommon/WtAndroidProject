package com.wotingfm.common.utils;

import android.util.Log;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.wotingfm.common.bean.MessageEvent;
import com.wotingfm.common.bean.Room;
import com.wotingfm.common.net.RetrofitUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by amine on 2017/7/25.
 */

public class IMManger {
    public static IMManger INSTANCE;


    public static IMManger getInstance() {
        if (INSTANCE == null) {
            synchronized (IMManger.class) {
                if (INSTANCE == null) {
                    INSTANCE = new IMManger();
                }
            }
        }
        return INSTANCE;
    }

    private String roomID;
    private boolean isSuccess = true;

    public boolean sendMsg(final String sessionId, final String type, final String userId) {
        final Map<String, Object> data = new HashMap<>();
        data.put("sessionId", sessionId);
        data.put("type", type);
        data.put("userId", userId);
        if ("LAUNCH".equals(type)) {
            apprtcRoom(new RoomResult() {
                @Override
                public void room(final String roomid) {
                    IMMessage msg = MessageBuilder.createCustomMessage(sessionId, SessionTypeEnum.P2P, null);
                    //// data.put("chatRoom", chatRoom);
                    data.put("roomid", roomid);
                    roomID = roomid;
                    msg.setPushPayload(data);
                    // msg.setRemoteExtension(data); // 设置服务器扩展字段
                    NIMClient.getService(MsgService.class).sendMessage(msg, false).setCallback(new RequestCallback<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.i("mingku", "onSuccess");
                            isSuccess = true;
                        }

                        @Override
                        public void onFailed(int i) {
                            Log.i("mingku", "onFailed");
                        }

                        @Override
                        public void onException(Throwable throwable) {
                            Log.i("mingku", "throwable" + throwable.getMessage());
                        }
                    });
                    EventBus.getDefault().post(new MessageEvent(roomid, 10));
                    Log.e("roomId_44444444",roomid);
                }
            });

        } else {
            IMMessage msg = MessageBuilder.createCustomMessage(sessionId, SessionTypeEnum.P2P, null);
            data.put("roomid", roomID);
            msg.setPushPayload(data); // 设置服务器扩展字段
            isSuccess = true;
            NIMClient.getService(MsgService.class).sendMessage(msg, false);
        }
        return true;
    }

    public void apprtcRoom(final RoomResult roomResult) {
        RetrofitUtils.getInstance().apprtcRoom()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Room>() {
                    @Override
                    public void call(Room room) {
                        if (room != null && room.ret == 0 && room.data != null) {
                            if (roomResult != null)
                                roomResult.room(room.data.room_number);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        T.getInstance().showToast("失败");
                    }
                });
    }

    public interface RoomResult {
        void room(String roomid);
    }

}
