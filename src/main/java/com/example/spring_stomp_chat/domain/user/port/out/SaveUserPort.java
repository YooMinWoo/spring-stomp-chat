package com.example.spring_stomp_chat.domain.user.port.out;

import com.example.spring_stomp_chat.domain.user.model.User;

public interface SaveUserPort {
    void save(User user);
}
