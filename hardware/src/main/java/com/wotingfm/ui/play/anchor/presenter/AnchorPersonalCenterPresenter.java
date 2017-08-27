package com.wotingfm.ui.play.anchor.presenter;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.common.utils.CommonUtils;
import com.wotingfm.common.utils.L;
import com.wotingfm.common.utils.T;
import com.wotingfm.ui.bean.AnchorInfo;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;
import com.wotingfm.ui.mine.main.MineActivity;
import com.wotingfm.ui.play.anchor.model.AnchorPersonalCenterModel;
import com.wotingfm.ui.play.anchor.view.AlbumsListMeFragment;
import com.wotingfm.ui.play.anchor.view.AnchorPersonalCenterFragment;
import com.wotingfm.ui.play.find.main.view.LookListActivity;
import com.wotingfm.ui.play.main.PlayerActivity;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class AnchorPersonalCenterPresenter {

    private AnchorPersonalCenterModel model;
    private AnchorPersonalCenterFragment activity;
    private String uid;
    private AnchorInfo s;

    public AnchorPersonalCenterPresenter(AnchorPersonalCenterFragment activity) {
        this.activity = activity;
        this.model = new AnchorPersonalCenterModel();
        getData();
    }

    private void getData() {
        Bundle bundle = activity.getArguments();
        if (bundle != null) {
            uid = bundle.getString("uid");
            activity.setUid(uid);
        }
        getAnchorInfo();
    }

    /**
     * 获取主播信息
     */
    public void getAnchorInfo() {
        if (!TextUtils.isEmpty(uid)) {
            getAnchorInfo(uid);
        } else {
            activity.showErrorView();
        }
    }

    // 执行请求
    private void getAnchorInfo(String uid) {
        activity.showLoadingView();
        model.getAnchorInfo(uid, new AnchorPersonalCenterModel.OnLoadInterface() {
            @Override
            public void onSuccess(Object o) {
                delGet(o);
            }

            @Override
            public void onFailure(String msg) {
                activity.showErrorView();
            }
        });
    }

    /**
     * 返回数据后的数据处理
     *
     * @param o
     */
    private void delGet(Object o) {
        s = (AnchorInfo) o;
        if (s == null) {
            activity.showErrorView();
        } else {
            if (s.data != null && s.data.user != null) {
                activity.setHeadViewData(s);
                activity.setData(s.data.user.data);
                activity.showContentView();
            } else {
                activity.showErrorView();
            }
        }
    }

    /**
     * 点击关注按钮
     */
    public void follow() {
        if (s != null) {
            try {
                if (s.data.user.had_followd) {
                    unFollowAnchor(CommonUtils.getUserId(), s);
                } else {
                    followAnchor(CommonUtils.getUserId(), s);
                }
            } catch (Exception e) {
                Log.e("主播详情页---关注、取消关注","数据出错");
                e.printStackTrace();
            }
        }
    }

    // 关注主播
    private void followAnchor(String uid, final AnchorInfo sw) {
        L.e("关注主播==", "uid=" + uid + ":" + sw.data.user.id);
        activity.dialogShow();
        model.followAnchor(uid,sw, new AnchorPersonalCenterModel.OnLoadInterface() {
            @Override
            public void onSuccess(Object o) {
                delFollow(o);
            }

            @Override
            public void onFailure(String msg) {
                activity.dialogCancel();
                T.getInstance().showToast("关注失败");
            }
        });
    }

    private void delFollow(Object o){
        AnchorInfo sw=(AnchorInfo) o;
        sw.data.user.had_followd = true;
        sw.data.user.idols_count = sw.data.user.idols_count + 1;
        activity.setHeadViewData(sw);
        activity.dialogCancel();
        T.getInstance().showToast("关注成功");
    }

    // 取消关注主播
    private void unFollowAnchor(String uid,  AnchorInfo sw) {
        L.e("取消关注主播==", "uid=" + uid + ":" + sw.data.user.id);
        activity.dialogShow();
        model.unFollowAnchor(uid,sw, new AnchorPersonalCenterModel.OnLoadInterface() {
            @Override
            public void onSuccess(Object o) {
                delUnFollow(o);
            }

            @Override
            public void onFailure(String msg) {
                activity.dialogCancel();
                T.getInstance().showToast("取消关注失败");
            }
        });
    }

    private void delUnFollow(Object o){
        AnchorInfo sw=(AnchorInfo) o;
        sw.data.user.had_followd = false;
        sw.data.user.idols_count = sw.data.user.idols_count - 1;
        activity.setHeadViewData(sw);
        activity.dialogCancel();
        T.getInstance().showToast("取消关注成功");
    }

    /**
     * 关闭界面
     */
    public void close() {
        if (activity.getActivity() instanceof PlayerActivity) {
            PlayerActivity.close();
        } else if (activity.getActivity() instanceof MineActivity) {
            MineActivity.close();
        } else if (activity.getActivity() instanceof InterPhoneActivity) {
            InterPhoneActivity.close();
        } else if (activity.getActivity() instanceof LookListActivity) {
            LookListActivity.close();
        }
    }

    /**
     * 关闭界面
     * @param fragment
     */
    public void openFragment(AlbumsListMeFragment fragment) {
        if (activity.getActivity() instanceof PlayerActivity) {
            PlayerActivity.open(fragment);
        } else if (activity.getActivity() instanceof MineActivity) {
            MineActivity.open(fragment);
        } else if (activity.getActivity() instanceof InterPhoneActivity) {
            InterPhoneActivity.open(fragment);
        } else if (activity.getActivity() instanceof LookListActivity) {
            LookListActivity.open(fragment);
        }
    }
    /**
     * 数据销毁
     */
    public void destroy() {
        model = null;
    }

}
