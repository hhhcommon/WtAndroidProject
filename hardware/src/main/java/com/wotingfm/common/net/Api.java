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
    public static final String URL_GET_FRIENDS = "api/users/{id}/friends";
    // 群组列表
    public static final String URL_GET_GROUPS = "api/users/{id}/chat-groups";
    // 好友信息
    public static final String URL_GET_PERSON_NEWS = "api/users/{id}/friend-info";
    // 新的好友申请
    public static final String URL_GET_NEW_FRIEND = "api/users/{id}/received-friend-applies";
    // 加群方式(X)
    public static final String URL_APPLY_GROUP_TYPE = "";
    // 入组申请(X)
    public static final String URL_GROUP_APPLY = "";
    // 偏好设置
    public static final String URL_PREFERENCE = "";
    // 注销登录
    public static final String URL_CANCEL = "";
    // 获取用户数据
    public static final String URL_GET_USERINFO = "";

}
