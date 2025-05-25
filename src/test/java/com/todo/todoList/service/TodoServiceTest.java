package com.todo.todoList.service;

import com.todo.join.entity.User;
import com.todo.join.repository.UserRepository;
import com.todo.todoList.dto.TodoDTO;
import com.todo.todoList.entity.Todo;
import com.todo.todoList.repository.TodoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.AccessDeniedException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

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
                .thenReturn(saved);

        // When
        List<TodoDTO> list = todoService.findTodoById(user.getName(), Date.valueOf("2024-10-16"));

        // Then
        assertThat(list).hasSize(2);
        assertThat(list.get(0).getUserName()).isEqualTo(user.getName());
        assertThat(list.get(0).getTodoDate()).isEqualTo(Date.valueOf("2024-10-16"));

    }

    @Test
    public void 존재하지_않는_회원ID는_예외를_던진다() {
        // Given
        when(todoRepository.findByUser_NameAndTodoDate("없는 아이디", Date.valueOf("2024-10-16")))
                .thenReturn(new ArrayList<Todo>());

        // When
        // Then
        assertThatThrownBy(() -> todoService.findTodoById("없는 아이디", Date.valueOf("2024-10-16")))
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
        todoService.saveTodo(todoDTO, userName);

        ArgumentCaptor<Todo> captor = ArgumentCaptor.forClass(Todo.class);
        verify(todoRepository).save(captor.capture());
        Todo savedTodo = captor.getValue();

        // Then
        assertThat(savedTodo.getContent()).isEqualTo(todoDTO.getContent());
        assertThat(savedTodo.getUser().getName()).isEqualTo(user.getName());

    }

    @Test
    public void 회원ID로_Todo_수정() throws AccessDeniedException {
        // Given
        User user = User.builder()
                .name("testId")
                .build();

        Todo todo = Todo.builder()
                .id(1L)
                .user(user)
                .todoDate(Date.valueOf("2024-10-16"))
                .status("false")
                .content("테스트 수정전")
                .build();

        when(todoRepository.findById(1L)).thenReturn(Optional.ofNullable(todo));

        // When
        TodoDTO todoDTO = TodoDTO.builder()
                .todoId(todo.getId())
                .userName(user.getName())
                .todoDate(Date.valueOf("2024-10-16"))
                .status("true")
                .content("테스트 수정완료")
                .build();

        todoService.updateTodo(todoDTO, user.getName());


        // Then
        assertEquals("테스트 수정완료", todo.getContent());
        assertEquals("true", todo.getStatus());
    }

    @Test
    public void 회원ID로_Todo_수정시_Todo를_찾을_수_없음() throws AccessDeniedException {
        // Given
        User user = User.builder()
                .name("testId")
                .build();

        when(todoRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        // Then
        TodoDTO todoDTO = TodoDTO.builder()
                .todoId(1L)
                .userName(user.getName())
                .todoDate(Date.valueOf("2024-10-16"))
                .status("true")
                .content("테스트 수정완료")
                .build();

        assertThatThrownBy(() -> todoService.updateTodo(todoDTO, user.getName()))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Todo를 찾을 수 없습니다.");
    }

    @Test
    public void 회원ID로_Todo_수정_권한이_없음() throws AccessDeniedException {
        // Given
        User user = User.builder()
                .name("testId")
                .build();

        Todo todo = Todo.builder()
                .id(1L)
                .user(User.builder().name("invalid User").build())
                .todoDate(Date.valueOf("2024-10-16"))
                .status("false")
                .content("테스트 수정전")
                .build();

        when(todoRepository.findById(1L)).thenReturn(Optional.ofNullable(todo));

        // When
        // Then
        TodoDTO todoDTO = TodoDTO.builder()
                .todoId(todo.getId())
                .userName("invalid User")
                .todoDate(Date.valueOf("2024-10-16"))
                .status("true")
                .content("테스트 수정완료")
                .build();

        assertThatThrownBy(() -> todoService.updateTodo(todoDTO, user.getName()))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage("해당 유저는 수정 권한이 없습니다.");
    }

    @Test
    public void 회원ID로_Todo_삭제_할_수_있음() {
        // Given
        TodoDTO todo = TodoDTO.builder().todoId(1L).build();
        String name = "삭제";

        when(todoRepository.deleteByIdAndUserName(todo.getTodoId(), name)).thenReturn(1);

        // When
        // Then
        assertDoesNotThrow(() -> todoService.deleteTodo(todo, name));
    }

    @Test
    public void 삭제할_Todo가_없으면_예외를_던진다() {
        // Given
        TodoDTO todo = TodoDTO.builder().todoId(1L).build();
        String name = "삭제예외";

        when(todoRepository.deleteByIdAndUserName(todo.getTodoId(), name)).thenReturn(0);

        // When
        // Then
        assertThatThrownBy(() -> todoService.deleteTodo(todo, name))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("삭제할 Todo가 존재하지 않습니다.");

    }

    @Test
    void Todo_상태_변경_할_수_있음() throws AccessDeniedException {
        // Given
        User user = User.builder().name("testId").build();

        Todo todo = mock(Todo.class);
        when(todo.getUser()).thenReturn(user);

        TodoDTO dto = TodoDTO.builder()
                .todoId(1L)
                .status("true")
                .build();

        when(todoRepository.findById(anyLong())).thenReturn(Optional.of(todo));

        // When
        todoService.updateStatus(dto, "testId");

        // Then
        verify(todo).setStatus("true");
    }

    @Test
    void Todo_없으면_상태_변경_실패() {
        // Given
        TodoDTO dto = TodoDTO.builder()
                .todoId(1L)
                .status("true")
                .build();

        when(todoRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> todoService.updateStatus(dto, "test"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 Todo가 존재하지 않습니다.");
    }

    @Test
    void 다른_사용자의_Todo는_상태_변경_불가() {
        // Given
        User user = User.builder().name("otherUser").build();
        Todo todo = mock(Todo.class);
        when(todo.getUser()).thenReturn(user);

        TodoDTO dto = TodoDTO.builder()
                .todoId(1L)
                .status("true")
                .build();

        when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));

        // When & Then
        assertThatThrownBy(() -> todoService.updateStatus(dto, "test"))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage("수정 권한이 없습니다.");
    }

}