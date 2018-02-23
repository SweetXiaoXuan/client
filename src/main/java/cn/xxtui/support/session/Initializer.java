package cn.xxtui.support.session;

import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * Created by Administrator on 2017/8/11 0011.
 *
 * 第一步是延伸AbstractHttpSessionApplicationInitializer。这样可以确保Spring Bean的名称springSessionRepositoryFilter已经在Servlet容器中注册了。
 */

public class Initializer extends AbstractHttpSessionApplicationInitializer {

    /**
     *  AbstractHttpSessionApplicationInitializer还提供了一种机制，以便轻松确保Spring加载我们的Config
     */
    public Initializer() {

        super(Config.class);
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {

        servletContext.addListener("cn.xxtui.support.util.ApplicationStart");
        ServletRegistration.Dynamic dynamic=servletContext.addServlet("CXFServlet","org.apache.cxf.transport.servlet.CXFServlet");
        dynamic.addMapping("/api/*");
        dynamic.setLoadOnStartup(1);
        super.onStartup(servletContext);
    }
}