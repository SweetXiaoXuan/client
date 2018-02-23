package cn.xxtui.support.session;

import cn.xxtui.basic.serviceUtil.StringUtil;
import cn.xxtui.support.bean.ResultStruct;
import cn.xxtui.support.util.MeaasgeUtil;
import cn.xxtui.user.utils.Constant;
import cn.xxtui.user.utils.HttpRequest;
import cn.xxtui.user.utils.ResultMsgConstant;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.cxf.jaxrs.model.OperationResourceInfo;
import org.apache.cxf.jaxrs.utils.JAXRSUtils;
import org.apache.cxf.message.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * 用户访问资源方法时要进行的拦截处理，目录方法（或类）上使用ResourceAccess注解进行标识
 *
 * @author starlee
 */

public class SessionFilter implements ContainerRequestFilter {

    private Logger log = LogManager.getLogger(SessionFilter.class.getName());
    private MeaasgeUtil me = new MeaasgeUtil();

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        ResultStruct result = new ResultStruct();
        Message message = JAXRSUtils.getCurrentMessage();
        OperationResourceInfo opera = (OperationResourceInfo) message.getExchange().get(OperationResourceInfo.class);
        String path = path(opera);
        Map map = new HashMap<>();
        String url = URLEncoder.encode(path);
        map.put("url", url);
        String urlInfo = HttpRequest.sendGet(
                Constant.GET_URL_INFO, map);
        JSONObject json = JSON.parseObject(urlInfo);
        String status = String.valueOf(json.get("status"));
        if (ResultStruct.ERROR.equals(status)) {
//            result.setMsg("api未录入,默认禁止访问" + path + "，请联系管理员");
            result.setMsg(String.format(me.getValue(ResultMsgConstant.apiNotEntered), path));
            result.setStatus(ResultStruct.ERROR);
            containerRequestContext.abortWith(Response.ok(result.toString(), MediaType.APPLICATION_JSON_TYPE).build());
            return;
        } else {
            HttpServletRequest req = (HttpServletRequest) message.get("HTTP.REQUEST");
            req.setAttribute("uid", req.getSession().getAttribute("uid"));
            req.setAttribute("pid", req.getSession().getAttribute("pid"));
            req.setAttribute("userType", req.getSession().getAttribute("userType"));
            int level = (Integer) ((JSONObject) json.get("body")).get("level");
            if (level == 0) {
                log.info("直接可以访问" + path);
                return;
            } else {
                HttpSession session = req.getSession(false);
                if (session == null) {
                    result.setMsg(me.getValue(ResultMsgConstant.notLogin));
                    result.setStatus(Constant.NOT_LOGIN);
                    containerRequestContext.abortWith(Response.ok(JSON.toJSONString(result), MediaType.APPLICATION_JSON_TYPE).build());
                    return;
                }
                Enumeration<String> headers = req.getHeaders("rsid");

                String apprsid = "";
                while (headers.hasMoreElements()) {
                    apprsid = headers.nextElement();
                }
                String rv = get(apprsid, req);
                if (StringUtil.isEmpty(rv)) {
                    result.setMsg(me.getValue(ResultMsgConstant.notLogin));
                    result.setStatus(Constant.NOT_LOGIN);
                    containerRequestContext.abortWith(Response.ok(JSON.toJSONString(result), MediaType.APPLICATION_JSON_TYPE).build());
                    return;
                }
            }

        }

    }

    private String get(String rsid, HttpServletRequest req) {
        if (rsid == null) {
            return "";
        }
        Object sessionid = req.getSession().getAttribute("rsid");
        if (sessionid == null) {
            return "";
        } else {
            return sessionid.toString();
        }
    }

    private String path(OperationResourceInfo opera) {
        String methodPath = opera.getAnnotatedMethod().getAnnotation(javax.ws.rs.Path.class).value();
        String classPath = opera.getClassResourceInfo().getResourceClass().getAnnotation(javax.ws.rs.Path.class).value();
        String path = String.format("%s/%s", classPath, methodPath);
        String rpath = path.replace("///", "/");
        String p = rpath.replace("//", "/");
        return p;
    }

    private Map<String, String> getRequestCookies(Message message) {
        Map<String, String> cookies = new HashMap<String, String>();
        // 获取map_header
        Map map_header = (Map) message.get(Message.PROTOCOL_HEADERS);
        // 如果map_header不为空，输出map_header
        if (map_header != null) {
            log.info("header++++++++++++++++" + map_header.toString());
        }
        // 从map_header中获取cookie
        Object obj = map_header.get("Cookie");
        // 如果obj不为空，输出obj
        if (obj != null) {
            log.info("cookies+++++++++++++++" + obj.toString());
        } else {
            // 否则返回cookies
            return cookies;
        }

        List<String> cookieStr = (List) obj;// ArryayList,并且只有一个元素
        StringTokenizer strtoken = new StringTokenizer(cookieStr.get(0), ";");
        while (strtoken.hasMoreTokens()) {
            String cookie = strtoken.nextToken();
            int position = cookie.indexOf("=");
            if (position > 0) {
                String key = cookie.substring(0, position);
                String value = cookie.substring(position + 1, cookie.length());
                cookies.put(key.trim(), value.trim());
            }
        }
        return cookies;
    }
}
