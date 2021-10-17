package com.cuki.domain.schedule.controller;

import com.cuki.domain.schedule.utils.MemberNotFoundException;
import com.cuki.domain.schedule.utils.MemberNotMatchException;
import com.cuki.domain.schedule.utils.ScheduleNotFoundException;
import com.cuki.global.common.response.ApiResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionAdvice {

    @ExceptionHandler({MemberNotFoundException.class, ScheduleNotFoundException.class})
    public ApiResponse<String> notFoundException(RuntimeException e) {
        return ApiResponse.notFound(e.getMessage());
    }

    @ExceptionHandler(MemberNotMatchException.class)
    public ApiResponse<String> memberNotMatchException(RuntimeException e) {
        return ApiResponse.forbidden(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ApiResponse<String> illegalArgumentException(RuntimeException e) {
        return ApiResponse.conflict(e.getMessage());
    }
}
