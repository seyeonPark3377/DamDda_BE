package org.eightbit.damdda.security.filter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class KakaoPayIpFilter implements Filter {

    // 카카오페이 서버의 허용된 IP 주소 리스트
    private static final List<String> ALLOWED_IPS = Arrays.asList(
            "121.53.178.16",
            "203.217.232.16",
            "113.29.182.165",
            "211.249.206.13",
            "121.53.178.18",
            "203.217.232.18",
            "0:0:0:0:0:0:0:1"
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestURI = httpRequest.getRequestURI();
        String remoteIp = httpRequest.getRemoteAddr();
        System.out.println("remoteIp: "+remoteIp);

        System.out.println("kakao log: " + requestURI);

        // 카카오페이 엔드포인트에만 필터 적용
        if (requestURI.startsWith("/payment/kakao/success") ||
                requestURI.startsWith("/payment/kakao/cancel") ||
                requestURI.startsWith("/payment/kakao/fail")) {
            System.out.println("kakao log: enter if문");

            // 허용된 IP 주소 중 하나와 일치하는지 확인
            if (ALLOWED_IPS.contains(remoteIp)) {
                // IP가 일치하면 요청을 처리
                chain.doFilter(request, response);
                System.out.println("kakao log: 일치");
            } else {
                // IP가 일치하지 않으면 403 Forbidden 응답
                System.out.println("불일치");
                httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            }
        } else {
            // 카카오페이 엔드포인트가 아니면 필터를 적용하지 않고 다음 필터로 넘김
            chain.doFilter(request, response);
            System.out.println("kakao log: pass");
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 필터 초기화 작업
    }

    @Override
    public void destroy() {
        // 필터 종료 시 작업
    }
}