package com.pressure;

import cn.xxtui.basic.serviceUtil.StringUtil;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * HTTP访问工具类
 *
 * @author liumengwei
 * @Time 2017/8/4
 */
public class HttpRequest {
    // 报错说明
    private static String content = "Verification code or phone number error";
    // 请求域名
//    private static final String ADDRESS_URL = "http://www.hodays.com:8119";
    private static final String ADDRESS_URL = "http://www.hodays.com:8122";
//    private static final String ADDRESS_URL = "http://localhost:8119";

    /**
     * POST请求
     *
     * @param address 请求地址
     * @param params 请求参数
     * @return java.lang.String
     * @throws IOException
     */
    public static String sendPost(String address, Map<String, Object> params, String rsid) throws IOException {
        HttpClient client = new DefaultHttpClient();
        List<NameValuePair> nvps = new ArrayList<>();
        HttpPost post = new HttpPost(ADDRESS_URL + address);
        if (params != null) {
            for (Map.Entry<String, Object> param : params.entrySet()) {
                nvps.add(new BasicNameValuePair(param.getKey(), String.valueOf(param.getValue())));
            }
            try {
                post.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        post.setHeader("rsid", rsid);
        HttpResponse response = client.execute(post);
        content = EntityUtils.toString(response.getEntity(), "UTF-8");
        return content;
    }

    /**
     * GET请求
     *
     * @param address 请求地址
     * @param params 请求参数
     * @return java.lang.String
     * @throws IOException
     */
    public static String sendGet(String address, Map<String, Object> params, String rsid) throws IOException {
        HttpClient client = new DefaultHttpClient();
        HttpGet get;
        if ("".equals(params) || params == null || StringUtil.isEmpty(params.toString())) {
            get = new HttpGet(ADDRESS_URL + address);
        } else {
            StringBuffer str = new StringBuffer();
            for (Map.Entry<String, Object> param : params.entrySet()) {
                str.append(param.getKey()).append("=").append(param.getValue()).append("&");
            }
            String param = str.substring(0, str.length() - 1);
            get = new HttpGet(ADDRESS_URL + address + "?" + param);
        }
        get.setHeader("rsid", rsid);
        HttpResponse response = client.execute(get);
        content = EntityUtils.toString(response.getEntity(), "UTF-8");
        return content;
    }
}
