package com.wotingfm.common.net;


import com.wotingfm.common.bean.HomeBanners;
import com.wotingfm.common.bean.Player;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;
import com.wotingfm.ui.user.login.model.Login;

import retrofit2.http.GET;
import retrofit2.http.POST;
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


    @GET("api/listenings/player")
    Observable<Player> getPlayerList();

    @GET("api/listenings/player")
    Observable<HomeBanners> getHomeBanners();

    @POST(Api.URL_LOGIN)// 登录
    Observable<Login> login(@Query("phone") String phone, @Query("password") String password);

    @POST(Api.URL_REGISTER)// 注册
    Observable<Login> register(@Query("phone") String phone,@Query("password") String password,@Query("code") String code);

    @POST(Api.URL_REGISTER_YZM)// 获取验证码
    Observable<Login> registerForYzm(@Query("phone") String phone);

    @POST(Api.URL_RESET_PASSWORDS)// 忘记密码重置
    Observable<Login> resetPasswords(@Query("phone") String phone,@Query("password") String password,@Query("code") String code);

    @POST(Api.URL_GET_FRIENDS)// 好友列表
    Observable<Contact> getFriends(@Query("token") String token);
}


