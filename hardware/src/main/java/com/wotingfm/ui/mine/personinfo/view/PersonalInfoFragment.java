package com.wotingfm.ui.mine.personinfo.view;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.woting.commonplat.manager.PhoneMsgManager;
import com.wotingfm.R;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.utils.DialogUtils;
import com.wotingfm.common.utils.GlideUtils;
import com.wotingfm.common.view.pickview.LoopView;
import com.wotingfm.common.view.pickview.OnItemSelectedListener;
import com.wotingfm.ui.base.basefragment.BaseFragment;
import com.wotingfm.ui.mine.main.MineActivity;
import com.wotingfm.ui.mine.personinfo.presenter.PersonInfoPresenter;

import java.util.List;
import java.util.Map;

/**
 * 个人信息
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class PersonalInfoFragment extends BaseFragment implements View.OnClickListener {
    private View rootView;
    private ImageView image_head;
    private PersonInfoPresenter presenter;
    private TextView text_nickname, text_brief_introduction, text_gender, text_age, text_region;
    private Dialog cityDialog, dialog, imgDialog, sexDialog, dateDialog;
    private int provinceIndex, cityIndex;
    private int pYear, pMonth, pDay;
    private List<String> dayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_personal_info, container, false);
            rootView.setOnClickListener(this);
            initView();
            imgDialog();
            sexDialog();
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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left_btn:// 返回
                closeFragment();
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
            case R.id.view_gender:// 性别按钮
                if (sexDialog != null) {
                    sexDialog.show();
                }
                break;
            case R.id.tv_quxiao_sex:
                if (sexDialog != null) {
                    sexDialog.dismiss();
                }
                break;
            case R.id.tv_man:
                presenter.setSex("男");
                if (sexDialog != null) {
                    sexDialog.dismiss();
                }
                break;
            case R.id.tv_women:
                presenter.setSex("女");
                if (sexDialog != null) {
                    sexDialog.dismiss();
                }
                break;
            case R.id.view_nickname:// 昵称按钮
                presenter.jumpName();
                break;
            case R.id.view_brief_introduction:// 简介按钮
                presenter.jumpIntroduce();
                break;

            case R.id.view_age:// 年龄按钮
                if (dateDialog != null) {
                    dateDialog.show();
                }
                break;
            case R.id.view_region:// 地区按钮
                if (cityDialog != null) {
                    cityDialog.show(); // 设置群地址
                }
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
            GlideUtils.loadImageViewRound(url, image_head, 150, 150);
        } else {
            GlideUtils.loadImageViewRound(R.mipmap.icon_avatar_d, image_head, 60, 60);
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
            GlobalStateConfig.LoopViewW = PhoneMsgManager.ScreenWidth / 2-40;
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
                        String p = provinceList.get(provinceIndex).trim();
                        if (p != null && !p.equals("")) {
                            if (p.equals("澳门特别行政区") || p.equals("香港特别行政区") || p.equals("台湾省")) {
                                presenter.sendAddress(name);
                            } else {
                                String news = p + "-" + name;
                                presenter.sendAddress(news);
                            }
                        } else {
                            presenter.sendAddress(name);
                        }
                        cityDialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    // 上传头像选择框
    private void imgDialog() {
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

    // 性别选择框
    private void sexDialog() {
        final View dialog = LayoutInflater.from(this.getActivity()).inflate(R.layout.dialog_updata_sex, null);
        dialog.findViewById(R.id.tv_man).setOnClickListener(this);  // 男
        dialog.findViewById(R.id.tv_women).setOnClickListener(this);  // 女
        dialog.findViewById(R.id.tv_quxiao_sex).setOnClickListener(this);   // 取消

        sexDialog = new Dialog(this.getActivity(), R.style.MyDialogs);
        sexDialog.setContentView(dialog);
        sexDialog.setCanceledOnTouchOutside(true);

        DisplayMetrics dm = new DisplayMetrics();
        this.getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        ViewGroup.LayoutParams params = dialog.getLayoutParams();
        params.width = screenWidth;
        dialog.setLayoutParams(params);

        Window window = sexDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.inOutStyle);
        window.setBackgroundDrawableResource(R.color.transparent_40_black);
    }

    // 年龄选择框
    public void agePickerDialog(final List<String> year, final List<String> month, final List<String> day28, final List<String> day29, final List<String> day30, final List<String> day31) {
        GlobalStateConfig.LoopViewW = PhoneMsgManager.ScreenWidth / 6;
        dayList = day31;
        final View dialog = LayoutInflater.from(this.getActivity()).inflate(R.layout.dialog_updata_age, null);
        LoopView pickYear = (LoopView) dialog.findViewById(R.id.pick_year);
        LoopView pickMonth = (LoopView) dialog.findViewById(R.id.pick_month);
        final LoopView pickDay = (LoopView) dialog.findViewById(R.id.pick_day);

        // 设置字体样式
        pickYear.setTextSize(15, 17);
        pickYear.setItems(year);
        pickYear.setInitPosition(year.size()-1);
        pYear = year.size()-1;
        pickMonth.setTextSize(15, 17);
        pickMonth.setItems(month);
        pickMonth.setInitPosition(0);
        pMonth = 0;
        pickDay.setTextSize(15, 17);
        pickDay.setItems(day31);
        pickDay.setInitPosition(0);
        pDay = 0;

        pickYear.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                pYear = index;
                if (month.get(pMonth).trim().equals("02")) {// 判断是不是闰年
                    String y = year.get(pYear).trim();
                    int yearInt = Integer.valueOf(y.substring(0, 4));
                    if (yearInt % 4 == 0 && yearInt % 100 != 0 || yearInt % 400 == 0) {// 是闰年
                        dayList = day29;
                        pickDay.setItems(day29);
                    } else {
                        dayList = day28;
                        pickDay.setItems(day28);
                    }
                }
            }
        });

        pickMonth.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                pMonth = index;
                if (month.get(pMonth).trim().equals("02")) {// 判断是不是闰年
                    String y = year.get(pYear).trim();
                    int yearInt = Integer.valueOf(y.substring(0, 4));
                    if (yearInt % 4 == 0 && yearInt % 100 != 0 || yearInt % 400 == 0) {// 是闰年
                        dayList = day29;
                        pickDay.setItems(day29);
                    } else {
                        dayList = day28;
                        pickDay.setItems(day28);
                    }

                } else if (month.get(pMonth).trim().equals("01") || month.get(pMonth).trim().equals("03")
                        || month.get(pMonth).trim().equals("05") || month.get(pMonth).trim().equals("07")
                        || month.get(pMonth).trim().equals("08") || month.get(pMonth).trim().equals("10")
                        || month.get(pMonth).trim().equals("12")) {   // 31 天
                    dayList = day31;
                    pickDay.setItems(day31);
                } else {// 30 天
                    dayList = day30;
                    pickDay.setItems(day30);
                }
            }
        });

        pickDay.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                pDay = index;
            }
        });

        dateDialog = new Dialog(this.getActivity(), R.style.MyDialogs);
        dateDialog.setContentView(dialog);
        dateDialog.setCanceledOnTouchOutside(true);

        DisplayMetrics dm = new DisplayMetrics();
        this.getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        ViewGroup.LayoutParams params = dialog.getLayoutParams();
        params.width = screenWidth;
        dialog.setLayoutParams(params);

        Window window = dateDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.inOutStyle);
        window.setBackgroundDrawableResource(R.color.transparent_40_black);

        dialog.findViewById(R.id.tv_confirm_age).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String y = year.get(pYear).trim();
                String m = month.get(pMonth).trim();
                String d = dayList.get(pDay).trim();
                String date = y + "-" + m + "-" + d + "";
                Log.e("日期", date.trim());
                presenter.sendAge(date.trim());
                dateDialog.dismiss();

            }
        });

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.destroy();
        presenter=null;
    }
}
