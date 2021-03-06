package com.wotingfm.common.net;


import com.wotingfm.ui.bean.AlbumInfo;
import com.wotingfm.ui.bean.AnchorInfo;
import com.wotingfm.ui.bean.BaseResult;
import com.wotingfm.ui.bean.CLive;
import com.wotingfm.ui.bean.Channels;
import com.wotingfm.ui.bean.HomeBanners;
import com.wotingfm.ui.bean.LiveBean;
import com.wotingfm.ui.bean.Player;
import com.wotingfm.ui.bean.Provinces;
import com.wotingfm.ui.bean.Radio;
import com.wotingfm.ui.bean.RadioInfo;
import com.wotingfm.ui.bean.Room;
import com.wotingfm.ui.bean.Selected;
import com.wotingfm.ui.bean.SelectedMore;
import com.wotingfm.ui.bean.SerchList;
import com.wotingfm.ui.bean.Subscrible;
import com.wotingfm.ui.bean.TrailerInfo;
import com.wotingfm.ui.play.find.classification.main.model.Classification;
import com.wotingfm.ui.play.report.model.Reports;

import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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
    String HOST = "http://api.wotingfm.com/";
    String TEST_HOST = "http://api.wotingfm.com/";
    String BASE_URL = (isOfficialServer ? HOST : TEST_HOST);

    //首页播放fm列表
    @GET("api/listenings/player")
    Observable<Player> getPlayerList(@Query("album_id") String album_id);

    //首页推荐逻辑
    @GET("api/listenings/player")
    Observable<Player> getPlayerList();

    //播放节目
    @GET("api/listenings/singles/{singleId}/plays")
    Observable<BaseResult> playSingles(@Path("singleId") String singleId);

    //播放节目
    @POST(" api/listenings/singles/{single}/downloads")
    Observable<BaseResult> downloadSingle(@Path("single") String single);

    //fm
    @POST("api/app-rtc/rooms")
    Observable<Room> apprtcRoom();

    //预告详情
    @GET("api/voice-lives/{voiceLiveId}")
    Observable<TrailerInfo> getTrailerInfo(@Path("voiceLiveId") String voiceLiveId);

    //预约
    @POST("api/voice-lives/{id}/reservations")
    Observable<BaseResult> reservations(@Path("id") String id);

    //取消预约
    @DELETE("api/voice-lives/{id}/reservations")
    Observable<BaseResult> deleteReservations(@Path("id") String id);

    //预约电台
    @POST("api/listenings/radios/channels/playbills/{id}/reservations")
    Observable<BaseResult> reservationsRadio(@Path("id") String id);

    //取消预约电台
    @DELETE("api/listenings/radios/channels/playbills/{id}/reservations")
    Observable<BaseResult> deleteReservationsRadio(@Path("id") String id);

    //取消预约
    @GET("api/listenings/radios/channels/hots")
    Observable<BaseResult> channelsHots();

    //分类子页面-获取子分类
    @GET("api/listenings/channels/{id}/children-channels")
    Observable<Channels> getChannels(@Path("id") String id);

    //分类子页面-获取某一个频道的所有专辑
    @GET("api/listenings/channels/{id}/albums")
    Observable<Subscrible> getChannelsAlbums(@Path("id") String id, @Query("page") int page);

    //直播模块首页列表
    @GET("api/voice-lives/editor-recommandations")
    Observable<LiveBean> getRecommandations(@Query("page") int page);

    //直播模块首页列表
    @GET("/api/voice-lives/{voiceLiveId}/status")
    Observable<BaseResult> endLive(@Path("voiceLiveId") String voiceLiveId, @Query("user_id") String user_id, @Query("action") String action);

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
    Observable<BaseResult> subscriptionsAlbums(@Path("id") String albumsId);

    //订阅电台
    @POST("api/listenings/radios/channels/{channelId}/subscriptions")
    Observable<BaseResult> subscriptionsRadio(@Path("channelId") String channelId);

    //取消订阅电台
    @DELETE("api/listenings/radios/channels/{channelId}/subscriptions")
    Observable<BaseResult> deleteSubscriptionsRadio(@Path("channelId") String channelId);

    //创建直播
    @POST("api/voice-lives")
    Observable<CLive> carteLive(@Query("user_id") String user_id);

    //取消订阅
    @DELETE("api/listenings/albums/{id}/subscriptions")
    Observable<BaseResult> deleteSubscriptionsAlbums(@Path("id") String albumsId);

    //关注
    @POST("/api/fans")
    Observable<BaseResult> submitFans(@Query("idol_id") String idol_id, @Query("user_id") String user_id);

    //取消关注
    @DELETE("/api/fans")
    Observable<BaseResult> submitNoFans(@Query("idol_id") String idol_id, @Query("user_id") String user_id);

    //喜欢（收藏）节目
    @POST("api/listenings/singles/{id}/likes/{type}")
    Observable<Object> followUsers(@Path("id") String id, @Path("type") String type);

    //发布的专辑列表
    @GET("api/users/{id}/albums")
    Observable<Subscrible> albumsList(@Path("id") String id, @Query("page") int page);

    //搜索(全部)
    /*
        users:主播 singles:节目 albums:专辑; radios:电台
        q 	   搜索参数
        type  搜索类型
        page  分页
     */
    @GET("api/listenings/search")
    Observable<SerchList> serchList(@Query("type") String type, @Query("q") String q, @Query("page") int page);

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
    @DELETE("api/listenings/singles/{id}/likes/{type}")
    Observable<Object> unfollowUsers(@Path("id") String id, @Path("type") String type);

    //取消订阅专辑
    @DELETE("api/listenings/albums/{id}/subscriptions")
    Observable<Object> unSubscriptions(@Path("id") String id);

    //专辑详情  获得专辑信息
    @GET("api/listenings/albums/{id}")
    Observable<AlbumInfo> albumsInfo(@Path("id") String id);

    //相似推荐
    @GET("api/listenings/albums/{id}/similars")
    Observable<Subscrible> albumsSimilars(@Path("id") String id);

    //本地
    @GET("api/listenings/radios/channels/")
    Observable<Radio> getChannelsRadio(@Query("channel_type") String channel_type, @Query("page") int page);

    //获取省／国家／地区电台
    @GET("api/listenings/radios/channels/")
    Observable<Radio> getChannelsRadioLocation(@Query("channel_type") String channel_type, @Query("area_code") String area_code, @Query("page") int page);

    //电台列表 热门
    @GET("api/listenings/radios/channels/hots")
    Observable<Radio> getChannelsRadioHots(@Query("scope") String scope);

    //电台列表
    @GET("api/listenings/radios/channels/hots")
    Observable<Radio> getChannelsRadioList(@Query("scope") String scope, @Query("page") int page);

    //获取省市列表
    @GET("/api/listenings/radios/channels/provinces")
    Observable<Provinces> getProvinces();

    //获取电台详情
    @GET("api/listenings/radios/channels/{id}")
    Observable<RadioInfo> getRadioInfo(@Path("id") String id);

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

    // 忘记密码/修改密码：共同
    @POST(Api.URL_RESET_PASSWORDS)
    Observable<Object> resetPasswords(@Query("phone") String phone,
                                      @Query("password") String password,
                                      @Query("code") String code);

    // 修改手机号
    @POST(Api.URL_RESET_PHONE_NUMBER)
    Observable<Object> resetPhoneNumber(@Query("phone") String phone,
                                        @Query("newPhone") String newPhone,
                                        @Query("code") String code);

    // 好友列表
    @GET(Api.URL_GET_FRIENDS)
    Observable<Object> getFriends(@Path("id") String id);

    // 群组列表
    @GET(Api.URL_GET_GROUPS)
    Observable<Object> getGroups(@Path("id") String id);

    // 获取好友信息
    @GET(Api.URL_GET_PERSON_NEWS)
    Observable<Object> getPersonNews(@Path("id") String id);

    // 新的好友申请请求
    @GET(Api.URL_GET_NEW_FRIEND)
    Observable<Object> newFriend(@Path("id") String id);

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
    Observable<Object> getSearchGroup(@Path("type") String type,
                                      @Query("q") String s);

    // 搜索的好友
    @GET(Api.URL_GET_PERSON__SEARCH)
    Observable<Object> getSearchPerson(@Path("type") String type,
                                       @Query("q") String s);

    // 删除好友申请
    @DELETE(Api.URL_NEW_FRIEND_DEL)
    Observable<Object> newFriendDel(@Path("id") String id);

    // 同意好友申请
    @POST(Api.URL_NEW_FRIEND_APPLY)
    Observable<Object> newFriendApply(@Path("id") String id);

    // 拒绝好友申请
    @POST(Api.URL_NEW_FRIEND_REFUSE)
    Observable<Object> newFriendRefuse(@Path("id") String id);

    // 设置管理员
    @PUT(Api.URL_GROUP_SET_MANAGER)
    Observable<Object> setManager(@Path("id") String id,
                                  @Query("new_admin_ids") String s);

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

    // 修改群信息(备用频道)
    @PUT(Api.URL_EDIT_GROUP)
    Observable<Object> setChannel(@Path("id") String id,
                                  @Query("channel") String c);

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

    // 修改群信息(加群方式)
    @PUT(Api.URL_EDIT_GROUP)
    Observable<Object> applyGroupType(@Path("id") String id,
                                      @Query("password") String password,
                                      @Query("member_access_mode") int type);

    // 修改好友备注
    @POST(Api.URL_CHANGE_PERSON_NOTE)
    Observable<Object> editPersonNote(@Path("userId") String pid,
                                      @Path("friendId") String id,
                                      @Query("aliasName") String s);

    // 删除群成员
    @DELETE(Api.URL_DEL_GROUP_NUM)
    Observable<Object> groupNumDel(@Path("id") String gid,
                                   @Query("member_user_id") String id);

    // 添加群成员
    @POST(Api.URL_ADD_GROUP_NUM)
    Observable<Object> groupNumAdd(@Path("id") String gid,
                                   @Query("member_user_id") String id);

    // 偏好设置
    @POST(Api.URL_PREFERENCE)
    Observable<Object> preference(@Path("id") String id,
                                  @Query("interest_id") String s);

    // 获取用户自己偏好设置
    @GET(Api.URL_GET_PREFERENCE)
    Observable<Object> getPreference(@Path("id") String id);


    // 注销登录(X)
    @POST(Api.URL_CANCEL)
    Observable<Object> cancel(@Query("s") String s);

    // 获取用户数据
    @GET(Api.URL_GET_USER_INFO)
    Observable<Object> getUserInfo(@Path("id") String id);

    // 入组申请
    @POST(Api.URL_GROUP_APPLY)
    Observable<Object> groupApply(@Path("id") String gid,
                                  @Query("apply_message") String news,
                                  @Query("password") String password);

    // 好友订阅专辑
    @GET(Api.URL_PERSON_SUB)
    Observable<Object> getPersonSub(@Path("id") String id);

    // 退出群组
    @DELETE(Api.URL_GROUP_DELETE)
    Observable<Object> exitGroup(@Path("gid") String gid,
                                 @Path("pid") String pid);

    // 移交群主
    @PUT(Api.URL_GROUP_TRANSFER_MANAGER)
    Observable<Object> transferManager(@Path("id") String gid,
                                       @Query("new_owner_id") String pid);

    // 意见反馈
    @POST(Api.URL_FEED_BACK)
    Observable<Object> feedback(@Query("contact_way") String information,
                                @Query("content") String feedback);

    // 修改用户自己昵称
    @POST(Api.URL_EDIT_USER)
    Observable<Object> editUserForName(@Path("id") String id,
                                       @Query("nickName") String news);

    // 完善自己的信息
    @POST(Api.URL_EDIT_USER)
    Observable<Object> editUserForInformation(@Path("id") String id,
                                              @Query("nickName") String name,
                                              @Query("portraitMini") String url);


    // 修改用户自己介绍
    @POST(Api.URL_EDIT_USER)
    Observable<Object> editUserForIntroduce(@Path("id") String id,
                                            @Query("userSign") String news);

    // 修改用户自己年龄
    @POST(Api.URL_EDIT_USER)
    Observable<Object> editUserForAge(@Path("id") String id,
                                      @Query("userAge") String news);

    // 修改用户自己位置
    @POST(Api.URL_EDIT_USER)
    Observable<Object> editUserForAddress(@Path("id") String id,
                                          @Query("area") String news);

    // 修改用户自己性别
    @POST(Api.URL_EDIT_USER)
    Observable<Object> editUserSex(@Path("id") String id,
                                   @Query("gender") String news);

    // 修改用户自己头像
    @POST(Api.URL_EDIT_USER)
    Observable<Object> editUserImg(@Path("id") String id,
                                   @Query("portraitMini") String news);

    // 删除好友
    @DELETE(Api.URL_PERSON_DEL)
    Observable<Object> delPerson(@Path("userId") String pid,
                                 @Path("friendId") String id);

    // 获取消息
    @GET(Api.URL_MSG_APPLY)
    Observable<Object> applies();

    // 消息删除
    @DELETE(Api.URL_MESSAGE_DEL)
    Observable<Object> msgDel(@Path("id") String id,
                              @Query("type") String type);

    // 消息同意
    @POST(Api.URL_MESSAGE)
    Observable<Object> msgApply(@Path("GroupId") String gid,
                                @Path("applierId") String pid,
                                @Query("status") String status);

    // 消息拒绝
    @POST(Api.URL_MESSAGE)
    Observable<Object> msgRefuse(@Path("GroupId") String gid,
                                 @Path("applierId") String pid,
                                 @Query("status") String status);

    // 极光id绑定
    @POST(Api.URL_JG_BIND)
    Observable<Object> bingJG(@Query("j_token") String id);

    // 获取版本号
    @GET(Api.URL_GET_VERSION)
    Observable<Object> getVersion();

    // 获取我关注的人
    @GET(Api.URL_GET_MY_FOCUS)
    Observable<Object> getMyFocus(@Path("id") String id);

    // 获取我喜欢的节目列表
    @GET(Api.URL_GET_MY_FAVORITE)
    Observable<Object> getMyFavorite(@Path("id") String id);

    // 解散群
    @DELETE(Api.URL_GROUP_DISSOLVE)
    Observable<Object> groupDissolve(@Path("id") String id);

    //取消关注
    @DELETE(Api.URL_DEL_FANS)
    Observable<Object> delFocus(@Query("idol_id") String idol_id, @Query("user_id") String user_id);

    // 好友同意加入群组
    @POST(Api.URL_AGREE_INVITEE_MSG)
    Observable<Object> agreeEnterGroup(@Path("id") String id);

    // 好友拒绝加入群组
    @POST(Api.URL_REFUSE_INVITEE_MSG)
    Observable<Object> refuseEnterGroup(@Path("id") String id);

    // 删除加入群组后的展示消息
    @DELETE(Api.URL_DEL_INVITEE_MSG)
    Observable<Object> delEnterGroup(@Path("id") String id);

    // 获取某个电台下面正在播的节目
    @GET(Api.URL_GET_RADIOING)
    Observable<Object> getSeek(@Path("id") String id);

    // 续播上传
    @POST(Api.URL_UP_PLAY_MSG)
    Observable<Object> upDataPlayMsg(@Query("playingId") String playingId,
                                     @Query("playingType") String playingType,
                                     @Query("listType") String listType,
                                     @Query("currentTime") String currentTime);

    // 获取续播
    @GET(Api.URL_UP_PLAY_MSG)
    Observable<Object> getOnPlay();


}


