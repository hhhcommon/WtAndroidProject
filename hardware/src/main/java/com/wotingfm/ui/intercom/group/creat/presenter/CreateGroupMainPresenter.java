package com.wotingfm.ui.intercom.group.creat.presenter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.woting.commonplat.manager.FileManager;
import com.woting.commonplat.utils.BitmapUtils;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.ui.intercom.group.creat.model.CreateGroupMainModel;
import com.wotingfm.ui.intercom.group.creat.view.CreateGroupMainFragment;
import com.wotingfm.ui.intercom.group.standbychannel.view.StandbyChannelFragment;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;
import com.wotingfm.ui.intercom.person.newfriend.model.NewFriendModel;
import com.wotingfm.ui.intercom.person.newfriend.view.NewFriendFragment;
import com.wotingfm.ui.photocut.PhotoCutActivity;
import com.wotingfm.ui.user.login.model.Login;
import com.wotingfm.ui.user.register.model.RegisterModel;

import java.io.File;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class CreateGroupMainPresenter {

    private final CreateGroupMainFragment activity;
    private final CreateGroupMainModel model;
    private int viewType = 1; // 1公开群，2密码群，3审核群
    private final int TO_GALLERY = 1;           // 标识 打开系统图库
    private final int TO_CAMERA = 2;            // 标识 打开系统照相机
    private final int PHOTO_REQUEST_CUT = 7;    // 标识 跳转到图片裁剪界面
    private String outputFilePath;
    private boolean headViewShow = false;// 图片选择界面是否展示

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
     * @param type
     * @param name
     * @param password
     */
    public void send(int type, String name, String password) {
        // 临时测试数据
        InterPhoneActivity.open(new StandbyChannelFragment());
        if (checkData(name, password, type)) {
            send(name, password, type);
        }
    }

    /**
     * 设置审核群
     */
    public void setSen() {
        if(viewType==1){
            activity.setViewS();
            viewType=3;
        }else if(viewType==2){
            activity.setViewS();
            viewType=3;
        }else if(viewType==3){
            activity.setViewG();
            viewType=1;
        }
    }

    /**
     * 设置密码群
     */
    public void setPassword() {
        if(viewType==1){
            activity.setViewM();
            viewType=2;
        }else if(viewType==2){
            activity.setViewG();
            viewType=1;
        }else if(viewType==3){
            activity.setViewM();
            viewType=2;
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
                    upImageUrl(Path);
                }
                break;
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // 检查数据的正确性  检查通过则进行登录
    private boolean checkData(String name, String password, int type) {
        if (name == null || name.trim().equals("")) {
            Toast.makeText(activity.getActivity(), "群名称不能为空", Toast.LENGTH_LONG).show();
            return false;
        }
        if (type == 2) {
            if (password == null || password.trim().equals("")) {
                Toast.makeText(activity.getActivity(), "进群密码不能为空", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return true;
    }

    // 发送网络请求
    private void send(String name, String password, int type) {
        model.loadNews(name, password, type, new CreateGroupMainModel.OnLoadInterface() {
            @Override
            public void onSuccess(Login result) {
//                loginView.removeDialog();
                dealLoginSuccess(result);
            }

            @Override
            public void onFailure(String msg) {
//                loginView.removeDialog();
//                ToastUtils.showVolleyError(loginView);
            }
        });
    }

    // 处理返回数据
    private void dealLoginSuccess(Login result) {

    }

    // 图片裁剪
    private void startPhotoZoom(Uri uri) {
        Intent intent = new Intent(activity.getActivity(), PhotoCutActivity.class);
        intent.putExtra("URI", uri.toString());
        intent.putExtra("type", 1);
        activity.getActivity().startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    // 上传头像
    private void upImageUrl(String path) {
    }

}
