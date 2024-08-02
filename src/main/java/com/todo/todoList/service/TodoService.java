package com.todo.todoList.service;

import com.todo.join.entity.User;
import com.todo.join.repository.UserRepository;
import com.todo.todoList.dto.TodoDTO;
import com.todo.todoList.entity.Todo;
import com.todo.todoList.repository.TodoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    public List<TodoDTO> findTodoById(String name) {
        List<Todo> todoList = todoRepository.findByUser_Name(name);
        List<TodoDTO> todoDTO = new ArrayList<>();

        for (Todo todo : todoList) {
            TodoDTO dto = TodoDTO.builder()
                    .todoId(todo.getId())
                    .userName(todo.getUser().getName())
                    .todoDate(todo.getTodoDate())
                    .status(todo.getStatus())
                    .content(todo.getContent()).build();

            todoDTO.add(dto);
        }
        return todoDTO;
    }

    public void saveTodo(List<TodoDTO> todoDTOList, String name) {

        List<Todo> todoList = new ArrayList<>();

        for (TodoDTO todoDTO : todoDTOList) {
            Optional<User> userOptional = Optional.ofNullable(userRepository.findByName(name));
            if (userOptional.isPresent()) {
                User user = userOptional.get();

                Todo todo = Todo.builder()
                        .user(user)
                        .todoDate(todoDTO.getTodoDate())
                        .createdDate(Date.valueOf(LocalDate.now()))
                        .status(todoDTO.getStatus())
                        .content(todoDTO.getContent())
                        .build();

                todoRepository.save(todo);
            }
        }
    }

    @Transactional
    public void updateTodo(TodoDTO todoDTO, String name) {
        Optional<Todo> todoOptional = todoRepository.findById(todoDTO.getTodoId());

        if(todoOptional.isPresent()) {
          Todo todo = todoOptional.get();
            if (todo.getUser().getName().equals(name)) {
                todo.setTodoDate(todoDTO.getTodoDate());
                todo.setStatus(todoDTO.getStatus());
                todo.setContent(todoDTO.getContent());
            }
        }

    }

    public void deleteTodo(TodoDTO todoDTO, String name) {
        Optional<Todo> todoOptional = todoRepository.findById(todoDTO.getTodoId());

        if (todoOptional.isPresent()) {
            Todo todo = todoOptional.get();
                if (todo.getUser().getName().equals(name)) {
                    todoRepository.deleteById(todoDTO.getTodoId());
                }
        }


    }
}
