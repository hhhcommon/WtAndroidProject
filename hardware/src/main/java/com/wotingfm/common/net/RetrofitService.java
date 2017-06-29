package com.wotingfm.common.net;


import com.wotingfm.common.bean.AlbumInfo;
import com.wotingfm.common.bean.AnchorInfo;
import com.wotingfm.common.bean.BaseResult;
import com.wotingfm.common.bean.Channels;
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
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

import static com.google.zxing.qrcode.decoder.ErrorCorrectionLevel.Q;


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
    Observable<Player> getPlayerList(@Query("album_id") String album_id, @Query("q") String q);

    //分类子页面-获取子分类
    @GET("api/listenings/channels/{id}/children-channels")
    Observable<Channels> getChannels(@Path("id") String id);

    //分类子页面-获取某一个频道的所有专辑
    @GET("api/listenings/channels/{id}/albums")
    Observable<Subscrible> getChannelsAlbums(@Path("id") String id, @Query("page") int page);

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
    Observable<Object> login(@Query("phone") String phone,
                             @Query("password") String password);

    // 注册
    @POST(Api.URL_REGISTER)
    Observable<Object> register(@Query("phone") String phone,
                                @Query("password") String password,
                                @Query("code") String code);

    // 获取验证码
    @POST(Api.URL_REGISTER_YZM)
    Observable<Object> registerForYzm(@Query("phone") String phone);

    // 忘记密码重置
    @POST(Api.URL_RESET_PASSWORDS)
    Observable<Object> resetPasswords(@Query("phone") String phone,
                                      @Query("password") String password,
                                      @Query("code") String code);

    // 好友列表
    @GET(Api.URL_GET_FRIENDS)
    Observable<Object> getFriends(@Path("id") String id,
                                  @Query("token") String token);

    // 群组列表
    @GET(Api.URL_GET_GROUPS)
    Observable<Object> getGroups(@Path("id") String id,
                                 @Query("token") String token);

    // 获取好友信息
    @GET(Api.URL_GET_PERSON_NEWS)
    Observable<Object> getPersonNews(@Path("id") String id,
                                     @Query("token") String token);

    // 新的好友申请请求
    @GET(Api.URL_GET_NEW_FRIEND)
    Observable<Object> newFriend(@Path("id") String id,
                                 @Query("token") String token);

    // 群组详情
    @GET(Api.URL_GET_GROUP_NEWS)
    Observable<Object> getGroupNews(@Path("id") String id);

    // 群组成员
    @GET(Api.URL_GET_GROUP_PERSON)
    Observable<Object> getGroupPerson(@Path("id") String id);

    // 推荐的成员
    @GET(Api.URL_GET_PERSON_RECOMMEND)
    Observable<Object> getRecommendPerson(@Path("id") String id,
                                          @Query("token") String type);

    // 推荐的群组
    @GET(Api.URL_GET_GROUP__RECOMMEND)
    Observable<Object> getRecommendGroup(@Path("id") String id,
                                         @Query("token") String type);

    // 搜索的群组
    @GET(Api.URL_GET_GROUP__SEARCH)
    Observable<Object> getSearchGroup(@Query("q") String s);

    // 搜索的好友
    @GET(Api.URL_GET_PERSON__SEARCH)
    Observable<Object> getSearchPerson(@Query("q") String s);

    // 加群方式
    @POST(Api.URL_APPLY_GROUP_TYPE)
    Observable<Object> applyGroupType(@Query("password") String password,
                                      @Query("token") int type);

    // 删除好友申请
    @DELETE(Api.URL_NEW_FRIEND_DEL)
    Observable<Object> newFriendDel(@Path("id") String id,
                                    @Query("token") String token);

    // 同意好友申请
    @POST(Api.URL_NEW_FRIEND_APPLY)
    Observable<Object> newFriendApply(@Path("id") String id,
                                      @Query("token") String token);

    // 拒绝好友申请
    @POST(Api.URL_NEW_FRIEND_REFUSE)
    Observable<Object> newFriendRefuse(@Path("id") String id,
                                       @Query("token") String token);

    // 设置管理员
    @POST(Api.URL_GROUP_SET_MANAGER)
    Observable<Object> setManager(@Path("id") String id, @Query("new_admin_ids") String s);

    // 添加好友
    @POST(Api.URL_PERSON_APPLY)
    Observable<Object> personApply(@Query("friend_id") String id,
                                   @Query("apply_message") String s);

    // 创建群组
    @POST(Api.URL_CREATE_GROUP)
    Observable<Object> CreateGroup(@Query("title") String name,
                                   @Query("password") String password,
                                   @Query("member_access_mode") int type,
                                   @Query("logo_url") String url);

    // 设置群组备用频道
    @POST(Api.URL_SET_CHANNEL)
    Observable<Object> setChannel(@Path("id") String id,
                                  @Query("channel1") String channel1,
                                  @Query("channel2") String channel2);

    // 修改群信息(群名称)
    @PUT(Api.URL_EDIT_GROUP)
    Observable<Object> editGroupName(@Path("id") String id,
                                     @Query("title") String s);

    // 修改群信息(群介绍)
    @PUT(Api.URL_EDIT_GROUP)
    Observable<Object> editGroupIntroduce(@Path("id") String id,
                                          @Query("introduction") String s);

    // 修改群信息(群地址)
    @PUT(Api.URL_EDIT_GROUP)
    Observable<Object> editGroupAddress(@Path("id") String id,
                                        @Query("location") String s);

    // 修改群信息(群头像)
    @PUT(Api.URL_EDIT_GROUP)
    Observable<Object> editGroupUrl(@Path("id") String id,
                                    @Query("logo_url") String s);

    // 修改群信息(群密码)
    @PUT(Api.URL_EDIT_GROUP)
    Observable<Object> editGroupMM(@Path("id") String id,
                                   @Query("password") String s);

    // 修改好友备注（X）
    @PUT(Api.URL_CHANGE_PERSON_NOTE)
    Observable<Object> editPersonNote(@Path("id") String id,
                                      @Query("note") String s);


    // 删除群成员
    @DELETE(Api.URL_DEL_GROUP_NUM)
    Observable<Object> groupNumDel(@Path("id") String gid,
                                   @Query("member_user_id") String id);

    // 删除群成员
    @POST(Api.URL_ADD_GROUP_NUM)
    Observable<Object> groupNumAdd(@Path("id") String gid,
                                   @Query("member_user_id") String id);


    // 偏好设置
    @POST(Api.URL_PREFERENCE)
    Observable<Object> preference(@Query("s") String s);

    // 注销登录
    @POST(Api.URL_CANCEL)
    Observable<Object> cancel(@Query("s") String s);

    // 获取用户数据
    @POST(Api.URL_GET_USER_INFO)
    Observable<Object> getUserInfo(@Query("s") String s);

    // 入组申请
    @POST(Api.URL_GROUP_APPLY)
    Observable<Object> groupApply(@Path("id") String gid, @Query("s") String s);

}


