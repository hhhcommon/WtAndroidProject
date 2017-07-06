package com.wotingfm.ui.intercom.group.editgroupmessage.presenter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;
import com.google.gson.Gson;
import com.woting.commonplat.config.GlobalNetWorkConfig;
import com.woting.commonplat.manager.FileManager;
import com.woting.commonplat.utils.BitmapUtils;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.utils.ToastUtils;
import com.wotingfm.ui.intercom.group.editgroupmessage.model.AddressModel;
import com.wotingfm.ui.intercom.group.editgroupmessage.model.EditGroupMessageModel;
import com.wotingfm.ui.intercom.group.editgroupmessage.view.EditGroupMessageFragment;
import com.wotingfm.ui.intercom.group.groupintroduce.view.EditGroupIntroduceFragment;
import com.wotingfm.ui.intercom.group.groupname.view.EditGroupNameFragment;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;
import com.wotingfm.ui.photocut.PhotoCutActivity;
import org.json.JSONObject;
import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class EditGroupMessagePresenter {

    private final EditGroupMessageFragment activity;
    private final EditGroupMessageModel model;
    private final int TO_GALLERY = 1;           // 标识 打开系统图库
    private final int TO_CAMERA = 2;            // 标识 打开系统照相机
    private final int PHOTO_REQUEST_CUT = 7;    // 标识 跳转到图片裁剪界面
    private Contact.group group;
    private String outputFilePath;
    private boolean headViewShow = false;// 图片选择界面是否展示

    public EditGroupMessagePresenter(EditGroupMessageFragment activity) {
        this.activity = activity;
        this.model = new EditGroupMessageModel(activity);
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

            if(positionMap!=null&&positionMap.size()>0&&provinceList!=null&&provinceList.size()>0){
                activity.cityPickerDialog(positionMap, provinceList);
            }else{
                ToastUtils.show_always(activity.getActivity(),"address失败");
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
                    upImageUrl(Path);
                }
                break;
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
    private void upImageUrl(String path) {
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
     * @param name
     */
    public void sendAddress(final String name) {
        if(name!=null&&!name.equals("")){
            if(GlobalStateConfig.test){
                activity.setViewGroupAddress(name);
            }else{
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
}
