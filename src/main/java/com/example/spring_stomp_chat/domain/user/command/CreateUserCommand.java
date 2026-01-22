package com.example.spring_stomp_chat.domain.user.command;

public record CreateUserCommand(
        String name,
        String username,
        String password
) {
}
