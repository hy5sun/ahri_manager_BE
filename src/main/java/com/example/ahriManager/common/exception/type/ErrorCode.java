package com.example.ahriManager.common.exception.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // provider type 불일치
    WRONG_PROVIDER_TYPE(HttpStatus.BAD_REQUEST.value(), "Bad Request", "잘못된 provider 타입입니다."),

    // 잘못된 요청
    BAD_REQUEST(HttpStatus.BAD_REQUEST.value(), "Bad Request", "잘못된 요청입니다."),
    // 잘못된 값 입력
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST.value(), "Bad Request", "올바르지 않은 입력값입니다."),
    // 금지된 접근
    ACCESS_DENIED(HttpStatus.FORBIDDEN.value(), "Forbidden", "접근이 거부됐습니다."),
    // 잘못된 HTTP 메서드 호출
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED.value(), "Method Not Allowed", "잘못된 HTTP 메서드를 호출했습니다."),
    // 서버 에러
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error", "서버 에러가 발생했습니다.");

    private final int statusCode;
    private final String error;
    private final String message;
}
