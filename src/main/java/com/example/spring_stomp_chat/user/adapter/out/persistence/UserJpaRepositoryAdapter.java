package com.example.spring_stomp_chat.user.adapter.out.persistence;

import com.example.spring_stomp_chat.user.adapter.out.persistence.UserJpaEntity;
import com.example.spring_stomp_chat.user.adapter.out.persistence.UserJpaRepository;
import com.example.spring_stomp_chat.user.domain.model.User;
import com.example.spring_stomp_chat.user.application.port.out.LoadUserPort;
import com.example.spring_stomp_chat.user.application.port.out.SaveUserPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class UserJpaRepositoryAdapter implements LoadUserPort, SaveUserPort {

    private final UserJpaRepository userJpaRepository;

    @Override
    public void save(User user) {
        userJpaRepository.save(UserJpaEntity.from(user));
    }

    @Override
    public User findById(Long userId) {
        return userJpaRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 유저입니다"))
                .toDomain();
    }

    @Override
    public User findByUsername(String username) {
        return userJpaRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 유저입니다"))
                .toDomain();
    }

    @Override
    public boolean existsByUsername(String username) {
        return userJpaRepository.existsByUsername(username);
    }
}
