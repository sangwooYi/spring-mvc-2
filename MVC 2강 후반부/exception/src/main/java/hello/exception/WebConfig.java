package hello.exception;

import hello.exception.filter.LogFilter;
import hello.exception.interceptor.LogInterceptor;
import hello.exception.resolver.MyHandlerExceptionResolver;
import hello.exception.resolver.UserHandlerExceptionResolver;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    //@Bean
    public FilterRegistrationBean<? extends Filter> logFilter() {
        FilterRegistrationBean<Filter> filterFilterRegistration = new FilterRegistrationBean<>();

        filterFilterRegistration.setFilter(new LogFilter());
        filterFilterRegistration.setOrder(1);
        filterFilterRegistration.addUrlPatterns("/*");
        // 해당 Dispatcher Type에 대해서만 해당 필터 호출된다는 설정
        filterFilterRegistration.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.ERROR);

        return filterFilterRegistration;
    }

    // 인터셉터 등록방법 잘 기억해두자.
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "/*.ico", "/error", "/error-page/**");
                // 이렇게 error-page 전부 설정부분에서 인터셉터 제외 가능 ( 이걸 통해, 요청이 한번만 발생하도록 설정 가능 )
    }

    // ExceptionResolvers 확장해서 등록하는 방법!
    @Override
    public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
        resolvers.add(new MyHandlerExceptionResolver());
        resolvers.add(new UserHandlerExceptionResolver());
    }

}
