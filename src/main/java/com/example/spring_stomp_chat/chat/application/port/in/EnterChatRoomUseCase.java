package com.example.spring_stomp_chat.chat.application.port.in;

import com.example.spring_stomp_chat.chat.application.command.EnterChatByUserCommand;

public interface EnterChatRoomUseCase {
    void enterByTargetUser(EnterChatByUserCommand command);
//    void openByRoomId(OpenChatByRoomIdQuery query);
}
