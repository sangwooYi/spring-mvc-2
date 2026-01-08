package hello.exception.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// @ResponseStatus 에 설정된 HTTP STATUS 코드를 던져준다.
// 여기에 설정된 값은 response.sendError 에 각각 code , reason 값으로 전달됨
// + properties 파일에 공통값으로 정의해서 아래처럼 꺼내올수도 있음!
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "error.bad")
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }

}
