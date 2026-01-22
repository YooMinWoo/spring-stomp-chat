package com.example.spring_stomp_chat.user.adapter.in;

import com.example.spring_stomp_chat.user.adapter.in.request.LoginRequest;
import com.example.spring_stomp_chat.user.adapter.in.request.SignInRequest;
import com.example.spring_stomp_chat.user.adapter.in.response.TokenResponse;
import com.example.spring_stomp_chat.user.domain.command.CreateUserCommand;
import com.example.spring_stomp_chat.user.domain.command.LoginUserCommand;
import com.example.spring_stomp_chat.user.domain.port.in.CreateUserUseCase;
import com.example.spring_stomp_chat.user.domain.port.in.LoginUserUseCase;
import com.example.spring_stomp_chat.user.domain.port.in.LogoutUserUseCase;
import com.example.spring_stomp_chat.user.domain.port.in.ReissueUseCase;
import com.example.spring_stomp_chat.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Tag(name = "User(유저)")
@RequestMapping("/user")
public class UserController {

    private final CreateUserUseCase createUserUsecase;
    private final LoginUserUseCase loginUserUseCase;
    private final LogoutUserUseCase logoutUserUseCase;
    private final ReissueUseCase reissueUseCase;

    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    public ResponseEntity<Void> createUser(@RequestBody SignInRequest request){
        CreateUserCommand createUserCommand = new CreateUserCommand(request.name(), request.username(), request.password());
        createUserUsecase.createUser(createUserCommand);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request){
        LoginUserCommand loginUserCommand = new LoginUserCommand(request.username(), request.password());
        TokenResponse tokenResponse = loginUserUseCase.login(loginUserCommand);
        return ResponseEntity.ok(tokenResponse);
    }

    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal CustomUserDetails userDetails){
        logoutUserUseCase.logout(userDetails.getUser());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "토큰 재발급")
    @PostMapping("/reissue")
    public ResponseEntity<TokenResponse> reissue(@RequestHeader("refreshToken") String refreshToken){
        TokenResponse response = reissueUseCase.reissue(refreshToken);
        return ResponseEntity.ok(response);
    }
}
