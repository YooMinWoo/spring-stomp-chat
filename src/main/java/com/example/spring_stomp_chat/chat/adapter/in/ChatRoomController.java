package com.example.spring_stomp_chat.chat.adapter.in;

import com.example.spring_stomp_chat.chat.adapter.in.request.EnterChatRoomByTargetUserRequest;
import com.example.spring_stomp_chat.chat.application.command.EnterChatByUserCommand;
import com.example.spring_stomp_chat.chat.application.port.in.EnterChatRoomUseCase;
import com.example.spring_stomp_chat.global.security.CustomUserDetails;
import com.example.spring_stomp_chat.user.adapter.in.request.SignInRequest;
import com.example.spring_stomp_chat.user.application.command.CreateUserCommand;
import com.example.spring_stomp_chat.user.domain.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Tag(name = "ChatRoom(채팅방)")
@RequestMapping("/chat-rooms")
public class ChatRoomController {

    private final EnterChatRoomUseCase enterChatRoomUseCase;

    @Operation(summary = "1:1 채팅 요청하기")
    @PostMapping("/enter")
    public ResponseEntity<Void> enterChatRoom(@AuthenticationPrincipal CustomUserDetails userDetails,
                                              @RequestBody EnterChatRoomByTargetUserRequest request){
        Long requestUserId = userDetails.getUser().getId();
        EnterChatByUserCommand command = new EnterChatByUserCommand(requestUserId, request.targetUserId());
        enterChatRoomUseCase.enterByTargetUser(command);
        return ResponseEntity.noContent().build();
    }
}
