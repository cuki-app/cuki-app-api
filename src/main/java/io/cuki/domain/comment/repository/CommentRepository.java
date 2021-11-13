package io.cuki.domain.comment.repository;

import io.cuki.domain.comment.entity.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @EntityGraph(attributePaths = "member")
    Slice<Comment> findAllByScheduleId(Long scheduleId, Pageable pageable);

    Optional<Comment> findById(Long commentId);
}
