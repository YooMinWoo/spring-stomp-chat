package com.example.spring_stomp_chat.domain.user.port.in;

import com.example.spring_stomp_chat.domain.user.model.User;

public interface LogoutUserUseCase {

    void logout(User user);
}
