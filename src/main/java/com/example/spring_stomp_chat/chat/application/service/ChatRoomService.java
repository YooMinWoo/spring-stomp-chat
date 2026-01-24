package com.example.spring_stomp_chat.chat.application.service;

import com.example.spring_stomp_chat.chat.adapter.in.response.ChatRoomResponse;
import com.example.spring_stomp_chat.chat.application.command.EnterChatByUserCommand;
import com.example.spring_stomp_chat.chat.application.dto.LoadChatRoom;
import com.example.spring_stomp_chat.chat.application.port.in.EnterChatRoomUseCase;
import com.example.spring_stomp_chat.chat.application.port.in.GetMyChatRoomsUseCase;
import com.example.spring_stomp_chat.chat.application.port.out.CreateChatRoomPort;
import com.example.spring_stomp_chat.chat.application.port.out.LoadChatRoomPort;
import com.example.spring_stomp_chat.chat.domain.model.ChatRoom;
import com.example.spring_stomp_chat.user.application.port.out.LoadUserPort;
import com.example.spring_stomp_chat.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatRoomService implements EnterChatRoomUseCase, GetMyChatRoomsUseCase {

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

    @Override
    public List<ChatRoomResponse> getMyChatRooms(Long userId) {
        List<LoadChatRoom> loadChatRooms = loadChatRoomPort.loadChatRooms(userId);
        if (loadChatRooms.isEmpty()) return List.of();

        List<Long> targetUserIds = new ArrayList<>();
        for (LoadChatRoom room : loadChatRooms) {
            targetUserIds.add(room.getTargetUserId());
        }

        List<User> users = loadUserPort.loadUsersByIds(targetUserIds);

        Map<Long, User> userMap = new HashMap<>();
        for (User u : users) {
            userMap.put(u.getId(), u);
        }

        List<ChatRoomResponse> responses = new ArrayList<>();
        for (LoadChatRoom data : loadChatRooms) {
            User targetUser = userMap.get(data.getTargetUserId());

            responses.add(ChatRoomResponse.from(
                    data.getChatRoomId(),
                    targetUser.getName(),
                    data.getLastMessage(),
                    data.getLastMessageAt()
            ));
        }
        return responses;
    }
}
