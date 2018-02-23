package cn.xxtui.user.bean;

import java.io.Serializable;

/**
 * json返回user数据
 * @author liumengwei
 * @Time 2017/8/17
 * @since V1.0
 */
public class UserResult implements Serializable {
    private static final long serialVersionUID = 5876364340719908225L;
    private String phone;
    private String headPic;
    private String givename = "";
    private String platform_uid;
    private String coverImage;
    private Integer gender;
    private String idNumber;
    private String selfIntroduction;
    private String username;
    private Boolean verified;

    public String getPlatform_uid() {
        return platform_uid;
    }

    public void setPlatform_uid(String platform_uid) {
        this.platform_uid = platform_uid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public String getGivename() {
        return givename;
    }

    public void setGivename(String givename) {
        this.givename = givename;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getSelfIntroduction() {
        return selfIntroduction;
    }

    public void setSelfIntroduction(String selfIntroduction) {
        this.selfIntroduction = selfIntroduction;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
