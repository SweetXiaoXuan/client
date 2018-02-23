package cn.xxtui.support.session;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.HttpSessionStrategy;

/**
 * Created by Administrator on 2017/8/11 0011.
 *
 * 该@EnableRedisHttpSession注释创建Spring Bean与名springSessionRepositoryFilter实现过滤。
 * 过滤器是负责替换HttpSessionSpring Session支持的实现的过滤器。在这种情况下，Spring Session由Redis支持。
 */
@Configuration
@ImportResource("classpath:application.xml")
@EnableRedisHttpSession
public class Config {

    private String host="api.hodays.com";
    private int port=6379;
    /**
     * 我们创建一个RedisConnectionFactory将Spring Session连接到Redis Server的连接。
     * 我们配置连接以连接到默认端口上的localhost（6379）
     * @return
     */
    @Bean
    public LettuceConnectionFactory connectionFactory() {
        LettuceConnectionFactory let= new LettuceConnectionFactory(host,port);
        let.setPassword("hodays");
        return let;
    }
    @Bean
    public HttpSessionStrategy httpSessionStrategy() {
//        return new HeaderHttpSessionStrategy();
//        return new CookieHttpSessionStrategy();
        return new RSIDHttpSessionStrategy();
    }


}