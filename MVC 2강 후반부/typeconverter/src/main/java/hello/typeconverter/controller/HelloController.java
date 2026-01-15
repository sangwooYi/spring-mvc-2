package hello.typeconverter.controller;

import hello.typeconverter.type.IpPort;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class HelloController {

    @GetMapping("/hello-v1")
    public String helloV1(HttpServletRequest request) {

        String data = request.getParameter("data");

        Integer intValue = Integer.valueOf(data);
        log.info("intValue={}", intValue);
        return "V1 OK";
    }

    @GetMapping("/hello-v2")
    public String helloV2(@RequestParam Integer data) {
        log.info("data = {}", data);
        return "V2 OK";
    }

    @GetMapping("/ip-port")
    public String ipPort(@RequestParam IpPort ipPort) { // @ModelAttribute 로 받아도 받아진다!
        log.info("ipPort = {}", ipPort);

        return "ipPort OK";
    }
}
