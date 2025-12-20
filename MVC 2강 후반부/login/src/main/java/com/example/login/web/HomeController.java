package com.example.login.web;

import com.example.login.domain.member.Member;
import com.example.login.domain.member.MemberRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;

    @GetMapping("/")
    public String home(HttpServletRequest request,
                       // 근데 스프링에서는 @CookieValue를 지원해줌! (알아서 뽑아서 세팅해준다 )
                       @CookieValue(name = "memberId", required = false) Long memberId, Model model) {
        // 그냥 일반적으로 쿠키 받을때는 이렇게
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("memberId")) {
                log.info("cookie Name = {}", cookie.getName() );
                log.info("cookie Value = {}", cookie.getValue() );
            }
        }

        Member loginMember = memberRepository.findById(memberId);

        if (loginMember == null) {
            return "home";
        }

        model.addAttribute("member", loginMember);

        log.info("여기 오냐?");
        return "loginHome";
    }

    @PostMapping("/logout")
    public String logout(HttpServletResponse response) {

        log.info("로그아웃요~");

        // 쿠키 만료시키려면 해당 기존 쿠키이름으로 maxAge 0 짜리 쿠키로 덮어써줘야함
        Cookie cookie = new Cookie("memberId", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        return "home";
    }
}
