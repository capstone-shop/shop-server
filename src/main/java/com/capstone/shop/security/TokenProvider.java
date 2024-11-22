package com.capstone.shop.security;
import com.capstone.shop.config.AppProperties;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.util.Date;
@Service
@RequiredArgsConstructor
public class TokenProvider {
    private final AppProperties appProperties;
    private static final Logger logger = LoggerFactory.getLogger(TokenProvider.class);

    public String createToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        boolean isAdditionalInfoCompleted = userPrincipal.isAdditionalInfoCompleted();
        Long userId = userPrincipal.getId();

        Date now = new Date();

        Date tokenExpiry = new Date(now.getTime() + appProperties.getAuth().getAccessTokenExpiry());
        System.out.println(tokenExpiry);
        System.out.println(appProperties.getAuth().getAccessTokenExpiry());
        return Jwts.builder()
                .setSubject(userId.toString())
                .claim("role", userPrincipal.getAuthorities())
                .claim("isAdditionalInfoCompleted", isAdditionalInfoCompleted)
                .signWith(SignatureAlgorithm.HS512, appProperties.getAuth().getTokenSecret())
                .setExpiration(tokenExpiry)
                .compact();
    }

    public String createRefreshToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Long userId = userPrincipal.getId();

        Date now = new Date();
        Date refreshTokenExpiry = new Date(now.getTime() + appProperties.getAuth().getRefreshTokenExpiry());

        return Jwts.builder()
                .setSubject(userId.toString())
                .signWith(SignatureAlgorithm.HS512, appProperties.getAuth().getTokenSecret())
                .setExpiration(refreshTokenExpiry)
                .compact();
    }

    public String createNewRefreshToken(Long userId) {
        Date now = new Date();
        Date refreshExpiry = new Date(now.getTime() + appProperties.getAuth().getRefreshTokenExpiry());

        return Jwts.builder()
                .setSubject(userId.toString())
                .setIssuedAt(now)
                .setExpiration(refreshExpiry)
                .signWith(SignatureAlgorithm.HS512, appProperties.getAuth().getTokenSecret())
                .compact();
    }

    public String createAccessTokenFromRefreshToken(String refreshToken) {
        Claims claims = Jwts.parser()
                .setSigningKey(appProperties.getAuth().getTokenSecret())
                .parseClaimsJws(refreshToken)
                .getBody();

        Long userId = Long.parseLong(claims.getSubject());
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + appProperties.getAuth().getAccessTokenExpiry());

        return Jwts.builder()
                .setSubject(userId.toString())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, appProperties.getAuth().getTokenSecret())
                .compact();
    }

    public Claims getExpiredTokenClaims(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(appProperties.getAuth().getTokenSecret())
                    .parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            logger.info("Expired JWT token.");
            return e.getClaims();
        }
        return null;
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(appProperties.getAuth().getTokenSecret())
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }

    public Long getUserIdFromExpiredToken(String token) {  //토큰이 만료됐을때 유저아이디 추출
        try {
            Claims claims = getExpiredTokenClaims(token);
            return claims != null ? Long.parseLong(claims.getSubject()) : null;
        } catch (Exception e) {
            logger.error("Error getting user id from expired token", e);
            return null;
        }
    }

    public Boolean getAdditionalInfoCompletedFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(appProperties.getAuth().getTokenSecret())
                .parseClaimsJws(token)
                .getBody();

        return claims.get("isAdditionalInfoCompleted", Boolean.class);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(appProperties.getAuth().getTokenSecret())
                    .parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
        }
        return false;
    }

    public boolean validateRefreshToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(appProperties.getAuth().getTokenSecret())
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}