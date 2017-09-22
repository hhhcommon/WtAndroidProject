package com.wotingfm.common.manager;

import android.util.Log;
import com.google.gson.GsonBuilder;
import com.netease.nimlib.sdk.avchat.AVChatCallback;
import com.netease.nimlib.sdk.avchat.AVChatManager;
import com.netease.nimlib.sdk.avchat.constant.AVChatType;
import com.netease.nimlib.sdk.avchat.constant.AVChatUserRole;
import com.netease.nimlib.sdk.avchat.constant.AVChatVideoCropRatio;
import com.netease.nimlib.sdk.avchat.model.AVChatChannelInfo;
import com.netease.nimlib.sdk.avchat.model.AVChatData;
import com.netease.nimlib.sdk.avchat.model.AVChatParameters;
import com.woting.commonplat.nim.base.util.log.LogUtil;
import com.wotingfm.common.config.GlobalStateConfig;

/**
 * 对讲控制
 * author：辛龙 (xinLong)
 * 2017/7/28 11:21
 * 邮箱：645700751@qq.com
 */
public class InterPhoneControl {

    /**
     * 呼叫方==呼叫
     *
     * @param id
     * @return
     */
    public static void call(String id, final Listener lin) {
        AVChatManager.getInstance().enableRtc();//开启音视频引擎。
        AVChatManager.getInstance().call2(id, AVChatType.AUDIO, null, new AVChatCallback<AVChatData>() {
                    @Override
                    public void onSuccess(AVChatData data) {
                        GlobalStateConfig.avChatData = data;
                        Log.e("呼叫返回数据", new GsonBuilder().serializeNulls().create().toJson(data));
                        lin.type(true);
                    }

                    @Override
                    public void onFailed(int code) {
                        LogUtil.d("呼叫", "avChat call failed code->" + code);
                        lin.type(false);
                    }

                    @Override
                    public void onException(Throwable exception) {
                        LogUtil.d("呼叫", "avChat call onException->" + exception);
                        lin.type(false);
                    }
                }
        );
    }

