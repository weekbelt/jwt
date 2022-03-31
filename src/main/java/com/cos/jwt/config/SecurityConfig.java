package com.cos.jwt.config;

import com.cos.jwt.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CorsFilter corsFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable();
        http
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)    // 세션 사용하지 않겠다.
            .and()
            .addFilter(corsFilter)      // @CrossOrigin(인증X), 시큐리티 필터에 등록 인증(O)
            .formLogin().disable()
            .httpBasic().disable()
            .addFilter(new JwtAuthenticationFilter(authenticationManager()))   // AuthenticationManager
            .authorizeRequests()
            .antMatchers("/api/v1/user/**")
            .access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
            .antMatchers("/api/v1/manager/**")
            .access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
            .antMatchers("/api/v1/admin/**")
            .access("hasRole('ROLE_ADMIN')")
            .anyRequest().permitAll()
        ;
    }
}
