package com.todo.todoList.service;

import com.todo.join.entity.User;
import com.todo.join.repository.UserRepository;
import com.todo.todoList.dto.TodoDTO;
import com.todo.todoList.entity.Todo;
import com.todo.todoList.repository.TodoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
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

    //FIXME todoList가 비었다고 존재하지 않은 사용자는 아님 -> 다른 예외 케이스 검증필요
    public List<TodoDTO> findTodoById(String name, Date date) {
        List<Todo> todoList = todoRepository.findByUser_NameAndTodoDate(name, date);
        List<TodoDTO> todoDTO = new ArrayList<>();

        if (todoList.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
        }

        for (Todo todo : todoList) {
            todoDTO.add(todoDtoFromTodo(todo));
        }
        return todoDTO;
    }


    public void saveTodo(TodoDTO todoDTO, String name) {

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
    public void updateTodo(TodoDTO todoDTO, String name) throws AccessDeniedException {
        Todo todo = todoRepository.findById(todoDTO.getTodoId())
                .orElseThrow(() -> new EntityNotFoundException("Todo를 찾을 수 없습니다."));

        //FIXME N+1 문제 고려 필요
        if (!todo.getUser().getName().equals(name)) {
            throw new AccessDeniedException("해당 유저는 수정 권한이 없습니다.");
        }

        todo.setTodoDate(todoDTO.getTodoDate());
        todo.setStatus(todoDTO.getStatus());
        todo.setContent(todoDTO.getContent());
    }

    public void deleteTodo(TodoDTO todoDTO, String name) {
        int deletedCount = todoRepository.deleteByIdAndUserName(todoDTO.getTodoId(), name);

        if (deletedCount == 0) {
            throw new IllegalArgumentException("삭제할 Todo가 존재하지 않습니다.");
        }
    }

    @Transactional
    public void updateStatus(TodoDTO todoDTO, String name) throws AccessDeniedException {
        Todo todo = todoRepository.findById(todoDTO.getTodoId())
                .orElseThrow(() -> new IllegalArgumentException("해당 Todo가 존재하지 않습니다."));

        if (!todo.getUser().getName().equals(name)) {
            throw new AccessDeniedException("수정 권한이 없습니다.");
        }

        todo.setStatus(todoDTO.getStatus());
    }


    private TodoDTO todoDtoFromTodo(Todo todo) {
        TodoDTO dto = TodoDTO.builder()
                .todoId(todo.getId())
                .userName(todo.getUser().getName())
                .todoDate(todo.getTodoDate())
                .status(todo.getStatus())
                .content(todo.getContent()).build();

        return dto;
    }

}
