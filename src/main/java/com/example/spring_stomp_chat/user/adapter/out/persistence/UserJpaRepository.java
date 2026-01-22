package com.example.spring_stomp_chat.user.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserJpaEntity, Long> {
    boolean existsByUsername(String username);

    Optional<UserJpaEntity> findByUsername(String username);
}
