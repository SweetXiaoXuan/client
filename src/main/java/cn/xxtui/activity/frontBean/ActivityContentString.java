package cn.xxtui.activity.frontBean;

import javax.ws.rs.FormParam;
import java.util.HashMap;
import java.util.Map;

/**
 * Activity details related parameters
 *
 * @since V1.0
 */
public class ActivityContentString {
    @FormParam("type")
    public Integer type;
    @FormParam("content")
    public String content;
    @FormParam("status")
    private Integer status;

    public Map<String, Object> getMap() {
        return new HashMap<String, Object>() {
            {
                put("type", getType());
                put("content", getContent());
                put("status", getStatus());
            }
        };
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
