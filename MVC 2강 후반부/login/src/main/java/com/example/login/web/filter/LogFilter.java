package com.example.login.web.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.UUID;


// 필터가 있는경우 항상 필터를 먼저 거치고 나서 서블릿이 호출된다.
// HTTP 요청 -> WAS -> 필터 -> 서블릿 -> 컨트롤러
@Slf4j
public class LogFilter implements Filter {

    
    // 필터 초기화, 서블릿 컨테이너 생성 때 1회 실행 ( 따라서, 싱글톤임 )
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        log.info("log filter init");
    }

    // 필터 로직이 들어가는 부분, 고객 요청이 올때마다 얘가 호출 됨
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        log.info("log filter doFilter");

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();

        String uuId = UUID.randomUUID().toString();

        try {
            log.info("Request [{}] [{}]", uuId, requestURI);
            
            // 다음 필터 있으면, 다음 필터로 연결, 없으면 서블릿 호출
            chain.doFilter(request, response);
        } catch (Exception e) {
            throw e;
        } finally {
            log.info("Response [{}] [{}]", uuId, requestURI);
        }

        // FilterChain 의 doFilter 를 이용해서 필터를 체이닝 가능 ( ex 로그 필터 -> 로그인 필터 식으로 )
        //chain.doFilter();
    }

    // 서블릿 컨테이너 종료 될때 호출
    @Override
    public void destroy() {
        Filter.super.destroy();
        log.info("log filter destroy");
    }
}
