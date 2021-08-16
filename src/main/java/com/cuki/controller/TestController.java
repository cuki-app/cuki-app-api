package com.cuki.controller;

import com.cuki.dto.AuthResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @PostMapping("/users/sign-up")
    public ResponseEntity<String> signup() {
        return ResponseEntity.ok("로그인 성공. 아직 구현 전 입니다.");
    }

    @PostMapping("/auth")
    public AuthResponseDto login() {
        return new AuthResponseDto(
                "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIyIiwiYXV0aCI6IlJPTEVfVVNFUiIsImV4cCI6MTYxNTExNDI4MH0.43LvabP41Awhicy6YYAYHtDPnxNYpEygtE-DjLaDjNpAxZf01Nx4xE_dGk0V4jBpjwCgKVGKZIMyEeIppwzARQ",
                "eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE2MTU3MTcyODB9.DKqk-EZVT0TJAvvHpSN8nClIHKq-k4KYMHpx-Ltf7V8OB6Og4D_dsYnr3Z4Rw7iR7ckv-ZWMyi5SkheESw-T0g"
        );
    }

    @GetMapping("/auth/reissue")
    public AuthResponseDto reissue() {
        return new AuthResponseDto(
                "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIyIiwiYXV0aCI6IlJPTEVfVVNFUiIsImV4cCI6MTYxNTExNDM2NX0.5VXa6Cht_DPEEGe7-BrElvsrs7qRXmVnkDdi4Lm3PxZ0vAgqFdirhe5RlE1D-Wc1zaUepBmGhhw-u-oP_-rbKQ",
                "eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE2MTU3MTczNjV9.tZytWyCWkWIYitvT3pa8FSnxilBDMtSevUzKRFK21TGLITf2eLXEwNNS_Q7rylD9uUe3Rx9ZR2NVqE_ZNWxTqg"
        );
    }
}
