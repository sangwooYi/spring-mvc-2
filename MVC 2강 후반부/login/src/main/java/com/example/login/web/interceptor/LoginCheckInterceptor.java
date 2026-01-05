package com.example.login.web.interceptor;

import com.example.login.web.session.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;


// 스프링을 쓰는 이상
// 인터셉터를 쓰는게 더 편하다! ( 필터가 꼭 필요한 경우 아닌 이상 .. )
@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestURI = request.getRequestURI();
        log.info("인증 체크 interceptor 실행 {}", requestURI);

        HttpSession session = request.getSession(false);

        // 미인증
        if (session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
            log.info("미인증 사용자 요청 Interceptor");

            response.sendRedirect("/login?redirectURL="+requestURI);

            // false 로 반환해 주어야 그 이후 진행이 안된다 ( 컨트롤러 호출부터 안 됨 )
            return false;
        }

        return true;
    }
}
