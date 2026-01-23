package com.example.spring_stomp_chat.chat.application.service;

import com.example.spring_stomp_chat.chat.application.command.EnterChatByUserCommand;
import com.example.spring_stomp_chat.chat.application.port.in.EnterChatRoomUseCase;
import com.example.spring_stomp_chat.chat.application.port.out.CreateChatRoomPort;
import com.example.spring_stomp_chat.chat.application.port.out.LoadChatRoomPort;
import com.example.spring_stomp_chat.chat.domain.model.ChatRoom;
import com.example.spring_stomp_chat.user.application.port.out.LoadUserPort;
import com.example.spring_stomp_chat.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomService implements EnterChatRoomUseCase {

    private final LoadUserPort loadUserPort;
    private final LoadChatRoomPort loadChatRoomPort;
    private final CreateChatRoomPort createChatRoomPort;

    @Override
    public void enterByTargetUser(EnterChatByUserCommand command) {
        User requestUser = loadUserPort.findById(command.requestUserId());
        User targetUser = loadUserPort.findById(command.targetUserId());

        ChatRoom chatRoom = loadChatRoomPort.loadChatRoomByTargetUser(requestUser.getId(), targetUser.getId());
        if(chatRoom != null){
            // 채팅 내역 가져오기
            return;
        }
        // 채팅방 생성하기
        ChatRoom newChatRoom = ChatRoom.create(requestUser.getId(), targetUser.getId());
        createChatRoomPort.save(newChatRoom);
    }
}
