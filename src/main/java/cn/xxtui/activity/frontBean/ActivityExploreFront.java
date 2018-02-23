package cn.xxtui.activity.frontBean;

import javax.persistence.Entity;
import javax.ws.rs.FormParam;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lixing on 16/3/19.
 * Post exploration activities
 * @since V1.0
 */
@Entity
public class ActivityExploreFront {
    public Map<String, Object> getMap(){
    return new HashMap<String, Object>(){
        {
            put("subject", getSubject());
            put("indexPic", getIndexPic());
            put("indexPicIsShow", getIndexPicIsShow());
            put("content", getContent());
            put("headPic", getHeadPic());
            put("draft", getDraft());
        }
    };
}

    @FormParam("subject")
    private String subject;
    @FormParam("indexPic")
    private String indexPic;
    @FormParam("content")
    private String content;
    // 首页图片是否显示 0不显示  1显示
    @FormParam("indexPicIsShow")
    private Integer indexPicIsShow;
    @FormParam("headPic")
    private String headPic;
    // 是否存为草稿
    @FormParam("draft")
    private Integer draft;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getIndexPic() {
        return indexPic;
    }

    public void setIndexPic(String indexPic) {
        this.indexPic = indexPic;
    }

    public Integer getIndexPicIsShow() {
        return indexPicIsShow;
    }

    public void setIndexPicIsShow(Integer indexPicIsShow) {
        this.indexPicIsShow = indexPicIsShow;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public Integer getDraft() {
        return draft;
    }

    public void setDraft(Integer draft) {
        this.draft = draft;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
