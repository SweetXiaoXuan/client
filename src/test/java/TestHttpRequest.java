import cn.com.ql.BaseBean;
import cn.com.ql.Result;
import cn.xxtui.user.verification.utils.HttpRequest;
import com.alibaba.fastjson.JSON;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TestHttpRequest {
    @Test
    public void testPost() throws IOException {
        String url = "/lv/api/u/userRegist";
        Map<String, Object> map = new HashMap<>();
        map.put("username", "111111");
        map.put("password", "111111");
        map.put("phone", "1222");
        map.put("gender", "1");
        String value = HttpRequest.sendPost(url, map);
        Assert.assertEquals("{\"body\":{\"error\":\"phone number must digital and  length 11\"},\"status\":\"14\",\"msg\":\"registe fail\"}", value);
    }

    @Test
    public void testJSON() {

        String v = "{\"body\":{\"error\":\"phone number must digital\"},\"status\":\"0\",\"msg\":\"registe fail\"}";
        BaseBean abc=JSON.parseObject(v,BaseBean.class);
        Result<TestErrorBean> errorBean=abc.getBody(TestErrorBean.class);
        System.out.println(errorBean.unique().error);
    }
    @Test
    public void testArray()
    {
        String v = "{\"body\":[{\"error\":\"phone number must digital\"}],\"status\":\"0\",\"msg\":\"registe fail\"}";
        BaseBean abc=JSON.parseObject(v,BaseBean.class);
        Result<TestErrorBean> errorBean=abc.getBody(TestErrorBean.class);
        System.out.println(errorBean.list().get(0).error);
    }
}
