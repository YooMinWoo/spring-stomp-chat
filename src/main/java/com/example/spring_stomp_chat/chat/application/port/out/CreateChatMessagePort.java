package com.example.spring_stomp_chat.chat.application.port.out;

import com.example.spring_stomp_chat.chat.domain.model.ChatMessage;

public interface CreateChatMessagePort {
    void save(ChatMessage message);
}
