package io.cuki.domain.participation.exception;

public class PermissionIsAlreadyDecidedException extends RuntimeException {

    public PermissionIsAlreadyDecidedException(String message) {
        super(message);
    }
}
