package com.example.ahriManager.oauth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OAuthToken {
    @JsonProperty("access_token")
    private String accessToken;
}
