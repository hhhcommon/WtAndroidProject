package com.woting.commonplat.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * HeightListView
 */
public class HeightListView extends ListView {

    public HeightListView(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取 ListView 对应的 Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) return;
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount() 返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight() 获取子项间分隔符占用的高度
        // params.height 最后得到整个 ListView 完整显示需要的高度
        listView.setLayoutParams(params);
    }
}
