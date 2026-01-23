package com.example.spring_stomp_chat.chat.adapter;

import com.example.spring_stomp_chat.chat.adapter.mapper.ChatRoomMapper;
import com.example.spring_stomp_chat.chat.adapter.out.persistence.ChatRoomJpaEntity;
import com.example.spring_stomp_chat.chat.adapter.out.persistence.ChatRoomJpaRepository;
import com.example.spring_stomp_chat.chat.adapter.out.persistence.ChatRoomRepositoryAdapter;
import com.example.spring_stomp_chat.chat.domain.model.ChatRoom;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@Import({ChatRoomRepositoryAdapter.class})
public class ChatRoomRepositoryAdapterTest {

    @Autowired
    private ChatRoomRepositoryAdapter chatRoomRepositoryAdapter;

    @Autowired
    private ChatRoomJpaRepository chatRoomJpaRepository;

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
}
