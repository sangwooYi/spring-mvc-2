package com.example.login.web.session;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionManager {

    // 동시성 이슈 있을때는 HashMap 보다는 ConcurrentHashMap 을 사용할것!
    private Map<String, Object> sessionStore = new ConcurrentHashMap<>();
    private static final String SESSION_COOKIE_NAME = "mySession";

    /**
     *  세션 생성
     */
    public void createSession(Object value, HttpServletResponse response) {

        // UUID 값 만드는 방법 기억하자!
        String sessionId = UUID.randomUUID().toString();
        sessionStore.put(sessionId, value);

        // 쿠키 생성
        Cookie cookie = new Cookie(SESSION_COOKIE_NAME, sessionId);
        cookie.setMaxAge(30*60);    // 30분, 초 단위임
        cookie.setPath("/");

        response.addCookie(cookie);
    }

    /**
     *  세션 조회
     */
    public Object getSession(HttpServletRequest request) {
        Cookie cookie = this.findCookie(request, SESSION_COOKIE_NAME);

        Object result = null;
        if (cookie != null) {
            result = sessionStore.get(cookie.getValue());
        }
        return result;
    }

    /**
     * 세션 만료
     */
    public void expireCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie sessioCookie = this.findCookie(request, SESSION_COOKIE_NAME);

        if (sessioCookie != null) {
            sessionStore.remove(sessioCookie.getValue());

            Cookie cookie = new Cookie(SESSION_COOKIE_NAME, "");
            cookie.setMaxAge(0);
            sessioCookie.setPath("/");
            response.addCookie(cookie);
        }
    }


    public Cookie findCookie(HttpServletRequest request, String cookieName) {

        Cookie[] cookies = request.getCookies();

        Cookie result = null;

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(SESSION_COOKIE_NAME)) {
                result = cookie;
                break;
            }
        }
        return result;
    }
}
