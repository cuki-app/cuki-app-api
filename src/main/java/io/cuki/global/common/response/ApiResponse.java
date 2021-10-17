package io.cuki.global.common.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public final class ApiResponse<T> {
    private final int statusCode;
    private final T result;
    private final LocalDateTime timestamp;

    private ApiResponse(int statusCode, T result, LocalDateTime timestamp) {
        this.statusCode = statusCode;
        this.result = result;
        this.timestamp = timestamp;
    }

    private static <T> ApiResponse<T> of(int statusCode, T result, LocalDateTime timestamp) {
        return new ApiResponse<>(statusCode, result, timestamp);
    }

    public static <T> ApiResponse<T> ok(T result) {
        return of(
                HttpStatus.OK.value(),
                result,
                LocalDateTime.now()
        );
    }

    public static <T> ApiResponse<T> notFound(T result) {
        return of(
                HttpStatus.NOT_FOUND.value(),
                result,
                LocalDateTime.now()
        );
    }

    public static <T> ApiResponse<T> forbidden(T result) {
        return of(
                HttpStatus.FORBIDDEN.value(),
                result,
                LocalDateTime.now()
        );
    }

    public static <T> ApiResponse<T> conflict(T result) {
        return of(
                HttpStatus.CONFLICT.value(),
                result,
                LocalDateTime.now()
        );
    }

}
