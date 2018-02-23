package com.lv.user;

import cn.xxtui.activity.frontBean.MobileActivityBean;
import junit.framework.TestCase;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Map;

/**
 * user接口测试
 * @author liumengwei
 * @Date 2017/9/18确实
 */
public class TestUser extends TestCase {
    private String localIp = "localhost:8112";
    private String remoteIp = "www.hodays.com:8112";

    //test mobileAddActivity
    public void testMobileAddActivity() throws IOException {
        MobileActivityBean mobileActivityBean = new MobileActivityBean();
        mobileActivityBean.setUid("42");
        String url = localIp + "/flvoice/api/activity/ios/addMobileActivity";
        String content = sendPost(url, mobileActivityBean.getMap());

        System.out.println(Response.ok(content.toString()).build());

    }


    private static String sendPost(String address, Map<String, Object> params) throws IOException {
        HttpClient client = new DefaultHttpClient();
        StringBuffer str = new StringBuffer();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            str.append(param.getKey()).append("=").append(param.getValue()).append("&");
        }
        String param = str.substring(0, str.length() - 1);
        HttpPost post = new HttpPost(address + "?" + param);
        HttpResponse response = client.execute(post);
        String content = EntityUtils.toString(response.getEntity(), "UTF-8");
        return content;
    }
}
