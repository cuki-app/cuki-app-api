package io.cuki.domain.comment.controller;

import io.cuki.domain.comment.dto.CommentResponseDto;
import io.cuki.domain.comment.entity.CommentAuthority;
import io.cuki.global.common.response.ApiResponse;
import io.cuki.domain.comment.dto.RegisterCommentRequestDto;
import io.cuki.domain.comment.dto.SuccessfullyDeletedCommentResponseDto;
import io.cuki.domain.comment.dto.SuccessfullyRegisteredCommentResponseDto;
import io.cuki.domain.comment.service.CommentService;
import io.cuki.global.util.SecurityUtil;
import io.cuki.global.util.SliceCustom;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = {"댓글 API"})
@RestController
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @ApiOperation(value = "댓글등록")
    @PostMapping("/schedules/{scheduleId}/comments")
    public ApiResponse<SuccessfullyRegisteredCommentResponseDto> registerComment(@RequestBody RegisterCommentRequestDto registerCommentRequestDto, @PathVariable Long scheduleId) {
        return ApiResponse.ok(commentService.registerComment(registerCommentRequestDto, scheduleId));
    }

    @ApiOperation(value = "댓글 조회 - 게시물 기준")
    @GetMapping("/schedules/{scheduleId}/comments")
    public ApiResponse<SliceCustom<CommentResponseDto>> getComments(@PathVariable Long scheduleId, @RequestParam int page, @RequestParam(defaultValue = "30") int size) {
        final Long memberId = SecurityUtil.getCurrentMemberId();
        return ApiResponse.ok(commentService.getComments(scheduleId, memberId, page, size));
    }

    @ApiOperation(value = "댓글 삭제")
    @DeleteMapping("/schedules/{scheduleId}/comments/{commentId}")
    public ApiResponse<SuccessfullyDeletedCommentResponseDto> deleteComment(@PathVariable Long scheduleId, @PathVariable Long commentId) {
        return ApiResponse.ok(commentService.deleteComment(scheduleId, commentId));
    }
}
