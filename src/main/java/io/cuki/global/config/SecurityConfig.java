package io.cuki.global.config;

import io.cuki.domain.member.entity.jwt.TokenProvider;
import io.cuki.global.error.JwtAccessDeniedHandler;
import io.cuki.global.error.JwtAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    public SecurityConfig(TokenProvider tokenProvider,
                          JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
                          JwtAccessDeniedHandler jwtAccessDeniedHandler
    ) {
        this.tokenProvider = tokenProvider;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/h2-console/**", "/favicon.ico",
                        "/v2/api-docs", "/configuration/**",
                        "/swagger*/**", "/webjars/**",
                        "/members/sign-up/email", "/members/sign-up/verification-code", "/members/sign-up",
                        "/auth/login/email", "/auth/login/verification-code", "/auth/login", "/auth/reissue"
                );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // csrf ????????? ?????? jwt ?????? ???????????? ????????? ???????????? disable
                .csrf().disable()

                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                // ?????? ?????????????????? iframe ????????? ??????????????? ??????
                .and()
                .headers()
                .frameOptions()
                .sameOrigin()

                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                .antMatchers("/members/sign-up/email", "/members/sign-up/verification-code", "/members/sign-up").permitAll()
                .antMatchers("/auth/login/email", "/auth/login/verification-code", "/auth/login").permitAll()
                .antMatchers("/auth/reissue").permitAll()
                .antMatchers("/schedules").permitAll()  //
//                .antMatchers("/slack").permitAll()
                .anyRequest().authenticated()

                // JwtSecurityConfig ????????? ??????
                .and()
                .apply(new JwtSecurityConfig(tokenProvider));
    }
}
