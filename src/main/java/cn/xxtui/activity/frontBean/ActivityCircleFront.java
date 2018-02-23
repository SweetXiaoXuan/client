package cn.xxtui.activity.frontBean;

import javax.persistence.Entity;
import javax.ws.rs.FormParam;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lixing on 16/3/19.
 * Activity Circle Sheet - Front End
 * @since V1.0
 */
@Entity
public class ActivityCircleFront {
    public Map<String, Object> getMap(){
    return new HashMap<String, Object>(){
        {
            put("pid", getPid());
            put("createTime", getCreateTime());
            put("name", getName());
            put("pic", getPic());
            put("notice", getNotice());
            put("status", getStatus());
        }
    };
}

    @Override
    public String toString() {
        return "ActivityCircleFront{" +
                "uid=" + uid +
                ", pid=" + pid +
                ", createTime=" + createTime +
                ", name='" + name + '\'' +
                ", pic='" + pic + '\'' +
                ", notice='" + notice + '\'' +
                ", status=" + status +
                ", extend='" + extend + '\'' +
                '}';
    }

    @FormParam("uid")
    private long uid;
    @FormParam("pid")
    private Long pid;
    @FormParam("createTime")
    private Long createTime;
    @FormParam("name")
    private String name;
    @FormParam("pic")
    private String pic;
    @FormParam("notice")
    private String notice;
    @FormParam("status")
    private long status;
    private String extend;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public Long getPid() {
        return pid;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }
}
