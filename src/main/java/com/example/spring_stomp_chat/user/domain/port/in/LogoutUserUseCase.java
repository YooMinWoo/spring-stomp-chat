package com.example.spring_stomp_chat.user.domain.port.in;

import com.example.spring_stomp_chat.user.domain.model.User;

public interface LogoutUserUseCase {

    void logout(User user);
}
