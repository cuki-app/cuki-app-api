package io.cuki.global.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;


@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class ApiResponse<T> {
    private final int statusCode;
    private T result;
    private final LocalDateTime timestamp;

    public ApiResponse(int statusCode, LocalDateTime timestamp) {
        this.statusCode = statusCode;
        this.timestamp = timestamp;
    }

    protected ApiResponse(int statusCode, T result, LocalDateTime timestamp) {
        this.statusCode = statusCode;
        this.result = result;
        this.timestamp = timestamp;
    }


    protected static <T> ApiResponse<T> of(int statusCode, T result, LocalDateTime timestamp) {
        return new ApiResponse<>(statusCode, result, timestamp);
    }

    public static <T> ApiResponse<T> ok(T result) {
        return of(
                HttpStatus.OK.value(),
                result,
                LocalDateTime.now()
        );
    }
}
