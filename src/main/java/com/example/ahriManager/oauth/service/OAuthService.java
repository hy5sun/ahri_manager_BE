package com.example.ahriManager.oauth.service;

import com.example.ahriManager.common.factory.WebClientFactory;
import com.example.ahriManager.oauth.dto.KakaoProfile;
import com.example.ahriManager.oauth.dto.OAuthToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

@Service
@Slf4j
public class OAuthService {
    private final static String KAKAO_AUTH_BASE_URL = "https://kauth.kakao.com";
    private final static String KAKAO_API_BASE_URL = "https://kapi.kakao.com";

    private final WebClientFactory webClientFactory;

    @Value("${oauth.kakao.clientId}")
    private String kakaoClientId;

    @Value("${oauth.kakao.redirectURI}")
    private String kakaoRedirectURI;

    public OAuthService(WebClientFactory webClientFactory) {
        this.webClientFactory = webClientFactory;
    }

    public String getKakaoLoginUrl() {
        return  KAKAO_AUTH_BASE_URL + "/oauth/authorize?client_id=" + kakaoClientId
                + "&redirect_uri=" + kakaoRedirectURI + "&response_type=code";
    }

    private String getKakaoAccessToken(String authCode) {
        WebClient kakaoWebClient = webClientFactory.createWebClient(KAKAO_AUTH_BASE_URL);

        OAuthToken token = kakaoWebClient.post()
                .uri("/oauth/token")
                .body(fromFormData("grant_type", "authorization_code")
                        .with("client_id", kakaoClientId)
                        .with("redirect_uri", kakaoRedirectURI)
                        .with("code", authCode))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(OAuthToken.class)
                .block();

        return token.getAccessToken();
    }

    public KakaoProfile getKakaoProfile(String authCode) {
        String accessToken = getKakaoAccessToken(authCode);

        WebClient kakaoWebClient = webClientFactory.createWebClient(KAKAO_API_BASE_URL);

        return kakaoWebClient.get()
                .uri("/v2/user/me")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(KakaoProfile.class)
                .block();
    }
}
