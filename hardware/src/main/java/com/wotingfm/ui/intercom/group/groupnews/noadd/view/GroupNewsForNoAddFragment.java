package com.wotingfm.ui.intercom.group.groupnews.noadd.view;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.woting.commonplat.manager.PhoneMsgManager;
import com.woting.commonplat.widget.TipView;
import com.wotingfm.R;
import com.wotingfm.common.utils.DialogUtils;
import com.wotingfm.common.utils.GlideUtils;
import com.wotingfm.common.view.myscrollview.ObservableScrollView;
import com.wotingfm.ui.base.basefragment.BaseFragment;
import com.wotingfm.ui.base.baseinterface.ScrollViewListener;
import com.wotingfm.ui.intercom.group.groupnews.noadd.adapter.GroupNewsPersonForNoAddAdapter;
import com.wotingfm.ui.intercom.group.groupnews.noadd.presenter.GroupNewsForNoAddPresenter;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;

import java.util.List;

/**
 * 未加入的组详情界面
 * 作者：xinLong on 2017/6/5 01:30
 * 邮箱：645700751@qq.com
 */
public class GroupNewsForNoAddFragment extends BaseFragment implements View.OnClickListener, TipView.TipViewClick, ScrollViewListener {
    private View rootView;
    private TextView tv_groupName, tv_groupNumber, tv_address, tv_groupIntroduce, tv_number, tv_send, tvTitle;
    private RelativeLayout re_groupIntroduce, re_groupNumber;
    private GridView gridView;
    private GroupNewsForNoAddPresenter presenter;
    private GroupNewsPersonForNoAddAdapter adapter;
    private Dialog dialog;
    private TipView tip_view;
    private int type;
    private ObservableScrollView scrollView;
    private int height = 480;// 滑动开始变色的高
    private RelativeLayout mRelativeLayout;
    private ImageView head_left_btn, img_other,img_url;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_groupnewsnoadd, container, false);
            rootView.setOnClickListener(this);
            inItView();
            presenter = new GroupNewsForNoAddPresenter(this);
            presenter.getData();
            if(PhoneMsgManager.ScreenWidth==480){
                height=240;
            }
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
        head_left_btn.setImageResource(R.drawable.icon_mine_img_close);
        img_other.setImageResource(R.drawable.icon_chat_other);
        scrollView = (ObservableScrollView) rootView.findViewById(R.id.scrollView);//
        scrollView.setScrollViewListener(this);
        img_url = (ImageView) rootView.findViewById(R.id.img_url);// 群头像
        tv_groupName = (TextView) rootView.findViewById(R.id.tv_groupName);// 群名称
        tv_groupNumber = (TextView) rootView.findViewById(R.id.tv_groupNumber);// 群号
        tv_address = (TextView) rootView.findViewById(R.id.tv_address);// 地址
        tv_groupIntroduce = (TextView) rootView.findViewById(R.id.tv_groupIntroduce);// 群介绍
        re_groupIntroduce = (RelativeLayout) rootView.findViewById(R.id.re_groupIntroduce);// 群介绍按钮(不需要)
        re_groupIntroduce.setOnClickListener(this);
        tv_number = (TextView) rootView.findViewById(R.id.tv_number);// 成员数
        re_groupNumber = (RelativeLayout) rootView.findViewById(R.id.re_groupNumber);// 成员数按钮
        re_groupNumber.setOnClickListener(this);
        gridView = (GridView) rootView.findViewById(R.id.gridView);// 成员展示
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        tv_send = (TextView) rootView.findViewById(R.id.tv_send);// 加入群
        tv_send.setOnClickListener(this);
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
            case R.id.tv_send:
                presenter.apply();
                break;
            case R.id.re_groupNumber:
                presenter.jump();// 跳转到成员列表界面
                break;
            case R.id.img_other:
                break;
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
    public void setViewData(String url,String name, String number, String address, String introduce) {
        if (url != null && !url.trim().equals("") && url.startsWith("http")) {
            GlideUtils.loadImageViewSrc(url, img_url, true, 8);
        } else {
            GlideUtils.loadImageViewSrc(R.mipmap.p, img_url, true, 8);
        }

        tv_groupName.setText(name);// 群名称
        tv_groupNumber.setText(number);// 群号
        tv_address.setText(address);// 地址
        tv_groupIntroduce.setText(introduce);// 群介绍
    }

    /**
     * 设置成员展示
     *
     * @param list
     */
    public void setGridViewData(List<Contact.user> list) {
        re_groupNumber.setVisibility(View.VISIBLE);
        if (adapter == null) {
            adapter = new GroupNewsPersonForNoAddAdapter(this.getActivity(), list);
            gridView.setAdapter(adapter);
        } else {
            adapter.changeData(list);
        }
        tv_number.setText("（" + String.valueOf(list.size()) + "）");// 成员数
    }

    @Override
    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldX, int oldY) {
        Log.e("滑动大小","x"+x);
        Log.e("滑动大小","y"+y);
        Log.e("滑动大小","oldX"+oldX);
        Log.e("滑动大小","oldY"+oldY);
        Log.e("滑动大小","height"+height);
        if (y <= 0) {   //设置标题的背景颜色
            mRelativeLayout.setBackgroundColor(Color.argb((int) 0, 255, 255, 255));
            tvTitle.setVisibility(View.GONE);
            head_left_btn.setImageResource(R.drawable.icon_mine_img_close);
            img_other.setImageResource(R.drawable.icon_chat_other);
        } else if (y > 0 && y <= height) {
            float scale = (float) y / height;
            float alpha = (255 * scale);
            mRelativeLayout.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
            tvTitle.setVisibility(View.GONE);
            head_left_btn.setImageResource(R.drawable.icon_mine_img_close);
            img_other.setImageResource(R.drawable.icon_chat_other);
        } else if(y>height) {
            mRelativeLayout.setBackgroundColor(Color.argb((int) 255, 255, 255, 255));
            tvTitle.setVisibility(View.VISIBLE);
            head_left_btn.setImageResource(R.drawable.icon_back_black);
            img_other.setImageResource(R.drawable.icon_chat_other_black);
        }
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
            tip_view.setTipView(TipView.TipStatus.NO_DATA, "您还没有聊天对象哟\n快去找好友们聊天吧");
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
