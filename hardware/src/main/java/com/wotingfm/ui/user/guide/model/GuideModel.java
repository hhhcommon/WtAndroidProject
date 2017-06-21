package com.wotingfm.ui.user.guide.model;

import com.wotingfm.common.bean.HomeBanners;
import com.wotingfm.common.config.GlobalUrlConfig;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.ui.base.baseinterface.OnLoadInterface;
import com.wotingfm.ui.base.model.CommonModel;
import com.wotingfm.ui.base.model.UserInfo;
import com.wotingfm.ui.user.guide.view.GuideActivity;

import org.json.JSONObject;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class GuideModel extends UserInfo  {

    /**
     * 组装数据
     *
     * @param activity
     * @return
     */
    public JSONObject assemblyData( GuideActivity activity) {
        JSONObject jsonObject = CommonModel.getJsonObject(activity);
        return jsonObject;
    }

    /**
     * 进行数据交互
     *
     * @param url      请求地址
     * @param tag      地址标签
     * @param js       请求参数
     * @param listener 监听
     */
    public void loadNews(String url, String tag, JSONObject js, final OnLoadInterface listener) {
      /*  RetrofitUtils.getInstance().getHomeBanners()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<HomeBanners.Banner>>() {
                    @Override
                    public void call(List<HomeBanners.Banner> banners) {
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                    }
                });*/
    }

}
