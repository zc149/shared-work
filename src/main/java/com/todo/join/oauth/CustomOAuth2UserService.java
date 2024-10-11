package com.todo.join.oauth;

import com.todo.join.dto.UserDTO;
import com.todo.join.entity.User;
import com.todo.join.oauth.dto.NaverResponse;
import com.todo.join.oauth.dto.OAuth2Response;
import com.todo.join.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;


@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println(oAuth2User);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;

        if (registrationId.equals("naver")) {
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        } else {
            return null;
        }

        String username = oAuth2Response.getProviderId() + " " + oAuth2Response.getProviderId();
        User existData = userRepository.findByName(username);

        if (existData == null) {

            User user = User.builder()
                    .name(username)
                    .nickName(oAuth2Response.getNickName())
                    .email(oAuth2Response.getEmail())
                    .phone(oAuth2Response.getMobile())
                    .createdDate(Date.valueOf(LocalDate.now()))
                    .modifiedDate(Date.valueOf(LocalDate.now()))
                    .role("ROLE_USER")
                    .build();

            userRepository.save(user);

            UserDTO userDTO = UserDTO.builder()
                    .name(username)
                    .nickName(oAuth2Response.getNickName())
                    .phone(oAuth2Response.getMobile())
                    .email(oAuth2Response.getEmail())
                    .role("ROLE_USER")
                    .build();

            return new CustomOAuth2User(userDTO);
        } else {
            existData.setNickName(oAuth2Response.getNickName());
            existData.setEmail(oAuth2Response.getEmail());
            existData.setPhone(oAuth2Response.getMobile());
            existData.setModifiedDate(Date.valueOf(LocalDate.now()));

            userRepository.save(existData);

            UserDTO userDTO = UserDTO.builder()
                    .name(username)
                    .nickName(oAuth2Response.getNickName())
                    .phone(oAuth2Response.getMobile())
                    .email(oAuth2Response.getEmail())
                    .role("ROLE_USER")
                    .build();

            return new CustomOAuth2User(userDTO);
        }
    }
}
