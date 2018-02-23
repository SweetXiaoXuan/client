package cn.xxtui.activity.frontBean;

import cn.xxtui.basic.serviceUtil.StringUtil;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * Activity related parameters
 * @since V1.0
 */
public class ActivityItem {
    private List<OrganizeItem> organizers =new ArrayList<>();
    private String address;
    //主题
    private String subject;
    //结束时间
    private String endDateTime;
    private String insertTime;
    //开始时间
    private String beginDateTime;
    //圈子人数
    private Long maxAttendances;
    private Integer activityNum;
    private Long aid;
    private String name;
    private Integer status;
    private Long registers;
    private Integer attenceNum;
    private Integer isLogin;
    private Integer isSignUp;
    private Integer isAttention;
    private Integer focusNum;
    private String indexPic;
    private String authors;
    private String description;
    private String videoImage;
    private String upTime;
    private String upTimeColumns;
    private Integer pushColumnsPage;
    private Integer readersNum;
    private Integer pushIndexPage;
    // 首页图片是否显示 0不显示  1显示
    private Integer indexPicIsShow;

    public static ActivityItem toJson(ActivityItem item, JSONObject jsonObject, String status) {
        if (jsonObject != null) {
            item.setUpTime("");
            item.setUpTimeColumns("");
            if ("all".equals(status)) {
                if (jsonObject.getString("upTime") != null &&
                        !StringUtil.isEmpty(jsonObject.getString("upTime"))) {
                    item.setUpTime(jsonObject.getString("upTime"));
                }
            } else if (!"null".equals(status)) {
                if (jsonObject.getString("upTimeColumns") != null &&
                        !StringUtil.isEmpty(jsonObject.getString("upTimeColumns"))) {
                    item.setUpTimeColumns(jsonObject.getString("upTimeColumns"));
                }
            }
            Integer activityStatus = jsonObject.getInteger("status");
            if (activityStatus == 3) {
                item.setInsertTime(jsonObject.getString("insertTime"));
                item.setReadersNum(jsonObject.getInteger("readersNum"));
            } else {
                item.setBeginDateTime(jsonObject.getString("beginDateTime"));
                item.setEndDateTime(jsonObject.getString("endDateTime"));
            }
            String videoImage = jsonObject.getString("videoImage");
            item.setVideoImage(StringUtil.isEmpty(videoImage) ? "" : videoImage);
            item.setStatus(activityStatus);
            item.setIndexPicIsShow(jsonObject.getInteger("indexPicIsShow"));
            item.setAddress(jsonObject.getString("address"));
            String jsonDescription = jsonObject.getString("description");
            item.setDescription(StringUtil.isEmpty(jsonDescription) || "null".equals(jsonDescription) ? "" : jsonDescription);
            item.setIsAttention(jsonObject.getInteger("isAttention"));
            item.setIsSignUp(jsonObject.getInteger("isSignUp"));
            item.setIsLogin(jsonObject.getInteger("isLogin"));
            item.setAttenceNum(jsonObject.getInteger("attenceNum"));
            item.setFocusNum(jsonObject.getInteger("focusNum"));
            item.setAid(jsonObject.getLong("aid"));
            item.setActivityNum(jsonObject.getInteger("activityNum"));
            item.setSubject(jsonObject.getString("subject"));
            List<Map<String, Object>> list = (List<Map<String, Object>>) jsonObject.get("organizers");
            for (Map<String, Object> map : list) {
                OrganizeItem organizeItem = new OrganizeItem();
                organizeItem.setHeadPic("null".equals(map.get("headPic") + "") ? "" : map.get("headPic") + "");
                organizeItem.setLevel(0);
                Object name = map.get("username");
                organizeItem.setUsername(name == null ? "" : name.toString());
                organizeItem.setPid(Long.parseLong(map.get("pid").toString()));
                item.getOrganizers().add(organizeItem);
            }
            // 首图
            String indexpic = jsonObject.getString("indexPic");
            item.setIndexPic(StringUtil.isEmpty(indexpic) ? "" : indexpic);
        }
        return item;
    }

