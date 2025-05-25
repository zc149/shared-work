package com.todo.todoList.repository;

import com.todo.todoList.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface TodoRepository extends JpaRepository<Todo,Long> {
    List<Todo> findByUser_NameAndTodoDate(String userName, Date date);

    @Modifying
    @Query("DELETE FROM Todo t WHERE t.id = :todoId AND t.user.name = :name")
    int deleteByIdAndUserName(@Param("todoId") Long todoId, @Param("name") String name);

}
