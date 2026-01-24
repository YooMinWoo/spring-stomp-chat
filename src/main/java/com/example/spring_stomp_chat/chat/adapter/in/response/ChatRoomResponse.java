package com.example.spring_stomp_chat.chat.adapter.in.response;

import com.example.spring_stomp_chat.chat.domain.model.ChatRoom;

import java.time.LocalDateTime;

public record ChatRoomResponse(
        Long id,
        String roomName,
        String lastMessage,
        LocalDateTime lastMessageAt
) {
    public static ChatRoomResponse from(Long chatRoomId, String roomName, String lastMessage, LocalDateTime lastMessageAt){
        return new ChatRoomResponse(
                chatRoomId,
                roomName,
                lastMessage,
                lastMessageAt
        );
    }
}
