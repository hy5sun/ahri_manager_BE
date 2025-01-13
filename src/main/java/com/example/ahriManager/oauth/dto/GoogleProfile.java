package com.example.ahriManager.oauth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GoogleProfile {
    @JsonProperty("email")
    private String email;
}
