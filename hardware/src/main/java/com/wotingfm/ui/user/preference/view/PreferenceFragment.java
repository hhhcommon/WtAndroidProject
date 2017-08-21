package com.wotingfm.ui.user.preference.view;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wotingfm.R;
import com.wotingfm.common.utils.DialogUtils;
import com.wotingfm.ui.mine.main.MineActivity;
import com.wotingfm.ui.user.logo.LogoActivity;
import com.wotingfm.ui.user.preference.presenter.PreferencePresenter;

/**
 * 偏好设置
 * 作者：xinLong on 2017/6/4 22:16
 * 邮箱：645700751@qq.com
 */
public class PreferenceFragment extends Fragment implements View.OnClickListener {

    private TextView tv_pass, tv_enter, tv_RAWY, tv_FYKSJ, tv_ZZS, tv_XSSH, tv_TGSTXS, tv_YQQ, tv_XJ;
    private ImageView left;
    private View rootView;
    private Dialog dialog;
    private PreferencePresenter presenter;
    private String fromType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_preference, container, false);
            rootView.setOnClickListener(this);
            inItView();
            inItListener();
            presenter = new PreferencePresenter(this);
            presenter.getData();
        }
        return rootView;
    }

    // 设置界面
    private void inItView() {
        left = (ImageView) rootView.findViewById(R.id.head_left_btn);                 // 返回
        tv_pass = (TextView) rootView.findViewById(R.id.tv_pass);                     // 跳过
        tv_enter = (TextView) rootView.findViewById(R.id.tv_enter);                   // 进入应用

        tv_RAWY = (TextView) rootView.findViewById(R.id.tv_RAWY);                     // 热爱文艺
        tv_FYKSJ = (TextView) rootView.findViewById(R.id.tv_FYKSJ);                   // 放眼看世界
        tv_ZZS = (TextView) rootView.findViewById(R.id.tv_ZZS);                       // 涨知识
        tv_XSSH = (TextView) rootView.findViewById(R.id.tv_XSSH);                     // 享受生活
        tv_TGSTXS = (TextView) rootView.findViewById(R.id.tv_TGSTXS);                 // 听故事听小说
        tv_YQQ = (TextView) rootView.findViewById(R.id.tv_YQQ);                       // 有情趣
        tv_XJ = (TextView) rootView.findViewById(R.id.tv_XJ);                         // 喜剧
    }

    // 设置监听
    private void inItListener() {
        left.setOnClickListener(this);
        tv_pass.setOnClickListener(this);
        tv_enter.setOnClickListener(this);
        tv_RAWY.setOnClickListener(this);
        tv_FYKSJ.setOnClickListener(this);
        tv_ZZS.setOnClickListener(this);
        tv_XSSH.setOnClickListener(this);
        tv_TGSTXS.setOnClickListener(this);
        tv_YQQ.setOnClickListener(this);
        tv_XJ.setOnClickListener(this);
    }

    /**
     * 根据来源设置界面
     *
     * @param fromType
     */
    public void setView(String fromType) {
        this.fromType = fromType;
        if (fromType.equals("login")) {
            left.setVisibility(View.INVISIBLE);
        } else if (fromType.equals("person")) {
            tv_pass.setText("确定");
            tv_enter.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left_btn:
                close();
                break;
            case R.id.tv_pass:
                if (fromType.equals("login")) {
                    close();
                } else if (fromType.equals("person")) {
                    presenter.postData();
                }
                break;
            case R.id.tv_enter:
                presenter.postData();
                break;
            case R.id.tv_RAWY:
                presenter.setEnterBackground(1);
                presenter.setEnterBackground();
                break;
            case R.id.tv_XSSH:
                presenter.setEnterBackground(2);
                presenter.setEnterBackground();
                break;
            case R.id.tv_XJ:
                presenter.setEnterBackground(3);
                presenter.setEnterBackground();
                break;
            case R.id.tv_FYKSJ:
                presenter.setEnterBackground(4);
                presenter.setEnterBackground();
                break;
            case R.id.tv_TGSTXS:
                presenter.setEnterBackground(5);
                presenter.setEnterBackground();
                break;
            case R.id.tv_ZZS:
                presenter.setEnterBackground(6);
                presenter.setEnterBackground();
                break;
            case R.id.tv_YQQ:
                presenter.setEnterBackground(7);
                presenter.setEnterBackground();
                break;
        }
    }

    /**
     * 界面关闭
     */
    public void close() {
        if (fromType.equals("person")) {
            MineActivity.close();
        } else if (fromType.equals("login")) {
            LogoActivity.close();
            LogoActivity.closeActivity();
        }
    }

    /**
     * 设置热爱文艺样式
     */
    public void setViewForRAWY(boolean b) {
        if (!b) {
            tv_RAWY.setBackgroundResource(R.drawable.background_preference_white);
            tv_RAWY.setTextColor(this.getResources().getColor(R.color.color_preference_word));
        } else {
            tv_RAWY.setBackgroundResource(R.drawable.background_preference_green);
            tv_RAWY.setTextColor(this.getResources().getColor(R.color.white));
        }
    }

    /**
     * 设置放眼看世界样式
     */
    public void setViewForFYKSJ(boolean b) {
        if (!b) {
            tv_FYKSJ.setBackgroundResource(R.drawable.background_preference_white);
            tv_FYKSJ.setTextColor(this.getResources().getColor(R.color.color_preference_word));
        } else {
            tv_FYKSJ.setBackgroundResource(R.drawable.background_preference_light_blue);
            tv_FYKSJ.setTextColor(this.getResources().getColor(R.color.white));
        }
    }

    /**
     * 设置涨知识样式
     */
    public void setViewForZZS(boolean b) {
        if (!b) {
            tv_ZZS.setBackgroundResource(R.drawable.background_preference_white);
            tv_ZZS.setTextColor(this.getResources().getColor(R.color.color_preference_word));
        } else {
            tv_ZZS.setBackgroundResource(R.drawable.background_preference_blue);
            tv_ZZS.setTextColor(this.getResources().getColor(R.color.white));
        }
    }

    /**
     * 设置享受生活样式
     */
    public void setViewForXSSH(boolean b) {
        if (!b) {
            tv_XSSH.setBackgroundResource(R.drawable.background_preference_white);
            tv_XSSH.setTextColor(this.getResources().getColor(R.color.color_preference_word));
        } else {
            tv_XSSH.setBackgroundResource(R.drawable.background_preference_orange);
            tv_XSSH.setTextColor(this.getResources().getColor(R.color.white));
        }
    }

    /**
     * 设置听故事听小说样式
     */
    public void setViewForTGSTXS(boolean b) {
        if (!b) {
            tv_TGSTXS.setBackgroundResource(R.drawable.background_preference_white);
            tv_TGSTXS.setTextColor(this.getResources().getColor(R.color.color_preference_word));
        } else {
            tv_TGSTXS.setBackgroundResource(R.drawable.background_preference_purple);
            tv_TGSTXS.setTextColor(this.getResources().getColor(R.color.white));
        }
    }

    /**
     * 设置有情趣样式
     */
    public void setViewForYQQ(boolean b) {
        if (!b) {
            tv_YQQ.setBackgroundResource(R.drawable.background_preference_white);
            tv_YQQ.setTextColor(this.getResources().getColor(R.color.color_preference_word));
        } else {
            tv_YQQ.setBackgroundResource(R.drawable.background_preference_pink);
            tv_YQQ.setTextColor(this.getResources().getColor(R.color.white));
        }
    }

    /**
     * 设置喜剧样式
     */
    public void setViewForXJ(boolean b) {
        if (!b) {
            tv_XJ.setBackgroundResource(R.drawable.background_preference_white);
            tv_XJ.setTextColor(this.getResources().getColor(R.color.color_preference_word));
        } else {
            tv_XJ.setBackgroundResource(R.drawable.background_preference_red);
            tv_XJ.setTextColor(this.getResources().getColor(R.color.white));
        }
    }

    /**
     * 设置按钮的样式
     *
     * @param type true输入完成状态，false未输入完成状态
     */
    public void setBackground(boolean type) {
        if (type) {
            tv_enter.setBackgroundResource(R.drawable.wt_user_register_background);
        } else {
            tv_enter.setBackgroundResource(R.drawable.background_login_tvlogin_off);
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
