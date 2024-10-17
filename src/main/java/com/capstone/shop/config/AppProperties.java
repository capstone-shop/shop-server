package com.capstone.shop.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
@Getter
@ConfigurationProperties(prefix = "app")//application.뭐시기 읽어옴
public class AppProperties {
    //auth는 jwt 설정값, oauth2는 oauth2 담을거
    private final Auth auth = new Auth();
    private final OAuth2 oauth2 = new OAuth2();
    public static class Auth {
        private String tokenSecret;
        private long tokenExpirationMsec;
        public String getTokenSecret() {
            return tokenSecret;
        }
        public void setTokenSecret(String tokenSecret) {
            this.tokenSecret = tokenSecret;
        }
        public long getTokenExpirationMsec() {
            return tokenExpirationMsec;
        }
        public void setTokenExpirationMsec(long tokenExpirationMsec) {
            this.tokenExpirationMsec = tokenExpirationMsec;
        }
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