package io.cuki.global.error;

import io.cuki.global.common.response.ErrorResponse;
import io.cuki.global.error.exception.MemberNotFoundException;
import io.cuki.global.error.exception.MemberNotMatchException;
import io.cuki.domain.schedule.exception.ScheduleNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionAdvice {

    @ExceptionHandler({MemberNotFoundException.class, ScheduleNotFoundException.class})
    public ResponseEntity<ErrorResponse> notFoundException(RuntimeException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.notFound(e.getMessage()));
    }

    @ExceptionHandler(MemberNotMatchException.class)
    public ResponseEntity<ErrorResponse> memberNotMatchException(RuntimeException e) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ErrorResponse.forbidden(e.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> illegalArgumentException(RuntimeException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ErrorResponse.conflict(e.getMessage()));
    }
}
