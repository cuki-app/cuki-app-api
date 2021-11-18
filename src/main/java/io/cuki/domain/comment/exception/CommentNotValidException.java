package io.cuki.domain.comment.exception;

public class CommentNotValidException extends RuntimeException {
    public CommentNotValidException(String message) {
        super(message);
    }
}
