//package com.todo.join.service;
//
//import com.todo.join.dto.JoinDTO;
//import com.todo.join.entity.User;
//import com.todo.join.repository.UserRepository;
//import jakarta.transaction.Transactional;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest
//@Transactional
//class JoinServiceTest {
//
//    @Autowired
//    private JoinService joinService;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private BCryptPasswordEncoder bCryptPasswordEncoder;
//
//    @Test
//    @DisplayName("회원가입")
//    public void joinProcess() {
//        // Given
//        String name = "testId";
//        String password = "password";
//        String email = "test@naver.com";
//
//        JoinDTO joinDTO = JoinDTO.builder()
//                .name(name)
//                .password(password)
//                .checkPassword(password)
//                .email(email)
//                .build();
//
//        // When
//        joinService.joinProcess(joinDTO);
//
//        // Then
//        User savedUser = userRepository.findByName(name);
//        assertThat(savedUser).isNotNull();
//    }
//
//    @Test
//    @DisplayName("중복된 ID 검증")
//    public void duplicateId() {
//        // Given
//        String name = "testId";
//        String password = "password";
//        String email = "test@naver.com";
//
//        User existingUser = User.builder()
//                .name(name)
//                .password(bCryptPasswordEncoder.encode(password))
//                .email(email)
//                .build();
//
//        userRepository.save(existingUser);
//
//        Long userCount = userRepository.count();
//
//        JoinDTO joinDTO = JoinDTO.builder()
//                .name(name) // 중복된 ID
//                .password(password)
//                .checkPassword(password)
//                .email(email)
//                .build();
//
//        // When
//        joinService.joinProcess(joinDTO);
//
//        // Then
//        User savedUser = userRepository.findByName(name);
//        assertThat(savedUser).isNotNull();
//        assertThat(userRepository.count()).isEqualTo(userCount);
//    }
//}