    /**
     * 呼叫方==挂断电话
     *
     * @return
     */
    public static void hangUp(final Listener lin) {
        AVChatManager.getInstance().hangUp2(GlobalStateConfig.avChatData.getChatId(), new AVChatCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                LogUtil.e("呼叫方==挂断电话", "accept success");
                lin.type(true);
            }

            @Override
            public void onFailed(int code) {
                LogUtil.e("呼叫方==挂断电话", "hangup onFailed->" + code);
                lin.type(false);
            }

            @Override
            public void onException(Throwable exception) {
                LogUtil.e("呼叫方==挂断电话", "hangup onException->" + exception);
                lin.type(false);
            }
        });
    }

    /**
     * 被叫方==接受
     *
     * @return
     */
    public static void accept(final Listener lin) {
        //接听，告知服务器，以便通知其他端
        AVChatManager.getInstance().enableRtc();
        AVChatManager.getInstance().accept2(GlobalStateConfig.avChatData.getChatId(), new AVChatCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                LogUtil.e("被叫方==接受", "accept success");
                lin.type(true);
            }

            @Override
            public void onFailed(int code) {
                LogUtil.e("被叫方==接受", "accept onFailed->" + code);
                lin.type(false);
            }

            @Override
            public void onException(Throwable exception) {
                LogUtil.e("被叫方==接受", "accept exception->" + exception);
                lin.type(false);
            }
        });
    }

    /**
     * 被叫方==拒绝接受
     *
     * @return
     */
    public static void refuse(final Listener lin) {
        AVChatManager.getInstance().hangUp2(GlobalStateConfig.avChatData.getChatId(), new AVChatCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                LogUtil.e("被叫方==拒绝接受", "reject onSuccess-");
                lin.type(true);
            }

            @Override
            public void onFailed(int code) {
                LogUtil.e("被叫方==拒绝接受", "reject onFailed->" + code);
                lin.type(false);
            }

            @Override
            public void onException(Throwable exception) {
                LogUtil.e("被叫方==拒绝接受", "reject onException");
                lin.type(false);
            }
        });
    }

    /**
     * 退出房間==个人
     * @return
     */
    public static boolean quitRoomPerson() {
        AVChatManager.getInstance().hangUp2(GlobalStateConfig.avChatData.getChatId(), new AVChatCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                LogUtil.e("退出房間==个人", "reject onSuccess-");
            }

            @Override
            public void onFailed(int code) {
                LogUtil.e("退出房間==个人", "hangup onFailed->" + code);
            }

            @Override
            public void onException(Throwable exception) {
                LogUtil.e("退出房間==个人", "hangup onException->" + exception);
            }
        });
        AVChatManager.getInstance().disableRtc();
        return true;
    }

    /**
     * 退出房間==群组
     *
     * @param room_id
     * @return
     */
    public static void quitRoomGroup( String room_id,final Listener lin) {
        Log.e("退出房间", "room_id=" + room_id);
        //离开房间
        AVChatManager.getInstance().leaveRoom2(room_id, new AVChatCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                LogUtil.e("退出房间", "reject onSuccess-");
                lin.type(true);
            }

            @Override
            public void onFailed(int code) {
                LogUtil.e("退出房间", "hangup onFailed->" + code);
                lin.type(false);
            }

            @Override
            public void onException(Throwable exception) {
                LogUtil.e("退出房间", "hangup onException->" + exception);
                lin.type(false);
            }
        });
        //关闭音视频引擎
        AVChatManager.getInstance().disableRtc();
    }

    /**
     * 進入房間=群组
     *
     * @param room_id
     * @return
     */
    public static void enterRoom(final String room_id,final Listener lin) {
        Log.d("进入的房间id", "room_id=" + room_id);
        // 开启音视频引擎
        AVChatManager.getInstance().enableRtc();
        // join
        AVChatManager.getInstance().setParameter(AVChatParameters.KEY_SESSION_MULTI_MODE_USER_ROLE, AVChatUserRole.NORMAL);
        AVChatManager.getInstance().setParameter(AVChatParameters.KEY_AUDIO_REPORT_SPEAKER, true);
        AVChatManager.getInstance().setParameter(AVChatParameters.KEY_VIDEO_FIXED_CROP_RATIO, AVChatVideoCropRatio.CROP_RATIO_1_1);
        // 加入房间
        AVChatManager.getInstance().joinRoom2(room_id, AVChatType.AUDIO, new AVChatCallback<AVChatData>() {
            @Override
            public void onSuccess(AVChatData avChatData) {
                LogUtil.e("進入房間=群组", "reject onSuccess-");
                lin.type(true);
            }

            @Override
            public void onFailed(int code) {
                LogUtil.e("進入房間=群组",  "hangup onFailed->" + code);
                if(code==404){
                    InterPhoneControl.nimCreate(room_id, new InterPhoneControl.Listener() {
                        @Override
                        public void type(boolean b) {
                            if(b){
                                InterPhoneControl.enterRoom(room_id, new InterPhoneControl.Listener() {
                                    @Override
                                    public void type(boolean b) {
                                        if(b){
                                            Log.e("进入房间","进入房间成功");
                                            lin.type(true);
                                        }else{
                                            Log.e("进入房间","进入房间失败");
                                            lin.type(false);
                                        }
                                    }
                                });
                            }else{
                                lin.type(false);
                            }
                        }
                    });
                }else{
                    lin.type(false);
                }
            }

            @Override
            public void onException(Throwable exception) {
                LogUtil.e("進入房間=群组", "hangup onException->" + exception);
                lin.type(false);
            }
        });
    }

    /**
     * 网易云创建群组
     * @param gid
     * @param lin
     */
    public static void nimCreate( String gid,final Listener lin){
        AVChatManager.getInstance().createRoom(gid, "", new AVChatCallback<AVChatChannelInfo>() {
            @Override
            public void onSuccess(AVChatChannelInfo avChatChannelInfo) {
                Log.e("网易云创建群组返回数据", new GsonBuilder().serializeNulls().create().toJson(avChatChannelInfo));
                lin.type(true);
            }

            @Override
            public void onFailed(int i) {
                lin.type(false);
            }

            @Override
            public void onException(Throwable throwable) {
                lin.type(false);
            }
        });
    }

    /**
     * 結束单对单說話
     *
     * @return
     */
    public static boolean over() {
        AVChatManager.getInstance().hangUp2(GlobalStateConfig.avChatData.getChatId(), new AVChatCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                LogUtil.e("結束单对单說話", "reject onSuccess-");
            }

            @Override
            public void onFailed(int code) {
                LogUtil.e("結束单对单說話", "hangup onFailed->" + code);
            }

            @Override
            public void onException(Throwable exception) {
                LogUtil.e("結束单对单說話", "hangup onException->" + exception);
            }
        });
        AVChatManager.getInstance().disableRtc();
        return true;
    }

    public interface Listener {
        void type(boolean b);
    }

}