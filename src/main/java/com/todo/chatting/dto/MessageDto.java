package com.todo.chatting.dto;

import lombok.*;


@Data
@AllArgsConstructor
@Builder
@ToString
public class MessageDto {
    private Long userId;
    private String message;
    private String writer;
    private String date;
}
