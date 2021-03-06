package com.wotingfm.ui.intercom.person.personmessage.view;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.woting.commonplat.manager.PhoneMsgManager;
import com.woting.commonplat.widget.TipView;
import com.wotingfm.R;
import com.wotingfm.ui.base.basefragment.BaseFragment;
import com.wotingfm.ui.bean.AlbumsBean;
import com.wotingfm.common.utils.DialogUtils;
import com.wotingfm.common.utils.GlideUtils;
import com.wotingfm.common.utils.ToastUtils;
import com.wotingfm.common.view.myscrollview.ObservableScrollView;
import com.wotingfm.ui.base.baseinterface.ScrollViewListener;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;
import com.wotingfm.ui.intercom.person.personmessage.adapter.PersonMessageSubAdapter;
import com.wotingfm.ui.intercom.person.personmessage.presenter.PersonMessagePresenter;

import java.util.List;

/**
 * 用户详情，区分好友与非好友
 * 作者：xinLong on 2017/6/5 01:30
 * 邮箱：645700751@qq.com
 */
public class PersonMessageFragment extends BaseFragment implements View.OnClickListener, TipView.TipViewClick, ScrollViewListener {
    private View rootView;
    private TextView tv_send, tv_name, tv_introduce, tv_number, tv_address, tvTitle, tv_subNum, tv_focus;
    private LinearLayout lin_note;
    private PersonMessagePresenter presenter;
    private Dialog dialog, confirmDialog, delDialog,LDialog;
    private int type;
    private TipView tip_view;
    private boolean b;
    private ObservableScrollView scrollView;
    private int height = 480;// 滑动开始变色的高
    private RelativeLayout mRelativeLayout, re_sunNumber;
    private ImageView head_left_btn, img_other, img_call, img_url, img_bg;
    private GridView gridView;
    private PersonMessageSubAdapter adapter;
    private ResultListener Listener;
    private String acc_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_person_news, container, false);
            rootView.setOnClickListener(this);
            initViews();// 设置界面
            Dialog();
            DDialog();
            initDialogL();
            isLoginView(-1);
            presenter = new PersonMessagePresenter(this);
            presenter.getData();
            if (PhoneMsgManager.ScreenWidth == 480) {
                height = 240;
            }
        }
        return rootView;
    }

    // 初始化视图
    private void initViews() {
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

        head_left_btn.setImageResource(R.drawable.icon_mine_img_close);
        img_other.setImageResource(R.drawable.icon_chat_other);
        scrollView = (ObservableScrollView) rootView.findViewById(R.id.scrollView);//
        scrollView.setScrollViewListener(this);
        rootView.findViewById(R.id.lin_send).setOnClickListener(this);        // 黄色按钮
        tv_send = (TextView) rootView.findViewById(R.id.tv_send);             // 添加好友展示信息
        img_call = (ImageView) rootView.findViewById(R.id.img_call);          // 呼叫展示图片
        lin_note = (LinearLayout) rootView.findViewById(R.id.lin_note);       // 备注
        lin_note.setOnClickListener(this);

        img_bg = (ImageView) rootView.findViewById(R.id.img_bg);              // 背景图片
        img_url = (ImageView) rootView.findViewById(R.id.img_url);            // 头像展示图片
        tv_name = (TextView) rootView.findViewById(R.id.tv_name);             // 姓名
        tv_introduce = (TextView) rootView.findViewById(R.id.tv_introduce);   // 介绍
        tv_number = (TextView) rootView.findViewById(R.id.tv_number);         // 听号
        tv_address = (TextView) rootView.findViewById(R.id.tv_address);       // 地址
        tv_focus = (TextView) rootView.findViewById(R.id.tv_focus);           // 关注

        re_sunNumber = (RelativeLayout) rootView.findViewById(R.id.re_sunNumber);// 订阅按钮
        re_sunNumber.setOnClickListener(this);
        tv_subNum = (TextView) rootView.findViewById(R.id.tv_subNum);            // 订阅数
        tv_subNum.setOnClickListener(this);
        gridView = (GridView) rootView.findViewById(R.id.gridView);              // 订阅展示
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));


    }

    @Override
    public void onTipViewClick() {
        presenter.tipClick(type);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_subNum:

                break;
            case R.id.head_left_btn:
                close();
                break;
            case R.id.img_other:
                delDialog.show();
                break;
            case R.id.lin_note: // 设置备注
                presenter.jumpNote();
                break;
            case R.id.tv_quxiao:
                delDialog.dismiss();
                break;
            case R.id.tv_name:
                LDialog.show();
                delDialog.dismiss();
                break;
            case R.id.lin_send:
                if (b) {
                    if (!TextUtils.isEmpty(acc_id)) {
                        presenter.call(acc_id);
                    }
                } else {
                    presenter.apply();
                }
                break;
            case R.id.re_sunNumber:
                ToastUtils.show_always(this.getActivity(), "跳转到订阅列表");
                break;
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
            head_left_btn.setImageResource(R.drawable.icon_mine_img_close);
            img_other.setImageResource(R.drawable.icon_chat_other);
        } else if (y > 0 && y <= height) {
            float scale = (float) y / height;
            float alpha = (255 * scale);
            mRelativeLayout.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
            tvTitle.setVisibility(View.GONE);
            head_left_btn.setImageResource(R.drawable.icon_mine_img_close);
            img_other.setImageResource(R.drawable.icon_chat_other);
        } else if (y > height) {
            mRelativeLayout.setBackgroundColor(Color.argb((int) 255, 255, 255, 255));
            tvTitle.setVisibility(View.VISIBLE);
            head_left_btn.setImageResource(R.drawable.icon_back_black);
            img_other.setImageResource(R.drawable.icon_chat_other_black);
        }
    }

    /**
     * 设置成员展示
     *
     * @param list
     */
    public void setGridViewData(List<AlbumsBean> list) {
        re_sunNumber.setVisibility(View.VISIBLE);
        if (adapter == null) {
            adapter = new PersonMessageSubAdapter(this.getActivity(), list);
            gridView.setAdapter(adapter);
        } else {
            adapter.changeData(list);
        }
        setGridListener(list);
        tv_subNum.setText("(" + String.valueOf(list.size()) + ")");// 订阅数
    }

    private void setGridListener(final List<AlbumsBean> list) {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                presenter.setGridItemClick(list, position);
            }
        });
    }

    /**
     * 隐藏订阅展示
     */
    public void setGridViewDataNull() {
        re_sunNumber.setVisibility(View.GONE);
    }

