package com.todo.chatting.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.todo.chatting.dto.MessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ChatRedisUtil {

    private final StringRedisTemplate chatRedisTemplate;
    private final ObjectMapper objectMapper;

    public void saveChatMessage(String chatRoomId, MessageDto message) {
        String key = "chatroom:" + chatRoomId;
        try {
            String messageJson = objectMapper.writeValueAsString(message); // Message 객체를 JSON으로 변환
            chatRedisTemplate.opsForList().rightPush(key, messageJson);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public List<MessageDto> getChatMessages(String chattingRoomId) {
        String key = "chatroom:" + chattingRoomId;
        List<String> messageJsonList = chatRedisTemplate.opsForList().range(key, 0, -1);

        List<MessageDto> messages = new ArrayList<>();

        if (messageJsonList != null) {
            for (String messageJson : messageJsonList) {
                try {
                    MessageDto messageDTO = objectMapper.readValue(messageJson, MessageDto.class);
                    messages.add(messageDTO);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }
        return messages;
    }

    public void deleteChatRoomMessages(String chattingRoomId) {
        String key = "chatroom:" + chattingRoomId;
        chatRedisTemplate.delete(key);
    }



    /*
     사용자가 최초 들어왔을때 DB -> Redis
     마지막 사용자가 연결이 끊어졌을때 Redis -> DB
     구현을 위한 메소드 시작점
     */

    public void incrementUserCount(String chattingRoomId) {
        ValueOperations<String, String> operations = chatRedisTemplate.opsForValue();
        operations.increment("chatroom:" + chattingRoomId + ":userCount");

        // 현재 사용자 수 출력
        Long currentCount = getUserCount(chattingRoomId);
        System.out.println("현재 사용자 수 (증가 후): " + currentCount);
    }

    public void decrementUserCount(String chattingRoomId) {
        ValueOperations<String, String> operations = chatRedisTemplate.opsForValue();
        operations.decrement("chatroom:" + chattingRoomId + ":userCount");

        // 현재 사용자 수 출력
        Long currentCount = getUserCount(chattingRoomId);
        System.out.println("현재 사용자 수 (감소 후): " + currentCount);
    }

    public Long getUserCount(String chattingRoomId) {
        ValueOperations<String, String> operations = chatRedisTemplate.opsForValue();
        String count = operations.get("chatroom:" + chattingRoomId + ":userCount");
        return count == null ? 0 : Long.parseLong(count);
    }
}
