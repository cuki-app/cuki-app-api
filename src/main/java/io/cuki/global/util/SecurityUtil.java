package io.cuki.global.util;

import io.cuki.domain.member.exception.AuthenticationNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class SecurityUtil {

    private SecurityUtil() {}

    public static Long getCurrentMemberId() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            log.error("Security Context에 인증 정보가 없습니다.");
            throw new AuthenticationNotFoundException("Security Context에 인증 정보가 없습니다.");
        }

        log.debug("Security Context에서 조회한 회원의 번호 : {}", authentication.getName());

        return Long.parseLong(authentication.getName());
    }
}
