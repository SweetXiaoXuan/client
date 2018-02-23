package com.pressure;

/**
 * 静态变量配置
 * @author liumengwei
 * @Time 2017/8/2
 *
 */
public enum Constant {
    POST("POSTRequest", "0"), GET("GETRequest", "1"),

    /**
     * url地址
     */
    /** user */
    // 验证手机号是否注册
    PHONE_EXIST("user verify", "/flvoice/api/user/ios/verify/u/lv"),
    // 用户注册
    USER_REGIST("user regist", "/flvoice/api/user/ios/regist/u/lv"),
    // 密码登陆
    LOGIN_PASS("user loginPass", "/flvoice/api/user/ios/loginPass"),
    // 修改密码
    CHANGE_PASS("user changePass", "/flvoice/api/user/ios/changePass"),
    // 第三方登陆
    USER_BIND_LOGIN("user userBindLogin", "/flvoice/api/user/ios/userBindLogin"),
    // 第三方解绑
    USER_UNBIND("user userUnBind", "/flvoice/api/user/ios/userUnBind"),
    // 退出登录
    LOGOUT("user logout", "/flvoice/api/user/ios/logout"),
    // 实名认证
    VERIFIED("verified", "/flvoice/api/user/ios/verified"),
    // 查询用户报名所有活动下的相关用户
    ACTIVITY_USER_LIST("activeUserList", "/flvoice/api/user/ios/activeUserList/%s/%s"),
    // 查询用户发布活动
    QUERY_PUBLISHING_ACTIVITY("queryPublishingActivity", "/lv/api/u/queryPublishingActivity/%s/%s"),
    // 查询用户发布圈子
    QUERY_PUBLISHING_ACTIVITY_CIRCLE("queryPublishingActivityCircle", "/flvoice/api/user/ios/queryPublishingActivityCircle/%s"),
    // 查询用户下 报名 参与 关注 发布的活动
    USER_ACTIVITIES("userActivities", "/flvoice/api/user/ios/userActivities/%s"),
    // 根据活动主题关键词查询用户报名所有活动下的相关用户
    SEARCH_USER_ACTIVITIES("searchUserActivities", "/flvoice/api/user/ios/searchUserActivities/%s"),
    // 获取发布的活动报名人信息
    ACTIVITY_REGISTRATION_INFORMATION("activityRegistrationInformation", "/flvoice/api/user/ios/activityRegistrationInformation/%s/%s/%s"),
    // 用户报名信息审核
    USER_REGISTRATION_INFORMATION_AUDIT("userRegistrationInformationAudit", "/flvoice/api/user/ios/userRegistrationInformationAudit/%s/%s/%s"),
    // 添加封面图
    UPLOAD_COVER_MAP("uploadCoverMap", "/flvoice/api/user/ios/uploadCoverMap"),
    // 添加用户已读消息信息
    ADD_USER_READ_MESSAGES("addUserReadMessages", "/flvoice/api/user/ios/addUserReadMessages/%s"),
    // 发现草稿列表
    FIND_DRAFT_LIST("findDraftList", "/flvoice/api/activity/ios/findDraftList/%s"),
    // 探索草稿列表
    EXPLORE_THE_DRAFT_LIST("exploreTheDraftList", "/flvoice/api/activity/ios/exploreTheDraftList/%s"),
    // 我参与的活动下其他用户
    ACTIVITY_ATTEND_USER_LIST("activityAttendUserList", "/flvoice/api/activity/ios/activityAttendUserList/%s/%s"),
    // 查询用户参与的活动
    GET_THE_PARTICIPATION_LIST("getTheParticipationList", "/flvoice/api/user/ios/getTheParticipationList/%s"),
    // 查询用户参与的圈子
    GET_THE_PARTICIPATION_CIRCLE_LIST("getTheParticipationCircleList", "/flvoice/api/user/ios/getTheParticipationCircleList/%s"),
    // 管理图文列表
    RELEASE_GRAPHIC_LIST("releaseGraphicList", "/flvoice/api/user/ios/releaseGraphicList/%s"),
    // 删除图文
    DELETE_GRAPHIC("deleteGraphic", "/flvoice/api/user/ios/deleteGraphic/%s"),
    // 查询用户参与圈子下用户列表
    PARTICIPATE_CIRCLE_FOR_USER_LIST("participateCircleForUserList", "/flvoice/api/user/ios/participateCircleForUserList/%s%s"),
    // 查询用户发起圈子下用户列表
    INITIATED_CIRCLE_FOR_USER_LIST("initiatedCircleForUserList", "/flvoice/api/user/ios/initiatedCircleForUserList/%s/%s"),
    // 将用户从圈子中删除
    DELETE_MEMBER("deleteMember", "/flvoice/api/user/ios/deleteMember/%s"),
    // 修改用户头像
    CHANGE_AVATAR("changeAvatar", "/flvoice/api/user/ios/changeAvatar"),
    // 修改用户名
    CHANGE_USERNAME("changeUsername", "/flvoice/api/user/ios/changeUsername"),
    // 修改用户自我介绍
    CHANGE_SIGNATURE("changeSignature", "/flvoice/api/user/ios/changeSignature"),
    // 修改用户性别
    CHANGE_GENDER("changeGender", "/flvoice/api/user/ios/changeGender"),

