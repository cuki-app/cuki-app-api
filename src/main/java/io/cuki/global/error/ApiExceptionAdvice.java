package io.cuki.global.error;

import io.cuki.domain.comment.exception.CommentNotFoundException;
import io.cuki.domain.comment.exception.CommentNotValidException;
import io.cuki.domain.member.exception.*;
import io.cuki.domain.participation.exception.*;
import io.cuki.domain.schedule.exception.*;
import io.cuki.global.common.response.ErrorResponse;
import io.cuki.infra.email.exception.IncorrectVerificationCodeException;
import io.cuki.infra.email.exception.SendMailFailedException;
import io.cuki.infra.email.exception.VerificationCodeExpiredException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ApiExceptionAdvice {

    @ExceptionHandler({MemberNotFoundException.class, CommentNotFoundException.class,
                        ScheduleNotFoundException.class, ParticipationNotFoundException.class})
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

    @ExceptionHandler({MemberNotMatchException.class, WriterAuthorityException.class,
            PermissionIsAlreadyDecidedException.class})
    public ResponseEntity<ErrorResponse<String>> forbiddenException(RuntimeException e) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ErrorResponse.forbidden(e.getMessage()));
    }

    @ExceptionHandler({IllegalArgumentException.class, MemberAlreadyExistException.class,
            RefreshTokenNotValidException.class, AuthorityNotFoundInJwtException.class,
            MemberAlreadyLoggedOutException.class, RefreshTokenNotMatchException.class,
            IncorrectVerificationCodeException.class, VerificationCodeExpiredException.class,
            DeactivatedMemberException.class, UsernameNotFoundException.class,
            SendMailFailedException.class, RefeshTokenNotFoundException.class,
            NicknameNotValidException.class, EmailAddressNotValidException.class,
            InvalidDetailsException.class, InvalidTitleException.class,
            InvalidFixedNumberException.class, InvalidLocationException.class,
            InvalidPeriodException.class, NicknameAlreadyExistException.class,
            CommentNotValidException.class
    })
    public ResponseEntity<ErrorResponse<String>> badRequestException(RuntimeException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.badRequest(e.getMessage()));
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse<String>> parseException(RuntimeException e) {
        log.error(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.badRequest("시작일과 종료일의 날짜 포맷을 지켜서 작성해주십시오."));
    }


    @ExceptionHandler(IllegalAccessException.class)
    public ResponseEntity<ErrorResponse<String>> illegalAccessException(Exception e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.badRequest(e.getMessage()));
    }

    @ExceptionHandler({ScheduleStatusIsAlreadyChangedException.class, InappropriateAccessToParticipationException.class,
            DuplicateParticipationException.class, FixedNumberOutOfBoundsException.class
    })
    public ResponseEntity<ErrorResponse<String>> conflictException(RuntimeException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ErrorResponse.conflict(e.getMessage()));
    }

}
