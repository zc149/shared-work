package com.todo.todoList.controller;

import com.todo.join.entity.User;
import com.todo.join.jwt.JWTUtil;
import com.todo.todoList.dto.TodoDTO;
import com.todo.todoList.dto.TodoListDTO;
import com.todo.todoList.service.TodoService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@Controller
@RequestMapping("/todolist")
@RequiredArgsConstructor
@Slf4j
public class TodoListController {

    private final TodoService todoService;
    private final JWTUtil jwtUtil;


    @PostMapping
    public ResponseEntity<List<TodoDTO>> findAllTodo(@RequestBody TodoDTO todoDTO, HttpServletRequest request) {
        String name = jwtUtil.getJwtName(request);
        Date date = todoDTO.getTodoDate();
        List<TodoDTO> todoList = todoService.findTodoById(name,date);

        return ResponseEntity.ok(todoList);
    }

    @PostMapping("/create")
    public ResponseEntity<TodoDTO> saveTodo(@RequestBody TodoDTO todoDTO, HttpServletRequest request) {
        String name = jwtUtil.getJwtName(request);
        todoService.saveTodo(todoDTO,name);
        return ResponseEntity.ok(todoDTO);
    }

    @PostMapping("/update")
    public ResponseEntity<TodoDTO> updateTodo(@RequestBody TodoDTO todoDTO, HttpServletRequest request) {
        System.out.println(todoDTO.getStatus());
        String name = jwtUtil.getJwtName(request);
        todoService.updateTodo(todoDTO, name);
        return ResponseEntity.ok(todoDTO);
    }

    @PostMapping("/delete")
    public ResponseEntity<TodoDTO> deleteTodo(@RequestBody TodoDTO todoDTO, HttpServletRequest request) {
        String name = jwtUtil.getJwtName(request);
        todoService.deleteTodo(todoDTO, name);
        return ResponseEntity.ok(todoDTO);
    }

    @PostMapping("/check")
    public ResponseEntity<TodoDTO> checkTodo(@RequestBody TodoDTO todoDTO, HttpServletRequest request) {
        String name = jwtUtil.getJwtName(request);
        todoService.updateStatus(todoDTO,name);
        return ResponseEntity.ok(todoDTO);
    }

}
