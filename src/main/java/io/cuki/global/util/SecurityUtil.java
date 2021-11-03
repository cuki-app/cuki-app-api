package io.cuki.global.util;

import io.cuki.domain.member.exception.AuthenticationNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.NoSuchElementException;

@Slf4j
public class SecurityUtil {

    private SecurityUtil() {}

    public static Long getCurrentMemberId() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            throw new AuthenticationNotFoundException("Security Context에 인증 정보가 없습니다.");
        }

        log.debug("authentication.getName() -> {}" + authentication.getName());

        return Long.parseLong(authentication.getName());
    }
}
