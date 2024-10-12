package org.eightbit.damdda.member.filter;

import org.eightbit.damdda.member.domain.User;
import org.eightbit.damdda.member.service.JwtService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authorizationHeader = request.getHeader("x-damdda-authorization");
        Enumeration<String> headerNames = request.getHeaderNames();

        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            String value = request.getHeader(name);
            System.out.println(value);
            System.out.println();
            // "Bearer"가 포함된 헤더 값을 찾으면 출력하고 변수에 저장
            if (value != null && value.contains("Bearer")) {
                System.out.println(name + " : " + value);
                authorizationHeader = value;  // Bearer 토큰 값 저장
                System.out.println(authorizationHeader+"---------");
                break;

            }
        }

        if (authorizationHeader == null) {
            System.out.println("Bearer 토큰을 포함한 헤더가 없습니다.");
        } else {
            // 추출된 Bearer 토큰을 처리할 로직 추가
            System.out.println("추출된 Authorization Header: " + authorizationHeader);
        }
//        Enumeration<String> haederNames = request.getHeaderNames();
//        for( ; haederNames.hasMoreElements(); ) {
//            String name = haederNames.nextElement();
//            String value = request.getHeader(name);
//            if(value.contains("Bearer")){
//                System.out.println(name + " : " + value);
//                authorizationHeader = value;
//            }
////            System.out.println(name + " : " + value);
//        }

        String token = null;
        String username = null;
        Long memberId = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // "Bearer " 이후 토큰 값만 추출
            System.out.println(token+"++++++++++");
            username = jwtService.getAuthUser(request); // 토큰에서 사용자 이름 추출
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            User user = (User) userDetailsService.loadUserByUsername(username);
            memberId = user.getMember().getId();

            if (jwtService.validateToken(token)) { // 토큰 유효성 검증
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken); // 인증 정보 설정
            }
        }

        request.setAttribute("memberId", memberId);
        request.setAttribute("username", username);

        System.out.println("Request Info: " + memberId + ", " + username);
        filterChain.doFilter(request, response);
    }
}

