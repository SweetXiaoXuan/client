package cn.xxtui.user.service.smsSpi;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

/**
 * 短信验证相关方法
 * @author liumengwei
 * @Time 2017/8/2
 * @since V1.0
 */
public class SmsVerifyKit {
    public static final String ADDRESS = "https://webapi.sms.mob.com/sms/verify";
    public static final String APPKEY = "1fe939a369f27";


    public static String result(String phone, String code) {
        StringBuffer params = new StringBuffer();
        params.append("appkey=").append(APPKEY);
        params.append("&phone=").append(phone);
        params.append("&zone=").append(86);
        params.append("&code=").append(code);
        String validationResults = requestData(ADDRESS, params.toString());
        return validationResults;
    }

    /**
     * 发起https 请求
     * @param address
     * @param params
     * @return
     */
    public static String requestData(String address ,String params){
        HttpURLConnection conn = null;
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager(){
                public X509Certificate[] getAcceptedIssuers(){return null;}
                public void checkClientTrusted(X509Certificate[] certs, String authType){}
                public void checkServerTrusted(X509Certificate[] certs, String authType){}
            }};
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());
            HostnameVerifier hv = new HostnameVerifier() {
                public boolean verify(String urlHostName, SSLSession session) {
                    return urlHostName.equals(session.getPeerHost());
                }
            };
            HttpsURLConnection.setDefaultHostnameVerifier(hv);
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            URL url = new URL(address);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(3000);
            if (params!=null) {
                conn.setDoOutput(true);
                DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                out.write(params.getBytes(Charset.forName("UTF-8")));
                out.flush();
                out.close();
            }
            conn.connect();
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String result = parsRtn(conn.getInputStream());
                return result;
            } else {
                System.out.println(conn.getResponseCode() + " "+ conn.getResponseMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null)
                conn.disconnect();
        }
        return null;
    }

    private static String parsRtn(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuffer buffer = new StringBuffer();
        String line;
        boolean first = true;
        while ((line = reader.readLine()) != null) {
            if(first){
                first = false;
            }else{
                buffer.append("\n");
            }
            buffer.append(line);
        }
        return buffer.toString();
    }
}
