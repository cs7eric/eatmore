package com.cs7eric.eatmore.filter;

import com.alibaba.fastjson.JSON;
import com.cs7eric.eatmore.common.R;
import com.cs7eric.eatmore.util.BaseContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录检查过滤器
 *
 * @author cs7eric
 * @date 2023/01/13
 */

@Slf4j
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
public class LoginCheckFilter implements Filter {

    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        // 1. 获取本次请求的 URI
        String requestURI = httpServletRequest.getRequestURI();
        log.info("拦截到请求：{}",requestURI);


        // 定义不需要处理的请求路径
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**"
        };

        // 2. 判断本次请求是否需要处理
        boolean check = check(urls, requestURI);

        // 3. 如果不需要处理，则直接放行
        if(check){
            log.info("本次请求{}不需要处理",requestURI);
            filterChain.doFilter(httpServletRequest,httpServletResponse);
            return;
        }

        // 4. 判断登录状态，如果已登录，则直接放行
        if(httpServletRequest.getSession().getAttribute("employee") != null){
            log.info("用户已登录，用户id为：{}", httpServletRequest.getSession().getAttribute("employee"));
            BaseContext.setCurrentId((Long) httpServletRequest.getSession().getAttribute("employee"));
            filterChain.doFilter(httpServletRequest,httpServletResponse);
            return;
        }

        log.info("用户未登录");
        // 5. 如果未登录，则返回未登录状态
        httpServletResponse.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
    }

    public boolean check(String[] urls,String URI) {

        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url,URI);
            if(match){
                return true;
            }
        }
        return false;
    }
    
}
