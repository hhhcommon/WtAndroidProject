package com.wotingfm.ui.intercom.group.creat.presenter;

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
import com.google.gson.GsonBuilder;
import com.netease.nimlib.sdk.avchat.AVChatCallback;
import com.netease.nimlib.sdk.avchat.AVChatManager;
import com.netease.nimlib.sdk.avchat.model.AVChatChannelInfo;
import com.woting.commonplat.config.GlobalNetWorkConfig;
import com.woting.commonplat.manager.FileManager;
import com.woting.commonplat.utils.SequenceUUID;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.constant.BroadcastConstants;
import com.wotingfm.common.manager.InterPhoneControl;
import com.wotingfm.common.net.upLoadImage;
import com.wotingfm.common.utils.ToastUtils;
import com.wotingfm.ui.intercom.group.creat.model.CreateGroupMainModel;
import com.wotingfm.ui.intercom.group.creat.view.CreateGroupMainFragment;
import com.wotingfm.ui.intercom.group.standbychannel.view.StandbyChannelFragment;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;
import com.wotingfm.ui.photocut.PhotoCutActivity;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.File;

/**
 * 创建群处理中心
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class CreateGroupMainPresenter {

    private  CreateGroupMainFragment activity;
    private  CreateGroupMainModel model;
    private boolean b1 = false;// 密码群 选择状态
    private boolean b2 = false;// 审核群 选择状态
    private final int TO_GALLERY = 5;
    private final int TO_CAMERA = 6;
    private final int PHOTO_REQUEST_CUT = 7;    // 标识 跳转到图片裁剪界面
    private int Code;    // 标识
    private String outputFilePath;
    private String url;
    private MessageReceiver Receiver;
    private String UUID;


    public CreateGroupMainPresenter(CreateGroupMainFragment activity) {
        this.activity = activity;
        this.model = new CreateGroupMainModel();
        setReceiver();
    }

    /**
     * 提交数据
     *
     * @param name
     * @param password
     */
    public void send(String name, String password) {
        if (GlobalNetWorkConfig.CURRENT_NETWORK_STATE_TYPE != -1) {
            if (GlobalStateConfig.test) {
                // 测试数据
                jumpChannel("000");
            } else {
                // 实际数据
                if (checkData(name, password)) {
                    activity.dialogShow();
                    sendCreate(name, password);
                }
            }
        } else {
            ToastUtils.show_always(activity.getActivity(), "网络连接失败，请稍后再试！");
        }
    }

    /**
     * 设置审核群
     */
    public void setSen() {
        if (b2) {
            activity.setViewS(false);
            b2 = false;
        } else {
            activity.setViewS(true);
            b2 = true;
        }
    }

    /**
     * 设置密码群
     */
    public void setPassword() {
        if (b1) {
            activity.setViewM(false);
            b1 = false;
        } else {
            activity.setViewM(true);
            b1 = true;
        }
    }

    // 检查数据的正确性  检查通过则进行登录
    private boolean checkData(String name, String password) {
        if (name == null || name.trim().equals("")) {
            Toast.makeText(activity.getActivity(), "群名称不能为空", Toast.LENGTH_LONG).show();
            return false;
        }
        if (b1) {
            if (password == null || password.trim().equals("")) {
                Toast.makeText(activity.getActivity(), "进群密码不能为空", Toast.LENGTH_LONG).show();
                return false;
            }else if(password.length()!=4){
                Toast.makeText(activity.getActivity(), "密码必须为4位呦", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        if (!b1 && !b2) {
            Toast.makeText(activity.getActivity(), "请选择加群方式", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    // 发送网络请求=创建群组// 0密码群，1审核群，2密码审核群
    private void sendCreate(String name, String password) {
        int type = 0;
        if (b1) {
            type = 0;
        }
        if (b2) {
            type = 1;
        }
        if (b1 && b2) {
            type = 2;
        }
        model.loadNews(name, password, type, url, new CreateGroupMainModel.OnLoadInterface() {
            @Override
            public void onSuccess(Object o) {
                activity.dialogCancel();
                dealSuccess(o);
            }

            @Override
            public void onFailure(String msg) {
                activity.dialogCancel();
                ToastUtils.show_always(activity.getActivity(), "创建失败，请稍后再试！");
            }
        });
    }


    // 处理返回数据
    private void dealSuccess(Object o) {
        try {
            String s = new GsonBuilder().serializeNulls().create().toJson(o);
            JSONObject js = new JSONObject(s);
            int ret = js.getInt("ret");
            Log.e("创建群==ret", String.valueOf(ret));
            if (ret == 0) {
                // 创建成功后返回的数据
                activity.getActivity().sendBroadcast(new Intent(BroadcastConstants.GROUP_GET));

                String msg = js.getString("data");
                JSONTokener jsonParser = new JSONTokener(msg);
                JSONObject arg1 = (JSONObject) jsonParser.nextValue();
                try {
                   final String gid  = arg1.getString("id");
                    if (gid != null && !gid.trim().equals("")) {
                        InterPhoneControl.nimCreate(gid, new InterPhoneControl.Listener() {
                            @Override
                            public void type(boolean b) {
                                if(b){
                                    jumpChannel(gid);
                                }else{
                                    ToastUtils.show_always(activity.getActivity(), "创建失败，请稍后再试！");
                                }
                            }
                        });
                    }else{
                        InterPhoneActivity.close();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    InterPhoneActivity.close();
                }
                ToastUtils.show_always(activity.getActivity(), "创建成功");
            } else {
                ToastUtils.show_always(activity.getActivity(), "创建失败，请稍后再试！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.show_always(activity.getActivity(), "创建失败，请稍后再试！");
        }
    }

    // 跳转到设置频道界面
    private void jumpChannel(String id) {
        InterPhoneActivity.close();
        StandbyChannelFragment fragment = new StandbyChannelFragment();
        Bundle bundle = new Bundle();
        bundle.putString("fromType", "create");
        bundle.putString("groupId", id);
        fragment.setArguments(bundle);
        InterPhoneActivity.open(fragment);
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
                    url = upLoadImage.URL+UUID;
                    activity.setImageUrl(url);
                    ToastUtils.show_always(activity.getActivity(), "图片上传成功");
                } else  {
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

    /**
     * 界面销毁,注销广播
     */
    public void destroy() {
        if (Receiver != null) {
            activity.getActivity().unregisterReceiver(Receiver);
            Receiver = null;
        }
        model=null;
    }

}
