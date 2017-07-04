package com.wotingfm.ui.intercom.group.groupnews.add.view;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.woting.commonplat.widget.TipView;
import com.wotingfm.R;
import com.wotingfm.common.utils.DialogUtils;
import com.wotingfm.common.utils.ToastUtils;
import com.wotingfm.common.view.myscrollview.ObservableScrollView;
import com.wotingfm.ui.base.baseinterface.ScrollViewListener;
import com.wotingfm.ui.intercom.group.groupmanage.GroupManageFragment;
import com.wotingfm.ui.intercom.group.groupnews.add.adapter.GroupNewsPersonForAddAdapter;
import com.wotingfm.ui.intercom.group.groupnews.add.presenter.GroupNewsForAddPresenter;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;
import com.wotingfm.ui.intercom.main.simulation.SimulationInterPhoneFragment;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;

import java.util.List;

/**
 * 已加入的组详情界面
 * 作者：xinLong on 2017/6/5 01:30
 * 邮箱：645700751@qq.com
 */
public class GroupNewsForAddFragment extends Fragment implements View.OnClickListener, TipView.TipViewClick, ScrollViewListener {
    private View rootView;
    private GroupNewsForAddPresenter presenter;
    private TextView tv_groupName, tv_groupNumber, tv_address, tv_groupIntroduce, tv_number, tv_channel1, tv_channel2, tvTitle;
    private RelativeLayout re_groupIntroduce, re_groupNumber, re_groupEwm, re_channel1, re_channel2, re_groupManager;
    private GridView gridView;
    private GroupNewsPersonForAddAdapter adapter;
    private Dialog dialog;
    private TipView tip_view;
    private int type;
    private ObservableScrollView scrollView;
    private int height = 480;// 滑动开始变色的高
    private RelativeLayout mRelativeLayout;
    private ImageView head_left_btn, img_other;
    private LinearLayout lin_chose;
    private ResultListener Listener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_groupnewsadd, container, false);
            rootView.setOnClickListener(this);
            inItView();
            presenter = new GroupNewsForAddPresenter(this);
            presenter.getData();
        }
        return rootView;
    }

    private void inItView() {
        tip_view = (TipView) rootView.findViewById(R.id.tip_view);// 提示界面
        tip_view.setTipClick(this);

        mRelativeLayout = (RelativeLayout) rootView.findViewById(R.id.mRelativeLayout);// 标题栏
        head_left_btn = (ImageView) rootView.findViewById(R.id.head_left_btn);// 返回按钮
        head_left_btn.setOnClickListener(this);
        tvTitle = (TextView) rootView.findViewById(R.id.tvTitle);//
        img_other = (ImageView) rootView.findViewById(R.id.img_other);// 其它按钮
        img_other.setOnClickListener(this);

        scrollView = (ObservableScrollView) rootView.findViewById(R.id.scrollView);//
        scrollView.setScrollViewListener(this);
        tv_groupName = (TextView) rootView.findViewById(R.id.tv_groupName);// 群名称
        tv_groupNumber = (TextView) rootView.findViewById(R.id.tv_groupNumber);// 群号
        tv_address = (TextView) rootView.findViewById(R.id.tv_address);// 地址
        re_groupEwm = (RelativeLayout) rootView.findViewById(R.id.re_groupEwm);// 群二维码
        re_groupEwm.setOnClickListener(this);

        tv_groupIntroduce = (TextView) rootView.findViewById(R.id.tv_groupIntroduce);// 群介绍
        re_groupIntroduce = (RelativeLayout) rootView.findViewById(R.id.re_groupIntroduce);// 群介绍按钮（不需要）
        re_groupIntroduce.setOnClickListener(this);

        re_groupManager = (RelativeLayout) rootView.findViewById(R.id.re_groupManager);// 管理群
        re_groupManager.setOnClickListener(this);

        re_groupNumber = (RelativeLayout) rootView.findViewById(R.id.re_groupNumber);// 成员数按钮
        re_groupNumber.setOnClickListener(this);
        tv_number = (TextView) rootView.findViewById(R.id.tv_number);// 成员数
        gridView = (GridView) rootView.findViewById(R.id.gridView);// 成员展示
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));

        tv_channel1 = (TextView) rootView.findViewById(R.id.tv_channel1);// 对讲频道1
        re_channel1 = (RelativeLayout) rootView.findViewById(R.id.re_channel1);
        re_channel1.setOnClickListener(this);
        tv_channel2 = (TextView) rootView.findViewById(R.id.tv_channel2);// 对讲频道2
        re_channel2 = (RelativeLayout) rootView.findViewById(R.id.re_channel2);
        re_channel2.setOnClickListener(this);

        rootView.findViewById(R.id.lin_send).setOnClickListener(this);// 开始对讲

        lin_chose = (LinearLayout) rootView.findViewById(R.id.lin_chose); // 图片选择
        lin_chose.setOnClickListener(this);
        rootView.findViewById(R.id.tv_exit).setOnClickListener(this);  // 退出该群
        rootView.findViewById(R.id.tv_quxiao).setOnClickListener(this);   // 取消
    }

    @Override
    public void onTipViewClick() {
        presenter.tipClick(type);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left_btn:
                InterPhoneActivity.close();
                break;
            case R.id.img_other:
                presenter.headViewShow();
                break;
            case R.id.re_groupEwm:
                ToastUtils.show_always(this.getActivity(), "二维码界面");
                break;
            case R.id.re_groupManager:
                presenter.jumpManager();
                break;
            case R.id.lin_send:
                ToastUtils.show_always(this.getActivity(), "开始对讲");
                break;
            case R.id.re_channel1:
                SimulationInterPhoneFragment fragment = new SimulationInterPhoneFragment();
                Bundle bundle = new Bundle();
                bundle.putString("channel", "00000");
                fragment.setArguments(bundle);
                InterPhoneActivity.open(fragment);
                break;
            case R.id.re_channel2:
                SimulationInterPhoneFragment fragment1 = new SimulationInterPhoneFragment();
                Bundle bundle1 = new Bundle();
                bundle1.putString("channel", "00000");
                fragment1.setArguments(bundle1);
                InterPhoneActivity.open(fragment1);
                break;
            case R.id.re_groupNumber:
                presenter.jumpGroupNumberShow();
                break;
            case R.id.tv_exit:
                presenter.exit();
                presenter.headViewShow();
                break;
            case R.id.tv_quxiao:
                presenter.headViewShow();
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
     * 设置群管理功能是否展示
     *
     * @param b
     */
    public void setViewForMy(boolean b) {
        if (b) {
            re_groupManager.setVisibility(View.VISIBLE);
        } else {
            re_groupManager.setVisibility(View.GONE);
        }
    }

    @Override
    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldX, int oldY) {
        Log.e("滑动大小", "x" + x);
        Log.e("滑动大小", "y" + y);
        Log.e("滑动大小", "oldX" + oldX);
        Log.e("滑动大小", "oldY" + oldY);
        Log.e("滑动大小", "height" + height);
        if (y <= 0) {   //设置标题的背景颜色
            mRelativeLayout.setBackgroundColor(Color.argb((int) 0, 255, 255, 255));
            tvTitle.setVisibility(View.GONE);
            head_left_btn.setImageResource(R.mipmap.nav_icon_back_white);
            img_other.setImageResource(R.mipmap.nav_icon_more_white);
        } else if (y > 0 && y <= height) {
            float scale = (float) y / height;
            float alpha = (255 * scale);
            mRelativeLayout.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
            tvTitle.setVisibility(View.GONE);
            head_left_btn.setImageResource(R.mipmap.nav_icon_back_white);
            img_other.setImageResource(R.mipmap.nav_icon_more_white);
        } else if (y > height) {
            mRelativeLayout.setBackgroundColor(Color.argb((int) 255, 255, 255, 255));
            tvTitle.setVisibility(View.VISIBLE);
            head_left_btn.setImageResource(R.mipmap.nav_icon_back_black);
            img_other.setImageResource(R.mipmap.nav_icon_more_black);
        }
    }

    /**
     * 设置界面数据
     *
     * @param name
     * @param number
     * @param address
     * @param introduce
     */
    public void setViewData(String name, String number, String address, String introduce, String channel1, String channel2) {
        tv_groupName.setText(name);          // 群名称
        tvTitle.setText(name);               // 群名称
        tv_groupNumber.setText(number);      // 群号
        tv_address.setText(address);         // 地址
        tv_groupIntroduce.setText(introduce);// 群介绍
        tv_channel1.setText(channel1);       // 对讲频道1
        tv_channel2.setText(channel2);       // 对讲频道2
    }

    /**
     * 设置成员展示
     *
     * @param list
     */
    public void setGridViewData(List<Contact.user> list) {
        re_groupNumber.setVisibility(View.VISIBLE);
        if (adapter == null) {
            adapter = new GroupNewsPersonForAddAdapter(this.getActivity(), list);
            gridView.setAdapter(adapter);
        } else {
            adapter.changeData(list);
        }
        setGridListener(list);
        tv_number.setText("（" + String.valueOf(list.size()) + "人）");// 成员数
    }

    private void setGridListener(final List<Contact.user> list) {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                presenter.setGridItemClick(list, position);
            }
        });
    }


    /**
     * 隐藏成员展示
     * 设置没有群成员的界面，也就意味着数据有错
     */
    public void setViewForNoGroupPerson() {
        re_groupNumber.setVisibility(View.GONE);
    }

    /**
     * 是否登录，是否有数据
     *
     * @param type 登录后数据类型
     *             0 正常有数据
     *             NO_DATA,没有数据 1
     *             NO_NET,没有网络 2
     *             NO_LOGIN,没有登录 3
     *             IS_ERROR,加载错误 4
     */
    public void isLoginView(int type) {
        this.type = type;
        if (type == 0) {
            // 已经登录，并且有数据
            scrollView.setVisibility(View.VISIBLE);
            tip_view.setVisibility(View.GONE);
        } else if (type == 1) {
            // 已经登录，没有数据
            scrollView.setVisibility(View.GONE);
            tip_view.setVisibility(View.VISIBLE);
            tip_view.setTipView(TipView.TipStatus.NO_DATA);
        } else if (type == 2) {
            // 没有网络
            scrollView.setVisibility(View.GONE);
            tip_view.setVisibility(View.VISIBLE);
            tip_view.setTipView(TipView.TipStatus.NO_NET);
        } else if (type == 3) {
            // 没有登录
            scrollView.setVisibility(View.GONE);
            tip_view.setVisibility(View.VISIBLE);
            tip_view.setTipView(TipView.TipStatus.NO_LOGIN);
        } else if (type == 4) {
            // 已经登录，数据加载失败
            scrollView.setVisibility(View.GONE);
            tip_view.setVisibility(View.VISIBLE);
            tip_view.setTipView(TipView.TipStatus.IS_ERROR);
        }
    }

    /**
     * 设置退出组的返回监听
     */
    public void exitResult(){
        Listener.resultListener(true);
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
     * 回调结果值
     *
     * @param l
     */
    public void setResultListener(ResultListener l) {
        Listener = l;
    }

    public interface ResultListener {
        void resultListener(boolean type);
    }
}
