package com.example.spring_stomp_chat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SpringStompChatApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringStompChatApplication.class, args);
	}

}
