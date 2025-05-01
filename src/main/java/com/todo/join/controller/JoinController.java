//package com.todo.join.controller;
//
//import ch.qos.logback.core.model.Model;
//import com.todo.join.dto.JoinDTO;
//import com.todo.join.service.JoinService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//
//@Slf4j
//@RequiredArgsConstructor
//@Controller
//public class JoinController {
//
//    private final JoinService joinService;
//
//    @GetMapping("/join")
//    public String joinForm() {
//        return "join";
//    }
//
//    @PostMapping("/join")
//    public String joinProcess(@ModelAttribute JoinDTO joinDTO) {
//        log.info(joinDTO.getName());
//        joinService.joinProcess(joinDTO);
//        return "redirect:/login";
//    }
//}
