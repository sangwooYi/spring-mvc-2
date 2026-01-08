package hello.exception.servlet;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Slf4j
@Controller
public class ServletController {


    @GetMapping("/error-ex")
    public void errorEx() {
        throw new RuntimeException("예외 발생 TEST!!");
    }

    @GetMapping("/error-404")
    public void error404(HttpServletResponse response) throws IOException {
        // 에러코드, 에러메시지 전달 가능 ( 에러코드만 전달해도 OK )
        // 에러메시지는 인자로 전달해도 기본적으로는 숨겨지고 ( 물론 꺼내는 방법이 있음 )
        // 해당 코드에 맞는 HTTP 에러메시지가 디폴트로 나감
        // 에러코드가 존재하지 않는 코드면 그냥 500 에러 나감
        response.sendError(404, "그냥 임의의 오류!");
    }

}
