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
                .antMatchers(HttpMethod.PUT, "/member/{id}/password").permitAll()
                .antMatchers(HttpMethod.GET,
                        "/member/findid",
                        "/member/profile",
                        "/member/check",
                        "/member/check/**",
                        "/packages/project/{projectId}",
                        "/files/projects/**",
                        "/api/projects/projects",
                        "/api/projects/{projectId}"
                ).permitAll()

                .antMatchers(HttpMethod.POST, "/member", "/member/login").permitAll()
                .anyRequest().authenticated().and()
                .logout()
//                .logoutUrl("/member/logout")
//                .logoutSuccessUrl("/members/login?logout")
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
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
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
