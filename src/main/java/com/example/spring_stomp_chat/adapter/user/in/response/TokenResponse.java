package com.example.spring_stomp_chat.adapter.user.in.response;

import com.example.spring_stomp_chat.global.jwt.JwtTokenDto;

public record TokenResponse(
        String accessToken,
        String refreshToken
) {
    public static TokenResponse from(JwtTokenDto dto){
        return new TokenResponse(dto.getAccessToken(), dto.getRefreshToken());
    }
}
