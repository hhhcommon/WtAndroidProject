package com.wotingfm.ui.mine.set.presenter;

import android.content.Intent;
import com.woting.commonplat.config.GlobalNetWorkConfig;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.constant.BroadcastConstants;
import com.wotingfm.common.utils.CommonUtils;
import com.wotingfm.common.utils.ToastUtils;
import com.wotingfm.ui.mine.set.model.SettingModel;
import com.wotingfm.ui.mine.set.view.SettingFragment;

/**
 * 个人中心界面处理器
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class SettingPresenter {

    private final SettingFragment activity;
    private final SettingModel model;

    public SettingPresenter(SettingFragment activity) {
        this.activity = activity;
        this.model = new SettingModel();
    }

    /**
     * 判断注销按钮是否显示
     */
    public void getData() {
        if (CommonUtils.isLogin()) {
            activity.setCloseView(true);
        } else {
            activity.setCloseView(false);
        }
    }

    /**
     * 发送注销请求
     */
    public void cancel() {
        if(true){
            // 测试代码
            dealCancelSuccess();
        }else{
            // 正式代码
            if (GlobalNetWorkConfig.CURRENT_NETWORK_STATE_TYPE != -1) {
                activity.dialogShow();
                model.loadNews(new SettingModel.OnLoadInterface() {
                    @Override
                    public void onSuccess(Object o) {
                        dealCancelSuccess();
                    }

                    @Override
                    public void onFailure(String msg) {
                        dealCancelSuccess();
                    }
                });
            } else {
                ToastUtils.show_always(activity.getActivity(), "网络连接失败，请稍后再试！");
            }
        }
    }

    /**
     * 处理注销登录返回数据
     */
    private void dealCancelSuccess() {
        activity.dialogCancel();
        // 保存注销后数据
        model.cancel();
        // 隐藏注销登录按钮
        activity.setCloseView(false);
        // 发送注销登录广播通知所有界面
        activity.getActivity().sendBroadcast(new Intent(BroadcastConstants.CANCEL));
    }

}
