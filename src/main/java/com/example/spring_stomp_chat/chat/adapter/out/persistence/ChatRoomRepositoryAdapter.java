package com.example.spring_stomp_chat.chat.adapter.out.persistence;

import com.example.spring_stomp_chat.chat.adapter.in.response.ChatRoomResponse;
import com.example.spring_stomp_chat.chat.adapter.mapper.ChatRoomMapper;
import com.example.spring_stomp_chat.chat.application.dto.LoadChatRoom;
import com.example.spring_stomp_chat.chat.application.port.out.CreateChatRoomPort;
import com.example.spring_stomp_chat.chat.application.port.out.LoadChatRoomPort;
import com.example.spring_stomp_chat.chat.domain.model.ChatRoom;
import com.example.spring_stomp_chat.chat.domain.model.ChatRoomParticipant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class ChatRoomRepositoryAdapter implements CreateChatRoomPort, LoadChatRoomPort {

    private final ChatRoomJpaRepository chatRoomJpaRepository;
    private final ChatRoomParticipantJpaRepository chatRoomParticipantJpaRepository;
    private final ChatMessageJpaRepository chatMessageJpaRepository;

    @Override
    public void save(ChatRoom chatRoom) {
        chatRoomJpaRepository.save(ChatRoomMapper.toEntity(chatRoom));
    }

    @Override
    public ChatRoom loadChatRoomByTargetUser(Long requestUserId, Long targetUserId) {
        return null;
    }

    @Override
    public List<LoadChatRoom> loadChatRooms(Long userId) {
        List<ChatRoomParticipantJpaEntity> myParticipations = chatRoomParticipantJpaRepository.findByParticipantId(userId);
        if (myParticipations.isEmpty()) return new ArrayList<>();

        List<Long> roomIds = new ArrayList<>();
        for (ChatRoomParticipantJpaEntity p : myParticipations) {
            roomIds.add(p.getChatRoom().getId());
        }

        List<ChatMessageJpaEntity> latestMessages = chatMessageJpaRepository.findLatestMessagesByRoomIds(roomIds);
        List<ChatRoomParticipantJpaEntity> allParticipants = chatRoomParticipantJpaRepository.findAllByChatRoomIdIn(roomIds, userId);

        Map<Long, ChatMessageJpaEntity> lastMessageMap = new HashMap<>();
        for (ChatMessageJpaEntity m : latestMessages) {
            lastMessageMap.put(m.getChatRoom().getId(), m);
        }

        Map<Long, ChatRoomParticipantJpaEntity> participantsMap = new HashMap<>();
        for (ChatRoomParticipantJpaEntity p : allParticipants) {
            participantsMap.put(p.getChatRoom().getId(), p);
        }

        List<LoadChatRoom> responses = new ArrayList<>();
        for (ChatRoomParticipantJpaEntity myPart : myParticipations) {
            ChatRoomJpaEntity room = myPart.getChatRoom();
            ChatMessageJpaEntity lastMessage = lastMessageMap.get(room.getId());
            ChatRoomParticipantJpaEntity targetUser = participantsMap.get(room.getId());

            LoadChatRoom loadChatRoom = LoadChatRoom.builder()
                    .chatRoomId(room.getId())
                    .targetUserId(targetUser.getParticipantId())
                    .lastMessage(lastMessage.getContent())
                    .lastMessageAt(room.getLastMessageAt())
                    .build();

            responses.add(loadChatRoom);
        }

        return responses;
    }
}
