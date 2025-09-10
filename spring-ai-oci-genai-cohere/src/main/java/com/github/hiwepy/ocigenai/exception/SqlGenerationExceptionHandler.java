package com.github.hiwepy.ocigenai.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SqlGenerationExceptionHandler {

    @ExceptionHandler(SqlGenerationException.class)
    public ProblemDetail handle(SqlGenerationException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.EXPECTATION_FAILED, ex.getMessage());
    }

}
