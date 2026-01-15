package hello.exception.api;

import hello.exception.exception.BadRequestException;
import hello.exception.exception.UserException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
public class ApiExceptionController {

    @GetMapping("/api/members/{id}")
    public MemberDto getMember(@PathVariable("id") String memberId) {
        if (memberId.equals("ex")) {
            throw new RuntimeException("RuntimeException 예외 발생!");
        }

        if (memberId.equals("bad")) {
            throw new IllegalArgumentException("IllegalArgumentException 예외 발생!");
        }

        if (memberId.equals("user-ex")) {
            throw new UserException("UserException 예외 발생!");
        }
        return new MemberDto(memberId, "hello " + memberId);
    }

    @GetMapping("/api/response-status-ex1")
    public String responseStatusEx1() {
        throw new BadRequestException("Bad Request 예외 발생!");
    }

    @GetMapping("/api/response-status-ex2")
    public String responseStatusEx2() {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "error.bad", new IllegalArgumentException());
    }

    @GetMapping("/api/default-handler-ex")
    public String defaultHandler(@RequestParam Integer data) {
        return "OK";
    }

    // 임시 DTO 내부클래스에 선언 (테스트용)
    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String memberId;
        private String name;
    }

}
