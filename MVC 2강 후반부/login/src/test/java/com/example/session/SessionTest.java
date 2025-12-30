package com.example.session;

import com.example.login.domain.member.Member;
import com.example.login.web.session.SessionManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;


public class SessionTest {

    SessionManager sessionManager = new SessionManager();

    @Test
    void sessionTest() {

        // 테스트용 MockHttpServletRequest / HttpServletResponse !!
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        Member member = new Member("aaa", "bbb", "123123");
        sessionManager.createSession(member, response);

        // 응답 쿠키 저장
        request.setCookies(response.getCookies());

        Object result = sessionManager.getSession(request);

        Assertions.assertThat(result).isEqualTo(member);

        // 만료
        sessionManager.expireCookie(request, response);

        Object result2 = sessionManager.getSession(request);
        Assertions.assertThat(result2).isNull();
    }

}
