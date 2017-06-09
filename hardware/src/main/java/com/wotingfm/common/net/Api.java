package com.wotingfm.common.net;


public class Api {
    //请求实例  banner
    public static final String URL_BANNER = "api/home/banners";
    // 登录
    public static final String URL_LOGIN = "api/accounts/login";
    // 注册
    public static final String URL_REGISTER = "api/accounts/register";
    // 获取验证码
    public static final String URL_REGISTER_YZM = "api/accounts/verify-codes";
    // 忘记密码重置
    public static final String URL_RESET_PASSWORDS = "api/accounts/reset-passwords";
    // 好友列表
    public static final String URL_GET_FRIENDS = "api/users/:id/friends";
}
