package cn.xxtui.activity.frontBean;

import com.alibaba.fastjson.JSONObject;

/**
 * Platform related
 * @since V1.0
 */
public class PlatformItem {
    private String icon;
    private String username;
    private String schema;

    public static PlatformItem addData(JSONObject json) {
        PlatformItem platformItem = new PlatformItem();
        platformItem.setIcon(json.getString("headPic"));//未来以实际数据为准
        platformItem.setSchema("intent://");
        platformItem.setUsername("abc");
        return platformItem;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
