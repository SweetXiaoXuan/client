package cn.xxtui.user.utils;

import cn.xxtui.support.bean.ResultStruct;
import org.slf4j.Logger;

import javax.ws.rs.core.Response;

/**
 * System exception
 * @author liumengwei
 * @Time 2017/8/2
 *
 */
public enum SystemException {
    SYSTEM_EXCEPTION("500", "System exception");

    SystemException(String value, String description) {
        this.value = value;
        this.description = description;
    }
    private String value;
    private String description;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static Response setResult(ResultStruct resultStruct, Exception e, Logger logger) {
        e.printStackTrace();
        logger.error(e.getMessage(), e);
        resultStruct.setBody("");
        resultStruct.setMsg(e.getMessage());
        resultStruct.setStatus(String.valueOf(SYSTEM_EXCEPTION.getValue()));
        return Response.ok(resultStruct.toString()).build();
    }

}
