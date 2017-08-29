package com.wotingfm.ui.base.baseinterface;

import com.wotingfm.common.view.myscrollview.ObservableNestedScrollView;

/**
 * 作者：xinLong on 2017/6/30 13:57
 * 邮箱：645700751@qq.com
 */
public interface NestedScrollViewListener {
    void onScrollChanged(ObservableNestedScrollView scrollView, int x, int y, int oldx, int oldy);
}