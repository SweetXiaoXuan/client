package cn.xxtui.brand.frontService;

import cn.xxtui.basic.serviceUtil.ReturnValue;
import cn.xxtui.basic.serviceUtil.StringUtil;
import cn.xxtui.brand.frontBean.FBrandBean;
import cn.xxtui.brand.frontBean.FMapBean;
import cn.xxtui.session.service.MobileSessionService;
import cn.xxtui.support.bean.ResultBean;
import cn.xxtui.support.bean.ResultStruct;
import cn.xxtui.support.session.SessionConstant;
import cn.xxtui.support.session.SessionControl;
import cn.xxtui.support.util.MeaasgeUtil;
import cn.xxtui.support.util.ValidateMode;
import cn.xxtui.support.util.XXMediaType;
import cn.xxtui.user.verification.bean.BodyResult;
import cn.xxtui.user.verification.bean.LoginResult;
import cn.xxtui.user.verification.service.smsSpi.SmsVerifyKit;
import cn.xxtui.user.verification.utils.Constant;
import cn.xxtui.user.verification.utils.HttpRequest;
import cn.xxtui.user.verification.utils.ImageUtils;
import cn.xxtui.user.verification.utils.ResultMsgConstant;
import cn.xxtui.user.verification.utils.SystemException;
import com.alibaba.fastjson.JSON;
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
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 企业品牌相关接口
 * @since 2018/2/7 create
 */
@Component
@Path("/brandCompany")
public class FCompanyService {
    @Bean
    public FCompanyService fEnterpriseService() {
        return new FCompanyService();
    }
    SmsVerifyKit s;
    private final static Logger logger = LoggerFactory.getLogger(FCompanyService.class);
    @Resource
    private MobileSessionService ms;
    private MeaasgeUtil me = new MeaasgeUtil();

    /**
     * 企业品牌注册
     * @param device 设备
     * @param fBrandBean 相关参数
     * @param request 请求体
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     * @Date 2018/2/7
     */
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    @Consumes({MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Path("/{device}/brandCompanyRegist")
    public Response brandCompanyRegist(
            @PathParam("device") String device,
            @BeanParam FBrandBean fBrandBean,
            @Context HttpServletRequest request) {
        ResultStruct resultStruct = new ResultStruct();
        try {
            String content;
            String phone = fBrandBean.getPhone();
            String email = fBrandBean.getEmail();
            Map<String, Object> params = new HashMap<>();
            params.putAll(FMapBean.getEnterpriseService(fBrandBean));

            content = StringUtil.isEmpty(params);
            if (!StringUtil.isEmpty(content)) {
                resultStruct.setMsg(content);
                resultStruct.setStatus(ResultStruct.ERROR);
                return Response.ok(resultStruct.toString()).build();
            }
            // 判断手机号长度
            resultStruct = StringUtil.phoneFormat(phone, resultStruct);
            if (resultStruct.getStatus() == ResultStruct.ERROR) {
                return Response.ok(resultStruct.toString()).build();
            }
            // 判断邮箱格式
            if (!ValidateMode.email(email)) {
                resultStruct.setStatus(ResultStruct.ERROR);
                resultStruct.setMsg(me.getValue(ResultMsgConstant.emailFormatError));
                return Response.ok(resultStruct.toString()).build();
            }
            List<String> listPic = new ArrayList<>();
            listPic.add(fBrandBean.getBrandPic());
            listPic.add(fBrandBean.getBusinessLicensePic());
            // 访问图片 查看图片是否真实存在
            resultStruct = ImageUtils.accessImage(resultStruct, listPic);
            if (resultStruct.getStatus() == ResultStruct.ERROR) {
                return Response.ok(resultStruct.toString()).build();
            }

            content = HttpRequest.sendPost(Constant.BRAND_COMPANY_REGIST, params);
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
}
