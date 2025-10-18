package hello.thymeleafbasic.basic;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/basic")
public class BasicController {

    @GetMapping("/text-basic")
    public String textBasic(Model model) {

        model.addAttribute("data", "Hello Spring!!");
        model.addAttribute("udata", "Hello <b>Spring</b>!!");


        return "basic/text-basic";
    }

    @GetMapping("/text-unescaped")
    public String textUnescaped(Model model) {

        model.addAttribute("udata", "Hello <b>Spring</b>!!");

        return "basic/text-unescaped";
    }

    @GetMapping("/variable")
    public String variable(Model model) {

        User userA = new User("이상우", 34);
        User userB = new User("아이묭", 30);
        User userC = new User("이케다", 36);

        List<User> userList = new ArrayList<>();
        userList.add(userA);
        userList.add(userB);
        userList.add(userC);

        Map<String, User> userMap = new HashMap<>();
        userMap.put("userA", userA);
        userMap.put("userB", userB);
        userMap.put("userC", userC);

        model.addAttribute("user", userA);
        model.addAttribute("users", userList);
        model.addAttribute("userMap", userMap);

        return "basic/variable";
    }

    @GetMapping("/basic-objects")
    public String basicObjects(HttpServletRequest request,
                               HttpServletResponse response,
                               Model model,
                               HttpSession httpSession,
                               @RequestParam String paramData) {

        log.info(paramData);
        
        // 세션 값 할당
        httpSession.setAttribute("sessionData", "Hello Session");
        
        // 모델 값 할당
        model.addAttribute("request", request);
        model.addAttribute("response", response);
        model.addAttribute("request", request);
        model.addAttribute("session", httpSession);
        model.addAttribute("servletContext", request.getServletContext());

        return  "basic/basic-objects";
    }

    @GetMapping("/date")
    public String date(Model model) {

        log.info("LocalDateTime now = {}", LocalDateTime.now());

        model.addAttribute("localDatetime", LocalDateTime.now());

        return "basic/date";
    }

    // @ComponentScan 이 @Component 를 전부 스캔해서 자동 빈 등록해줌
    @Component("helloBean")
    static class HelloBean {
        public String hello(String data) {
            return String.format("Hello %s", data);
         }
    }
}
