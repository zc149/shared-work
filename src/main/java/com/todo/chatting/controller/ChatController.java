package com.todo.chatting.controller;


import com.todo.chatting.dto.MessageDto;
import com.todo.chatting.service.ChatRedisUtil;
import com.todo.chatting.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatRedisUtil chatRedisUtil;
    private final ChatService chatService;

    @MessageMapping("/chatting/{chattingRoomId}")
    public void sendMessage(@DestinationVariable Long chattingRoomId, MessageDto messageDto) {
        String dateFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        String formattedDate = sdf.format(new Date());


        messageDto.setDate(formattedDate);
        chatRedisUtil.saveChatMessage("1", messageDto);
        simpMessagingTemplate.convertAndSend("/subscription/chatting/" + chattingRoomId, messageDto);
    }

    @GetMapping("/chatting/{chattingRoomId}/messages")
    public List<MessageDto> getMessages(@PathVariable Long chattingRoomId) {

        List<MessageDto> messages = chatRedisUtil.getChatMessages(String.valueOf(chattingRoomId));

        if (messages.size() == 0) {
            chatService.saveAllMessagesToRedis();
        }

        return chatRedisUtil.getChatMessages(String.valueOf(chattingRoomId));
    }
}

