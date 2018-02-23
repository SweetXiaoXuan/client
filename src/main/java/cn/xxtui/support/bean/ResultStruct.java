package cn.xxtui.support.bean;

import cn.xxtui.activity.frontBean.ActivityItem;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultStruct {
    private String status = ResultBean.OK;// 默认0 ok
    private Object body = "null";
    private String msg = "";
    @JSONField(serialize = false, deserialize = false)
    public final static String OK = ResultBean.OK;
    @JSONField(serialize = false, deserialize = false)
    public final static String ERROR = ResultBean.ERROR;
    @JSONField(serialize = false, deserialize = false)
    public final static String PROTECT = ResultBean.PROTECT;
    @JSONField(serialize = false, deserialize = false)
    public final static String NotAssess = ResultBean.NotAssess;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static JSONObject jsonObject(JSONObject rjsonObject, ResultStruct result, String content, Logger logger) {
        rjsonObject.put("status", "0");
        rjsonObject.put("msg", result.getMsg());
        rjsonObject.put("body", new ArrayList<>());
        try {
            JSONObject json = JSON.parseObject(content);
            Object body = json.get("body");
            if (body != null && !"null".equals(body)) {
                String status = json.get("status").toString();
                JSONObject jsonBody = (JSONObject) body;
                JSONArray jsonArray = jsonBody.getJSONArray("comments");
                List<Map<String, Object>> mapList = new ArrayList<>();
                for (Object object : jsonArray) {
                    ActivityItem item = new ActivityItem();
                    Map<String, Object> setMap = new HashMap<>();
                    JSONObject jsonObject = (JSONObject) object;
                    String strCommentContent = jsonObject.getString("commentContent");
                    Integer commentType = jsonObject.getInteger("commentType");
                    Map<String, Object> mapGraphic = new HashMap<>();
                    JSONArray commentContentArray = new JSONArray();
                    JSONObject commentContent = new JSONObject();
                    String commentContentStr = null;
                    if (commentType == 2) {
                        commentContentArray = jsonObject.getJSONArray("commentContent");
                    } else if(commentType == 4 || commentType == 3) {
                        commentContentStr = jsonObject.getString("commentContent");
                    } else {
                        commentContent = jsonObject.getJSONObject("commentContent");
                    }
                    if (commentType == 8) {
                        item = ActivityItem.setInte(item, json, commentContent);
                    } else if (commentType == 5) {
                        for (Map.Entry<String, Object> mapBody : commentContent.entrySet()) {
                            String key = mapBody.getKey().toString();
                            Object obody = mapBody.getValue();
                            if ("uid".equals(key)) {
                                mapGraphic.put("uid", Long.parseLong(obody.toString()));
                            } else {
                                mapGraphic.put(key, obody);
                            }
                        }

                    }

                    setMap = setMap(setMap, jsonObject, item, commentType, mapGraphic, strCommentContent, commentContentArray, commentContentStr);
                    mapList.add(setMap);
                }
                Map<String, Object> map = new HashMap<>();
                map.put("comments", mapList);
                map.put("hasNext", jsonBody.getBoolean("hasNext"));

                rjsonObject.put("status", status);
                rjsonObject.put("body", map);
            } else {
                rjsonObject.put("status", ResultBean.OK);
                rjsonObject.put("body", result.getBody());
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return rjsonObject;
    }

    /**
     * 将返回json信息循环put Map
     * @param setMap
     * @param jsonObject
     * @param item
     * @return
     */
    public static Map<String, Object> setMap(
            Map<String, Object> setMap, JSONObject jsonObject, ActivityItem item,
            Integer commentType, Map<String, Object> mapGraphic, String strCommentContent,
            JSONArray commentContentArray, String commentContentStr) {
        for (Map.Entry<String, Object> mapBody : jsonObject.entrySet()) {
            String key = mapBody.getKey().toString();
            Object obody = mapBody.getValue();
            if ("commentContent".equals(key)) {
                if (commentType == 8) {
                    setMap.put("commentContent", item);
                } else if (commentType == 5) {
                    setMap.put("commentContent", mapGraphic);
                } else if (commentType == 2) {
                    setMap.put("commentContent", commentContentArray);
                } else if (commentType == 3 || commentType == 4) {
                    setMap.put("commentContent", commentContentStr);
                } else if (commentType != 8 && commentType != 5 && commentType != 2) {
                    setMap.put("commentContent", strCommentContent);
                }
            } else if ("uid".equals(key)) {
                setMap.put("uid", Long.parseLong(obody.toString()));
            } else if ("cid".equals(key)) {
                setMap.put("cid", Long.parseLong(obody.toString()));
            } else {
                setMap.put(key, obody);
            }
        }
        return setMap;
    }

}
