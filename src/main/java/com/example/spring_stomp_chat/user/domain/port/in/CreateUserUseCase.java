package com.example.spring_stomp_chat.user.domain.port.in;

import com.example.spring_stomp_chat.user.domain.command.CreateUserCommand;

public interface CreateUserUseCase {
    void createUser(CreateUserCommand createUserCommand);
}
