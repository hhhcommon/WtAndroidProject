package com.wotingfm.ui.play.find.live.view;


import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.netease.neliveplayer.NELivePlayer;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.avchat.AVChatManager;
import com.netease.nimlib.sdk.avchat.constant.AVChatResCode;
import com.netease.nimlib.sdk.avchat.constant.AVChatVideoScalingType;
import com.netease.nimlib.sdk.avchat.model.AVChatAudioFrame;
import com.netease.nimlib.sdk.avchat.model.AVChatCameraCapturer;
import com.netease.nimlib.sdk.avchat.model.AVChatNetworkStats;
import com.netease.nimlib.sdk.avchat.model.AVChatSessionStats;
import com.netease.nimlib.sdk.avchat.model.AVChatSurfaceViewRenderer;
import com.netease.nimlib.sdk.avchat.model.AVChatVideoFrame;
import com.netease.nimlib.sdk.chatroom.ChatRoomService;
import com.woting.commonplat.nim.DemoCache;
import com.woting.commonplat.nim.base.util.log.LogUtil;
import com.woting.commonplat.nim.entertainment.activity.LiveBaseActivity;
import com.woting.commonplat.nim.entertainment.constant.LiveType;
import com.woting.commonplat.nim.entertainment.helper.ChatRoomMemberCache;
import com.woting.commonplat.nim.entertainment.helper.MicHelper;
import com.woting.commonplat.nim.entertainment.http.ChatRoomHttpClient;
import com.woting.commonplat.nim.im.config.UserPreferences;
import com.woting.commonplat.nim.permission.MPermission;
import com.woting.commonplat.nim.permission.annotation.OnMPermissionDenied;
import com.woting.commonplat.nim.permission.annotation.OnMPermissionGranted;
import com.woting.commonplat.nim.video.NEVideoView;
import com.woting.commonplat.nim.video.VideoPlayer;
import com.woting.commonplat.nim.video.constant.VideoConstant;
import com.woting.commonplat.utils.SequenceUUID;
import com.wotingfm.R;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.constant.StringConstant;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.common.utils.DialogUtils;
import com.wotingfm.common.utils.GlideUtils;
import com.wotingfm.common.utils.T;
import com.wotingfm.common.utils.TimeUtil;
import com.wotingfm.ui.bean.AnchorInfo;
import com.wotingfm.ui.bean.LiveBean;

import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 直播
 */
public class LiveRoomActivity extends LiveBaseActivity implements VideoPlayer.VideoPlayerProxy {
    private static final String TAG = LiveRoomActivity.class.getSimpleName();
    private final int BASIC_PERMISSION_REQUEST_CODE = 110;

    // view
    private AVChatSurfaceViewRenderer videoRender;
    private NEVideoView videoView;
    private View closeBtn;
    private View liveFinishLayout;
    private TextView preparedText;

    // 播放器
    private VideoPlayer videoPlayer;
    // state
    private boolean isStartLive = false; // 推流是否开始

    private AVChatCameraCapturer mVideoCapturer;
    private Dialog dialog;
    private View tv_edit;
    private View lin_anchor;
    private AnchorInfo anchor;
    private static LiveRoomActivity context;
    private TextView tv_close;
    private TextView tv_beginTime,tv_anchor_name;
    private String begin_time;
    private String title;
    private String avatar;
    private ImageView master_head;
    private TextView tvTrailerFollow;
    private boolean focus;
    private ImageView img_anchor_head;
    private String anchor_name;

    /**
     * 静态方法 启动观众
     *
     * @param context 上下文
     */
    public static void startAudience(Context context, LiveBean.DataBean db) {
        Intent intent = new Intent();
        intent.setClass(context, LiveRoomActivity.class);
        intent.putExtra(EXTRA_ROOM_ID, db.live_number);
        intent.putExtra("anchor_id", db.owner.id);
        intent.putExtra("anchor_name", db.owner.name);
        intent.putExtra("begin_time", db.begin_at);
        intent.putExtra("title", db.title);
        intent.putExtra("avatar", db.owner.avatar);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        registerAudienceObservers(true);
        findViews();
        findInputViews();
        updateRoomUI(true);
        enterRoom();
        requestBasicPermission(); // 申请APP基本权限.同意之后，请求拉流
    }

