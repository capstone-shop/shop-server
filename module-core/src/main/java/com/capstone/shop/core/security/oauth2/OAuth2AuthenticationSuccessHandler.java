package com.capstone.shop.core.security.oauth2;

import com.capstone.shop.core.exception.BadRequestException;
import com.capstone.shop.core.security.RefreshTokenService;
import com.capstone.shop.core.security.TokenProvider;
import com.capstone.shop.core.security.UserPrincipal;
import com.capstone.shop.core.config.AppProperties;
import com.capstone.shop.core.domain.enums.Role;
import com.capstone.shop.core.domain.entity.User;
//import com.capstone.shop.util.CookieUtils;
import com.capstone.shop.core.domain.repository.UserRepository;
import com.capstone.shop.core.util.CookieUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {


    private final TokenProvider tokenProvider;
    private final AppProperties appProperties;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;


    //oauth2인증이 성공적으로 이뤄졌을 때 실행된다
    //token을 포함한 uri을 생성 후 인증요청 쿠키를 비워주고 redirect 한다.
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        String targetUrl = determineTargetUrl(request, response, authentication);

        // 리다이렉트할 URI 구성 (프론트엔드 uri)
        String additionalInfoUrl = "https://induk.shop/additional-info";
        String frontEndUri = "https://induk.shop/oauth2/redirect";
//        String additionalInfoUrl = "https://localhost:3000/additionalInfo";
//        String frontEndUri = "http://localhost:3000/oauth2/redirect";
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(frontEndUri);

        if (authentication.getPrincipal() instanceof UserPrincipal) {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

            // 사용자 정보 조회
            User user = userRepository.findById(userPrincipal.getId())
                    .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없음"));

            // ROLE_PREUSER라면 추가 정보 입력 페이지로 리다이렉트
//            if (user.getRole() == Role.ROLE_PREUSER) {
//                logger.info("ROLE_PREUSER detected, redirecting to additional info page.");
//
//                // 토큰 먼저 생성
//                String accessToken = tokenProvider.createToken(authentication);
//                String refreshToken = tokenProvider.createRefreshToken(authentication);
//                refreshTokenService.saveRefreshToken(user, refreshToken);
//
//                // 추가 정보 URL에 토큰을 쿼리 파라미터로 추가
//                UriComponentsBuilder builder2 = UriComponentsBuilder.fromUriString(additionalInfoUrl)
//                        .queryParam("token", accessToken);
//
//                if (!response.isCommitted()) {
//                    getRedirectStrategy().sendRedirect(request, response, builder2.build().toUriString());
//                }
//                return;
//            }
        }
        // 기존 targetUrl에서 쿼리 파라미터로 토큰 추가
        String token = extractTokenFromTargetUrl(targetUrl); // targetUrl에서 토큰을 추출하는 메서드 필요
        if (token != null) {
            builder.queryParam("token", token);
        }

        String finalRedirectUrl = builder.build().toUriString();

        // 응답이 이미 커밋되었는지 확인
        if (response.isCommitted()) {
            logger.debug("추가 정보를 입력해야 합니다. " + finalRedirectUrl);
            return;
        }

        // 인증 속성 제거 및 리다이렉트
        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, finalRedirectUrl);
    }
    private String extractTokenFromTargetUrl(String targetUrl) {
        // URL 파라미터에서 토큰 추출 로직 구현
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(targetUrl).build();
        return uriComponents.getQueryParams().getFirst("token");
    }
    //token을 생성하고 이를 포함한 프론트엔드로의 uri를 생성한다.
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Optional<String> redirectUri = CookieUtils.getCookie(request, HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);
        if(redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
            throw new BadRequestException("Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication");
        }
        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());
        Long userId = ((UserPrincipal)authentication.getPrincipal()).getId();
        User user = userRepository.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("User not found with ID: " + userId));


        String accessToken = tokenProvider.createToken(authentication);
        String refreshToken = tokenProvider.createRefreshToken(authentication);
        refreshTokenService.saveRefreshToken(user, refreshToken);


        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("token", accessToken)
                .build().toUriString();
    }




    //인증정보 요청 내역을 쿠키에서 삭제한다.
    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    private boolean isAuthorizedRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);
        return appProperties.getOauth2().getAuthorizedRedirectUris()
                .stream()
                .anyMatch(authorizedRedirectUri -> {
                    URI authorizedURI = URI.create(authorizedRedirectUri);
                    if(authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                            && authorizedURI.getPort() == clientRedirectUri.getPort()) {
                        return true;
                    }
                    return false;
                });
    }
}