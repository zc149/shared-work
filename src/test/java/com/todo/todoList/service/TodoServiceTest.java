package com.todo.todoList.service;

import com.todo.join.entity.User;
import com.todo.join.repository.UserRepository;
import com.todo.todoList.dto.TodoDTO;
import com.todo.todoList.entity.Todo;
import com.todo.todoList.repository.TodoRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TodoServiceTest {

    @Autowired
    private TodoService todoService;

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("회원 ID로 TodoList 조회")
    public void findTodoById() {
        // Given
        User user = User.builder()
                .name("testId")
                .build();

        userRepository.save(user);

        Todo todo1 = Todo.builder()
                .user(user)
                .todoDate(Date.valueOf("2024-10-16"))
                .content("테스트1")
                .build();

        Todo todo2 = Todo.builder()
                .user(user)
                .todoDate(Date.valueOf("2024-10-16"))
                .content("테스트2")
                .build();

        todoRepository.save(todo1);
        todoRepository.save(todo2);

        // When
        List<TodoDTO> list = todoService.findTodoById(user.getName(),Date.valueOf("2024-10-16"));

        // Then
        assertThat(list).hasSize(2);

    }

    @Test
    @DisplayName("Todo 저장")
    public void saveTodo() {
        // Given
        User user = User.builder()
                .name("testId")
                .build();

        userRepository.save(user);

        TodoDTO todoDTO = TodoDTO.builder()
                .userName(user.getName())
                .todoDate(Date.valueOf("2024-10-16"))
                .content("테스트 저장확인")
                .build();

        // When
        todoService.saveTodo(todoDTO,user.getName());

        // Then
        List<Todo> todoList = todoRepository.findByUser_NameAndTodoDate(user.getName(), Date.valueOf("2024-10-16"));
        assertThat(todoList.get(0).getContent()).isEqualTo("테스트 저장확인");
    }

    @Test
    @DisplayName("Todo 수정")
    public void updateTodo() {
        // Given
        User user = User.builder()
                .name("testId")
                .build();

        Todo todo1 = Todo.builder()
                .user(user)
                .todoDate(Date.valueOf("2024-10-16"))
                .status("false")
                .content("테스트 수정전")
                .build();

        todoRepository.save(todo1);

        // When
        TodoDTO todoDTO = TodoDTO.builder()
                .todoId(todo1.getId())
                .userName(user.getName())
                .todoDate(Date.valueOf("2024-10-16"))
                .status("true")
                .content("테스트 수정완료")
                .build();

        todoService.updateTodo(todoDTO, user.getName());

        // Then
        Optional<Todo> updatedTodo = todoRepository.findById(todo1.getId());
        Todo todo = updatedTodo.orElseThrow(() -> new AssertionError("Todo not found"));
        assertThat(todo.getStatus()).isEqualTo("true");

    }

    @Test
    @DisplayName("Todo 삭제")
    public void deleteTodo() {
        // Given
        User user = User.builder()
                .name("testId")
                .build();

        Todo todo = Todo.builder()
                .user(user)
                .todoDate(Date.valueOf("2024-10-16"))
                .status("false")
                .content("테스트 삭제전")
                .build();

        Todo savedTodo = todoRepository.save(todo);

        // When
        TodoDTO todoDTO = TodoDTO.builder()
                    .todoId(savedTodo.getId())
                    .userName(savedTodo.getUser().getName())
                    .build();

        todoService.deleteTodo(todoDTO, user.getName());

        // Then
        List<Todo> todoList = todoRepository.findByUser_NameAndTodoDate(user.getName(),Date.valueOf("2024-10-16"));
        assertThat(todoList).isEmpty();
    }

    @Test
    @DisplayName("Todo 상태 변경")
    public void updateStatus () {
        // Given
        User user = User.builder()
                .name("testId")
                .build();

        Todo todo = Todo.builder()
                .user(user)
                .todoDate(Date.valueOf("2024-10-16"))
                .status("false")
                .content("테스트 Todo 수행전")
                .build();

        Todo savedTodo = todoRepository.save(todo);

        // When

        TodoDTO todoDTO = TodoDTO.builder()
                    .todoId(savedTodo.getId())
                    .status("true")
                    .build();

        todoService.updateStatus(todoDTO, user.getName());

        // Then
        Optional<Todo> updatedTodoOptional = todoRepository.findById(savedTodo.getId());
        Todo updatedTodo = updatedTodoOptional.orElseThrow(() -> new AssertionError("Todo not found"));
        assertThat(updatedTodo.getStatus()).isEqualTo("true");
    }

}