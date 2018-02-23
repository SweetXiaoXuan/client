package cn.xxtui.session.service;

import cn.xxtui.basic.serviceUtil.ReturnValue;
import cn.xxtui.user.verification.bean.LoginResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * session操作
 * @author
 * @Time 2017/8/31
 *
 */
@Component
public class MobileSessionService {
	private final static Logger logger = LoggerFactory.getLogger(MobileSessionService.class);
	@Bean
	private MobileSessionService mobileSessionService() {
		return new MobileSessionService();
	}

	/**
	 * 保存session到redis中
	 * @param loginResult 登陆返回结果
	 * @param req 请求体
	 * @return cn.xxtui.basic.serviceUtil.ReturnValue
	 * @author liumengwei
	 */
	@Transactional
	public ReturnValue save(LoginResult loginResult, HttpSession session, HttpServletRequest req) {
		ReturnValue rv = new ReturnValue();
		try {
			req.getSession().setAttribute("rsid", session.getId());
			req.getSession().setAttribute("uid", loginResult.getBody().getUid());
			req.getSession().setAttribute("pid", loginResult.getBody().getPid());
			req.getSession().setAttribute("userType", loginResult.getBody().getUserType());
			req.getSession().setAttribute("loginTime", loginResult.getBody().getLogin_time());
			session.setMaxInactiveInterval(60 * 60 * 2);
//            logger.info("session有效时间：" + session.getMaxInactiveInterval());
			rv.setFlag(ReturnValue.FLAG_SUCCESS);
		} catch (Exception ex) {
			rv.setFlag(ReturnValue.FLAG_FAIL);
			rv.setMeg(ex.getMessage() + " logon register failed");
		}
		return rv;
	}

	/**
	 * 根据rsid从session中获取相关信息
	 * @param rsid sessionid
	 * @param req 请求体
	 * @return java.lang.String
	 */
	@Transactional
	public String get(String rsid, HttpServletRequest req) {
		if(rsid == null) {
			return "";
		}
		Object sessionid = req.getSession().getAttribute("rsid");
		if (sessionid == null) {
			return "";
		} else {
			return sessionid.toString();
		}
	}

	/**
	 * 删除session信息
	 * @param request 请求体
	 * @return java.lang.Boolean
	 * @author liumengwei
	 * @Date 2017/9/2
	 *
	 */
	public Boolean logoutSession(HttpServletRequest request) {
		request.getSession().invalidate();
		Object sessionid = request.getSession().getAttribute("uid");
		if (sessionid == null || sessionid == "")
			return true;
		else
			return false;
	}

}
