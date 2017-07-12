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
    // 忘记密码/修改密码：共同
    public static final String URL_RESET_PASSWORDS = "api/accounts/reset-passwords";
    // 创建群组
    public static final String URL_CREATE_GROUP = "api/chat-groups";
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
    public static final String URL_NEW_FRIEND_REFUSE = "api/friends/applies/{id}/denies";
    // 设置管理员
    public static final String URL_GROUP_SET_MANAGER = "api/chat-groups/{id}/admins";
    // 好友申请
    public static final String URL_PERSON_APPLY = "api/friends/applies";
    // 修改群信息
    public static final String URL_EDIT_GROUP = "api/chat-groups/{id}";
    // 删除群成员
    public static final String URL_DEL_GROUP_NUM = "api/chat-groups/{id}/members";
    // 添加群成员
    public static final String URL_ADD_GROUP_NUM = "api/chat-groups/{id}/members";
    // 搜索的好友
    public static final String URL_GET_PERSON__SEARCH = "api/listenings/search";
    // 搜索的群组
    public static final String URL_GET_GROUP__SEARCH = "api/listenings/search";
    // 好友订阅的专辑
    public static final String URL_PERSON_SUB = "api/users/{id}/subscriptions/albums";
    // 退出群组
    public static final String URL_GROUP_DELETE = "api/users/{pid}/chat-groups/{gid}";
    // 移交群主
    public static final String URL_GROUP_TRANSFER_MANAGER = "api/chat-groups/{id}/owners";
    // 推荐的成员(X)
    public static final String URL_GET_PERSON_RECOMMEND = "test";
    // 推荐的群组(X)
    public static final String URL_GET_GROUP__RECOMMEND = "test";
    // 加群方式(X)
    public static final String URL_APPLY_GROUP_TYPE = "test";
    // 入组申请(X)
    public static final String URL_GROUP_APPLY = "test";
    // 偏好设置(X)
    public static final String URL_PREFERENCE = "test";
    // 注销登录(X)
    public static final String URL_CANCEL = "test";
    // 设置群组备用频道(X)
    public static final String URL_SET_CHANNEL = "test";
    // 修改好友备注(X)
    public static final String URL_CHANGE_PERSON_NOTE = "test";
    // 意见反馈(X)
    public static final String URL_FEED_BACK = "test";
    // 获取用户自身数据引导页
    public static final String URL_GET_USER_INFO = "/api/users/{id}";
    // 修改用户信息
    public static final String URL_EDIT_USER = "/api/users/{id}";
    // 修改手机号(X)
    public static final String URL_RESET_PHONE_NUMBER = "test";

}
