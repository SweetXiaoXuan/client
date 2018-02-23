package cn.xxtui.support.session;

/**
 * Created by Administrator on 2017/8/31 0031.
 */
import org.springframework.session.Session;
import org.springframework.session.web.http.HttpSessionStrategy;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RSIDHttpSessionStrategy implements HttpSessionStrategy {
    private String headerName = "rsid";

    public String getRequestedSessionId(HttpServletRequest request) {
        return request.getHeader(this.headerName);
    }

    public void onNewSession(Session session, HttpServletRequest request,
                             HttpServletResponse response) {
        response.setHeader(this.headerName, session.getId());
    }

    public void onInvalidateSession(HttpServletRequest request,
                                    HttpServletResponse response) {
        response.setHeader(this.headerName, "");
    }

    /**
     * The name of the header to obtain the session id from. Default is "x-auth-token".
     *
     * @param headerName the name of the header to obtain the session id from.
     */
    public void setHeaderName(String headerName) {
        Assert.notNull(headerName, "headerName cannot be null");
        this.headerName = headerName;
    }
}