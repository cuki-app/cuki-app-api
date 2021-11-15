package io.cuki.global.error;

import io.cuki.domain.comment.exception.CommentNotFoundException;
import io.cuki.domain.member.exception.*;
import io.cuki.domain.schedule.exception.ParticipationNotFoundException;
import io.cuki.domain.schedule.exception.ScheduleStatusIsAlreadyChangedException;
import io.cuki.global.common.response.ErrorResponse;
import io.cuki.domain.schedule.exception.ScheduleNotFoundException;
import io.cuki.infra.email.exception.IncorrectVerificationCodeException;
import io.cuki.infra.email.exception.SendMailFailedException;
import io.cuki.infra.email.exception.VerificationCodeExpiredException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionAdvice {

    @ExceptionHandler({MemberNotFoundException.class, ScheduleNotFoundException.class,
            CommentNotFoundException.class, ParticipationNotFoundException.class})
    public ResponseEntity<ErrorResponse<String>> notFoundException(RuntimeException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.notFound(e.getMessage()));
    }

    @ExceptionHandler(AuthenticationNotFoundException.class)
    public ResponseEntity<ErrorResponse<String>> authenticationException(RuntimeException e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponse.unauthorized(e.getMessage()));
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
            IncorrectVerificationCodeException.class, VerificationCodeExpiredException.class,
            DeactivatedMemberException.class, UsernameNotFoundException.class,
            SendMailFailedException.class, NoSuchRefeshTokenException.class
    })
    public ResponseEntity<ErrorResponse<String>> illegalArgumentException(RuntimeException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.badRequest(e.getMessage()));
    }

    @ExceptionHandler(IllegalAccessException.class)
    public ResponseEntity<ErrorResponse<String>> illegalAccessException(Exception e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.badRequest(e.getMessage()));
    }

    @ExceptionHandler(ScheduleStatusIsAlreadyChangedException.class)
    public ResponseEntity<ErrorResponse<String>> conflictException(Exception e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ErrorResponse.conflict(e.getMessage()));
    }
}
