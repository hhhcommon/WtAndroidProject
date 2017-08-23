package com.wotingfm.ui.intercom.main.view;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.wotingfm.R;
import com.wotingfm.ui.bean.MessageEvent;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.constant.BroadcastConstants;
import com.wotingfm.common.utils.CommonUtils;
import com.wotingfm.ui.base.baseadapter.MyFragmentPagerAdapter;
import com.wotingfm.ui.intercom.add.find.FindFragment;
import com.wotingfm.ui.intercom.group.creat.view.CreateGroupMainFragment;
import com.wotingfm.ui.intercom.main.chat.view.ChatFragment;
import com.wotingfm.ui.intercom.main.contacts.fragment.ContactsFragment;
import com.wotingfm.ui.intercom.main.simulation.view.SimulationInterPhoneFragment;
import com.wotingfm.ui.intercom.scanning.activity.CaptureFragment;
import com.wotingfm.ui.user.logo.LogoActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * 对讲模块主页
 * 作者：xinLong on 2017/6/4 23:20
 * 邮箱：645700751@qq.com
 */

public class InterPhoneFragment extends Fragment implements View.OnClickListener {
    private TextView tv_chat, tv_linkman, line_chat, line_linkman;
    private PopupWindow addDialog;
    private ViewPager mPager;
    private ImageView img_more,img_person;
    private View rootView;
    private InterPhonePresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_intercom, container, false);
            InitTextView();  // 初始化视图
            InitViewPager(); // 初始化 ViewPager
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

        tv_chat = (TextView) rootView.findViewById(R.id.tv_chat);          // 展示条
        line_chat = (TextView) rootView.findViewById(R.id.line_chat);      // 展示条
        tv_linkman = (TextView) rootView.findViewById(R.id.tv_linkman);    // 展示条
        line_linkman = (TextView) rootView.findViewById(R.id.line_linkman);// 展示条

        tv_chat.setOnClickListener(new txListener(0));
        tv_linkman.setOnClickListener(new txListener(1));

    }

    // 初始化ViewPager
    public void InitViewPager() {
        mPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        mPager.setOffscreenPageLimit(1);
        ArrayList<Fragment> fragmentList = new ArrayList<>();
        Fragment ctFragment = new ChatFragment();   // 对讲页
        Fragment cFragment = new ContactsFragment();// 通讯录
        fragmentList.add(ctFragment);
        fragmentList.add(cFragment);
        mPager.setAdapter(new MyFragmentPagerAdapter(getChildFragmentManager(), fragmentList));
        mPager.setOnPageChangeListener(new MyOnPageChangeListener());// 页面变化时的监听器
        update(0);
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

    // TextView 点击事件监听
    public class txListener implements View.OnClickListener {
        private int index = 0;

        public txListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            update(index);
        }
    }

    // ViewPager 监听事件
    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageSelected(int arg0) {
            update(arg0);
        }
    }

    // 设置头部样式
    private void update(int arg0) {
        mPager.setCurrentItem(arg0);
        if (arg0 == 0) {
            tv_chat.setTextColor(this.getActivity().getResources().getColor(R.color.app_basic));
            tv_linkman.setTextColor(this.getActivity().getResources().getColor(R.color.black_head_word));
            line_chat.setVisibility(View.VISIBLE);
            line_linkman.setVisibility(View.INVISIBLE);
        } else if (arg0 == 1) {
            tv_chat.setTextColor(this.getActivity().getResources().getColor(R.color.black_head_word));
            tv_linkman.setTextColor(this.getActivity().getResources().getColor(R.color.app_basic));
            line_chat.setVisibility(View.INVISIBLE);
            line_linkman.setVisibility(View.VISIBLE);
        }
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
