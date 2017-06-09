package com.wotingfm.common.net;


import com.wotingfm.common.bean.HomeBanners;
import com.wotingfm.common.bean.Player;

import retrofit2.http.GET;
import rx.Observable;


/**
 * Created by amine on 2017/2/8.
 */

public interface RetrofitService {

    /**
     * 是否是正式服务器
     */
    boolean isOfficialServer = true;
    //是否输出日志
    boolean isLOG = true;
    String HOST = "http://woting.suitingwei.com/";
    String TEST_HOST = "http://woting.suitingwei.com/";
    String BASE_URL = (isOfficialServer ? HOST : TEST_HOST);


    @GET("api/listenings/player")
    Observable<Player> getPlayerList();

    @GET("api/listenings/player")
    Observable<HomeBanners> getHomeBanners();
}


