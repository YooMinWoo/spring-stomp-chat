package com.example.spring_stomp_chat.domain.user.port.in;

import com.example.spring_stomp_chat.adapter.user.in.response.TokenResponse;
import com.example.spring_stomp_chat.domain.user.command.LoginUserCommand;

public interface LoginUserUseCase {
    TokenResponse login(LoginUserCommand command);
}
