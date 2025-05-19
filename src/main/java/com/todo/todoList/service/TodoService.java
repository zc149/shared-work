package com.todo.todoList.service;

import com.todo.join.entity.User;
import com.todo.join.repository.UserRepository;
import com.todo.todoList.dto.TodoDTO;
import com.todo.todoList.entity.Todo;
import com.todo.todoList.repository.TodoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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

    public List<TodoDTO> findTodoById(String name, Date date) {
        List<Todo> todoList = todoRepository.findByUser_NameAndTodoDate(name,date).orElse(null);
        List<TodoDTO> todoDTO = new ArrayList<>();

        if (todoList == null) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
        }

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

    public void saveTodo(TodoDTO todoDTO , String name) {

            Optional<User> userOptional = Optional.ofNullable(userRepository.findByName(name));

            if (userOptional.isPresent()) {
                User user = userOptional.get();

                Todo todo = Todo.builder()
                        .user(user)
                        .todoDate(todoDTO.getTodoDate())
                        .createdDate(Date.valueOf(LocalDate.now()))
                        .status("false")
                        .content(todoDTO.getContent())
                        .build();

                todoRepository.save(todo);
        }
    }


    // 수정이랑 삭제 로직을 id로 검색하는게 아니라 findbyName으로 해서 그 아이디만 지우는게 더 좋지 않나? (생각중)
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

    @Transactional
    public void updateStatus(TodoDTO todoDTO, String name) {
        Optional<Todo> todoOptional = todoRepository.findById(todoDTO.getTodoId());

        if(todoOptional.isPresent()) {
            Todo todo = todoOptional.get();
            if (todo.getUser().getName().equals(name)) {
                todo.setStatus(todoDTO.getStatus());
            }
        }
    }

}
