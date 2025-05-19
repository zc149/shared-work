package com.todo.todoList.service;

import com.todo.join.entity.User;
import com.todo.join.repository.UserRepository;
import com.todo.todoList.dto.TodoDTO;
import com.todo.todoList.entity.Todo;
import com.todo.todoList.repository.TodoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    @InjectMocks
    private TodoService todoService;

    @Mock
    private TodoRepository todoRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    public void 회원ID로_TodoList를_조회_할_수_있다() {
        // Given
        User user = User.builder()
                .name("testId")
                .build();

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

        List<Todo> saved = List.of(todo1, todo2);

        when(todoRepository.findByUser_NameAndTodoDate(user.getName(), Date.valueOf("2024-10-16")))
                .thenReturn(Optional.of(saved));

        // When
        List<TodoDTO> list = todoService.findTodoById(user.getName(),Date.valueOf("2024-10-16"));

        // Then
        assertThat(list).hasSize(2);
        assertThat(list.get(0).getUserName()).isEqualTo(user.getName());
        assertThat(list.get(0).getTodoDate()).isEqualTo(Date.valueOf("2024-10-16"));

    }

    @Test
    public void 존재하지_않는_회원ID는_예외를_던진다() {
        // Given
        when(todoRepository.findByUser_NameAndTodoDate("없는 아이디", Date.valueOf("2024-10-16")))
                .thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> todoService.findTodoById("없는 아이디",Date.valueOf("2024-10-16")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 사용자입니다.");
    }

    //FIXME Dto -> Entity 의 책임까지 save 함수가 갖고 있는게 맞나??
    @Test
    public void 회원ID로_Todo_저장() {
        // Given
        String userName = "testId";

        User user = User.builder()
                .name(userName)
                .build();

        TodoDTO todoDTO = TodoDTO.builder()
                .todoDate(Date.valueOf("2024-10-16"))
                .content("테스트 저장확인")
                .build();

        when(userRepository.findByName(userName)).thenReturn(user);

        // When
        todoService.saveTodo(todoDTO,userName);

        ArgumentCaptor<Todo> captor = ArgumentCaptor.forClass(Todo.class);
        verify(todoRepository).save(captor.capture());
        Todo savedTodo = captor.getValue();

        // Then
        assertThat(savedTodo.getContent()).isEqualTo(todoDTO.getContent());
        assertThat(savedTodo.getUser().getName()).isEqualTo(user.getName());

    }

//    @Test
//    @DisplayName("Todo 수정")
//    public void updateTodo() {
//        // Given
//        User user = User.builder()
//                .name("testId")
//                .build();
//
//        Todo todo1 = Todo.builder()
//                .user(user)
//                .todoDate(Date.valueOf("2024-10-16"))
//                .status("false")
//                .content("테스트 수정전")
//                .build();
//
//        todoRepository.save(todo1);
//
//        // When
//        TodoDTO todoDTO = TodoDTO.builder()
//                .todoId(todo1.getId())
//                .userName(user.getName())
//                .todoDate(Date.valueOf("2024-10-16"))
//                .status("true")
//                .content("테스트 수정완료")
//                .build();
//
//        todoService.updateTodo(todoDTO, user.getName());
//
//        // Then
//        Optional<Todo> updatedTodo = todoRepository.findById(todo1.getId());
//        Todo todo = updatedTodo.orElseThrow(() -> new AssertionError("Todo not found"));
//        assertThat(todo.getStatus()).isEqualTo("true");
//
//    }
//
//    @Test
//    @DisplayName("Todo 삭제")
//    public void deleteTodo() {
//        // Given
//        User user = User.builder()
//                .name("testId")
//                .build();
//
//        Todo todo = Todo.builder()
//                .user(user)
//                .todoDate(Date.valueOf("2024-10-16"))
//                .status("false")
//                .content("테스트 삭제전")
//                .build();
//
//        Todo savedTodo = todoRepository.save(todo);
//
//        // When
//        TodoDTO todoDTO = TodoDTO.builder()
//                    .todoId(savedTodo.getId())
//                    .userName(savedTodo.getUser().getName())
//                    .build();
//
//        todoService.deleteTodo(todoDTO, user.getName());
//
//        // Then
//        List<Todo> todoList = todoRepository.findByUser_NameAndTodoDate(user.getName(),Date.valueOf("2024-10-16"));
//        assertThat(todoList).isEmpty();
//    }
//
//    @Test
//    @DisplayName("Todo 상태 변경")
//    public void updateStatus () {
//        // Given
//        User user = User.builder()
//                .name("testId")
//                .build();
//
//        Todo todo = Todo.builder()
//                .user(user)
//                .todoDate(Date.valueOf("2024-10-16"))
//                .status("false")
//                .content("테스트 Todo 수행전")
//                .build();
//
//        Todo savedTodo = todoRepository.save(todo);
//
//        // When
//
//        TodoDTO todoDTO = TodoDTO.builder()
//                    .todoId(savedTodo.getId())
//                    .status("true")
//                    .build();
//
//        todoService.updateStatus(todoDTO, user.getName());
//
//        // Then
//        Optional<Todo> updatedTodoOptional = todoRepository.findById(savedTodo.getId());
//        Todo updatedTodo = updatedTodoOptional.orElseThrow(() -> new AssertionError("Todo not found"));
//        assertThat(updatedTodo.getStatus()).isEqualTo("true");
//    }

}