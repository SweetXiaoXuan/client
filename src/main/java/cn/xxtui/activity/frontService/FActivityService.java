package cn.xxtui.activity.frontService;

import cn.xxtui.activity.frontBean.ActivityBean;
import cn.xxtui.activity.frontBean.ActivityCircleFront;
import cn.xxtui.activity.frontBean.ActivityContentString;
import cn.xxtui.activity.frontBean.ActivityExploreFront;
import cn.xxtui.activity.frontBean.ActivityItem;
import cn.xxtui.activity.frontBean.CommentBean;
import cn.xxtui.activity.frontBean.DraftListBean;
import cn.xxtui.activity.frontBean.MobileActivityBean;
import cn.xxtui.activity.frontBean.PlatformItem;
import cn.xxtui.basic.serviceUtil.StringUtil;
import cn.xxtui.support.bean.ResultBean;
import cn.xxtui.support.bean.ResultStruct;
import cn.xxtui.support.util.MeaasgeUtil;
import cn.xxtui.support.util.XXMediaType;
import cn.xxtui.user.verification.utils.Constant;
import cn.xxtui.user.verification.utils.HttpRequest;
import cn.xxtui.user.verification.utils.ImageUtils;
import cn.xxtui.user.verification.utils.ResultMsgConstant;
import cn.xxtui.user.verification.utils.SystemException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * All activities related interface
 * @author shinglee
 * @Time 2017/8/2
 * @since V1.0
 */
@Component
@Path("/activity")
public class FActivityService {
    @Bean
    public FActivityService fActivityService() {
        return new FActivityService();
    }

    private final static Logger logger = LoggerFactory.getLogger(FActivityService.class);
    private MeaasgeUtil me = new MeaasgeUtil();

