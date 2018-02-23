package cn.xxtui.user.utils;

/**
 * 静态变量配置
 * @author liumengwei
 * @Time 2017/8/2
 *
 */
public class Constant {
    /**
     * url地址
     */

    /** user */
    // 验证手机号是否注册
    public final static String PHONE_EXIST = "/lv/api/u/phoneExist";
    // 用户注册
    public final static String USER_REGIST = "/lv/api/u/userRegist";
    // 实名认证
    public final static String VERIFIED = "/lv/api/u/verified";
    // 密码登陆
    public final static String LOGIN_PASS = "/lv/api/u/loginPass";
    // 修改密码
    public final static String CHANGE_PASS = "/lv/api/u/changePass";
    // 第三方登陆
    public final static String USER_BIND_LOGIN = "/lv/api/u/userBindLogin";
    // 第三方解绑
    public final static String USER_UNBIND = "/lv/api/u/userUnBind";
    // 查询用户报名所有活动下的相关用户
    public final static String ACTIVITY_USER_LIST = "/lv/api/u/activeUserList/%s/%s";
    // 查询用户发布活动
    public final static String QUERY_PUBLISHING_ACTIVITY = "/lv/api/u/queryPublishingActivity/%s";
    // 查询用户发布圈子
    public final static String QUERY_PUBLISHING_ACTIVITY_CIRCLE = "/lv/api/u/queryPublishingActivityCircle/%s";
    // 查询用户下 报名 参与 关注 发布的活动
    public final static String USER_ACTIVITIES = "/lv/api/u/userActivities/%s";
    // 根据活动主题关键词查询用户报名所有活动下的相关用户
    public final static String SEARCH_USER_ACTIVITIES = "/lv/api/u/searchUserActivities/%s";
    // 获取发布的活动报名人信息
    public final static String ACTIVITY_REGISTRATION_INFORMATION = "/lv/api/u/activityRegistrationInformation/%s/%s/%s";
    // 用户报名信息审核
    public final static String USER_REGISTRATION_INFORMATION_AUDIT = "/lv/api/u/userRegistrationInformationAudit/%s/%s";
    // 添加封面图
    public final static String UPLOAD_COVER_MAP = "/lv/api/u/uploadCoverMap";
    // 添加用户已读消息信息
    public final static String ADD_USER_READ_MESSAGES = "/lv/api/u/addUserReadMessages/%s";
    // 发现草稿列表
    public final static String FIND_DRAFT_LIST = "/lv/api/lv/activity/findDraftList/%s";
    // 探索草稿列表
    public final static String EXPLORE_THE_DRAFT_LIST = "/lv/api/lv/activity/exploreTheDraftList/%s";
    // 我参与的活动下其他用户
    public final static String ACTIVITY_ATTEND_USER_LIST = "/lv/api/lv/activity/activityAttendUserList/%s/%s";
    // 查询用户参与的活动
    public final static String GET_THE_PARTICIPATION_LIST = "/lv/api/u/getTheParticipationList/%s/%s";
    // 管理图文列表
    public final static String RELEASE_GRAPHIC_LIST = "/lv/api/lv/activity/releaseGraphicList/%s";
    // 删除图文
    public final static String DELETE_GRAPHIC = "/lv/api/lv/activity/deleteGraphic/%s";
    // 查询用户参与圈子下用户列表
    public final static String PARTICIPATE_CIRCLE_FOR_USER_LIST = "/lv/api/u/participateCircleForUserList/%s/%s";
    // 查询用户发起圈子下用户列表
    public final static String INITIATED_CIRCLE_FOR_USER_LIST = "/lv/api/u/initiatedCircleForUserList/%s/%s";
    // 将用户从圈子中删除
    public final static String DELETE_MEMBER = "/lv/api/u/deleteMember/%s";
    // 修改用户信息
    public final static String CHANGE_USER_INFO = "/lv/api/u/changeUserInfo";
    // 获取用户信息
    public final static String GET_USER_INFO = "/lv/api/u/getUserInfo";

