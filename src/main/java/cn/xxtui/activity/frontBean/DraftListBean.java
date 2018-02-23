package cn.xxtui.activity.frontBean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by 24593 on 2017/12/16.
 */
public class DraftListBean {

    private Long aid;
    private Long pid;
    private String indexPic;
    private String insertTime;
    private String headPic;
    private String mapPic;
    private String subject;
    private List<OrganizeItem> organizers = new ArrayList<>();
    private Integer status;

    public static List<DraftListBean> toJson(String draftList) {
        JSONObject jsonDraftList = JSON.parseObject(draftList);
        List<DraftListBean> listBeans = new ArrayList<>();
        if (jsonDraftList.getJSONObject("body") != null) {
            JSONArray activitiesJsonFindDraftList =
                    jsonDraftList.getJSONObject("body").getJSONArray("activities");
            for(Object obj : activitiesJsonFindDraftList) {
                JSONObject jsonObject = (JSONObject) obj;
                DraftListBean bean = new DraftListBean();
                bean.setAid(jsonObject.getLong("aid"));
                bean.setPid(jsonObject.getLong("pid"));
                bean.setIndexPic(jsonObject.getString("indexPic"));
                bean.setInsertTime(jsonObject.getString("insertTime"));
                bean.setHeadPic(jsonObject.getString("headPic"));
                bean.setMapPic(jsonObject.getString("mapPic"));
                bean.setSubject(jsonObject.getString("subject"));
                bean.setStatus(jsonObject.getInteger("status"));

                List<Map<String, Object>> list = (List<Map<String, Object>>) jsonObject.get("organizers");
                for (Map<String, Object> map : list) {
                    OrganizeItem organizeItem = new OrganizeItem();
                    Object logoPic = map.get("headPic");
                    organizeItem.setHeadPic(logoPic == null || "null".equals(logoPic) ? "" : logoPic.toString());
                    organizeItem.setUsername(map.get("username") + "");
                    organizeItem.setPid(Long.parseLong(map.get("pid").toString()));
                    organizeItem.setLevel(Integer.parseInt(map.get("level").toString()));
                    bean.getOrganizers().add(organizeItem);
                }
                listBeans.add(bean);
            }
        }
        return listBeans;
    }

    public Long getAid() {
        return aid;
    }

    public void setAid(Long aid) {
        this.aid = aid;
    }

    public String getIndexPic() {
        return indexPic;
    }

    public void setIndexPic(String indexPic) {
        this.indexPic = indexPic;
    }

    public String getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(String insertTime) {
        this.insertTime = insertTime;
    }

    public String getMapPic() {
        return mapPic;
    }

    public void setMapPic(String mapPic) {
        this.mapPic = mapPic;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


    public List<OrganizeItem> getOrganizers() {
        return organizers;
    }

    public void setOrganizers(List<OrganizeItem> organizers) {
        this.organizers = organizers;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }
}
