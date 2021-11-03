package io.cuki.global.common.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;


@Getter
public class ErrorResponse<T> extends ApiResponse<T> {

    private final T message;

    private ErrorResponse(int statusCode, LocalDateTime timestamp, T message) {
        super(statusCode, timestamp);
        this.message = message;
    }

    private static <T> ErrorResponse<T> of(int statusCode, LocalDateTime timestamp, T message) {
        return new ErrorResponse<>(statusCode, timestamp, message);
    }

    public static <T> ErrorResponse <T> notFound(T message) {
        return of(
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now(),
                message
                );
    }

    public static <T> ErrorResponse<T> forbidden(T message) {
        return of(
                HttpStatus.FORBIDDEN.value(),
                LocalDateTime.now(),
                message
        );
    }

    public static <T> ErrorResponse<T> conflict(T message) {
        return of(
                HttpStatus.CONFLICT.value(),
                LocalDateTime.now(),
                message
        );
    }

    public static <T> ErrorResponse<T> badRequest(T message) {
        return of(
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),
                message
        );
    }

    public static <T> ErrorResponse<T> unauthorized(T message) {
        return of(
                HttpStatus.UNAUTHORIZED.value(),
                LocalDateTime.now(),
                message
        );
    }
}
