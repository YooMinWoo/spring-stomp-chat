package com.example.spring_stomp_chat.chat.adapter.out.persistence;

import com.example.spring_stomp_chat.chat.adapter.mapper.ChatRoomMapper;
import com.example.spring_stomp_chat.chat.application.port.out.CreateChatRoomPort;
import com.example.spring_stomp_chat.chat.domain.model.ChatRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChatRoomRepositoryAdapter implements CreateChatRoomPort {

    private final ChatRoomJpaRepository chatRoomJpaRepository;

    @Override
    public void save(ChatRoom chatRoom) {
        chatRoomJpaRepository.save(ChatRoomMapper.toEntity(chatRoom));
    }
}
