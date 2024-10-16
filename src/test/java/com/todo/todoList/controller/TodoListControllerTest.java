package com.todo.todoList.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todo.join.jwt.JWTUtil;
import com.todo.todoList.dto.TodoDTO;
import com.todo.todoList.service.TodoService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TodoListController.class)
class TodoListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TodoService todoService;

    @MockBean
    private JWTUtil jwtUtil;

    @Test
    @WithMockUser( username = "testId", roles = {"USER"})
    @DisplayName("Todo 리스트 조회")
    void findAllTodo() throws Exception {
        // Given
        String userName = "testId";
        Date todoDate = Date.valueOf("2024-10-16");

        TodoDTO todoDTO1 = TodoDTO.builder()
                .userName(userName)
                .todoDate(todoDate)
                .content("테스트1")
                .build();

        TodoDTO todoDTO2 = TodoDTO.builder()
                .userName(userName)
                .todoDate(todoDate)
                .content("테스트2")
                .build();

        List<TodoDTO> todoDTOList = List.of(todoDTO1,todoDTO2);

        // Mocking
        when(todoService.findTodoById(any(String.class), any(Date.class))).thenReturn(todoDTOList);
        when(jwtUtil.getJwtName(any(HttpServletRequest.class))).thenReturn(userName);

        // When & Then
        mockMvc.perform(post("/todolist")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(todoDTO1))
                .characterEncoding("utf-8")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].content").value("테스트1"))
                .andExpect(jsonPath("$[1].content").value("테스트2"));

    }

    @Test
    @WithMockUser( username = "testId", roles = {"USER"})
    @DisplayName("Todo 추가")
    void saveTodo() throws Exception{
        // Given
        String userName = "testId";

        TodoDTO todoDTO = TodoDTO.builder()
                .userName(userName)
                .todoDate(Date.valueOf("2024-10-16"))
                .content("테스트 추가")
                .build();

        // Mocking
        doNothing().when(todoService).saveTodo(any(TodoDTO.class), any(String.class));

        // When & Then
        mockMvc.perform(post("/todolist/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(todoDTO))
                        .characterEncoding("utf-8")
                        .with(csrf()))
                        .andExpect(status().isOk());

    }

    @Test
    @WithMockUser( username = "testId", roles = {"USER"})
    @DisplayName("Todo 수정")
    void updateTodo() throws Exception{
        // Given
        String userName = "testId";

        TodoDTO todoDTO = TodoDTO.builder()
                .todoId(1L)
                .userName(userName)
                .todoDate(Date.valueOf("2024-10-16"))
                .content("테스트 수정")
                .build();

        // Mocking
        doNothing().when(todoService).updateTodo(any(TodoDTO.class), any(String.class));

        // When & Then
        mockMvc.perform(post("/todolist/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(todoDTO))
                        .characterEncoding("utf-8")
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser( username = "testId", roles = {"USER"})
    @DisplayName("Todo 삭제")
    void deleteTodo() throws Exception {
        // Given
        String userName = "testId";

        TodoDTO todoDTO = TodoDTO.builder()
                .todoId(1L)
                .userName(userName)
                .todoDate(Date.valueOf("2024-10-16"))
                .status("완료")
                .content("삭제할 할 일")
                .build();

        // Mocking
        doNothing().when(todoService).deleteTodo(any(TodoDTO.class), any(String.class));

        // When & Then
        mockMvc.perform(post("/todolist/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(todoDTO))
                        .characterEncoding("utf-8")
                        .with(csrf()))
                        .andExpect(status().isOk());
    }

    @Test
    @WithMockUser( username = "testId", roles = {"USER"})
    @DisplayName("Todo Status 수정")
    void checkTodo() throws Exception {
        // Given
        String userName = "testId";

        TodoDTO todoDTO = TodoDTO.builder()
                .todoId(1L)
                .userName(userName)
                .status("완료")
                .build();

        // Mocking
        when(jwtUtil.getJwtName(any(HttpServletRequest.class))).thenReturn(userName);
        doNothing().when(todoService).updateStatus(any(TodoDTO.class), any(String.class));

        // When & Then
        mockMvc.perform(post("/todolist/check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(todoDTO))
                        .characterEncoding("utf-8")
                        .with(csrf()))
                        .andExpect(status().isOk());
    }
}