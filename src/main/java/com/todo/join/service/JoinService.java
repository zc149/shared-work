package com.todo.join.service;

import com.todo.join.dto.JoinDTO;
import com.todo.join.entity.User;
import com.todo.join.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class JoinService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void joinProcess(JoinDTO joinDTO) {
        String name = joinDTO.getName();
        String password = joinDTO.getPassword();
        String email = joinDTO.getEmail();
        String phone = joinDTO.getPhone();


        Boolean isExist = userRepository.existsByName(name);

        if (isExist) {
            log.info("중복된 회원 ID");
            // 중복된 사용자니까 로직 추가 필요
            return;
        }
        if (!joinDTO.getPassword().equals(joinDTO.getCheckPassword())){
            return;
        }
        User user = User.builder()
                .name(name)
                .password(bCryptPasswordEncoder.encode(password))
                .realName(joinDTO.getRealName())
                .nickName(joinDTO.getNickName())
                .email(email)
                .phone(phone)
                .createdDate(Date.valueOf(LocalDate.now()))
                .modifiedDate(Date.valueOf(LocalDate.now()))
                .role("ROLE_USER").build();

        userRepository.save(user);
    }


}
