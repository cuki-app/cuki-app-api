package io.cuki.global.common.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public final class ApiResponse<T> {
    private final int statusCode;
    private final String message;
    private final T result;
    private final LocalDateTime timestamp;

    private ApiResponse(int statusCode, String message, T result, LocalDateTime timestamp) {
        this.statusCode = statusCode;
        this.message = message;
        this.result = result;
        this.timestamp = timestamp;
    }

    private static <T> ApiResponse<T> of(int statusCode, String message, T result, LocalDateTime timestamp) {
        return new ApiResponse<>(statusCode, message, result, timestamp);
    }

    public static <T> ApiResponse<T> ok(T result) {
        return of(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                result,
                LocalDateTime.now()
        );
    }
}
