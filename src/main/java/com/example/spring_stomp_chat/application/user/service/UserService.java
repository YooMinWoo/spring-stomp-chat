package com.example.spring_stomp_chat.application.user.service;

import com.example.spring_stomp_chat.adapter.user.in.response.TokenResponse;
import com.example.spring_stomp_chat.domain.user.command.CreateUserCommand;
import com.example.spring_stomp_chat.domain.user.command.LoginUserCommand;
import com.example.spring_stomp_chat.domain.user.model.User;
import com.example.spring_stomp_chat.domain.user.model.UserRole;
import com.example.spring_stomp_chat.domain.user.port.in.CreateUserUseCase;
import com.example.spring_stomp_chat.domain.user.port.in.LoginUserUseCase;
import com.example.spring_stomp_chat.domain.user.port.in.LogoutUserUseCase;
import com.example.spring_stomp_chat.domain.user.port.in.ReissueUseCase;
import com.example.spring_stomp_chat.domain.user.port.out.LoadUserPort;
import com.example.spring_stomp_chat.domain.user.port.out.SaveUserPort;
import com.example.spring_stomp_chat.global.jwt.JwtTokenDto;
import com.example.spring_stomp_chat.global.jwt.JwtTokenProvider;
import com.example.spring_stomp_chat.global.redis.RedisService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService implements CreateUserUseCase, LoginUserUseCase, LogoutUserUseCase, ReissueUseCase {

    private final LoadUserPort loadUserPort;
    private final SaveUserPort saveUserPort;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;

    @Override
    public void createUser(CreateUserCommand command) {
        // 1. 중복 검사 (비즈니스 규칙)
        if (loadUserPort.existsByUsername(command.username())) {
            throw new RuntimeException("이미 존재하는 아이디입니다.");
        }

        User user = User.create(
                command.name(),
                command.username(),
                passwordEncoder.encode(command.password()),
                UserRole.USER);
        saveUserPort.save(user);
    }

    @Override
    public TokenResponse login(LoginUserCommand command) {
        User user = loadUserPort.findByUsername(command.username());
        if(!passwordEncoder.matches(command.password(), user.getPassword())){
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        JwtTokenDto jwtTokenDto = jwtTokenProvider.generateToken(user);
        String redisKey = "refreshToken:" + user.getId();
        redisService.setData(redisKey, jwtTokenDto.getRefreshToken(), 1000L * 60 * 60 * 24);
        return TokenResponse.from(jwtTokenDto);
    }

    @Override
    public void logout(User user) {
        redisService.deleteData("refreshToken:" + user.getId());
    }

    @Override
    public TokenResponse reissue(String refreshToken) {
        try{
            jwtTokenProvider.validateToken(refreshToken);
            Long userId = jwtTokenProvider.getUserId(refreshToken);

            // Redis에 저장된 토큰과 일치하는지 확인
            String savedToken = (String) redisService.getData("refreshToken:" + userId);
            if (savedToken == null || !savedToken.equals(refreshToken)) {
                throw new RuntimeException("유효하지 않은 재발급 요청입니다.");
            }

            // 4. 새로운 토큰 세트 생성
            User user = loadUserPort.findById(userId);
            JwtTokenDto newTokens = jwtTokenProvider.generateToken(user);

            // 5. Redis 값 업데이트 (RTR 전략: 기존 토큰 덮어쓰기)
            redisService.setData("refreshToken:" + userId, newTokens.getRefreshToken(), 1000L * 60 * 60 * 24);

            return TokenResponse.from(newTokens);
        }catch (JwtException e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
