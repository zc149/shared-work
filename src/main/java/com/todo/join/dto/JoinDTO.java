package com.todo.join.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class JoinDTO {

    private String name;
    private String password;
    private String checkPassword;
    private String realName;
    private String nickName;
    private String phone;
    private String email;


}
