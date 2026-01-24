package com.example.spring_stomp_chat.chat.service;


import com.example.spring_stomp_chat.chat.adapter.in.response.ChatRoomResponse;
import com.example.spring_stomp_chat.chat.application.command.EnterChatByUserCommand;
import com.example.spring_stomp_chat.chat.application.dto.LoadChatRoom;
import com.example.spring_stomp_chat.chat.application.port.out.CreateChatRoomPort;
import com.example.spring_stomp_chat.chat.application.port.out.LoadChatRoomPort;
import com.example.spring_stomp_chat.chat.application.service.ChatRoomService;
import com.example.spring_stomp_chat.chat.domain.model.ChatRoom;
import com.example.spring_stomp_chat.user.application.port.out.LoadUserPort;
import com.example.spring_stomp_chat.user.domain.model.User;
import com.example.spring_stomp_chat.user.domain.model.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ChatRoomServiceTest {

    @InjectMocks
    private ChatRoomService chatRoomService;

    @Mock
    private LoadUserPort loadUserPort;

    @Mock
    private LoadChatRoomPort loadChatRoomPort;

    @Mock
    private CreateChatRoomPort createChatRoomPort;

    //
    @Nested
    @DisplayName("채팅방 입장 테스트 (EnterChatRoomUseCase)")
    class EnterChatRoomUseCase{

        @Test
        void 이미_채팅방이_존재하면_새로_생성하지_않는다() {
            // given
            Long requestUserId = 1L;
            Long targetUserId = 2L;

            User requestUser = User.withId(requestUserId, "홍길동", "user1", "encoded_password", UserRole.USER);
            User targetUser = User.withId(targetUserId, "고길동", "user2", "encoded_password", UserRole.USER);

            ChatRoom existingRoom = ChatRoom.create(requestUserId, targetUserId);

            given(loadUserPort.findById(requestUserId)).willReturn(requestUser);
            given(loadUserPort.findById(targetUserId)).willReturn(targetUser);
            given(loadChatRoomPort.loadChatRoomByTargetUser(requestUserId, targetUserId))
                    .willReturn(existingRoom);

            EnterChatByUserCommand command =
                    new EnterChatByUserCommand(requestUserId, targetUserId);

            // when
            chatRoomService.enterByTargetUser(command);

            // then
            verify(createChatRoomPort, never()).save(any());
        }
    }

    @Nested
    @DisplayName("채팅방 목록 불러오기 테스트 (GetMyChatRoomsUseCase)")
    class GetMyChatRoomsUseCase{
        @Test
        @DisplayName("채팅방 정보와 유저 정보를 결합하여 응답 DTO를 정상 생성한다")
        void getMyChatRooms_Success() {
            // 1. Given: 데이터 준비
            Long userId = 1L;
            LocalDateTime now = LocalDateTime.now();

            // LoadChatRoom (어댑터에서 주는 데이터) 생성 - @Builder 활용
            LoadChatRoom room1 = LoadChatRoom.builder()
                    .chatRoomId(101L)
                    .targetUserId(2L)
                    .lastMessage("첫 번째 메시지")
                    .lastMessageAt(now)
                    .build();

            LoadChatRoom room2 = LoadChatRoom.builder()
                    .chatRoomId(102L)
                    .targetUserId(3L)
                    .lastMessage("두 번째 메시지")
                    .lastMessageAt(now.minusMinutes(10))
                    .build();

            // User 도메인 객체 생성 - 제공해주신 withId 정적 팩토리 메서드 활용
            User user2 = User.withId(2L, "상대방A", "userA", "pass", UserRole.USER);
            User user3 = User.withId(3L, "상대방B", "userB", "pass", UserRole.USER);

            // Mock 설정
            when(loadChatRoomPort.loadChatRooms(userId)).thenReturn(List.of(room1, room2));
            when(loadUserPort.loadUsersByIds(anyList())).thenReturn(List.of(user2, user3));

            // 2. When: 실행
            List<ChatRoomResponse> result = chatRoomService.getMyChatRooms(userId);

            // 3. Then: 검증
            assertThat(result).hasSize(2);

            // 첫 번째 방 결과 검증
            assertThat(result.get(0).id()).isEqualTo(101L);
            assertThat(result.get(0).roomName()).isEqualTo("상대방A");
            assertThat(result.get(0).lastMessage()).isEqualTo("첫 번째 메시지");

            // 두 번째 방 결과 검증
            assertThat(result.get(1).id()).isEqualTo(102L);
            assertThat(result.get(1).roomName()).isEqualTo("상대방B");

            // 4. Verify: N+1 방지 여부 확인
            verify(loadChatRoomPort, times(1)).loadChatRooms(userId);
            verify(loadUserPort, times(1)).loadUsersByIds(anyList());
        }

        @Test
        @DisplayName("참여 중인 채팅방이 없으면 빈 리스트를 반환한다")
        void getMyChatRooms_Empty() {
            // given
            when(loadChatRoomPort.loadChatRooms(anyLong())).thenReturn(List.of());

            // when
            List<ChatRoomResponse> result = chatRoomService.getMyChatRooms(1L);

            // then
            assertThat(result).isEmpty();
            // 채팅방이 없으니 유저 조회는 아예 일어나지 않아야 함
            verify(loadUserPort, never()).loadUsersByIds(anyList());
        }
    }

}
