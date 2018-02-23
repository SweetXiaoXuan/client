package cn.xxtui.user.bean;

import javax.servlet.http.HttpSession;
import java.io.Serializable;

/**
 * json返回body数据
 * @author liumengwei
 * @Time 2017/8/17
 * @since V1.0
 */
public class BodyResult implements Serializable {
    private static final long serialVersionUID = -8724730577300194433L;
    private String uid;
    private String rsid;
    private String pid;
    private String userType;
    private String login_time;
    private UserResult user;

    public static BodyResult setBodyResult(HttpSession session, LoginResult loginResult) {
        BodyResult body = loginResult.getBody();
        BodyResult bodyResult = new BodyResult();
        bodyResult.setRsid(session.getId());
        bodyResult.setLogin_time(body.getLogin_time());
        bodyResult.setUid(body.getUid());
        bodyResult.setUser(body.getUser());
        return bodyResult;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getLogin_time() {
        return login_time;
    }

    public void setLogin_time(String login_time) {
        this.login_time = login_time;
    }

    public UserResult getUser() {
        return user;
    }

    public void setUser(UserResult user) {
        this.user = user;
    }

    public String getRsid() {
        return rsid;
    }

    public void setRsid(String rsid) {
        this.rsid = rsid;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }


}
