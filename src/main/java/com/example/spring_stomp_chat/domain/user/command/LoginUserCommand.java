package com.example.spring_stomp_chat.domain.user.command;

public record LoginUserCommand(
        String username,
        String password
){}
