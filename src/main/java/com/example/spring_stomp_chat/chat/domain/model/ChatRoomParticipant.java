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
        chatRoomParticipant.isActive = false;
        chatRoomParticipant.currentJoinedAt = null;
        chatRoomParticipant.leftAt = null;

        return chatRoomParticipant;
    }

    // 메시지 발신자용 로직
    public void markAsSender() {
        handleRejoin();
        this.lastReadAt = LocalDateTime.now();
    }

    // 메시지 수신자용 로직 (상태만 활성화)
    public void markAsReceiver() {
        handleRejoin();
    }

    // 공통 재입장 로직
    private void handleRejoin() {
        if (!this.isActive) {
            this.isActive = true;
            this.currentJoinedAt = LocalDateTime.now();
            this.leftAt = null;
        }
    }
}
