package com.todo.join.controller;

import com.todo.join.dto.EmailCheckDTO;
import com.todo.join.dto.EmailRequestDTO;
import com.todo.join.service.MailSendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/mail")
public class MailController {

    private final MailSendService mailSendService;

    @PostMapping("/send-certification")
    public String mailSend(@RequestBody EmailRequestDTO emailRequestDTO) {
      log.info("인증 요청 이메일 : " + emailRequestDTO.getEmail());
      return mailSendService.joinEmail(emailRequestDTO.getEmail());
    }

    @PostMapping("/authCheck")
    public String authCheck(@RequestBody EmailCheckDTO emailCheckDTO) {
        boolean checked = mailSendService.checkAuthNum(emailCheckDTO.getEmail(),emailCheckDTO.getAuthNum());

        if (checked) {
            log.info("이메일 인증 완료");
            return "ok";
        } else {
            log.info("이메일 인증 실패");
            return "fail";
        }
    }

}