    /** acticity */
    // 主页接口
    APP_ACTIVITIES("appActivities", "/flvoice/api/activity/index/ios/%s"),
    // 获取用户是否报名或关注
    GET_USER_FOCUS_OR_SIGN_UP_STATUS("getUserFocusOrSignUpStatus", "/flvoice/api/activity/ios/getUserFocusOrSignUpStatus/%s"),
    // 发现活动框架信息
    DETAILS("details", "/flvoice/api/activity/ios/details/%s"),
    // 探索活动框架信息
    EXPLORE("explore", "/flvoice/api/activity/ios/explore/%s"),
    // 围观活动框架信息
    ONLOOKERS("onlookers", "/flvoice/api/activity/ios/onlookers/%s"),
    // 活动详细信息
    CONTENTS("contents", "/flvoice/api/activity/ios/contents/%s"),
    // 关注活动发布者
    FOCUS_ON_THE_PUBLISHER("focusOnThePublisher", "/flvoice/api/activity/ios/focusOnThePublisher/%s"),
    // 活动关注
    ATTENTION("attention", "/flvoice/api/activity/ios/attention/%s"),
    // 活动报名
    SIGN_UP("signUp", "/flvoice/api/activity/ios/signUp/%s"),
    // 图文点赞
    LIKE("like", "/flvoice/api/activity/ios/like/%s/%s"),
    // 查询活动评论
    COMMENT("comment", "/flvoice/api/activity/ios/comment/%s/%s/1"),
    // 查询活动公告
    ANNOUNCEMENT("comment", "/flvoice/api/activity/ios/announcement/%s/%s/6"),
    // 查询主办方发布图文
    ORGANIZERS_GRAPHIC("organizersGraphic", "/flvoice/api/activity/ios/organizersGraphic/%s/%s"),
    // 查询用户发布图文
    USER_GRAPHIC("userGraphic", "/flvoice/api/activity/ios/userGraphic/%s/%s"),
    // 探索 发布一级评论
    POST_COMMENTS("postComments", "/flvoice/api/activity/ios/postComments/%s/%s/%s"),
    // 发布公告
    POST_ANNOUNCEMENT("postAnnouncement", "/lv/api/lv/activity/postAnnouncement/%s/%s"),
    // 围观 发布图文
    POST_GRAPHIC("postGraphic", "/flvoice/api/activity/ios/postGraphic/%s/%s"),
    // 用户转发图文
    FORWARDING_GRAPHIC("forwardingGraphic", "/flvoice/api/activity/ios/forwardingGraphic/%s/%s/%s"),
    // 发布视频、直播
    POST_VIDEO("postVideo", "/flvoice/api/activity/ios/postVideo/%s/%s/%s/"),
    // 发布视频、直播
    POST_LIVE("postLive", "/flvoice/api/activity/ios/postLive/%s/%s/%s/"),
    // 用户转发活动
    FORWARDING_ACTIVITIES("forwardingActivities", "/flvoice/api/activity/ios/forwardingActivities/%s/%s/%s"),
    // 发布二级评论
    POST_COMMENT_COMMENT("postCommentComment", "/flvoice/api/activity/ios/postCommentComment/%s/%s"),
    // 添加活动
    MOBILE_ADD_ACTIVITY("addMobileActivity", "/flvoice/api/activity/ios/addMobileActivity"),
    // 添加圈子信息
    ADD_ACTIVITY_CIRCLE("addActivityCircle", "/lv/api/lv/activity/addActivityCircle"),
    // 添加探索活动
    ADD_EXPLORE_ACTIVITY("addExploreActivity", "/flvoice/api/activity/android/addExploreActivity"),
    // 修改活动状态
    UPDATE_ACTIVITY("updateActivity", "/flvoice/api/activity/ios/updateActivity/%s"),
    // 退出我参与的活动
    QUIT_ACTIVITY("quitActivity", "/flvoice/api/activity/ios/quitActivity/%s"),
    // 退出我参与的圈子
    QUIT_ACTIVITY_CIRCLE("quitActivityCircle", "/flvoice/api/activity/ios/quitActivityCircle/%s"),
    // 圈子框架信息
    ACTIVITY_CIRCLE_INFO("activityCircleInfo", "/flvoice/api/activity/ios/activityCircleInfo/%s"),
    // 圈子下用户列表
    ACTIVITY_CIRCLE_FOR_USERS("activityCircleForUsers", "/flvoice/api/activity/ios/activityCircleForUsers/%s/%s"),
    // 查询相关活动
    QUERY_RELIVE_ACTIVITY("queryRelativeActivity", "/flvoice/api/user/ios/queryPublishingActivity/%s"),
    // 退出我发起的圈子
    DELETE_INITIATED_ACTIVITY_CIRCLE("deleteInitiatedActivityCircle", "/lv/api/lv/activity/deleteInitiatedActivityCircle/%s"),
    // 查询往期精彩
    WONDERFUL_PAST("wonderfulPast", "/flvoice/api/activity/ios/wonderfulPast/%s/%s"),
    // 添加往期精彩
    ADD_WONDERFUL_PAST("addWonderfulPast", "/flvoice/api/activity/ios/addWonderfulPast/%s"),
    // 更新发现活动状态信息
    UPDATE_ACTIVITY_STATUS("updateActivityStatus", "/flvoice/api/activity/ios/updateActivityStatus/%s"),
    // 更新探索活动状态信息
    UPDATE_EXPLORE_ACTIVITY_STATUS("updateExploreActivityStatus", "/flvoice/api/activity/ios/updateExploreActivityStatus/%s"),

    /** auth */
    // 查询接口是否录入
    GET_URL_INFO("getURLInfo", "/lv/api/auth/getURLInfo");

    private String description;
    private String url;

    Constant(String description, String url) {
        this.description = description;
        this.url = url;
    };

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}