package com.woting.ui.intercom.group.editgroupmessage.presenter;

import android.app.Activity;
import android.content.BroadcastReceiver;
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

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.ObjectMetadata;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.google.gson.Gson;
import com.woting.commonplat.config.GlobalNetWorkConfig;
import com.woting.commonplat.manager.FileManager;
import com.woting.commonplat.utils.SequenceUUID;
import com.woting.common.application.BSApplication;
import com.woting.common.config.GlobalStateConfig;
import com.woting.common.constant.BroadcastConstants;
import com.woting.common.net.upLoadImage;
import com.woting.common.utils.ToastUtils;
import com.woting.ui.intercom.group.editgroupmessage.model.AddressModel;
import com.woting.ui.intercom.group.editgroupmessage.model.EditGroupMessageModel;
import com.woting.ui.intercom.group.editgroupmessage.view.EditGroupMessageFragment;
import com.woting.ui.intercom.group.groupintroduce.view.EditGroupIntroduceFragment;
import com.woting.ui.intercom.group.groupname.view.EditGroupNameFragment;
import com.woting.ui.intercom.main.contacts.model.Contact;
import com.woting.ui.intercom.main.view.InterPhoneActivity;
import com.woting.ui.photocut.PhotoCutActivity;

import org.json.JSONObject;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class EditGroupMessagePresenter {

    private EditGroupMessageFragment activity;
    private EditGroupMessageModel model;
    private final int TO_GALLERY = 5;
    private final int TO_CAMERA = 6;
    private final int PHOTO_REQUEST_CUT = 7;    // 标识 跳转到图片裁剪界面
    private int Code;    // 标识
    private String url;
    private MessageReceiver Receiver;
    private String UUID;
    private Contact.group group;
    private String outputFilePath;

    public EditGroupMessagePresenter(EditGroupMessageFragment activity) {
        this.activity = activity;
        this.model = new EditGroupMessageModel(activity);
        setReceiver();
        try {
            group = (Contact.group) activity.getArguments().getSerializable("group");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (group != null) {
            getData();
            getAddress();
        } else {
            ToastUtils.show_always(activity.getActivity(), "数据出错了，请稍后再试！");
            InterPhoneActivity.close();
        }
    }

    private void getAddress() {
        Object[] list = model.getAddress();
        if (list != null && list.length > 0) {
            Map<String, List<AddressModel.SubCata>> tempMap = (Map<String, List<AddressModel.SubCata>>) list[0];
            Map<String, List<String>> positionMap = (Map<String, List<String>>) list[1];
            List<String> provinceList = (List<String>) list[2];

            if (positionMap != null && positionMap.size() > 0 && provinceList != null && provinceList.size() > 0) {
                activity.cityPickerDialog(positionMap, provinceList);
            } else {
                ToastUtils.show_always(activity.getActivity(), "address失败");
            }
        }

    }

    // 适配界面数据
    private void getData() {
        String imgUrl = group.getLogo_url();
        if (imgUrl != null && !imgUrl.equals("")) {
            activity.setViewForImage(imgUrl);
        } else {
            activity.setViewForImage("");
        }
        String groupName = group.getTitle();
        if (groupName != null && !groupName.equals("")) {
            activity.setViewForGroupName(groupName);
        } else {
            activity.setViewForGroupName("");
        }
        String groupAddress = group.getLocation();
        if (groupAddress != null && !groupAddress.equals("")) {
            activity.setViewGroupAddress(groupAddress);
        } else {
            activity.setViewGroupAddress("");
        }
        String groupIntroduce = group.getIntroduction();
        if (groupIntroduce != null && !groupIntroduce.equals("")) {
            activity.setViewForGroupIntroduce(groupIntroduce);
        } else {
            activity.setViewForGroupIntroduce("");
        }
    }

    /**
     * 跳转到群名称界面
     */
    public void setGroupName() {
        if (group != null) {
            EditGroupNameFragment fragment = new EditGroupNameFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("group", group);
            fragment.setArguments(bundle);
            InterPhoneActivity.open(fragment);
            fragment.setResultListener(new EditGroupNameFragment.ResultListener() {
                @Override
                public void resultListener(boolean type, String name) {
                    if (type) {
                        if (name != null && !name.equals("")) {
                            // 上层界面数据更改
                            activity.setResult(2, name);
                            activity.setViewForGroupName(name);
                        }
                    }
                }
            });
        } else {
            ToastUtils.show_always(activity.getActivity(), "数据出错了，请稍后再试！");
        }
    }

    /**
     * 跳转到群介绍界面
     */
    public void setGroupIntroduce() {
        if (group != null) {
            EditGroupIntroduceFragment fragment = new EditGroupIntroduceFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("group", group);
            fragment.setArguments(bundle);
            InterPhoneActivity.open(fragment);
            fragment.setResultListener(new EditGroupIntroduceFragment.ResultListener() {
                @Override
                public void resultListener(boolean type, String name) {
                    if (type) {
                        if (name != null && !name.equals("")) {
                            // 上层界面数据更改
                            activity.setResult(4, name);
                            activity.setViewForGroupIntroduce(name);
                        }
                    }
                }
            });
        } else {
            ToastUtils.show_always(activity.getActivity(), "数据出错了，请稍后再试！");
        }

    }

    /**
     * 修改地理位置
     *
     * @param name
     */
    public void sendAddress(final String name) {
        if (name != null && !name.equals("")) {
            if (GlobalStateConfig.test) {
                activity.setViewGroupAddress(name);
            } else {
                if (GlobalNetWorkConfig.CURRENT_NETWORK_STATE_TYPE != -1) {
                    activity.dialogShow();
                    String id = group.getId();
                    model.loadNews(id, name, new EditGroupMessageModel.OnLoadInterface() {
                        @Override
                        public void onSuccess(Object o) {
                            activity.dialogCancel();
                            dealSuccess(o, name);
                        }

                        @Override
                        public void onFailure(String msg) {
                            activity.dialogCancel();
                            ToastUtils.show_always(activity.getActivity(), "修改失败，请稍后再试！");
                        }
                    });
                } else {
                    ToastUtils.show_always(activity.getActivity(), "网络连接失败，请稍后再试！");
                }
            }
        }
    }

    private void dealSuccess(Object o, String name) {
        try {
            String s = new Gson().toJson(o);
            JSONObject js = new JSONObject(s);
            int ret = js.getInt("ret");
            Log.e("修改群地址=ret", String.valueOf(ret));
            if (ret == 0) {
                activity.setResult(3, name);
                activity.setViewGroupAddress(name);
                ToastUtils.show_always(activity.getActivity(), "修改成功");
            } else {
                ToastUtils.show_always(activity.getActivity(), "修改失败，请稍后再试！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 设置数据出错界面
            ToastUtils.show_always(activity.getActivity(), "修改失败，请稍后再试！");
        }
    }

    /**
     * 修改群头像
     *
     * @param name
     */
    public void sendImg(final String name) {
        if (name != null && !name.equals("")) {
            if (GlobalNetWorkConfig.CURRENT_NETWORK_STATE_TYPE != -1) {
                String id = group.getId();
                model.loadNewsForImg(id, name, new EditGroupMessageModel.OnLoadInterface() {
                    @Override
                    public void onSuccess(Object o) {
                        activity.dialogCancel();
                        dealImgSuccess(o, name);
                    }

                    @Override
                    public void onFailure(String msg) {
                        activity.dialogCancel();
                        ToastUtils.show_always(activity.getActivity(), "修改失败，请稍后再试！");
                    }
                });
            } else {
                ToastUtils.show_always(activity.getActivity(), "网络连接失败，请稍后再试！");
            }
        }
    }

    private void dealImgSuccess(Object o, String name) {
        try {
            String s = new Gson().toJson(o);
            JSONObject js = new JSONObject(s);
            int ret = js.getInt("ret");
            Log.e("修改群头像=ret", String.valueOf(ret));
            if (ret == 0) {
                // 上层界面数据更改
                activity.setResult(1, name);
                activity.setViewForImage(name);
                ToastUtils.show_always(activity.getActivity(), "图片上传成功");
            } else {
                ToastUtils.show_always(activity.getActivity(), "修改失败，请稍后再试！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 设置数据出错界面
            ToastUtils.show_always(activity.getActivity(), "修改失败，请稍后再试！");
        }
    }

    /**
     * 拍照
     */
    public void camera() {
        String savePath = FileManager.getImageSaveFilePath(BSApplication.mContext);
        FileManager.createDirectory(savePath);
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(savePath, fileName);
        Uri outputFileUri = Uri.fromFile(file);
        outputFilePath = file.getAbsolutePath();
        Intent intents = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
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
                    sendImg(upLoadImage.URL + UUID);
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
