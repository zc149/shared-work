//package com.todo.join.service;
//
//import com.todo.join.dto.CustomUserDetails;
//import com.todo.join.dto.UserDTO;
//import com.todo.join.entity.User;
//import com.todo.join.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//
//@Service
//@RequiredArgsConstructor
//public class LoginService implements UserDetailsService {
//
//    private final UserRepository userRepository;
//
//    public UserDTO findByName (String username) {
//        Optional<User> userOptional = Optional.ofNullable(userRepository.findByName(username));
//        UserDTO userDTO = null;
//        if (userOptional.isPresent()) {
//
//            User user = userOptional.get();
//
//            userDTO = UserDTO.builder()
//                    .id(user.getId())
//                    .name(user.getName())
//                    .nickName(user.getNickName())
//                    .phone(user.getPhone())
//                    .email(user.getEmail())
//                    .build();
//
//        }
//        return userDTO;
//    };
//
//
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//
//        User userData = userRepository.findByName(username);
//
//        if (userData != null) {
//            return new CustomUserDetails(userData);
//        }
//
//        return null;
//    }
//
//}
