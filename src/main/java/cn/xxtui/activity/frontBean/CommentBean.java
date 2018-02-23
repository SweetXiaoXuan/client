package cn.xxtui.activity.frontBean;

import javax.ws.rs.FormParam;

/**
 * 评论相关bean
 * @author liumengwei
 * @Time 2017/8/30
 * @since V1.0
 */
public class CommentBean {
    @FormParam("text")
    private String text;
    @FormParam("graphicType")
    private Integer graphicType;
    @FormParam("pic")
    private String[] pic;
    @FormParam("cid")
    private String cid;// 评论id
    @FormParam("url")
    private String url;
    @FormParam("activityId")
    private Long activityId;
    @FormParam("content")
    private String content;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String[] getPic() {
        return pic;
    }

    public void setPic(String[] pic) {
        this.pic = pic;
    }

    public String getCid() {
        return cid;
    }
    public String getUrl() {
        return url;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getGraphicType() {
        return graphicType;
    }

    public void setGraphicType(Integer graphicType) {
        this.graphicType = graphicType;
    }
}
