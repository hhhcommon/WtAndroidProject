package com.wotingfm.ui.mine.personinfo.presenter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import com.google.gson.Gson;
import com.woting.commonplat.config.GlobalNetWorkConfig;
import com.woting.commonplat.manager.FileManager;
import com.woting.commonplat.utils.BitmapUtils;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.constant.StringConstant;
import com.wotingfm.common.utils.ToastUtils;
import com.wotingfm.ui.intercom.group.editgroupmessage.model.AddressModel;
import com.wotingfm.ui.intercom.group.editgroupmessage.model.EditGroupMessageModel;
import com.wotingfm.ui.intercom.group.groupintroduce.view.EditGroupIntroduceFragment;
import com.wotingfm.ui.intercom.group.groupname.view.EditGroupNameFragment;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;
import com.wotingfm.ui.mine.editusermessage.view.EditUserFragment;
import com.wotingfm.ui.mine.feedback.model.FeedbackModel;
import com.wotingfm.ui.mine.fm.model.FMInfo;
import com.wotingfm.ui.mine.fm.model.FMSetModel;
import com.wotingfm.ui.mine.fm.view.FMSetFragment;
import com.wotingfm.ui.mine.main.MineActivity;
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

    private final PersonalInfoFragment activity;
    private final PersonInfoModel model;
    private final int TO_GALLERY = 1;           // 标识 打开系统图库
    private final int TO_CAMERA = 2;            // 标识 打开系统照相机
    private final int PHOTO_REQUEST_CUT = 7;    // 标识 跳转到图片裁剪界面
    private String outputFilePath;
    private boolean headViewShow = false;// 图片选择界面是否展示
    private String sex;

    public PersonInfoPresenter(PersonalInfoFragment activity) {
        this.activity = activity;
        this.model = new PersonInfoModel(activity);
        getData();
        getAddress();
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
            sex=user.getGender();
        } else {
            String url = BSApplication.SharedPreferences.getString(StringConstant.PORTRAIT, "");
            String name = BSApplication.SharedPreferences.getString(StringConstant.USER_NAME, "");
            String introduce = BSApplication.SharedPreferences.getString(StringConstant.USER_SIGN, "");
            sex = BSApplication.SharedPreferences.getString(StringConstant.GENDERUSR, "男");
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
     * 设置性别
     */
    public void setSex() {
        if(sex.equals("女")){
            sendSex("男");
        }else{
            sendSex("女");
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
     * 跳转修改昵称界面
     */
    public void jumpName() {
        EditUserFragment fragment = new EditUserFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", 1);
        fragment.setArguments(bundle);
        MineActivity.open(fragment);
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
        MineActivity.open(fragment);
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
        MineActivity.open(fragment);
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
            String s = new Gson().toJson(o);
            JSONObject js = new JSONObject(s);
            int ret = js.getInt("ret");
            Log.e("修改个人地址=ret", String.valueOf(ret));
            if (ret == 0) {
                activity.setViewForAddress(name);
                ToastUtils.show_always(activity.getActivity(), "地理位置修改成功");
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
                sex=name;
            } else {
                if (GlobalNetWorkConfig.CURRENT_NETWORK_STATE_TYPE != -1) {
                    activity.dialogShow();
                    model.loadNewsForSex(name, new PersonInfoModel.OnLoadInterface() {
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
            String s = new Gson().toJson(o);
            JSONObject js = new JSONObject(s);
            int ret = js.getInt("ret");
            Log.e("修改性别=ret", String.valueOf(ret));
            if (ret == 0) {
                activity.setViewForGender(name);
                sex=name;
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
