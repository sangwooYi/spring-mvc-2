package com.example.login;

import com.example.login.web.argumentResolver.LoginMemberArgumentResolver;
import com.example.login.web.filter.LogFilter;
import com.example.login.web.filter.LoginCheckFilter;
import com.example.login.web.interceptor.LogInterceptor;
import com.example.login.web.interceptor.LoginCheckInterceptor;
import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // 필터 쓰려면 이렇게
    // @Configuration  랑 @Bean 으로 별도로 수동 빈 등록이 필요
    //@Bean
    public FilterRegistrationBean<? extends Filter> logFilter() {

        FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
        filterFilterRegistrationBean.setFilter(new LogFilter());
        filterFilterRegistrationBean.setOrder(1);           // 필터 순서 ( 이거로 체이닝 순서 결정, 낮은 숫자가 높은 우선순위)
        filterFilterRegistrationBean.addUrlPatterns("/*");  // 필터 거칠 url 패턴 ("/*" 면 그냥 전체)

        return filterFilterRegistrationBean;
    }

    //@Bean
    public FilterRegistrationBean<? extends Filter> loginCheckFilter() {

        FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
        filterFilterRegistrationBean.setFilter(new LoginCheckFilter());
        filterFilterRegistrationBean.setOrder(2);           // 필터 순서 ( 이거로 체이닝 순서 결정, 낮은 숫자가 높은 우선순위)
        filterFilterRegistrationBean.addUrlPatterns("/*");  // 필터 거칠 url 패턴 ("/*" 면 그냥 전체)

        return filterFilterRegistrationBean;
    }

    // 인터셉터는 WebMvcConfigurer 상속받아 addInterceptors Override 해주어야 함
    // 체크 경로 / 제외 경로를 인터셉터는 등록 단계에 아래처럼 구현해 주어야 함.
    // Bean 등록이 아니기때문에 이 하나 메서드안에 인터셉터 여러개 한번에 선언 가능
    // 필터는 Bean 등록이기 때문에 각각 해주어야 한다!
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor())
                .order(1)
                .addPathPatterns("/**")                                 // 체크 경로 이래야 전체 경로
                .excludePathPatterns("/css/**", "/*.ico", "/error");    // 제외 경로 설정

        registry.addInterceptor(new LoginCheckInterceptor())
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns("/", "/member/add", "/login", "/logout", "/css/**", "/*.ico");
    }

    // 내가 커스텀한 ArgumentResolver 등록방법!
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver());
    }
}
