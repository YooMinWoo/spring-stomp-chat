package com.example.spring_stomp_chat.user.domain.port.out;

import com.example.spring_stomp_chat.user.domain.model.User;

public interface LoadUserPort {
    User findById(Long userId);

    boolean existsByUsername(String username);

    User findByUsername(String username);
}