    public static ActivityItem setInte(ActivityItem item, JSONObject json, JSONObject jsonObject) {
        item = ActivityItem.toJson(item, jsonObject, "null");
        // 活动状态为1 围观
        if ("1".equals(item.getStatus())) {
            PlatformItem platformItem = PlatformItem.addData(json);
            item.getPlatforms().add(platformItem);
        }
        // 活动状态为3 探索
        if ("3".equals(item.getStatus())) {
            item.setAuthors("hodays");//未来以实际为准
        }
        item.setUpTimeColumns(null);
        item.setUpTime(null);
        item.setPushColumnsPage(null);
        item.setPushIndexPage(null);
        item.setIndexPicIsShow(null);
        return item;
    }

    private List<PlatformItem> platforms=new ArrayList<>();

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public List<PlatformItem> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(List<PlatformItem> platforms) {
        this.platforms = platforms;
    }

    public List<OrganizeItem> getOrganizers() {
        return organizers;
    }

    public void setOrganizers(List<OrganizeItem> organizers) {
        this.organizers = organizers;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
    }

    public String getBeginDateTime() {
        return beginDateTime;
    }

    public void setBeginDateTime(String beginDateTime) {
        this.beginDateTime = beginDateTime;
    }

    public Long getMaxAttendances() {
        return maxAttendances;
    }

    public void setMaxAttendances(Long maxAttendances) {
        this.maxAttendances = maxAttendances;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getRegisters() {
        return registers;
    }

    public void setRegisters(Long registers) {
        this.registers = registers;
    }

    public String getUpTime() {
        return upTime;
    }

    public void setUpTime(String upTime) {
        this.upTime = upTime;
    }

    public String getUpTimeColumns() {
        return upTimeColumns;
    }

    public void setUpTimeColumns(String upTimeColumns) {
        this.upTimeColumns = upTimeColumns;
    }

    public Integer getPushColumnsPage() {
        return pushColumnsPage;
    }

    public void setPushColumnsPage(Integer pushColumnsPage) {
        this.pushColumnsPage = pushColumnsPage;
    }

    public Integer getPushIndexPage() {
        return pushIndexPage;
    }

    public void setPushIndexPage(Integer pushIndexPage) {
        this.pushIndexPage = pushIndexPage;
    }

    public Integer getIndexPicIsShow() {
        return indexPicIsShow;
    }

    public void setIndexPicIsShow(Integer indexPicIsShow) {
        this.indexPicIsShow = indexPicIsShow;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(String insertTime) {
        this.insertTime = insertTime;
    }

    public Integer getReadersNum() {
        return readersNum;
    }

    public void setReadersNum(Integer readersNum) {
        this.readersNum = readersNum;
    }

    public Integer getActivityNum() {
        return activityNum;
    }

    public void setActivityNum(Integer activityNum) {
        this.activityNum = activityNum;
    }

    public Long getAid() {
        return aid;
    }

    public void setAid(Long aid) {
        this.aid = aid;
    }

    public Integer getAttenceNum() {
        return attenceNum;
    }

    public void setAttenceNum(Integer attenceNum) {
        this.attenceNum = attenceNum;
    }

    public Integer getIsSignUp() {
        return isSignUp;
    }

    public void setIsSignUp(Integer isSignUp) {
        this.isSignUp = isSignUp;
    }

    public Integer getIsAttention() {
        return isAttention;
    }

    public void setIsAttention(Integer isAttention) {
        this.isAttention = isAttention;
    }

    public Integer getFocusNum() {
        return focusNum;
    }

    public void setFocusNum(Integer focusNum) {
        this.focusNum = focusNum;
    }

    public String getIndexPic() {
        return indexPic;
    }

    public void setIndexPic(String indexPic) {
        this.indexPic = indexPic;
    }

    public Integer getIsLogin() {
        return isLogin;
    }

    public void setIsLogin(Integer isLogin) {
        this.isLogin = isLogin;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getVideoImage() {
        return videoImage;
    }

    public void setVideoImage(String videoImage) {
        this.videoImage = videoImage;
    }
}
