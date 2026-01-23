package com.example.spring_stomp_chat.chat.application.port.out;

import com.example.spring_stomp_chat.chat.domain.model.ChatRoom;

public interface LoadChatRoomPort {
    ChatRoom loadChatRoomByTargetUser(Long requestUserId, Long targetUserId);
}
