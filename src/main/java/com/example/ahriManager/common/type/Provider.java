package com.example.ahriManager.common.type;

import com.example.ahriManager.common.exception.BusinessException;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

import static com.example.ahriManager.common.exception.type.ErrorCode.WRONG_PROVIDER_TYPE;

@Getter
@AllArgsConstructor
public enum Provider {
    KAKAO("kakao"),
    GOOGLE("google");

    private final String value;

    @JsonCreator
    public static Provider fromType(String value) {
        return Arrays.stream(values())
                .filter(type->type.getValue().equals(value))
                .findAny()
                .orElseThrow(()->new BusinessException(WRONG_PROVIDER_TYPE));
    }
}
