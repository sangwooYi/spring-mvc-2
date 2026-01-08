package hello.exception.filter;

// 이 Filter를 써야한다!
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.UUID;

@Slf4j
public class LogFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        log.info("Filter init!");
    }
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();
        String uuid = UUID.randomUUID().toString();

        try {
            log.info("REQUEST  [uuid={}][DispatcherType={}][URI={}]", uuid, request.getDispatcherType(), requestURI);
            // doFilter 가 필수!!
            chain.doFilter(request, response);
        } catch (Exception e) {
            throw e;
        }finally {
            log.info("RESPONSE [uuid={}][DispatcherType={}][URI={}]", uuid, request.getDispatcherType(), requestURI);
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
        log.info("Filter destroy!");
    }

}
