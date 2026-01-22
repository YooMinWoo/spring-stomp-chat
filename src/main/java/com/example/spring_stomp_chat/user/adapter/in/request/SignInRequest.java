package com.example.spring_stomp_chat.user.adapter.in.request;

public record SignInRequest(
        String name,
        String username,
        String password
){}
