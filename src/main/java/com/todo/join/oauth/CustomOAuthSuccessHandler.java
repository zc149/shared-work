//package com.todo.join.oauth;
//
//import com.todo.join.jwt.JWTUtil;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.util.Collection;
//import java.util.Iterator;
//
//@Component
//@RequiredArgsConstructor
//public class CustomOAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
//
//    private final JWTUtil jwtUtil;
//
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//
//        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();
//
//        String username = customOAuth2User.getName();
//
//        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
//        GrantedAuthority auth = iterator.next();
//        String role = auth.getAuthority();
//
//        String access = jwtUtil.createJwt("access", username, role, 600000L);
//        String refresh = jwtUtil.createJwt("refresh", username, role, 86400000L);
//
//        response.addCookie(jwtUtil.createCookie("access", access));
//        response.addCookie(jwtUtil.createCookie("refresh", refresh));
//        response.setStatus(HttpStatus.OK.value());
//        response.sendRedirect("/home?username=" + username);
//    }
//}
