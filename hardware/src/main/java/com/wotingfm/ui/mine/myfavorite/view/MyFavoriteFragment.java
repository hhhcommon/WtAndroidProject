package com.wotingfm.ui.mine.myfavorite.view;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.woting.commonplat.widget.TipView;
import com.wotingfm.R;
import com.wotingfm.common.utils.DialogUtils;
import com.wotingfm.ui.base.basefragment.BaseFragment;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;
import com.wotingfm.ui.mine.main.MineActivity;
import com.wotingfm.ui.mine.myfavorite.adapter.MyFavoriteAdapter;
import com.wotingfm.ui.mine.myfavorite.model.Favorite;
import com.wotingfm.ui.mine.myfavorite.presenter.MyFavoritePresenter;
import com.wotingfm.ui.mine.myfocus.adapter.MyFocusAdapter;
import com.wotingfm.ui.mine.myfocus.model.Focus;
import com.wotingfm.ui.mine.myfocus.presenter.MyFocusPresenter;

import java.util.List;

/**
 * 删除组成员
 * 作者：xinLong on 2017/6/5 01:30
 * 邮箱：645700751@qq.com
 */
public class MyFavoriteFragment extends BaseFragment implements View.OnClickListener {
    private View rootView;
    private ListView lv_favorite;
    private MyFavoritePresenter presenter;
    private MyFavoriteAdapter mAdapter;
    private Dialog dialog;
    private TipView tip_view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_myfavorite, container, false);
            rootView.setOnClickListener(this);
            inItView();
            presenter = new MyFavoritePresenter(this);
            presenter.getData();
        }
        return rootView;
    }


    // 初始化界面
    private void inItView() {
        tip_view = (TipView) rootView.findViewById(R.id.tip_view);// 提示界面
        rootView.findViewById(R.id.head_left_btn).setOnClickListener(this);
        lv_favorite = (ListView) rootView.findViewById(R.id.lv_favorite);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left_btn:
                closeFragment();
                break;
        }
    }

    // 适配数据
    public void updateUI(final List<Favorite> list) {
        if (mAdapter == null) {
            mAdapter = new MyFavoriteAdapter(this.getActivity(), list);
            lv_favorite.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
        lv_favorite.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                presenter.onItemClick(list, position);
            }
        });
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

    /**
     * 是否登录，是否有数据
     *
     * @param type 登录后数据类型
     *             0 正常有数据
     *             NO_DATA,没有数据 1
     *             NO_NET,没有网络 2
     *             NO_LOGIN,没有登录 3
     *             IS_ERROR,加载错误 4
     */
    public void isLoginView(int type) {
        if (type == 0) {
            // 已经登录，并且有数据
            lv_favorite.setVisibility(View.VISIBLE);
            tip_view.setVisibility(View.GONE);
        } else if (type == 1) {
            // 已经登录，没有数据
            lv_favorite.setVisibility(View.GONE);
            tip_view.setVisibility(View.VISIBLE);
            tip_view.setTipView(TipView.TipStatus.NO_DATA);
        } else if (type == 2) {
            // 没有网络
            lv_favorite.setVisibility(View.GONE);
            tip_view.setVisibility(View.VISIBLE);
            tip_view.setTipView(TipView.TipStatus.NO_NET);
        } else if (type == 3) {
            // 没有登录
            lv_favorite.setVisibility(View.GONE);
            tip_view.setVisibility(View.VISIBLE);
            tip_view.setTipView(TipView.TipStatus.NO_LOGIN);
        } else if (type == 4) {
            // 已经登录，数据加载失败
            lv_favorite.setVisibility(View.GONE);
            tip_view.setVisibility(View.VISIBLE);
            tip_view.setTipView(TipView.TipStatus.IS_ERROR);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.destroy();
        presenter = null;
    }
}
