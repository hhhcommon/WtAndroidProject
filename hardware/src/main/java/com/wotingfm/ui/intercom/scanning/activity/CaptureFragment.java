package com.wotingfm.ui.intercom.scanning.activity;

import android.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.zxing.Result;
import com.woting.commonplat.manager.PhoneMsgManager;
import com.wotingfm.R;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.utils.ToastUtils;
import com.wotingfm.ui.intercom.group.groupnews.add.view.GroupNewsForAddFragment;
import com.wotingfm.ui.intercom.group.groupnews.noadd.view.GroupNewsForNoAddFragment;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;
import com.wotingfm.ui.intercom.person.personmessage.view.PersonMessageFragment;
import com.wotingfm.ui.intercom.scanning.DecodeThread;
import com.wotingfm.ui.intercom.scanning.InactivityTimer;
import com.wotingfm.ui.intercom.scanning.handle.CaptureActivityHandler;
import com.wotingfm.ui.intercom.scanning.manager.BeepManager;
import com.wotingfm.ui.intercom.scanning.manager.CameraManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

/**
 * 这个活动打开相机和实际扫描一个背景线程。
 * 它吸引取景器来帮助用户把条码正确,显示了图像处理发生的反馈,然后覆盖当扫描结果是成功的。
 *
 * @author 辛龙
 *         2016年1月21日
 */
public final class CaptureFragment extends Fragment implements SurfaceHolder.Callback, OnClickListener {
    private static final String TAG = CaptureFragment.class.getSimpleName();
    private CameraManager cameraManager;
    private CaptureActivityHandler handler;
    private InactivityTimer inactivityTimer;
    private BeepManager beepManager;
    private SurfaceView scanPreview = null;
    private LinearLayout scanContainer;
    private RelativeLayout scanCropView;
    private ImageView scanLine;
    private Rect mCropRect = null;
    private boolean isHasSurface = false;
    private TranslateAnimation animation;
    private View rootView;

