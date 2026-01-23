package com.example.spring_stomp_chat.chat.application.command;

public record EnterChatByUserCommand(
        Long requestUserId,
        Long targetUserId
) {
}
