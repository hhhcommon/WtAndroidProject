package com.wotingfm.common.net;


import com.wotingfm.common.bean.AnchorInfo;
import com.wotingfm.common.bean.HomeBanners;
import com.wotingfm.common.bean.Player;
import com.wotingfm.common.bean.Reports;
import com.wotingfm.common.bean.Subscrible;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;
import com.wotingfm.ui.user.login.model.Login;

import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
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

    //首页播放fm列表
    @GET("api/listenings/player")
    Observable<Player> getPlayerList();

    //获取用户订阅的专辑
    @GET("api/users/{id}/subscriptions/albums")
    Observable<Subscrible> getSubscriptionsList(@Path("id") String id);

    //主播详情
    @GET("api/listenings/users/{id}")
    Observable<AnchorInfo> getAnchorInfo(@Path("id") String id);

    //获得举报内容列表
    @GET("api/reports/reasons")
    Observable<Reports> getPlayerReports();

    @GET("api/listenings/player")
    Observable<HomeBanners> getHomeBanners();

    //喜欢（收藏）节目
    @POST("api/listenings/singles/{id}/likes")
    Observable<Object> followUsers(@Path("id") String id);

    //举报某一个节目
    @POST("api/listenings/singles/{id}/reports")
    Observable<Object> reportsPlayer(@Path("id") String id, @Query("report_reason") String report_reason, @Query("content") String content);

    //取消喜欢（收藏）节目
    @DELETE("api/listenings/singles/{id}/likes")
    Observable<Object> unfollowUsers(@Path("id") String id);

    //取消订阅专辑
    @DELETE(" api/listenings/albums/{id}/subscriptions")
    Observable<Object> unSubscriptions(@Path("id") String id);

    @POST(Api.URL_LOGIN)
// 登录
    Observable<Login> login(@Query("phone") String phone, @Query("password") String password);

    @POST(Api.URL_REGISTER)
// 注册
    Observable<Login> register(@Query("phone") String phone, @Query("password") String password, @Query("code") String code);

    @POST(Api.URL_REGISTER_YZM)
// 获取验证码
    Observable<Login> registerForYzm(@Query("phone") String phone);

    @POST(Api.URL_RESET_PASSWORDS)
// 忘记密码重置
    Observable<Login> resetPasswords(@Query("phone") String phone, @Query("password") String password, @Query("code") String code);

    @POST(Api.URL_GET_FRIENDS)
// 好友列表
    Observable<Contact> getFriends(@Query("token") String token);


}


