//package com.todo.join.config;
//
//
//import com.todo.join.jwt.CustomLogoutFilter;
//import com.todo.join.jwt.JWTFilter;
//import com.todo.join.jwt.JWTUtil;
//import com.todo.join.jwt.LoginFilter;
//import com.todo.join.oauth.CustomOAuth2UserService;
//import com.todo.join.oauth.CustomOAuthSuccessHandler;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.security.web.authentication.logout.LogoutFilter;
//
//@Configuration
//@EnableWebSecurity
//@RequiredArgsConstructor
//public class SecurityConfig {
//
//    //AuthenticationManager가 인자로 받을 AuthenticationConfiguraion 객체 생성자 주입
//    private final AuthenticationConfiguration authenticationConfiguration;
//    private final JWTUtil jwtUtil;
//    private final CustomOAuth2UserService customOAuth2UserService;
//    private final CustomOAuthSuccessHandler customOAuthSuccessHandler;
//
//    //AuthenticationManager Bean 등록
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
//
//        return configuration.getAuthenticationManager();
//    }
//
//
//    @Bean
//    public BCryptPasswordEncoder bCryptPasswordEncoder() {
//
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
//
//        // csrf disable
//        http.csrf((auth) -> auth.disable());
//
//        // Form 로그인 방식 disable
//        http.formLogin((auth) -> auth.disable());
//
//        http.logout((auth) -> auth.disable());
//
//        // http basic 인증 방식 disable
//        http.httpBasic((auth) -> auth.disable());
//
//        // oauth2
//        /*
//        http.oauth2Login((oauth2) -> oauth2
//                .userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig
//                        .userService(customOAuth2UserService))
//                        .successHandler(customOAuthSuccessHandler));
//        */
//
//        //경로별 인가 작업
//        http
//                .authorizeHttpRequests((auth) -> auth
//                        .requestMatchers("/login/form", "/", "/join", "/mail/**", "/css/**" , "/js/**" , "/image/**").permitAll()
//                        .requestMatchers("/admin").hasRole("ADMIN")
//                        .anyRequest().authenticated());
//
//
//        // JWT 검증 필터 등록
//        http
//                .addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class);
//
//        // 커스텀한 Login filter 필터 등록
//        http
//                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration),jwtUtil), UsernamePasswordAuthenticationFilter.class);
//
//        http
//                .addFilterBefore(new CustomLogoutFilter(jwtUtil), LogoutFilter.class);
//
//
//        //세션 설정
//        http
//                .sessionManagement((session) -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//
//
//        return http.build();
//    }
//}
