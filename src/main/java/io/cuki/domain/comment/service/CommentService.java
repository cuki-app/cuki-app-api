package io.cuki.domain.comment.service;

import io.cuki.domain.comment.dto.CommentResponseDto;
import io.cuki.domain.comment.entity.CommentAuthority;
import io.cuki.domain.comment.exception.CommentNotFoundException;
import io.cuki.domain.comment.repository.CommentRepository;
import io.cuki.domain.member.entity.Member;
import io.cuki.domain.member.exception.MemberNotFoundException;
import io.cuki.domain.member.exception.MemberNotMatchException;
import io.cuki.domain.member.repository.MemberRepository;
import io.cuki.domain.schedule.entity.Schedule;
import io.cuki.domain.schedule.exception.ScheduleNotFoundException;
import io.cuki.domain.schedule.repository.SchedulesRepository;
import io.cuki.global.util.SecurityUtil;
import io.cuki.domain.comment.dto.RegisterCommentRequestDto;
import io.cuki.domain.comment.dto.SuccessfullyDeletedCommentResponseDto;
import io.cuki.domain.comment.dto.SuccessfullyRegisteredCommentResponseDto;
import io.cuki.domain.comment.entity.Comment;
import io.cuki.global.util.SliceCustom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
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
    @Transactional
    public SuccessfullyRegisteredCommentResponseDto registerComment(RegisterCommentRequestDto requestDto, Long scheduleId) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new MemberNotFoundException("디비에서 현재 접속중인 멤버의 id를 찾을 수 없습니다."));
        Schedule schedule = schedulesRepository.findById(scheduleId)
                .orElseThrow(ScheduleNotFoundException::new);

        Comment.isValidContent(requestDto.getContent());

        Comment comment = Comment.builder()
                .content(requestDto.getContent())
                .member(member)
                .schedule(schedule)
                .build();

        commentRepository.save(comment);

        return SuccessfullyRegisteredCommentResponseDto.builder()
                .commentId(comment.getId())
                .build();
    }

    // 댓글 조회 - 특정 게시물 기준
    public SliceCustom<CommentResponseDto> getComments(Long scheduleId, Long memberId, int page, int size) {
        if (!schedulesRepository.existsById(scheduleId)) {
            log.error("{} -> , 해당 id의 게시글은 존재하지 않습니다.", scheduleId);
            throw new ScheduleNotFoundException();
        }

        final Sort createdDate = Sort.by(Sort.Direction.DESC, "createdDate");
        final PageRequest pageRequest = PageRequest.of(page, size, createdDate);

        final Slice<Comment> commentsSlice  = commentRepository.findAllByScheduleId(scheduleId, pageRequest);
        List<CommentResponseDto> responseDtos = new ArrayList<>();

        for (Comment comment : commentsSlice) {
            log.debug("댓글 조회 : 댓글 id = {}, 댓글 생성일 = {}", comment.getId(), comment.getCreatedDate());
            responseDtos.add(
                    CommentResponseDto.builder()
                            .commentId(comment.getId())
                            .writerId(comment.getMember().getId())
                            .nickname(comment.getMember().getNickname().getNickname())
                            .content(comment.getContent())
                            .textDate(makeTextDate(comment.getCreatedDate()))
                            .commentAuthority(Objects.equals(memberId, comment.getMember().getId()) ? CommentAuthority.OWNER : CommentAuthority.GUEST)
                            .createdDate(comment.getCreatedDate())
                            .build()
            );
        }

        final Slice<CommentResponseDto> dtoSlice  = new SliceImpl<>(responseDtos, pageRequest, commentsSlice.hasNext());

        return new SliceCustom<>(responseDtos, dtoSlice.hasNext(), dtoSlice.getNumber());
    }

    // 댓글 삭제
    @Transactional
    public SuccessfullyDeletedCommentResponseDto deleteComment(Long scheduleId, Long commentId) {
        Optional<Comment> comment = commentRepository.findById(commentId);

        if (!comment.isPresent()) {
            log.error("{} -> , 해당 id의 댓글은 존재하지 않습니다.", commentId);
            throw new CommentNotFoundException();
        } else if (!Objects.equals(comment.get().getMember().getId(), SecurityUtil.getCurrentMemberId())) {
            log.error("현재 로그인 한 회원과 댓글 작성자가 일치하지 않습니다.");
            throw new MemberNotMatchException("현재 로그인 한 회원과 댓글 작성자가 일치하지 않습니다.");
        }

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
        String textDate;

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
