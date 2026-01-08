package hello.exception;

import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

// 그냥 이렇게 정해져 있다!
// 자바빈 관리되야하므로 @Component 반드시 들어가 있어야 함
//@Component
public class WebServerCustomizer implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {

    @Override
    public void customize(ConfigurableWebServerFactory factory) {

        // 이렇게 에러코드, 에러페이지 경로를 설정해주면 된다. ( 페이지가 아닌 서버 요청 경로임 !!)
        ErrorPage errorPage404 = new ErrorPage(HttpStatus.NOT_FOUND, "/error-page/404");
        ErrorPage errorPage500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error-page/500");
        ErrorPage errorPageEx = new ErrorPage(RuntimeException.class, "/error-page/500");

        // 이렇게 팩토리에 등록까지 해주면 끝
        factory.addErrorPages(errorPage404, errorPage500, errorPageEx);
    }
}