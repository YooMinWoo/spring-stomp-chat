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
public class ChatRoomParticipant {
    private Long id;
    private Long chatRoomId;
    private Long participantId;
    private LocalDateTime lastReadAt;
    private Boolean isActive;
    private LocalDateTime currentJoinedAt;
    private LocalDateTime leftAt;

    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;
}
