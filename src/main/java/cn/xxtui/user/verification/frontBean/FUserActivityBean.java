package cn.xxtui.user.verification.frontBean;

import cn.xxtui.basic.serviceUtil.StringUtil;
import com.alibaba.fastjson.JSONObject;

/**
 * Created by 24593 on 2018/1/19.
 */
public class FUserActivityBean {
    private Integer attenceNum;
    private Integer focusNum;
    private Integer groupOrActivity;
    private Integer infoNum;
    private Integer status;
    private Integer userActivityStatus;
    private Long aid;
    private Long uid;
    private String indexPic;
    private String subject;

    public static FUserActivityBean toJson(FUserActivityBean userActivityBean, JSONObject jsonObject) {
        userActivityBean.setAid(jsonObject.getLong("aid"));
        userActivityBean.setAttenceNum(jsonObject.getInteger("attenceNum"));
        userActivityBean.setFocusNum(jsonObject.getInteger("focusNum"));
        userActivityBean.setGroupOrActivity(jsonObject.getInteger("groupOrActivity"));
        String indexPic = jsonObject.getString("indexPic");
        userActivityBean.setIndexPic(StringUtil.isEmpty(indexPic) ? "" : indexPic);
        userActivityBean.setInfoNum(jsonObject.getInteger("infoNum"));
        userActivityBean.setStatus(jsonObject.getInteger("status"));
        userActivityBean.setSubject(jsonObject.getString("subject"));
        userActivityBean.setUid(jsonObject.getLong("uid"));
        userActivityBean.setUserActivityStatus(jsonObject.getInteger("userActivityStatus"));
        return userActivityBean;
    }

    public Integer getAttenceNum() {
        return attenceNum;
    }

    public void setAttenceNum(Integer attenceNum) {
        this.attenceNum = attenceNum;
    }

    public Integer getFocusNum() {
        return focusNum;
    }

    public void setFocusNum(Integer focusNum) {
        this.focusNum = focusNum;
    }

    public Integer getInfoNum() {
        return infoNum;
    }

    public void setInfoNum(Integer infoNum) {
        this.infoNum = infoNum;
    }

    public Integer getGroupOrActivity() {
        return groupOrActivity;
    }

    public void setGroupOrActivity(Integer groupOrActivity) {
        this.groupOrActivity = groupOrActivity;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getUserActivityStatus() {
        return userActivityStatus;
    }

    public void setUserActivityStatus(Integer userActivityStatus) {
        this.userActivityStatus = userActivityStatus;
    }

    public Long getAid() {
        return aid;
    }

    public void setAid(Long aid) {
        this.aid = aid;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getIndexPic() {
        return indexPic;
    }

    public void setIndexPic(String indexPic) {
        this.indexPic = indexPic;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
