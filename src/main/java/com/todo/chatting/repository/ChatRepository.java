package com.todo.chatting.repository;

import com.todo.chatting.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

@Repository
public interface ChatRepository extends JpaRepository<Message,Long> {

    Message findTopByOrderByIdDesc();

}
