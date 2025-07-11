package com.todo.join.controller;

import com.todo.join.dto.UserDTO;
//import com.todo.join.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequiredArgsConstructor
public class LoginController {

//    private  final LoginService loginService;
//
//    @GetMapping("/login/form")
//    public String loginPage() {
//        return "login";
//    }

    @GetMapping("/home")
//    public String homePage(@RequestParam String username, Model model) {
    public String homePage(Model model) {

//        UserDTO userDTO = loginService.findByName(username);
        UserDTO userDTO = UserDTO.builder()
                .id(100L)
                .name("temp")
                .nickName("temp")
                .phone("temp")
                .email("temp")
                .role("ROLE_ADMIN")
                .build();

        model.addAttribute("userDTO", userDTO);
        return "home";
    }

}
