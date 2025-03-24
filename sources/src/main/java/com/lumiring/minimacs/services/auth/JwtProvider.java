package com.lumiring.minimacs.services.auth;

import com.lumiring.minimacs.config.JwtSecrets;
import com.lumiring.minimacs.entity.user.UserEntity;
import com.lumiring.minimacs.domain.constant.JwtErrorCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
@Component
public class JwtProvider {
    private final SecretKey jwtAccessSecret;
    private final SecretKey jwtRefreshSecret;


    public JwtProvider() {
        this.jwtAccessSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(JwtSecrets.getAccessSecret()));
        this.jwtRefreshSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(JwtSecrets.getRefreshSecret()));
    }

    public String generateAccessToken(@NonNull UserEntity userEntity) {
        final LocalDateTime now = LocalDateTime.now();
        final Instant accessExpirationInstant = now.plusMinutes(1440).atZone(ZoneId.systemDefault()).toInstant();
        final Date accessExpiration = Date.from(accessExpirationInstant);
        return Jwts.builder()
                .subject(userEntity.getUsername())
                .expiration(accessExpiration)
                .signWith(jwtAccessSecret)
                .claim("roles", userEntity.getRoles())
                .compact();
    }

    public String generateRefreshToken(@NonNull UserEntity userEntity) {
        final LocalDateTime now = LocalDateTime.now();
        final Instant refreshExpirationInstant = now.plusDays(30).atZone(ZoneId.systemDefault()).toInstant();
        final Date refreshExpiration = Date.from(refreshExpirationInstant);
        return Jwts.builder()
                .subject(userEntity.getUsername())
                .expiration(refreshExpiration)
                .signWith(jwtRefreshSecret)
                .compact();
    }

    public JwtErrorCode validateAccessToken(@NonNull String accessToken) {
        return validateToken(accessToken, jwtAccessSecret);
    }

    public JwtErrorCode validateRefreshToken(@NonNull String refreshToken) {
        return validateToken(refreshToken, jwtRefreshSecret);
    }

    private JwtErrorCode validateToken(@NonNull String token, @NonNull Key secret) {
        try {
            Jwts.parser()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(token);
            return JwtErrorCode.OK;
        } catch (ExpiredJwtException expEx) {
            return JwtErrorCode.EXPIRED;
        } catch (UnsupportedJwtException unsEx) {
            return JwtErrorCode.UNSUPPORTED;
        } catch (MalformedJwtException mjEx) {
            return JwtErrorCode.MALFORMED;
        } catch (SignatureException sEx) {
            return JwtErrorCode.INVALID_SIGNATURE;
        } catch (Exception e) {
            return JwtErrorCode.INVALID_TOKEN;
        }
    }


    public Claims getAccessClaims(@NonNull String token) {
        return getClaims(token, jwtAccessSecret);
    }

    public Claims getRefreshClaims(@NonNull String token) {
        return getClaims(token, jwtRefreshSecret);
    }

    private Claims getClaims(@NonNull String token, @NonNull Key secret) {
        return Jwts.parser()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

//    public String extractUserName(String token) {
//        return extractClaim(token, Claims::getSubject);
//    }
//
//
//    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
//        final Claims claims = extractAllClaims(token);
//        return claimsResolvers.apply(claims);
//    }
//
//    private Claims extractAllClaims(String token) {
//        return Jwts.parser().setSigningKey(getSigningKey()).build().parseClaimsJws(token)
//                .getBody();
//    }

//    private Key getSigningKey() {
//        byte[] keyBytes = Decoders.BASE64.decode(jwtAccessSecret);
//        return Keys.hmacShaKeyFor(keyBytes);
//    }

}