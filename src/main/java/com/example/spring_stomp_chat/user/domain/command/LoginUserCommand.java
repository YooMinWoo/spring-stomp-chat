package com.example.spring_stomp_chat.user.domain.command;

public record LoginUserCommand(
        String username,
        String password
){}
