package io.cuki.domain.comment.repository;

import io.cuki.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByScheduleIdOrderByCreatedDateDesc(Long ScheduleId);
}
