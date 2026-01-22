package com.example.spring_stomp_chat.user.domain.port.in;

import com.example.spring_stomp_chat.user.adapter.in.response.TokenResponse;
import com.example.spring_stomp_chat.user.domain.command.LoginUserCommand;

public interface LoginUserUseCase {
    TokenResponse login(LoginUserCommand command);
}
