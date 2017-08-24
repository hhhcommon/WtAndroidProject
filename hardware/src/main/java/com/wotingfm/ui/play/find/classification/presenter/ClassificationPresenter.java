package com.wotingfm.ui.play.find.classification.presenter;

import com.wotingfm.ui.play.find.classification.model.Classification;
import com.wotingfm.ui.play.find.classification.model.ClassificationModel;
import com.wotingfm.ui.play.find.classification.view.ClassificationFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class ClassificationPresenter {

    private ClassificationFragment activity;
    private ClassificationModel model;
    List<Classification.DataBeanX> commons =new ArrayList<>();

    public ClassificationPresenter(ClassificationFragment activity) {
        this.activity = activity;
        this.model = new ClassificationModel();
        setData();
        refresh();
    }

    private void setData(){
        activity.setData(commons);
    }

    // 发送网络请求
    public void refresh() {
        model.loadNews(new ClassificationModel.OnLoadInterface() {
            @Override
            public void onSuccess(List<Classification.DataBeanX>  o) {
                activity.refreshCancel();
                dealLoginSuccess(o);
            }

            @Override
            public void onFailure(String msg) {
                activity.refreshCancel();
                activity.showErrorView();
            }
        });
    }

    // 处理返回数据
    private void dealLoginSuccess(List<Classification.DataBeanX>   datas) {
        try {
            if (datas != null && !datas.isEmpty()) {
                commons.clear();
                commons.addAll(datas);
                activity.setData(commons);
                activity.showContentView();
            } else {
                activity.showEmptyView();
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 设置数据出错界面
            activity.showErrorView();
        }
    }

    /**
     * 数据销毁
     */
    public void destroy() {
        model = null;
    }

}
