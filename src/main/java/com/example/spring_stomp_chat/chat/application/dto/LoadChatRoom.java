package com.example.spring_stomp_chat.chat.application.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class LoadChatRoom {
    private Long chatRoomId;
    private Long targetUserId;
    private String lastMessage;
    private LocalDateTime lastMessageAt;
}
