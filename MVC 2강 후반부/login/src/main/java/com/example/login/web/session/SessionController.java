package com.example.login.web.session;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@Slf4j
// 얘는 이제 그냥 string 반환하면 그걸 그대로 바디에 뿌려줌
// 그냥 Ajax 반환용이나, 바디 값 받아서 처리하는 용으로 할떄는 이거 붙여줘도 됨!
@RestController
public class SessionController {

    @GetMapping("/session-info")
    public String sessionInfo(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            return "session 없다 임마";
        }
        session.getAttributeNames().asIterator()
                .forEachRemaining( name -> log.info("session name = {}, value = {}", name, session.getAttribute(name)));

        log.info("sessionId = {}", session.getId());

        // 유효시간, 디폴트 1800 ( 1800초 )
        // 참고로 변수명대로, 비활성화 시간 기준임 ( 최근 요청시간 기준으로 N초 유효기간 설정 )
        log.info("maxInactiveInterval = {} ", session.getMaxInactiveInterval());
        log.info("creationTIme = {}", new Date(session.getCreationTime()));
        log.info("lastAccessedTime = {}", new Date(session.getLastAccessedTime()));
        log.info("isNew = {} ", session.isNew());

        return "세션 확인";
    }
}
