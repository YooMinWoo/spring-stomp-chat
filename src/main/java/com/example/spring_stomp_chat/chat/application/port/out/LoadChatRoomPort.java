package com.example.spring_stomp_chat.chat.application.port.out;

import com.example.spring_stomp_chat.chat.adapter.in.response.ChatRoomResponse;
import com.example.spring_stomp_chat.chat.application.dto.LoadChatRoom;
import com.example.spring_stomp_chat.chat.domain.model.ChatRoom;

import java.util.List;

public interface LoadChatRoomPort {
    ChatRoom loadChatRoomByTargetUser(Long requestUserId, Long targetUserId);

    List<LoadChatRoom> loadChatRooms(Long userId);
}
