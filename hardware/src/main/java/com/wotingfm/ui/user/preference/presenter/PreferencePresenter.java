package com.wotingfm.ui.user.preference.presenter;

import com.woting.commonplat.config.GlobalNetWorkConfig;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.utils.CommonUtils;
import com.wotingfm.common.utils.ToastUtils;
import com.wotingfm.ui.user.preference.model.PreferenceModel;
import com.wotingfm.ui.user.preference.view.PreferenceFragment;

/**
 * 偏好设置的处理器
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class PreferencePresenter {

    private final PreferenceFragment activity;
    private final PreferenceModel model;
    private String fromType;// 来源，个人中心=person,登录=login

    // 偏好显示参数，false为未选中
    private boolean type1 = false;
    private boolean type2 = false;
    private boolean type3 = false;
    private boolean type4 = false;
    private boolean type5 = false;
    private boolean type6 = false;
    private boolean type7 = false;

    public PreferencePresenter(PreferenceFragment activity) {
        this.activity = activity;
        this.model = new PreferenceModel();
    }

    // 获取界面来源
    public void getData() {
        fromType = activity.getArguments().getString("fromType");
        if (fromType == null || fromType.trim().equals("")) fromType = "login";
        activity.setView(fromType);// 根据界面来源设置界面样式
    }

    /**
     * 判断此时页面展示状况
     *
     * @param type
     */
    public void setBackground(int type) {
        if (type == 1) {
            if (type1) {
                activity.setViewForRAWY(true);
                type1 = false;
            } else {
                activity.setViewForRAWY(false);
                type1 = true;
            }
        } else if (type == 2) {
            if (type2) {
                activity.setViewForFYKSJ(true);
                type2 = false;
            } else {
                activity.setViewForFYKSJ(false);
                type2 = true;
            }
        } else if (type == 3) {
            if (type3) {
                activity.setViewForZZS(true);
                type3 = false;
            } else {
                activity.setViewForZZS(false);
                type3 = true;
            }
        } else if (type == 4) {
            if (type4) {
                activity.setViewForXSSH(true);
                type4 = false;
            } else {
                activity.setViewForXSSH(false);
                type4 = true;
            }
        } else if (type == 5) {
            if (type5) {
                activity.setViewForTGSTXS(true);
                type5 = false;
            } else {
                activity.setViewForTGSTXS(false);
                type5 = true;
            }
        } else if (type == 6) {
            if (type6) {
                activity.setViewForYQQ(true);
                type6 = false;
            } else {
                activity.setViewForYQQ(false);
                type6 = true;
            }
        } else if (type == 7) {
            if (type7) {
                activity.setViewForXJ(true);
                type7 = false;
            } else {
                activity.setViewForXJ(false);
                type7 = true;
            }
        }
    }

    // 先判断有没有选中状态的数据
    // 发送数据，待实现
    public void postData() {
        // 测试代码
        if(GlobalStateConfig.test){
            activity.close();
        }else{
            // 实际代码
            if (GlobalNetWorkConfig.CURRENT_NETWORK_STATE_TYPE != -1) {
                activity.dialogShow();
                String s = "";
                model.loadNews(s, new PreferenceModel.OnLoadInterface() {
                    @Override
                    public void onSuccess(Object result) {
                        activity.dialogCancel();
                        activity.close();
                    }

                    @Override
                    public void onFailure(String msg) {
                        activity.dialogCancel();
                        activity.close();
                    }
                });
            } else {
                ToastUtils.show_always(activity.getActivity(), "网络连接失败，请稍后再试！");
            }
        }
    }

}
