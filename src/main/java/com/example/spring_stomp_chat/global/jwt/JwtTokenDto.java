package com.example.spring_stomp_chat.global.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class JwtTokenDto {
    private final String accessToken;
    private final String refreshToken;
}
