package com.alex.studentmanagementsystem.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiRequestException extends RuntimeException {
    private HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
    public ApiRequestException(String message) {
        super(message);
    }

    public ApiRequestException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
