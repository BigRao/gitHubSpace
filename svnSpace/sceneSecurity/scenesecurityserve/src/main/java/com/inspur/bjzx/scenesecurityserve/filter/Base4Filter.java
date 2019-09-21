package com.inspur.bjzx.scenesecurityserve.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

//@WebFilter(filterName = "Base4Filter", urlPatterns = {"/*"})
public class Base4Filter implements Filter {
    //@Value("${tokenURL}")
    //String url;

    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        //System.err.println("进入filter");
        ////System.out.println("tokenurl====" + url);
        //HttpServletRequest request = (HttpServletRequest) req;
        //HttpServletResponse response = (HttpServletResponse) resp;
        //response.setHeader("Access-Control-Allow-Origin", "*");
        //response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        //response.setHeader("Access-Control-Max-Age", "3600");
        //response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
        //response.setCharacterEncoding("UTF-8");
        //response.setContentType("text/html;charset=utf-8");
        ////String token = request.getParameter("token");
        //String useraccount = request.getParameter("useraccount");
        ////token="1888c085008b35c007415fd932b1e970";
        //// useraccount="c3VwcG9ydA==";
        ////String url="http://localhost:8081/authority/v1/isActive";
        //String ans = "{\"useraccount\":\"" + useraccount + "\"}";
        //RestTemplate restTemplate = new RestTemplate();
        //MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<>();
        //requestEntity.add("param", ans);
        //String responseans = restTemplate.postForObject( "",requestEntity, String.class);
        //System.out.println("responseans==============" + responseans);
        //Gson g = new Gson();
        //Map<String, String> map = g.fromJson(responseans, HashMap.class);
        //if ("1".equals(map.get("result"))) {
        //    Map<String, Object> result = new HashMap<String, Object>();
        //    response.setStatus(500);
        //    PrintWriter out = response.getWriter();
        //    out.write("{\"message\":\"会话过期\",\"islader\":\"NO\"}");
        //    return;
        //}
        //MyHttpServletRequestWrapper myHttpServletRequestWrapper = new MyHttpServletRequestWrapper((HttpServletRequest) req);
        //
        //chain.doFilter(myHttpServletRequestWrapper, resp);
    }

    public void init(FilterConfig config) throws ServletException {

    }


}
