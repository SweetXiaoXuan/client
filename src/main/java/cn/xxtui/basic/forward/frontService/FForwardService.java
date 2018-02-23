package cn.xxtui.basic.forward.frontService;


import cn.xxtui.support.util.XXMediaType;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

@Component
@Path("/forward")
public class FForwardService {
    @Bean
    public FForwardService fForwardService() {
        return new FForwardService();
    }

    @GET
    @Produces({XXMediaType.TEXTUTF8})
    @Path("/get/{device}/{request}")
    public Response get(@PathParam("request") String pid,@PathParam("device") String device, @Context HttpRequest request) {
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet("http://www.hodays.com:8119/lv/api/lv/index/indexShare");
        //HttpHost host = new HttpHost("www.hodays.com",8119);
        String content="error";
        if("ios".equals(device)) {
            try {
                HttpResponse response = client.execute(get);
                content = EntityUtils.toString(response.getEntity(), "UTF-8");

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return Response.ok(content).build();
    }


}
