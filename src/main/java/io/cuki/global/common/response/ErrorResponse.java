package io.cuki.global.common.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;

@Getter
public class ErrorResponse {
    private final int statusCode;
    private final String message;
    private final LocalDateTime timeStamp;

    private ErrorResponse(int statusCode, String message, LocalDateTime timeStamp) {
        this.statusCode = statusCode;
        this.message = message;
        this.timeStamp = timeStamp;
    }

    private static ErrorResponse of(int statusCode, String message, LocalDateTime timeStamp) {
        return new ErrorResponse(statusCode, message, timeStamp);
    }


    public static ErrorResponse notFound(String message) {
        return of(
                HttpStatus.NOT_FOUND.value(),
                message,
                LocalDateTime.now()
        );
    }

    public static ErrorResponse forbidden(String message) {
        return of(
                HttpStatus.FORBIDDEN.value(),
                message,
                LocalDateTime.now()
        );
    }

    public static ErrorResponse conflict(String message) {
        return of(
                HttpStatus.CONFLICT.value(),
                message,
                LocalDateTime.now()
        );
    }
}
