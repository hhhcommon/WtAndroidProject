package com.wotingfm.ui.intercom.group.creat.presenter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.woting.commonplat.config.GlobalNetWorkConfig;
import com.woting.commonplat.manager.FileManager;
import com.woting.commonplat.utils.BitmapUtils;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.constant.BroadcastConstants;
import com.wotingfm.common.net.MyHttp;
import com.wotingfm.common.utils.ToastUtils;
import com.wotingfm.ui.intercom.group.creat.model.CreateGroupMainModel;
import com.wotingfm.ui.intercom.group.creat.view.CreateGroupMainFragment;
import com.wotingfm.ui.intercom.group.groupname.view.EditGroupNameFragment;
import com.wotingfm.ui.intercom.group.standbychannel.view.StandbyChannelFragment;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;
import com.wotingfm.ui.intercom.person.newfriend.model.NewFriendModel;
import com.wotingfm.ui.intercom.person.newfriend.view.NewFriendFragment;
import com.wotingfm.ui.photocut.PhotoCutActivity;
import com.wotingfm.ui.user.login.model.Login;
import com.wotingfm.ui.user.register.model.RegisterModel;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.util.List;

/**
 * 创建群处理中心
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class CreateGroupMainPresenter {

    private final CreateGroupMainFragment activity;
    private final CreateGroupMainModel model;
    private boolean b1 = false;// 密码群 选择状态
    private boolean b2 = false;// 审核群 选择状态
    private final int TO_GALLERY = 1;           // 标识 打开系统图库
    private final int TO_CAMERA = 2;            // 标识 打开系统照相机
    private final int PHOTO_REQUEST_CUT = 7;    // 标识 跳转到图片裁剪界面
    private String outputFilePath;
    private boolean headViewShow = false;// 图片选择界面是否展示
    private String url="test"; // 阿里云返回的图片路径

    public CreateGroupMainPresenter(CreateGroupMainFragment activity) {
        this.activity = activity;
        this.model = new CreateGroupMainModel();
    }

    /**
     * 判断界面展示
     */
    public void headViewShow() {
        if (headViewShow) {
            activity.imageShow(false);
            headViewShow = false;
        } else {
            activity.imageShow(true);
            headViewShow = true;
        }
    }

    /**
     * 提交数据
     *
     * @param name
     * @param password
     */
    public void send(String name, String password) {
        // 临时测试数据
        if (GlobalNetWorkConfig.CURRENT_NETWORK_STATE_TYPE != -1) {
            if (GlobalStateConfig.test) {
                // 测试数据
                InterPhoneActivity.open(new StandbyChannelFragment());
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
        activity.getActivity().startActivityForResult(intents, TO_CAMERA);
    }

    /**
     * 调用图库
     */
    public void photoAlbum() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        activity.getActivity().startActivityForResult(intent, TO_GALLERY);
    }

    /**
     * 返回值得监听
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TO_GALLERY:                // 照片的原始资源地址
                if (resultCode == -1) {
                    Uri uri = data.getData();
                    Log.e("URI:", uri.toString());
                    String path = BitmapUtils.getFilePath(activity.getActivity(), uri);
                    Log.e("path:", path + "");
                    if (path != null && !path.trim().equals("")) startPhotoZoom(Uri.parse(path));
                }
                break;
            case TO_CAMERA:
                if (resultCode == Activity.RESULT_OK) {
                    startPhotoZoom(Uri.parse(outputFilePath));
                }
                break;
            case PHOTO_REQUEST_CUT:
                if (resultCode == 1) {
                    String Path = data.getStringExtra("return");
                    activity.dialogShow();
                    upImageUrl(Path);
                }
                break;
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
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
            }
        }
        if (b1 && b2) {
            Toast.makeText(activity.getActivity(), "请选择加群方式", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    // 发送网络请求=创建群组// 0密码群，1审核群，3密码审核群
    private void sendCreate(String name, String password) {
        int type = 0;
        if (b1) {
            type = 0;
        }
        if (b2) {
            type = 1;
        }
        if(b1&&b2){
            type=3;
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
            String s = new Gson().toJson(o);
            JSONObject js = new JSONObject(s);
            int ret = js.getInt("ret");
            Log.e("创建群==ret", String.valueOf(ret));
            if (ret == 0) {
                // 创建成功后返回的数据
                activity.getActivity().sendBroadcast(new Intent(BroadcastConstants.GROUP_GET));
                String gid="000";
                InterPhoneActivity.close();
                StandbyChannelFragment fragment = new StandbyChannelFragment();
                Bundle bundle = new Bundle();
                bundle.putString("fromType", "create");
                bundle.putString("groupId", gid);
                fragment.setArguments(bundle);
                InterPhoneActivity.open(fragment);
            } else {
                ToastUtils.show_always(activity.getActivity(), "创建失败，请稍后再试！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.show_always(activity.getActivity(), "创建失败，请稍后再试！");
        }
    }

    // 图片裁剪
    private void startPhotoZoom(Uri uri) {
        Intent intent = new Intent(activity.getActivity(), PhotoCutActivity.class);
        intent.putExtra("URI", uri.toString());
        intent.putExtra("type", 1);
        activity.getActivity().startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    // 上传头像
    private void upImageUrl(final String path) {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                activity.dialogCancel();
                if (msg.what == 1) {
                    activity.setImageUrl(url);
                } else if (msg.what == 0) {

                }
            }
        };

        new Thread() {
            @Override
            public void run() {
                super.run();
                Message msg = new Message();
                try {
                    String ExtName = path.substring(path.lastIndexOf("."));
                    String TestURI = "wt/common/upload4App.do?FType=GroupP&ExtName=";
                    String Response = MyHttp.postFile(new File(path), TestURI + ExtName + "&PCDType=");
                    url = "";
                    msg.what = 1;
                } catch (Exception e) {
                    // 异常处理
                    e.printStackTrace();
                    msg.what = -1;
                }
                handler.sendMessage(msg);
            }
        }.start();
    }

}
