package cn.xxtui.activity.frontBean;

import javax.ws.rs.FormParam;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Activity related parameters
 *
 * @since V1.0
 */
public class ActivityBean {
    @FormParam("indexPicIsShow")
    private Integer indexPicIsShow;
    @FormParam("activityNum")
    private Integer activityNum;
    @FormParam("draft")
    private Integer draft;
    @FormParam("aType")
    private Integer aType;
    @FormParam("rid")
    private String rid;
    @FormParam("organizer")
    private String organizer;
    @FormParam("mapPic")
    private String mapPic;
    @FormParam("headPic")
    private String headPic;
    @FormParam("indexPic")
    private String indexPic;
    @FormParam("name")
    private String name;
    @FormParam("subject")
    private String subject;
    @FormParam("address")
    private String address;
    @FormParam("beginDateTime")
    private String beginDateTime;
    @FormParam("endDateTime")
    private String endDateTime;
    @FormParam("aid")
    private Long aid;
    @FormParam("description")
    private String description;
    @FormParam("status")
    private Integer status;
    @FormParam("registerEndTime")
    private String registerEndTime;
    @FormParam("telephone")
    private String telephone;
    @FormParam("email")
    private String email;
    @FormParam("mobile")
    private String mobile;
    @FormParam("fee")
    private Double fee;
    @FormParam("platformPhone")
    private String platformPhone;
    // 主办方信息(用户、企业)
    private List<Map<String, Object>> organizers;

    public Map<String, Object> updateFindActivityStatusAndContent() {
        return new HashMap<String, Object>() {
            {
                put("headPic", getHeadPic());
                put("indexPic", getIndexPic());
                put("description", getDescription());
                put("draft", getDraft());
            }
        };
    }

    public Map<String, Object> updateOnlookersActivityStatusAndContent() {
        return new HashMap<String, Object>() {
            {
                put("subject", getSubject());
                put("headPic", getHeadPic());
                put("indexPic", getIndexPic());
                put("description", getDescription());
                put("draft", getDraft());
            }
        };
    }

    public Map<String, Object> getMap() {
        return new HashMap<String, Object>() {
            {
                put("indexPicIsShow", getIndexPicIsShow());
                put("activityNum", getActivityNum());
                put("rid", getRid());
                put("headPic", getHeadPic());
                put("mapPic", getMapPic());
                put("indexPic", getIndexPic());
                put("subject", getSubject());
                put("address", getAddress());
                put("beginDateTime", getBeginDateTime());
                put("endDateTime", getEndDateTime());
                put("organizer", getOrganizer());
                put("description", getDescription());
                put("status", getStatus());
                put("registerEndTime", getRegisterEndTime());
                put("telephone", getTelephone());
                put("email", getEmail());
                put("mobile", getMobile());
                put("fee", getFee());
                put("draft", getDraft());
            }
        };
    }

    public String getRegisterEndTime() {
        return registerEndTime;
    }

    public void setRegisterEndTime(String registerEndTime) {
        this.registerEndTime = registerEndTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getBeginDateTime() {
        return beginDateTime;
    }

    public void setBeginDateTime(String beginDateTime) {
        this.beginDateTime = beginDateTime;
    }

    public String getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Map<String, Object>> getOrganizers() {
        return organizers;
    }

    public void setOrganizers(List<Map<String, Object>> organizers) {
        this.organizers = organizers;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public String getPlatformPhone() {
        return platformPhone;
    }

    public void setPlatformPhone(String platformPhone) {
        this.platformPhone = platformPhone;
    }

    public String getMapPic() {
        return mapPic;
    }

    public void setMapPic(String mapPic) {
        this.mapPic = mapPic;
    }

    public Integer getIndexPicIsShow() {
        return indexPicIsShow;
    }

    public void setIndexPicIsShow(Integer indexPicIsShow) {
        this.indexPicIsShow = indexPicIsShow;
    }

    public Integer getaType() {
        return aType;
    }

    public void setaType(Integer aType) {
        this.aType = aType;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public Long getAid() {
        return aid;
    }

    public void setAid(Long aid) {
        this.aid = aid;
    }

    public Integer getDraft() {
        return draft;
    }

    public void setDraft(Integer draft) {
        this.draft = draft;
    }

    public String getIndexPic() {
        return indexPic;
    }

    public void setIndexPic(String indexPic) {
        this.indexPic = indexPic;
    }

    public Integer getActivityNum() {
        return activityNum;
    }

    public void setActivityNum(Integer activityNum) {
        this.activityNum = activityNum;
    }
}
