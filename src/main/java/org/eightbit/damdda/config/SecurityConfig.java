package org.eightbit.damdda.config;

import lombok.RequiredArgsConstructor;
import org.eightbit.damdda.member.except.AuthEntryPoint;
import org.eightbit.damdda.member.filter.JwtAuthenticationFilter;
import org.eightbit.damdda.member.filter.KakaoPayIpFilter;
import org.eightbit.damdda.member.filter.LoginFilter;
import org.eightbit.damdda.member.service.JwtService;
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

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
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
        KakaoPayIpFilter kakaoPayIpFilter = new KakaoPayIpFilter();


        http.csrf().disable()
                .cors().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers("/payment/toss/success/**","/payment/kakao/success/**", "/payment/kakao/cancel/", "/payment/kakao/fail/**").permitAll()
                .antMatchers(HttpMethod.GET, "/member/check/id", "/member/check/nickname", "/member/findid","/packages/project/{projectId}", "/files/projects/**", "/api/projects/projects","/api/projects/{projectId}").permitAll()
                .antMatchers(HttpMethod.POST, "/member/profile", "/member/login").permitAll()
                .antMatchers("/api/**").permitAll()
                .anyRequest().authenticated().and()
                .logout()
                .logoutUrl("/member/logout")
//                .logoutSuccessUrl("/members/login?logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID").and()
                .addFilterBefore(kakaoPayIpFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(loginFilter, UsernamePasswordAuthenticationFilter.class)  // 로그인 필터 추가
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)  // JWT 필터 추가
                .exceptionHandling().authenticationEntryPoint(authEntryPoint);
    }


    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:3000"); // 프론트엔드 주소
//        config.setAllowedOrigins(List.of("*"));
        config.setAllowedMethods(List.of("*"));
        config.setAllowCredentials(true);
        config.applyPermitDefaultValues();

        source.registerCorsConfiguration("/**", config);
        return source;
    }
}