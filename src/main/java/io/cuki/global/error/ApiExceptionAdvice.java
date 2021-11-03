package io.cuki.global.error;

import io.cuki.domain.member.exception.*;
import io.cuki.global.common.response.ErrorResponse;
import io.cuki.domain.schedule.exception.ScheduleNotFoundException;
import io.cuki.infra.email.exception.IncorrectVerificationCodeException;
import io.cuki.infra.email.exception.VerificationCodeExpiredException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionAdvice {

    @ExceptionHandler({MemberNotFoundException.class, ScheduleNotFoundException.class})
    public ResponseEntity<ErrorResponse<MessageResponseDto>> notFoundException(RuntimeException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.notFound(new MessageResponseDto(e.getMessage())));
    }

    @ExceptionHandler(MemberNotMatchException.class)
    public ResponseEntity<ErrorResponse<String>> memberNotMatchException(RuntimeException e) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ErrorResponse.forbidden(e.getMessage()));
    }

    @ExceptionHandler({IllegalArgumentException.class, MemberAlreadyExistException.class,
            RefreshTokenNotValidException.class, AuthorityNotFoundInJwtException.class,
            MemberAlreadyLoggedOutException.class, RefreshTokenNotMatchException.class,
            IncorrectVerificationCodeException.class, VerificationCodeExpiredException.class
    })
    public ResponseEntity<ErrorResponse<String>> illegalArgumentException(RuntimeException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.badRequest(e.getMessage()));
    }
}
