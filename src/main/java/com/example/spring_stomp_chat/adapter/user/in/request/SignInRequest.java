package com.example.spring_stomp_chat.adapter.user.in.request;

public record SignInRequest(
        String name,
        String username,
        String password
){}
