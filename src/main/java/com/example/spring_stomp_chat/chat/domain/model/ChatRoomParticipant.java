package com.example.spring_stomp_chat.chat.domain.model;

import com.example.spring_stomp_chat.user.domain.model.User;
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
    private Long participantId;
    private LocalDateTime lastReadAt;
    private Boolean isActive;
    private LocalDateTime currentJoinedAt;
    private LocalDateTime leftAt;

    public static ChatRoomParticipant create(Long participantId) {
        ChatRoomParticipant chatRoomParticipant = new ChatRoomParticipant();
        chatRoomParticipant.participantId = participantId;
        chatRoomParticipant.lastReadAt = null;
        chatRoomParticipant.isActive = true;
        chatRoomParticipant.currentJoinedAt = null;
        chatRoomParticipant.leftAt = null;

        return chatRoomParticipant;
    }
}