    public Handler getHandler() {
        return handler;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        Window window = this.getActivity().getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        inactivityTimer = new InactivityTimer(this.getActivity());
        beepManager = new BeepManager(this.getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.activity_capture, container, false);
            rootView.setOnClickListener(this);
            inItView();
        }
        return rootView;
    }

    private void inItView() {
        rootView.findViewById(R.id.head_left_btn).setOnClickListener(this);
        TextView name = (TextView) rootView.findViewById(R.id.tv_center);
        name.setText("扫一扫");
        scanPreview = (SurfaceView) rootView.findViewById(R.id.capture_preview);
        scanContainer = (LinearLayout) rootView.findViewById(R.id.capture_container);
        scanCropView = (RelativeLayout) rootView.findViewById(R.id.capture_crop_view);
        LayoutParams pr2 = scanCropView.getLayoutParams();
        pr2.width = PhoneMsgManager.ScreenWidth - 200;
        pr2.height = PhoneMsgManager.ScreenWidth - 200;
        scanCropView.setLayoutParams(pr2);

        scanLine = (ImageView) rootView.findViewById(R.id.capture_scan_line);
        animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.95f);
        animation.setDuration(4500);
        animation.setRepeatCount(-1);
        animation.setRepeatMode(Animation.RESTART);
        scanLine.startAnimation(animation);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left_btn:
                InterPhoneActivity.close();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // CameraManager must be initialized here, not in onCreate(). This is
        // necessary because we don't
        // want to open the camera driver and measure the screen size if we're
        // going to show the help on
        // first launch. That led to bugs where the scanning rectangle was the
        // wrong size and partially
        // off screen.
        cameraManager = new CameraManager(this.getActivity());
        handler = null;
        if (isHasSurface) {
            // The activity was paused but not stopped, so the surface still
            // exists. Therefore
            // surfaceCreated() won't be called, so init the camera here.
            initCamera(scanPreview.getHolder());
        } else {
            // Install the callback and wait for surfaceCreated() to init the
            // camera.
            scanPreview.getHolder().addCallback(this);
        }
        inactivityTimer.onResume();
    }

    @Override
    public void onPause() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        inactivityTimer.onPause();
        beepManager.close();
        cameraManager.closeDriver();
        if (!isHasSurface) {
            scanPreview.getHolder().removeCallback(this);
        }
        super.onPause();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (holder == null) {
            Log.e(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
        }
        if (!isHasSurface) {
            isHasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isHasSurface = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    /**
     * 有效的条形码被发现,所以给的成功和显示
     * 结果。
     */
    public void handleDecode(Result rawResult, Bundle bundle) {
        inactivityTimer.onActivity();
        beepManager.playBeepSoundAndVibrate();
        String news = rawResult.getText().trim();
        if (news != null && !news.equals("")) {
            if (news.contains("http")) {
                bundle.putString("result", news);
                startActivity(new Intent(this.getActivity(), ResultActivity.class).putExtras(bundle));
            } else {
                try {
                    JSONObject js = new JSONObject(news);
                    String type = js.getString("type");
                    String id = js.getString("id");
                    if (type != null && type.trim().equals("group")) {
                        jumpGroup(id);
                    } else if (type != null && type.trim().equals("person")) {
                        jumpPerson(id);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
//            ToastUtils.show_always(this.getActivity(), news + "");
        }
    }

    public void handleDecode(Result rawResult) {

    }

    /**
     * 跳转到好友信息界面
     *
     * @param id
     */
    private void jumpPerson(String id) {
        InterPhoneActivity.close();
        if (judgeFriends(id)) {
            PersonMessageFragment fragment = new PersonMessageFragment();
            Bundle bundle = new Bundle();
            bundle.putString("type", "true");
            bundle.putString("id", id);
            fragment.setArguments(bundle);
            InterPhoneActivity.open(fragment);
        } else {
            PersonMessageFragment fragment = new PersonMessageFragment();
            Bundle bundle = new Bundle();
            bundle.putString("type", "false");
            bundle.putString("id", id);
            fragment.setArguments(bundle);
            InterPhoneActivity.open(fragment);
        }
    }

    /**
     * 跳转到群组界面
     *
     * @param id
     */
    private void jumpGroup(String id) {
        InterPhoneActivity.close();
        if (judgeGroups(id)) {
            GroupNewsForAddFragment fragment = new GroupNewsForAddFragment();
            Bundle bundle = new Bundle();
            bundle.putString("id", id);
            bundle.putString("type", "true");
            fragment.setArguments(bundle);
            InterPhoneActivity.open(fragment);
        } else {
            GroupNewsForNoAddFragment fragment = new GroupNewsForNoAddFragment();
            Bundle bundle = new Bundle();
            bundle.putString("id", id);
            fragment.setArguments(bundle);
            InterPhoneActivity.open(fragment);
        }
    }

    /**
     * 判断该群组是不是自己所在的群组
     *
     * @param id
     * @return
     */
    private boolean judgeGroups(String id) {
        boolean b = false;
        List<Contact.group> list = GlobalStateConfig.list_group;
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                String _id = list.get(i).getId();
                if (_id != null && !_id.equals("")) {
                    if (id.equals(_id)) {
                        b = true;
                        break;
                    }
                }
            }
        }
        return b;
    }

    /**
     * 判断该用户是否是自己好友
     *
     * @param id
     * @return
     */
    private boolean judgeFriends(String id) {
        boolean b = false;
        List<Contact.user> list = GlobalStateConfig.list_person;
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                String _id = list.get(i).getId();
                if (_id != null && !_id.equals("")) {
                    if (id.equals(_id)) {
                        b = true;
                        break;
                    }
                }
            }
        }
        return b;
    }


    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            Log.w(TAG, "initCamera() while already open -- late SurfaceView callback?");
            return;
        }
        try {
            cameraManager.openDriver(surfaceHolder);
            // Creating the handler starts the preview, which can also throw a
            // RuntimeException.
            if (handler == null) {
                handler = new CaptureActivityHandler(this, cameraManager, DecodeThread.ALL_MODE);
            }
            initCrop();
        } catch (IOException ioe) {
            Log.w(TAG, ioe);
            displayFrameworkBugMessageAndExit();
        } catch (RuntimeException e) {
            // Barcode Scanner has seen crashes in the wild of this variety:
            // java.?lang.?RuntimeException: Fail to connect to camera service
            Log.w(TAG, "Unexpected error initializing camera", e);
            displayFrameworkBugMessageAndExit();
        }
    }

    private void displayFrameworkBugMessageAndExit() {
        // camera error
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle(getString(R.string.app_name));
        builder.setMessage("相机打开出错，请稍后重试");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                InterPhoneActivity.close();
            }
        });

        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                InterPhoneActivity.close();
            }
        });

        builder.show();
    }

    public void restartPreviewAfterDelay(long delayMS) {
        if (handler != null) {
            handler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
        }
    }

    public Rect getCropRect() {
        return mCropRect;
    }

    /**
     * 初始化截取的矩形区域
     */
    private void initCrop() {
        int cameraWidth = cameraManager.getCameraResolution().y;
        int cameraHeight = cameraManager.getCameraResolution().x;
        /** 获取布局中扫描框的位置信息 */
        int[] location = new int[2];
        scanCropView.getLocationInWindow(location);
        int cropLeft = location[0];
        int cropTop = location[1] - getStatusBarHeight();
        int cropWidth = scanCropView.getWidth();
        int cropHeight = scanCropView.getHeight();
        /** 获取布局容器的宽高 */
        int containerWidth = scanContainer.getWidth();
        int containerHeight = scanContainer.getHeight();
        /** 计算最终截取的矩形的左上角顶点x坐标 */
        int x = cropLeft * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的左上角顶点y坐标 */
        int y = cropTop * cameraHeight / containerHeight;
        /** 计算最终截取的矩形的宽度 */
        int width = cropWidth * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的高度 */
        int height = cropHeight * cameraHeight / containerHeight;
        /** 生成最终的截取的矩形 */
        mCropRect = new Rect(x, y, width + x, height + y);
    }

    private int getStatusBarHeight() {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        inactivityTimer.shutdown();
        scanPreview = null;
        scanContainer = null;
        scanCropView = null;
        scanLine = null;
        inactivityTimer = null;
        beepManager = null;
        animation = null;
        cameraManager = null;
    }


}
