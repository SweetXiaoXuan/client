package com.pressure;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpPressure {
    public static void main(String[] args) {
//        // user login
//        Map<String, Object> loginPass = new HashMap<>();
//        loginPass.put("username", "18203231324");
//        loginPass.put("password", "123456");
//        String rsid = "";
//        httpPressure(Constant.POST.getUrl(), Constant.LOGIN_PASS.getDescription(),
//                Constant.LOGIN_PASS.getUrl(), 1000, loginPass, rsid);

//        // user regist
//        Map<String, Object> regist = new HashMap<>();
//        regist.put("username", "leilei");
//        regist.put("password", "leilei");
//        regist.put("phone", "18514533167");
//        regist.put("gender", "0");
//        String rsid = "";
//        httpPressure(Constant.POST.getUrl(), Constant.USER_REGIST.getDescription(),
//                Constant.USER_REGIST.getUrl(), 1000, regist, rsid);

//        // user verify
//        Map<String, Object> verify = new HashMap<>();
//        verify.put("code", "1111");
//        verify.put("state", "0");
//        verify.put("phone", "18514533167");
//        String rsid = "";
//        httpPressure(Constant.POST.getUrl(), Constant.PHONE_EXIST.getDescription(),
//                Constant.PHONE_EXIST.getUrl(), 1000, verify, rsid);

//        // changePass
//        Map<String, Object> changePass = new HashMap<>();
//        changePass.put("username", "leilei");
//        changePass.put("password", "leilei");
//        changePass.put("phone", "18514533167");
//        changePass.put("gender", "0");
//        String rsid = "";
//        httpPressure(Constant.POST.getUrl(), Constant.CHANGE_PASS.getDescription(),
//                Constant.CHANGE_PASS.getUrl(), 1000, changePass, rsid);

//        // user userBindLogin
//        Map<String, Object> userBindLogin = new HashMap<>();
//        userBindLogin.put("platform", "WEIBO");
//        userBindLogin.put("platformUid", "123456");
//        userBindLogin.put("avatar", "http://img.hodays.com/designer/avatar.jpg");
//        userBindLogin.put("nickname", "abcabc");
//        String rsid = "";
//        httpPressure(Constant.POST.getUrl(), Constant.USER_BIND_LOGIN.getDescription(),
//                Constant.USER_BIND_LOGIN.getUrl(), 1000, userBindLogin, rsid);

//        // user userUnBind
//        Map<String, Object> userUnBind = new HashMap<>();
//        userUnBind.put("platform", "WEIBO");
//        userUnBind.put("platformUid", "123456");
//        String rsid = "";
//        httpPressure(Constant.POST.getUrl(), Constant.USER_UNBIND.getDescription(),
//                Constant.USER_UNBIND.getUrl(), 1000, userUnBind, rsid);

//        // user logout
//        Map<String, Object> logout = new HashMap<>();
//        String rsid = "";
//        httpPressure(Constant.GET.getUrl(), Constant.LOGOUT.getDescription(),
//                Constant.LOGOUT.getUrl(), 1000, logout, rsid);

//        // user verified
//        Map<String, Object> verified = new HashMap<>();
//        verified.put("givename", "陈磊");
//        verified.put("type", "0");
//        verified.put("IDNumber", "152123198706062213");
//        verified.put("headPic", "http://img.hodays.com//images/activity/0/20160505/1462419613838_272_204.img");
//        String rsid = "";
//        httpPressure(Constant.POST.getUrl(), Constant.VERIFIED.getDescription(),
//                Constant.VERIFIED.getUrl(), 1000, verified, rsid);

//        // appActivities
//        Map<String, Object> appActivities = new HashMap<>();
//        appActivities.put("status", "all");
//        String rsid = "";
//        httpPressure(Constant.POST.getUrl(), Constant.APP_ACTIVITIES.getDescription(),
//                String.format(Constant.APP_ACTIVITIES.getUrl(), 1), 1000, appActivities, rsid);
//
//        // details
//        Map<String, Object> details = new HashMap<>();
//        Long aid = 484L;
//        String rsida = "";
//        httpPressure(Constant.GET.getUrl(), Constant.DETAILS.getDescription(),
//                String.format(Constant.DETAILS.getUrl(), aid), 2, details, rsida);

//        // explore
//        Map<String, Object> explore = new HashMap<>();
//        Long aid = 484L;
//        String rsid = "";
//        httpPressure(Constant.GET.getUrl(), Constant.EXPLORE.getDescription(),
//                String.format(Constant.EXPLORE.getUrl(), aid), 1000, explore, rsid);

//        // onlookers
//        Map<String, Object> onlookers = new HashMap<>();
//        Long aid = 484L;
//        String rsid = "";
//        httpPressure(Constant.GET.getUrl(), Constant.ONLOOKERS.getDescription(),
//                String.format(Constant.ONLOOKERS.getUrl(), aid), 1000, onlookers, rsid);

//        // activityCircleInfo
//        Map<String, Object> activityCircleInfo = new HashMap<>();
//        Long aid = 484L;
//        String rsid = "";
//        httpPressure(Constant.GET.getUrl(), Constant.ACTIVITY_CIRCLE_INFO.getDescription(),
//                String.format(Constant.ACTIVITY_CIRCLE_INFO.getUrl(), aid),
//                1000, activityCircleInfo, rsid);

//        // activityCircleForUsers
//        Map<String, Object> activityCircleForUsers = new HashMap<>();
//        Long aid = 484L;
//        Integer page = 1;
//        String rsid = "";
//        httpPressure(Constant.GET.getUrl(), Constant.ACTIVITY_CIRCLE_FOR_USERS.getDescription(),
//                String.format(Constant.ACTIVITY_CIRCLE_FOR_USERS.getUrl(), aid, page),
//                1000, activityCircleForUsers, rsid);

//        // wonderfulPast
//        Map<String, Object> wonderfulPast = new HashMap<>();
//        Long aid = 484L;
//        Integer page = 1;
//        String rsid = "";
//        httpPressure(Constant.GET.getUrl(), Constant.WONDERFUL_PAST.getDescription(),
//                String.format(Constant.WONDERFUL_PAST.getUrl(), aid, page),
//                1000, wonderfulPast, rsid);

//        // comment
//        Map<String, Object> comment = new HashMap<>();
//        Long aid = 484L;
//        String rsid = "";
//        httpPressure(Constant.GET.getUrl(), Constant.COMMENT.getDescription(),
//                String.format(Constant.COMMENT.getUrl(), aid, 1), 1000, comment, rsid);

//        // organizersGraphic
//        Map<String, Object> organizersGraphic = new HashMap<>();
//        Long aid = 484L;
//        Integer page = 1;
//        String rsid = "";
//        httpPressure(Constant.GET.getUrl(), Constant.ORGANIZERS_GRAPHIC.getDescription(),
//                String.format(Constant.ORGANIZERS_GRAPHIC.getUrl(), aid, page), 1000, organizersGraphic, rsid);

//        // userGraphic
//        Map<String, Object> userGraphic = new HashMap<>();
//        Long aid = 484L;
//        Integer page = 1;
//        String rsid = "";
//        httpPressure(Constant.GET.getUrl(), Constant.USER_GRAPHIC.getDescription(),
//                String.format(Constant.USER_GRAPHIC.getUrl(), aid, page), 1000, userGraphic, rsid);

//        // announcement
//        Map<String, Object> announcement = new HashMap<>();
//        Long aid = 484L;
//        Integer page = 1;
//        String rsid = "";
//        httpPressure(Constant.GET.getUrl(), Constant.ANNOUNCEMENT.getDescription(),
//                String.format(Constant.ANNOUNCEMENT.getUrl(), aid, page), 1000, announcement, rsid);

//        // contents
//        Map<String, Object> contents = new HashMap<>();
//        Long aid = 484L;
//        String rsid = "";
//        httpPressure(Constant.GET.getUrl(), Constant.CONTENTS.getDescription(),
//                String.format(Constant.CONTENTS.getUrl(), aid), 1000, contents, rsid);

//        // attention
//        Map<String, Object> attention = new HashMap<>();
//        Long aid = 484L;
//        String rsid = "";
//        httpPressure(Constant.POST.getUrl(), Constant.ATTENTION.getDescription(),
//                String.format(Constant.ATTENTION.getUrl(), aid), 1000, attention, rsid);

//        // signUp
//        Map<String, Object> signUp = new HashMap<>();
//        signUp.put("selfIntroduce", "自我介绍自我介绍");
//        String rsid = "";
//        Long aid = 484L;
//        httpPressure(Constant.POST.getUrl(), Constant.SIGN_UP.getDescription(),
//                String.format(Constant.SIGN_UP.getUrl(), aid), 1000, signUp, rsid);

//        // getUserFocusOrSignUpStatus
//        Map<String, Object> getUserFocusOrSignUpStatus = new HashMap<>();
//        String rsid = "";
//        Long aid = 484L;
//        httpPressure(Constant.GET.getUrl(), Constant.GET_USER_FOCUS_OR_SIGN_UP_STATUS.getDescription(),
//                String.format(Constant.GET_USER_FOCUS_OR_SIGN_UP_STATUS.getUrl(), aid),
//                1000, getUserFocusOrSignUpStatus, rsid);

//        // like
//        Map<String, Object> like = new HashMap<>();
//        String rsid = "";
//        Long aid = 484L;
//        Long cid = 4L;
//        httpPressure(Constant.POST.getUrl(), Constant.LIKE.getDescription(),
//                String.format(Constant.LIKE.getUrl(), aid, cid), 1000, like, rsid);

//        // addWonderfulPast
//        Map<String, Object> addWonderfulPast = new HashMap<>();
//        addWonderfulPast.put("cid", "228,229");
//        String rsid = "";
//        Long aid = 484L;
//        httpPressure(Constant.POST.getUrl(), Constant.ADD_WONDERFUL_PAST.getDescription(),
//                String.format(Constant.ADD_WONDERFUL_PAST.getUrl(), aid), 1000, addWonderfulPast, rsid);

//        // activeUserList
//        Map<String, Object> activeUserList = new HashMap<>();
//        String rsid = "";
//        Integer type = 0;
//        Integer page = 1;
//        httpPressure(Constant.GET.getUrl(), Constant.ACTIVITY_USER_LIST.getDescription(),
//                String.format(Constant.ACTIVITY_USER_LIST.getUrl(), type, page), 1000, activeUserList, rsid);

//        // uploadCoverMap
//        Map<String, Object> uploadCoverMap = new HashMap<>();
//        uploadCoverMap.put("url", "http://img.hodays.com//images/activity/0/20160505/1462419613838_272_204.img");
//        uploadCoverMap.put("signature", "个性签名哈哈哈");
//        String rsid = "";
//        httpPressure(Constant.POST.getUrl(), Constant.UPLOAD_COVER_MAP.getDescription(),
//                Constant.UPLOAD_COVER_MAP.getUrl(), 1000, uploadCoverMap, rsid);

//        // findDraftList
//        Map<String, Object> findDraftList = new HashMap<>();
//        String rsid = "";
//        Integer page = 1;
//        httpPressure(Constant.POST.getUrl(), Constant.FIND_DRAFT_LIST.getDescription(),
//                String.format(Constant.FIND_DRAFT_LIST.getUrl(), page), 1000, findDraftList, rsid);

//        // exploreTheDraftList
//        Map<String, Object> exploreTheDraftList = new HashMap<>();
//        String rsid = "";
//        Integer page = 1;
//        httpPressure(Constant.POST.getUrl(), Constant.EXPLORE_THE_DRAFT_LIST.getDescription(),
//                String.format(Constant.EXPLORE_THE_DRAFT_LIST.getUrl(), page), 1000, exploreTheDraftList, rsid);

//        // activeUserListFans
//        Map<String, Object> activeUserListFans = new HashMap<>();
//        String rsid = "";
//        Integer type = 1;
//        Integer page = 1;
//        httpPressure(Constant.GET.getUrl(), Constant.ACTIVITY_USER_LIST.getDescription(),
//                String.format(Constant.ACTIVITY_USER_LIST.getUrl(), type, page), 1000, activeUserListFans, rsid);

//        // userActivities
//        Map<String, Object> userActivities = new HashMap<>();
//        String rsid = "";
//        Integer page = 1;
//        httpPressure(Constant.GET.getUrl(), Constant.USER_ACTIVITIES.getDescription(),
//                String.format(Constant.USER_ACTIVITIES.getUrl(), page), 1000, userActivities, rsid);

//        // searchUserActivities
//        Map<String, Object> searchUserActivities = new HashMap<>();
//        searchUserActivities.put("activitySubject", "活动");
//        String rsid = "";
//        Integer page = 1;
//        httpPressure(Constant.GET.getUrl(), Constant.SEARCH_USER_ACTIVITIES.getDescription(),
//                String.format(Constant.SEARCH_USER_ACTIVITIES.getUrl(), page),
//                1000, searchUserActivities, rsid);

//        // addUserReadMessages
//        Map<String, Object> addUserReadMessages = new HashMap<>();
//        String rsid = "";
//        Long aid = 484L;
//        httpPressure(Constant.POST.getUrl(), Constant.ADD_USER_READ_MESSAGES.getDescription(),
//                String.format(Constant.ADD_USER_READ_MESSAGES.getUrl(), aid),
//                1000, addUserReadMessages, rsid);

//        // changeAvatar
//        Map<String, Object> changeAvatar = new HashMap<>();
//        changeAvatar.put("headpic", "http://img.hodays.com//images/0038/activity//20171125/1511596957968_1242_993.img");
//        String rsid = "";
//        httpPressure(Constant.POST.getUrl(), Constant.CHANGE_AVATAR.getDescription(),
//                Constant.CHANGE_AVATAR.getUrl(), 1000, changeAvatar, rsid);
//
//        // changeUsername
//        Map<String, Object> changeUsername = new HashMap<>();
//        changeUsername.put("username", "aa");
//        String rsid = "";
//        httpPressure(Constant.POST.getUrl(), Constant.CHANGE_USERNAME.getDescription(),
//                Constant.CHANGE_USERNAME.getUrl(), 1000, changeUsername, rsid);
//
//        // changeSignature
//        Map<String, Object> changeSignature = new HashMap<>();
//        changeSignature.put("signature", "法规不规范");
//        String rsid = "";
//        httpPressure(Constant.POST.getUrl(), Constant.CHANGE_SIGNATURE.getDescription(),
//                Constant.CHANGE_SIGNATURE.getUrl(), 1000, changeSignature, rsid);
//
//        // changeGender
//        Map<String, Object> changeGender = new HashMap<>();
//        changeGender.put("gender", "1");
//        String rsid = "";
//        httpPressure(Constant.POST.getUrl(), Constant.CHANGE_GENDER.getDescription(),
//                Constant.CHANGE_GENDER.getUrl(), 1000, changeGender, rsid);

//        // postComments
//        Map<String, Object> postComments = new HashMap<>();
//        postComments.put("text", "aaa");
//        postComments.put("comment", "0");
//        String rsid = "";
//        Long aid = 484L;
//        Integer type = 1;
//        Integer comment = 1;
//        httpPressure(Constant.POST.getUrl(), Constant.POST_COMMENTS.getDescription(),
//                String.format(Constant.POST_COMMENTS.getUrl(), aid, type, comment), 1000, postComments, rsid);

//        // postCommentComment
//        Map<String, Object> postCommentComment = new HashMap<>();
//        postCommentComment.put("text", "oafj");
//        String rsid = "";
//        Long aid = 484L;
//        Long cid = 144L;
//        httpPressure(Constant.POST.getUrl(), Constant.POST_COMMENT_COMMENT.getDescription(),
//                String.format(Constant.POST_COMMENT_COMMENT.getUrl(), aid, cid), 1000, postCommentComment, rsid);

//        // postGraphic
//        Map<String, Object> postGraphic = new HashMap<>();
//        postGraphic.put("text", "afgerge");
//        postGraphic.put("pic", "http://img.hodays.com//images/activity/0/20160505/1462419613838_272_204.img");
//        postGraphic.put("content", "<p><img src=\"http://img.hodays.com//images/activity/0/20160505/1462419613838_272_204.img\"/></p>");
//        String rsid = "";
//        Long aid = 484L;
//        Integer type = 2;
//        Integer comment = 1;
//        httpPressure(Constant.POST.getUrl(), Constant.POST_GRAPHIC.getDescription(),
//                String.format(Constant.POST_GRAPHIC.getUrl(), aid, type, comment), 1000, postGraphic, rsid);

//        // postVideo
//        Map<String, Object> postVideo = new HashMap<>();
//        postVideo.put("url", "http://img.hodays.com//images/activity/0/20160505/1462419613838_272_204.img");
//        String rsid = "";
//        Long aid = 484L;
//        Integer type = 2;
//        Integer comment = 1;
//        httpPressure(Constant.POST.getUrl(), Constant.POST_VIDEO.getDescription(),
//                String.format(Constant.POST_VIDEO.getUrl(), aid, type, comment), 1000, postVideo, rsid);

//        // postLive
//        Map<String, Object> postLive = new HashMap<>();
//        postLive.put("url", "http://img.hodays.com//images/activity/0/20160505/1462419613838_272_204.img");
//        String rsid = "";
//        Long aid = 484L;
//        Integer type = 3;
//        Integer comment = 1;
//        httpPressure(Constant.POST.getUrl(), Constant.POST_LIVE.getDescription(),
//                String.format(Constant.POST_LIVE.getUrl(), aid, type, comment), 1000, postLive, rsid);

//        // forwardingActivities
//        Map<String, Object> forwardingActivities = new HashMap<>();
//        forwardingActivities.put("activityId", "526");
//        String rsid = "";
//        Long aid = 484L;
//        Integer type = 8;
//        Integer comment = 1;
//        httpPressure(Constant.POST.getUrl(), Constant.FORWARDING_ACTIVITIES.getDescription(),
//                String.format(Constant.FORWARDING_ACTIVITIES.getUrl(), aid, type, comment), 1000, forwardingActivities, rsid);

//        // forwardingGraphic
//        Map<String, Object> forwardingGraphic = new HashMap<>();
//        forwardingGraphic.put("cid", "222");
//        String rsid = "";
//        Long aid = 484L;
//        Integer type = 5;
//        Integer comment = 1;
//        httpPressure(Constant.POST.getUrl(), Constant.FORWARDING_GRAPHIC.getDescription(),
//                String.format(Constant.FORWARDING_GRAPHIC.getUrl(), aid, type, comment), 1000, forwardingGraphic, rsid);

//        // updateActivity
//        Map<String, Object> updateActivity = new HashMap<>();
//        updateActivity.put("status", "2");
//        String rsid = "";
//        Long aid = 484L;
//        httpPressure(Constant.POST.getUrl(), Constant.UPDATE_ACTIVITY.getDescription(),
//                String.format(Constant.UPDATE_ACTIVITY.getUrl(), aid), 1000, updateActivity, rsid);

//        // postComments 公告
//        Map<String, Object> postCommentsGG = new HashMap<>();
//        postCommentsGG.put("text", "公告公告");
//        String rsid = "";
//        Long aid = 484L;
//        Integer type = 6;
//        Integer comment = 0;
//        httpPressure(Constant.POST.getUrl(), Constant.POST_COMMENTS.getDescription(),
//                String.format(Constant.POST_COMMENTS.getUrl(), aid, type, comment), 1000, postCommentsGG, rsid);

//        // focusOnThePublisher
//        Map<String, Object> focusOnThePublisher = new HashMap<>();
//        String rsid = "";
//        Long pid = 69L;
//        httpPressure(Constant.POST.getUrl(), Constant.FOCUS_ON_THE_PUBLISHER.getDescription(),
//                String.format(Constant.FOCUS_ON_THE_PUBLISHER.getUrl(), pid), 1000, focusOnThePublisher, rsid);

//        // addExploreActivity
//        Map<String, Object> addExploreActivity = new HashMap<>();
//        addExploreActivity.put("subject", "主题");
//        addExploreActivity.put("context", "<html><p>aaaaaaaaaaaaaa</p></html>");
//        addExploreActivity.put("indexPic", "http://img.hodays.com//images/0042/avatar//20171021/1508580774234_0_0.vedio");
//        addExploreActivity.put("indexPicIsShow", "1");
//        addExploreActivity.put("logoPic", "http://img.hodays.com//images/activity/0/20160505/1462419613838_272_204.img");
//        addExploreActivity.put("videoImage", "");
//        addExploreActivity.put("status", "0");
//        String rsid = "";
//        httpPressure(Constant.POST.getUrl(), Constant.ADD_EXPLORE_ACTIVITY.getDescription(),
//                Constant.ADD_EXPLORE_ACTIVITY.getUrl(), 1000, addExploreActivity, rsid);

//        // addMobileActivity
//        Map<String, Object> addMobileActivity = new HashMap<>();
//        addMobileActivity.put("subject", "主题");
//        addMobileActivity.put("title", "标题");
//        addMobileActivity.put("indexPic", "http://img.hodays.com//images/activity/0/20160505/1462419613838_272_204.img");
//        addMobileActivity.put("registerEndTime", "1503417600");
//        addMobileActivity.put("startTime", "1503417600");
//        addMobileActivity.put("endTime", "1503417600");
//        addMobileActivity.put("rid", rsid);
//        addMobileActivity.put("organizer", "新犀国际,爱奇艺,腾讯");
//        addMobileActivity.put("address", "活动地点");
//        addMobileActivity.put("activityNumber", "10");
//        addMobileActivity.put("fee", "10.01");
//        addMobileActivity.put("telephone", "010-10101010");
//        addMobileActivity.put("email", "aaa@qq.com");
//        addMobileActivity.put("mobile", "010-10101010");
//        addMobileActivity.put("content", "<p>\n    <img src=\"http://img.hodays.com:8088//image_news//20160412/1460442933436\" style=\"\" title=\"Undertaker.jpg\"/>\n</p>\n<p>\n    <img src=\"http://img.hodays.com:8088//image_news//20160412/1460442933549\" style=\"\" title=\"W.png\"/>\n</p>\n<p>\n    <br/>\n</p>");
//        addMobileActivity.put("type", "0");
//        addMobileActivity.put("mapPic", "http://img.hodays.com//images/activity/0/20160505/1462419613838_272_204.img");
//        addMobileActivity.put("aType", rsid);
//        addMobileActivity.put("indexPicIsShow", "0");
//        addMobileActivity.put("logoPic", "http://img.hodays.com//images/activity/0/20160505/1462419613838_272_204.img");
//        addMobileActivity.put("videoImage", "");
//        String rsid = "";
//        httpPressure(Constant.POST.getUrl(), Constant.MOBILE_ADD_ACTIVITY.getDescription(),
//                Constant.MOBILE_ADD_ACTIVITY.getUrl(), 1000, addMobileActivity, rsid);

//        // updateExploreActivityStatus
//        Map<String, Object> updateExploreActivityStatus = new HashMap<>();
//        updateExploreActivityStatus.put("indexPic", "http://img.hodays.com//images/activity/0/20160505/1462419613838_272_204.img");
//        updateExploreActivityStatus.put("indexPicIsShow", "1");
//        updateExploreActivityStatus.put("logoPic", "http://img.hodays.com//images/activity/0/20160505/1462419613838_272_204.img");
//        updateExploreActivityStatus.put("status", "3");
//        updateExploreActivityStatus.put("draft", "false");
//        updateExploreActivityStatus.put("subject", "sgdh");
//        updateExploreActivityStatus.put("content", "<html>待回复微风日发货那今晚看你说的话vis的<html>");
//        updateExploreActivityStatus.put("type", "1");
//        updateExploreActivityStatus.put("activityStatus", "3");
//        updateExploreActivityStatus.put("videoImage", "");
//        String rsid = "";
//        Long aid = 484L;
//        httpPressure(Constant.POST.getUrl(), Constant.UPDATE_EXPLORE_ACTIVITY_STATUS.getDescription(),
//                String.format(Constant.UPDATE_EXPLORE_ACTIVITY_STATUS.getUrl(), aid),
//                1000, updateExploreActivityStatus, rsid);

//        // updateActivityStatus
//        Map<String, Object> updateActivityStatus = new HashMap<>();
//        updateActivityStatus.put("indexPicIsShow", "1");
//        updateActivityStatus.put("logoPic", "http://img.hodays.com//images/activity/0/20160505/1462419613838_272_204.img");
//        updateActivityStatus.put("headpic", "http://img.hodays.com//images/activity/0/20160505/1462419613838_272_204.img");
//        updateActivityStatus.put("mapPic", "http://img.hodays.com//images/activity/0/20160505/1462419613838_272_204.img");
//        updateActivityStatus.put("status", "3");
//        updateActivityStatus.put("draft", "false");
//        updateActivityStatus.put("subject", "sgdh");
//        updateActivityStatus.put("address", "sgdh");
//        updateActivityStatus.put("content", "<html>待回复微风日发货那今晚看你说的话vis的<html>");
//        updateActivityStatus.put("type", "1");
//        updateActivityStatus.put("activityStatus", "3");
//        updateActivityStatus.put("videoImage", "");
//        updateActivityStatus.put("beginDateTime", "1503417600");
//        updateActivityStatus.put("endDateTime", "1503417600");
//        updateActivityStatus.put("registerEndTime", "1503417600");
//        updateActivityStatus.put("telephone", "010-11111111");
//        updateActivityStatus.put("email", "222@qq.com");
//        updateActivityStatus.put("mobile", "179384629");
//        updateActivityStatus.put("fee", "1");
//        updateActivityStatus.put("type", "1");
//        updateActivityStatus.put("rid", "");
//        updateActivityStatus.put("organizer", "腾讯");
//        updateActivityStatus.put("activityNumber", "10");
//        String rsid = "";
//        Long aid = 484L;
//        httpPressure(Constant.POST.getUrl(), Constant.UPDATE_EXPLORE_ACTIVITY_STATUS.getDescription(),
//                String.format(Constant.UPDATE_EXPLORE_ACTIVITY_STATUS.getUrl(), aid),
//                1000, updateActivityStatus, rsid);

//        // releaseGraphicList
//        Map<String, Object> releaseGraphicList = new HashMap<>();
//        String rsid = "";
//        Integer page = 1;
//        httpPressure(Constant.GET.getUrl(), Constant.RELEASE_GRAPHIC_LIST.getDescription(),
//                String.format(Constant.RELEASE_GRAPHIC_LIST.getUrl(), page), 1000, releaseGraphicList, rsid);

//        // deleteGraphic
//        Map<String, Object> deleteGraphic = new HashMap<>();
//        String rsid = "";
//        Long cid = 122L;
//        httpPressure(Constant.POST.getUrl(), Constant.DELETE_GRAPHIC.getDescription(),
//                String.format(Constant.DELETE_GRAPHIC.getUrl(), cid), 1000, deleteGraphic, rsid);

//        // activityAttendUserList
//        Map<String, Object> activityAttendUserList = new HashMap<>();
//        String rsid = "";
//        Long aid = 484L;
//        Integer page = 1;
//        httpPressure(Constant.GET.getUrl(), Constant.ACTIVITY_ATTEND_USER_LIST.getDescription(),
//                String.format(Constant.ACTIVITY_ATTEND_USER_LIST.getUrl(), aid, page),
//                1000, activityAttendUserList, rsid);

//        // getTheParticipationList
//        Map<String, Object> getTheParticipationList = new HashMap<>();
//        String rsid = "";
//        Integer page = 1;
//        httpPressure(Constant.GET.getUrl(), Constant.GET_THE_PARTICIPATION_LIST.getDescription(),
//                String.format(Constant.GET_THE_PARTICIPATION_LIST.getUrl(), page),
//                1000, getTheParticipationList, rsid);

//        // quitActivity
//        Map<String, Object> quitActivity = new HashMap<>();
//        String rsid = "";
//        Long aid = 484L;
//        httpPressure(Constant.POST.getUrl(), Constant.QUIT_ACTIVITY.getDescription(),
//                String.format(Constant.QUIT_ACTIVITY.getUrl(), aid), 1000, quitActivity, rsid);

//        // userRegistrationInformationAudit
//        Map<String, Object> userRegistrationInformationAudit = new HashMap<>();
//        String rsid = "";
//        Long aid = 484L;
//        String signUpUid = "24";
//        Integer status = 3;
//        httpPressure(Constant.POST.getUrl(), Constant.USER_REGISTRATION_INFORMATION_AUDIT.getDescription(),
//                String.format(Constant.USER_REGISTRATION_INFORMATION_AUDIT.getUrl(), aid, signUpUid, status),
//                1000, userRegistrationInformationAudit, rsid);

        // activityRegistrationInformation
        Map<String, Object> activityRegistrationInformation = new HashMap<>();
        String rsid = "91e8c6a0-0613-4f54-b06d-7b91d17712f6";
        Long aid = 424L;
        Integer page = 1;
        Integer status = 0;
        httpPressure(Constant.GET.getUrl(), Constant.ACTIVITY_REGISTRATION_INFORMATION.getDescription(),
                String.format(Constant.ACTIVITY_REGISTRATION_INFORMATION.getUrl(), aid, status, page),
                1000, activityRegistrationInformation, rsid);

//        // queryPublishingActivity
//        Map<String, Object> queryPublishingActivity = new HashMap<>();
//        String rsid = "";
//        Integer page = 1;
//        httpPressure(Constant.GET.getUrl(), Constant.QUERY_RELIVE_ACTIVITY.getDescription(),
//                String.format(Constant.QUERY_RELIVE_ACTIVITY.getUrl(), page),
//                1000, queryPublishingActivity, rsid);

//        // initiatedCircleForUserList
//        Map<String, Object> initiatedCircleForUserList = new HashMap<>();
//        String rsid = "";
//        Integer page = 1;
//        Long aid = 484L;
//        httpPressure(Constant.GET.getUrl(), Constant.INITIATED_CIRCLE_FOR_USER_LIST.getDescription(),
//                String.format(Constant.INITIATED_CIRCLE_FOR_USER_LIST.getUrl(), aid, page),
//                1000, initiatedCircleForUserList, rsid);

//        // participateCircleForUserList
//        Map<String, Object> participateCircleForUserList = new HashMap<>();
//        String rsid = "";
//        Integer page = 1;
//        Long aid = 484L;
//        httpPressure(Constant.GET.getUrl(), Constant.PARTICIPATE_CIRCLE_FOR_USER_LIST.getDescription(),
//                String.format(Constant.PARTICIPATE_CIRCLE_FOR_USER_LIST.getUrl(), aid, page),
//                1000, participateCircleForUserList, rsid);

//        // queryPublishingActivityCircle
//        Map<String, Object> queryPublishingActivityCircle = new HashMap<>();
//        String rsid = "";
//        Integer page = 1;
//        httpPressure(Constant.GET.getUrl(), Constant.QUERY_PUBLISHING_ACTIVITY_CIRCLE.getDescription(),
//                String.format(Constant.QUERY_PUBLISHING_ACTIVITY_CIRCLE.getUrl(), page),
//                1000, queryPublishingActivityCircle, rsid);
//
//        // getTheParticipationCircleList
//        Map<String, Object> getTheParticipationCircleList = new HashMap<>();
//        String rsid = "";
//        Integer page = 1;
//        httpPressure(Constant.GET.getUrl(), Constant.GET_THE_PARTICIPATION_CIRCLE_LIST.getDescription(),
//                String.format(Constant.GET_THE_PARTICIPATION_CIRCLE_LIST.getUrl(), page),
//                1000, getTheParticipationCircleList, rsid);

//        // deleteMember
//        Map<String, Object> deleteMember = new HashMap<>();
//        deleteMember.put("uid", "37,36");
//        String rsid = "";
//        Long aid = 484L;
//        httpPressure(Constant.POST.getUrl(), Constant.DELETE_MEMBER.getDescription(),
//                String.format(Constant.DELETE_MEMBER.getUrl(), aid), 1000, deleteMember, rsid);

//        // quitActivityCircle
//        Map<String, Object> quitActivityCircle = new HashMap<>();
//        String rsid = "";
//        Long aid = 484L;
//        httpPressure(Constant.POST.getUrl(), Constant.QUIT_ACTIVITY_CIRCLE.getDescription(),
//                String.format(Constant.QUIT_ACTIVITY_CIRCLE.getUrl(), aid), 1000, quitActivityCircle, rsid);
    }

    public static void httpPressure(
            final String requestType, final String requestUrlName, final String address,
            int num, final Map<String, Object> params, final String rsid) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    String content;
                    if ("0".equals(requestType)) {
                        content = HttpRequest.sendPost(address, params, rsid);
                    } else {
                        content = HttpRequest.sendGet(address, params, rsid);
                    }
                    System.out.println(requestUrlName + "---->" + address + "：\n" + content);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        for (int i = 0; i < num; i++) {
            new Thread(runnable).start();
        }
    }
}