package com.cuki.service;

import com.cuki.dto.UserRequestDto;
import com.cuki.dto.UserResponseDto;
import com.cuki.entity.User;
import com.cuki.repository.UserRepository;
import com.cuki.util.SecurityUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponseDto signup(UserRequestDto userRequestDto) {
        if (userRepository.existsByEmail(userRequestDto.getEmail())) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
        }

        User user = userRequestDto.toUser(passwordEncoder);
        return UserResponseDto.of(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public UserResponseDto getMyInfo() {
        return userRepository.findById(SecurityUtil.getCurrentUserId())
                .map(UserResponseDto::of)
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다."));

    }

    @Transactional(readOnly = true)
    public UserResponseDto getUserInfo(String email) {
        return userRepository.findByEmail(email)
                .map(UserResponseDto::of)
                .orElseThrow(() -> new RuntimeException("유저 정보가 없습니다."));
    }
}
