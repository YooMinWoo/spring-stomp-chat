package com.example.spring_stomp_chat.user.application.port.out;

import com.example.spring_stomp_chat.user.domain.model.User;

import java.util.List;

public interface LoadUserPort {
    User findById(Long userId);

    boolean existsByUsername(String username);

    User findByUsername(String username);

    List<User> loadUsersByIds(List<Long> targetUserIds);
}
