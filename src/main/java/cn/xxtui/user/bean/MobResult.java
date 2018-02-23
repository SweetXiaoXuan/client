package cn.xxtui.user.bean;


import java.io.Serializable;

/**
 * Mob短信平台json返回数据
 * @author liumengwei
 * @Time 2017/8/2
 * @since V1.0
 */
public class MobResult implements Serializable{
    private static final long serialVersionUID = 1391540171704234853L;
    private String status;
    private String error;

    public void setStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }
    public void setError(String error) {
        this.error = error;
    }
    public String getError() {
        return error;
    }
}