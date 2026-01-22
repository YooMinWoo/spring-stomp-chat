package com.example.spring_stomp_chat.domain.user.port.in;

import com.example.spring_stomp_chat.domain.user.command.CreateUserCommand;

public interface CreateUserUseCase {
    void createUser(CreateUserCommand createUserCommand);
}
