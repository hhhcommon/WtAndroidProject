package com.wotingfm.ui.mine.fm.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class FMSetModel {

    /**
     * 获取测试数据
     * @return
     */
    public List<FMInfo> getTestData() {
        List<FMInfo> list = new ArrayList<>();
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
        return list;
    }

}
