package com.example.spring_stomp_chat.chat.application.port.in;

import com.example.spring_stomp_chat.chat.adapter.in.response.ChatRoomResponse;

import java.util.List;

public interface GetMyChatRoomsUseCase {
    List<ChatRoomResponse> getMyChatRooms(Long userId);
}
