package com.wotingfm.ui.mine.set.view;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wotingfm.R;
import com.wotingfm.common.utils.DialogUtils;
import com.wotingfm.ui.mine.about.AboutFragment;
import com.wotingfm.ui.mine.myfavorite.view.MyFavoriteFragment;
import com.wotingfm.ui.mine.myfocus.view.MyFocusFragment;
import com.wotingfm.ui.mine.security.main.AccountSecurityFragment;
import com.wotingfm.ui.mine.feedback.view.FeedbackFragment;
import com.wotingfm.ui.mine.messageset.MessageSettingFragment;
import com.wotingfm.ui.mine.personinfo.view.PersonalInfoFragment;
import com.wotingfm.ui.mine.main.MineActivity;
import com.wotingfm.ui.mine.set.presenter.SettingPresenter;
import com.wotingfm.ui.play.activity.MeSubscribeListFragment;
import com.wotingfm.ui.user.preference.view.PreferenceFragment;

/**
 * 设置
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class SettingFragment extends Fragment implements View.OnClickListener {
    private View rootView;
    private SettingPresenter presenter;
    private Dialog dialog;
    private TextView tv_close;
    private Dialog clearCacheDialog;
    private TextView textCache;
    private LinearLayout lin_login;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_setting, container, false);
            rootView.setOnClickListener(this);
            initView();
            initEvent();
            initDialog();
            presenter = new SettingPresenter(this);
            presenter.getData();
        }
        return rootView;
    }

    // 初始化视图
    private void initView() {

        lin_login = (LinearLayout) rootView.findViewById(R.id.lin_login);//
        tv_close = (TextView) rootView.findViewById(R.id.tv_close);// 注销登录
        tv_close.setOnClickListener(this);
        TextView textTitle = (TextView) rootView.findViewById(R.id.tv_center);// 标题
        textTitle.setText(getString(R.string.setting));
        textCache = (TextView) rootView.findViewById(R.id.textCache);// 标题
    }

    // 初始化点击事件
    private void initEvent() {
        rootView.findViewById(R.id.head_left_btn).setOnClickListener(this);             // 返回

        rootView.findViewById(R.id.view_personal_info).setOnClickListener(this);        // 个人信息
        rootView.findViewById(R.id.view_my_sub).setOnClickListener(this);               // 我订阅的
        rootView.findViewById(R.id.view_my_concern).setOnClickListener(this);           // 我关注的
        rootView.findViewById(R.id.view_like).setOnClickListener(this);                 // 我喜欢的

        rootView.findViewById(R.id.view_preference_setting).setOnClickListener(this);   // 偏好设置
        rootView.findViewById(R.id.view_message_settings).setOnClickListener(this);     // 消息设置
        rootView.findViewById(R.id.view_clear_cache).setOnClickListener(this);          // 清理缓存

        rootView.findViewById(R.id.view_account_security).setOnClickListener(this);     // 账号安全
        rootView.findViewById(R.id.view_feedback).setOnClickListener(this);             // 意见反馈

        rootView.findViewById(R.id.view_about).setOnClickListener(this);                // 关于
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left_btn:// 返回
                MineActivity.close();
                break;
            case R.id.view_personal_info:// 个人信息
                MineActivity.open(new PersonalInfoFragment());
                break;
            case R.id.view_my_concern:// 我关注的
                MineActivity.open(new MyFocusFragment());
                break;
            case R.id.view_my_sub:// 我订阅的
                /**
                 * 此处跳转传递参数，根据参数关闭的时候执行MineActivity.close();
                 */
                MineActivity.open(MeSubscribeListFragment.newInstance());
                break;
            case R.id.view_like:// 我喜欢的
                MineActivity.open(new MyFavoriteFragment());
                break;
            case R.id.view_preference_setting:// 偏好设置
                PreferenceFragment fragment = new PreferenceFragment();
                Bundle bundle = new Bundle();
                bundle.putString("fromType", "person");
                fragment.setArguments(bundle);
                MineActivity.open(fragment);
                break;
            case R.id.view_message_settings:// 消息设置
                MineActivity.open(new MessageSettingFragment());
                break;
            case R.id.view_clear_cache:// 清理缓存
                clearDialogShow();
                break;
            case R.id.view_account_security:// 账号安全
                MineActivity.open(new AccountSecurityFragment());
                break;
            case R.id.view_feedback:// 意见反馈
                MineActivity.open(new FeedbackFragment());
                break;
            case R.id.view_about:// 关于
                MineActivity.open(new AboutFragment());
                break;
            case R.id.tv_close:// 注销登录
                presenter.cancel();
                break;
            case R.id.tv_confirm:           // 确定清除
                presenter.clear();
                break;
            case R.id.tv_cancle:            // 取消清除
                clearCacheDialog.dismiss();
                break;
        }
    }

    /**
     * 退出登录按钮是否显示
     *
     * @param b true 显示，false 不显示
     */
    public void setCloseView(boolean b) {
        if (b) {
            lin_login.setVisibility(View.VISIBLE);
            tv_close.setVisibility(View.VISIBLE);
        } else {
            lin_login.setVisibility(View.GONE);
            tv_close.setVisibility(View.GONE);
        }
    }

    /**
     * 设置缓存大小
     *
     * @param S
     */
    public void setCache(String S) {
        textCache.setText(S);
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

    // 初始化对话框
    private void initDialog() {
        // 清除缓存对话框
        View dialog1 = LayoutInflater.from(this.getActivity()).inflate(R.layout.dialog_talk_person_del, null);
        dialog1.findViewById(R.id.tv_confirm).setOnClickListener(this); // 清空
        dialog1.findViewById(R.id.tv_cancle).setOnClickListener(this);  // 取消
        TextView textTitle = (TextView) dialog1.findViewById(R.id.tv_title);
        textTitle.setText("是否删除本地存储缓存?");

        clearCacheDialog = new Dialog(this.getActivity(), R.style.MyDialogs);
        clearCacheDialog.setContentView(dialog1);
        clearCacheDialog.setCanceledOnTouchOutside(false);
        clearCacheDialog.getWindow().setBackgroundDrawableResource(R.color.transparent_background);
    }

    /**
     * 展示弹出框
     */
    public void clearDialogShow() {
        if (clearCacheDialog != null) clearCacheDialog.show();
    }

    /**
     * 取消弹出框
     */
    public void clearDialogCancel() {
        if (clearCacheDialog != null) clearCacheDialog.dismiss();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (rootView != null) {
            ((ViewGroup) rootView.getParent()).removeView(rootView);
        }
    }


}
