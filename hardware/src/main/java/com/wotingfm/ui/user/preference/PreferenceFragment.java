package com.wotingfm.ui.user.preference;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wotingfm.R;
import com.wotingfm.ui.main.view.MainActivity;
import com.wotingfm.ui.user.logo.LogoActivity;

/**
 * 偏好设置
 * 作者：xinLong on 2017/6/4 22:16
 * 邮箱：645700751@qq.com
 */
public class PreferenceFragment extends Fragment implements View.OnClickListener {

    private TextView tv_pass, tv_enter, tv_RAWY, tv_FYKSJ, tv_ZZS, tv_XSSH, tv_TGSTXS, tv_YQQ, tv_XJ;
    private ImageView left;

    // 偏好显示参数，false为未选中
    private boolean type1 = false;
    private boolean type2 = false;
    private boolean type3 = false;
    private boolean type4 = false;
    private boolean type5 = false;
    private boolean type6 = false;
    private boolean type7 = false;

    private String fromType;// 来源，个人中心=person,登录=login
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_preference, container, false);
            inItView();
            inItListener();
            getData();
            setView();
        }
        return rootView;
    }

    // 设置界面
    private void inItView() {
        left = (ImageView) rootView.findViewById(R.id.head_left_btn);                // 返回
        tv_pass = (TextView)  rootView.findViewById(R.id.tv_pass);                     // 跳过
        tv_enter = (TextView)  rootView.findViewById(R.id.tv_enter);                   // 进入应用

        tv_RAWY = (TextView) rootView. findViewById(R.id.tv_RAWY);                     // 热爱文艺
        tv_FYKSJ = (TextView) rootView. findViewById(R.id.tv_FYKSJ);                   // 放眼看世界
        tv_ZZS = (TextView)  rootView.findViewById(R.id.tv_ZZS);                       // 涨知识
        tv_XSSH = (TextView)  rootView.findViewById(R.id.tv_XSSH);                     // 享受生活
        tv_TGSTXS = (TextView)  rootView.findViewById(R.id.tv_TGSTXS);                 // 听故事听小说
        tv_YQQ = (TextView) rootView. findViewById(R.id.tv_YQQ);                       // 有情趣
        tv_XJ = (TextView)  rootView.findViewById(R.id.tv_XJ);                         // 喜剧

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

    // 获取界面来源
    private void getData() {
        fromType = getArguments().getString("fromType");
        if (fromType == null || fromType.trim().equals("")) fromType = "login";
    }

    // 根据来源设置界面
    private void setView() {
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
                LogoActivity.close();
                break;
            case R.id.tv_pass:
                if (fromType.equals("login")) {
                    LogoActivity.close();
                } else if (fromType.equals("person")) {
                    // 先判断有没有选中状态的数据
                    // 发送数据，待实现
                }
                break;
            case R.id.tv_enter:
                // 先判断有没有选中状态的数据
                // 发送数据，待实现
                startActivity(new Intent(getActivity(), MainActivity.class));
                break;
            case R.id.tv_RAWY:
                setBackground(1, type1);
                break;
            case R.id.tv_FYKSJ:
                setBackground(2, type2);
                break;
            case R.id.tv_ZZS:
                setBackground(3, type3);
                break;
            case R.id.tv_XSSH:
                setBackground(4, type4);
                break;
            case R.id.tv_TGSTXS:
                setBackground(5, type5);
                break;
            case R.id.tv_YQQ:
                setBackground(6, type6);
                break;
            case R.id.tv_XJ:
                setBackground(7, type7);
                break;

        }
    }

    private void setBackground(int type, boolean b) {
        if (type == 1) {
            if (b) {
                tv_RAWY.setBackgroundResource(R.drawable.background_preference_white);
                tv_RAWY.setTextColor(this.getResources().getColor(R.color.color_preference_word));
                type1 = false;
            } else {
                tv_RAWY.setBackgroundResource(R.drawable.background_preference_green);
                tv_RAWY.setTextColor(this.getResources().getColor(R.color.white));
                type1 = true;
            }
        } else if (type == 2) {
            if (b) {
                tv_FYKSJ.setBackgroundResource(R.drawable.background_preference_white);
                tv_FYKSJ.setTextColor(this.getResources().getColor(R.color.color_preference_word));
                type2 = false;
            } else {
                tv_FYKSJ.setBackgroundResource(R.drawable.background_preference_light_blue);
                tv_FYKSJ.setTextColor(this.getResources().getColor(R.color.white));
                type2 = true;
            }
        } else if (type == 3) {
            if (b) {
                tv_ZZS.setBackgroundResource(R.drawable.background_preference_white);
                tv_ZZS.setTextColor(this.getResources().getColor(R.color.color_preference_word));
                type3 = false;
            } else {
                tv_ZZS.setBackgroundResource(R.drawable.background_preference_blue);
                tv_ZZS.setTextColor(this.getResources().getColor(R.color.white));
                type3 = true;
            }
        } else if (type == 4) {
            if (b) {
                tv_XSSH.setBackgroundResource(R.drawable.background_preference_white);
                tv_XSSH.setTextColor(this.getResources().getColor(R.color.color_preference_word));
                type4 = false;
            } else {
                tv_XSSH.setBackgroundResource(R.drawable.background_preference_orange);
                tv_XSSH.setTextColor(this.getResources().getColor(R.color.white));
                type4 = true;
            }
        } else if (type == 5) {
            if (b) {
                tv_TGSTXS.setBackgroundResource(R.drawable.background_preference_white);
                tv_TGSTXS.setTextColor(this.getResources().getColor(R.color.color_preference_word));
                type5 = false;
            } else {
                tv_TGSTXS.setBackgroundResource(R.drawable.background_preference_purple);
                tv_TGSTXS.setTextColor(this.getResources().getColor(R.color.white));
                type5 = true;
            }
        } else if (type == 6) {
            if (b) {
                tv_YQQ.setBackgroundResource(R.drawable.background_preference_white);
                tv_YQQ.setTextColor(this.getResources().getColor(R.color.color_preference_word));
                type6 = false;
            } else {
                tv_YQQ.setBackgroundResource(R.drawable.background_preference_pink);
                tv_YQQ.setTextColor(this.getResources().getColor(R.color.white));
                type6 = true;
            }
        } else if (type == 7) {
            if (b) {
                tv_XJ.setBackgroundResource(R.drawable.background_preference_white);
                tv_XJ.setTextColor(this.getResources().getColor(R.color.color_preference_word));
                type7 = false;
            } else {
                tv_XJ.setBackgroundResource(R.drawable.background_preference_red);
                tv_XJ.setTextColor(this.getResources().getColor(R.color.white));
                type7 = true;
            }
        }
    }
}
