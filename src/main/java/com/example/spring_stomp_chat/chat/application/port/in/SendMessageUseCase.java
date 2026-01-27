package com.example.spring_stomp_chat.chat.application.port.in;

import com.example.spring_stomp_chat.chat.application.command.SendMessageCommand;

public interface SendMessageUseCase {

    void sendMessage(SendMessageCommand command);
}
