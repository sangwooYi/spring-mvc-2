package hello.exception.servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/error-page")
public class ErrorPageController {

    // RequestDispatcher에 상수 정의되어 있음
    // +스프링 3버전 이후부터는 javax 가아니라 전부 jakarta 로 사용해야함!
    public static final String ERROR_EXCEPTION = "jakarta.servlet.error.exception";
    public static final String ERROR_EXCEPTION_TYPE = "jakarta.servlet.error.exception_type";
    public static final String ERROR_MESSAGE = "jakarta.servlet.error.message";
    public static final String ERROR_REQUEST_URI = "jakarta.servlet.error.request_uri";
    public static final String ERROR_SERVLET_NAME = "jakarta.servlet.error.servlet_name";
    public static final String ERROR_STATUS_CODE = "jakarta.servlet.error.status_code";

    @RequestMapping("/404")
    public String error404Page(HttpServletRequest request, HttpServletResponse response) {

        // 에러 발생되어 다시 컨트롤러 호출 된경우 request attribute 에 해당 정보를 담아준다.
        this.printErrorInfo(request);

        return "error-page/404";
    }

    @RequestMapping("/500")
    public String error500Page(HttpServletRequest request, HttpServletResponse response) {

        log.info("일반 500 에러 발생");
        this.printErrorInfo(request);

        return "error-page/500";
    }
    // produces 는 Request 헤더의 Accept 타입에 따라서 적절하게 판단 해줌
    // 우리 예시처럼 Accept 가 application/json 이라면 아래처럼 설정!
    @RequestMapping(value="/500", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> errorPage500Api(
            HttpServletRequest request, HttpServletResponse response) {

        log.info("JSON 에러 반환");

        Map<String, Object> resMap = new HashMap<>();
        Exception ex = (Exception) request.getAttribute(ERROR_EXCEPTION);
        resMap.put("status", request.getAttribute(ERROR_STATUS_CODE));
        resMap.put("message", ex.getMessage());

        Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        // 객체를 반환하는 방법 ResponseEntity 기억!!
        return new ResponseEntity<>(resMap, HttpStatusCode.valueOf(statusCode));
    }

    public void printErrorInfo(HttpServletRequest request) {
        log.info("ERROR_EXCEPTION = {}", request.getAttribute(ERROR_EXCEPTION));
        log.info("ERROR_EXCEPTION_TYPE = {}", request.getAttribute(ERROR_EXCEPTION_TYPE));
        log.info("ERROR_MESSAGE = {}", request.getAttribute(ERROR_MESSAGE));
        log.info("ERROR_REQUEST_URI = {}", request.getAttribute(ERROR_REQUEST_URI));
        log.info("ERROR_SERVLET_NAME = {}", request.getAttribute(ERROR_SERVLET_NAME));
        log.info("ERROR_STATUS_CODE = {}", request.getAttribute(ERROR_STATUS_CODE));
        
        // 서블릿이 호출된게 어떤 목적인지를 표기해주는 것
        log.info("dispatcher type = {}", request.getDispatcherType());
    }

}
