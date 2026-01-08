package hello.exception.resolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;


// 컨트롤러 에러 발생시, 서블릿으로 예외 전달되기 전에
// ExceptionResolver가 가로채서 먼저 처리가능한지 체크가 가능하다!
// 따라서 그냥 에러가 방치되어 WAS 로 던져지는 것이 아니라, 서버측에서 의도에 맞게 처리하여 반환이 가능!
@Slf4j
public class MyHandlerExceptionResolver implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

        try {
            // a instance of b a 가 b에 하위 자식인지 체크하는 것 (인스턴스)
            if (ex instanceof IllegalArgumentException) {
                log.info("IllegalArgumentException resolver to 400");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());

                return new ModelAndView();
            }
        } catch (IOException e) {
            log.error("Resolver IOException", e);
        } catch (Exception e) {
            log.error("Exception", e);
        }

        return null;
    }
}
