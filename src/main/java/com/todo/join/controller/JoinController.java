package com.todo.join.controller;

import com.todo.join.dto.JoinDTO;
import com.todo.join.service.JoinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class JoinController {

    private final JoinService joinService;

    @PostMapping("/join")
    public String joinProcess(@RequestBody JoinDTO joinDTO) {
      log.info(joinDTO.getName());
      joinService.joinProcess(joinDTO);
      return "ok"; // 회원가입 성공 관련 로직 추가해야함 임시로 ok 뿌림
    }
}
