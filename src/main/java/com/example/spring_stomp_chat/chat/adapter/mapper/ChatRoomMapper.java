package com.example.spring_stomp_chat.chat.adapter.mapper;

import com.example.spring_stomp_chat.chat.adapter.out.persistence.ChatRoomJpaEntity;
import com.example.spring_stomp_chat.chat.adapter.out.persistence.ChatRoomParticipantJpaEntity;
import com.example.spring_stomp_chat.chat.domain.model.ChatRoom;
import com.example.spring_stomp_chat.chat.domain.model.ChatRoomParticipant;

import java.util.ArrayList;

public class ChatRoomMapper {
    public static ChatRoomJpaEntity toEntity(ChatRoom chatRoom) {
        ChatRoomJpaEntity chatRoomJpaEntity = ChatRoomJpaEntity.builder()
                .lastMessageAt(chatRoom.getLastMessageAt())
                .isActive(chatRoom.getIsActive())
                .chatRoomParticipants(new ArrayList<>())
                .build();

        for (ChatRoomParticipant p : chatRoom.getParticipants()) {
            ChatRoomParticipantJpaEntity chatRoomParticipantJpaEntity = toEntity(p, chatRoomJpaEntity);
            chatRoomJpaEntity.getChatRoomParticipants().add(chatRoomParticipantJpaEntity);
        }
//
        return chatRoomJpaEntity;
    }

    private static ChatRoomParticipantJpaEntity toEntity(
            ChatRoomParticipant participant,
            ChatRoomJpaEntity chatRoom
    ) {
        return ChatRoomParticipantJpaEntity.builder()
                .chatRoom(chatRoom)
                .participantId(participant.getParticipantId())
                .lastReadAt(participant.getLastReadAt())
                .isActive(participant.getIsActive())
                .build();
    }
}
