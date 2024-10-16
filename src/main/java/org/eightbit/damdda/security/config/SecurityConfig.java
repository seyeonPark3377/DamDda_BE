package org.eightbit.damdda.security.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.eightbit.damdda.security.jwt.AuthEntryPoint;
import org.eightbit.damdda.security.jwt.JwtService;
import org.eightbit.damdda.security.filter.JwtAuthenticationFilter;
import org.eightbit.damdda.security.filter.LoginFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Log4j2
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final AuthEntryPoint authEntryPoint;

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        LoginFilter loginFilter = new LoginFilter(authenticationManagerBean(), jwtService); // 로그인 필터
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtService, userDetailsService); // JWT 인증 필터

        http.csrf().disable()
                .cors().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST,
                        "/damdda/member", // 회원 정보 등록(회원가입)
                        "/damdda/member/login" // 로그인
                ).permitAll()
                .antMatchers(HttpMethod.GET,
                        "/damdda/member/findid", // 아이디 찾기
                        "/damdda/member/check", // 회원 정보 확인
                        "/damdda/member/check/**", // 아이디, 닉네임 중복 확인
                        "/damdda/projects/projects", // 프로젝트 목록 조회
                        "/damdda/files/projects/**", // 프로젝트 문서 및 이미지 조회
                        "/damdda/project/{projectId}", // 프로젝트 상세 조회
                        "/damdda/packages/{projectId}" // 프로젝트 선물 구성 조회
                ).permitAll()
                .antMatchers(HttpMethod.PUT,
                        "/damdda/member/{id}/password" // 비밀번호 수정
                ).permitAll()
                .anyRequest().authenticated().and()
                .logout()
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID").and()
                .addFilterBefore(loginFilter, UsernamePasswordAuthenticationFilter.class)  // 로그인 필터 추가
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)  // JWT 필터 추가
                .exceptionHandling().authenticationEntryPoint(authEntryPoint);
    }


    @Value("${app.cors.allowed-origins}")
    private String[] allowedOrigins;  // Allowed origins from external configuration

    /**
     * Configures global CORS settings for the application.
     *
     * @return CorsConfigurationSource the CORS configuration source
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // Set allowed origins from the external configuration
        config.setAllowedOrigins(Arrays.asList(allowedOrigins));

        // Allow specific HTTP methods and headers for cross-origin requests
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        config.setAllowedHeaders(List.of("*"));

        // Allow credentials (cookies, authorization headers)
        config.setAllowCredentials(true);

        // Log the CORS configuration
        log.debug("CORS allowed origins: {}", Arrays.toString(allowedOrigins));

        // Apply CORS configuration to all paths
        source.registerCorsConfiguration("/**", config);

        return source;
    }

}
