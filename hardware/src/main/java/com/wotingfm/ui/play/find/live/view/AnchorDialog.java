package com.wotingfm.ui.play.find.live.view;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.woting.commonplat.manager.PhoneMsgManager;
import com.woting.commonplat.widget.GlideCircleTransform;
import com.wotingfm.R;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.constant.StringConstant;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.common.utils.T;
import com.wotingfm.ui.bean.AnchorInfo;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;
import com.wotingfm.ui.mine.main.MineActivity;
import com.wotingfm.ui.play.anchor.view.AnchorPersonalCenterFragment;
import com.wotingfm.ui.play.find.main.view.LookListActivity;
import com.wotingfm.ui.play.main.PlayerActivity;
import com.wotingfm.ui.play.report.view.ReportFragment;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 为测试，此页面中暂时没有判断登录
 */
public class AnchorDialog extends Dialog implements View.OnClickListener {

    private Context context;
    private ImageView img_close, img_head;
    private TextView tv_report, tv_focus, tv_find,tv_name,tv_focusNews,tv_fansNews;
    private AnchorInfo anchor;
    private boolean focus;

    public AnchorDialog(Context contexts) {
        super(contexts,R.style.MyDialogs);
        context = contexts;
        setContentView(R.layout.anchor_dialog);
        getWindow().setBackgroundDrawableResource(R.color.transparent_40_black);
        getWindow().setLayout(PhoneMsgManager.ScreenWidth-80, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.CENTER);
        setCanceledOnTouchOutside(true);
        inItView();
        inItListener();
    }

    private void inItView() {
        img_close = (ImageView) findViewById(R.id.img_close);
        tv_report = (TextView) findViewById(R.id.tv_report);
        tv_focus = (TextView) findViewById(R.id.tv_focus);
        tv_find = (TextView) findViewById(R.id.tv_find);
        img_head = (ImageView) findViewById(R.id.img_head);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_focusNews = (TextView) findViewById(R.id.tv_focusNews);
        tv_fansNews = (TextView) findViewById(R.id.tv_fansNews);
    }

    private void inItListener() {
        img_close.setOnClickListener(this);
        tv_report.setOnClickListener(this);
        tv_focus.setOnClickListener(this);
        tv_find.setOnClickListener(this);
    }


    /**
     * 展示dialog
     */
    public void setMenuData(AnchorInfo anchors) {
        anchor = anchors;
        Glide.with(BSApplication.getInstance()).load(anchors.data.user.avatar)// Glide
                .transform(new GlideCircleTransform(BSApplication.getInstance()))
                .error(R.mipmap.oval_defut_photo)
                .placeholder(R.mipmap.oval_defut_photo)
                .into(img_head);
        tv_name .setText(anchors.data.user.name);
        tv_focusNews .setText("关注："+anchors.data.user.idols_count);
        tv_fansNews .setText("粉丝："+anchors.data.user.fans_count);
        focus=anchors.data.user.had_followd;
        isFollow(focus);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_close:
                dismiss();
                break;
            case R.id.tv_report:
                if (anchor != null)
                    openFragment(ReportFragment.newInstance(anchor.data.user.id, "REPORT_SINGLE"));
                dismiss();
                break;
            case R.id.tv_focus:
                if(anchor != null){
                    if(focus){
                        String id=BSApplication.SharedPreferences.getString(StringConstant.USER_ID,"");
                        unFollowAnchor(id,anchor);
                    }else{
                        String id=BSApplication.SharedPreferences.getString(StringConstant.USER_ID,"");
                        followAnchor(id,anchor);
                    }
                }
                break;
            case R.id.tv_find:
                if (anchor != null)
                    openFragment(AnchorPersonalCenterFragment.newInstance(anchor.data.user.id));
                dismiss();
                break;
        }
    }

    private void followAnchor(String uid, final AnchorInfo sw) {
        RetrofitUtils.getInstance().followAnchor(uid, sw.data.user.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object Object) {
                        sw.data.user.fans_count = sw.data.user.fans_count + 1;
                        tv_fansNews.setText("粉丝  " + sw.data.user.fans_count);
                        focus= true;
                        isFollow(true);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        T.getInstance().showToast("关注失败");
                    }
                });
    }

    private void unFollowAnchor(String uid, final AnchorInfo sw) {
        RetrofitUtils.getInstance().unFollowAnchor(uid, sw.data.user.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object s) {
                        sw.data.user.fans_count = sw.data.user.fans_count - 1;
                        tv_fansNews.setText("粉丝  " + sw.data.user.fans_count);
                        focus= false;
                        isFollow(false);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        T.getInstance().showToast("取消关注失败");
                    }
                });
    }

    public void openFragment(Fragment fragment) {
        if (context instanceof PlayerActivity) {
            PlayerActivity playerActivity = (PlayerActivity) context;
            playerActivity.open(fragment);
        } else if (context instanceof MineActivity) {
            MineActivity mineActivity = (MineActivity) context;
            mineActivity.open(fragment);
        } else if (context instanceof InterPhoneActivity) {
            InterPhoneActivity interPhoneActivity = (InterPhoneActivity) context;
            interPhoneActivity.open(fragment);
        } else if (context instanceof LookListActivity) {
            LookListActivity lookListActivity = (LookListActivity) context;
            lookListActivity.open(fragment);
        }else if (context instanceof LiveRoomActivity) {
            LiveRoomActivity liveRoomActivity = (LiveRoomActivity) context;
            liveRoomActivity.open(fragment);
        }
    }

    private void isFollow(boolean isFollow) {
        if (isFollow == true) {
            tv_focus.setText("已关注");
        } else {
            tv_focus.setText("关注");
        }
    }
}
