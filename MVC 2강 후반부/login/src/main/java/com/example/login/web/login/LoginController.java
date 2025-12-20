package com.example.login.web.login;

import com.example.login.domain.login.LoginForm;
import com.example.login.domain.login.LoginService;
import com.example.login.domain.member.Member;
import com.example.login.domain.member.MemberRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm loginForm) {

        return "login/loginForm";
    }

    @PostMapping("/login")
    public String login(HttpServletRequest request, HttpServletResponse response,
                            @Validated @ModelAttribute("loginForm") LoginForm form, BindingResult bindingResult) {
    
        // 그냥 일반적으로 쿠키 받을때는 이렇게
        Cookie[] cookies = request.getCookies();

        for (Cookie cookie : cookies) {

            if (cookie.getName().equals("memberId")) {
                log.info("cookie Name = {}", cookie.getName() );
                log.info("cookie Value = {}", cookie.getValue() );
            }

        }

        if (bindingResult.hasErrors()) {
            return "login/loginForm";
        }

        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());

        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 혹은 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        // 로그인 성공
        log.info("로그인 성공 = {}", loginMember);

        // 세션 시간 값 (setMaxAge) 안정해주면 그냥 세션쿠키가 됨
        Cookie cookie = new Cookie("memberId", String.valueOf(loginMember.getId()));
        cookie.setPath("/");
        response.addCookie(cookie);

        return "redirect:/";
    }
}
