package com.wotingfm.ui.intercom.group.editgroupmessage.view;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.woting.commonplat.manager.PhoneMsgManager;
import com.woting.commonplat.utils.BitmapUtils;
import com.wotingfm.R;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.utils.DialogUtils;
import com.wotingfm.common.utils.GlideUtils;
import com.wotingfm.common.view.pickview.LoopView;
import com.wotingfm.common.view.pickview.OnItemSelectedListener;
import com.wotingfm.ui.intercom.group.editgroupmessage.presenter.EditGroupMessagePresenter;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;

import java.util.List;
import java.util.Map;

/**
 * 编辑资料
 * 作者：xinLong on 2017/6/5 01:30
 * 邮箱：645700751@qq.com
 */
public class EditGroupMessageFragment extends Fragment implements View.OnClickListener {
    private View rootView;
    private EditGroupMessagePresenter presenter;
    private ImageView image_headView;
    private TextView tv_groupName, tv_groupIntroduce, tv_groupAddress;
    private Dialog cityDialog,imgDialog,dialog;
    private int provinceIndex, cityIndex;
    private ResultListener Listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_editgroupmessage, container, false);
            rootView.setOnClickListener(this);
            inItView();
            imgDialog();
            presenter = new EditGroupMessagePresenter(this);
        }
        return rootView;
    }

    // 初始化界面
    private void inItView() {
        rootView.findViewById(R.id.head_left_btn).setOnClickListener(this);

        rootView.findViewById(R.id.re_headView).setOnClickListener(this);
        image_headView = (ImageView) rootView.findViewById(R.id.image_headView); // 图片

        rootView.findViewById(R.id.re_groupName).setOnClickListener(this);
        tv_groupName = (TextView) rootView.findViewById(R.id.tv_groupName); // 群名称

        rootView.findViewById(R.id.re_groupAddress).setOnClickListener(this);
        tv_groupAddress = (TextView) rootView.findViewById(R.id.tv_groupAddress); // 群地址

        rootView.findViewById(R.id.re_groupIntroduce).setOnClickListener(this);
        tv_groupIntroduce = (TextView) rootView.findViewById(R.id.tv_groupIntroduce); // 群介绍


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left_btn:
                InterPhoneActivity.close();
                break;
            case R.id.view_head_portrait:// 头像按钮
                if (imgDialog != null) {
                    imgDialog.show();
                }
                break;
            case R.id.tv_paizhao:
                presenter.camera();
                if (imgDialog != null) {
                    imgDialog.dismiss();
                }
                break;
            case R.id.tv_xiangce:
                presenter.photoAlbum();
                if (imgDialog != null) {
                    imgDialog.dismiss();
                }
                break;
            case R.id.tv_quxiao:
                if (imgDialog != null) {
                    imgDialog.dismiss();
                }
                break;
            case R.id.re_groupName:
                presenter.setGroupName();  // 设置群名称
                break;
            case R.id.re_groupAddress:
                if (cityDialog != null) {
                    cityDialog.show(); // 设置群地址
                }
                break;
            case R.id.re_groupIntroduce:
                presenter.setGroupIntroduce();  // 设置群介绍
                break;
        }
    }

    /**
     * 设置图片
     *
     * @param url
     */
    public void setViewForImage(String url) {
        if (url != null && !url.equals("")) {
            GlideUtils.loadImageViewSize(this.getActivity(), url, 60, 60, image_headView, true);
        } else {
            Bitmap bmp = BitmapUtils.readBitMap(this.getActivity(), R.mipmap.icon_avatar_d);
            image_headView.setImageBitmap(bmp);
        }
    }

    /**
     * 设置组名称
     *
     * @param s
     */
    public void setViewForGroupName(String s) {
        if (s != null && !s.trim().equals("")) {
            tv_groupName.setText(s);
        } else {
            tv_groupName.setText("");
        }
    }

    /**
     * 设置组地址
     *
     * @param s
     */
    public void setViewGroupAddress(String s) {
        if (s != null && !s.trim().equals("")) {
            tv_groupAddress.setText(s);
        } else {
            tv_groupAddress.setText("");
        }
    }

    /**
     * 设置组介绍
     *
     * @param s
     */
    public void setViewForGroupIntroduce(String s) {
        if (s != null && !s.trim().equals("")) {
            tv_groupIntroduce.setText(s);
        } else {
            tv_groupIntroduce.setText("");
        }
    }

    // 城市选择框
    public void cityPickerDialog(final Map<String, List<String>> positionMap, final List<String> provinceList) {
        GlobalStateConfig.LoopViewW= PhoneMsgManager.ScreenWidth/2;
        if (positionMap != null && positionMap.size() > 0 && provinceList != null && provinceList.size() > 0) {
            final View dialog = LayoutInflater.from(this.getActivity()).inflate(R.layout.dialog_city, null);
            final LoopView pickProvince = (LoopView) dialog.findViewById(R.id.pick_province);
            final LoopView pickCity = (LoopView) dialog.findViewById(R.id.pick_city);

            // 设置字体样式
            pickProvince.setTextSize(15, 17);
            pickProvince.setItems(provinceList);
            pickProvince.setInitPosition(0);

            pickCity.setTextSize(15, 17);
            List<String> tempList = positionMap.get(provinceList.get(0));
            pickCity.setItems(tempList);
            pickCity.setInitPosition(0);


            pickProvince.setListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(int index) {
                    provinceIndex = index;
                    List<String> tempList1 = positionMap.get(provinceList.get(provinceIndex));
                    pickCity.setItems(tempList1);
                    pickCity.setInitPosition(0);
                }
            });
            pickCity.setListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(int index) {
                    cityIndex = index;
                }
            });

            cityDialog = new Dialog(this.getActivity(), R.style.MyDialogs);
            cityDialog.setContentView(dialog);
            cityDialog.setCanceledOnTouchOutside(true);

            DisplayMetrics dm = new DisplayMetrics();
            this.getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            int screenWidth = dm.widthPixels;
            ViewGroup.LayoutParams params = dialog.getLayoutParams();
            params.width = screenWidth;
            dialog.setLayoutParams(params);

            Window window = cityDialog.getWindow();
            window.setGravity(Gravity.BOTTOM);
            window.setWindowAnimations(R.style.inOutStyle);
            window.setBackgroundDrawableResource(R.color.transparent_40_black);

            dialog.findViewById(R.id.tv_confirm).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        String name = positionMap.get(provinceList.get(provinceIndex)).get(cityIndex);
                        presenter.sendAddress(name);
                        cityDialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    }

    // 上传头像选择框
    public void imgDialog() {

        final View dialog = LayoutInflater.from(this.getActivity()).inflate(R.layout.dialog_updata_img, null);
        dialog.findViewById(R.id.tv_paizhao).setOnClickListener(this);  // 拍照
        dialog.findViewById(R.id.tv_xiangce).setOnClickListener(this);  // 相册
        dialog.findViewById(R.id.tv_quxiao).setOnClickListener(this);   // 取消

        imgDialog = new Dialog(this.getActivity(), R.style.MyDialogs);
        imgDialog.setContentView(dialog);
        imgDialog.setCanceledOnTouchOutside(true);

        DisplayMetrics dm = new DisplayMetrics();
        this.getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        ViewGroup.LayoutParams params = dialog.getLayoutParams();
        params.width = screenWidth;
        dialog.setLayoutParams(params);

        Window window = imgDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.inOutStyle);
        window.setBackgroundDrawableResource(R.color.transparent_40_black);
    }

    /**
     * 展示弹出框
     */
    public void dialogShow() {
        dialog = DialogUtils.Dialog(this.getActivity());
    }

    /**
     * 取消弹出框
     */
    public void dialogCancel() {
        if (dialog != null) dialog.dismiss();
    }

    /**
     * 返回值设置
     *
     * @param type
     * @param news
     */
    public void setResult(int type, String news) {
        Listener.resultListener(type, news);
    }

    /**
     * 回调结果值
     *
     * @param l
     */
    public void setResultListener(ResultListener l) {
        Listener = l;
    }

    public interface ResultListener {
        void resultListener(int type, String news);
    }
}
