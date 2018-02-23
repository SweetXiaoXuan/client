package cn.xxtui.support.util;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.SessionCookieConfig;

public class ApplicationStart implements ServletContextListener {

    private Logger log = LogManager.getLogger("ApplicationStart");

    public void contextInitialized(ServletContextEvent sce) {
        SessionCookieConfig sessionConfig = sce.getServletContext().getSessionCookieConfig();
        sessionConfig.setPath("/");
        if (log.isInfoEnabled()) {
            log.info("start up system");
        }
        SystemConf.init();
    }

    public void contextDestroyed(ServletContextEvent sce) {


    }

}
