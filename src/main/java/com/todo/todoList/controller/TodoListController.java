package com.todo.todoList.controller;

import com.todo.join.entity.User;
import com.todo.todoList.dto.TodoDTO;
import com.todo.todoList.dto.TodoListDTO;
import com.todo.todoList.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/todolist")
@RequiredArgsConstructor
public class TodoListController {

    private final TodoService todoService;

//    @PostMapping
//    public List<TodoDTO> findAllTodo(@RequestBody User user) {
//        return todoService.findTodoById(user.getName());
//    }

    @GetMapping
    public String findAllTodo(Model model) {

        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        List<TodoDTO> todoList = todoService.findTodoById(name);
        model.addAttribute("todoList", todoList);
        return "home";
    }

    @PostMapping("/create")
    public List<TodoDTO> saveTodo(@RequestBody TodoListDTO todoDTOList) {

        String name = SecurityContextHolder.getContext().getAuthentication().getName();

        todoService.saveTodo(todoDTOList.getTodoList(),name);
        return todoService.findTodoById(name);
    }

    @PostMapping("/update")
    public List<TodoDTO> updateTodo(@RequestBody TodoDTO todoDTO) {

        String name = SecurityContextHolder.getContext().getAuthentication().getName();

        todoService.updateTodo(todoDTO, name);
        return todoService.findTodoById(name);
    }

    @PostMapping("/delete")
    public List<TodoDTO> deleteTodo(@RequestBody TodoDTO todoDTO) {

        String name = SecurityContextHolder.getContext().getAuthentication().getName();

        todoService.deleteTodo(todoDTO, name);
        return todoService.findTodoById(name);
    }
}