    /** acticity */
    // 主页接口
    public final static String APP_ACTIVITIES = "/lv/api/lv/activity/appActivities/%s/%s";
    // 获取用户是否报名或关注
    public final static String GET_USER_FOCUS_OR_SIGN_UP_STATUS = "/lv/api/lv/activity/getUserFocusOrSignUpStatus/%s";
    // 发现活动框架信息
    public final static String DETAILS = "/lv/api/lv/activity/details/%s";
    // 探索活动框架信息
    public final static String EXPLORE = "/lv/api/lv/activity/explore/%s";
    // 围观活动框架信息
    public final static String ONLOOKERS = "/lv/api/lv/activity/onlookers/%s";
    // 活动详细信息
    public final static String CONTENTS = "/lv/api/lv/activity/contents/%s";
    // 关注活动发布者
    public final static String FOCUS_ON_THE_PUBLISHER = "/lv/api/lv/activity/focusOnThePublisher/%s";
    // 活动关注
    public final static String ATTENTION = "/lv/api/lv/activity/attention/%s";
    // 活动报名
    public final static String SIGN_UP = "/lv/api/lv/activity/signUp/%s";
    // 图文点赞
    public final static String LIKE = "/lv/api/lv/activity/like/%s/%s";
    // 查询活动评论
    public final static String COMMENT = "/lv/api/lv/activity/comment/%s/%s/%s";
    // 查询活动公告
    public final static String ANNOUNCEMENT = "/lv/api/lv/activity/comment/%s/%s/%s";
    // 查询主办方发布图文
    public final static String ORGANIZERS_GRAPHIC = "/lv/api/lv/activity/organizersGraphic/%s/%s";
    // 查询用户发布图文
    public final static String USER_GRAPHIC = "/lv/api/lv/activity/userGraphic/%s/%s";
    // 探索 发布一级评论
    public final static String POST_COMMENTS = "/lv/api/lv/activity/postComments/%s/%s";
    // 发布公告
    public final static String POST_ANNOUNCEMENT = "/lv/api/lv/activity/postAnnouncement/%s/%s";
    // 围观 发布图文
    public final static String POST_GRAPHIC = "/lv/api/lv/activity/postGraphic/%s/%s";
    // 用户转发图文
    public final static String FORWARDING_GRAPHIC = "/lv/api/lv/activity/forwardingGraphic/%s/%s";
    // 发布视频、直播
    public final static String POST_VIDEO_OR_LIVE = "/lv/api/lv/activity/postVideoOrLive/%s/%s";
    // 用户转发活动
    public final static String FORWARDING_ACTIVITIES = "/lv/api/lv/activity/forwardingActivities/%s/%s";
    // 发布二级评论
    public final static String POST_COMMENT_COMMENT = "/lv/api/lv/activity/postCommentComment/%s/%s";
    // 添加活动
    public final static String MOBILE_ADD_ACTIVITY = "/lv/api/lv/activity/addMobileActivity";
    // 添加圈子信息
    public final static String ADD_ACTIVITY_CIRCLE = "/lv/api/lv/activity/addActivityCircle";
    // 添加探索活动
    public final static String ADD_EXPLORE_ACTIVITY = "/lv/api/lv/activity/addExploreActivity";
    // 修改活动状态
    public final static String UPDATE_ACTIVITY = "/lv/api/lv/activity/updateActivity/%s";
    // 退出我参与的活动
    public final static String QUIT_ACTIVITY = "/lv/api/lv/activity/quitActivity/%s";
    // 圈子框架信息
    public final static String ACTIVITY_CIRCLE_INFO = "/lv/api/lv/activity/activityCircleInfo/%s";
    // 圈子下用户列表
    public final static String ACTIVITY_CIRCLE_FOR_USERS = "/lv/api/lv/activity/activityCircleForUsers/%s/%s";
    // 查询相关活动
    public final static String QUERY_RELIVE_ACTIVITY = "/lv/api/lv/activity/queryRelativeActivity/%s";
    // 退出我发起的圈子
    public final static String DELETE_INITIATED_ACTIVITY_CIRCLE = "/lv/api/lv/activity/deleteInitiatedActivityCircle/%s";
    // 查询往期精彩
    public final static String WONDERFUL_PAST = "/lv/api/lv/activity/wonderfulPast/%s/%s";
    // 添加往期精彩
    public final static String ADD_WONDERFUL_PAST = "/lv/api/lv/activity/addWonderfulPast/%s";
    // 更新发现活动状态信息
    public final static String UPDATE_ACTIVITY_STATUS = "/lv/api/lv/activity/updateActivityStatus/%s";
    // 发现升级围观
    public final static String UPDATE_FIND_ACTIVITY_STATUS_AND_CONTENT = "/lv/api/lv/activity/updateFindActivityStatusAndContent/%s";
    // 更新探索活动状态信息
    public final static String UPDATE_EXPLORE_ACTIVITY_STATUS = "/lv/api/lv/activity/updateExploreActivityStatus/%s";
    // 查询该图文所有信息
    public final static String GET_SIGNLE_GRAPHIC = "/lv/api/lv/activity/getSingleGraphic/%s";

    /** auth */
    // 查询接口是否录入
    public final static String GET_URL_INFO = "/lv/api/auth/getURLInfo";

    /** 企业品牌方 */
    // 企业品牌注册
    public final static String BRAND_COMPANY_REGIST = "/lv/api/lv/brand/brandCompanyRegist";

    /** 个人品牌方 */
    // 个人品牌注册
    public final static String BRAND_PERSONAL_REGIST = "/lv/api/lv/brand/brandPersonalRegist";

    /**
     * 报错代码 面向APP
     */
    // 未登录
    public final static String NOT_LOGIN = "10000";
}