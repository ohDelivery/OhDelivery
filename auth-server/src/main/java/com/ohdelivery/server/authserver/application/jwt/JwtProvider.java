package com.ohdelivery.server.authserver.application.jwt;

import com.ohdelivery.server.authserver.application.dto.UserInfo;
import com.ohdelivery.server.authserver.application.jwt.exceptions.JwtExceptions.JwtExpiredException;
import com.ohdelivery.server.authserver.application.jwt.exceptions.JwtExceptions.JwtInvalidException;
import com.ohdelivery.server.authserver.domain.TokenStatus;
import com.ohdelivery.server.authserver.domain.Tokens;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class JwtProvider {

    private Key key;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpirationSec;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpirationSec;

    private final String roleClaim = "role";

    private final RedisTemplate<String, String> redisTemplate;

    private final String refreshRedisPrefix = "token:refresh:";
    private final String blacklistRedisPrefix = "token:blacklist:";

    @PostConstruct
    private void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }


    public Tokens createTokens(String subject, String role){
        String accessToken = generateToken(subject, role, accessTokenExpirationSec * 1000);
        String refreshToken = generateToken(subject, role, refreshTokenExpirationSec * 1000);

        //refresh 토큰 저장
        String key = refreshRedisPrefix + subject;
        redisTemplate.opsForValue().set(key, refreshToken, refreshTokenExpirationSec, TimeUnit.SECONDS);

        return Tokens.create(
                accessToken,
                refreshToken
        );
    }

    private String generateToken(String subject, String role, long expirationMs) {
        return Jwts.builder()
                .setSubject(subject)
                .claim(roleClaim, role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public UserInfo validateToken(String token) {
        try {

            checkBlackList(token);

            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            return getUserInfoFromToken(token);
        } catch (ExpiredJwtException e) {
            throw new JwtExpiredException();
        } catch (MalformedJwtException | UnsupportedJwtException e) {
            throw new JwtInvalidException();
        }
    }

    public void invalidateTokens(String token, TokenStatus tokenStatus){
        //refresh 토큰 삭제
        String subject = getClaimFromToken(token).getSubject();
        String key = refreshRedisPrefix + subject;
        redisTemplate.delete(key);

        putTokenOnBlackList(token, tokenStatus);
    }

    private Claims getClaimFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private UserInfo getUserInfoFromToken(String token) {
        Claims claims = getClaimFromToken(token);

        return UserInfo.create(
                claims.getSubject(),
                claims.get(roleClaim, String.class)
        );
    }

    public Tokens checkRefreshToken(String token){
        UserInfo userInfo = getUserInfoFromToken(token);

        if (!redisTemplate.hasKey(refreshRedisPrefix + userInfo.getId())){
            throw new JwtInvalidException();
        }

        redisTemplate.delete(refreshRedisPrefix + token);

        return createTokens(userInfo.getId(), userInfo.getRole().getAuthority());
    }

    private void putTokenOnBlackList(String token, TokenStatus tokenStatus){
        String key = blacklistRedisPrefix + token;

        Date expirationDate = getClaimFromToken(token).getExpiration();
        long expirationMs = (expirationDate.getTime() - System.currentTimeMillis());

        redisTemplate.opsForValue().set(key, tokenStatus.toString(), expirationMs/1000, TimeUnit.SECONDS);
    }

    private void checkBlackList(String token){
        if (!redisTemplate.hasKey(blacklistRedisPrefix + token)) {
            return;
        }

        TokenStatus tokenStatus = TokenStatus.valueOf(redisTemplate.opsForValue().get(blacklistRedisPrefix + token));

        throw new JwtInvalidException(tokenStatus.getMessage());
    }

}
