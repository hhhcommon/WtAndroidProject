package com.wotingfm.ui.intercom.main;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.wotingfm.R;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.constant.StringConstant;
import com.wotingfm.ui.base.baseadapter.MyFragmentPagerAdapter;
import com.wotingfm.ui.intercom.find.FindActivity;
import com.wotingfm.ui.intercom.group.CreateGroupActivity;
import com.wotingfm.ui.intercom.main.chat.fragment.ChatFragment;
import com.wotingfm.ui.intercom.main.contacts.fragment.ContactsFragment;
import com.wotingfm.ui.scanning.activity.CaptureActivity;
import com.wotingfm.ui.user.login.view.LoginActivity;

import java.util.ArrayList;

/**
 * 对讲模块主页
 * 作者：xinLong on 2017/6/4 23:20
 * 邮箱：645700751@qq.com
 */

public class InterPhoneActivity extends FragmentActivity implements View.OnClickListener {
    private static TextView tv_chat, tv_linkman, line_chat, line_linkman;
    private PopupWindow addDialog;
    private static ViewPager mPager;
    private ImageView img_more;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intercom);
        InitTextView();  // 初始化视图
        InitViewPager(); // 初始化 ViewPager
        dialog();        // 初始化功能弹出框
    }

    // 初始化视图
    private void InitTextView() {
        img_more = (ImageView) findViewById(R.id.img_more);
        img_more.setOnClickListener(this);

        tv_chat = (TextView) findViewById(R.id.tv_chat);
        line_chat = (TextView) findViewById(R.id.line_chat);
        tv_linkman = (TextView) findViewById(R.id.tv_linkman);
        line_linkman = (TextView) findViewById(R.id.line_linkman);

        tv_chat.setOnClickListener(new txListener(0));
        tv_linkman.setOnClickListener(new txListener(1));

    }

    // 初始化ViewPager
    public void InitViewPager() {
        mPager = (ViewPager) findViewById(R.id.viewpager);
        mPager.setOffscreenPageLimit(1);
        ArrayList<Fragment> fragmentList = new ArrayList<>();
        Fragment ctFragment = new ChatFragment();// 电台首页
        Fragment cFragment = new ContactsFragment();// 通讯录
        fragmentList.add(ctFragment);
        fragmentList.add(cFragment);
        mPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList));
        mPager.setOnPageChangeListener(new MyOnPageChangeListener());// 页面变化时的监听器
        update(0);
    }

    // "更多" 对话框
    private void dialog() {
        View dialog = LayoutInflater.from(this).inflate(R.layout.dialog_intercom, null);
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
                        addDialog.showAsDropDown(img_more);
                    }
                }
                break;
            case R.id.tv_addFriend:// 跳转到添加好友
                if (isLogin()) {
                    Intent Intent = new Intent(this, FindActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("type", "friend");
                    Intent.putExtras(bundle);
                    startActivity(Intent);
                } else {
                    startActivity(new Intent(this, LoginActivity.class));
                }
                addDialog.dismiss();
                break;
            case R.id.tv_addGroup:        // 跳转到加入群组
                if (isLogin()) {
                    Intent Intent = new Intent(this, FindActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("type", "group");
                    Intent.putExtras(bundle);
                    startActivity(Intent);
                } else {
                    startActivity(new Intent(this, LoginActivity.class));
                }
                addDialog.dismiss();
                break;
            case R.id.tv_createGroup:// 跳转到创建讨论组
                if (isLogin()) {
                    startActivity(new Intent(this, CreateGroupActivity.class));
                } else {
                    startActivity(new Intent(this, LoginActivity.class));
                }
                addDialog.dismiss();
                break;
            case R.id.tv_scanning:
                if (isLogin()) {
                    startActivity(new Intent(this, CaptureActivity.class));
                } else {
                    startActivity(new Intent(this, LoginActivity.class));
                }
                addDialog.dismiss();
                break;
            case R.id.tv_intercom:
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

    private boolean isLogin() {
        String login = BSApplication.SharedPreferences.getString(StringConstant.ISLOGIN, "false");// 是否登录
        if (!login.trim().equals("") && login.equals("true")) {
            return true;
        } else {
            return false;
        }
    }

    // 设置头部样式
    private void update(int arg0) {
        mPager.setCurrentItem(arg0);
        if (arg0 == 0) {
            tv_chat.setTextColor(this.getResources().getColor(R.color.app_basic));
            tv_linkman.setTextColor(this.getResources().getColor(R.color.black_head_word));
            line_chat.setTextColor(this.getResources().getColor(R.color.gray_edit_hint_word));
            line_linkman.setTextColor(this.getResources().getColor(R.color.app_basic));
            line_chat.setVisibility(View.VISIBLE);
            line_linkman.setVisibility(View.INVISIBLE);
        } else if (arg0 == 1) {
            tv_chat.setTextColor(this.getResources().getColor(R.color.black_head_word));
            tv_linkman.setTextColor(this.getResources().getColor(R.color.app_basic));
            line_chat.setTextColor(this.getResources().getColor(R.color.gray_edit_hint_word));
            line_linkman.setTextColor(this.getResources().getColor(R.color.app_basic));
            line_chat.setVisibility(View.INVISIBLE);
            line_linkman.setVisibility(View.VISIBLE);
        }
    }

    /*
     * 手机实体返回按键的处理
     */
    long waitTime = 2000;
    long touchTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && KeyEvent.KEYCODE_BACK == keyCode) {
            long currentTime = System.currentTimeMillis();
            if ((currentTime - touchTime) >= waitTime) {
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_LONG).show();
                touchTime = currentTime;
            } else {
                finish();
                android.os.Process.killProcess(android.os.Process.myPid());
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
