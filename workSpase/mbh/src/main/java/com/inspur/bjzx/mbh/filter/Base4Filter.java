package com.inspur.bjzx.mbh.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter(filterName = "Base4Filter", urlPatterns = {"/*"})
public class Base4Filter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {

        MyHttpServletRequestWrapper myHttpServletRequestWrapper = new MyHttpServletRequestWrapper((HttpServletRequest) req);

        chain.doFilter(myHttpServletRequestWrapper, resp);
    }

    @Override
    public void destroy() {

    }
}
