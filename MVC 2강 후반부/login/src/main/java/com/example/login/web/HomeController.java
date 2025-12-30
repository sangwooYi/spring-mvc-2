package com.example.login.web;

import com.example.login.domain.member.Member;
import com.example.login.domain.member.MemberRepository;
import com.example.login.web.session.SessionConst;
import com.example.login.web.session.SessionManager;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.Session;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;
    private final SessionManager sessionManager;

    @GetMapping("/123213123")
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

    @GetMapping("/12312311111")
    public String homeLogin(HttpServletRequest request,
                       // 근데 스프링에서는 @CookieValue를 지원해줌! (알아서 뽑아서 세팅해준다 )
                       @CookieValue(name = "memberId", required = false) Long memberId, Model model) {

        // sessionManager 로 대체 가능
        Member loginMember = (Member) sessionManager.getSession(request);

        if (loginMember == null) {
            return "home";
        }

        model.addAttribute("member", loginMember);

        log.info("여기 오냐?");
        return "loginHome";
    }

    @GetMapping("/vqa11")
    public String homeLoginV3(HttpServletRequest request,
                            // 근데 스프링에서는 @CookieValue를 지원해줌! (알아서 뽑아서 세팅해준다 )
                            @CookieValue(name = "memberId", required = false) Long memberId, Model model) {

        log.info("오니? = {}", memberId);

        // 실제로 Session 을 생성해야하는 곳이 아니라면 false 로 받아야하는 부분 주의!
        // true -> 현재 세션 없으면 새로 생성 (디폴트)  // false -> 현재 세션 없으면 null 반환
        HttpSession session = request.getSession(false);

        if (session == null) {
            return "home";
        }

        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

        if (loginMember == null) {
            return "home";
        }

        model.addAttribute("member", loginMember);

        return "loginHome";
    }

    // @SessionAttribute 사용. name 에 해당하는 key 값을 통해 해당 Key에 해당하는 세션값을 꺼내서 담아준다.
    // 주의할 점은 Value 값이 뭔지 잘 기억해야 함! ( 우리는 세션에 Member 를 담았으니까 아래처럼 선언이 가능 한 것 )
    @GetMapping("/")
    public String homeLoginV3Spring(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model) {

        if (loginMember == null) {
            return "home";
        }

        model.addAttribute("member", loginMember);

        return "loginHome";
    }


    @PostMapping("/logout22")
    public String logout(HttpServletRequest request, HttpServletResponse response) {

        sessionManager.expireCookie(request, response);
        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logoutV3(HttpServletRequest request, HttpServletResponse response) {

        // 없으면 null 반환, 있으면 현재 세션 반환
        HttpSession session = request.getSession(false);

        if (session != null) {
            // 세션 만료시키는것 ( 서버에서는 삭제 / 클라이언트와 unbind 시켜 줌 )
            // 따라서 클라이언트 개발자 도구에서는 해당 세션이 보인다 ( 근데 아무 의미없는 dummy 쿠키가 된 것 )
            session.invalidate();
        }

        return "redirect:/";
    }
}
