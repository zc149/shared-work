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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final ChatRedisUtil chatRedisUtil;

    @Transactional
    public void saveDb(String chattingRoomId) {

        Timestamp latestTimestamp = chatRepository.findLatestMessageDate();

        List<MessageDto> messages = chatRedisUtil.getChatMessages(chattingRoomId);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


        for (MessageDto messageDTO : messages) {
            Optional<User> userOptional = userRepository.findById(messageDTO.getUserId());

            if (userOptional.isPresent()) {

                User user = userOptional.get();
                try {
                    java.util.Date utilDate = sdf.parse(messageDTO.getDate());
                    java.sql.Timestamp sqlTimestamp = new java.sql.Timestamp(utilDate.getTime());

                    if (latestTimestamp == null || sqlTimestamp.after(latestTimestamp)) {
                        Message chatMessage = Message.builder()
                                .message(messageDTO.getMessage())
                                .createdDate(sqlTimestamp)
                                .user(user)
                                .build();

                        chatRepository.save(chatMessage);
                    }
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

            }
        }
    }

    @Transactional
    public void saveAllMessagesToRedis () {
        List<Message> messages = chatRepository.findAll();

        for (Message message : messages) {
            MessageDto messageDto = MessageDto.builder()
                    .userId(message.getUser().getId())
                    .writer(message.getUser().getNickName())
                    .message(message.getMessage())
                    .date(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(message.getCreatedDate()))
                    .build();

            chatRedisUtil.saveChatMessage("1", messageDto);
        }

    }
}
