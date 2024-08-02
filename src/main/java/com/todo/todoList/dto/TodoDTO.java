package com.todo.todoList.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TodoDTO {
    private Long todoId;
    private String userName;
    private Date todoDate;
    private String status;
    private String content;
    private int dDay;
}
