package com.example.spring_stomp_chat.chat.application.port.out;

import com.example.spring_stomp_chat.chat.domain.model.ChatRoom;

public interface CreateChatRoomPort {

    void save(ChatRoom newChatRoom);
}
