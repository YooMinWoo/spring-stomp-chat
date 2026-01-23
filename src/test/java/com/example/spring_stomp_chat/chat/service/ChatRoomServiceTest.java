package com.example.spring_stomp_chat.chat.service;


import com.example.spring_stomp_chat.chat.application.command.EnterChatByUserCommand;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

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
}
