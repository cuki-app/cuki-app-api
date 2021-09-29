package com.cuki.domain.comment.service;

import com.cuki.domain.comment.dto.CommentResponseDto;
import com.cuki.domain.comment.dto.RegisterCommentRequestDto;
import com.cuki.domain.comment.dto.SuccessfullyDeletedCommentResponseDto;
import com.cuki.domain.comment.dto.SuccessfullyRegisteredCommentResponseDto;
import com.cuki.domain.comment.entity.Comment;
import com.cuki.domain.comment.repository.CommentRepository;
import com.cuki.domain.member.entity.Member;
import com.cuki.domain.member.repository.MemberRepository;
import com.cuki.domain.schedule.entity.Schedule;
import com.cuki.domain.schedule.repository.SchedulesRepository;
import com.cuki.global.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final SchedulesRepository schedulesRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository, MemberRepository memberRepository, SchedulesRepository schedulesRepository) {
        this.commentRepository = commentRepository;
        this.memberRepository = memberRepository;
        this.schedulesRepository = schedulesRepository;
    }

    // 댓글 등록
    public SuccessfullyRegisteredCommentResponseDto registerComment(RegisterCommentRequestDto registerCommentRequestDto) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new IllegalArgumentException("디비에서 현재 접속중인 멤버의 id를 찾을 수 없습니다."));

        Schedule schedule = schedulesRepository.findById(registerCommentRequestDto.getScheduleId())
                .orElseThrow(() -> new IllegalArgumentException("디비에서 해당 게시글 번호로는 스케줄을 찾을 수 없습니다."));

        Comment comment = commentRepository.save(registerCommentRequestDto.of(member, schedule));

        return SuccessfullyRegisteredCommentResponseDto.builder()
                .commentId(comment.getId())
                .build();
    }

    // 댓글 조회 - 특정 게시물 기준
    public List<CommentResponseDto> getComments(Long scheduleId) {
        List<Comment> comments = commentRepository.findAllByScheduleIdOrderByCreatedDateDesc(scheduleId);
        List<CommentResponseDto> responseDtos = new ArrayList<>();

        for (Comment comment : comments) {
            responseDtos .add(
                    CommentResponseDto.builder()
                            .commentId(comment.getId())
                            .nickname(comment.getMember().getNickname())
                            .content(comment.getContent())
                            .textDate(makeTextDate(comment.getCreatedDate()))
                            .build()
            );
        }

        return responseDtos ;
    }

    // 댓글 삭제
    public SuccessfullyDeletedCommentResponseDto deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);

        return SuccessfullyDeletedCommentResponseDto.builder()
                .commentId(commentId)
                .build();
    }

    // textDate 만들기
    private String makeTextDate(LocalDateTime createdDate) {
        final int MAX_SEC = 60;
        final int MAX_MIN = 60;
        final int MAX_HOUR = 24;
        final int MAX_DAY = 7;

        long curTime = System.currentTimeMillis();
        long regTime = Timestamp.valueOf(createdDate).getTime();
        long diffTime = (curTime - regTime) / 1000;
        String textDate = "";

        if (diffTime < MAX_SEC)
            textDate = diffTime + "초";
        else if ((diffTime /= MAX_SEC) < MAX_MIN)
            textDate = diffTime + "분";
        else if ((diffTime /= MAX_MIN) < MAX_HOUR)
            textDate = diffTime + "시간";
        else if ((diffTime /= MAX_HOUR) < MAX_DAY)
            textDate = diffTime + "일";
        else
            textDate = (diffTime /= MAX_DAY) + "주";

        return textDate;
    }
}