//    /**
//     * 删除好友界面的展示
//     *
//     * @param type true 展示/false 不展示
//     */
//    public void imageShow(boolean type) {
//        if (type) {
//            Animation mAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.wt_slide_in_from_bottom);
//            lin_chose.setAnimation(mAnimation);
//            lin_chose.setVisibility(View.VISIBLE);
//        } else {
//            Animation mAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.wt_slide_out_from_bottom);
//            lin_chose.setAnimation(mAnimation);
//            lin_chose.setVisibility(View.GONE);
//        }
//    }

    /**
     * 根据好友状态设置界面展示
     *
     * @param b true 好友/false 非好友
     */
    public void setView(boolean b) {
        this.b = b;
        if (b) {
            // 是好友
            tv_send.setVisibility(View.GONE);
            img_call.setVisibility(View.VISIBLE);
            lin_note.setVisibility(View.VISIBLE);
            img_other.setVisibility(View.VISIBLE);
        } else {
            // 不是好友
            tv_send.setVisibility(View.VISIBLE);
            img_call.setVisibility(View.GONE);
            lin_note.setVisibility(View.GONE);
            img_other.setVisibility(View.GONE);
        }
    }

    /**
     * 设置界面展示数据
     *
     * @param url       头像
     * @param name      姓名
     * @param introduce 介绍
     * @param number    听号
     * @param address   地址
     * @param acc_id    地址
     */
    public void setViewData(String url, String name, String introduce, String number, String address, String focus, String acc_id) {
        this.acc_id = acc_id;
        if (url != null && !url.trim().equals("") && url.startsWith("http")) {
            GlideUtils.loadImageViewSrc(url, img_bg, true, 8);
        } else {
            GlideUtils.loadImageViewSrc(R.mipmap.p, img_bg, true, 8);
        }

        if (url != null && !url.equals("") && url.startsWith("http")) {
            GlideUtils.loadImageViewRound(url, img_url, 150, 150);
        } else {
            GlideUtils.loadImageViewRound(R.mipmap.icon_avatar_d, img_url, 60, 60);
        }
        tv_name.setText(name);             // 姓名
        tvTitle.setText(name);             // 姓名
        tv_introduce.setText(introduce);   // 介绍
        tv_number.setText(number);         // 听号
        tv_address.setText(address);       // 地址
        tv_focus.setText("关注 " + focus);  // 关注
    }


    /**
     * 修改备注后设置界面数据
     *
     * @param name
     */
    public void setViewDataForName(String name) {
        tv_name.setText(name);             // 姓名
    }

    /**
     * 关闭当前界面
     */
    public void close() {
        InterPhoneActivity.close();
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
        if (type == -1) {
            // 默认界面
            scrollView.setVisibility(View.GONE);
            tip_view.setVisibility(View.GONE);
            mRelativeLayout.setBackgroundColor(Color.argb((int) 255, 255, 255, 255));
            tvTitle.setVisibility(View.VISIBLE);
            head_left_btn.setImageResource(R.mipmap.nav_icon_back_black);
            img_other.setImageResource(R.mipmap.nav_icon_more_black);
        } else if (type == 0) {
            // 已经登录，并且有数据
            scrollView.setVisibility(View.VISIBLE);
            tip_view.setVisibility(View.GONE);
            mRelativeLayout.setBackgroundColor(Color.argb((int) 0, 255, 255, 255));
            tvTitle.setVisibility(View.GONE);
            head_left_btn.setImageResource(R.mipmap.nav_icon_back_white);
            img_other.setImageResource(R.mipmap.nav_icon_more_white);
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

    private void Dialog() {
        final View dialog1 = LayoutInflater.from(this.getActivity()).inflate(R.layout.dialog_talk_person_del, null);
        TextView tv_cancel = (TextView) dialog1.findViewById(R.id.tv_cancle);
        TextView tv_confirm = (TextView) dialog1.findViewById(R.id.tv_confirm);
        confirmDialog = new Dialog(this.getActivity(), R.style.MyDialogs);
        confirmDialog.setContentView(dialog1);
        confirmDialog.setCanceledOnTouchOutside(true);
        confirmDialog.getWindow().setBackgroundDrawableResource(R.color.transparent_background);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialogCancel();
            }
        });

        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.callOk(acc_id);
                confirmDialogCancel();
            }
        });
    }

    // 删除好友选择框
    private void DDialog() {
        final View dialog = LayoutInflater.from(this.getActivity()).inflate(R.layout.dialog_group_person_del, null);
        TextView name = (TextView) dialog.findViewById(R.id.tv_name);
        name.setText("删除好友");
        name.setOnClickListener(this);  // 拍照
        dialog.findViewById(R.id.tv_quxiao).setOnClickListener(this);   // 取消

        delDialog = new Dialog(this.getActivity(), R.style.MyDialogs);
        delDialog.setContentView(dialog);
        delDialog.setCanceledOnTouchOutside(true);

        DisplayMetrics dm = new DisplayMetrics();
        this.getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        ViewGroup.LayoutParams params = dialog.getLayoutParams();
        params.width = screenWidth;
        dialog.setLayoutParams(params);

        Window window = delDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.inOutStyle);
        window.setBackgroundDrawableResource(R.color.transparent_40_black);
    }

    // 删除好友对话框
    private void initDialogL() {
        View dialog1 = LayoutInflater.from(this.getActivity()).inflate(R.layout.dialog_talk_person_del, null);
        dialog1.findViewById(R.id.tv_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.delFriend();
                LDialog.dismiss();
            }
        }); // 确定
        dialog1.findViewById(R.id.tv_cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LDialog.dismiss();
            }
        });  // 取消
        TextView textTitle = (TextView) dialog1.findViewById(R.id.tv_title);
        textTitle.setText("确定删除该好友?");

        LDialog = new Dialog(this.getActivity(), R.style.MyDialogs);
        LDialog.setContentView(dialog1);
        LDialog.setCanceledOnTouchOutside(false);
        LDialog.getWindow().setBackgroundDrawableResource(R.color.transparent_background);
    }

    /**
     * 展示弹出框
     */
    public void confirmDialogShow() {
        confirmDialog.show();
    }

    /**
     * 取消弹出框
     */
    public void confirmDialogCancel() {
        confirmDialog.dismiss();
    }

    /**
     * 返回值设置
     *
     * @param type
     * @param name
     */
    public void setResult(boolean type, String name) {
        Listener.resultListener(type, name);
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
        void resultListener(boolean type, String name);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.destroy();
        presenter = null;
    }
}
