//package com.todo.join.jwt;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.todo.join.dto.CustomUserDetails;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import java.io.IOException;
//import java.util.Collection;
//import java.util.Iterator;
//import java.util.Map;
//
//@Slf4j
//@RequiredArgsConstructor
//public class LoginFilter extends UsernamePasswordAuthenticationFilter {
//
//    private final AuthenticationManager authenticationManager;
//    private final JWTUtil jwtUtil;
//
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//
//        String username = request.getParameter("username");
//        String password = request.getParameter("password");
//
//
//        // UsernamePasswordAuthenticationToken 생성
//        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
//
//        // AuthenticationManager에 인증 요청
//        return authenticationManager.authenticate(authToken);
//
//    }
//
//    @Override
//    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
//    log.info("로그인 성공");
//
//        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
//
//        String username = customUserDetails.getUsername();
//
//        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
//        GrantedAuthority auth = iterator.next();
//
//        String role = auth.getAuthority();
//
//        String access = jwtUtil.createJwt("access", username, role, 600000L);
//        String refresh = jwtUtil.createJwt("refresh", username, role, 86400000L);
//
//        /*
//          타임리프 SSR을 사용하니 CSR의 header 보다는 쿠키를 활용하는게 간편하겠다고 생각
//         */
//
//        response.addCookie(jwtUtil.createCookie("access", access));
//        response.addCookie(jwtUtil.createCookie("refresh", refresh));
//        response.setStatus(HttpStatus.OK.value());
//        response.sendRedirect("/home?username=" + username);
//    }
//
//    @Override
//    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
//    log.info("로그인 실패");
//        response.setStatus(401);
//    }
//
//
//
//
//}
