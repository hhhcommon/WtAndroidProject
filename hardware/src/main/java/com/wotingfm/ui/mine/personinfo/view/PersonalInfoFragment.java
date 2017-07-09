package com.wotingfm.ui.mine.personinfo.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
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

import com.woting.commonplat.utils.BitmapUtils;
import com.wotingfm.R;
import com.wotingfm.common.utils.DialogUtils;
import com.wotingfm.common.utils.GlideUtils;
import com.wotingfm.common.view.pickview.LoopView;
import com.wotingfm.common.view.pickview.OnItemSelectedListener;
import com.wotingfm.ui.mine.main.MineActivity;
import com.wotingfm.ui.mine.personinfo.presenter.PersonInfoPresenter;

import java.util.List;
import java.util.Map;

/**
 * 个人信息
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class PersonalInfoFragment extends Fragment implements View.OnClickListener {
    private View rootView;
    private LinearLayout lin_chose;
    private ImageView image_head;
    private PersonInfoPresenter presenter;
    private TextView text_nickname, text_brief_introduction, text_gender, text_age, text_region;
    private Dialog cityDialog, dialog;
    private int provinceIndex, cityIndex;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_personal_info, container, false);
            rootView.setOnClickListener(this);
            initView();
            presenter = new PersonInfoPresenter(this);
        }
        return rootView;
    }

    // 初始化视图
    private void initView() {
        rootView.findViewById(R.id.head_left_btn).setOnClickListener(this);// 返回
        TextView textTitle = (TextView) rootView.findViewById(R.id.tv_center);// 标题
        textTitle.setText(getString(R.string.personal_info));

        rootView.findViewById(R.id.view_head_portrait).setOnClickListener(this);  // 头像按钮
        image_head = (ImageView) rootView.findViewById(R.id.image_head);// 头像

        rootView.findViewById(R.id.view_nickname).setOnClickListener(this);  // 昵称按钮
        text_nickname = (TextView) rootView.findViewById(R.id.text_nickname);// 昵称


        rootView.findViewById(R.id.view_brief_introduction).setOnClickListener(this);  // 简介按钮
        text_brief_introduction = (TextView) rootView.findViewById(R.id.text_brief_introduction);// 简介


        rootView.findViewById(R.id.view_gender).setOnClickListener(this);  // 性别按钮
        text_gender = (TextView) rootView.findViewById(R.id.text_gender);  // 性别

        rootView.findViewById(R.id.view_age).setOnClickListener(this);  // 年龄按钮
        text_age = (TextView) rootView.findViewById(R.id.text_age);     // 年龄

        rootView.findViewById(R.id.view_region).setOnClickListener(this);  // 地区按钮
        text_region = (TextView) rootView.findViewById(R.id.text_region);  // 地区

        lin_chose = (LinearLayout) rootView.findViewById(R.id.lin_chose); // 图片选择
        lin_chose.setOnClickListener(this);
        rootView.findViewById(R.id.tv_paizhao).setOnClickListener(this);  // 拍照
        rootView.findViewById(R.id.tv_xiangce).setOnClickListener(this);  // 相册
        rootView.findViewById(R.id.tv_quxiao).setOnClickListener(this);   // 取消
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left_btn:// 返回
                MineActivity.close();
                break;
            case R.id.view_head_portrait:// 头像按钮
                presenter.headViewShow();  // 头像上传
                break;
            case R.id.tv_paizhao:
                presenter.camera();
                presenter.headViewShow();
                break;
            case R.id.tv_xiangce:
                presenter.photoAlbum();
                presenter.headViewShow();
                break;
            case R.id.tv_quxiao:
                presenter.headViewShow();
                break;
            case R.id.view_nickname:// 昵称按钮
                presenter.jumpName();
                break;
            case R.id.view_brief_introduction:// 简介按钮
                presenter.jumpIntroduce();
                break;
            case R.id.view_gender:// 性别按钮
                presenter.setSex();
                break;
            case R.id.view_age:// 年龄按钮
                presenter.jumpAge();
                break;
            case R.id.view_region:// 地区按钮
                if (cityDialog != null) {
                    cityDialog.show(); // 设置群地址
                }
                break;
        }
    }

    /**
     * 图片上传界面的展示
     *
     * @param type
     */
    public void imageShow(boolean type) {
        if (type) {
            Animation mAnimation = AnimationUtils.loadAnimation(this.getActivity(), R.anim.wt_slide_in_from_bottom);
            lin_chose.setAnimation(mAnimation);
            lin_chose.setVisibility(View.VISIBLE);
        } else {
            Animation mAnimation = AnimationUtils.loadAnimation(this.getActivity(), R.anim.wt_slide_out_from_bottom);
            lin_chose.setAnimation(mAnimation);
            lin_chose.setVisibility(View.GONE);
        }
    }

    /**
     * 设置图片
     *
     * @param url
     */
    public void setViewForImage(String url) {
        if (url != null && !url.equals("")) {
            GlideUtils.loadImageViewSize(this.getActivity(), url, 60, 60, image_head, true);
        } else {
            Bitmap bmp = BitmapUtils.readBitMap(this.getActivity(), R.mipmap.icon_avatar_d);
            image_head.setImageBitmap(bmp);
        }
    }

    /**
     * 设置昵称
     *
     * @param s
     */
    public void setViewForName(String s) {
        if (s != null && !s.trim().equals("")) {
            text_nickname.setText(s);
        } else {
            text_nickname.setText("");
        }
    }

    /**
     * 设置简介
     *
     * @param s
     */
    public void setViewForIntroduce(String s) {
        if (s != null && !s.trim().equals("")) {
            text_brief_introduction.setText(s);
        } else {
            text_brief_introduction.setText("");
        }
    }

    /**
     * 设置性别
     *
     * @param s
     */
    public void setViewForGender(String s) {
        if (s != null && !s.trim().equals("")) {
            text_gender.setText(s);
        } else {
            text_gender.setText("");
        }
    }

    /**
     * 设置年龄
     *
     * @param s
     */
    public void setViewForAge(String s) {
        if (s != null && !s.trim().equals("")) {
            text_age.setText(s);
        } else {
            text_age.setText("");
        }
    }

    /**
     * 设置地区
     *
     * @param s
     */
    public void setViewForAddress(String s) {
        if (s != null && !s.trim().equals("")) {
            text_region.setText(s);
        } else {
            text_region.setText("");
        }
    }

    // 城市选择框
    public void cityPickerDialog(final Map<String, List<String>> positionMap, final List<String> provinceList) {
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

            cityDialog = new Dialog(this.getActivity(), R.style.MyDialog);
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
     * 返回值得监听
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        presenter.setResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (rootView != null) {
            ((ViewGroup) rootView.getParent()).removeView(rootView);
        }
    }

}
