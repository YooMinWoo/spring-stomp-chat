package com.example.spring_stomp_chat.user.application.port.in;

import com.example.spring_stomp_chat.user.application.command.CreateUserCommand;

public interface CreateUserUseCase {
    void createUser(CreateUserCommand createUserCommand);
}
