//package com.todo.join.jwt;
//
//import com.todo.join.dto.CustomUserDetails;
//import com.todo.join.entity.User;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//@Slf4j
//@RequiredArgsConstructor
//public class JWTFilter extends OncePerRequestFilter {
//
//    private final JWTUtil jwtUtil;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//
//        String token = null;
//        Cookie[] cookies = request.getCookies();
//
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                if ("access".equals(cookie.getName())) {
//                    token = cookie.getValue();
//                    break;
//                }
//            }
//        }
//
//        String refresh = null;
//
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                if (cookie.getName().equals("refresh")) {
//                    refresh = cookie.getValue();
//                    break;
//                }
//            }
//        }
//
//
//        // 요청 URL을 가져옵니다.
//        String requestURI = request.getRequestURI();
//
//        // 로그인 요청은 JWT 검증을 건너뜁니다.
//        if (requestURI.startsWith("/login") || requestURI.startsWith("/css/login") || requestURI.startsWith("/image") ||requestURI.equals("/join") || requestURI.startsWith("/js/join") || requestURI.startsWith("/mail")) {
//            filterChain.doFilter(request, response); // 필터 체인을 계속 진행합니다.
//            return;
//        }
//
//        // 토큰이 없을 시 필터 체인 계속 진행
//        if (token == null || token.isEmpty()) {
//            log.info("@@ token null or empty @@");
//
//            if (refresh == null) {
//                response.sendRedirect("/login/form");
//                return;
//            }
//
//            jwtUtil.getRefresh(request, response, refresh);
//            return;
//        }
//
//
//        //토큰 소멸 시간 검증
//        if (jwtUtil.isExpired(token)) {
//            System.out.println("@@ token expired @@");
//            jwtUtil.getRefresh(request,response,refresh);
//            return;
//        }
//
//        String category = jwtUtil.getCategory(token);
//
//        if (!category.equals("access")) {
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            return;
//        }
//
//        //토큰에서 username과 role 획득
//        String username = jwtUtil.getUsername(token);
//        String role = jwtUtil.getRole(token);
//
//        //userEntity를 생성하여 값 set
//        User user = new User();
//        user.setName(username);
//        user.setPassword("temppassword");
//        user.setRole(role);
//
//        //UserDetails에 회원 정보 객체 담기
//        CustomUserDetails customUserDetails = new CustomUserDetails(user);
//
//        //스프링 시큐리티 인증 토큰 생성
//        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
//        //세션에 사용자 등록
//        SecurityContextHolder.getContext().setAuthentication(authToken);
//
//        filterChain.doFilter(request, response);
//    }
//
//}
