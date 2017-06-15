package com.wotingfm.ui.mine.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.wotingfm.R;
import com.wotingfm.ui.mine.MineActivity;
import com.wotingfm.ui.mine.adapter.FMListAdapter;
import com.wotingfm.ui.mine.model.FMInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * FM 设置
 * Created by Administrator on 2017/6/9.
 */
public class FMSetFragment extends Fragment implements View.OnClickListener {
    private View rootView;
    private ListView fmList;

//    private FMListAdapter adapter;
    private List<FMInfo> list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_fm_set, container, false);
            rootView.setOnClickListener(this);

            initView();
            initEvent();
        }
        return rootView;
    }

    // 初始化视图
    private void initView() {
        TextView textTitle = (TextView) rootView.findViewById(R.id.tv_center);// 标题
        textTitle.setText(getString(R.string.fm_settings));

        View headView = LayoutInflater.from(getActivity()).inflate(R.layout.head_view_fm, null);
        fmList = (ListView) rootView.findViewById(R.id.fm_list_view);// 推荐的 FM 列表
        fmList.addHeaderView(headView);

        getData();
    }

    // 初始化点击事件
    private void initEvent() {
        rootView.findViewById(R.id.head_left_btn).setOnClickListener(this);// 返回
    }

    // 获取数据 -- > 测试数据
    private void getData() {
        if (list != null) list.clear();
        FMInfo fmInfo = new FMInfo();
        fmInfo.setFmName("FM 87.5MHz");
        fmInfo.setType(1);
        list.add(fmInfo);

        fmInfo = new FMInfo();
        fmInfo.setFmName("FM 97.3MHz");
        fmInfo.setType(0);
        list.add(fmInfo);

        fmInfo = new FMInfo();
        fmInfo.setFmName("FM 106.4MHz");
        fmInfo.setType(0);
        list.add(fmInfo);
        fmList.setAdapter(new FMListAdapter(getActivity(), list));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (rootView != null) {
            ((ViewGroup) rootView.getParent()).removeView(rootView);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left_btn:// 返回
                MineActivity.close();
                break;
        }
    }
}
