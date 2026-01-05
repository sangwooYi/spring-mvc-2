package com.example.login.web.filter;

// Filter 받을 때 꼭 이 Filter 여야한다!
import com.example.login.web.session.SessionConst;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import java.io.IOException;

@Slf4j
public class LoginCheckFilter implements Filter {

    // 허용 URL 목록
    private static final String[] whiteList = {"/", "/members/add", "/login", "/logout", "/css/*"};

    // doFilter 만 구현하면 된다! ( 나머지는 디폴트 존재 )
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String requestURI = httpServletRequest.getRequestURI();

        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        try {
            log.info("인증 체크필터 시작 {}", requestURI);

            if (this.isLoginCheckPath(requestURI)) {
                log.info("인증 체크 로직 실행 {}", requestURI);

                HttpSession session = httpServletRequest.getSession(false);
                if (session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
                    log.info("미인증 사용자 접근 {}", requestURI);
                    
                    // 로그인 페이지로 리다이렉트  sendRedirect 사용
                    httpServletResponse.sendRedirect("/login?redirectURL=" + requestURI);

                    return;
                }
                log.info("인증 성공 {}", requestURI);
            }
            chain.doFilter(request, response);
        } catch (Exception e) {
            throw e;
        } finally {
            log.info("loginCheck Filter finally");
        }
    }

    /**
     * 화이트 리스트에 포함될 경우 인증체크 패스
     * 경로에 없는 경우만 체크
     * 따라서 white list 에 포함 -> false (체크 패스)
     * 없는 경우 -> true 반환 ( 체크 )
     *
     */
    private boolean isLoginCheckPath(String requestURI) {

        // whiteList에 없는 경우가 체크 ( 따라서 뒤집어 반환 )
        return !PatternMatchUtils.simpleMatch(whiteList, requestURI);
    }
}