    private void registerAudienceObservers(boolean register) {
        AVChatManager.getInstance().observeAVChatState(this, register);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_live_room;
    }

    @Override
    protected int getLayoutId() {
        return R.id.audience_layout;
    }

    protected void findViews() {
        super.findViews();
        rootView = findView(R.id.audience_layout);
        videoRender = findView(R.id.video_render);
        videoRender.setZOrderMediaOverlay(false);
        videoRender.setVisibility(View.GONE);
        closeBtn = findView(R.id.close_btn);

        lin_anchor = findViewById(R.id.lin_anchor);
        tv_edit = findView(R.id.tv_edit);
        lin_anchor.setOnClickListener(buttonClickListener);
        tv_edit.setOnClickListener(buttonClickListener);
        closeBtn.setOnClickListener(buttonClickListener);

        // 直播结束布局
        liveFinishLayout = findView(R.id.re_finish);
        liveFinishLayout.setOnClickListener(buttonClickListener);
        tv_close = (TextView) findView(R.id.tv_close);
        tv_close.setOnClickListener(buttonClickListener);
        preparedText = findView(R.id.prepared_text);
        master_head = findView(R.id.master_head);
        tv_beginTime = (TextView) findView(R.id.tv_beginTime);

        tvTrailerFollow = (TextView) findView(R.id.tvTrailerFollow);
        tvTrailerFollow.setOnClickListener(buttonClickListener);

        img_anchor_head = findView(R.id.img_anchor_head);
        tv_anchor_name = findView(R.id.tv_anchor_name);

        if (begin_time != null) {
            setBeginTime();
            mHandler.postDelayed(timeRunnable, 1000);
        }
        if (!TextUtils.isEmpty(title)) {
            masterNameText.setText(title);
        } else {
            masterNameText.setText("直播Title");
        }
        if (!TextUtils.isEmpty(anchor_name)) {
            tv_anchor_name.setText(anchor_name);
        } else {
            tv_anchor_name.setText("直播");
        }
        if (!TextUtils.isEmpty(avatar)) {
            GlideUtils.loadImageViewRound(avatar, master_head, 150, 150);
            GlideUtils.loadImageViewRound(avatar, img_anchor_head, 150, 150);

        } else {
            GlideUtils.loadImageViewRound(R.mipmap.oval_defut_photo, master_head, 150, 150);
            GlideUtils.loadImageViewRound(R.mipmap.oval_defut_photo, img_anchor_head, 150, 150);
        }

    }

