package com.wotingfm.ui.intercom.group.setmanager.presenter;

import android.util.Log;
import com.google.gson.Gson;
import com.woting.commonplat.config.GlobalNetWorkConfig;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.utils.ToastUtils;
import com.wotingfm.ui.intercom.group.setmanager.view.SetManagerFragment;
import com.wotingfm.ui.intercom.group.setmanager.model.SetManagerModel;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;
import org.json.JSONObject;
import java.util.List;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class SetManagerPresenter {

    private final SetManagerFragment activity;
    private final SetManagerModel model;
    private List<Contact.user> list;
    private String gId;// 组Id

    public SetManagerPresenter(SetManagerFragment activity) {
        this.activity = activity;
        this.model = new SetManagerModel();
    }

    /**
     * 获取展示数据
     */
    public void getData() {
        if (GlobalStateConfig.test) {
            // 测试数据
            list = model.getData();
        } else {
            // 实际数据
            list = getList();
        }
        if(list!=null&&list.size()>0){
            activity.setView(list);
            activity.isLoginView(0);
        }else{
            activity.isLoginView(1);
        }
    }

    private List<Contact.user> getList() {
        // 从来源界面传递群成员列表数据
        try {
            gId = activity.getArguments().getString("gid");// 群id
        } catch (Exception e) {
            e.printStackTrace();
        }
        String id = null;// 群创建者id
        try {
            id = activity.getArguments().getString("id");
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<Contact.user> list = null;// 群成员列表
        try {
            list = (List<Contact.user>) activity.getArguments().getSerializable("list");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(list!=null&&list.size()>0){
            List<Contact.user> _list = model.assemblyData(list, id);
            return _list;
        }else{
            return null;
        }


    }

    /**
     * 点击按钮的数据更改
     *
     * @param position
     */
    public void changeData(int position) {
        int type = list.get(position).getType();
        if (type == 3) {
            list.get(position).setType(2);
        } else {
            list.get(position).setType(3);
        }
        activity.setView(list);
    }

    /**
     * 发送申请
     */
    public void send() {
        if (GlobalNetWorkConfig.CURRENT_NETWORK_STATE_TYPE != -1) {
            String s = null;
            if(list!=null&&list.size()>0){
                 s = model.getString(list);
            }
            if (s != null && !s.equals("")) {
                activity.dialogShow();
                model.loadNews(gId, s, new SetManagerModel.OnLoadInterface() {
                    @Override
                    public void onSuccess(Object o) {
                        activity.dialogCancel();
                        dealSuccess(o);
                    }

                    @Override
                    public void onFailure(String msg) {
                        activity.dialogCancel();
                        ToastUtils.show_always(activity.getActivity(), "设置管理员失败，请稍后再试！");
                    }
                });
            } else {
                ToastUtils.show_always(activity.getActivity(), "提交数据不能为空");
            }
        } else {
            ToastUtils.show_always(activity.getActivity(), "网络连接失败，请稍后再试！");
        }

    }

    // 处理返回的数据
    private void dealSuccess(Object o) {
        try {
            String s = new Gson().toJson(o);
            JSONObject js = new JSONObject(s);
            int ret = js.getInt("ret");
            Log.e("设置管理员==ret", String.valueOf(ret));
            if (ret == 0) {
                InterPhoneActivity.close();
                ToastUtils.show_always(activity.getActivity(), "设置成功");
            } else {
                ToastUtils.show_always(activity.getActivity(), "设置管理员失败，请稍后再试！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 设置数据出错界面
            ToastUtils.show_always(activity.getActivity(), "设置管理员失败，请稍后再试！");
        }
    }


}
