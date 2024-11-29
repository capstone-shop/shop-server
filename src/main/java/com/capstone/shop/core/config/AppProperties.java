package com.capstone.shop.core.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
@Getter
@ConfigurationProperties(prefix = "app")//settings.뭐시기 읽어옴
public class AppProperties {
    //auth는 jwt 설정값, oauth2는 oauth2 담을거
    private final Auth auth = new Auth();
    private final OAuth2 oauth2 = new OAuth2();
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Auth {
        private String tokenSecret;
        private long accessTokenExpiry;
        private long refreshTokenExpiry;

    }
    public static final class OAuth2 {
        private List<String> authorizedRedirectUris = new ArrayList<>();
        public List<String> getAuthorizedRedirectUris() {
            return authorizedRedirectUris;
        }
        public OAuth2 authorizedRedirectUris(List<String> authorizedRedirectUris) {
            this.authorizedRedirectUris = authorizedRedirectUris;
            return this;
        }
    }
    public Auth getAuth() {
        return auth;
    }
    public OAuth2 getOauth2() {
        return oauth2;
    }
}