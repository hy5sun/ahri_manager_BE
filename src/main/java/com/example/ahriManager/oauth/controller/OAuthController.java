package com.example.ahriManager.oauth.controller;

import com.example.ahriManager.common.exception.BusinessException;
import com.example.ahriManager.common.response.CustomResponse;
import com.example.ahriManager.oauth.dto.KakaoProfile;
import com.example.ahriManager.oauth.service.OAuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static com.example.ahriManager.common.exception.type.ErrorCode.INTERNAL_SERVER_ERROR;

@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
@Slf4j
public class OAuthController {

    private final OAuthService oAuthService;

    @GetMapping("/{provider}/login")
    @ResponseStatus(HttpStatus.FOUND)
    public void connectLogin(@PathVariable("provider") String provider, HttpServletResponse response) {
        String url = oAuthService.getLoginUrl(provider);

        try {
            response.sendRedirect(url);
        } catch (IOException e) {
            throw new BusinessException(INTERNAL_SERVER_ERROR);
        }
    }
}
