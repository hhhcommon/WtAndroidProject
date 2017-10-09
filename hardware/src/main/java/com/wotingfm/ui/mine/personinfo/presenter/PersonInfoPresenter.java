package com.wotingfm.ui.mine.personinfo.presenter;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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
import com.google.gson.GsonBuilder;
import com.woting.commonplat.config.GlobalNetWorkConfig;
import com.woting.commonplat.manager.FileManager;
import com.woting.commonplat.utils.SequenceUUID;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.constant.BroadcastConstants;
import com.wotingfm.common.constant.StringConstant;
import com.wotingfm.common.net.upLoadImage;
import com.wotingfm.common.utils.ToastUtils;
import com.wotingfm.ui.intercom.group.editgroupmessage.model.AddressModel;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;
import com.wotingfm.ui.mine.editusermessage.view.EditUserFragment;
import com.wotingfm.ui.mine.personinfo.model.PersonInfoModel;
import com.wotingfm.ui.mine.personinfo.view.PersonalInfoFragment;
import com.wotingfm.ui.photocut.PhotoCutActivity;

import org.json.JSONObject;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class PersonInfoPresenter {

    private PersonalInfoFragment activity;
    private PersonInfoModel model;
    private final int TO_GALLERY = 5;
    private final int TO_CAMERA = 6;
    private final int PHOTO_REQUEST_CUT = 7;    // 标识 跳转到图片裁剪界面
    private int Code;    // 标识
    private String outputFilePath;
    private String url;
    private MessageReceiver Receiver;
    private String UUID;
    private String age;


    public PersonInfoPresenter(PersonalInfoFragment activity) {
        this.activity = activity;
        this.model = new PersonInfoModel(activity);
        setReceiver();
        getData();
        getAddress();
        getTime();// 组装时间选择器
    }

    private void getTime() {
        activity.agePickerDialog(model.getYearList(), model.getMonthList(), model.getDayList28(), model.getDayList29(), model.getDayList30(), model.getDayList31());
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
        if (GlobalStateConfig.test) {
            Contact.user user = model.getUser();
            activity.setViewForImage(user.getAvatar());
            activity.setViewForName(user.getName());
            activity.setViewForIntroduce(user.getIntroduction());
            activity.setViewForGender(user.getGender());
            activity.setViewForAge(user.getAge());
            activity.setViewForAddress(user.getLocation());
        } else {
            String url = BSApplication.SharedPreferences.getString(StringConstant.PORTRAIT, "");
            String name = BSApplication.SharedPreferences.getString(StringConstant.NICK_NAME, "");
            String introduce = BSApplication.SharedPreferences.getString(StringConstant.USER_SIGN, "");
            String sex = BSApplication.SharedPreferences.getString(StringConstant.GENDER, "男");
            String age = BSApplication.SharedPreferences.getString(StringConstant.AGE, "0");
            String address = BSApplication.SharedPreferences.getString(StringConstant.REGION, "");

            activity.setViewForImage(url);
            activity.setViewForName(name);
            activity.setViewForIntroduce(introduce);
            activity.setViewForGender(sex);
            activity.setViewForAge(age);
            activity.setViewForAddress(address);
        }
    }

    /**
     * 设置性别
     */
    public void setSex(String sex) {
        if (sex.equals("男")) {
            sendSex("男");
        } else {
            sendSex("女");
        }
    }

    /**
     * 跳转修改昵称界面
     */
    public void jumpName() {
        EditUserFragment fragment = new EditUserFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", 1);
        fragment.setArguments(bundle);
        activity.openFragment(fragment);
        fragment.setResultListener(new EditUserFragment.ResultListener() {
            @Override
            public void resultListener(boolean b, String name) {
                if (b) {
                    if (name != null && !name.equals("")) {
                        activity.setViewForName(name);
                    }
                }
            }
        });
    }

    /**
     * 跳转修改介绍界面
     */
    public void jumpIntroduce() {
        EditUserFragment fragment = new EditUserFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", 2);
        fragment.setArguments(bundle);
        activity.openFragment(fragment);
        fragment.setResultListener(new EditUserFragment.ResultListener() {
            @Override
            public void resultListener(boolean b, String name) {
                if (b) {
                    if (name != null && !name.equals("")) {
                        activity.setViewForIntroduce(name);
                    }
                }
            }
        });
    }

    /**
     * 跳转修改年龄界面
     */
    public void jumpAge() {
        EditUserFragment fragment = new EditUserFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", 3);
        fragment.setArguments(bundle);
        activity.openFragment(fragment);
        fragment.setResultListener(new EditUserFragment.ResultListener() {
            @Override
            public void resultListener(boolean b, String name) {
                if (b) {
                    if (name != null && !name.equals("")) {
                        activity.setViewForAge(name);
                    }
                }
            }
        });
    }

    /**
     * 发送申请
     *
     * @param date
     */
    public void sendAge(final String date) {
        age = String.valueOf(model.getAgeFromBirthTime(date));
        if (GlobalNetWorkConfig.CURRENT_NETWORK_STATE_TYPE != -1) {
            activity.dialogShow();
            String id = BSApplication.SharedPreferences.getString(StringConstant.USER_ID, "");
            model.loadNews(id, age, 3, new PersonInfoModel.OnLoadInterface() {
                @Override
                public void onSuccess(Object o) {
                    activity.dialogCancel();
                    dealAgeSuccess(o, age);
                }

                @Override
                public void onFailure(String msg) {
                    activity.dialogCancel();
                }
            });
        } else {
            ToastUtils.show_always(activity.getActivity(), "网络连接失败，请稍后再试！");
        }
    }

    private void dealAgeSuccess(Object o, String age) {
        try {
            String s = new GsonBuilder().serializeNulls().create().toJson(o);
            JSONObject js = new JSONObject(s);
            int ret = js.getInt("ret");
            Log.e("ret", String.valueOf(ret));
            if (ret == 0) {
                activity.setViewForAge(age);
                model.saveAge(age);
            } else {
                ToastUtils.show_always(activity.getActivity(), "修改失败，请稍后再试！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.show_always(activity.getActivity(), "修改失败，请稍后再试！");
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
                activity.setViewForAddress(name);
            } else {
                if (GlobalNetWorkConfig.CURRENT_NETWORK_STATE_TYPE != -1) {
                    model.loadNewsForAddress(name, new PersonInfoModel.OnLoadInterface() {
                        @Override
                        public void onSuccess(Object o) {
                            dealSuccess(o, name);
                        }

                        @Override
                        public void onFailure(String msg) {
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
            String s = new GsonBuilder().serializeNulls().create().toJson(o);
            JSONObject js = new JSONObject(s);
            int ret = js.getInt("ret");
            Log.e("修改个人地址=ret", String.valueOf(ret));
            if (ret == 0) {
                activity.setViewForAddress(name);
                model.saveAddress(name);
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
     * 修改性别
     *
     * @param name
     */
    public void sendSex(final String name) {
        if (GlobalStateConfig.test) {
            activity.setViewForGender(name);
            model.saveSex(name);
        } else {
            if (GlobalNetWorkConfig.CURRENT_NETWORK_STATE_TYPE != -1) {
                activity.dialogShow();
                String news;
                if (name.equals("女")) {
                    news = "0";
                } else {
                    news = "1";
                }
                model.loadNewsForSex(news, new PersonInfoModel.OnLoadInterface() {
                    @Override
                    public void onSuccess(Object o) {
                        activity.dialogCancel();
                        dealSexSuccess(o, name);
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

    private void dealSexSuccess(Object o, String name) {
        try {
            String s = new GsonBuilder().serializeNulls().create().toJson(o);
            JSONObject js = new JSONObject(s);
            int ret = js.getInt("ret");
            Log.e("修改性别=ret", String.valueOf(ret));
            if (ret == 0) {
                activity.setViewForGender(name);
                model.saveSex(name);
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
     * 修改用户头像
     *
     * @param name
     */
    public void sendImg(final String name) {
        if (name != null && !name.equals("")) {
            if (GlobalNetWorkConfig.CURRENT_NETWORK_STATE_TYPE != -1) {
                model.loadNewsForImg(name, new PersonInfoModel.OnLoadInterface() {
                    @Override
                    public void onSuccess(Object o) {
                        activity.dialogCancel();
                        dealImgSuccess(o, name);
                    }

                    @Override
                    public void onFailure(String msg) {
                        activity.dialogCancel();
                        ToastUtils.show_always(activity.getActivity(), "头像修改失败，请稍后再试！");
                    }
                });
            } else {
                ToastUtils.show_always(activity.getActivity(), "网络连接失败，请稍后再试！");
            }
        }
    }

    private void dealImgSuccess(Object o, String name) {
        try {
            String s = new GsonBuilder().serializeNulls().create().toJson(o);
            JSONObject js = new JSONObject(s);
            int ret = js.getInt("ret");
            Log.e("修改个人头像=ret", String.valueOf(ret));
            if (ret == 0) {
                activity.setViewForImage(name);
                model.saveImg(name);
                activity.getActivity().sendBroadcast(new Intent(BroadcastConstants.MINE_CHANGE));
            } else {
                ToastUtils.show_always(activity.getActivity(), "头像修改失败，请稍后再试！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 设置数据出错界面
            ToastUtils.show_always(activity.getActivity(), "头像修改失败，请稍后再试！");
        }
    }

    /**
     * 拍照
     */
    public void camera() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Intent intents = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri outputFileUri;
            String savePath = FileManager.getImageSaveFilePath(BSApplication.mContext);
            GlobalStateConfig.savePath=savePath;
            FileManager.createDirectory(savePath);
            String fileName = System.currentTimeMillis() + ".jpg";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//如果是7.0android系统
                ContentValues contentValues = new ContentValues(1);
                File file = new File(savePath, fileName);
                outputFilePath = file.getAbsolutePath();
                contentValues.put(MediaStore.Images.Media.DATA, outputFilePath);
                outputFileUri = activity.getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            } else {
                File file = new File(savePath, fileName);
                outputFileUri = Uri.fromFile(file);
            }
            intents.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            Code = TO_CAMERA;
            activity.getActivity().startActivityForResult(intents, TO_CAMERA);
        } else {
            ToastUtils.show_always(activity.getActivity(), "请确认插入SD卡");
        }
    }


    /**
     * 调用图库
     */
    public void photoAlbum() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        Code = TO_GALLERY;
        activity.getActivity().startActivityForResult(intent, TO_GALLERY);
    }

    // 图片裁剪
    private void startPhotoZoom(Uri uri) {
        Intent intent = new Intent(activity.getActivity(), PhotoCutActivity.class);
        intent.putExtra("URI", uri.toString());
        intent.putExtra("type", 1);
        Code = PHOTO_REQUEST_CUT;
        activity.getActivity().startActivityForResult(intent, PHOTO_REQUEST_CUT);
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
                    url = upLoadImage.URL + UUID;
                    sendImg(url);

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
