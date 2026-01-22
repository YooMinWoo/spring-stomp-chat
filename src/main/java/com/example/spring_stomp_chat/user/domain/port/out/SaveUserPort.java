package com.example.spring_stomp_chat.user.domain.port.out;

import com.example.spring_stomp_chat.user.domain.model.User;

public interface SaveUserPort {
    void save(User user);
}
