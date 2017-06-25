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
    // 群组详情
    public static final String URL_GET_GROUP_NEWS = "api/chat-groups/{id}";
    // 群组成员
    public static final String URL_GET_GROUP_PERSON = "api/chat-groups/{id}/members";
    // 删除好友申请
    public static final String URL_NEW_FRIEND_DEL = "api/friends/applies/{id}";
    // 同意好友申请
    public static final String URL_NEW_FRIEND_APPLY = "api/friends/applies/{id}/approves";
    // 拒绝好友申请
    public static final String URL_NEW_FRIEND_REFUSE = "friends/applies/{id}/denies";
    // 搜索的好友(X)
    public static final String URL_GET_PERSON__SEARCH = "api/contacts/search";
    // 搜索的群组(X)
    public static final String URL_GET_GROUP__SEARCH = "api/contacts/search";
    // 推荐的成员(X)
    public static final String URL_GET_PERSON_RECOMMEND = "";
    // 推荐的群组(X)
    public static final String URL_GET_GROUP__RECOMMEND = "";
    // 加群方式(X)
    public static final String URL_APPLY_GROUP_TYPE = "";
    // 入组申请(X)
    public static final String URL_GROUP_APPLY = "";
    // 好友申请
    public static final String URL_PERSON_APPLY = "api/friends/applies";
    // 偏好设置(X)
    public static final String URL_PREFERENCE = "";
    // 注销登录(X)
    public static final String URL_CANCEL = "";
    // 获取用户数据(X)
    public static final String URL_GET_USER_INFO = "";

}
