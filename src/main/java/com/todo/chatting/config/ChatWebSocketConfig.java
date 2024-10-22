package com.todo.chatting.config;


import com.todo.chatting.service.ChatRedisUtil;
import com.todo.chatting.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@EnableWebSocketMessageBroker
@RequiredArgsConstructor
@Configuration
public class ChatWebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final ChatRedisUtil chatRedisUtil;
    private final ChatService chatService;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chatting")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/subscription");
        registry.setApplicationDestinationPrefixes("/publication");
    }


    /*
        채팅방 입장, 퇴장 로직
        지금은 하나의 채팅방만 생각하고 만든거라 기능 추가시 수정해야함
    */

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        chatRedisUtil.incrementUserCount("1");

        if (chatRedisUtil.getUserCount("1") == 1 && chatRedisUtil.getChatMessages("1").size() == 0) {
            chatService.saveAllMessagesToRedis();
            chatRedisUtil.getChatMessages("1");
        }

    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {

        chatRedisUtil.decrementUserCount("1");

            if (chatRedisUtil.getUserCount("1") <= 0) {
                chatService.saveDb("1");
                chatRedisUtil.deleteChatRoomMessages("1");
            }
    }

}
