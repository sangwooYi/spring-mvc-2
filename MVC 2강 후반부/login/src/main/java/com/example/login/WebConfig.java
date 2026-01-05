package com.example.login;

import com.example.login.web.filter.LogFilter;
import com.example.login.web.filter.LoginCheckFilter;
import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {

    // 필터 쓰려면 이렇게
    // @Configuration  랑 @Bean 으로 별도로 수동 빈 등록이 필요
    @Bean
    public FilterRegistrationBean<? extends Filter> logFilter() {

        FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
        filterFilterRegistrationBean.setFilter(new LogFilter());
        filterFilterRegistrationBean.setOrder(1);           // 필터 순서 ( 이거로 체이닝 순서 결정, 낮은 숫자가 높은 우선순위)
        filterFilterRegistrationBean.addUrlPatterns("/*");  // 필터 거칠 url 패턴 ("/*" 면 그냥 전체)

        return filterFilterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean<? extends Filter> loginCheckFilter() {

        FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
        filterFilterRegistrationBean.setFilter(new LoginCheckFilter());
        filterFilterRegistrationBean.setOrder(2);           // 필터 순서 ( 이거로 체이닝 순서 결정, 낮은 숫자가 높은 우선순위)
        filterFilterRegistrationBean.addUrlPatterns("/*");  // 필터 거칠 url 패턴 ("/*" 면 그냥 전체)

        return filterFilterRegistrationBean;
    }
}
