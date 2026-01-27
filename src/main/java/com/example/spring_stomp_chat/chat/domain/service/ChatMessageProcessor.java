package com.example.spring_stomp_chat.chat.domain.service;

import com.example.spring_stomp_chat.chat.domain.model.ChatRoom;
import com.example.spring_stomp_chat.chat.domain.model.ChatRoomParticipant;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ChatMessageProcessor {

    public void processMessageDelivery(ChatRoom room, List<ChatRoomParticipant> participants, Long senderId) {

        // 1. 채팅방 상태 업데이트
        room.processNewMessage();

        // 2. 참여자들 상태 업데이트
        participants.forEach(participant -> {
            if (participant.getParticipantId().equals(senderId)) {
                participant.markAsSender(); // 발신자 처리
            } else {
                participant.markAsReceiver(); // 수신자 처리
            }
        });
    }
}
