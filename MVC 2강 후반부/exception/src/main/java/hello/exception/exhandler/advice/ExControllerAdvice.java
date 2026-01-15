package hello.exception.exhandler.advice;

import hello.exception.exception.UserException;
import hello.exception.exhandler.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// @ExceptionHandler 공통 선언하는 방법
@Slf4j
@RestControllerAdvice
/**
 * ControllerAdvice 적용 클래스 타겟 지정 방법들
 *
 * 1. 어노테이션 기준 타겟 지정
 * @ControllerAdvice(annotations = RestController.class)
 *
 * 2. 대상패키지 경로 지정
 * @ControllerAdvice("org.example.controllers")
 *
 * 3. 직접 클래스 지정 ( 부모 클래스로 지정 가능 )
 * @ControllerAdvice(assignableTypes = {ControllerInterface.class, AbstractController.class})
 */
public class ExControllerAdvice {

    // HTTP 응답코드 설정 방법 1 @ResponseStatus 사용
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    // @ExceptionHandler 에 전달되는 클래스와, 메서드에서 받는 인자가 같은 클래스면 생략 가능!
    // 그냥 @ExceptionHandler 라고만 써도 됨!
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult illegalExHandler(IllegalArgumentException ex) {
        log.error("[exception handler]", ex);
        return new ErrorResult("BAD", ex.getMessage());
    }

    // HTTP 응답코드 설정방법 2 ResponseEntity 사용
    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorResult> userExHandler(UserException ex) {
        log.error("[user exception handler] ", ex);

        ErrorResult errorResult = new ErrorResult("USER-EX", ex.getMessage());
        // 반환타입이 ResponseEntity<ErrorResult>
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResult exHandler(Exception ex) {
        log.error("[Tada Ex Handler] ", ex);

        return new ErrorResult("ERROR", ex.getMessage());
    }
}
