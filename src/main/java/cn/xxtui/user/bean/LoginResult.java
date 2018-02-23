package cn.xxtui.user.bean;

import java.io.Serializable;

/**
 * json返回数据
 * @author liumengwei
 * @Time 2017/8/17
 * @since V1.0
 */
public class LoginResult implements Serializable {
    private static final long serialVersionUID = -3584161189116977976L;
    private BodyResult body;
    private String status;
    private String msg;


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BodyResult getBody() {
        return body;
    }

    public void setBody(BodyResult body) {
        this.body = body;
    }

}
