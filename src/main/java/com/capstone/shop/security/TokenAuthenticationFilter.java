package com.capstone.shop.security;


import com.capstone.shop.entity.User;
import com.capstone.shop.entity.UserRefreshToken;
import com.capstone.shop.user.v1.repository.UserRefreshTokenRepository;
import com.capstone.shop.user.v1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.capstone.shop.security.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final CustomUserDetailService customUserDetailsService;
    private final UserRefreshTokenRepository userRefreshTokenRepository; // Refresh token을 저장하는 Repository
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(TokenAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);
            if (StringUtils.hasText(jwt)) {
                if (tokenProvider.validateToken(jwt)) {
                    authenticateUser(jwt, request);
                } else {
                    logger.info("토큰 만료.");
                    // 토큰이 만료되었을 때
                    Long userId = tokenProvider.getUserIdFromExpiredToken(jwt);
                    if (userId != null) {
                        handleExpiredAccessToken(userId, request, response);
                        return;
                    }
                }
            }
        } catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
        }
        filterChain.doFilter(request, response);
    }

    private void handleExpiredAccessToken(Long userId, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        UserRefreshToken userRefreshToken = userRefreshTokenRepository.findByUserId(userId);

        if (userRefreshToken != null && tokenProvider.validateRefreshToken(userRefreshToken.getRefreshToken())) {
            String newAccessToken = tokenProvider.createAccessTokenFromRefreshToken(userRefreshToken.getRefreshToken());
            authenticateUser(newAccessToken, request);
            response.setHeader("Authorization", "Bearer " + newAccessToken);
            logger.info("리프래쉬토큰으로 새로운 액세스토큰 생성함.");
        } else {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없음: " + userId));
            String newRefreshToken = tokenProvider.createNewRefreshToken(userId);
            saveRefreshTokenToDB(user, newRefreshToken);

            String newAccessToken = tokenProvider.createAccessTokenFromRefreshToken(newRefreshToken);
            authenticateUser(newAccessToken, request);
            response.setHeader("Authorization", "Bearer " + newAccessToken);
        }
    }

    private String getRefreshTokenFromDB(HttpServletRequest request) {
        Long userId = tokenProvider.getUserIdFromToken(getJwtFromRequest(request));
        UserRefreshToken userRefreshToken =  userRefreshTokenRepository.findByUserId(userId);
        return userRefreshToken.getRefreshToken();
    }

    private Long getUserIdFromRefreshToken(String refreshToken) {
        return tokenProvider.getUserIdFromToken(refreshToken);
    }

//    private void saveRefreshTokenToDB(User user, String newRefreshToken) {
//        userRefreshTokenRepository.save(new UserRefreshToken(user, newRefreshToken));
//    } 같은 사용자가 여러번 동시에 요청을 보내면 토큰이 중복 생성되는 문제.. 아래 메서드로 다시 만듦
    private void saveRefreshTokenToDB(User user, String newRefreshToken) {
        UserRefreshToken userRefreshToken = userRefreshTokenRepository.findByUserId(user.getId());
        if (userRefreshToken == null) {
            userRefreshToken = new UserRefreshToken(user, newRefreshToken);
        } else {
            userRefreshToken.setRefreshToken(newRefreshToken);
        }
        userRefreshTokenRepository.save(userRefreshToken);
    }


    private void authenticateUser(String jwt, HttpServletRequest request) throws IOException { //토큰에서 사용자 id추출하고 authentication
        Long userId = tokenProvider.getUserIdFromToken(jwt);
        UserDetails userDetails = customUserDetailsService.loadUserById(userId);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        return StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ") ? bearerToken.substring(7) : null;
    }
}
