package com.example.spring_stomp_chat.user.domain.model;

import lombok.Getter;


@Getter
public class User {
    private final Long id;
    private String name;
    private final String username;
    private String password;
    private UserRole userRole;

    private User(Long id, String name, String username, String password, UserRole userRole) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.userRole = userRole;
    }

    public static User create(String name, String username, String password, UserRole userRole){
        return new User(null, name, username, password, userRole);
    }

    public static User withId(Long id, String name, String username, String password, UserRole userRole){
        return new User(id, name, username, password, userRole);
    }
}
