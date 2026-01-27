package com.example.spring_stomp_chat.chat.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoom {
    private Long id;
    private LocalDateTime lastMessageAt;
    private Boolean isActive;

    private List<ChatRoomParticipant> participants = new ArrayList<>();

    public static ChatRoom create(Long requestUserId, Long targetUserId){
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.isActive = true;
        chatRoom.lastMessageAt = null;
        chatRoom.getParticipants().add(ChatRoomParticipant.create(requestUserId));
        chatRoom.getParticipants().add(ChatRoomParticipant.create(targetUserId));
        return chatRoom;
    }

    public void processNewMessage() {
        this.isActive = true;
        this.lastMessageAt = LocalDateTime.now();
    }
}
