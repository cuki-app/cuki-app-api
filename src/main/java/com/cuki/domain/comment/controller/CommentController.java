package com.cuki.domain.comment.controller;

import com.cuki.domain.comment.dto.CommentResponseDto;
import com.cuki.domain.comment.dto.RegisterCommentRequestDto;
import com.cuki.domain.comment.dto.SuccessfullyDeletedCommentResponseDto;
import com.cuki.domain.comment.dto.SuccessfullyRegisteredCommentResponseDto;
import com.cuki.domain.comment.service.CommentService;
import com.cuki.global.common.response.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = {"댓글 API"})
@RequestMapping("/comments")
@RestController
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @ApiOperation(value = "댓글등록")
    @PostMapping
    public ApiResponse<SuccessfullyRegisteredCommentResponseDto> registerComment(@RequestBody RegisterCommentRequestDto registerCommentRequestDto) {
        return ApiResponse.ok(commentService.registerComment(registerCommentRequestDto));
    }

    @ApiOperation(value = "댓글 조회 - 게시물 기준")
    @GetMapping("/{scheduleId}")
    public ApiResponse<List<CommentResponseDto>> getComments(@PathVariable Long scheduleId) {
        return ApiResponse.ok(commentService.getComments(scheduleId));
    }

    @ApiOperation(value = "댓글 삭제")
    @DeleteMapping("/{commentId}")
    public ApiResponse<SuccessfullyDeletedCommentResponseDto> deleteComment(@PathVariable Long commentId) {
        return ApiResponse.ok(commentService.deleteComment(commentId));
    }
}
