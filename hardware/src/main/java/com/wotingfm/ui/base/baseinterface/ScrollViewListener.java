package com.wotingfm.ui.base.baseinterface;

import com.wotingfm.common.view.myscrollview.ObservableScrollView;

/**
 * 作者：xinLong on 2017/6/30 13:57
 * 邮箱：645700751@qq.com
 */
public interface ScrollViewListener {
    void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy);
}