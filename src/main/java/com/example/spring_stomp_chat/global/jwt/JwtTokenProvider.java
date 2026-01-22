package com.example.spring_stomp_chat.global.jwt;

import com.example.spring_stomp_chat.user.domain.model.User;
import com.example.spring_stomp_chat.global.redis.RedisService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final long accessTokenValidityMs = 1000L * 60 * 60; // 1시간
    private final long refreshTokenExpiration = 1000 * 60 * 60 * 24; // 24시간
    private final RedisService redisService;

    public JwtTokenDto generateToken(User user) {
        long now = System.currentTimeMillis();

        // 액세스 토큰 생성
        String accessToken = Jwts.builder()
                .claim("category", "access")
                .claim("id", user.getId())
                .claim("userRole", user.getUserRole())
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + accessTokenValidityMs))
                .signWith(key)
                .compact();

        // 리프레시 토큰 생성
        String refreshToken = Jwts.builder()
                .claim("category", "refresh")
                .claim("id", user.getId())
                .claim("userRole", user.getUserRole())
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + refreshTokenExpiration))
                .signWith(key)
                .compact();

        return new JwtTokenDto(accessToken, refreshToken);
    }

    public void isExpired(String token){
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            // 여기서 만료 에러 던짐
            throw new JwtException("EXPIRED_TOKEN");
        }
    }

    public boolean isValidToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            throw new JwtException("INVALID_TOKEN");
        } catch (ExpiredJwtException e) {
            throw new JwtException("EXPIRED_TOKEN");
        } catch (UnsupportedJwtException e) {
            throw new JwtException("UNSUPPORTED_TOKEN");
        } catch (IllegalArgumentException e) {
            throw new JwtException("EMPTY_TOKEN");
        }
    }

    public Long getUserId(String accessToken){
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(accessToken)
                .getBody()
                .get("id", Long.class);
    }
}
