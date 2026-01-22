package com.example.spring_stomp_chat.chat.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoom {
    private Long id;
    private LocalDateTime lastMessageAt;
    private Boolean isActive;

    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;
}
