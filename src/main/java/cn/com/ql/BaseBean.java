package cn.com.ql;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BaseBean implements Serializable {
    public String status = "";
    public String msg = "";
    public Object body = null;

    public <T> Result<T> getBody(Class<T> clazz) {
        if (Integer.valueOf(status) == 0) {
            List<T> tt = new ArrayList<>();
            if (body!= null) {
                String v = String.valueOf(body);
                String vv = v.trim();
                if (vv.contains("[") && vv.indexOf("[") == 0) {
                    List<T> l = JSON.parseArray(v, clazz);
                    tt.addAll(l);
                } else {
                    T o = JSON.parseObject(String.valueOf(body), clazz);
                    tt.add(o);
                }
            }
            return new Result<>(tt);
        }
        return null;
    }
}
