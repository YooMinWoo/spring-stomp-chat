package com.example.spring_stomp_chat.chat.adapter;

import com.example.spring_stomp_chat.chat.adapter.mapper.ChatRoomMapper;
import com.example.spring_stomp_chat.chat.adapter.out.persistence.*;
import com.example.spring_stomp_chat.chat.application.dto.LoadChatRoom;
import com.example.spring_stomp_chat.chat.domain.model.ChatRoom;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@Import({ChatRoomRepositoryAdapter.class})
public class ChatRoomRepositoryAdapterTest {

    @Autowired
    private ChatRoomRepositoryAdapter chatRoomRepositoryAdapter;

    @Autowired
    private ChatRoomJpaRepository chatRoomJpaRepository;

    @Autowired
    private ChatRoomParticipantJpaRepository participantJpaRepository;

    @Autowired
    private ChatMessageJpaRepository messageJpaRepository;

    @Test
    @DisplayName("새로운 채팅방과 참여자들이 DB에 올바르게 저장되어야 한다")
    void saveChatRoomTest() {
        // 1. Given: 도메인 모델 생성
        Long userA = 1L;
        Long userB = 2L;
        ChatRoom newChatRoom = ChatRoom.create(userA, userB);

        // 2. When: 어댑터를 통해 저장
        chatRoomRepositoryAdapter.save(newChatRoom);

        // 3. Then: DB에서 직접 꺼내어 검증
        // 도메인 로직에 따라 적절한 조회 메서드 사용 (여기선 전체 조회로 예시)
        List<ChatRoomJpaEntity> entities = chatRoomJpaRepository.findAll();

        assertThat(entities).hasSize(1);
        ChatRoomJpaEntity savedRoom = entities.get(0);

        // 참여자(Participants)가 2명인지, 각 ID가 맞는지 확인
        assertThat(savedRoom.getChatRoomParticipants()).hasSize(2);
        assertThat(savedRoom.getChatRoomParticipants())
                .extracting("participantId")
                .containsExactlyInAnyOrder(userA, userB);
    }

    @Test
    @DisplayName("DB의 여러 테이블에 흩어진 정보들을 LoadChatRoom DTO로 정확히 조립한다")
    void loadChatRooms_Integration_Success() {
        // 1. Given: 테스트 데이터 저장 (실제 DB에 저장하는 순서대로)

        // 채팅방 생성
        ChatRoomJpaEntity room = ChatRoomJpaEntity.builder()
                .lastMessageAt(LocalDateTime.now())
                .isActive(true)
                .build();
        chatRoomJpaRepository.save(room);

        // 참여자 저장 (나와 상대방)
        Long myId = 1L;
        Long targetId = 2L;
        ChatRoomParticipantJpaEntity myPart = ChatRoomParticipantJpaEntity.builder()
                .chatRoom(room).participantId(myId).isActive(true).build();
        ChatRoomParticipantJpaEntity otherPart = ChatRoomParticipantJpaEntity.builder()
                .chatRoom(room).participantId(targetId).isActive(true).build();
        participantJpaRepository.saveAll(List.of(myPart, otherPart));

        ChatMessageJpaEntity oldMsg = ChatMessageJpaEntity.builder()
                .chatRoom(room)
                .senderId(myId)
                .content("첫 번째 메시지")
                .build();

        ChatMessageJpaEntity latestMsg = ChatMessageJpaEntity.builder()
                .chatRoom(room)
                .senderId(targetId)
                .content("최신 메시지")
                .build();
        messageJpaRepository.saveAll(List.of(oldMsg, latestMsg));

        // 2. When: 로직 실행
        List<LoadChatRoom> result = chatRoomRepositoryAdapter.loadChatRooms(myId);

        // 3. Then: 결과 검증
        assertThat(result).hasSize(1);
        LoadChatRoom data = result.get(0);

        assertThat(data.getChatRoomId()).isEqualTo(room.getId());
        assertThat(data.getTargetUserId()).isEqualTo(targetId); // 나(1L)를 제외한 상대방(2L)이 와야 함
        assertThat(data.getLastMessage()).isEqualTo("최신 메시지"); // 최신 메시지가 와야 함
        assertThat(data.getLastMessageAt()).isNotNull();
    }

    @Test
    @DisplayName("참여 중인 채팅방이 없는 경우 빈 리스트를 반환한다")
    void loadChatRooms_Empty() {
        // given: 데이터 없음

        // when
        List<LoadChatRoom> result = chatRoomRepositoryAdapter.loadChatRooms(99L);

        // then
        assertThat(result).isEmpty();
    }
}
