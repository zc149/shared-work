package com.todo.todoList.entity;

import com.todo.join.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.sql.Date;

@Entity
@DynamicUpdate
@Setter
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column( name = "TODO_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @Column(name = "TODO_DATE")
    private Date todoDate;

    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "TODO_CONTENT")
    private String content;


}
