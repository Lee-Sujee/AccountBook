package com.ssafy.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

	private final String SECRET_KEY = "zH38Jkja92HkLm283jfLA82mna9QPl39akslZPQ2KFJscm02ja==";
    private final long EXPIRATION_TIME = 1000 * 60 * 60; // 1시간

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    // 토큰 생성
    public String generateToken(String userId) {
        return Jwts.builder()
                .claim("userId", userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // userId 꺼내기
    public String getUserId(String token) {
        return extractClaims(token).get("userId", String.class);
    }

    // 토큰 검증
    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            String userId = getUserId(token);
            String principalId = ((CustomUserDetails) userDetails).getUserId();

            return (userId.equals(principalId) && !isTokenExpired(token));
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }
}
