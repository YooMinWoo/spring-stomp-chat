package com.example.spring_stomp_chat.domain.user.port.out;

import com.example.spring_stomp_chat.domain.user.model.User;

public interface LoadUserPort {
    User findById(Long userId);

    boolean existsByUsername(String username);

    User findByUsername(String username);
}
