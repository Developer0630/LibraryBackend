package com.library.library_manager.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {
    // 404 Not Found
    STAFF_NOT_FOUND(40403, "Staff member not found!", HttpStatus.NOT_FOUND),
    POSITION_NOT_FOUND(40404, "Position not found!", HttpStatus.NOT_FOUND),
    USER_NOT_FOUND(40405, "User not found!", HttpStatus.NOT_FOUND),

    // 400 Bad Request
    USER_ALREADY_EXISTS(40001, "Username or email already exists!", HttpStatus.BAD_REQUEST),
    INVALID_INPUT(40000, "Invalid input data", HttpStatus.BAD_REQUEST),

    // 401 Unauthorized
    UNAUTHORIZED(40101, "Username or password is not correct", HttpStatus.UNAUTHORIZED),

    // 500 Internal Server Error
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR)
    ;

    int code;
    String message;
    HttpStatus status;
}