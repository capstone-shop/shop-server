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
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            String jwt = getJwtFromRequest(request);
            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                authenticateUser(jwt, request);
            } else if (jwt != null) {
                handleExpiredAccessToken(request, response);
                return;
            }
        } catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
        }
        filterChain.doFilter(request, response);
    }

    private void handleExpiredAccessToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String refreshToken = getRefreshTokenFromDB(request);
        if (refreshToken != null && tokenProvider.validateRefreshToken(refreshToken)) {
            String newAccessToken = tokenProvider.createAccessTokenFromRefreshToken(refreshToken);
            response.setHeader("Authorization", "Bearer " + newAccessToken);
        } else if (refreshToken != null) {
            Long userId = getUserIdFromRefreshToken(refreshToken);
            User user = userRepository.findById(userId).orElseThrow(() ->
                    new IllegalArgumentException("User not found with ID: " + userId));
            String newRefreshToken = tokenProvider.createNewRefreshToken(userId);
            saveRefreshTokenToDB(user, newRefreshToken);

            String newAccessToken = tokenProvider.createAccessTokenFromRefreshToken(newRefreshToken);
            response.setHeader("Authorization", "Bearer " + newAccessToken);
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid authentication.");
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

    private void saveRefreshTokenToDB(User user, String newRefreshToken) {
        userRefreshTokenRepository.save(new UserRefreshToken(user, newRefreshToken));
    }

    private void authenticateUser(String jwt, HttpServletRequest request) {
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
