package com.example.spring_stomp_chat.chat.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageJpaRepository extends JpaRepository<ChatMessageJpaEntity, Long> {
}
