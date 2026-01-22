package com.example.spring_stomp_chat.user.application.command;

public record LoginUserCommand(
        String username,
        String password
){}
