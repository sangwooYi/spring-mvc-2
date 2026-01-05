package com.example.login.web.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Slf4j
public class LogInterceptor implements HandlerInterceptor {

    private static final String LOG_ID = "logId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        String uuid = UUID.randomUUID().toString();
        request.setAttribute(LOG_ID, uuid);

        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
        }

        log.info("REQUEST [{}] [{}] [{}]", requestURI, uuid, handler);
        
        // true 가 반환되어야 다음단계로 진행, false 로 반환되는 경우 컨트롤러 호출도 안 됨.
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("postHandle {}", modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String requestURI = request.getRequestURI();
        String uuid = (String) request.getAttribute(LOG_ID);
        log.info("afterCompletion RESPONSE [{}] [{}] [{}]", requestURI, uuid, handler);

        if (ex != null) {
            log.error("afterCompletion ERROR : ", ex);
        }
    }


}
