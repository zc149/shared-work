package com.todo.join.jwt;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@Slf4j
public class JWTUtil {

    private SecretKey secretKey;

    public JWTUtil(@Value("${spring.jwt.secret}") String secret) {

        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public String getUsername(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("username", String.class);
    }

    public String getRole(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }

    public Boolean isExpired(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

    public String getCategory(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("category", String.class);
    }

    public String createJwt(String category, String username, String role, Long expiredMs) {

        return Jwts.builder()
                .claim("category", category)
                .claim("username", username)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }

    // JWT 쿠키에서 사용자 name 조회
    public String getJwtName(HttpServletRequest request) {

        String token = null;
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("access".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        } else {
            log.info("Access Authorization 쿠키를 확인 해주세요");
        }

        String username = getUsername(token);

        return username;
    }


    /*
        refresh 토큰을 타임리프 SSR을 사용함으로써 별도의 controller를 구현하지 않고 메서드로 구현
     */

    public void getRefresh(HttpServletRequest request, HttpServletResponse response, String refresh) throws IOException {


        if (refresh == null) {
//            response.sendRedirect(request.getRequestURI() + "?message=refresh token null");
            return; // 잘못된 요청 시 리다이렉트
        }

        if (isExpired(refresh)) {
//            response.sendRedirect(request.getRequestURI() + "?message=refresh token expired");
            return; // 만료된 경우 리다이렉트
        }

        String category = getCategory(refresh);
        if (!category.equals("refresh")) {
            System.out.println("333");
//            response.sendRedirect(request.getRequestURI() + "?message=invalid refresh token");
            return; // 잘못된 토큰 시 리다이렉트
        }

        String username = getUsername(refresh);
        String role = getRole(refresh);

        String newAccess = createJwt("access", username, role, 600000L);
        String newRefresh = createJwt("refresh", username, role, 86400000L);
        response.addCookie(createCookie("access", newAccess));
        response.addCookie(createCookie("refresh", newRefresh));

        response.sendRedirect("/home?username=" + username);
    }

    public Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        //cookie.setSecure(true);
        //cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
}