    @Override
    protected void parseIntent() {
        super.parseIntent();
//        roomId = getIntent().getStringExtra(EXTRA_ROOM_ID);
        roomId = "11352015";
        String anchor_id = getIntent().getStringExtra("anchor_id");
        begin_time = getIntent().getStringExtra("begin_time");
        title = getIntent().getStringExtra("title");
        avatar = getIntent().getStringExtra("avatar");
        anchor_name= getIntent().getStringExtra("anchor_name");
        getAnchorInfo(anchor_id);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    setBeginTime();
                    break;
            }
        }
    };

    // 更新时间
    private Runnable timeRunnable = new Runnable() {
        @Override
        public void run() {
            mHandler.sendEmptyMessage(1);
            mHandler.postDelayed(this, 1000);
        }
    };

    private void setBeginTime() {
        long time = System.currentTimeMillis() - Long.parseLong(begin_time);
        tv_beginTime.setText(TimeUtil.formatterTime(time));
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 恢复播放
        if (videoPlayer != null) {
            videoPlayer.onActivityResume();
        }
    }

    @Override
    protected void onDestroy() {
        if (timeRunnable != null) {
            mHandler.removeCallbacks(timeRunnable);
            timeRunnable = null;
        }
        // 释放资源
        if (videoPlayer != null) {
            videoPlayer.resetVideo();
        }
        registerAudienceObservers(false);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishLive();
    }

    private void finishLive() {
        if (isStartLive) {
            logoutChatRoom();
        } else {
            NIMClient.getService(ChatRoomService.class).exitChatRoom(roomId);
            clearChatRoom();
        }
    }

    // 离开聊天室
    private void logoutChatRoom() {
        if (isMeOnMic) {
            MicHelper.getInstance().leaveChannel(false, liveType == LiveType.VIDEO_TYPE, true, meetingName);
            mVideoCapturer = null;
        }
        NIMClient.getService(ChatRoomService.class).exitChatRoom(roomId);
        clearChatRoom();
//        EasyAlertDialogHelper.createOkCancelDiolag(this, null, getString(R.string.finish_confirm),
//                getString(R.string.confirm), getString(R.string.cancel), true,
//                new EasyAlertDialogHelper.OnDialogActionListener() {
//                    @Override
//                    public void doCancelAction() {
//                    }
//
//                    @Override
//                    public void doOkAction() {
//                        if (isMeOnMic) {
//                            MicHelper.getInstance().leaveChannel(false, liveType == LiveType.VIDEO_TYPE, true, meetingName);
//                            mVideoCapturer = null;
//                        }
//                        NIMClient.getService(ChatRoomService.class).exitChatRoom(roomId);
//                        clearChatRoom();
//                    }
//                }).show();
    }

    private void clearChatRoom() {
        ChatRoomMemberCache.getInstance().clearRoomCache(roomId);
        finish();
    }

    @Override
    protected void doCloseInteraction() {

    }

    @Override
    protected void onConnected() {

    }

    @Override
    protected void onDisconnected() {

    }

    /********************************
     * 初始化
     *******************************/

    private void fetchLiveUrl() {
        ChatRoomHttpClient.getInstance().studentEnterRoom("13200000008", roomId, new ChatRoomHttpClient.ChatRoomHttpCallback<ChatRoomHttpClient.EnterRoomParam>() {
            @Override
            public void onSuccess(ChatRoomHttpClient.EnterRoomParam enterRoomParam) {
                if (enterRoomParam.getAvType().equals("AUDIO")) {
                    liveType = LiveType.AUDIO_TYPE;
                } else if (enterRoomParam.getAvType().equals("VIDEO")) {
                    liveType = LiveType.VIDEO_TYPE;
                    setRequestedOrientation(enterRoomParam.getOrientation() == 1 ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT : ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }
                pullUrl = enterRoomParam.getPullUrl();

                initAudienceParam();
            }

            @Override
            public void onFailed(int code, String errorMsg) {
                if (code == -1) {
//                    Toast.makeText(AudienceActivity.this, "无法连接服务器, code=" + code, Toast.LENGTH_SHORT).show();
                } else {
//                    Toast.makeText(AudienceActivity.this, "观众进入房间失败, code=" + code + ", errorMsg:" + errorMsg, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initAudienceParam() {
        // 初始化拉流
        videoView = findView(R.id.video_view);
        videoRender.setVisibility(View.GONE);
        videoView.setVisibility(View.VISIBLE);
        videoPlayer = new VideoPlayer(this, videoView, null, pullUrl, UserPreferences.getPlayerStrategy(), this, VideoConstant.VIDEO_SCALING_MODE_FILL_BLACK);
        videoPlayer.openVideo();
    }

    private AnchorDialog anchorDialog;
    private View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.close_btn:
                    finishLive();
                    break;
                case R.id.tv_edit:
                    com.woting.commonplat.nim.entertainment.activity.EditDialog.dialogShow();
                    break;
                case R.id.tv_close:
                    NIMClient.getService(ChatRoomService.class).exitChatRoom(roomId);
                    clearChatRoom();
                    break;
                case R.id.switch_btn:
                    mVideoCapturer.switchCamera();
                    break;
                case R.id.lin_anchor:
                    if (anchorDialog == null) {
                        anchorDialog = new AnchorDialog(context);
                    }
                    if (anchor != null)
                        anchorDialog.setMenuData(anchor);
                    anchorDialog.show();
                    break;
                case R.id.tvTrailerFollow:
                    if (focus) {
                        String id = BSApplication.SharedPreferences.getString(StringConstant.USER_ID, "");
                        unFollowAnchor(id, anchor);
                    } else {
                        String id = BSApplication.SharedPreferences.getString(StringConstant.USER_ID, "");
                        followAnchor(id, anchor);
                    }
                    break;
            }
        }
    };

    /**
     * 基本权限管理
     */
    private void requestBasicPermission() {
        MPermission.with(this)
                .addRequestCode(BASIC_PERMISSION_REQUEST_CODE)
                .permissions(
                        Manifest.permission.READ_PHONE_STATE)
                .request();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        MPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @OnMPermissionGranted(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionSuccess() {
        fetchLiveUrl();
        Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
    }

    @OnMPermissionDenied(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionFailed() {
        finish();
        Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show();
    }

    @OnMPermissionGranted(LIVE_PERMISSION_REQUEST_CODE)
    public void onLivePermissionGranted() {
        Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTakeSnapshotResult(String s, boolean b, String s1) {

    }

    @Override
    public void onConnectionTypeChanged(int i) {

    }

    @Override
    public void onAVRecordingCompletion(String s, String s1) {

    }

    @Override
    public void onAudioRecordingCompletion(String s) {

    }

    @Override
    public void onLowStorageSpaceWarning(long l) {

    }

    @Override
    public void onFirstVideoFrameAvailable(String s) {

    }

    @Override
    public void onVideoFpsReported(String s, int i) {

    }

    @Override
    public void onLeaveChannel() {

    }

    @Override
    public void onJoinedChannel(int i, String s, String s1, int i1) {
        if (i == AVChatResCode.JoinChannelCode.OK && liveType == LiveType.AUDIO_TYPE) {
            AVChatManager.getInstance().setSpeaker(true);
        }
    }

    @Override
    public void onUserJoined(String s) {
        if (liveType == LiveType.VIDEO_TYPE && s.equals(roomInfo.getCreator())) {
            if (s.equals(DemoCache.getAccount())) {
                livingBg.setVisibility(View.VISIBLE);
            }
            AVChatManager.getInstance().setupRemoteVideoRender(roomInfo.getCreator(), videoRender, false, AVChatVideoScalingType.SCALE_ASPECT_BALANCED);
        }
        if (liveType != LiveType.VIDEO_TYPE) {
            preparedText.setVisibility(View.GONE);
        }
    }

    @Override
    public void onUserLeave(String s, int i) {
        LogUtil.d(TAG, "onUserLeave");
        if (s.equals(roomInfo.getCreator())) {
            MicHelper.getInstance().leaveChannel(false, liveType == LiveType.VIDEO_TYPE, true, meetingName);
            mVideoCapturer = null;
        }
        resetConnectionView();
    }

    @Override
    public void onProtocolIncompatible(int i) {

    }

    @Override
    public void onDisconnectServer() {

    }

    @Override
    public void onNetworkQuality(String s, int i, AVChatNetworkStats avChatNetworkStats) {

    }

    @Override
    public void onCallEstablished() {
        LogUtil.d(TAG, "audience onCallEstablished");
        AVChatManager.getInstance().enableAudienceRole(false);
    }

    @Override
    public void onDeviceEvent(int i, String s) {

    }

    @Override
    public void onFirstVideoFrameRendered(String s) {
        if (liveFinishLayout.getVisibility() == View.VISIBLE) {
            liveFinishLayout.setVisibility(View.GONE);
        }
        if (s.equals(roomInfo.getCreator())) {
            preparedText.setVisibility(View.GONE);
        }
        if (s.equals(DemoCache.getAccount())) {
            livingBg.setVisibility(View.GONE);
        }
    }

    @Override
    public void onVideoFrameResolutionChanged(String s, int i, int i1, int i2) {

    }

    @Override
    public boolean onVideoFrameFilter(AVChatVideoFrame frame, boolean maybeDualInput) {
//        LogUtil.i(TAG, "on video frame filter, avchatVideoFrame:" + avChatVideoFrame + ", gpuEffect:" + mGPUEffect);
        return true;
    }

    @Override
    public boolean onAudioFrameFilter(AVChatAudioFrame avChatAudioFrame) {
        return true;
    }

    @Override
    public void onAudioDeviceChanged(int i) {

    }

    @Override
    public void onReportSpeaker(Map<String, Integer> map, int i) {

    }

    @Override
    public void onAudioMixingEvent(int i) {

    }

    @Override
    public void onSessionStats(AVChatSessionStats avChatSessionStats) {
        Log.e("直播中返回数据", new GsonBuilder().serializeNulls().create().toJson(avChatSessionStats));

    }

    @Override
    public void onLiveEvent(int i) {
//        Toast.makeText(AudienceActivity.this, "onLiveEvent:" + i, Toast.LENGTH_SHORT).show();
    }

    /***************************
     * VideoPlayer.VideoPlayerProxy
     ***************************/

    @Override
    public boolean isDisconnected() {
        return false;
    }

    @Override
    public void onError() {
        LogUtil.d(TAG, "on error, show finish layout");
        preparedText.setVisibility(View.VISIBLE);
        showFinishLayout();
    }

    @Override
    public void onCompletion() {
        LogUtil.d(TAG, "on completion, show finish layout");
        isStartLive = false;
        showFinishLayout();
    }

    @Override
    public void onPrepared() {
        LogUtil.d(TAG, "on prepared, hide preparedText");
        if (liveType == LiveType.NOT_ONLINE) {
            return;
        }
        isStartLive = true;
        preparedText.setVisibility(View.GONE);
        liveFinishLayout.setVisibility(View.GONE);
        videoRender.setVisibility(View.GONE);
        videoView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onInfo(NELivePlayer mp, int what, int extra) {
        // web端推流的时候，不报onPrepared上来，只能用onInfo处理了。哼
        if ((what == NELivePlayer.NELP_FIRST_VIDEO_RENDERED || what == NELivePlayer.NELP_FIRST_AUDIO_RENDERED)&& liveType != LiveType.NOT_ONLINE) {
            LogUtil.d(TAG, "on info NELP_FIRST_VIDEO_RENDERED, hide preparedText");
            isStartLive = true;
            preparedText.setVisibility(View.GONE);
            liveFinishLayout.setVisibility(View.GONE);
            videoRender.setVisibility(View.GONE);
            videoView.setVisibility(View.VISIBLE);
        }
    }

    // 显示直播已结束布局
    private void showFinishLayout() {
        liveFinishLayout.setVisibility(View.VISIBLE);
        inputPanel.collapse(true);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        findViews();
        initAudienceParam();
    }


    /**
     * 展示弹出框
     */
    private void dialogShow() {
        dialog = DialogUtils.Dialog(this);
    }

    /**
     * 取消弹出框
     */
    private void dialogCancel() {
        if (dialog != null) dialog.dismiss();
    }

    // 执行请求
    private void getAnchorInfo(String uid) {
        RetrofitUtils.getInstance().getAnchorInfo(uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<AnchorInfo>() {
                    @Override
                    public void call(AnchorInfo o) {
                        try {
                            Log.e("获取主播信息返回数据", new GsonBuilder().serializeNulls().create().toJson(o));
                            anchor = (AnchorInfo) o;
                            focus= anchor.data.user.had_followd;
                            //填充UI
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }

    /**
     * 打开新的 Fragment
     */
    public static void open(Fragment frg) {
        context.getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_right_in, R.anim.slide_left_out,
                        R.anim.slide_left_in, R.anim.slide_right_out)
                .add(R.id.audience_layout, frg)
                .addToBackStack(SequenceUUID.getUUID())
                .commit();
    }

    /**
     * 关闭已经打开的 Fragment
     */
    public static void close() {
        if (context != null && context.getSupportFragmentManager() != null) {
            context.getSupportFragmentManager().popBackStackImmediate();
        }
    }

    private void followAnchor(String uid, final AnchorInfo sw) {
        RetrofitUtils.getInstance().followAnchor(uid, sw.data.user.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object Object) {
                        focus = true;
                        isFollow(true);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        T.getInstance().showToast("关注失败");
                    }
                });
    }

    private void unFollowAnchor(String uid, final AnchorInfo sw) {
        RetrofitUtils.getInstance().unFollowAnchor(uid, sw.data.user.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object s) {
                        focus = false;
                        isFollow(false);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        T.getInstance().showToast("取消关注失败");
                    }
                });
    }

    private void isFollow(boolean isFollow) {
        if (isFollow == true) {
            tvTrailerFollow.setText("已关注");
        } else {
            tvTrailerFollow.setText("关注");
        }
    }

}
