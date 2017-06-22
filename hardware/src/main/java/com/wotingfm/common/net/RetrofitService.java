package com.wotingfm.common.net;


import com.wotingfm.common.bean.AlbumInfo;
import com.wotingfm.common.bean.AnchorInfo;
import com.wotingfm.common.bean.BaseResult;
import com.wotingfm.common.bean.Classification;
import com.wotingfm.common.bean.HomeBanners;
import com.wotingfm.common.bean.Player;
import com.wotingfm.common.bean.Reports;
import com.wotingfm.common.bean.Selected;
import com.wotingfm.common.bean.SelectedMore;
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
    Observable<Player> getPlayerList(@Query("album_id") String album_id);

    //获取用户订阅的专辑
    @GET("api/users/{id}/subscriptions/albums")
    Observable<Subscrible> getSubscriptionsList(@Path("id") String id);

    //主播详情
    @GET("api/listenings/users/{id}")
    Observable<AnchorInfo> getAnchorInfo(@Path("id") String id);

    //获得举报内容列表
    @GET("api/reports/reasons")
    Observable<Reports> getPlayerReports(@Query("type") String type);

    //banner SELECTION:精选; LIVE:直播; RADIO:电台
    @GET("api/listenings/banners")
    Observable<HomeBanners> getHomeBanners(@Query("type") String type);

    //分类首页
    @GET("api/listenings/channels")
    Observable<Classification> getClassifications();

    //精选首页列表
    @GET("api/listenings/selections")
    Observable<Selected> getSelecteds();

    @GET("api/listenings/selections/search-more")
    Observable<SelectedMore> getSelectedsMore(@Query("page") int page, @Query("type") String type);

    //订阅专辑
    @POST("api/listenings/albums/{id}/subscriptions")
    Observable<BaseResult> subscriptionsAlbums();

    //取消订阅
    @DELETE("api/listenings/albums/{id}/subscriptions")
    Observable<BaseResult> deleteSubscriptionsAlbums();

    //关注
    @POST("/api/fans")
    Observable<BaseResult> submitFans(@Query("idol_id") String idol_id, @Query("user_id") String user_id);

    //取消关注
    @DELETE("/api/fans")
    Observable<BaseResult> submitNoFans(@Query("idol_id") String idol_id, @Query("user_id") String user_id);

    //喜欢（收藏）节目
    @POST("api/listenings/singles/{id}/likes")
    Observable<Object> followUsers(@Path("id") String id);

    //发布的专辑列表
    @GET("api/users/{id}/albums")
    Observable<Subscrible> albumsList(@Path("id") String id, @Query("page") int page);

    //关注主播
    @POST("api/fans")
    Observable<Object> followAnchor(@Query("user_id") String user_id, @Query("idol_id") String idol_id);

    //取消关注主播
    @DELETE("api/fans")
    Observable<Object> unFollowAnchor(@Query("user_id") String user_id, @Query("idol_id") String idol_id);

    //举报某一个节目
    @POST("api/listenings/singles/{id}/reports")
    Observable<Object> reportsPlayer(@Path("id") String id, @Query("report_reason") String report_reason, @Query("content") String content);

    //举报个人
    @POST("api/users/{userId}/reports")
    Observable<Object> reportsUser(@Path("userId") String userId, @Query("report_reason") String report_reason, @Query("content") String content);

    //取消喜欢（收藏）节目
    @DELETE("api/listenings/singles/{id}/likes")
    Observable<Object> unfollowUsers(@Path("id") String id);

    //取消订阅专辑
    @DELETE("api/listenings/albums/{id}/subscriptions")
    Observable<Object> unSubscriptions(@Path("id") String id);

    //专辑详情  获得专辑信息
    @GET("api/listenings/albums/{id}")
    Observable<AlbumInfo> albumsInfo(@Path("id") String id);

    //相似推荐
    @GET("api/listenings/albums/{id}/similars")
    Observable<Subscrible> albumsSimilars(@Path("id") String id);

    //获取专辑所有节目
    @GET("api/listenings/albums/{id}/singles")
    Observable<Player> singlesList(@Path("id") String id, @Query("page") int page);

    // 登录
    @POST(Api.URL_LOGIN)
    Observable<Object> login(@Query("phone") String phone, @Query("password") String password);

    // 注册
    @POST(Api.URL_REGISTER)
    Observable<Object> register(@Query("phone") String phone, @Query("password") String password, @Query("code") String code);

    // 获取验证码
    @POST(Api.URL_REGISTER_YZM)
    Observable<Object> registerForYzm(@Query("phone") String phone);

    // 忘记密码重置
    @POST(Api.URL_RESET_PASSWORDS)
    Observable<Login> resetPasswords(@Query("phone") String phone, @Query("password") String password, @Query("code") String code);

    // 好友列表
    @POST(Api.URL_GET_FRIENDS)
    Observable<Contact> getFriends(@Query("token") String token);

    // 加群方式
    @POST(Api.URL_APPLY_GROUP_TYPE)
    Observable<Object> applyGroupType(@Query("password") String password, @Query("token") int type);

    // 入组申请
    @POST(Api.URL_GROUP_APPLY)
    Observable<Object> groupApply(@Query("s") String s);
}


