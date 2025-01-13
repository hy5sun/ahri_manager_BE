package com.example.ahriManager.oauth.service;

import com.example.ahriManager.common.exception.BusinessException;
import com.example.ahriManager.common.factory.WebClientFactory;
import com.example.ahriManager.common.type.ProviderType;
import com.example.ahriManager.member.domain.Member;
import com.example.ahriManager.member.repository.MemberRepository;
import com.example.ahriManager.oauth.dto.GoogleProfile;
import com.example.ahriManager.oauth.dto.KakaoProfile;
import com.example.ahriManager.oauth.dto.OAuthToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import static com.example.ahriManager.common.exception.type.ErrorCode.*;
import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuthService {
    private final static String KAKAO_AUTH_BASE_URL = "https://kauth.kakao.com";
    private final static String KAKAO_API_BASE_URL = "https://kapi.kakao.com";
    private final static String GOOGLE_AUTH_BASE_URL = "https://accounts.google.com";
    private final static String GOOGLE_OAUTH_BASE_URL = "https://oauth2.googleapis.com";
    private final static String GOOGLE_API_BASE_URL = "https://www.googleapis.com";

    private final WebClientFactory webClientFactory;
    private final MemberRepository memberRepository;

    @Value("${oauth.kakao.clientId}")
    private String kakaoClientId;

    @Value("${oauth.kakao.redirectURI}")
    private String kakaoRedirectURI;

    @Value("${oauth.google.clientId}")
    private String googleClientId;

    @Value("${oauth.google.clientPw}")
    private String googleClientPw;

    @Value("${oauth.google.redirectURI}")
    private String googleRedirectURI;

    public String getLoginUrl(String provider) {
        ProviderType providerType = ProviderType.fromType(provider);

        if (providerType.equals(ProviderType.KAKAO)) {
            return getKakaoLoginUrl();
        } else {
            return getGoogleLoginUrl();
        }
    }

    private String getKakaoLoginUrl() {
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

    private String getGoogleLoginUrl() {
        return GOOGLE_AUTH_BASE_URL + "/o/oauth2/v2/auth?client_id=" + googleClientId
                + "&redirect_uri=" + googleRedirectURI + "&response_type=code&scope=openid%20profile%20email";
    }

    private String getGoogleAccessToken(String authCode) {
        WebClient googleWebClient = webClientFactory.createWebClient(GOOGLE_OAUTH_BASE_URL);

        OAuthToken token = googleWebClient.post()
                .uri("/token")
                .body(fromFormData("grant_type", "authorization_code")
                        .with("client_id", googleClientId)
                        .with("client_secret", googleClientPw)
                        .with("redirect_uri", googleRedirectURI)
                        .with("code", authCode))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(OAuthToken.class)
                .block();

        return token.getAccessToken();
    }

    public GoogleProfile getGoogleProfile(String authCode) {
        String accessToken = getGoogleAccessToken(authCode);

        WebClient kakaoWebClient = webClientFactory.createWebClient(GOOGLE_API_BASE_URL);

        return kakaoWebClient.get()
                .uri("/oauth2/v3/userinfo")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(GoogleProfile.class)
                .block();
    }

    public void getProfile(String provider, String authCode) {
        ProviderType providerType = ProviderType.fromType(provider);

        String email = switch (providerType) {
            case KAKAO -> getKakaoProfile(authCode).getKakaoAccount().getEmail();
            case GOOGLE -> getGoogleProfile(authCode).getEmail();
            // default -> 이전에 provider type에 대한 검증을 진행하기 때문에 생략함
        };

        Member member = findMember(email, providerType);
        checkNicknameSet(member);
    }

    private void saveSocialAccount(String email, ProviderType providerType) {
        Member member = Member.builder()
                .email(email)
                .provider(providerType)
                .nickname("")
                .build();

        memberRepository.save(member);
        log.info(member.getEmail() + ": 회원 저장");
    }

    private Member findMember(String email, ProviderType provider) {
        return memberRepository.findByEmailAndProvider(email, provider)
                .orElseGet(()-> {
                    saveSocialAccount(email, provider);
                    throw new BusinessException(MEMBER_NOT_FOUND);
                });
    }

    private void checkNicknameSet(Member member) {
        if (member.getNickname().isEmpty()) {
            throw new BusinessException(NICKNAME_NOT_SET);
        }
    }
}
