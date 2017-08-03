package com.wotingfm.ui.mine.fm.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.wotingfm.R;
import com.wotingfm.ui.mine.fm.presenter.FMSetPresenter;
import com.wotingfm.ui.mine.main.MineActivity;
import com.wotingfm.ui.mine.fm.adapter.FMListAdapter;
import com.wotingfm.ui.mine.fm.model.FMInfo;

import java.util.List;

/**
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class FMSetFragment extends Fragment implements View.OnClickListener {
    private View rootView;
    private ListView fmList;
    private FMListAdapter adapter;
    private FMSetPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_fm_set, container, false);
            rootView.setOnClickListener(this);
            initView();
            presenter = new FMSetPresenter(this);
            presenter.setView();
        }
        return rootView;
    }

    // 初始化视图
    private void initView() {
        rootView.findViewById(R.id.head_left_btn).setOnClickListener(this);// 返回
        TextView textTitle = (TextView) rootView.findViewById(R.id.tv_center);// 标题
        textTitle.setText(getString(R.string.fm_settings));

        View headView = LayoutInflater.from(getActivity()).inflate(R.layout.head_view_fm, null);
        fmList = (ListView) rootView.findViewById(R.id.fm_list_view);// 推荐的 FM 列表
        fmList.addHeaderView(headView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left_btn:// 返回
                MineActivity.close();
                break;
        }
    }

    /**
     * 数据适配
     *
     * @param list
     */
    public void setViewData(List<FMInfo> list) {
        if (adapter == null) {
            adapter = new FMListAdapter(getActivity(), list);
            fmList.setAdapter(adapter);
        } else {
            adapter.changeData(list);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.destroy();
        presenter = null;
    }
}
