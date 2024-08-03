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
        System.out.println(todoDTO.getTodoDate());

        String name = jwtUtil.getJwtName(request);
        Date date = todoDTO.getTodoDate();

        List<TodoDTO> todoList = todoService.findTodoById(name,date);

        for (TodoDTO a : todoList) {
            System.out.println(a.getContent());
        }

        return ResponseEntity.ok(todoList);
    }

//    @PostMapping("/create")
//    public List<TodoDTO> saveTodo(@RequestBody TodoListDTO todoDTOList) {
//
//        String name = SecurityContextHolder.getContext().getAuthentication().getName();
//
//        todoService.saveTodo(todoDTOList.getTodoList(),name);
//        return todoService.findTodoById(name);
//    }
//
//    @PostMapping("/update")
//    public List<TodoDTO> updateTodo(@RequestBody TodoDTO todoDTO) {
//
//        String name = SecurityContextHolder.getContext().getAuthentication().getName();
//
//        todoService.updateTodo(todoDTO, name);
//        return todoService.findTodoById(name);
//    }
//
//    @PostMapping("/delete")
//    public List<TodoDTO> deleteTodo(@RequestBody TodoDTO todoDTO) {
//
//        String name = SecurityContextHolder.getContext().getAuthentication().getName();
//
//        todoService.deleteTodo(todoDTO, name);
//        return todoService.findTodoById(name);
//    }
}
