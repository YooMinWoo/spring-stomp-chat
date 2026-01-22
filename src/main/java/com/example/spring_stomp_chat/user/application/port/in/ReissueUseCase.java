package com.example.spring_stomp_chat.user.application.port.in;

import com.example.spring_stomp_chat.user.adapter.in.response.TokenResponse;

public interface ReissueUseCase {
    TokenResponse reissue(String refreshToken);
}
