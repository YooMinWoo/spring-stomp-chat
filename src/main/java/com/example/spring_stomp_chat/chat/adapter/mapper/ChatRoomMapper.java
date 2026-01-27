package com.example.spring_stomp_chat.chat.adapter.mapper;

import com.example.spring_stomp_chat.chat.adapter.out.persistence.ChatRoomJpaEntity;
import com.example.spring_stomp_chat.chat.adapter.out.persistence.ChatRoomParticipantJpaEntity;
import com.example.spring_stomp_chat.chat.domain.model.ChatRoom;
import com.example.spring_stomp_chat.chat.domain.model.ChatRoomParticipant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ChatRoomMapper {
    public static ChatRoomJpaEntity toEntity(ChatRoom chatRoom) {
        ChatRoomJpaEntity chatRoomJpaEntity = ChatRoomJpaEntity.builder()
                .id(chatRoom.getId())
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
                .id(participant.getId())
                .chatRoom(chatRoom)
                .participantId(participant.getParticipantId())
                .lastReadAt(participant.getLastReadAt())
                .isActive(participant.getIsActive())
                .build();
    }

    public static ChatRoom toDomain(ChatRoomJpaEntity roomEntity, List<ChatRoomParticipantJpaEntity> participantEntities) {
        if (roomEntity == null) return null;

        return ChatRoom.builder()
                .id(roomEntity.getId())
                .lastMessageAt(roomEntity.getLastMessageAt())
                .isActive(roomEntity.getIsActive())
                .participants(mapToParticipantDomains(participantEntities))
                .build();
    }

    private static List<ChatRoomParticipant> mapToParticipantDomains(List<ChatRoomParticipantJpaEntity> entities) {
        if (entities == null) return Collections.emptyList();

        return entities.stream()
                .map(entity -> ChatRoomParticipant.builder()
                        .id(entity.getId())
                        .participantId(entity.getParticipantId())
                        .lastReadAt(entity.getLastReadAt())
                        .isActive(entity.getIsActive())
                        .currentJoinedAt(entity.getCurrentJoinedAt())
                        .leftAt(entity.getLeftAt())
                        .build())
                .collect(Collectors.toList());
    }
}
