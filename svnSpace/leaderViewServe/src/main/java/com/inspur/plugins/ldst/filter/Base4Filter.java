package com.inspur.plugins.ldst.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter(filterName = "Base4Filter", urlPatterns = {"/*"})
public abstract class Base4Filter implements Filter {

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {

        MyHttpServletRequestWrapper myHttpServletRequestWrapper = new MyHttpServletRequestWrapper((HttpServletRequest) req);

        chain.doFilter(myHttpServletRequestWrapper, resp);
    }

}
