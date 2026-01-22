package com.example.spring_stomp_chat.user.application.command;

public record CreateUserCommand(
        String name,
        String username,
        String password
) {
}
