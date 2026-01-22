package com.example.spring_stomp_chat.application.user.service;


import com.example.spring_stomp_chat.adapter.user.in.response.TokenResponse;
import com.example.spring_stomp_chat.domain.user.command.CreateUserCommand;
import com.example.spring_stomp_chat.domain.user.command.LoginUserCommand;
import com.example.spring_stomp_chat.domain.user.model.User;
import com.example.spring_stomp_chat.domain.user.model.UserRole;
import com.example.spring_stomp_chat.domain.user.port.out.LoadUserPort;
import com.example.spring_stomp_chat.domain.user.port.out.SaveUserPort;
import com.example.spring_stomp_chat.global.jwt.JwtTokenDto;
import com.example.spring_stomp_chat.global.jwt.JwtTokenProvider;
import com.example.spring_stomp_chat.global.redis.RedisService;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock private SaveUserPort saveUserPort;
    @Mock private LoadUserPort loadUserPort;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtTokenProvider jwtTokenProvider;
    @Mock private RedisService redisService;

    @InjectMocks
    private UserService userService;

    // 1. 회원가입(CreateUserUseCase) 관련 테스트 그룹
    @Nested
    @DisplayName("회원가입 테스트 (CreateUserUseCase)")
    class CreateUser {

        @Test
        @DisplayName("성공: 모든 정보가 유효할 때 유저를 저장한다")
        void success() {
            //given
            CreateUserCommand command = new CreateUserCommand("홍길동", "hong", "1234");
            given(passwordEncoder.encode(command.password())).willReturn("encoded_1234");
            given(loadUserPort.existsByUsername(command.username())).willReturn(false);

            //when
            userService.createUser(command);

            //then
            ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
            verify(saveUserPort).save(userCaptor.capture()); // save가 호출되었는지 가로챔

            User savedUser = userCaptor.getValue();
            assertThat(savedUser.getPassword()).isEqualTo("encoded_1234");
            assertThat(savedUser.getName()).isEqualTo("홍길동");
        }

        @Test
        @DisplayName("실패: 이미 존재하는 아이디인 경우 예외가 발생한다")
        void fail_duplicate_username() {
            // given
            CreateUserCommand command = new CreateUserCommand("홍길동", "duplicate_id", "pw");
            // Mocking: 중복된 아이디가 있다고 가정 (true 리턴)
            given(loadUserPort.existsByUsername("duplicate_id")).willReturn(true);

            // when & then
            assertThatThrownBy(() -> userService.createUser(command))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("이미 존재하는 아이디");

            // 검증: 저장은 호출되지 않아야 함
            verify(saveUserPort, never()).save(any(User.class));
        }
    }

    // 1. 회원가입(CreateUserUseCase) 관련 테스트 그룹
    @Nested
    @DisplayName("로그인 테스트 (LoginUserUseCase)")
    class Login {

        @Test
        @DisplayName("성공: 아이디/비밀번호가 일치할 때 로그인을 성공한다")
        void success() {
            // given (준비)
            LoginUserCommand command = new LoginUserCommand("user1", "password123");
            User mockUser = User.withId(1L, "홍길동", "user1", "encoded_password", UserRole.USER);
            JwtTokenDto mockJwt = new JwtTokenDto("access-token", "refresh-token");

            given(loadUserPort.findByUsername(anyString())).willReturn(mockUser);
            given(passwordEncoder.matches(command.password(), mockUser.getPassword())).willReturn(true);
            given(jwtTokenProvider.generateToken(mockUser)).willReturn(mockJwt);

            // when (실행)
            TokenResponse response = userService.login(command);

            // then (검증)
            assertThat(response.accessToken()).isEqualTo("access-token");
            assertThat(response.refreshToken()).isEqualTo("refresh-token");

            // Redis에 정확한 키와 값, TTL이 저장되었는지 검증 (실무 핵심)
            verify(redisService, times(1)).setData(
                    eq("refreshToken:1"),
                    eq("refresh-token"),
                    anyLong()
            );
        }
        @Test
        @DisplayName("로그인 실패: 비밀번호가 다르면 예외가 발생한다")
        void login_fail_password_mismatch() {
            // given
            LoginUserCommand command = new LoginUserCommand("user1", "wrong-password");
            User mockUser = User.withId(1L, "홍길동", "user1", "encoded_password", UserRole.USER);

            given(loadUserPort.findByUsername(anyString())).willReturn(mockUser);
            given(passwordEncoder.matches(anyString(), anyString())).willReturn(false);

            // when & then
            assertThatThrownBy(() -> userService.login(command))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("비밀번호가 일치하지 않습니다.");

            // 비밀번호가 틀렸으므로 토큰 생성이나 Redis 저장은 절대 호출되면 안됨
            verify(jwtTokenProvider, never()).generateToken(any());
            verify(redisService, never()).setData(anyString(), any(), anyLong());
        }

    }
    @Nested
    @DisplayName("로그아웃 테스트 (LogoutUserUseCase)")
    class logout {
        @Test
        @DisplayName("로그아웃 성공")
        void success(){
            // given
            User mockUser = User.withId(1L, "홍길동", "user1", "encoded_password", UserRole.USER);

            // when
            userService.logout(mockUser);

            // then
            verify(redisService, times(1)).deleteData("refreshToken:1");
        }
    }

    @Nested
    @DisplayName("토큰 재발급 테스트 (ReissueUseCase)")
    class reissue {
        @Test
        @DisplayName("토큰 재발급 성공")
        void success(){
            // given
            User mockUser = User.withId(1L, "홍길동", "user1", "encoded_password", UserRole.USER);
            String refreshToken = "old-refreshToken";
            JwtTokenDto jwtTokenDto = new JwtTokenDto("new-accessToken", "new-refreshToken");

            given(jwtTokenProvider.validateToken(refreshToken)).willReturn(true);
            given(jwtTokenProvider.getUserId(refreshToken)).willReturn(1L);
            given((String) redisService.getData("refreshToken:" + 1)).willReturn(refreshToken);
            given(loadUserPort.findById(1L)).willReturn(mockUser);
            given(jwtTokenProvider.generateToken(mockUser)).willReturn(jwtTokenDto);

            // when
            TokenResponse response = userService.reissue(refreshToken);

            // then
            assertThat(response.accessToken()).isEqualTo("new-accessToken");
            assertThat(response.refreshToken()).isEqualTo("new-refreshToken");
            verify(redisService).setData(eq("refreshToken:1"), eq("new-refreshToken"), anyLong());
        }

        @Test
        @DisplayName("재발급 실패: Redis에 저장된 토큰과 다르면 예외가 발생한다 (RTR 방어)")
        void reissue_fail_token_mismatch() {
            // given
            String stolenToken = "stolen-rt";
            String currentTokenInRedis = "latest-rt"; // 이미 갱신되어 Redis에는 다른 값이 있음
            Long userId = 1L;

            given(jwtTokenProvider.getUserId(stolenToken)).willReturn(userId);
            given(redisService.getData("refreshToken:" + userId)).willReturn(currentTokenInRedis);

            // when & then
            assertThatThrownBy(() -> userService.reissue(stolenToken))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("유효하지 않은 재발급 요청입니다.");

            // 보안 위반이므로 새로운 토큰이 생성되어서는 안 됨
            verify(jwtTokenProvider, never()).generateToken(any());
        }

        @Test
        @DisplayName("재발급 실패: JWT 자체가 만료되거나 손상된 경우")
        void reissue_fail_invalid_jwt() {
            // given
            String invalidToken = "invalid-rt";
            // validateToken에서 예외 발생 시뮬레이션
            doThrow(new JwtException("INVALID_TOKEN"))
                    .when(jwtTokenProvider).validateToken(invalidToken);

            // when & then
            assertThatThrownBy(() -> userService.reissue(invalidToken))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("INVALID_TOKEN");
        }
    }
}