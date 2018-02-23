package cn.lv;

//sun.net.www.http.HttpClient

import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by newstart on 16/4/9.
 */
@WebServlet(name = "abc", urlPatterns = "/test")
public class ForwadServlet extends HttpServlet {

    private final Logger log= LogManager.getLogger(ForwadServlet.class);
    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        // http://www.qimiaodian.com:8119/lv/api/${lv/activity/contents}
        // Examp: http://localhost:8119/flvoice/lv/activity/contents
        // ---->: http://www.qimiaodian.com:8119/lv/api/lv/activity/contents
        HttpServletRequest httpRequest = (HttpServletRequest) req;

        String servletPath = httpRequest.getServletPath().replaceFirst("/", "");
        // 重新拼接新URL
        String httpUrl = URLConstants.API_QIMIAODIAN + servletPath;
        log.info("newUrl:  " + httpUrl);
        HttpClient httpClient = new DefaultHttpClient();
        String type=((HttpServletRequest) req).getMethod();
        HttpUriRequest request=null;
        if(type.equals("GET"))
        {
            request=new HttpGet(httpUrl);
            HttpGet get=(HttpGet) request;
        }
        else  if(type.equals("POST"))
        {
            request=new HttpPost(httpUrl);
            HttpPost post=(HttpPost) request;
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            for(Map.Entry<String,String[]> entry:req.getParameterMap().entrySet())
            {
                for(String v:entry.getValue()) {
                    nvps.add(new BasicNameValuePair(entry.getKey(),v));
                }
            }
            post.setEntity(new UrlEncodedFormEntity(nvps));
        }
        else
        {
            res.getOutputStream().print("Request Error");
            return;
        }
        HttpParams params=new BasicHttpParams();

        for(Map.Entry<String,String[]> entry:req.getParameterMap().entrySet())
        {
            System.out.println(entry.getKey()+":");
            for(String v:entry.getValue()) {
                System.out.print(v);
            }
            System.out.println();
            params.setParameter(entry.getKey(),entry.getValue());
        }
        request.setParams(params);
        HttpResponse response=httpClient.execute(request);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            /**读取服务器返回过来的json字符串数据**/
            String content = EntityUtils.toString(response.getEntity(), "UTF-8");
            res.setCharacterEncoding("UTF-8");
            if(log.isDebugEnabled())
            {
                log.debug(res.getCharacterEncoding());
                log.debug("response:");
                log.debug(content);
            }

            res.getOutputStream().write(content.getBytes());
        } else {
            res.getOutputStream().print("Error");
        }
    }
}
