package com.example.spring_stomp_chat.domain.user.port.in;

import com.example.spring_stomp_chat.adapter.user.in.response.TokenResponse;

public interface ReissueUseCase {
    TokenResponse reissue(String refreshToken);
}
