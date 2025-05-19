package com.todo.todoList.repository;

import com.todo.todoList.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface TodoRepository extends JpaRepository<Todo,Long> {
    Optional<List<Todo>> findByUser_NameAndTodoDate(String userName, Date date);
}
