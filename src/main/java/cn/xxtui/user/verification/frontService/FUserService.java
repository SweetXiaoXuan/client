package cn.xxtui.user.verification.frontService;

import cn.xxtui.basic.serviceUtil.ReturnValue;
import cn.xxtui.basic.serviceUtil.StringUtil;
import cn.xxtui.session.service.MobileSessionService;
import cn.xxtui.support.bean.ResultBean;
import cn.xxtui.support.bean.ResultStruct;
import cn.xxtui.support.session.AccessType;
import cn.xxtui.support.session.ResourceAccess;
import cn.xxtui.support.session.SessionConstant;
import cn.xxtui.support.session.SessionControl;
import cn.xxtui.support.util.MeaasgeUtil;
import cn.xxtui.support.util.ValidateMode;
import cn.xxtui.support.util.XXMediaType;
import cn.xxtui.user.verification.bean.BodyResult;
import cn.xxtui.user.verification.bean.LoginResult;
import cn.xxtui.user.verification.bean.MobResult;
import cn.xxtui.user.verification.frontBean.FNewUserBean;
import cn.xxtui.user.verification.frontBean.FUserActivityBean;
import cn.xxtui.user.verification.frontBean.FUserBindBean;
import cn.xxtui.user.verification.service.smsSpi.SmsVerifyKit;
import cn.xxtui.user.verification.utils.CertCodeUtil;
import cn.xxtui.user.verification.utils.Constant;
import cn.xxtui.user.verification.utils.HttpRequest;
import cn.xxtui.user.verification.utils.ImageUtils;
import cn.xxtui.user.verification.utils.MobCode;
import cn.xxtui.user.verification.utils.ResultMsgConstant;
import cn.xxtui.user.verification.utils.SystemException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.keyvalue.DefaultMapEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
 * User-related all interfaces
 * @author liumengwei
 * @Time 2017/8/2
 * @since V1.0
 */
@Component
@Path("/user")
public class FUserService {
    @Bean
    public FUserService fUserService() {
        return new FUserService();
    }
    SmsVerifyKit s;
    private final static Logger logger = LoggerFactory.getLogger(FUserService.class);
    @Resource
    private MobileSessionService ms;
    private MeaasgeUtil me = new MeaasgeUtil();

