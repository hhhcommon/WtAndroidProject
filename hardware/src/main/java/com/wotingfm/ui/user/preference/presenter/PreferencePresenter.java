package com.wotingfm.ui.user.preference.presenter;

import android.util.Log;

import com.google.gson.GsonBuilder;
import com.woting.commonplat.config.GlobalNetWorkConfig;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.utils.CommonUtils;
import com.wotingfm.common.utils.ToastUtils;
import com.wotingfm.ui.user.preference.model.PreferenceModel;
import com.wotingfm.ui.user.preference.view.PreferenceFragment;

import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * 偏好设置的处理器
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class PreferencePresenter {

    private PreferenceFragment activity;
    private PreferenceModel model;
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
        if (fromType.equals("person")) getPreference();// 如果从个人中心跳转过来的需要获取自己的偏好设置
    }

    private void getPreference() {
        if (GlobalNetWorkConfig.CURRENT_NETWORK_STATE_TYPE != -1) {
            activity.dialogShow();
            model.getNews(new PreferenceModel.OnLoadInterface() {
                @Override
                public void onSuccess(Object o) {
                    activity.dialogCancel();
                    dealSuccess(o);
                }

                @Override
                public void onFailure(String msg) {
                    activity.dialogCancel();
                }
            });
        }
    }

    // 处理自己的偏好数据
    private void dealSuccess(Object o) {
        try {
            String s = new GsonBuilder().serializeNulls().create().toJson(o);
            JSONObject js = new JSONObject(s);
            int ret = js.getInt("ret");
            Log.e("ret", String.valueOf(ret));
            if (ret == 0) {
                String msg = js.getString("data");
                JSONTokener jsonParser = new JSONTokener(msg);
                JSONObject arg1 = (JSONObject) jsonParser.nextValue();
                String code = arg1.getString("interest_id");
                assemblyData(code);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 拆解数据
    private void assemblyData(String code) {
        if (code != null && !code.trim().equals("")) {
            if (code.contains(",")) {
                String[] strArray = code.split(","); //拆分字符为"," ,然后把结果交给数组strArray
                if (strArray.length > 0) {
                    for (int i = 0; i < strArray.length; i++) {
                        setPView(strArray[i]);
                    }
                }
            } else {
                setPView(code);
            }
        }
    }

    // 设置自己的偏好选择
    private void setPView(String s) {
        switch (s) {
            case "1":
                activity.setViewForRAWY(true);
                type1 = true;
                break;
            case "2":
                activity.setViewForXSSH(true);
                type2 = true;
                break;
            case "3":
                activity.setViewForXJ(true);
                type3 = true;
                break;
            case "4":
                activity.setViewForFYKSJ(true);
                type4 = true;
                break;
            case "5":
                activity.setViewForTGSTXS(true);
                type5 = true;
                break;
            case "6":
                activity.setViewForZZS(true);
                type6 = true;
                break;
            case "7":
                activity.setViewForYQQ(true);
                type7 = true;
                break;
        }
    }

    /**
     * 判断此时页面展示状况
     *
     * @param type
     */
    public void setBackground(int type) {
        switch (type) {
            case 1:
                if (type1) {
                    activity.setViewForRAWY(false);
                    type1 = false;
                } else {
                    activity.setViewForRAWY(true);
                    type1 = true;
                }
                break;
            case 2:
                if (type2) {
                    activity.setViewForXSSH(false);
                    type2 = false;
                } else {
                    activity.setViewForXSSH(true);
                    type2 = true;
                }
                break;
            case 3:
                if (type3) {
                    activity.setViewForXJ(false);
                    type3 = false;
                } else {
                    activity.setViewForXJ(true);
                    type3 = true;
                }
                break;
            case 4:
                if (type4) {
                    activity.setViewForFYKSJ(false);
                    type4 = false;
                } else {
                    activity.setViewForFYKSJ(true);
                    type4 = true;
                }
                break;
            case 5:
                if (type5) {
                    activity.setViewForTGSTXS(false);
                    type5 = false;
                } else {
                    activity.setViewForTGSTXS(true);
                    type5 = true;
                }
                break;
            case 6:
                if (type6) {
                    activity.setViewForZZS(false);
                    type6 = false;
                } else {
                    activity.setViewForZZS(true);
                    type6 = true;
                }
                break;
            case 7:
                if (type7) {
                    activity.setViewForYQQ(false);
                    type7 = false;
                } else {
                    activity.setViewForYQQ(true);
                    type7 = true;
                }
                break;
        }
    }

    // 发送数据
    public void postData() {
        // 测试代码
        if (GlobalStateConfig.test) {
            activity.close();
        } else {
            // 实际代码
            if (GlobalNetWorkConfig.CURRENT_NETWORK_STATE_TYPE != -1) {
                String s = getS();
                if (s!=null&&!s.equals("")) {
                    activity.dialogShow();
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
                    activity.close();
                }
            } else {
                ToastUtils.show_always(activity.getActivity(), "网络连接失败，请稍后再试！");
            }
        }
    }

    // 组装传输数据
    private String getS() {
        String s = "";
        if (type1) {// 热爱文艺
            s = "1,";
        }
        if (type2) {// 享受生活
            s = s + "2,";
        }
        if (type3) {// 喜剧
            s = s + "3,";
        }
        if (type4) {// 放眼看世界
            s = s + "4,";
        }
        if (type5) {// 听故事听小说
            s = s + "5,";
        }
        if (type6) {// 涨知识
            s = s + "6,";
        }
        if (type7) {// 有情趣
            s = s + "7,";
        }
        // 去掉最后一个逗号
        if (s.length() > 0) {
            s = s.substring(0, s.length() - 1);
        }
//        ToastUtils.show_always(activity.getActivity(), s);
        return s;
    }

    /**
     * 数据销毁
     */
    public void destroy() {
        model = null;
    }
}
