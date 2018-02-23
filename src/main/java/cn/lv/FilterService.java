package cn.lv;

import javax.servlet.*;
import java.io.IOException;

/**
 * Created by newstart on 16/4/9.
 */
//@WebFilter(urlPatterns = "/*")
public class FilterService implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        new ForwadServlet().service(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
