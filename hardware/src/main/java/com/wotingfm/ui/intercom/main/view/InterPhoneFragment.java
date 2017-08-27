package com.wotingfm.ui.intercom.main.view;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.wotingfm.R;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.constant.BroadcastConstants;
import com.wotingfm.common.utils.CommonUtils;
import com.wotingfm.ui.bean.MessageEvent;
import com.wotingfm.ui.intercom.add.find.FindFragment;
import com.wotingfm.ui.intercom.group.creat.view.CreateGroupMainFragment;
import com.wotingfm.ui.intercom.main.simulation.view.SimulationInterPhoneFragment;
import com.wotingfm.ui.intercom.scanning.activity.CaptureFragment;
import com.wotingfm.ui.play.find.main.adapter.MyAdapter;
import com.wotingfm.ui.user.logo.LogoActivity;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 对讲模块主页
 * 作者：xinLong on 2017/6/4 23:20
 * 邮箱：645700751@qq.com
 */

public class InterPhoneFragment extends Fragment implements View.OnClickListener {
    private PopupWindow addDialog;
    private ViewPager mPager;
    private ImageView img_more,img_person;
    private View rootView;
    private InterPhonePresenter presenter;
    private TabLayout tabs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_intercom, container, false);
            InitTextView();  // 初始化视图
            dialog();        // 初始化功能弹出框
            presenter = new InterPhonePresenter(this);
        }
        return rootView;
    }

    // 初始化视图
    private void InitTextView() {
        img_more = (ImageView) rootView.findViewById(R.id.img_more);       // 三个点
        img_more.setOnClickListener(this);
        img_person = (ImageView) rootView.findViewById(R.id.img_person);   // 个人中心
        img_person.setOnClickListener(this);

        mPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        mPager.setOffscreenPageLimit(1);
        tabs=(TabLayout) rootView.findViewById(R.id.tabs);   // 个人中心

    }

    /**
     * 数据适配
     *
     * @param type
     * @param list
     */
    public void setData(List<String> type, List<Fragment> list) {
        MyAdapter mAdapter = new MyAdapter(getChildFragmentManager(), type, list);
        mPager.setAdapter(mAdapter);
        // viewPager.setOffscreenPageLimit(1);
        tabs.setupWithViewPager(mPager);
        tabs.post(new Runnable() {
            @Override
            public void run() {
                try {
                    //拿到tabLayout的mTabStrip属性
                    Field mTabStripField = tabs.getClass().getDeclaredField("mTabStrip");
                    mTabStripField.setAccessible(true);

                    LinearLayout mTabStrip = (LinearLayout) mTabStripField.get(tabs);

                    int dp10 = dp2px(getContext(), 22);

                    for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                        View tabView = mTabStrip.getChildAt(i);

                        //拿到tabView的mTextView属性
                        Field mTextViewField = tabView.getClass().getDeclaredField("mTextView");
                        mTextViewField.setAccessible(true);

                        TextView mTextView = (TextView) mTextViewField.get(tabView);

                        tabView.setPadding(0, 0, 0, 0);

                        //因为我想要的效果是   字多宽线就多宽，所以测量mTextView的宽度
                        int width = 0;
                        width = mTextView.getWidth();
                        if (width == 0) {
                            mTextView.measure(0, 0);
                            width = mTextView.getMeasuredWidth();
                        }

                        //设置tab左右间距为10dp  注意这里不能使用Padding 因为源码中线的宽度是根据 tabView的宽度来设置的
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                        params.width = width ;
                        params.leftMargin = dp10;
                        params.rightMargin = dp10;
                        tabView.setLayoutParams(params);

                        tabView.invalidate();
                    }

                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
//                setIndicator(tabs,20,20);
            }
        });
    }

    // "更多" 对话框
    private void dialog() {
        View dialog = LayoutInflater.from(this.getActivity()).inflate(R.layout.dialog_intercom, null);
        TextView tv_addFriend = (TextView) dialog.findViewById(R.id.tv_addFriend);        // 添加好友
        TextView tv_addGroup = (TextView) dialog.findViewById(R.id.tv_addGroup);          // 加入群组
        TextView tv_createGroup = (TextView) dialog.findViewById(R.id.tv_createGroup);    // 创建群组
        TextView tv_scanning = (TextView) dialog.findViewById(R.id.tv_scanning);          // 扫一扫
        TextView tv_intercom = (TextView) dialog.findViewById(R.id.tv_intercom);          // 模拟对讲
        tv_addFriend.setOnClickListener(this);
        tv_addGroup.setOnClickListener(this);
        tv_createGroup.setOnClickListener(this);
        tv_scanning.setOnClickListener(this);
        tv_intercom.setOnClickListener(this);

        addDialog = new PopupWindow(dialog);
        // 使其聚集
        addDialog.setFocusable(true);
        addDialog.setBackgroundDrawable(new ColorDrawable(0x00000000));
        // 设置允许在外点击消失
        addDialog.setOutsideTouchable(true);
        // 控制popupwindow的宽度和高度自适应
        dialog.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        addDialog.setWidth(dialog.getMeasuredWidth());
        addDialog.setHeight(dialog.getMeasuredHeight());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_more:// 弹出功能弹出框
                if (addDialog != null && addDialog.isShowing()) {
                    addDialog.dismiss();
                } else {
                    if (addDialog != null) {
                        addDialog.showAsDropDown(img_more,0,-10);
                    }
                }
                break;
            case R.id.tv_addFriend:// 跳转到添加好友
                if (CommonUtils.isLogin()) {
                    FindFragment fragment = new FindFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("type", "friend");
                    fragment.setArguments(bundle);
                    InterPhoneActivity.open(fragment);
                } else {
                    startActivity(new Intent(this.getActivity(), LogoActivity.class));
                }
                addDialog.dismiss();
                break;
            case R.id.tv_addGroup:        // 跳转到加入群组
                if (CommonUtils.isLogin()) {
                    FindFragment fragment = new FindFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("type", "group");
                    fragment.setArguments(bundle);
                    InterPhoneActivity.open(fragment);
                } else {
                    startActivity(new Intent(this.getActivity(), LogoActivity.class));
                }
                addDialog.dismiss();
                break;
            case R.id.tv_createGroup:// 跳转到创建讨论组
                if (CommonUtils.isLogin()) {
                    InterPhoneActivity.open(new CreateGroupMainFragment());
                } else {
                    startActivity(new Intent(this.getActivity(), LogoActivity.class));
                }
                addDialog.dismiss();
                break;
            case R.id.tv_scanning:
                if (CommonUtils.isLogin()) {
                    InterPhoneActivity.open(new CaptureFragment());
                } else {
                    startActivity(new Intent(this.getActivity(), LogoActivity.class));
                }
                addDialog.dismiss();
                break;
            case R.id.tv_intercom:
                SimulationInterPhoneFragment f=  new SimulationInterPhoneFragment();
                Bundle b = new Bundle();
                b.putString("channel", "");
                f.setArguments(b);
                InterPhoneActivity.open(f);
                addDialog.dismiss();
                break;
            case R.id.img_person:
                GlobalStateConfig.mineFromType = 2;
                GlobalStateConfig.activityA = "A";
                GlobalStateConfig.activityB = "C";
              //  MainActivity.changeThree();
                EventBus.getDefault().post(new MessageEvent("three"));
                Intent push = new Intent(BroadcastConstants.MINE_ACTIVITY_CHANGE);
                Bundle bundle = new Bundle();
                bundle.putInt("viewType", 3);
                push.putExtras(bundle);
                this.getActivity().sendBroadcast(push);
                break;
        }

    }


    // 设置头部样式
    private void update(int arg0) {
        mPager.setCurrentItem(arg0);
    }

    /**
     * 反射修改下划线的宽度
     * @param tabs
     * @param leftDip
     * @param rightDip
     */
    public void setIndicator (TabLayout tabs,int leftDip,int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }

    /**
     * dpתpx
     *
     * @param context
     * @param dp
     * @return
     */
    public static int dp2px(Context context, float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp, context.getResources().getDisplayMetrics());
    }

    /**
         * 更改样式
         * @param type
         */
    public void change(int type) {
        update(type);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.destroy();
    }
}
