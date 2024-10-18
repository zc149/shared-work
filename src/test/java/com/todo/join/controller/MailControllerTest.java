package com.todo.join.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todo.join.dto.EmailCheckDTO;
import com.todo.join.dto.EmailRequestDTO;
import com.todo.join.service.MailSendService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = MailController.class)
class MailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MailSendService mailSendService;

    @Test
    @WithMockUser
    @DisplayName("인증 메일 전송")
    void mailSend() throws Exception{
        // Given
        EmailRequestDTO emailRequestDTO = new EmailRequestDTO();
        emailRequestDTO.setEmail("test@naver.com");

        // Mocking
        when(mailSendService.joinEmail(any(String.class))).thenReturn("123");

        // When & Then
        mockMvc.perform(post("/mail/send-certification")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emailRequestDTO))
                        .characterEncoding("utf-8")
                        .with(csrf()))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.auth").value("123"));

    }

    @Test
    @WithMockUser
    @DisplayName("인증번호 확인")
    void authCheck() throws Exception{
        // Given
        EmailCheckDTO emailCheckDTO = new EmailCheckDTO();
        emailCheckDTO.setEmail("test@naver.com");
        emailCheckDTO.setAuthNum("123");

        // Mocking
        when(mailSendService.checkAuthNum(any(String.class), any(String.class))).thenReturn(true);

        // When & Then
        mockMvc.perform(post("/mail/authCheck")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emailCheckDTO))
                        .characterEncoding("utf-8")
                        .with(csrf()))
                        .andExpect(status().isOk())
                        .andExpect(content().string("ok"));

    }

    @Test
    @WithMockUser
    @DisplayName("인증번호 확인 - 실패")
    void authCheck_Fail() throws Exception {
        // Given
        EmailCheckDTO emailCheckDTO = new EmailCheckDTO();
        emailCheckDTO.setEmail("test@naver.com");
        emailCheckDTO.setAuthNum("123");

        // Mocking
        when(mailSendService.checkAuthNum(any(String.class), any(String.class))).thenReturn(false);

        // When & Then
        mockMvc.perform(post("/mail/authCheck")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emailCheckDTO))
                        .characterEncoding("utf-8")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("fail"));
    }
}