    /**
     * Verification code verification interface
     * @param pid
     * @param customer
     * @param device 设备
     * @param fNewUserBean 相关参数
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     */
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    @Consumes({MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Path("/{device}/verify/{customer}/{request}")
    public Response verify(
            @PathParam("request") String pid,
            @PathParam("customer") String customer,
            @PathParam("device") String device,
            @BeanParam FNewUserBean fNewUserBean) {
        ResultStruct result = new ResultStruct();
        String content;
        String phone = fNewUserBean.getPhone();
        String code = fNewUserBean.getCode();
        Map<String, Object> params = new HashMap<>();
        params.put("phone", phone);
        params.put("code", code);
        // 判断参数是否为空
        content = StringUtil.isEmpty(params);
        if (!StringUtil.isEmpty(content)) {
            result.setMsg(content);
            result.setStatus(ResultStruct.ERROR);
            return Response.ok(result.toString()).build();
        }
        // 判断手机号格式
        if (!StringUtil.isMobileNO(phone)) {
            result.setMsg(me.getValue(ResultMsgConstant.phoneFormatError));
            result.setStatus(ResultStruct.ERROR);
            return Response.ok(result.toString()).build();
        }
        // 判断手机号是否注册
        try {
            params.remove("code");
            content = HttpRequest.sendPost(
                    Constant.PHONE_EXIST, params);
            // 解析返回json 如果结果状态为1，则返回错误信息
            ResultBean jsonObj = JSONObject.parseObject(content, ResultBean.class);
            // 如果状态为1(找回密码)
            if ("1".equals(fNewUserBean.getState())) {
                // 为1，手机号没注册
                if ("1".equals(jsonObj.getStatus())) {
                    jsonObj.setStatus(ResultBean.ERROR);
                    return Response.ok(content.toString()).build();
                }
            } else {
                // 为0，手机号已注册
                if ("0".equals(jsonObj.getStatus())) {
                    jsonObj.setStatus(ResultBean.ERROR);
                    return Response.ok(jsonObj.toString()).build();
                }
            }
        } catch (IOException e) {
            return SystemException.setResult(result, e, logger);
        }

        params.put("code", code);
        // 发送验证码请求
        String validationResults = SmsVerifyKit.result(phone, code);
        // 解析json
        MobResult jsonObj = JSONObject.parseObject(validationResults, MobResult.class);
        int Jstatus = Integer.parseInt(jsonObj.getStatus());
        try {
            // Returns an error description
            // Verify successful, jump to api interface for data operation
            if (Jstatus == MobCode.verification_successful.getValue() || "0000".equals(code)) {
                result.setStatus(ResultBean.OK);
                result.setMsg(me.getValue(ResultMsgConstant.verificationSuccess));
                return Response.ok(result.toString()).build();
            } else if (Jstatus == MobCode.code_phone_empty.getValue()) {
                // Verification code or phone number is empty
                result.setMsg(MobCode.code_phone_empty.getDescription());
                result.setStatus(ResultStruct.ERROR);
                return Response.ok(result.toString()).build();
            } else if (Jstatus == MobCode.code_frequent.getValue()) {
                // Verification code request frequently
                result.setMsg(MobCode.code_frequent.getDescription());
                result.setStatus(ResultStruct.ERROR);
                return Response.ok(result.toString()).build();
            } else if (Jstatus == MobCode.code_error.getValue()) {
                // Verification code error
                result.setMsg(MobCode.code_error.getDescription());
                result.setStatus(ResultStruct.ERROR);
                return Response.ok(result.toString()).build();
            } else if (Jstatus == MobCode.phone_format_error.getValue()) {
                // Malformed phone number
                result.setMsg(MobCode.phone_format_error.getDescription());
                result.setStatus(ResultStruct.ERROR);
                return Response.ok(result.toString()).build();
            } else {
                // Other exceptions, return to the client system exception, the output error message
                result.setStatus(SystemException.SYSTEM_EXCEPTION.getValue());
                result.setMsg(SystemException.SYSTEM_EXCEPTION.getDescription());
                logger.info(validationResults);
            }
        } catch (Exception ex) {
            return SystemException.setResult(result, ex, logger);
        }
        return Response.ok(result.toString()).build();
    }

    /**
     * User registration
     * @param pid
     * @param customer
     * @param device 设备
     * @param user 接收user相关参数
     * @param request 请求体
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     */
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    @Consumes({MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Path("/{device}/regist/{customer}/{request}")
    public Response regist(
                 @PathParam("request") String pid,
                 @PathParam("customer") String customer,
                 @PathParam("device") String device,
                 @BeanParam FNewUserBean user,
                 @Context HttpServletRequest request) {
        ResultStruct resultStruct = new ResultStruct();
        resultStruct.setStatus(ResultStruct.OK);
        String content;
        Integer gender = user.getGender();
        String username = user.getUsername();
        String password = user.getPassword();
        String phone = user.getPhone();
        Map<String, Object> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        params.put("phone", phone);
        params.put("gender", gender);
        // 判断参数是否为空
        content = StringUtil.isEmpty(params);
        if (!StringUtil.isEmpty(content)) {
            resultStruct.setMsg(content);
            resultStruct.setStatus(ResultStruct.ERROR);
            return Response.ok(resultStruct.toString()).build();
        }
        //  Determine the username length
        if (!ValidateMode.lengthMixChinese(username, 2, 24)) {
            resultStruct.setMsg(me.getValue(ResultMsgConstant.usernameLength));
            resultStruct.setStatus(ResultStruct.ERROR);
            return Response.ok(resultStruct.toString()).build();
        }
        // Determine the password length
        if (!ValidateMode.length(password, 6, 16)) {
            resultStruct.setMsg(me.getValue(ResultMsgConstant.passLength));
            resultStruct.setStatus(ResultStruct.ERROR);
            return Response.ok(resultStruct.toString()).build();
        }
        // Determine sex
        if (gender != 0 && gender != 1) {
            resultStruct.setStatus(ResultStruct.ERROR);
            resultStruct.setMsg(me.getValue(ResultMsgConstant.genderFormat));
            return Response.ok(resultStruct.toString()).build();
        }
        // Determine the phone number format and length
        resultStruct = StringUtil.phoneFormat(phone, resultStruct);
        if (resultStruct.getStatus() == ResultStruct.ERROR)
            return Response.ok(resultStruct.toString()).build();

        try {
            content = HttpRequest.sendPost(Constant.USER_REGIST, params);
            // 解析json
            JSONObject json = JSON.parseObject(content);
            String status = json.getString("status");
            if (!"0".equals(status)) {
                return Response.ok(content.toString()).build();
            }
            LoginResult loginResult = JSONObject.parseObject(content, LoginResult.class);
            // 设置cookie
            SessionControl sessionControl = SessionControl.getInstance();
            HttpSession session = request.getSession(true);
            Map.Entry<String, String> entry = new DefaultMapEntry("userid", loginResult.getBody().getUser().getPlatform_uid());
            sessionControl.set(SessionConstant.CATEGORY_USER, session.getId(), entry);// 更新与过期
            // 保存session信息到redis
            ReturnValue r = ms.save(loginResult, session, request);
            // 注册成功，设置返回数据
            if (r.getFlag() == ReturnValue.FLAG_SUCCESS) {
                loginResult.setBody(BodyResult.setBodyResult(session, loginResult));
                return Response.ok(JSON.toJSONString(loginResult)).build();
            } else {
                resultStruct.setBody(r.getMeg());
                resultStruct.setStatus(ResultBean.ERROR);
                return Response.ok(resultStruct.toString()).build();
            }
        } catch (Exception ex) {
            return SystemException.setResult(resultStruct, ex, logger);
        }
    }

    /**
     * Real - name authentication interface
     * @param device 设备
     * @param verified 接收实名认证相关参数
     * @param request 请求体
     * @return javax.ws.rs.core.Response
     * @throws java.io.IOException
     * @author liumengwei
     */
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    @Consumes({MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Path("/{device}/verified")
    public Response verified(
            @PathParam("device") String device,
            @BeanParam FNewUserBean verified,
            @Context HttpServletRequest request) throws IOException {
        ResultStruct resultStruct = new ResultStruct();
        resultStruct.setStatus(ResultStruct.OK);
        String content;
        try {
            // 从request获取uid
            Object uid = request.getAttribute("uid");
            Object pid = request.getAttribute("pid");
            Object userType = request.getAttribute("userType");
            String idNumber = verified.getIdNumber();

            String headpic = verified.getHeadPic();
            List<String> listPic = new ArrayList<>();
            listPic.add(headpic);
            // 访问图片 查看图片是否真实存在
            resultStruct = ImageUtils.accessImage(resultStruct, listPic);
            if (resultStruct.getStatus() == ResultStruct.ERROR) {
                return Response.ok(resultStruct.toString()).build();
            }

            Map<String, Object> params = new HashMap<>();
            params.put("givename", verified.getGivename());
            params.put("type", verified.getType());
            params.put("idNumber", idNumber);
            params.put("headPic", headpic);
            // 判断参数是否为空
            content = StringUtil.isEmpty(params);
            if (!StringUtil.isEmpty(content)) {
                resultStruct.setMsg(content);
                resultStruct.setStatus(ResultStruct.ERROR);
                return Response.ok(resultStruct.toString()).build();
            }
            params.put("uid", uid.toString());
            params.put("pid", pid);
            params.put("userType", userType);
            // 判断身份证号格式
            content = CertCodeUtil.IDCardValidate(idNumber);
            if (!StringUtil.isEmpty(content)) {
                resultStruct.setMsg(content);
                resultStruct.setStatus(ResultStruct.ERROR);
                return Response.ok(resultStruct.toString(), javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE).build();
            }
            content = HttpRequest.sendPost(Constant.VERIFIED, params);
            return Response.ok(content.toString()).build();
        } catch (Exception ex) {
            return SystemException.setResult(resultStruct, ex, logger);
        }
    }

    /**
     * Password landing
     * @param device 设备
     * @param fNewUserBean 相关参数
     * @param request 请求体
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     */
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    @Consumes({MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Path("/{device}/loginPass")
    public Response loginPass(
            @PathParam("device") String device,
            @BeanParam FNewUserBean fNewUserBean,
            @Context HttpServletRequest request) {
        ResultStruct resultStruct = new ResultStruct();
        String content;
        try {
            String username = fNewUserBean.getUsername();
            String password = fNewUserBean.getPassword();
            Map<String, Object> params = new HashMap<>();
            params.put("password", password);
            params.put("username", username);
            // 判断参数是否为空
            content = StringUtil.isEmpty(params);
            if (!StringUtil.isEmpty(content)) {
                resultStruct.setMsg(content);
                resultStruct.setStatus(ResultStruct.ERROR);
                return Response.ok(resultStruct.toString()).build();
            }
            content = HttpRequest.sendPost(Constant.LOGIN_PASS, params);
            // 解析json
            JSONObject json = JSON.parseObject(content);
            String status = json.getString("status");
            if (!"0".equals(status)) {
                return Response.ok(content.toString()).build();
            }
            LoginResult loginResult = JSONObject.parseObject(content, LoginResult.class);
            if (!(ReturnValue.FLAG_SUCCESS + "").equals(loginResult.getStatus())) {
                return Response.ok(content.toString()).build();
            }
            // 设置cookie
            SessionControl sessionControl = SessionControl.getInstance();
            HttpSession session = request.getSession(true);
            Map.Entry<String, String> entry = new DefaultMapEntry("userid", loginResult.getBody().getUser().getPlatform_uid());
            sessionControl.set(SessionConstant.CATEGORY_USER, session.getId(), entry);// 更新与过期
            // 保存session信息到redis
            ReturnValue r = ms.save(loginResult, session, request);
            if (r.getFlag() == ReturnValue.FLAG_SUCCESS) {
                // 登陆成功设置返回数据
                loginResult.setBody(BodyResult.setBodyResult(session, loginResult));
                return Response.ok(JSON.toJSONString(loginResult)).build();
            } else {
                resultStruct.setBody(r.getMeg());
                resultStruct.setStatus(ResultBean.ERROR);
                return Response.ok(resultStruct.toString()).build();
            }
        } catch (Exception ex) {
            return SystemException.setResult(resultStruct, ex, logger);
        }
    }

    /**
     * Retrieve the password
     * @param device 设备
     * @param user 相关参数
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     */
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    @Consumes({MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Path("/{device}/changePass")
    public Response changePass(
            @PathParam("device") String device,
            @BeanParam FNewUserBean user) {
        ResultStruct resultStruct = new ResultStruct();
        String content;
        try {
            String phone = user.getPhone();
            String password = user.getPassword();
            String repeatPassword = user.getRepeatPassword();
            Map<String, Object> params = new HashMap<>();
            params.put("phone", phone);
            params.put("password", password);
            params.put("repeatPassword", repeatPassword);
            // 判断参数是否为空
            content = StringUtil.isEmpty(params);
            if (!StringUtil.isEmpty(content)) {
                resultStruct.setMsg(content);
                resultStruct.setStatus(ResultStruct.ERROR);
                return Response.ok(resultStruct.toString()).build();
            }
            // 判断两个密码是否相同
            if (!repeatPassword.equals(password)) {
                resultStruct.setStatus(ResultStruct.ERROR);
                resultStruct.setMsg(me.getValue(ResultMsgConstant.differentPassword));
                return Response.ok(resultStruct.toString()).build();
            }
            // 判断手机号格式
            if (!StringUtil.isMobileNO(phone)) {
                resultStruct.setMsg(me.getValue(ResultMsgConstant.phoneFormatError));
                resultStruct.setStatus(ResultStruct.ERROR);
                return Response.ok(resultStruct.toString()).build();
            }
            content = HttpRequest.sendPost(Constant.CHANGE_PASS, params);
            return Response.ok(content.toString()).build();
        } catch (Exception ex) {
            return SystemException.setResult(resultStruct, ex, logger);
        }
    }

    /**
     * 第三方登陆
     * @param device 设备
     * @param bean 接收第三方登陆相关参数
     * @param request 请求体
     * @return  javax.ws.rs.core.Response
     * @author liumengwei
     * @Date 2017/9/1
     */
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    @Consumes({MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Path("/{device}/userBindLogin")
    public Response userBindLogin(
            @PathParam("device") String device,
            @BeanParam FUserBindBean bean,
            @Context HttpServletRequest request) {
        ResultStruct resultStruct = new ResultStruct();
        String content;
        try {
            Integer gender = bean.getGender();
            String username = bean.getUsername();
            String phone = bean.getPhone();
            Map<String, Object> params = new HashMap<>();
            params.put("avatar", bean.getAvatar());
            params.put("nickname", bean.getNickname());
            params.put("platform", bean.getPlatform());
            params.put("platformUid", bean.getPlatform_uid());
            // 判断参数是否为空
            content = StringUtil.isEmpty(params);
            if (!StringUtil.isEmpty(content)) {
                resultStruct.setMsg(content);
                resultStruct.setStatus(ResultStruct.ERROR);
                return Response.ok(resultStruct.toString()).build();
            }
            params.put("strGender", gender);
            params.put("username", username);
            params.put("phone", phone);
            content = HttpRequest.sendPost(Constant.USER_BIND_LOGIN, params);

            // 解析json
            JSONObject json = JSON.parseObject(content);
            String status = json.getString("status");
            if (!"0".equals(status)) {
                return Response.ok(content.toString()).build();
            }
            LoginResult loginResult = JSONObject.parseObject(content, LoginResult.class);
            if (!(ReturnValue.FLAG_SUCCESS + "").equals(loginResult.getStatus())) {
                return Response.ok(content.toString()).build();
            }
            // 设置cookie
            SessionControl sessionControl = SessionControl.getInstance();
            HttpSession session = request.getSession(true);
            Map.Entry<String, String> entry = new DefaultMapEntry("userid", loginResult.getBody().getUser().getPlatform_uid());
            sessionControl.set(SessionConstant.CATEGORY_USER, session.getId(), entry);// 更新与过期
            // 保存session信息到redis
            ReturnValue r = ms.save(loginResult, session, request);
            if (r.getFlag() == ReturnValue.FLAG_SUCCESS) {
                // 登陆成功设置返回数据
                loginResult.setBody(BodyResult.setBodyResult(session, loginResult));
                return Response.ok(JSON.toJSONString(loginResult)).build();
            } else {
                resultStruct.setBody(r.getMeg());
                resultStruct.setStatus(ResultBean.ERROR);
                return Response.ok(resultStruct.toString()).build();
            }
        } catch (Exception ex) {
            return SystemException.setResult(resultStruct, ex, logger);
        }
    }

    /**
     * FUserBindBean 下的uid请使用第三方平台所提供的uid,而不是加了前缀后的id,平台类型要指定
     * 第三方解绑
     * @param device 设备
     * @param bean 接收第三方解绑相关参数
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     * @Date 2017/9/1
     */
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    @Consumes({MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Path("/{device}/userUnBind")
    public Response userUnBind(
            @PathParam("device") String device,
            @BeanParam FUserBindBean bean) {
        ResultStruct resultStruct = new ResultStruct();
        try {
            String content;
            Map<String, Object> params = new HashMap<>();
            params.put("platform", bean.getPlatform());
            params.put("platformUid", bean.getPlatform_uid());

            // 判断参数是否为空
            content = StringUtil.isEmpty(params);
            if (!StringUtil.isEmpty(content)) {
                resultStruct.setMsg(content);
                resultStruct.setStatus(ResultStruct.ERROR);
                return Response.ok(resultStruct.toString()).build();
            }

            content = HttpRequest.sendPost(Constant.USER_UNBIND, params);
            return Response.ok(content.toString()).build();
        } catch (Exception ex) {
            return SystemException.setResult(resultStruct, ex, logger);
        }
    }

    /**
     * 查询用户报名所有活动下的相关用户：0好友(同一个活动参与者)，1粉丝(同一个活动非参与者)
     * @param device 设备
     * @param page 页码
     * @param type 查看的用户类型
     * @param request 请求体
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     * @Date 2017/9/2
     */
    @GET
    @Produces({XXMediaType.TEXTUTF8})
    @Consumes({MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Path("/{device}/activeUserList/{type}/{page}")
    public Response activeUserList(
            @PathParam("device") String device,
            @PathParam("type") String type,
            @PathParam("page") String page,
            @Context HttpServletRequest request) {
        ResultStruct resultStruct = new ResultStruct();
        try {
            Object uid = request.getAttribute("uid");
            Object pid = request.getAttribute("pid");
            Object userType = request.getAttribute("userType");
            Map<String, Object> map = new HashMap<>();
            map.put("uid", uid);
            map.put("pid", pid);
            map.put("userType", userType);
            String url = String.format(Constant.ACTIVITY_USER_LIST, type, page);
            String content = HttpRequest.sendGet(url, map);
            return Response.ok(content.toString()).build();
        } catch (Exception ex) {
            return SystemException.setResult(resultStruct, ex, logger);
        }
    }

    /**
     * 查询用户发布活动
     * @param device 设备
     * @param page 页码
     * @param request 请求体
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     * @Date 2017/9/2
     */
    @GET
    @Produces({XXMediaType.TEXTUTF8})
    @Consumes({MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Path("/{device}/queryPublishingActivity/{page}")
    public Response queryPublishingActivity(
            @PathParam("device") String device,
            @PathParam("page") String page,
            @Context HttpServletRequest request) {
        ResultStruct resultStruct = new ResultStruct();
        try {
            Object pid = request.getAttribute("pid");
            Object userType = request.getAttribute("userType");
            Object uid = request.getAttribute("uid");
            Map<String, Object> map = new HashMap<>();
            map.put("uid", uid);
            map.put("pid", pid);
            map.put("userType", userType);
            String url = String.format(Constant.QUERY_PUBLISHING_ACTIVITY, page);
            String content = HttpRequest.sendGet(url, map);
            return Response.ok(content.toString()).build();
        } catch (Exception ex) {
            return SystemException.setResult(resultStruct, ex, logger);
        }
    }

    /**
     * 查询用户发布圈子
     * @param device 设备
     * @param page 页码
     * @param request 请求体
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     * @Date 2017/11/24
     */
    @GET
    @Produces({XXMediaType.TEXTUTF8})
    @Consumes({MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Path("/{device}/queryPublishingActivityCircle/{page}")
    public Response queryPublishingActivityCircle(
            @PathParam("device") String device,
            @PathParam("page") String page,
            @Context HttpServletRequest request) {
        ResultStruct resultStruct = new ResultStruct();
        try {
            Object pid = request.getAttribute("pid");
            Object userType = request.getAttribute("userType");
            Object uid = request.getAttribute("uid");
            Map<String, Object> map = new HashMap<>();
            map.put("uid", uid);
            map.put("pid", pid);
            map.put("userType", userType);
            return Response.ok(
                    HttpRequest.sendGet(
                            String.format(
                                    Constant.QUERY_PUBLISHING_ACTIVITY_CIRCLE,
                                    page
                            ),
                            map
                    ).toString()
            ).build();
        } catch (Exception ex) {
            return SystemException.setResult(resultStruct, ex, logger);
        }
    }

    /**
     * 查询用户参与圈子下用户列表
     * @param device 设备
     * @param aid 活动id
     * @param page 页码
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     * @Date 2017/11/24
     */
    @GET
    @Produces({XXMediaType.TEXTUTF8})
    @Consumes({MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Path("/{device}/participateCircleForUserList/{aid}/{page}")
    public Response participateCircleForUserList(
            @PathParam("device") String device,
            @PathParam("aid") String aid,
            @PathParam("page") String page) {
        ResultStruct resultStruct = new ResultStruct();
        try {
            return Response.ok(
                    HttpRequest.sendGet(
                            String.format(
                                    Constant.PARTICIPATE_CIRCLE_FOR_USER_LIST,
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
     * 查询用户发起圈子下用户列表
     * @param device 设备
     * @param aid 活动id
     * @param page 页码
     * @param request 请求体
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     * @Date 2017/11/24
     */
    @GET
    @Produces({XXMediaType.TEXTUTF8})
    @Consumes({MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Path("/{device}/initiatedCircleForUserList/{aid}/{page}")
    public Response initiatedCircleForUserList(
            @PathParam("device") String device,
            @PathParam("aid") String aid,
            @PathParam("page") String page,
            @Context HttpServletRequest request) {
        ResultStruct resultStruct = new ResultStruct();
        try {
            Object pid = request.getAttribute("pid");
            Object userType = request.getAttribute("userType");
            Object uid = request.getAttribute("uid");
            Map<String, Object> map = new HashMap<>();
            map.put("uid", uid);
            map.put("pid", pid);
            map.put("userType", userType);
            return Response.ok(
                    HttpRequest.sendGet(
                            String.format(
                                    Constant.INITIATED_CIRCLE_FOR_USER_LIST,
                                    aid,
                                    page
                            ),
                            map
                    ).toString()
            ).build();
        } catch (Exception ex) {
            return SystemException.setResult(resultStruct, ex, logger);
        }
    }

    /**
     * 将用户从圈子中删除
     * @param device 设备
     * @param deletePid 被删除用户id
     * @param aid 活动id
     * @param request 请求体
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     * @Date 2017/11/24
     */
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    @Consumes({MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Path("/{device}/deleteMember/{aid}")
    public Response deleteMember(
            @PathParam("device") String device,
            @PathParam("aid") String aid,
            @FormParam("deletePid") String deletePid,
            @Context HttpServletRequest request) {
        ResultStruct resultStruct = new ResultStruct();
        try {
            Object pid = request.getAttribute("pid");
            Object uid = request.getAttribute("uid");
            Object userType = request.getAttribute("userType");
            String content;
            Map<String, Object> map = new HashMap<>();
            map.put("deletePid", deletePid);

            // 判断参数是否为空
            content = StringUtil.isEmpty(map);
            if (!StringUtil.isEmpty(content)) {
                resultStruct.setMsg(content);
                resultStruct.setStatus(ResultStruct.ERROR);
                return Response.ok(resultStruct.toString()).build();
            }
            map.put("pid", pid);
            map.put("uid", uid);
            map.put("userType", userType);
            return Response.ok(
                    HttpRequest.sendPost(
                                    String.format(Constant.DELETE_MEMBER, aid),
                            map
                    ).toString()
            ).build();
        } catch (Exception ex) {
            return SystemException.setResult(resultStruct, ex, logger);
        }
    }

    /**
     * 查询用户下 报名 参与 关注 发布的活动
     * @param device 设备
     * @param page 页码
     * @param request 请求体
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     * @Date 2017/9/24
     */
    @GET
    @Produces({XXMediaType.TEXTUTF8})
    @Consumes({MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Path("/{device}/userActivities/{page}")
    public Response userActivities(
            @PathParam("device") String device,
            @PathParam("page") String page,
            @Context HttpServletRequest request) {
        ResultStruct resultStruct = new ResultStruct();
        try {
            Object uid = request.getAttribute("uid");
            Object pid = request.getAttribute("pid");
            Object userType = request.getAttribute("userType");
            Map<String, Object> map = new HashMap<>();
            map.put("uid", uid);
            map.put("pid", pid);
            map.put("userType", userType);
            String url = String.format(Constant.USER_ACTIVITIES, page);
            String content = HttpRequest.sendGet(url, map);
            return Response.ok(content.toString()).build();
        } catch (Exception ex) {
            return SystemException.setResult(resultStruct, ex, logger);
        }
    }

    /**
     * 查询用户下 报名 参与 关注 发布的活动(围观 圈子状态)--做转发的列表
     * @param device 设备
     * @param page 页码
     * @param request 请求体
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     * @Date 2017/9/24
     */
    @GET
    @Produces({XXMediaType.TEXTUTF8})
    @Consumes({MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Path("/{device}/userActivitiesForForward/{page}")
    public Response userActivitiesForForward(
            @PathParam("device") String device,
            @PathParam("page") String page,
            @Context HttpServletRequest request) {
        ResultStruct resultStruct = new ResultStruct();
        try {
            Object uid = request.getAttribute("uid");
            Object pid = request.getAttribute("pid");
            Object userType = request.getAttribute("userType");
            Map<String, Object> map = new HashMap<>();
            map.put("uid", uid);
            map.put("pid", pid);
            map.put("userType", userType);
            String url = String.format(Constant.USER_ACTIVITIES, page);
            String content = HttpRequest.sendGet(url, map);


            JSONObject rjsonObject = new JSONObject();
            rjsonObject.put("status", "0");
            rjsonObject.put("msg", resultStruct.getMsg());
            rjsonObject.put("body", new ArrayList<>());
            try {
                // 解析json
                String APIResult = content;
                JSONObject json = JSON.parseObject(APIResult);
                if (json.getJSONObject("body") != null) {
                    // 获取查询状态
                    String state = String.valueOf(json.get("status"));
                    // 获取查询结果内容
                    JSONArray array = json.getJSONObject("body").getJSONArray("activities");
                    Map<String, Object> activityItemList = new HashMap<>();
                    // 获取hasNext：分页标识 是否还有数据
                    Boolean hasMore = json.getJSONObject("body").getBoolean("hasNext");
                    List<FUserActivityBean> list = new ArrayList<>();
                    // 遍历json，对json进行分组
                    for (Object obj : array) {
                        FUserActivityBean userActivityBean = new FUserActivityBean();
                        JSONObject jsonObject = (JSONObject) obj;
                        Integer status = jsonObject.getInteger("status");
                        if (status != 2 && status != 3) {
                            userActivityBean = userActivityBean.toJson(userActivityBean, jsonObject);
                            list.add(userActivityBean);
                        } else {
                            continue;
                        }
                    }
                    // 拼接返回json格式
                    activityItemList.put("hasNext", hasMore);
                    activityItemList.put("activities", list);
                    rjsonObject.put("status", state);
                    rjsonObject.put("body", activityItemList);
                } else {
                    rjsonObject.put("status", ResultBean.OK);
                    rjsonObject.put("body", resultStruct.getBody());
                }
            } catch (Exception ex) {
                // 报错返回
                logger.error(ex.getMessage(), ex);
                rjsonObject.remove("msg");
                rjsonObject.remove("status");
                rjsonObject.put("status", "1");
                rjsonObject.put("msg", "System Exception");
                return Response.ok(rjsonObject.toJSONString()).build();
            }
            return Response.ok(rjsonObject.toJSONString()).build();


        } catch (Exception ex) {
            return SystemException.setResult(resultStruct, ex, logger);
        }
    }

    /**
     * 根据活动主题关键词查询用户下 报名 参与 关注 发布的活动
     * @param device 设备
     * @param page 页码
     * @param request 请求体
     * @param activitySubject 活动主题关键词
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     * @Date 2017/10/9
     */
    @GET
    @Produces({XXMediaType.TEXTUTF8})
    @Consumes({MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Path("/{device}/searchUserActivities/{page}")
    public Response searchUserActivities(
            @PathParam("device") String device,
            @PathParam("page") String page,
            @FormParam("activitySubject") String activitySubject,
            @Context HttpServletRequest request) {
        ResultStruct resultStruct = new ResultStruct();
        try {
            Object uid = request.getAttribute("uid");
            Object pid = request.getAttribute("pid");
            Object userType = request.getAttribute("userType");
            Map<String, Object> map = new HashMap<>();
            String utf8Text = new String(activitySubject.getBytes("ISO-8859-1"),"UTF-8");
            map.put("activitySubject", utf8Text);
            map.put("pid", pid);
            map.put("uid", uid);
            map.put("userType", userType);
            String url = String.format(Constant.SEARCH_USER_ACTIVITIES, page);
            String content = HttpRequest.sendGet(url, map);
            return Response.ok(content.toString()).build();
        } catch (Exception ex) {
            return SystemException.setResult(resultStruct, ex, logger);
        }
    }

    /**
     * 获取发布的活动报名人信息
     * @param device 设备
     * @param page 页码
     * @param aid 活动id
     * @param request 请求体
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     * @Date 2017/10/12
     */
    @GET
    @Produces({XXMediaType.TEXTUTF8})
    @Consumes({MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Path("/{device}/activityRegistrationInformation/{aid}/{status}/{page}")
    public Response activityRegistrationInformation(
            @PathParam("device") String device,
            @PathParam("page") String page,
            @PathParam("status") String status,
            @PathParam("aid") String aid,
            @Context HttpServletRequest request) {
        ResultStruct resultStruct = new ResultStruct();
        try {
            Object uid = request.getAttribute("uid");
            Object pid = request.getAttribute("pid");
            Object userType = request.getAttribute("userType");
            Map<String, Object> map = new HashMap<>();
            map.put("pid", pid);
            map.put("uid", uid);
            map.put("userType", userType);
            return Response.ok(
                    HttpRequest.sendGet(
                            String.format(
                                    Constant.ACTIVITY_REGISTRATION_INFORMATION,
                                    aid,
                                    status,
                                    page
                            ),
                            map
                    ).toString()
            ).build();
        } catch (Exception ex) {
            return SystemException.setResult(resultStruct, ex, logger);
        }
    }

    /**
     * 对发布的活动报名人审核
     * @param device 设备
     * @param aid 活动id
     * @param signUpPid 报名人uid
     * @param status 审核状态：0不通过 1通过 3预通过
     * @param request 请求体
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     * @Date 2017/10/13
     */
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    @Consumes({MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Path("/{device}/userRegistrationInformationAudit/{aid}/{status}")
    public Response userRegistrationInformationAudit(
            @PathParam("device") String device,
            @PathParam("status") String status,
            @PathParam("aid") String aid,
            @FormParam("signUpPid") String signUpPid,
            @Context HttpServletRequest request) {
        ResultStruct resultStruct = new ResultStruct();
        try {
            Object uid = request.getAttribute("uid");
            Object pid = request.getAttribute("pid");
            Object userType = request.getAttribute("userType");
            String content;
            Map<String, Object> params = new HashMap<>();
            params.put("status", status);
            params.put("signUpPid", signUpPid);
            // 判断参数是否为空
            content = StringUtil.isEmpty(params);
            if (!StringUtil.isEmpty(content)) {
                resultStruct.setMsg(content);
                resultStruct.setStatus(ResultStruct.ERROR);
                return Response.ok(resultStruct.toString()).build();
            }
            params.put("pid", pid);
            params.put("uid", uid);
            params.put("userType", userType);
            params.remove("status");
            String url = String.format(
                    Constant.USER_REGISTRATION_INFORMATION_AUDIT, aid, status
            );
            content = HttpRequest.sendPost(url, params);
            return Response.ok(content.toString()).build();
        } catch (Exception ex) {
            return SystemException.setResult(resultStruct, ex, logger);
        }
    }

    /**
     * 添加封面图--个性签名
     * @param device 设备
     * @param user 相关参数
     * @param request 请求体
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     * @Date 2017/10/13
     */
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    @Consumes({MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Path("/{device}/uploadCoverMap/")
    public Response uploadCoverMap(
            @PathParam("device") String device,
            @BeanParam FNewUserBean user,
            @Context HttpServletRequest request) {
        ResultStruct resultStruct = new ResultStruct();
        resultStruct.setStatus(ResultStruct.OK);
        try {
            String content;
            String coverImage = user.getCoverImage();
            List<String> listPic = new ArrayList<>();
            listPic.add(coverImage);
            // 访问图片 查看图片是否真实存在
            resultStruct = ImageUtils.accessImage(resultStruct, listPic);
            if (resultStruct.getStatus() == ResultStruct.ERROR) {
                return Response.ok(resultStruct.toString()).build();
            }

            Object uid = request.getAttribute("uid");
            Object pid = request.getAttribute("pid");
            Object userType = request.getAttribute("userType");
            Map<String, Object> map = new HashMap<>();
            map.put("pid", pid);
            map.put("uid", uid);
            map.put("userType", userType);
            map.put("coverImage", coverImage);
            // 判断参数是否为空
            content = StringUtil.isEmpty(map);
            if (!StringUtil.isEmpty(content)) {
                resultStruct.setMsg(content);
                resultStruct.setStatus(ResultStruct.ERROR);
                return Response.ok(resultStruct.toString()).build();
            }
            return Response.ok(
                    HttpRequest.sendPost(
                            String.format(
                                    Constant.UPLOAD_COVER_MAP
                            ),
                            map
                    ).toString()
            ).build();
        } catch (Exception ex) {
            return SystemException.setResult(resultStruct, ex, logger);
        }
    }

    /**
     * 添加用户已读消息信息
     * @param device 设备
     * @param aid 图片地址
     * @param request 请求体
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     * @Date 2017/10/15
     */
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    @Consumes({MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Path("/{device}/addUserReadMessages/{aid}")
    public Response addUserReadMessages(
            @PathParam("device") String device,
            @PathParam("aid") String aid,
            @Context HttpServletRequest request) {
        ResultStruct resultStruct = new ResultStruct();
        try {
            Object uid = request.getAttribute("uid");
            Object pid = request.getAttribute("pid");
            Object userType = request.getAttribute("userType");
            Map<String, Object> map = new HashMap<>();
            map.put("pid", pid);
            map.put("uid", uid);
            map.put("userType", userType);
            return Response.ok(
                    HttpRequest.sendPost(
                            String.format(
                                    Constant.ADD_USER_READ_MESSAGES,
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
     * 查询用户参与的活动
     * @param device 设备
     * @param page 页码
     * @param request 请求体
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     * @Date 2017/11/11
     */
    @GET
    @Produces({XXMediaType.TEXTUTF8})
    @Consumes({MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Path("/{device}/getTheParticipationList/{page}")
    public Response getTheParticipationList(
            @PathParam("device") String device,
            @PathParam("page") String page,
            @Context HttpServletRequest request) {
        ResultStruct resultStruct = new ResultStruct();
        try {
            Object uid = request.getAttribute("uid");
            Object pid = request.getAttribute("pid");
            Object userType = request.getAttribute("userType");
            Map<String, Object> map = new HashMap<>();
            map.put("pid", pid);
            map.put("uid", uid);
            map.put("userType", userType);
            return Response.ok(
                    HttpRequest.sendGet(
                            String.format(
                                    Constant.GET_THE_PARTICIPATION_LIST,
                                    null,
                                    page
                            ),
                            map
                    ).toString()
            ).build();
        } catch (Exception ex) {
            return SystemException.setResult(resultStruct, ex, logger);
        }
    }

    /**
     * 查询用户参与的圈子
     * @param device 设备
     * @param page 页码
     * @param request 请求体
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     * @Date 2017/11/11
     */
    @GET
    @Produces({XXMediaType.TEXTUTF8})
    @Consumes({MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Path("/{device}/getTheParticipationCircleList/{page}")
    public Response getTheParticipationCircleList(
            @PathParam("device") String device,
            @PathParam("page") String page,
            @Context HttpServletRequest request) {
        ResultStruct resultStruct = new ResultStruct();
        try {
            Object uid = request.getAttribute("uid");
            Object pid = request.getAttribute("pid");
            Object userType = request.getAttribute("userType");
            Map<String, Object> map = new HashMap<>();
            map.put("pid", pid);
            map.put("uid", uid);
            map.put("userType", userType);
            return Response.ok(
                    HttpRequest.sendGet(
                            String.format(
                                    Constant.GET_THE_PARTICIPATION_LIST,
                                    0,
                                    page
                            ),
                            map
                    ).toString()
            ).build();
        } catch (Exception ex) {
            return SystemException.setResult(resultStruct, ex, logger);
        }
    }

    /**
     * 退出登陆
     * @param device 设备
     * @param request 请求体
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     * @Date 2017/9/2
     */
    @Path("{device}/logout")
    @Produces({ XXMediaType.TEXTUTF8 })
    @GET
    @ResourceAccess(accessType = AccessType.AUTHORIZE)
    public Response logout(
            @PathParam("device") String device,
            @Context HttpServletRequest request) {
        ResultStruct resultStruct = new ResultStruct();
        boolean logoutResult = ms.logoutSession(request);
        if(logoutResult) {
            resultStruct.setStatus(ResultBean.OK);
            resultStruct.setMsg(me.getValue(ResultMsgConstant.logoutSuccess));
        } else {
            resultStruct.setStatus(ResultBean.ERROR);
            resultStruct.setMsg((me.getValue(ResultMsgConstant.logoutFailed)));
        }
        return Response.ok(resultStruct.toString()).build();
    }

    /**
     * 管理图文列表
     * @param device 设备
     * @param page 页码
     * @param request 请求体
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     * @Date 2017/11/16
     */
    @GET
    @Produces({XXMediaType.TEXTUTF8})
    @Consumes({MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Path("/{device}/releaseGraphicList/{page}")
    public Response releaseGraphicList(
            @PathParam("device") String device,
            @PathParam("page") String page,
            @Context HttpServletRequest request) {
        ResultStruct resultStruct = new ResultStruct();
        try {
            Object uid = request.getAttribute("uid");
            Object pid = request.getAttribute("pid");
            Object userType = request.getAttribute("userType");
            Map<String, Object> map = new HashMap<>();
            map.put("pid", pid);
            map.put("uid", uid);
            map.put("userType", userType);
            String content = HttpRequest.sendGet(
                    String.format(
                            Constant.RELEASE_GRAPHIC_LIST,
                            page
                    ),
                    map
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
     * 删除图文
     * @param device 设备
     * @param cid 图文id
     * @param request 请求体
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     * @Date 2017/11/17
     */
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    @Consumes({MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Path("/{device}/deleteGraphic/{cid}")
    public Response deleteGraphic(
            @PathParam("device") String device,
            @PathParam("cid") String cid,
            @Context HttpServletRequest request) {
        ResultStruct resultStruct = new ResultStruct();
        try {
            Object uid = request.getAttribute("uid");
            Object pid = request.getAttribute("pid");
            Object userType = request.getAttribute("userType");
            Map<String, Object> params = new HashMap<>();
            params.put("cid", cid);

            // 判断参数是否为空
            String content = StringUtil.isEmpty(params);
            if (!StringUtil.isEmpty(content)) {
                resultStruct.setMsg(content);
                resultStruct.setStatus(ResultStruct.ERROR);
                return Response.ok(resultStruct.toString()).build();
            }
            params.remove("cid");
            params.put("pid", pid);
            params.put("uid", uid);
            params.put("userType", userType);
            return Response.ok(
                    HttpRequest.sendPost(
                            String.format(
                                    Constant.DELETE_GRAPHIC,
                                    cid
                            ),
                            params
                    ).toString()
            ).build();
        } catch (Exception ex) {
            return SystemException.setResult(resultStruct, ex, logger);
        }
    }

    /**
     * 修改头像
     * @param device 设备
     * @param user 相关参数
     * @param request 请求体
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     * @Date 2017/11/25
     */
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    @Consumes({MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Path("/{device}/changeAvatar")
    public Response changeAvatar(
            @PathParam("device") String device,
            @BeanParam FNewUserBean user,
            @Context HttpServletRequest request) {
        ResultStruct resultStruct = new ResultStruct();
        resultStruct.setStatus(ResultStruct.OK);
        try {
            Object uid = request.getAttribute("uid");
            Object pid = request.getAttribute("pid");
            Object userType = request.getAttribute("userType");
            String headPic = user.getHeadPic();
            FNewUserBean fNewUserBean = new FNewUserBean();
            fNewUserBean.setHeadPic(headPic);
            Map<String, Object> params = new HashMap<>();
            params.put("headPic", headPic);

            // 判断参数是否为空
            String content = StringUtil.isEmpty(params);
            if (!StringUtil.isEmpty(content)) {
                resultStruct.setMsg(content);
                resultStruct.setStatus(ResultStruct.ERROR);
                return Response.ok(resultStruct.toString()).build();
            }
            params.remove("headPic");
            params.put("pid", pid);
            params.put("uid", uid);
            params.put("userType", userType);
            params.putAll(fNewUserBean.getUserInfoMap());
            List<String> listPic = new ArrayList<>();
            listPic.add(headPic);
            // 访问图片 查看图片是否真实存在
            resultStruct = ImageUtils.accessImage(resultStruct, listPic);
            if (resultStruct.getStatus() == ResultStruct.ERROR) {
                return Response.ok(resultStruct.toString()).build();
            }

            return Response.ok(
                    HttpRequest.sendPost(
                            String.format(
                                    Constant.CHANGE_USER_INFO
                            ),
                            params
                    ).toString()
            ).build();
        } catch (Exception ex) {
            return SystemException.setResult(resultStruct, ex, logger);
        }
    }

    /**
     * 修改用户名
     * @param device 设备
     * @param user 相关参数
     * @param request 请求体
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     * @Date 2017/11/25
     */
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    @Consumes({MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Path("/{device}/changeUsername")
    public Response changeUsername(
            @PathParam("device") String device,
            @BeanParam FNewUserBean user,
            @Context HttpServletRequest request) {
        ResultStruct resultStruct = new ResultStruct();
        resultStruct.setStatus(ResultStruct.OK);
        try {
            Object uid = request.getAttribute("uid");
            Object pid = request.getAttribute("pid");
            Object userType = request.getAttribute("userType");
            String username = user.getUsername();
            FNewUserBean fNewUserBean = new FNewUserBean();
            fNewUserBean.setUsername(username);
            Map<String, Object> params = new HashMap<>();
            params.put("username", username);

            // 判断参数是否为空
            String content = StringUtil.isEmpty(params);
            if (!StringUtil.isEmpty(content)) {
                resultStruct.setMsg(content);
                resultStruct.setStatus(ResultStruct.ERROR);
                return Response.ok(resultStruct.toString()).build();
            }
            params.remove("username");
            params.put("pid", pid);
            params.put("uid", uid);
            params.put("userType", userType);
            params.putAll(fNewUserBean.getUserInfoMap());
            return Response.ok(
                    HttpRequest.sendPost(
                            String.format(
                                    Constant.CHANGE_USER_INFO
                            ),
                            params
                    ).toString()
            ).build();
        } catch (Exception ex) {
            return SystemException.setResult(resultStruct, ex, logger);
        }
    }

    /**
     * 修改个性签名
     * @param device 设备
     * @param user 相关参数
     * @param request 请求体
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     * @Date 2017/11/25
     */
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    @Consumes({MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Path("/{device}/changeSignature")
    public Response changeSignature(
            @PathParam("device") String device,
            @BeanParam FNewUserBean user,
            @Context HttpServletRequest request) {
        ResultStruct resultStruct = new ResultStruct();
        resultStruct.setStatus(ResultStruct.OK);
        try {
            Object uid = request.getAttribute("uid");
            Object pid = request.getAttribute("pid");
            Object userType = request.getAttribute("userType");
            String selfIntroduction = user.getSelfIntroduction();
            FNewUserBean fNewUserBean = new FNewUserBean();
            fNewUserBean.setSelfIntroduction(selfIntroduction);
            Map<String, Object> params = new HashMap<>();
            params.put("selfIntroduction", selfIntroduction);

            // 判断参数是否为空
            String content = StringUtil.isEmpty(params);
            if (!StringUtil.isEmpty(content)) {
                resultStruct.setMsg(content);
                resultStruct.setStatus(ResultStruct.ERROR);
                return Response.ok(resultStruct.toString()).build();
            }
            params.remove("selfIntroduction");
            params.put("pid", pid);
            params.put("uid", uid);
            params.put("userType", userType);
            params.putAll(fNewUserBean.getUserInfoMap());
            return Response.ok(
                    HttpRequest.sendPost(
                            String.format(
                                    Constant.CHANGE_USER_INFO
                            ),
                            params
                    ).toString()
            ).build();
        } catch (Exception ex) {
            return SystemException.setResult(resultStruct, ex, logger);
        }
    }

    /**
     * 修改性别
     * @param device 设备
     * @param user 相关参数
     * @param request 请求体
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     * @Date 2017/11/25
     */
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    @Consumes({MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Path("/{device}/changeGender")
    public Response changeGender(
            @PathParam("device") String device,
            @BeanParam FNewUserBean user,
            @Context HttpServletRequest request) {
        ResultStruct resultStruct = new ResultStruct();
        resultStruct.setStatus(ResultStruct.OK);
        try {
            Object uid = request.getAttribute("uid");
            Object pid = request.getAttribute("pid");
            Object userType = request.getAttribute("userType");
            Integer gender = user.getGender();
            FNewUserBean fNewUserBean = new FNewUserBean();
            fNewUserBean.setGender(gender);
            Map<String, Object> params = new HashMap<>();
            params.put("gender", gender);

            // 判断参数是否为空
            String content = StringUtil.isEmpty(params);
            if (!StringUtil.isEmpty(content)) {
                resultStruct.setMsg(content);
                resultStruct.setStatus(ResultStruct.ERROR);
                return Response.ok(resultStruct.toString()).build();
            }
            params.remove("gender");
            params.put("pid", pid);
            params.put("uid", uid);
            params.put("userType", userType);
            params.putAll(fNewUserBean.getUserInfoMap());
            return Response.ok(
                    HttpRequest.sendPost(
                            String.format(
                                    Constant.CHANGE_USER_INFO
                            ),
                           params
                    ).toString()
            ).build();
        } catch (Exception ex) {
            return SystemException.setResult(resultStruct, ex, logger);
        }
    }

    /**
     * 获取用户信息
     * @param device 设备
     * @param request 请求体
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     * @Date 2017/12/15
     */
    @GET
    @Produces({XXMediaType.TEXTUTF8})
    @Consumes({MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Path("/{device}/getUserInfo")
    public Response getUserInfo(
            @PathParam("device") String device,
            @Context HttpServletRequest request) {
        ResultStruct resultStruct = new ResultStruct();
        try {
            Object uid = request.getAttribute("uid");
            Object pid = request.getAttribute("pid");
            Object userType = request.getAttribute("userType");
            Map<String, Object> params = new HashMap<>();
            params.put("pid", pid);
            params.put("uid", uid);
            params.put("userType", userType);
            return Response.ok(
                    HttpRequest.sendGet(
                            String.format(
                                    Constant.GET_USER_INFO
                            ),
                            params
                    ).toString()
            ).build();
        } catch (Exception ex) {
            return SystemException.setResult(resultStruct, ex, logger);
        }
    }



}
