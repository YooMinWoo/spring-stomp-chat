package com.example.spring_stomp_chat.chat.application.command;

public record SendMessageCommand(
        Long chatRoomId,
        Long senderId,
        String content
) {
}
