package com.todo.join.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserDTO {
    private Long id;
    private String name;
    private String nickName;
    private String phone;
    private String email;
}