    /**
     * 主接口
     * @param device 设备
     * @param page 页码
     * @param status 查询状态
     * @param request 请求体
     * @return javax.ws.rs.core.Response
     * @author shinglee
     */
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    @Consumes({MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Path("/index/{device}/{page}/")
    public Response index(
            @PathParam("device") String device,
            @PathParam("page") String page,
            @FormParam("status") String status,
            @Context HttpServletRequest request) {
        ResultStruct result = new ResultStruct();
        // 判断查询状态
        if (StringUtil.isEmpty(status)) {
            result.setMsg("status" + me.getValue(ResultMsgConstant.canNotEmpty));
            result.setStatus(ResultStruct.ERROR);
            return Response.ok(result.toString()).build();
        }
        try {
            // 发送请求
            Object uid = request.getAttribute("uid");
            Object pid = request.getAttribute("pid");
            Object userType = request.getAttribute("userType");
            Map<String, Object> map = new HashMap<>();
            map.put("uid", uid);
            map.put("userType", userType);
            map.put("pid", pid);
            String url = String.format(Constant.APP_ACTIVITIES, status, page);
            logger.info(url);
            String content = HttpRequest.sendGet(url, map);
            JSONObject rjsonObject = new JSONObject();
            rjsonObject.put("status", "0");
            rjsonObject.put("msg", result.getMsg());
            rjsonObject.put("body", new ArrayList<>());
            try {
                // 解析json
                String APIResult = content;
                JSONObject json = JSON.parseObject(APIResult);
                if (json.getJSONObject("body") != null) {
                    // 获取查询状态
                    String state = String.valueOf(json.get("status"));
                    // 获取查询结果内容
                    JSONArray array = json.getJSONObject("body").getJSONArray("data");
                    Map<String, Object> activityItemList = new HashMap<>();
                    // 获取hasNext：分页标识 是否还有数据
                    Boolean hasMore = json.getJSONObject("body").getBoolean("hasNext");
                    List<ActivityItem> list1 = new ArrayList<>();
                    List<ActivityItem> list = new ArrayList<>();
                    // 遍历json，对json进行分组
                    for (Object obj : array) {
                        ActivityItem item = new ActivityItem();
                        ActivityItem item1 = new ActivityItem();
                        JSONObject jsonObject = (JSONObject) obj;
                        // status为all
                        if ("all".equals(status)) {
                            // 判断pushIndexPage 是否推首页(0未推 1已推)
                            // 判断upTime  null 未置顶首页   !null 已置顶首页
                            if (jsonObject.getInteger("pushIndexPage") == 1
                                    || jsonObject.getString("upTime") != null
                                    || !StringUtil.isEmpty(jsonObject.get("upTime").toString())) {
                                // 判断upTime  null 未置顶首页   !null 已置顶首页
                                if (jsonObject.getString("upTime") != null &&
                                        !StringUtil.isEmpty(jsonObject.get("upTime").toString())) {
                                    item = ActivityItem.toJson(item, jsonObject, status);
                                } else {
                                    item1 = ActivityItem.toJson(item1, jsonObject, status);
                                }
                            } else {
                                continue;
                            }
                        } else {
                            // 判断pushColumnsPage 是否推分栏(0未推 1已推)
                            // 判断upTimeColumns  null 未置顶分栏   !null 已置顶分栏
                            if (jsonObject.getInteger("pushColumnsPage") == 1
                                    || jsonObject.getString("upTimeColumns") != null
                                    || !StringUtil.isEmpty(jsonObject.getString("upTimeColumns"))) {
                                // 判断upTimeColumns  null 未置顶分栏   !null 已置顶分栏
                                if (jsonObject.getString("upTimeColumns") != null &&
                                        !StringUtil.isEmpty(jsonObject.getString("upTimeColumns"))) {
                                    item = ActivityItem.toJson(item, jsonObject, status);
                                } else {
                                    item1 = ActivityItem.toJson(item1, jsonObject, status);
                                }
                            } else {
                                continue;
                            }
                        }

                        // 活动状态为4 已删除，不追加活动信息
                        if ("4".equals(item.getStatus()) || "4".equals(item1.getStatus())) {
                            continue;
                        }
                        // 活动状态为1 围观
                        if ("1".equals(item.getStatus()) || "1".equals(item1.getStatus())) {
                            PlatformItem platformItem = PlatformItem.addData(json);
                            item.getPlatforms().add(platformItem);
                            item1.getPlatforms().add(platformItem);
                        }
                        // 活动状态为3 探索
                        if ("3".equals(item.getStatus()) || "3".equals(item1.getStatus())) {
                            item.setAuthors("hodays");//未来以实际为准
                            item1.setAuthors("hodays");//未来以实际为准
                        }
                        if (item.getAid() != null)
                            list.add(item);
                        else
                            list1.add(item1);
                    }
                    // 拼接返回json格式
                    activityItemList.put("hasNext", hasMore);
                    activityItemList.put("stickTop", list);
                    activityItemList.put("nonSetTop", list1);
                    rjsonObject.put("status", state);
                    rjsonObject.put("body", activityItemList);
                } else {
                    rjsonObject.put("status", ResultBean.OK);
                    rjsonObject.put("body", result.getBody());
                }
            } catch (Exception ex) {
                // 报错返回
                logger.error(ex.getMessage(), ex);
                rjsonObject.remove("msg");
                rjsonObject.remove("status");
                rjsonObject.put("status", "1");
                rjsonObject.put("msg", me.getValue(ResultMsgConstant.sysExption));
                return Response.ok(rjsonObject.toJSONString()).build();
            }
            return Response.ok(rjsonObject.toJSONString()).build();
        } catch (Exception ex) {
            // 报错返回
            return SystemException.setResult(result, ex, logger);
        }
    }

    /**
     * 发现活动框架接口
     * @param device 设备
     * @param aid 活动id
     * @param request 请求体
     * @return javax.ws.rs.core.Response
     * @throws java.io.IOException
     * @author liumengwei
     */
    @GET
    @Produces({XXMediaType.TEXTUTF8})
    @Consumes({MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Path("/{device}/details/{aid}")
    public Response details(
            @PathParam("device") String device,
            @PathParam("aid") String aid,
            @Context HttpServletRequest request) throws IOException {
        ResultStruct result = new ResultStruct();
        String content;
        try {
            Object uid = request.getAttribute("uid");
            Object pid = request.getAttribute("pid");
            Object userType = request.getAttribute("userType");
            Map<String, Object> map = new HashMap<>();
            map.put("uid", uid);
            map.put("userType", userType);
            map.put("pid", pid);
            String url = String.format(Constant.DETAILS, aid);
            content = HttpRequest.sendGet(url, map);
            return Response.ok(content.toString()).build();
        } catch (Exception ex) {
            return SystemException.setResult(result, ex, logger);
        }
    }

    /**
     * 探索活动框架接口
     * @param device 设备
     * @param aid 活动id
     * @param request 请求体
     * @return javax.ws.rs.core.Response
     * @throws java.io.IOException
     * @author liumengwei
     */
    @GET
    @Produces({XXMediaType.TEXTUTF8})
    @Consumes({MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Path("/{device}/explore/{aid}")
    public Response explore(
            @PathParam("device") String device,
            @PathParam("aid") String aid,
            @Context HttpServletRequest request) throws IOException {
        ResultStruct result = new ResultStruct();
        String content;
        try {
            Object uid = request.getAttribute("uid");
            Object pid = request.getAttribute("pid");
            Object userType = request.getAttribute("userType");
            Map<String, Object> map = new HashMap<>();
            map.put("uid", uid);
            map.put("userType", userType);
            map.put("pid", pid);
            String url = String.format(Constant.EXPLORE, aid);
            content = HttpRequest.sendGet(url, map);
            return Response.ok(content.toString()).build();
        } catch (Exception ex) {
            return SystemException.setResult(result, ex, logger);
        }
    }

    /**
     * 获取用户关注报名状态
     * @param device 设备
     * @param aid 活动id
     * @param request 请求体
     * @return javax.ws.rs.core.Response
     * @throws java.io.IOException
     * @author liumengwei
     * @Date 2017/9/13
     */
    @GET
    @Produces({XXMediaType.TEXTUTF8})
    @Consumes({MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Path("/{device}/getUserFocusOrSignUpStatus/{aid}")
    public Response getUserFocusOrSignUpStatus(
            @PathParam("device") String device,
            @PathParam("aid") String aid,
            @Context HttpServletRequest request) throws IOException {
        ResultStruct result = new ResultStruct();
        String content;
        try {
            Object uid = request.getAttribute("uid");
            Object pid = request.getAttribute("pid");
            Object userType = request.getAttribute("userType");
            Map<String, Object> map = new HashMap<>();
            map.put("uid", uid);
            map.put("userType", userType);
            map.put("pid", pid);
            String url = String.format(Constant.GET_USER_FOCUS_OR_SIGN_UP_STATUS, aid);
            content = HttpRequest.sendGet(url, map);
            return Response.ok(content.toString()).build();
        } catch (Exception ex) {
            return SystemException.setResult(result, ex, logger);
        }
    }

    /**
     * 围观活动框架接口
     * @param device 设备
     * @param aid 活动id
     * @param request 请求体
     * @return javax.ws.rs.core.Response
     * @throws java.io.IOException
     * @author liumengwei
     */
    @GET
    @Produces({XXMediaType.TEXTUTF8})
    @Consumes({MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Path("/{device}/onlookers/{aid}")
    public Response onlookers(
            @PathParam("device") String device,
            @PathParam("aid") String aid,
            @Context HttpServletRequest request) throws IOException {
        ResultStruct result = new ResultStruct();
        String content;
        try {
            Object uid = request.getAttribute("uid");
            Object pid = request.getAttribute("pid");
            Object userType = request.getAttribute("userType");
            Map<String, Object> map = new HashMap<>();
            map.put("uid", uid);
            map.put("userType", userType);
            map.put("pid", pid);
            String url = String.format(Constant.ONLOOKERS, aid);
            content = HttpRequest.sendGet(url, map);
            return Response.ok(content.toString()).build();
        } catch (Exception ex) {
            return SystemException.setResult(result, ex, logger);
        }
    }

    /**
     * 活动详情接口
     * @param device 设备
     * @param aid 活动id
     * @return javax.ws.rs.core.Response
     * @throws java.io.IOException
     * @author liumengwei
     */
    @GET
    @Produces({XXMediaType.TEXTUTF8})
    @Consumes({MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Path("/{device}/contents/{aid}")
    public Response contents(
            @PathParam("device") String device,
            @PathParam("aid") String aid) throws IOException {
        ResultStruct result = new ResultStruct();
        String content;
        try {
            String url = String.format(Constant.CONTENTS, aid);
            content = HttpRequest.sendGet(url, null);
            return Response.ok(content.toString()).build();
        } catch (Exception ex) {
            return SystemException.setResult(result, ex, logger);
        }
    }

    /**
     * 探索活动关注接口--关注发布者
     * @param device 设备
     * @param ppid 活动发布者权限id
     * @param isAttention 是否关注
     * @param request 请求体
     * @return javax.ws.rs.core.Response
     * @throws java.io.IOException
     * @author liumengwei
     */
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    @Consumes({MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Path("/{device}/focusOnThePublisher/{ppid}")
    public Response focusOnThePublisher(
            @PathParam("device") String device,
            @PathParam("ppid") String ppid,
            @FormParam("isAttention") String isAttention,
            @Context HttpServletRequest request) throws IOException {
        ResultStruct result = new ResultStruct();
        try {
            Object uid = request.getAttribute("uid");
            Object pid = request.getAttribute("pid");
            Object userType = request.getAttribute("userType");
            String content;
            Map<String, Object> params = new HashMap<>();
            params.put("isAttention", isAttention);
            // 判断参数是否为空
            content = StringUtil.isEmpty(params);
            if (!StringUtil.isEmpty(content)) {
                result.setMsg(content);
                result.setStatus(ResultStruct.ERROR);
                return Response.ok(result.toString()).build();
            }
            params.put("uid", uid);
            params.put("userType", userType);
            params.put("pid", pid);
            content = HttpRequest.sendPost(
                    String.format(
                            Constant.FOCUS_ON_THE_PUBLISHER,
                            ppid
                    ),
                    params
            ).toString();
            return Response.ok(content).build();
        } catch (Exception ex) {
            return SystemException.setResult(result, ex, logger);
        }
    }

    /**
     * 活动关注接口
     * @param device 设备
     * @param aid 活动id
     * @param isAttention 是否关注
     * @param request 请求体
     * @return javax.ws.rs.core.Response
     * @throws java.io.IOException
     * @author liumengwei
     */
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    @Consumes({MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Path("/{device}/attention/{aid}")
    public Response attention(
            @PathParam("device") String device,
            @PathParam("aid") String aid,
            @FormParam("isAttention") String isAttention,
            @Context HttpServletRequest request) throws IOException {
        ResultStruct result = new ResultStruct();
        try {
            Object uid = request.getAttribute("uid");
            Object pid = request.getAttribute("pid");
            Object userType = request.getAttribute("userType");
            String content;
            // 添加参数
            Map<String, Object> map = new HashMap<>();
            map.put("isAttention", isAttention);
            // 判断参数是否为空
            content = StringUtil.isEmpty(map);
            if (!StringUtil.isEmpty(content)) {
                result.setMsg(content);
                result.setStatus(ResultStruct.ERROR);
                return Response.ok(result.toString()).build();
            }
            map.put("uid", uid);
            map.put("pid", pid);
            map.put("userType", userType);
            // Returns an error description
            String url = String.format(Constant.ATTENTION, aid);
            content = HttpRequest.sendPost(url, map);
            return Response.ok(content.toString()).build();
        } catch (Exception ex) {
            return SystemException.setResult(result, ex, logger);
        }
    }

    /**
     * 活动报名接口
     * @param device 设备
     * @param aid 活动id
     * @param selfIntroduction 自我介绍
     * @param isSignUp 是否报名
     * @param request 请求体
     * @return javax.ws.rs.core.Response
     * @throws java.io.IOException
     * @author liumengwei
     */
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    @Consumes({MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Path("/{device}/signUp/{aid}")
    public Response signUp(
            @PathParam("device") String device,
            @PathParam("aid") String aid,
            @FormParam("selfIntroduction") String selfIntroduction,
            @FormParam("isSignUp") String isSignUp,
            @Context HttpServletRequest request) throws IOException {
        ResultStruct result = new ResultStruct();
        String content;
        try {
            Object uid = request.getAttribute("uid");
            Object pid = request.getAttribute("pid");
            Object userType = request.getAttribute("userType");
            Map<String, Object> map = new HashMap<>();
            if ("1".equals(isSignUp)) {
                map.put("selfIntroduction", selfIntroduction);
            }
            map.put("isSignUp", isSignUp);
            // 判断参数是否为空
            content = StringUtil.isEmpty(map);
            if (!StringUtil.isEmpty(content)) {
                result.setMsg(content);
                result.setStatus(ResultStruct.ERROR);
                return Response.ok(result.toString()).build();
            }
            if ("0".equals(isSignUp)) {
                map.put("selfIntroduction", selfIntroduction);
            }
            map.put("uid", uid);
            map.put("pid", pid);
            map.put("userType", userType);
            String url = String.format(Constant.SIGN_UP, aid);
            content = HttpRequest.sendPost(url, map);
            return Response.ok(content.toString()).build();
        } catch (Exception ex) {
            return SystemException.setResult(result, ex, logger);
        }
    }

    /**
     * 活动点赞接口
     * @param device 设备
     * @param aid 活动id
     * @param cid 图文id
     * @param isLike 是否点赞
     * @param request 请求体
     * @return javax.ws.rs.core.Response
     * @throws java.io.IOException
     * @author liumengwei
     */
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    @Consumes({MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Path("/{device}/like/{aid}/{cid}")
    public Response like(
            @PathParam("device") String device,
            @PathParam("aid") String aid,
            @PathParam("cid") String cid,
            @FormParam("isLike") String isLike,
            @Context HttpServletRequest request) throws IOException {
        ResultStruct result = new ResultStruct();
        String content;
        try {
            Object uid = request.getAttribute("uid");
            Object pid = request.getAttribute("pid");
            Object userType = request.getAttribute("userType");
            Map<String, Object> map = new HashMap<>();
            map.put("uid", uid);
            map.put("pid", pid);
            map.put("userType", userType);
            map.put("isLike", isLike);
            String url = String.format(Constant.LIKE, aid, cid);
            content = HttpRequest.sendPost(url, map);
            return Response.ok(content.toString()).build();
        } catch (Exception ex) {
            return SystemException.setResult(result, ex, logger);
        }
    }

    /**
     * 活动评论信息接口
     * @param device 设备
     * @param aid 活动id
     * @param page 页码
     * @return javax.ws.rs.core.Response
     * @throws java.io.IOException
     * @author liumengwei
     */
    @GET
    @Produces({XXMediaType.TEXTUTF8})
    @Consumes({MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Path("/{device}/comment/{aid}/{page}")
    public Response comment(
            @PathParam("device") String device,
            @PathParam("aid") String aid,
            @PathParam("page") String page) throws IOException {
        ResultStruct result = new ResultStruct();
        String content;
        try {
            String url = String.format(Constant.COMMENT, aid, page, 1);
            content = HttpRequest.sendGet(url, null);
            return Response.ok(content.toString()).build();
        } catch (Exception ex) {
            return SystemException.setResult(result, ex, logger);
        }
    }

    /**
     * 活动公告信息接口
     * @param device 设备
     * @param aid 活动id
     * @param page 页码
     * @param status ActivityComment表status状态：6 公告
     * @return javax.ws.rs.core.Response
     * @throws java.io.IOException
     * @author liumengwei
     */
    @GET
    @Produces({XXMediaType.TEXTUTF8})
    @Consumes({MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Path("/{device}/announcement/{aid}/{page}/{status}")
    public Response announcement(
            @PathParam("device") String device,
            @PathParam("aid") String aid,
            @PathParam("page") String page,
            @PathParam("status") String status) throws IOException {
        ResultStruct result = new ResultStruct();
        String content;
        try {
            String url = String.format(Constant.ANNOUNCEMENT, aid, page, status);
            content = HttpRequest.sendGet(url, null);
            return Response.ok(content.toString()).build();
        } catch (Exception ex) {
            return SystemException.setResult(result, ex, logger);
        }
    }

    /**
     * 主办方发布图文查询
     * @param device 设备
     * @param aid 活动id
     * @param page 页码
     * @param request 请求体
     * @return javax.ws.rs.core.Response
     * @throws java.io.IOException
     * @author liumengwei
     */
    @GET
    @Produces({XXMediaType.TEXTUTF8})
    @Consumes({MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Path("/{device}/organizersGraphic/{aid}/{page}/")
    public Response organizersGraphic(
            @PathParam("device") String device,
            @PathParam("aid") String aid,
            @PathParam("page") String page,
            @Context HttpServletRequest request) throws IOException {
        ResultStruct result = new ResultStruct();
        String content;
        try {
            Object uid = request.getAttribute("uid");
            Object pid = request.getAttribute("pid");
            Object userType = request.getAttribute("userType");
            Map<String, Object> map = new HashMap<>();
            map.put("uid", uid);
            map.put("pid", pid);
            map.put("userType", userType);
            String url = String.format(Constant.ORGANIZERS_GRAPHIC, aid, page);
            content = HttpRequest.sendGet(url, map);

            JSONObject rjsonObject = new JSONObject();
            rjsonObject.put("status", "0");
            rjsonObject.put("msg", result.getMsg());
            rjsonObject.put("body", new ArrayList<>());
            try {
                rjsonObject = ResultStruct.jsonObject(rjsonObject, result, content, logger);
            } catch (Exception ex) {
                return SystemException.setResult(result, ex, logger);
            }
            return Response.ok(rjsonObject.toJSONString().toString()).build();

        } catch (Exception ex) {
            return SystemException.setResult(result, ex, logger);
        }
    }

    /**
     * 用户发布图文查询
     * @param device 设备
     * @param aid 活动id
     * @param page 页码
     * @param request 请求体
     * @return javax.ws.rs.core.Response
     * @throws java.io.IOException
     * @author liumengwei
     */
    @GET
    @Produces({XXMediaType.TEXTUTF8})
    @Consumes({MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Path("/{device}/userGraphic/{aid}/{page}/")
    public Response userGraphic(
            @PathParam("device") String device,
            @PathParam("aid") String aid,
            @PathParam("page") String page,
            @Context HttpServletRequest request) throws IOException {
        ResultStruct result = new ResultStruct();
        String content;
        try {
            Object uid = request.getAttribute("uid");
            Object pid = request.getAttribute("pid");
            Object userType = request.getAttribute("userType");
            Map<String, Object> param = new HashMap<>();
            param.put("uid", uid);
            param.put("pid", pid);
            param.put("userType", userType);
            String url = String.format(Constant.USER_GRAPHIC, aid, page);
            content = HttpRequest.sendGet(url, param);

            JSONObject rjsonObject = new JSONObject();
            rjsonObject.put("status", "0");
            rjsonObject.put("msg", result.getMsg());
            rjsonObject.put("body", new ArrayList<>());
            try {
                rjsonObject = ResultStruct.jsonObject(rjsonObject, result, content, logger);
            } catch (Exception ex) {
                return SystemException.setResult(result, ex, logger);
            }
            return Response.ok(rjsonObject.toJSONString().toString()).build();

        } catch (Exception ex) {
            return SystemException.setResult(result, ex, logger);
        }
    }

    /**
     * 发现 探索用户发布评论  一级评论
     * @param device 设备
     * @param aid 活动id
     * @param type 评论类型
     * @param comment 详情页评论
     * @param commentBean 评论参数
     * @param request 请求体
     * @return javax.ws.rs.core.Response
     * @throws java.io.IOException
     * @author liumengwei
     */
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    @Consumes({MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Path("/{device}/postComments/{aid}/{type}/{comment}")
    public Response postComments(
            @PathParam("device") String device,
            @PathParam("aid") String aid,
            @PathParam("type") String type,
            @PathParam("comment") String comment,
            @BeanParam CommentBean commentBean,
            @Context HttpServletRequest request) throws IOException {
        ResultStruct result = new ResultStruct();
        String content;
        try {
            Object uid = request.getAttribute("uid");
            Object pid = request.getAttribute("pid");
            Object userType = request.getAttribute("userType");
            Map<String, Object> map = new HashMap<>();
            map.put("text", commentBean.getText());
            map.put("aid", aid);
            map.put("type", type);
            content = StringUtil.isEmpty(map);
            if (!StringUtil.isEmpty(content)) {
                result.setMsg(content);
                result.setStatus(ResultStruct.ERROR);
                return Response.ok(result.toString()).build();
            }
            map.put("uid", uid);
            map.put("pid", pid);
            map.put("userType", userType);
            String url;
            if (!"6".equals(type))
                url = String.format(Constant.POST_COMMENTS, aid, comment);
            else
                url = String.format(Constant.POST_ANNOUNCEMENT, aid, comment);
            content = HttpRequest.sendPost(url, map);
            return Response.ok(content.toString()).build();
        } catch (Exception ex) {
            return SystemException.setResult(result, ex, logger);
        }

    }

    /**
     * 围观用户发布图文  一级评论
     * @param device 设备
     * @param aid 活动id
     * @param type 评论类型
     * @param comment 活动页评论
     * @param commentBean 评论参数
     * @param request 请求体
     * @return javax.ws.rs.core.Response
     * @throws java.io.IOException
     * @author liumengwei
     * @Date 2017/9/6
     */
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    @Consumes({MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Path("/{device}/postGraphic/{aid}/{type}/{comment}")
    public Response postGraphic(
            @PathParam("device") String device,
            @PathParam("aid") String aid,
            @PathParam("type") String type,
            @PathParam("comment") String comment,
            @BeanParam CommentBean commentBean,
            @Context HttpServletRequest request) throws IOException {
        ResultStruct result = new ResultStruct();
        String content;
        try {
            Object uid = request.getAttribute("uid");
            Object pid = request.getAttribute("pid");
            Object userType = request.getAttribute("userType");
            Map<String, Object> map = new HashMap<>();
            map.put("text", commentBean.getText());
            map.put("graphicType", commentBean.getGraphicType());
            map.put("aid", aid);
            map.put("type", type);
            if ("ios".equals(device)) {
                String encodePic = commentBean.getPic()[0];
                String subPic = encodePic
                        .replace("\n", "").replace("\\","")
                        .replace("\"\"","");
                map.put("pic", subPic);
            } else if("android".equals(device)) {
                map.put("pic", commentBean.getPic());
            }
            content = StringUtil.isEmpty(map);
            if (!StringUtil.isEmpty(content)) {
                result.setMsg(content);
                result.setStatus(ResultStruct.ERROR);
                return Response.ok(result.toString()).build();
            }
            map.put("content", HtmlUtils.htmlEscape(commentBean.getContent()));
            map.put("uid", uid);
            map.put("userType", userType);
            map.put("pid", pid);
            String url = String.format(Constant.POST_GRAPHIC, aid, comment);
            content = HttpRequest.sendPost(url, map);
            return Response.ok(content.toString()).build();
        } catch (Exception ex) {
            return SystemException.setResult(result, ex, logger);
        }

    }

    /**
     * 用户发布视频
     * @param device 设备
     * @param aid 活动id
     * @param type 评论类型
     * @param comment 活动页评论
     * @param commentBean 评论参数
     * @param request 请求体
     * @return javax.ws.rs.core.Response
     * @throws java.io.IOException
     * @author liumengwei
     * @Date 2017/9/6
     */
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    @Consumes({MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Path("/{device}/postVideo/{aid}/{type}/{comment}")
    public Response postVideo(
            @PathParam("device") String device,
            @PathParam("aid") String aid,
            @PathParam("type") String type,
            @PathParam("comment") String comment,
            @BeanParam CommentBean commentBean,
            @Context HttpServletRequest request) throws IOException {
        ResultStruct result = new ResultStruct();
        String content;
        try {
            Object uid = request.getAttribute("uid");
            Object pid = request.getAttribute("pid");
            Object userType = request.getAttribute("userType");
            Map<String, Object> map = new HashMap<>();
            map.put("url", commentBean.getUrl());
            map.put("aid", aid);
            map.put("type", type);
            content = StringUtil.isEmpty(map);
            if (!StringUtil.isEmpty(content)) {
                result.setMsg(content);
                result.setStatus(ResultStruct.ERROR);
                return Response.ok(result.toString()).build();
            }
            map.put("uid", uid);
            map.put("userType", userType);
            map.put("pid", pid);
            String url = String.format(Constant.POST_VIDEO_OR_LIVE, aid, comment);
            content = HttpRequest.sendPost(url, map);
            return Response.ok(content.toString()).build();
        } catch (Exception ex) {
            return SystemException.setResult(result, ex, logger);
        }
    }

    /**
     * 用户发布直播
     * @param device 设备
     * @param aid 活动id
     * @param type 评论类型
     * @param comment 活动页评论
     * @param commentBean 评论参数
     * @param request 请求体
     * @return javax.ws.rs.core.Response
     * @throws java.io.IOException
     * @author liumengwei
     * @Date 2017/9/6
     */
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    @Consumes({MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Path("/{device}/postLive/{aid}/{type}/{comment}")
    public Response postLive(
            @PathParam("device") String device,
            @PathParam("aid") String aid,
            @PathParam("type") String type,
            @PathParam("comment") String comment,
            @BeanParam CommentBean commentBean,
            @Context HttpServletRequest request) throws IOException {
        ResultStruct result = new ResultStruct();
        String content;
        try {
            Object uid = request.getAttribute("uid");
            Object pid = request.getAttribute("pid");
            Object userType = request.getAttribute("userType");
            Map<String, Object> map = new HashMap<>();
            map.put("aid", aid);
            map.put("type", type);
            map.put("url", commentBean.getUrl());
            content = StringUtil.isEmpty(map);
            if (!StringUtil.isEmpty(content)) {
                result.setMsg(content);
                result.setStatus(ResultStruct.ERROR);
                return Response.ok(result.toString()).build();
            }
            map.put("uid", uid);
            map.put("pid", pid);
            map.put("userType", userType);
            String url = String.format(Constant.POST_VIDEO_OR_LIVE, aid, comment);
            content = HttpRequest.sendPost(url, map);
            return Response.ok(content.toString()).build();
        } catch (Exception ex) {
            return SystemException.setResult(result, ex, logger);
        }

    }

    /**
     * 用户转发活动
     * @param device 设备
     * @param aid 活动id
     * @param type 评论类型
     * @param comment 活动页评论
     * @param commentBean 评论参数
     * @param request 请求体
     * @return javax.ws.rs.core.Response
     * @throws java.io.IOException
     * @author liumengwei
     * @Date 2017/11/27
     */
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    @Consumes({MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Path("/{device}/forwardingActivities/{aid}/{type}/{comment}")
    public Response forwardingActivities(
            @PathParam("device") String device,
            @PathParam("aid") String aid,
            @PathParam("type") String type,
            @PathParam("comment") String comment,
            @BeanParam CommentBean commentBean,
            @Context HttpServletRequest request) throws IOException {
        ResultStruct result = new ResultStruct();
        String content;
        try {
            Object uid = request.getAttribute("uid");
            Object pid = request.getAttribute("pid");
            Object userType = request.getAttribute("userType");
            Map<String, Object> map = new HashMap<>();
            map.put("aid", aid);
            map.put("type", type);
            map.put("activityId", commentBean.getActivityId());
            content = StringUtil.isEmpty(map);
            if (!StringUtil.isEmpty(content)) {
                result.setMsg(content);
                result.setStatus(ResultStruct.ERROR);
                return Response.ok(result.toString()).build();
            }
            map.remove("activityId");
            map.put("uid", uid);
            map.put("pid", pid);
            map.put("userType", userType);
            map.put("text", commentBean.getActivityId());
            String url = String.format(Constant.FORWARDING_ACTIVITIES, aid, comment);
            content = HttpRequest.sendPost(url, map);
            JSONObject rjsonObject = new JSONObject();
            rjsonObject.put("status", "0");
            rjsonObject.put("msg", result.getMsg());
            rjsonObject.put("body", new ArrayList<>());
            try {
                JSONObject json = JSON.parseObject(content);
                JSONObject body = json.getJSONObject("body");
                if (body != null && !"null".equals(body)) {
                    String status = json.get("status").toString();
                    ActivityItem item = new ActivityItem();
                    JSONObject jsonObject = body.getJSONObject("commentContent");
                    item = ActivityItem.setInte(item, json, jsonObject);
                    Map<String, Object> setMap = new HashMap<>();
                    setMap = ResultStruct.setMap(
                            setMap, body, item, 8, null,null,
                            null, null);
                    rjsonObject.put("status", status);
                    rjsonObject.put("body", setMap);
                } else {
                    rjsonObject.put("status", ResultBean.OK);
                    rjsonObject.put("body", result.getBody());
                }
            } catch (Exception ex) {
                return SystemException.setResult(result, ex, logger);
            }
            return Response.ok(rjsonObject.toJSONString().toString()).build();
        } catch (Exception ex) {
            return SystemException.setResult(result, ex, logger);
        }
    }

    /**
     * 用户转发图文
     * @param device 设备
     * @param aid 活动id
     * @param type 评论类型
     * @param comment 活动页评论
     * @param commentBean 评论参数
     * @param request 请求体
     * @return javax.ws.rs.core.Response
     * @throws java.io.IOException
     * @author liumengwei
     * @Date 2017/12/10
     */
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    @Consumes({MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Path("/{device}/forwardingGraphic/{aid}/{type}/{comment}")
    public Response forwardingGraphic(
            @PathParam("device") String device,
            @PathParam("aid") String aid,
            @PathParam("type") String type,
            @PathParam("comment") String comment,
            @BeanParam CommentBean commentBean,
            @Context HttpServletRequest request) throws IOException {
        ResultStruct result = new ResultStruct();
        String content;
        try {
            Object uid = request.getAttribute("uid");
            Object pid = request.getAttribute("pid");
            Object userType = request.getAttribute("userType");
            Map<String, Object> map = new HashMap<>();
            map.put("aid", aid);
            map.put("type", type);
            map.put("cid", commentBean.getCid());
            content = StringUtil.isEmpty(map);
            if (!StringUtil.isEmpty(content)) {
                result.setMsg(content);
                result.setStatus(ResultStruct.ERROR);
                return Response.ok(result.toString()).build();
            }
            map.put("uid", uid);
            map.put("pid", pid);
            map.put("userType", userType);
            String url = String.format(Constant.FORWARDING_GRAPHIC, aid, comment);
            content = HttpRequest.sendPost(url, map);
            return Response.ok(content.toString()).build();
        } catch (Exception ex) {
            return SystemException.setResult(result, ex, logger);
        }
    }

    /**
     * 用户发布评论  二级评论
     * @param device 设备
     * @param aid 活动id
     * @param cid 评论id
     * @param commentBean 评论参数
     * @param request 请求体
     * @return javax.ws.rs.core.Response
     * @throws java.io.IOException
     * @author liumengwei
     */
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    @Consumes({MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Path("/{device}/postCommentComment/{aid}/{cid}")
    public Response postCommentComment(
            @PathParam("device") String device,
            @PathParam("aid") String aid,
            @PathParam("cid") String cid,
            @BeanParam CommentBean commentBean,
            @Context HttpServletRequest request) throws IOException {
        ResultStruct result = new ResultStruct();
        String content;
        try {
            Object uid = request.getAttribute("uid");
            Object pid = request.getAttribute("pid");
            Object userType = request.getAttribute("userType");
            Map<String, Object> map = new HashMap<>();
            map.put("uid", uid);
            map.put("text", commentBean.getText());
            map.put("aid", aid);
            content = StringUtil.isEmpty(map);
            if (!StringUtil.isEmpty(content)) {
                result.setMsg(content);
                result.setStatus(ResultStruct.ERROR);
                return Response.ok(result.toString()).build();
            }
            map.put("pid", pid);
            map.put("userType", userType);
            map.put("cid", cid);
            String url = String.format(Constant.POST_COMMENT_COMMENT, aid, cid);
            content = HttpRequest.sendPost(url, map);
            return Response.ok(content.toString()).build();
        } catch (Exception ex) {
            return SystemException.setResult(result, ex, logger);
        }

    }

    /**
     * 移动端添加活动
     * @param mobileActivityBean 添加活动参数
     * @param device 设备
     * @param request 请求体
     * @return javax.ws.rs.core.Response
     * @throws java.io.IOException
     * @author huayang
     */
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    @Consumes({MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Path("/{device}/addMobileActivity")

    public Response mobileAddActivity(
            @BeanParam MobileActivityBean mobileActivityBean,
            @PathParam("device") String device,
            @Context HttpServletRequest request) throws IOException {
        logger.info("request address:{}", request.getRequestURL().toString());
        logger.info("parameters:{}", mobileActivityBean.toString());

        ResultStruct result = new ResultStruct();
        try {
            String content;
            if (StringUtil.isEmpty(mobileActivityBean.getBeginDateTime())
                    || StringUtil.isEmpty(mobileActivityBean.getEndDateTime())
                    || StringUtil.isEmpty(mobileActivityBean.getRegisterEndTime())) {
                ResultBean bean = new ResultBean("beginDateTime or end endDateTime or registerEndTime is null:",
                        ResultStruct.ERROR);
                return Response.ok(bean.toString()).build();
            }
            List<String> listPic = new ArrayList<>();
            listPic.add(mobileActivityBean.getHeadPic());
            listPic.add(mobileActivityBean.getIndexPic());
            listPic.add(mobileActivityBean.getMapPic());
            // 访问图片 查看图片是否真实存在
            result = ImageUtils.accessImage(result, listPic);
            if (result.getStatus() == ResultStruct.ERROR) {
                return Response.ok(result.toString()).build();
            }
            Map<String, Object> map = new HashMap<>();
            map.putAll(mobileActivityBean.getMap());
            map.remove("rid");
            map.remove("content");
            map.remove("status");
            map.put("uid", request.getAttribute("uid"));
            map.put("privilegeId", request.getAttribute("pid"));
            map.put("userType", request.getAttribute("userType"));
            content = StringUtil.isEmpty(map);
            if (!StringUtil.isEmpty(content)) {
                result.setMsg(content);
                result.setStatus(ResultStruct.ERROR);
                return Response.ok(result.toString()).build();
            }
            // 添加参数uid,userType
            mobileActivityBean.setUid(request.getAttribute("uid").toString());
            mobileActivityBean.setUserType(Integer.parseInt(request.getAttribute("userType").toString()));
            mobileActivityBean.setPrivilegeId(Long.parseLong(request.getAttribute("pid").toString()));

            String url = String.format(Constant.MOBILE_ADD_ACTIVITY);
            content = HttpRequest.sendPost(url, mobileActivityBean.getMap());
            return Response.ok(content.toString()).build();
        } catch (Exception ex) {
            return SystemException.setResult(result, ex, logger);
        }
    }


    /**
     * 添加圈子信息
     * @param activityCircleFront 添加活动参数
     * @param device 设备
     * @param request 请求体
     * @return javax.ws.rs.core.Response
     * @throws java.io.IOException
     * @author huayang
     */
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    @Consumes({MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Path("/{device}/addActivityCircle")
    public Response addActivityCircle(
            @BeanParam ActivityCircleFront activityCircleFront,
            @PathParam("device") String device,
            @Context HttpServletRequest request) throws IOException {

        logger.info("request address:{}", request.getRequestURL().toString());
        logger.info("parameters:{}", activityCircleFront.toString());

        ResultStruct result = new ResultStruct();
        try {
            String content;
            Map<String, Object> map = new HashMap<>();
            map.putAll(activityCircleFront.getMap());
            map.remove("pid");
            content = StringUtil.isEmpty(map);
            if (!StringUtil.isEmpty(content)) {
                result.setMsg(content);
                result.setStatus(ResultStruct.ERROR);
                return Response.ok(result.toString()).build();
            }

            String uid = "001";
            activityCircleFront.setUid(Long.parseLong(uid.substring(2, uid.length())));

            String url = String.format(Constant.ADD_ACTIVITY_CIRCLE);
            content = HttpRequest.sendPost(url, activityCircleFront.getMap());

            return Response.ok(content.toString()).build();
        } catch (Exception ex) {
            return SystemException.setResult(result, ex, logger);
        }
    }


    /**
     * 发布探索活动接口
     * @param activityExploreFront 添加探索活动参数
     * @param device 设备
     * @param request 请求体
     * @return javax.ws.rs.core.Response
     * @throws java.io.IOException
     * @author liumengwei
     * @Date 2017/10/22
     */
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    @Consumes({MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Path("/{device}/addExploreActivity")
    public Response addExploreActivity(
            @PathParam("device") String device,
            @BeanParam ActivityExploreFront activityExploreFront,
            @Context HttpServletRequest request) throws IOException {
        ResultStruct result = new ResultStruct();
        try {
            List<String> listPic = new ArrayList<>();
            listPic.add(activityExploreFront.getHeadPic());
            listPic.add(activityExploreFront.getIndexPic());
            // 访问图片 查看图片是否真实存在
            result = ImageUtils.accessImage(result, listPic);
            if (result.getStatus() == ResultStruct.ERROR) {
                return Response.ok(result.toString()).build();
            }

            Object uid = request.getAttribute("uid");
            Object pid = request.getAttribute("pid");
            Object userType = request.getAttribute("userType");
            String content;
            Map<String, Object> map = new HashMap<>();
            map.putAll(activityExploreFront.getMap());
            content = StringUtil.isEmpty(map);
            if (!StringUtil.isEmpty(content)) {
                result.setMsg(content);
                result.setStatus(ResultStruct.ERROR);
                return Response.ok(result.toString()).build();
            }
            map.put("uid", uid);
            map.put("userType", userType);
            map.put("pid", pid);
            return Response.ok(
                    HttpRequest.sendPost(
                            String.format(
                                    Constant.ADD_EXPLORE_ACTIVITY
                            ),
                            map
                    ).toString()
            ).build();
        } catch (Exception ex) {
            return SystemException.setResult(result, ex, logger);
        }
    }

    /**
     * 修改活动状态
     * @param device 设备
     * @param aid 活动id
     * @param status 状态
     * @return javax.ws.rs.core.Response
     * @throws java.io.IOException
     * @author liumengwei
     * @Time 2017/9/2
     */
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    @Consumes({MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Path("/{device}/updateActivity/{aid}")
    public Response updateActivity(
            @PathParam("device") String device,
            @PathParam("aid") String aid,
            @FormParam("status") String status) throws IOException {
        ResultStruct result = new ResultStruct();
        try {
            String content;
            Map<String, Object> map = new HashMap<>();
            map.put("status", status);
            content = StringUtil.isEmpty(map);
            if (!StringUtil.isEmpty(content)) {
                result.setMsg(content);
                result.setStatus(ResultStruct.ERROR);
                return Response.ok(result.toString()).build();
            }
            String url = String.format(Constant.UPDATE_ACTIVITY, aid);
            content = HttpRequest.sendPost(url, map);
            return Response.ok(content.toString()).build();
        } catch (Exception ex) {
            return SystemException.setResult(result, ex, logger);
        }
    }

    /**
     * 草稿列表
     * @param device 设备
     * @param request 请求体
     * @param page 页码
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     * @Date 2017/11/11
     */
    @GET
    @Produces({XXMediaType.TEXTUTF8})
    @Consumes({MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Path("/{device}/draftList/{page}")
    public Response draftList(
            @PathParam("device") String device,
            @PathParam("page") String page,
            @Context HttpServletRequest request) {
        ResultStruct resultStruct = new ResultStruct();
        try {
            Object uid = request.getAttribute("uid");
            Object pid = request.getAttribute("pid");
            Object userType = request.getAttribute("userType");
            Map<String, Object> param = new HashMap<>();
            param.put("uid", uid);
            param.put("userType", userType);
            param.put("pid", pid);
            String findDraftList = HttpRequest.sendGet(
                    String.format(Constant.FIND_DRAFT_LIST, page), param
            ).toString();
            String exploreTheDraftList = HttpRequest.sendGet(
                    String.format(Constant.EXPLORE_THE_DRAFT_LIST, page), param
            ).toString();
            List<DraftListBean> listBeans = DraftListBean.toJson(findDraftList);
            List<DraftListBean> listBeanList = DraftListBean.toJson(exploreTheDraftList);
            listBeans.addAll(listBeanList);

            Map<String, Object> map = new HashMap<>();
            JSONObject jsonFindDraftList = JSON.parseObject(findDraftList);
            JSONObject jsonExploreDraftList = JSON.parseObject(exploreTheDraftList);
            Boolean findHasNext = false;
            Long findTotalpages = 0L;
            Boolean exploreHasNext = false;
            Long exploreTotalpages = 0L;
            JSONObject findBody = jsonFindDraftList.getJSONObject("body");
            JSONObject exploreBody = jsonExploreDraftList.getJSONObject("body");
            if (findBody!= null) {
                findHasNext = findBody.getBoolean("hasNext");
                findTotalpages = findBody.getLong("totalPages");
            }
            if (exploreBody != null) {
                exploreHasNext = exploreBody.getBoolean("hasNext");
                exploreTotalpages = exploreBody.getLong("totalPages");
            }
            map.put("hasNext", false);
            if ((findHasNext  == true && exploreHasNext == true) || findHasNext != exploreHasNext) {
                map.put("hasNext", true);
            }
            if (findTotalpages > exploreTotalpages) {
                map.put("totalPages", Integer.parseInt(findTotalpages.toString()));
            } else {
                map.put("totalPages", Integer.parseInt(exploreTotalpages.toString()));
            }
            map.put("activities", listBeans);

            resultStruct.setBody(map);
            resultStruct.setStatus(ResultBean.OK);
            return Response.ok(resultStruct.toString()).build();
        } catch (Exception ex) {
            return SystemException.setResult(resultStruct, ex, logger);
        }
    }

    /**
     * 我参与的活动下其他用户
     * @param device 设备
     * @param request 请求体
     * @param aid 活动id
     * @param page 页码
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     * @Date 2017/11/15
     */
    @GET
    @Produces({XXMediaType.TEXTUTF8})
    @Consumes({MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Path("/{device}/activityAttendUserList/{aid}/{page}")
    public Response activityAttendUserList(
            @PathParam("device") String device,
            @PathParam("aid") String aid,
            @PathParam("page") String page,
            @Context HttpServletRequest request) {
        ResultStruct resultStruct = new ResultStruct();
        try {
            Object uid = request.getAttribute("uid");
            Object pid = request.getAttribute("pid");
            Object userType = request.getAttribute("userType");
            Map<String, Object> param = new HashMap<>();
            param.put("uid", uid);
            param.put("userType", userType);
            param.put("pid", pid);
            return Response.ok(
                    HttpRequest.sendGet(
                            String.format(
                                    Constant.ACTIVITY_ATTEND_USER_LIST,
                                    aid,
                                    page
                            ),
                            param
                    ).toString()
            ).build();
        } catch (Exception ex) {
            return SystemException.setResult(resultStruct, ex, logger);
        }
    }

    /**
     * 退出我参与的活动
     * @param device 设备
     * @param request 请求体
     * @param aid 活动id
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     * @Date 2017/11/15
     */
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    @Consumes({MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Path("/{device}/quitActivity/{aid}")
    public Response quitActivity(
            @PathParam("device") String device,
            @PathParam("aid") String aid,
            @Context HttpServletRequest request) {
        ResultStruct resultStruct = new ResultStruct();
        try {
            return request(request, aid);
        } catch (Exception ex) {
            return SystemException.setResult(resultStruct, ex, logger);
        }
    }

    /**
     * 请求接口
     * @param request 请求体
     * @param aid 活动id
     * @return javax.ws.rs.core.Response
     * @throws IOException
     * @author liumengwei
     * @Date 2017/11/30
     */
    private Response request(HttpServletRequest request, String aid) throws IOException {
        Object uid = request.getAttribute("uid");
        Object pid = request.getAttribute("pid");
        Object userType = request.getAttribute("userType");
        Map<String, Object> param = new HashMap<>();
        param.put("uid", uid);
        param.put("userType", userType);
        param.put("pid", pid);
        return Response.ok(
                HttpRequest.sendPost(
                        String.format(
                                Constant.QUIT_ACTIVITY,
                                aid
                        ),
                        param
                ).toString()
        ).build();
    }

    /**
     * 退出我参与的圈子
     * @param device 设备
     * @param request 请求体
     * @param aid 活动id
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     * @Date 2017/11/15
     */
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    @Consumes({MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Path("/{device}/quitActivityCircle/{aid}")
    public Response quitActivityCircle(
            @PathParam("device") String device,
            @PathParam("aid") String aid,
            @Context HttpServletRequest request) {
        ResultStruct resultStruct = new ResultStruct();
        try {
            return request(request, aid);
        } catch (Exception ex) {
            return SystemException.setResult(resultStruct, ex, logger);
        }
    }

    /**
     * 删除我发起的圈子
     * @param device 设备
     * @param request 请求体
     * @param aid 活动id
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     * @Date 2017/11/15
     */
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    @Consumes({MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Path("/{device}/deleteInitiatedActivityCircle/{aid}")
    public Response deleteInitiatedActivityCircle(
            @PathParam("device") String device,
            @PathParam("aid") String aid,
            @Context HttpServletRequest request) {
        ResultStruct resultStruct = new ResultStruct();
        try {
            Object uid = request.getAttribute("uid");
            Object pid = request.getAttribute("pid");
            Object userType = request.getAttribute("userType");
            Map<String, Object> param = new HashMap<>();
            param.put("uid", uid);
            param.put("userType", userType);
            param.put("pid", pid);
            return Response.ok(
                    HttpRequest.sendPost(
                            String.format(
                                    Constant.DELETE_INITIATED_ACTIVITY_CIRCLE,
                                    aid
                            ),
                            param
                    ).toString()
            ).build();
        } catch (Exception ex) {
            return SystemException.setResult(resultStruct, ex, logger);
        }
    }

    /**
     * 圈子框架信息(他们)
     * @param device 设备
     * @param request 请求体
     * @param aid 活动id
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     * @Date 2017/11/23
     */
    @GET
    @Produces({XXMediaType.TEXTUTF8})
    @Consumes({MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Path("/{device}/activityCircleInfo/{aid}")
    public Response activityCircleInfo(
            @PathParam("device") String device,
            @PathParam("aid") String aid,
            @Context HttpServletRequest request) {
        ResultStruct resultStruct = new ResultStruct();
        try {
            Object uid = request.getAttribute("uid");
            Object pid = request.getAttribute("pid");
            Object userType = request.getAttribute("userType");
            Map<String, Object> param = new HashMap<>();
            param.put("uid", uid);
            param.put("userType", userType);
            param.put("pid", pid);
            return Response.ok(
                    HttpRequest.sendGet(
                            String.format(
                                    Constant.ACTIVITY_CIRCLE_INFO,
                                    aid
                            ),
                            param
                    ).toString()
            ).build();
        } catch (Exception ex) {
            return SystemException.setResult(resultStruct, ex, logger);
        }
    }

    /**
     * 查询圈子下用户列表
     * @param device 设备
     * @param aid 活动id
     * @param page 页码
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     * @Date 2017/11/23
     */
    @GET
    @Produces({XXMediaType.TEXTUTF8})
    @Consumes({MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Path("/{device}/activityCircleForUsers/{aid}/{page}")
    public Response activityCircleForUsers(
            @PathParam("device") String device,
            @PathParam("page") String page,
            @PathParam("aid") String aid) {
        ResultStruct resultStruct = new ResultStruct();
        try {
            return Response.ok(
                    HttpRequest.sendGet(
                            String.format(
                                    Constant.ACTIVITY_CIRCLE_FOR_USERS,
                                    aid,
                                    page
                            ),
                            null
                    ).toString()
            ).build();
        } catch (Exception ex) {
            return SystemException.setResult(resultStruct, ex, logger);
        }
    }

    /**
     * 查询该活动下相关活动
     * @param device 设备
     * @param aid 活动id
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     * @Date 2017/9/25
     */
    @GET
    @Produces({XXMediaType.TEXTUTF8})
    @Consumes({MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Path("/{device}/queryRelativeActivity/{aid}/")
    public Response queryRelativeActivity(
            @PathParam("device") String device,
            @PathParam("aid") String aid,
            @Context HttpServletRequest request) {
        ResultStruct resultStruct = new ResultStruct();
        try {
            Object uid = request.getAttribute("uid");
            Object pid = request.getAttribute("pid");
            Object userType = request.getAttribute("userType");
            Map<String, Object> param = new HashMap<>();
            param.put("uid", uid);
            param.put("userType", userType);
            param.put("pid", pid);
            return Response.ok(
                    HttpRequest.sendGet(
                            String.format(
                                    Constant.QUERY_RELIVE_ACTIVITY,
                                    aid
                            ),
                            param
                    ).toString()
            ).build();
        } catch (Exception ex) {
            return SystemException.setResult(resultStruct, ex, logger);
        }
    }

    /**
     * 添加往期精彩
     * @param device 设备
     * @param cid 评论id
     * @param aid 活动id
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     * @Date 2017/11/26
     */
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    @Consumes({MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Path("/{device}/addWonderfulPast/{aid}/")
    public Response addWonderfulPast(
            @PathParam("device") String device,
            @PathParam("aid") String aid,
            @FormParam("cid") String cid) {
        ResultStruct resultStruct = new ResultStruct();
        try {
            String content;
            Map<String, Object> map = new HashMap<>();
            map.put("cid", cid);
            content = StringUtil.isEmpty(map);
            if (!StringUtil.isEmpty(content)) {
                resultStruct.setMsg(content);
                resultStruct.setStatus(ResultStruct.ERROR);
                return Response.ok(resultStruct.toString()).build();
            }
            return Response.ok(
                    HttpRequest.sendPost(
                            String.format(
                                    Constant.ADD_WONDERFUL_PAST,
                                    aid
                            ),
                            map
                    ).toString()
            ).build();
        } catch (Exception ex) {
            return SystemException.setResult(resultStruct, ex, logger);
        }
    }

    /**
     * 查看往期精彩
     * @param device 设备
     * @param aid 活动id
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     * @Date 2017/11/26
     */
    @GET
    @Produces({XXMediaType.TEXTUTF8})
    @Consumes({MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Path("/{device}/wonderfulPast/{aid}/{page}/")
    public Response wonderfulPast(
            @PathParam("device") String device,
            @PathParam("page") String page,
            @PathParam("aid") String aid) {
        ResultStruct resultStruct = new ResultStruct();
        try {
            String content = HttpRequest.sendGet(
                    String.format(
                            Constant.WONDERFUL_PAST,
                            aid,
                            page
                    ),
                    null
            );
            JSONObject rjsonObject = new JSONObject();
            rjsonObject.put("status", "0");
            rjsonObject.put("msg", resultStruct.getMsg());
            rjsonObject.put("body", new ArrayList<>());
            try {
                rjsonObject = ResultStruct.jsonObject(rjsonObject, resultStruct, content, logger);
            } catch (Exception ex) {
                return SystemException.setResult(resultStruct, ex, logger);
            }
            return Response.ok(rjsonObject.toJSONString().toString()).build();
        } catch (Exception ex) {
            return SystemException.setResult(resultStruct, ex, logger);
        }
    }

    /**
     * 更新发现活动状态信息
     * @param device 设备
     * @param aid 活动id
     * @param activityContentString 活动详情相关参数
     * @param activityBean 活动相关参数
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     * @Date 2017/9/25
     */
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    @Consumes({MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Path("/{device}/updateActivityStatus/{aid}/")
    public Response updateActivityStatus(
            @PathParam("device") String device,
            @PathParam("aid") String aid,
            @BeanParam ActivityContentString activityContentString,
            @BeanParam ActivityBean activityBean,
            @Context HttpServletRequest request) {
        ResultStruct result = new ResultStruct();
        result.setStatus(ResultStruct.OK);
        try {
            List<String> listPic = new ArrayList<>();
            listPic.add(activityBean.getHeadPic());
            listPic.add(activityBean.getIndexPic());
            listPic.add(activityBean.getMapPic());
            // 访问图片 查看图片是否真实存在
            result = ImageUtils.accessImage(result, listPic);
            if (result.getStatus() == ResultStruct.ERROR) {
                return Response.ok(result.toString()).build();
            }

            Object uid = request.getAttribute("uid");
            Object pid = request.getAttribute("pid");
            Object userType = request.getAttribute("userType");
            String content;
            Map<String, Object> map = new HashMap<>();
            map.putAll(activityContentString.getMap());
            map.putAll(activityBean.getMap());
            map.remove("rid");
            content = StringUtil.isEmpty(map);
            if (!StringUtil.isEmpty(content)) {
                result.setMsg(content);
                result.setStatus(ResultStruct.ERROR);
                return Response.ok(result.toString()).build();
            }
            map.put("uid", uid);
            map.put("userType", userType);
            map.put("pid", pid);
            String url = String.format(Constant.UPDATE_ACTIVITY_STATUS, aid);
            content = HttpRequest.sendPost(url, map);
            return Response.ok(content.toString()).build();
        } catch (Exception ex) {
            return SystemException.setResult(result, ex, logger);
        }
    }

    /**
     * 发现升围观
     * @param device 设备
     * @param activityContentString 活动详情相关参数
     * @param activityBean 活动信息相关参数
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     * @Date 2017/12/22
     */
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    @Consumes({MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Path("/{device}/updateFindActivityStatusAndContent/{aid}/")
    public Response updateFindActivityStatusAndContent(
            @PathParam("device") String device,
            @PathParam("aid") String aid,
            @BeanParam ActivityContentString activityContentString,
            @BeanParam ActivityBean activityBean,
            @Context HttpServletRequest request) {
        ResultStruct result = new ResultStruct();
        try {
            List<String> listPic = new ArrayList<>();
            listPic.add(activityBean.getHeadPic());
            listPic.add(activityBean.getIndexPic());
            listPic.add(activityBean.getMapPic());
            // 访问图片 查看图片是否真实存在
            result = ImageUtils.accessImage(result, listPic);
            if (result.getStatus() == ResultStruct.ERROR) {
                return Response.ok(result.toString()).build();
            }

            Object uid = request.getAttribute("uid");
            Object pid = request.getAttribute("pid");
            Object userType = request.getAttribute("userType");
            String content;
            Map<String, Object> map = new HashMap<>();
            map.putAll(activityContentString.getMap());
            map.putAll(activityBean.updateFindActivityStatusAndContent());
            map.put("uid", uid);
            map.put("userType", userType);
            map.put("pid", pid);
            String url = String.format(
                    Constant.UPDATE_FIND_ACTIVITY_STATUS_AND_CONTENT,
                    aid);
            content = HttpRequest.sendPost(url, map);
            return Response.ok(content.toString()).build();
        } catch (Exception ex) {
            return SystemException.setResult(result, ex, logger);
        }
    }

    /**
     * 围观升圈子
     * @param device 设备
     * @param aid 活动id
     * @param activityContentString 活动详情相关参数
     * @param activityBean 活动相关参数
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     * @Date 2017/12/22
     */
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    @Consumes({MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Path("/{device}/updateOnlookersActivityStatusAndContent/{aid}/")
    public Response updateOnlookersActivityStatusAndContent(
            @PathParam("device") String device,
            @PathParam("aid") String aid,
            @BeanParam ActivityContentString activityContentString,
            @BeanParam ActivityBean activityBean,
            @Context HttpServletRequest request) {
        ResultStruct result = new ResultStruct();
        try {
            List<String> listPic = new ArrayList<>();
            listPic.add(activityBean.getHeadPic());
            listPic.add(activityBean.getIndexPic());
            listPic.add(activityBean.getMapPic());
            // 访问图片 查看图片是否真实存在
            result = ImageUtils.accessImage(result, listPic);
            if (result.getStatus() == ResultStruct.ERROR) {
                return Response.ok(result.toString()).build();
            }

            Object uid = request.getAttribute("uid");
            Object pid = request.getAttribute("pid");
            Object userType = request.getAttribute("userType");
            String content;
            Map<String, Object> map = new HashMap<>();
            map.putAll(activityContentString.getMap());
            map.putAll(activityBean.updateOnlookersActivityStatusAndContent());
            map.put("uid", uid);
            map.put("userType", userType);
            map.put("pid", pid);
            String url = String.format(
                    Constant.UPDATE_FIND_ACTIVITY_STATUS_AND_CONTENT,
                    aid);
            content = HttpRequest.sendPost(url, map);
            return Response.ok(content.toString()).build();
        } catch (Exception ex) {
            return SystemException.setResult(result, ex, logger);
        }
    }

    /**
     * 更新探索活动状态信息
     * @param device 设备
     * @param aid 活动id
     * @param activityContentString 活动详情相关参数
     * @param activityBean 活动相关参数
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     * @Date 2017/11/22
     */
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    @Consumes({MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Path("/{device}/updateExploreActivityStatus/{aid}/")
    public Response updateExploreActivityStatus(
            @PathParam("device") String device,
            @PathParam("aid") String aid,
            @BeanParam ActivityContentString activityContentString,
            @BeanParam ActivityExploreFront activityBean) {
        ResultStruct result = new ResultStruct();
        try {
            List<String> listPic = new ArrayList<>();
            listPic.add(activityBean.getHeadPic());
            listPic.add(activityBean.getIndexPic());
            // 访问图片 查看图片是否真实存在
            result = ImageUtils.accessImage(result, listPic);
            if (result.getStatus() == ResultStruct.ERROR) {
                return Response.ok(result.toString()).build();
            }

            String content;
            Map<String, Object> map = new HashMap<>();
            map.putAll(activityContentString.getMap());
            map.putAll(activityBean.getMap());

            content = StringUtil.isEmpty(map);
            if (!StringUtil.isEmpty(content)) {
                result.setMsg(content);
                result.setStatus(ResultStruct.ERROR);
                return Response.ok(result.toString()).build();
            }
            String url = String.format(Constant.UPDATE_EXPLORE_ACTIVITY_STATUS, aid);
            content = HttpRequest.sendPost(url, map);
            return Response.ok(content.toString()).build();
        } catch (Exception ex) {
            return SystemException.setResult(result, ex, logger);
        }
    }

    /**
     * 查询该图文所有信息
     * @param device 设备
     * @param cid 图文id
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     * @Date 2018/1/4
     */
    @GET
    @Produces({XXMediaType.TEXTUTF8})
    @Consumes({MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Path("/{device}/getSingleGraphic/{cid}/")
    public Response getSingleGraphic(
            @PathParam("device") String device,
            @PathParam("cid") String cid) {
        ResultStruct result = new ResultStruct();
        try {
            String content;
            String url = String.format(Constant.GET_SIGNLE_GRAPHIC, cid);
            content = HttpRequest.sendGet(url, null);
            JSONObject rjsonObject = new JSONObject();
            rjsonObject.put("status", "0");
            rjsonObject.put("msg", result.getMsg());
            rjsonObject.put("body", new ArrayList<>());
            try {
                rjsonObject = ResultStruct.jsonObject(rjsonObject, result, content, logger);
            } catch (Exception ex) {
                return SystemException.setResult(result, ex, logger);
            }
            return Response.ok(rjsonObject.toJSONString().toString()).build();
        } catch (Exception ex) {
            return SystemException.setResult(result, ex, logger);
        }
    }

}


