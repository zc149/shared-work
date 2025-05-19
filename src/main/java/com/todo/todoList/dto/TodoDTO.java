package com.todo.todoList.dto;

import lombok.*;

import java.sql.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TodoDTO {
    private Long todoId;
    private String userName;
    private Date todoDate;
    private String status;
    private String content;
    private int dDay;
}
