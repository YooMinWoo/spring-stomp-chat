package com.example.spring_stomp_chat.chat.adapter.mapper;

import com.example.spring_stomp_chat.chat.adapter.out.persistence.ChatMessageJpaEntity;
import com.example.spring_stomp_chat.chat.adapter.out.persistence.ChatRoomJpaEntity;
import com.example.spring_stomp_chat.chat.domain.model.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatMessageMapper {

    public ChatMessageJpaEntity toEntity(ChatMessage message){
        return ChatMessageJpaEntity.builder()
                .id(message.getId())
                .chatRoom(ChatRoomMapper.toEntity(message.getChatRoom()))
                .senderId(message.getSenderId())
                .content(message.getContent())
                .build();
    }
}
