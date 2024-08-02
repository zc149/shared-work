package com.todo.chatting.controller;


import com.todo.chatting.dto.MessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/chatting/{chattingRoomId}/entered")
    public void entered(@DestinationVariable Long chattingRoomId, MessageDto message) {
        final String payload = message.getWriter() + "님이 입장하셨습니다.";
        simpMessagingTemplate.convertAndSend("/subscription/chatting/" + chattingRoomId, payload);
        log.info("# roomId = {}", chattingRoomId);
        log.info("# message = {}", message);
    }

    @MessageMapping("/chatting/{chattingRoomId}")
    public void sendMessage(@DestinationVariable Long chattingRoomId, MessageDto message) {
        log.info("# roomId = {}", chattingRoomId);
        log.info("# message = {}", message.getMessage());

        simpMessagingTemplate.convertAndSend("/subscription/chatting/" + chattingRoomId, message.getMessage());
    }
}

