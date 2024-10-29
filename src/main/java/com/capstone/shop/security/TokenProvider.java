package com.capstone.shop.security;
import com.capstone.shop.config.AppProperties;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.util.Date;
@Service
public class TokenProvider {
    @Value("${jwt.secret}")
    private String secret;
    private static final Logger logger = LoggerFactory.getLogger(TokenProvider.class);

    public String createToken(Authentication authentication, Date expiryDate) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        //isAdditionalInfoCompleted는 유저가 Oauth2 로그인 시 폰번호, 주소 등을 입력하지 않았을 시
        //추가 정보 입력 페이지로 리다이렉트 하기 위함
        boolean isAdditionalInfoCompleted = userPrincipal.isAdditionalInfoCompleted();

        // 사용자의 ID를 가져와서 토큰에담는다.
        Long userId = userPrincipal.getId();

        return Jwts.builder()
                .setSubject(userId.toString())
                .claim("role",userPrincipal.getAuthorities())
                .claim("isAdditionalInfoCompleted", isAdditionalInfoCompleted)
                .signWith(SignatureAlgorithm.HS512, secret)
                .setExpiration(expiryDate) // 만료 시간 설정
                .compact();
    }

    public String createRefreshToken(Authentication authentication, Date refreshExpiry) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Long userId = userPrincipal.getId();
        return Jwts.builder()
                .setSubject(userId.toString())
                .signWith(SignatureAlgorithm.HS512, secret)
                .setExpiration(refreshExpiry) // 만료 시간 설정
                .compact();
    }

    public String createAccessTokenFromRefreshToken(String refreshToken) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(refreshToken)
                .getBody();

        Long userId = Long.parseLong(claims.getSubject());
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 3600000); // 1시간

        return Jwts.builder()
                .setSubject(userId.toString())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }


    public Claims getExpiredTokenClaims(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            logger.info("Expired JWT token.");
            return e.getClaims();
        }
        return null;
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }
    public Boolean getAdditionalInfoCompletedFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();

        return claims.get("isAdditionalInfoCompleted", Boolean.class);
    }


    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(secret)
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
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    //todo: 토큰 만료 임박 시 새로운 토큰 생성해서 새 요청시 갈아끼우기

}