package com.example.spring_stomp_chat.chat.adapter.in.request;

public record EnterChatRoomByTargetUserRequest(
        Long targetUserId
) {
}
