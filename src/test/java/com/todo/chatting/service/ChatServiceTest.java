package com.todo.chatting.service;

import com.todo.chatting.dto.MessageDto;
import com.todo.chatting.entity.Message;
import com.todo.chatting.repository.ChatRepository;
import com.todo.join.entity.User;
import com.todo.join.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ChatServiceTest {

    @InjectMocks
    private ChatService chatService;

    @Mock
    private ChatRepository chatRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ChatRedisUtil chatRedisUtil;

    @Test
    public void 초기_채팅방의_메세지를_DB에_저장한다() {
        //Given
        when(chatRepository.findTopByOrderByIdDesc()).thenReturn(null);

        List<MessageDto> dtos = new ArrayList<>();
        MessageDto messageDto1 = MessageDto.builder().userId(1L).message("123").date(String.valueOf(Timestamp.valueOf(LocalDateTime.now()))).build();
        MessageDto messageDto2 = MessageDto.builder().userId(2L).message("456").date(String.valueOf(Timestamp.valueOf(LocalDateTime.now()))).build();
        dtos.add(messageDto1);
        dtos.add(messageDto2);

        when(chatRedisUtil.getChatMessages(anyString())).thenReturn(dtos);

        when(userRepository.findById(1L)).thenReturn(Optional.of(User.builder().id(1L).name("name").build()));
        when(userRepository.findById(2L)).thenReturn(Optional.of(User.builder().id(2L).name("name2").build()));

        //When
        chatService.saveDb("1L");

        //Then
        verify(chatRepository, times(2)).save(any(Message.class));
    }

    @Test
    public void 추가된_메세지를_DB에_저장한다() {
        //Given
        Message latestMessage = Message.builder()
                .id(10L)
                .user(User.builder().id(1L).name("name").build())
                .message("hi")
                .createdDate(Timestamp.valueOf(LocalDateTime.now())).build();

        when(chatRepository.findTopByOrderByIdDesc()).thenReturn(latestMessage);

        List<MessageDto> dtos = new ArrayList<>();
        MessageDto messageDto1 = MessageDto.builder().userId(1L).message("123").date(String.valueOf(Timestamp.valueOf(LocalDateTime.now()))).build();
        MessageDto messageDto2 = MessageDto.builder().userId(2L).message("456").date(String.valueOf(Timestamp.valueOf(LocalDateTime.now()))).build();
        dtos.add(messageDto1);
        dtos.add(messageDto2);

        when(chatRedisUtil.getChatMessages(anyString())).thenReturn(dtos);

        when(userRepository.findById(1L)).thenReturn(Optional.of(User.builder().id(1L).name("name").build()));
        when(userRepository.findById(2L)).thenReturn(Optional.of(User.builder().id(2L).name("name2").build()));

        //When
        chatService.saveDb("1L");

        //Then
        verify(chatRepository, times(2)).save(any(Message.class));
    }

    @Test
    public void 채팅DB를_Redis에_전체_저장한다() {
        //Given
        List<Message> messages = new ArrayList<>();

        Message message = Message.builder()
                .id(10L)
                .user(User.builder().id(1L).name("name").build())
                .message("hi")
                .createdDate(Timestamp.valueOf(LocalDateTime.now())).build();

        Message message2 = Message.builder()
                .id(11L)
                .user(User.builder().id(2L).name("name2").build())
                .message("hi2")
                .createdDate(Timestamp.valueOf(LocalDateTime.now())).build();

        messages.add(message);
        messages.add(message2);

        when(chatRepository.findAll()).thenReturn(messages);

        //When
        chatService.saveAllMessagesToRedis();

        //Then
        verify(chatRedisUtil, times(2)).saveChatMessage(anyString(), any(MessageDto.class));
    }
}
