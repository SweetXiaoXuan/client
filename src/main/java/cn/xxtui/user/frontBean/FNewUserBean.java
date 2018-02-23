package cn.xxtui.user.frontBean;

import javax.ws.rs.FormParam;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户相关参数Bean
 * @author liumengwei
 * @since 2018/1/11
 */
public class FNewUserBean {
    // 用户id
    @FormParam("uid")
    private Long uid;
    // 性别
    @FormParam("gender")
    private Integer gender;
    // 证件类型 0身份证
    @FormParam("type")
    private Integer type;
    // 手机号
    @FormParam("phone")
    private String phone;
    // 用户名
    @FormParam("username")
    private String username;
    // 密码
    @FormParam("password")
    private String password;
    // 头像
    @FormParam("headPic")
    private String headPic;
    // 身份证号码
    @FormParam("idNumber")
    private String idNumber;
    // 姓名
    @FormParam("givename")
    private String givename;
    // 自我介绍
    @FormParam("selfIntroduction")
    private String selfIntroduction;
    // 个人封面图
    @FormParam("coverImage")
    private String coverImage;
    // 请求状态 0：用户注册 1：找回密码
    @FormParam("state")
    private String state;
    // 验证码
    @FormParam("code")
    private String code;
    // 重复密码
    @FormParam("repeatPassword")
    private String repeatPassword;

    public Map<String, Object> getUserInfoMap(){
        return new HashMap<String, Object>(){
            {
                put("strGender", getGender() + "");
                put("selfIntroduction", getSelfIntroduction());
                put("username", getUsername());
                put("headPic", getHeadPic());
            }
        };
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getGivename() {
        return givename;
    }

    public void setGivename(String givename) {
        this.givename = givename;
    }

    public String getSelfIntroduction() {
        return selfIntroduction;
    }

    public void setSelfIntroduction(String selfIntroduction) {
        this.selfIntroduction = selfIntroduction;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }
}
