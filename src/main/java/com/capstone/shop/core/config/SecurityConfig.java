package com.capstone.shop.core.config;

import com.capstone.shop.core.security.*;
import com.capstone.shop.core.security.oauth2.CustomOAuth2UserService;
import com.capstone.shop.core.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.capstone.shop.core.security.oauth2.OAuth2AuthenticationFailureHandler;
import com.capstone.shop.core.security.oauth2.OAuth2AuthenticationSuccessHandler;

import com.capstone.shop.core.domain.repository.UserRefreshTokenRepository;
import com.capstone.shop.core.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)

public class SecurityConfig {
    private final TokenProvider tokenProvider;
    private final CustomUserDetailService customUserDetailsService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final UserRefreshTokenRepository userRefreshTokenRepository;
    private final UserRepository userRepository;
    @Bean
    public PreuserRedirectFilter preuserRedirectFilter() {
        return new PreuserRedirectFilter(userRepository);
    }
    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(tokenProvider, customUserDetailsService, userRefreshTokenRepository,userRepository);
    }
    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }
    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                "/",
                                "/ws/**",
                                "/app/**",
                                "ws/**",
                                "wss/**",
                                "/chat/**",
                                "/chat/websocket/**",
                                "/api/v1/chat/**",
                                "/api/v1/chat/**",
                                "/api/v1/chat/create",
                                "local/redirect",
                                "/signin",
                                "/api/v1/merchandise/*",
                                "/signup",
                                "/favicon.ico",
                                "/oauth2/authorize",
                                "/oauth2/authorization/*",
                                "/additional-info",
                                "/additionalInfo",
                                "/login/oauth2/code/*",
                                "/api/v1/**",
                                "static/**",
                                "assets/**"
                        ).permitAll()
                        .requestMatchers(
                                "/*.png",
                                "/*.gif",
                                "/*.svg",
                                "/*.jpg",
                                "/*.html",
                                "/*.css",
                                "/*.js"
                        ).permitAll()
                        .requestMatchers("/swagger", "/swagger-ui.html", "/swagger-ui/**", "/api-docs", "/api-docs/**", "/v3/api-docs/**")
                        .permitAll()
                        // api/v1/admin으로 시작하는 요청은 ADMIN 역할을 가진 사용자만 접근 가능
//                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                        //testing
                        //        .anyRequest().permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2Login ->
                        oauth2Login
                                .authorizationEndpoint(authorizationEndpoint ->
                                        authorizationEndpoint
                                                .baseUri("/oauth2/authorize")
                                                .authorizationRequestRepository(cookieAuthorizationRequestRepository())
                                )
                                .redirectionEndpoint(redirectionEndpoint ->
                                        redirectionEndpoint
                                                .baseUri("/login/oauth2/code/*")
                                )
                                .userInfoEndpoint(userInfoEndpoint ->
                                        userInfoEndpoint
                                                .userService(customOAuth2UserService)
                                )
                                .successHandler(oAuth2AuthenticationSuccessHandler)
                                .failureHandler(oAuth2AuthenticationFailureHandler)
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling.authenticationEntryPoint(new RestAuthenticationEntryPoint()));

        http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class); //인증 전에 필터
        http.addFilterAfter(preuserRedirectFilter(), TokenAuthenticationFilter.class);  //인증 후에 필터

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}