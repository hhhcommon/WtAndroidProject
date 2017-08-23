package com.woting.ui.mine.main.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.woting.R;
import com.woting.common.utils.CommonUtils;
import com.woting.common.utils.GlideUtils;
import com.woting.ui.mine.main.presenter.MinePresenter;
import com.woting.ui.mine.message.notify.view.MsgNotifyFragment;
import com.woting.ui.mine.personinfo.view.PersonalInfoFragment;
import com.woting.ui.mine.set.view.SettingFragment;
import com.woting.ui.play.look.activity.LookListActivity;
import com.woting.ui.user.logo.LogoActivity;


/**
 * 个人中心主界面
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class MineFragment extends Fragment implements View.OnClickListener {

    private View rootView;
    private MinePresenter presenter;
    private ImageView image_avatar;
    private TextView text_user_name, text_user_number, tv_follow, tv_fans;

    public static MineFragment newInstance() {
        MineFragment fragment = new MineFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_mine, container, false);
            rootView.setOnClickListener(this);
            initView();
            initEvent();
            presenter = new MinePresenter(this);
            presenter.getData();
        }
        return rootView;
    }

    // 初始化视图
    private void initView() {
        image_avatar = (ImageView) rootView.findViewById(R.id.image_avatar);// 头像
        text_user_name = (TextView) rootView.findViewById(R.id.tv_name);     // 昵称
        text_user_number = (TextView) rootView.findViewById(R.id.tv_num);    // id号
        tv_follow = (TextView) rootView.findViewById(R.id.tv_follow);        // 关注
        tv_fans = (TextView) rootView.findViewById(R.id.tv_fans);            // 粉丝
    }

    // 初始化点击事件
    private void initEvent() {
        rootView.findViewById(R.id.lin_head).setOnClickListener(this);     // 个人中心
        rootView.findViewById(R.id.lin_follow).setOnClickListener(this);   // 关注
        rootView.findViewById(R.id.lin_fans).setOnClickListener(this);     // 粉丝
        rootView.findViewById(R.id.re_sub).setOnClickListener(this);       // 我的订阅
        rootView.findViewById(R.id.re_download).setOnClickListener(this);  // 我的下载
        rootView.findViewById(R.id.re_favorite).setOnClickListener(this);  // 我喜欢的
        rootView.findViewById(R.id.re_sing).setOnClickListener(this);      // 我的听单
        rootView.findViewById(R.id.re_recent).setOnClickListener(this);    // 最近收听、
        rootView.findViewById(R.id.re_msg).setOnClickListener(this);        // 消息中心
        rootView.findViewById(R.id.re_anchor).setOnClickListener(this);    // 我要当主播
        rootView.findViewById(R.id.re_set).setOnClickListener(this);        // 设置
    }

    /**
     * 设置界面数据(登录)
     *
     * @param url
     * @param name
     * @param num
     */
    public void setViewForLogin(String url, String name, String num, String follow, String fans) {
        if (url != null && !url.trim().equals("") && url.startsWith("http")) {
            GlideUtils.loadImageViewRound(url, image_avatar, 150, 150);
        } else {
            GlideUtils.loadImageViewRound(R.mipmap.icon_avatar_d, image_avatar, 70, 70);
        }
        text_user_name.setText(name);
        text_user_number.setText("我听号：" + num);
        tv_follow.setText(follow);
        tv_fans.setText(fans);
        /**
         * 其他待展示
         */
    }

    /**
     * 设置界面数据(未登录)
     */
    public void setView() {
        GlideUtils.loadImageViewSrc(R.mipmap.icon_avatar_d, image_avatar, false, 8);
        text_user_name.setText("点击登录");
        text_user_number.setText("登录后可享受更多服务");
        /**
         * 其他待隐藏
         */
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lin_head:// 登录
                if (!CommonUtils.isLogin()) {
                    startActivity(new Intent(this.getActivity(), LogoActivity.class));
                } else {
                    LookListActivity.open(new PersonalInfoFragment());
                }
                break;
            case R.id.lin_follow:// 关注
                break;
            case R.id.lin_fans:// 粉丝
                break;
            case R.id.re_sub:// 我的订阅
                break;
            case R.id.re_download:// 我的下载
                break;
            case R.id.re_favorite:// 我喜欢的
                break;
            case R.id.re_sing:// 我的听单
                break;
            case R.id.re_recent:// 最近收听
                break;
            case R.id.re_anchor:// 我要当主播
                break;
            case R.id.re_set:// 设置
                LookListActivity.open(new SettingFragment());
                break;
            case R.id.re_msg:// 消息中心
                if (CommonUtils.isLogin()) LookListActivity.open(new MsgNotifyFragment());
                break;
//            case R.id.image_qr_code:// 二维码
//                if (CommonUtils.isLogin()) {
//                    EWMShowFragment fragment = new EWMShowFragment();
//                    Bundle bundle = new Bundle();
//                    bundle.putString("from", "person");// 路径来源
//                    bundle.putString("image", BSApplication.SharedPreferences.getString(StringConstant.PORTRAIT, ""));// 头像
//                    bundle.putString("news", BSApplication.SharedPreferences.getString(StringConstant.USER_SIGN, ""));// 简介
//                    bundle.putString("name", BSApplication.SharedPreferences.getString(StringConstant.NICK_NAME, ""));// 姓名
//
//                    Map<String, Object> map = new HashMap<>();
//                    map.put("type", "person");
//                    map.put("id", BSApplication.SharedPreferences.getString(StringConstant.USER_ID, ""));
//                    String url = JsonEncloseUtils.jsonEnclose(map).toString();
//
//                    bundle.putString("uri", url);// 内容路径
//                    fragment.setArguments(bundle);
//                    LookListActivity.open(fragment);
//                }
//                break;

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter = null;
    }
}
