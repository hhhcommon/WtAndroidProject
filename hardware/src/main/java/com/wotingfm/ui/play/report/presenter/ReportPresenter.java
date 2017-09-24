package com.wotingfm.ui.play.report.presenter;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import com.wotingfm.common.utils.T;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;
import com.wotingfm.ui.mine.main.MineActivity;
import com.wotingfm.ui.play.find.main.view.LookListActivity;
import com.wotingfm.ui.play.main.PlayerActivity;
import com.wotingfm.ui.play.report.model.ReportModel;
import com.wotingfm.ui.play.report.model.Reports;
import com.wotingfm.ui.play.report.view.ReportFragment;

import java.util.List;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class ReportPresenter {

    private ReportModel model;
    private ReportFragment activity;
    private List<Reports.DataBean.Reasons> albumsBeen;
    private String playerId;
    private String type;
    private Reports.DataBean.Reasons reasonsBase;

    public ReportPresenter(ReportFragment activity) {
        this.activity = activity;
        this.model = new ReportModel();
        activity.showLoadingView();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getData();
            }
        },300);
    }

    private void getData() {
        Bundle bundle = activity.getArguments();
        if (bundle != null) {
            type = bundle.getString("type");
            playerId = bundle.getString("playerId");
            getReport();
        } else {
            activity.showErrorView();
        }
    }

    /**
     * 获取举报列表
     */
    public void getReport() {
        if (!TextUtils.isEmpty(type)) {
            getPlayerReports(type);
        }else{
            activity.showErrorView();
        }
    }

    /**
     * 选中的类型
     *
     * @param reasons
     */
    public void select(Reports.DataBean.Reasons reasons) {
        reasonsBase = reasons;
    }

    /**
     * 提交举报内容
     *
     * @param news
     */
    public void send(String news) {
        String report_reason = null;
        if (reasonsBase != null){
            report_reason = reasonsBase.title;
        }
        if (TextUtils.isEmpty(report_reason) && TextUtils.isEmpty(news)) {
            T.getInstance().showToast("请选择举报原因");
            return;
        }
        if ("REPORT_ALBUM".equals(type)) {
            reportsPlayer(report_reason, news);
        } else if ("REPORT_USER".equals(type)) {
            reportsPersonal(report_reason, news);
        }
    }

    /**
     * 获取数据
     *
     * @param type
     */
    private void getPlayerReports(String type) {
        model.getReport(type, new ReportModel.OnLoadInterface() {
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
        albumsBeen = (List<Reports.DataBean.Reasons>) o;
        if (albumsBeen != null && !albumsBeen.isEmpty()) {
            activity.showContentView();
            activity.setData(albumsBeen);
        } else {
            activity.showEmptyView();
        }
    }

    //举报节目
    public void reportsPlayer(String report_reason, String content) {
        model.reportsPlayer(playerId, report_reason, content, new ReportModel.OnLoadInterface() {
            @Override
            public void onSuccess(Object o) {
                T.getInstance().showToast("举报成功");
                activity.closeFragment();
            }

            @Override
            public void onFailure(String msg) {
                T.getInstance().showToast("举报失败");
            }
        });
    }

    //举报个人
    public void reportsPersonal(String report_reason, String content) {
        model.reportsPersonal(playerId, report_reason, content, new ReportModel.OnLoadInterface() {
            @Override
            public void onSuccess(Object o) {
                T.getInstance().showToast("举报成功");
                activity.closeFragment();
            }

            @Override
            public void onFailure(String msg) {
                T.getInstance().showToast("举报失败");
            }
        });
    }

    /**
     * 数据销毁
     */
    public void destroy() {
        model = null;
    }

}
