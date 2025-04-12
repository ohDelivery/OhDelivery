package com.ohdelivery.server.authserver.application.jwt;

import com.ohdelivery.server.authserver.application.dto.UserInfo;
import com.ohdelivery.server.authserver.application.jwt.exceptions.JwtExceptions.JwtExpiredException;
import com.ohdelivery.server.authserver.application.jwt.exceptions.JwtExceptions.JwtInvalidException;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class JwtProvider {

    private Key key;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpirationMs;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpirationMs;

    private final String roleClaim = "role";

    @PostConstruct
    private void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }


    public Tokens createTokens(String subject, String role){
        return Tokens.create(
                generateToken(subject, role, accessTokenExpirationMs),
                generateToken(subject, role, refreshTokenExpirationMs)
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
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            //TODO: check redis cache and blacklist

            return getUserInfoFromToken(token);
        } catch (ExpiredJwtException e) {
            throw new JwtExpiredException();
        } catch (MalformedJwtException | UnsupportedJwtException e) {
            throw new JwtInvalidException();
        }
    }

    public Claims getClaimFromToken(String token) {
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
}
