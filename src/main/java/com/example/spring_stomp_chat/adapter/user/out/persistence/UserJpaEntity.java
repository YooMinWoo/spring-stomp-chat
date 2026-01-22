package com.example.spring_stomp_chat.adapter.user.out.persistence;

import com.example.spring_stomp_chat.domain.user.model.User;
import com.example.spring_stomp_chat.domain.user.model.UserRole;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "USERS")
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 외부 생성 방지
public class UserJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String username;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    public static UserJpaEntity from(User user){
        UserJpaEntity entity = new UserJpaEntity();
        entity.name = user.getName();
        entity.username = user.getUsername();
        entity.password = user.getPassword();
        entity.userRole = user.getUserRole();
        return entity;
    }

    public User toDomain(){
        return User.withId(id, name, username, password, userRole);
    }
}
