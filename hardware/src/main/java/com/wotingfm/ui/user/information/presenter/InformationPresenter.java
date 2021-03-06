package com.wotingfm.ui.user.information.presenter;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.ObjectMetadata;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.woting.commonplat.manager.FileManager;
import com.woting.commonplat.nim.DemoCache;
import com.woting.commonplat.nim.base.util.string.MD5;
import com.woting.commonplat.nim.im.business.RegisterHttpClient;
import com.woting.commonplat.nim.im.ui.dialog.DialogMaker;
import com.woting.commonplat.utils.SequenceUUID;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.constant.BroadcastConstants;
import com.wotingfm.common.live.preference.Preferences;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.common.net.upLoadImage;
import com.wotingfm.common.utils.ToastUtils;
import com.wotingfm.ui.photocut.PhotoCutActivity;
import com.wotingfm.ui.user.information.model.InformationModel;
import com.wotingfm.ui.user.information.view.InformationFragment;
import com.wotingfm.ui.user.logo.LogoActivity;
import com.wotingfm.ui.user.preference.view.PreferenceFragment;

import org.json.JSONObject;

import java.io.File;

/**
 * 登录界面处理器
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class InformationPresenter {

    private InformationFragment activity;
    private InformationModel model;
    private final int TO_GALLERY = 5;
    private final int TO_CAMERA = 6;
    private final int PHOTO_REQUEST_CUT = 7;    // 标识 跳转到图片裁剪界面
    private int Code;    // 标识
    private String outputFilePath,acc_id,pws,url,UUID;
    private MessageReceiver Receiver;

    public InformationPresenter(InformationFragment activity) {
        this.activity = activity;
        this.model = new InformationModel();
        Bundle bundle = activity.getArguments();
        if (bundle != null) acc_id = bundle.getString("acc_id");
        if (bundle != null) pws = bundle.getString("pws");
        setReceiver();
    }

    // 发送网络请求
    public void send(final String name) {
        if (checkData(name)) {
            activity.dialogShow();
            model.loadNews(name, url, new InformationModel.OnLoadInterface() {
                @Override
                public void onSuccess(Object o) {
                    activity.dialogCancel();
                    dealSuccess(o, name);
                }

                @Override
                public void onFailure(String msg) {
                    activity.dialogCancel();
                    jump();// 跳转到偏好设置界面
                }
            });
        }
    }

    // 处理返回数据
    private void dealSuccess(Object o, String name) {
        try {
            Gson g = new GsonBuilder().serializeNulls().create();
            String s = g.toJson(o);
            JSONObject js = new JSONObject(s);
            int ret = js.getInt("ret");
            Log.e("ret", String.valueOf(ret));
            if (ret == 0) {
                model.saveImg(url);
                model.saveName(name);
                yxRegister(name);
            }else{
                jump();// 跳转到偏好设置界面

            }
        } catch (Exception e) {
            e.printStackTrace();
            jump();// 跳转到偏好设置界面
        }
    }

    // 跳转到偏好设置界面
    private void jump() {
        activity.getActivity().sendBroadcast(new Intent(BroadcastConstants.LOGIN));
        PreferenceFragment fragment = new PreferenceFragment();
        Bundle bundle = new Bundle();
        bundle.putString("fromType", "login");
        fragment.setArguments(bundle);
        LogoActivity.open(fragment);
    }

    /**
     * 数据校验
     *
     * @param name
     * @return
     */
    private boolean checkData(String name) {
        if (url == null || url.trim().equals("")) {
            Toast.makeText(activity.getActivity(), "头像不能为空", Toast.LENGTH_LONG).show();
            return false;
        }
        if (name == null || name.trim().equals("")) {
            Toast.makeText(activity.getActivity(), "昵称能为空", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    /**
     * 拍照
     */
    public void camera() {
        Intent intents = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri outputFileUri;
        String savePath = FileManager.getImageSaveFilePath(BSApplication.mContext);
        FileManager.createDirectory(savePath);
        String fileName = System.currentTimeMillis() + ".jpg";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//如果是7.0android系统
            ContentValues contentValues = new ContentValues(1);
            File file = new File(savePath, fileName);
            outputFilePath = file.getAbsolutePath();
            contentValues.put(MediaStore.Images.Media.DATA,outputFilePath);
            outputFileUri=  activity.getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);
        }else{
            File file = new File(savePath, fileName);
            outputFileUri = Uri.fromFile(file);
        }
        intents.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        Code = TO_CAMERA;
        activity.getActivity().startActivityForResult(intents, TO_CAMERA);
    }


    /**
     * 调用图库
     */
    public void photoAlbum() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        Code = TO_GALLERY;
        activity.startActivityForResult(intent, TO_GALLERY);
    }

    // 图片裁剪
    private void startPhotoZoom(Uri uri) {
        Intent intent = new Intent(activity.getActivity(), PhotoCutActivity.class);
        intent.putExtra("URI", uri.toString());
        intent.putExtra("type", 1);
        Code = PHOTO_REQUEST_CUT;
        activity.startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    private void setResults(int resultCode, Uri uri, String Path) {
        switch (Code) {
            case TO_GALLERY:// 照片的原始资源地址
                if (resultCode == Activity.RESULT_OK) {
                    String path;
                    int sdkVersion = Integer.valueOf(Build.VERSION.SDK);
                    if (sdkVersion >= 19) {
                        path = model.getPath_above19(activity.getActivity(), uri);
                    } else {
                        path = model.getFilePath_below19(activity.getActivity(), uri);
                    }
                    startPhotoZoom(Uri.parse(path));
                }
                break;
            case TO_CAMERA:
                if (resultCode == Activity.RESULT_OK) {
                    startPhotoZoom(Uri.parse(outputFilePath));
                }
                break;
            case PHOTO_REQUEST_CUT:
                if (resultCode == 1) {
                    activity.dialogShow();
                    chuLi(Path);
                }
                break;
        }
    }

    // 上传头像==异步
    private void upImageUrl(final String strPath) {
        // 指定数据类型，没有指定会自动根据后缀名判断
        ObjectMetadata objectMeta = new ObjectMetadata();
        objectMeta.setContentType("image/jpeg");
        // 构造上传请求
        final String UUID = SequenceUUID.getPureUUID() + ".jpg";
        PutObjectRequest put = new PutObjectRequest(upLoadImage.BUCKET_NAME, upLoadImage.objectKey + UUID, strPath);
        put.setMetadata(objectMeta);
        // 异步上传时可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                // 在这里可以实现进度条展现功能
                Log.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
            }
        });
        OSSAsyncTask task = upLoadImage.getInstance().oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                activity.dialogCancel();
                Log.d("PutObject", "UploadSuccess");
                Log.d("ETag", result.getETag());
                Log.d("RequestId", result.getRequestId());
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                }
                return;
            }
        });
    }

    // 上传头像==同步步
    private void chuLi(final String strPath) {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    activity.dialogCancel();
                    url = upLoadImage.URL + UUID;
                    activity.setImageUrl(url);
                    ToastUtils.show_always(activity.getActivity(), "图片上传成功");
                } else {
                    activity.dialogCancel();
                    ToastUtils.show_always(activity.getActivity(), "图片上传失败，请重新上传");
                }
            }
        };

        new Thread() {
            @Override
            public void run() {
                super.run();
                Message msg = new Message();
                try {
                    // 指定数据类型，没有指定会自动根据后缀名判断
                    ObjectMetadata objectMeta = new ObjectMetadata();
                    objectMeta.setContentType("image/jpeg");
                    // 构造上传请求
                    UUID = SequenceUUID.getPureUUID() + ".jpg";
                    PutObjectRequest put = new PutObjectRequest(upLoadImage.BUCKET_NAME, upLoadImage.objectKey + UUID, strPath);
                    put.setMetadata(objectMeta);
                    try {
                        PutObjectResult putObjectResult = upLoadImage.getInstance().oss.putObject(put);
                        Log.d("PutObject", "UploadSuccess");
                        Log.d("ETag", putObjectResult.getETag());
                        Log.d("RequestId", putObjectResult.getRequestId());
                        msg.what = 1;
                    } catch (ClientException e) {
                        // 本地异常如网络异常等
                        e.printStackTrace();
                        msg.what = -1;
                    } catch (ServiceException e) {
                        // 服务异常
                        Log.e("RequestId", e.getRequestId());
                        Log.e("ErrorCode", e.getErrorCode());
                        Log.e("HostId", e.getHostId());
                        Log.e("RawMessage", e.getRawMessage());
                        msg.what = -1;
                    }
                } catch (Exception e) {
                    // 异常处理
                    msg.what = -1;
                }
                handler.sendMessage(msg);
            }
        }.start();
    }

    // 设置广播接收器
    private void setReceiver() {
        if (Receiver == null) {
            Receiver = new MessageReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(BroadcastConstants.IMAGE_UPLOAD);
            activity.getActivity().registerReceiver(Receiver, filter);
        }
    }

    class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BroadcastConstants.IMAGE_UPLOAD)) {
                int resultCode = intent.getIntExtra("resultCode", 0);
                Uri uri = null;
                try {
                    uri = Uri.parse(intent.getStringExtra("uri"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String path = null;
                try {
                    path = intent.getStringExtra("path");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                setResults(resultCode, uri, path);
            }
        }
    }
    ///////////////////////////////////////
    private AbortableFuture<LoginInfo> loginRequest;

    private void onLoginDone() {
        loginRequest = null;
        DialogMaker.dismissProgressDialog();
    }

    private void saveLoginInfo(final String account, final String token) {
        Preferences.saveUserAccount(account);
        Preferences.saveUserToken(token);
    }

    // 云信的登录
    private void loginYx(final String account, final String token) {
        // 登录
        loginRequest = NIMClient.getService(AuthService.class).login(new LoginInfo(account, tokenFromPassword(token)));
        loginRequest.setCallback(new RequestCallback<LoginInfo>() {
            @Override
            public void onSuccess(LoginInfo param) {
                onLoginDone();
                DemoCache.setAccount(account);
                saveLoginInfo(account, token);

                // 发送登录广播通知所有界面
                RetrofitUtils.INSTANCE = null;

                // 发送登录广播通知所有界面
                activity.getActivity().sendBroadcast(new Intent(BroadcastConstants.LOGIN));
                jump();// 跳转到偏好设置界面
            }

            @Override
            public void onFailed(int code) {
                onLoginDone();
                jump();// 跳转到偏好设置界面

            }

            @Override
            public void onException(Throwable exception) {
                onLoginDone();
            }
        });
    }

    // 云信的注册
    private void yxRegister(String nickName){
        RegisterHttpClient.getInstance().register(acc_id, nickName, pws, new RegisterHttpClient.ContactHttpCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                loginYx(acc_id, pws);
            }

            @Override
            public void onFailed(int code, String errorMsg) {
                jump();// 跳转到偏好设置界面
            }
        });
    }

    // 密码加密
    private String tokenFromPassword(String password) {
        return MD5.getStringMD5(password);
    }

    /**
     * 界面销毁,注销广播
     */
    public void destroy() {
        if (Receiver != null) {
            activity.getActivity().unregisterReceiver(Receiver);
            Receiver = null;
        }
        model = null;
    }
}
