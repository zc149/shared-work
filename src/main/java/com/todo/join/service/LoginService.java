package com.todo.join.service;

import com.todo.join.dto.CustomUserDetails;
import com.todo.join.entity.User;
import com.todo.join.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User userData = userRepository.findByName(username);

        if (userData != null) {
            return new CustomUserDetails(userData);
        }

        return null;
    }

}
