package com.example.spring_stomp_chat.chat.adapter.out.persistence;

import com.example.spring_stomp_chat.chat.adapter.mapper.ChatMessageMapper;
import com.example.spring_stomp_chat.chat.application.port.out.CreateChatMessagePort;
import com.example.spring_stomp_chat.chat.domain.model.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChatMessageRepositoryAdapter implements CreateChatMessagePort {

    private final ChatMessageJpaRepository chatMessageJpaRepository;
    private final ChatMessageMapper chatMessageMapper;

    @Override
    public void save(ChatMessage message) {
        chatMessageJpaRepository.save(chatMessageMapper.toEntity(message));
    }
}
