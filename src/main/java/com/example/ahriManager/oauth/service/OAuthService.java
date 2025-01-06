package com.example.ahriManager.oauth.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OAuthService {
    private final static String KAKAO_AUTH_BASE_URL = "https://kauth.kakao.com";
    private final static String CODE = "code";

    @Value("${oauth.kakao.clientId}")
    private String kakaoClientId;

    @Value("${oauth.kakao.redirectURI}")
    private String kakaoRedirectURI;

    public String getKakaoLoginUrl() {
        return  KAKAO_AUTH_BASE_URL + "/oauth/authorize?client_id=" + kakaoClientId
                + "&redirect_uri=" + kakaoRedirectURI + "&response_type=" + CODE;
    }
}
