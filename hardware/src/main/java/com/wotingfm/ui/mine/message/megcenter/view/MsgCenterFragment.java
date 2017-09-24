package com.wotingfm.ui.mine.message.megcenter.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.wotingfm.R;
import com.wotingfm.ui.base.basefragment.BaseFragment;
import com.wotingfm.ui.mine.message.megcenter.presenter.MsgCenterPresenter;
import com.wotingfm.ui.mine.message.notify.view.MsgNotifyFragment;
import com.wotingfm.ui.mine.main.MineActivity;

/**
 * 消息中心
 * 作者：xinLong on 2017/6/5 01:30
 * 邮箱：645700751@qq.com
 */
public class MsgCenterFragment extends BaseFragment implements View.OnClickListener {
    private View rootView;
    private MsgCenterPresenter presenter;
    private TextView tv_title_notify, tv_time_notify, tv_news_notify, tv_title_group, tv_time_group, tv_news_group;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_msg_center, container, false);
            rootView.setOnClickListener(this);
            inItView();
            presenter = new MsgCenterPresenter(this);
        }
        return rootView;
    }

    // 设置界面
    private void inItView() {
        rootView.findViewById(R.id.head_left_btn).setOnClickListener(this);
        rootView.findViewById(R.id.re_notify).setOnClickListener(this);// 通知消息
        rootView.findViewById(R.id.re_group).setOnClickListener(this);// 群组消息

        // tv_title_notify=(TextView)rootView.findViewById(R.id.tv_title_notify);//
        tv_time_notify = (TextView) rootView.findViewById(R.id.tv_time_notify);//
        tv_news_notify = (TextView) rootView.findViewById(R.id.tv_news_notify);//

        // tv_title_group=(TextView)rootView.findViewById(R.id.tv_title_group);//
        tv_time_group = (TextView) rootView.findViewById(R.id.tv_time_group);//
        tv_news_group = (TextView) rootView.findViewById(R.id.tv_news_group);//

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left_btn:
                closeFragment();
                break;
            case R.id.re_notify:
                openFragment(new MsgNotifyFragment());
                break;
            case R.id.re_group:
                break;
        }
    }

    /**
     * 设置消息界面
     *
     * @param news
     * @param time
     */
    public void setViewForNotify(String news, String time) {
        if (news != null && !news.trim().equals("")) {
            tv_news_notify.setText(news);
        } else {
            tv_news_notify.setText("暂无消息");
        }
        if (time != null && !time.trim().equals("")) {
            tv_time_notify.setText(time);
        } else {
            tv_time_notify.setText("00:00");
        }
    }

    /**
     * 设置组界面
     *
     * @param news
     * @param time
     */
    public void setViewForGroup(String news, String time) {
        if (news != null && !news.trim().equals("")) {
            tv_news_group.setText(news);
        } else {
            tv_news_group.setText("暂无消息");
        }
        if (time != null && !time.trim().equals("")) {
            tv_time_group.setText(time);
        } else {
            tv_time_group.setText("00:00");
        }
    }

}
