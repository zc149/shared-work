package com.todo.chatting.service;

import com.todo.chatting.dto.MessageDto;
import com.todo.chatting.entity.Message;
import com.todo.chatting.repository.ChatRepository;
import com.todo.join.entity.User;
import com.todo.join.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final ChatRedisUtil chatRedisUtil;

    //FIXME findByID 를 계속 호출하는 부분을 고쳐야함
    @Transactional
    public void saveDb(String chattingRoomId) {

        Timestamp latestTimestamp = Optional.ofNullable(chatRepository.findTopByOrderByIdDesc()).map(Message::getCreatedDate).orElse(null);
        List<MessageDto> messages = chatRedisUtil.getChatMessages(chattingRoomId);

        Stream<MessageDto> messageDtoStream = messages.stream();

        if (latestTimestamp != null) {
            messageDtoStream = messageDtoStream.filter(
                    messageDto -> Timestamp.valueOf(messageDto.getDate()).after(latestTimestamp)
            );
        }

        messageDtoStream.forEach(messageDTO -> userRepository.findById(messageDTO.getUserId()).ifPresent(
                user -> {
                    Message chatMessage = messageDtoFromMessage(messageDTO, user);
                    chatRepository.save(chatMessage);
                }
        ));

    }

    @Transactional
    public void saveAllMessagesToRedis() {
        List<Message> messages = chatRepository.findAll();

        if (messages.isEmpty()) return;

        messages.forEach(
                message -> {
                    MessageDto messageDto = messageFromMessageDto(message);
                    chatRedisUtil.saveChatMessage("1", messageDto);
                });

    }

    private Message messageDtoFromMessage(MessageDto messageDTO, User user) {
        Message message = Message.builder()
                .message(messageDTO.getMessage())
                .createdDate(Timestamp.valueOf(messageDTO.getDate()))
                .user(user)
                .build();
        return message;
    }

    private MessageDto messageFromMessageDto(Message message) {
        MessageDto messageDto = MessageDto.builder()
                .userId(message.getUser().getId())
                .writer(message.getUser().getNickName())
                .message(message.getMessage())
                .date(String.valueOf(message.getCreatedDate()))
                .build();
        return messageDto;
    }
}
