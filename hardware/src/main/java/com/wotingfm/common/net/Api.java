package com.wotingfm.common.net;


import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

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
    public static final String URL_GET_PERSON__SEARCH = "api/searchUserAndGroup/{type}";
    // 搜索的群组
    public static final String URL_GET_GROUP__SEARCH = "api/searchUserAndGroup/{type}";
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
    // 入组申请
    public static final String URL_GROUP_APPLY = "api/chat-groups/{id}/applies";
    // 偏好设置
    public static final String URL_PREFERENCE = "api/users/{id}/interests";
    // 注销登录(X)
    public static final String URL_CANCEL = "test";
    // 修改好友备注
    public static final String URL_CHANGE_PERSON_NOTE = "api/users/{userId}/friends/{friendId}/alias";
    // 意见反馈
    public static final String URL_FEED_BACK = "api/advices";
    // 获取用户自身数据引导页
    public static final String URL_GET_USER_INFO = "api/users/{id}";
    // 修改用户信息
    public static final String URL_EDIT_USER = "api/users/{id}";
    // 修改手机号
    public static final String URL_RESET_PHONE_NUMBER = "api/accounts/reset-phoneNum  ";
    // 删除好友
    public static final String URL_PERSON_DEL = "api/users/{userId}/friends/{friendId}";
    // 获取消息（待修改）
    public static final String URL_MSG_APPLY = "api/chat-groups/getMes/applies";
    // 消息列表
    public static final String URL_MESSAGE = "api/chat-groups/{GroupId}/applies/{applierId}";
    // 消息删除
    public static final String URL_MESSAGE_DEL = "api/chat-groups/delMes/applies/{id}";
    // 极光id绑定
    public static final String URL_JG_BIND = "api/accounts/j_token";
    // 获取最新版本号
    public static final String URL_GET_VERSION = "api/accounts/versionNum";
    // 获取我关注的人
    public static final String URL_GET_MY_FOCUS = "api/users/{id}/idols";
    // 取消关注
    public static final String URL_DEL_FANS = "api/fans";
    // 获取我喜欢的节目列表
    public static final String URL_GET_MY_FAVORITE = "api/users/{id}/likes";

}
