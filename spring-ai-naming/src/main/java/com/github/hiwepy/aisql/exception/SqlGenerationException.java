package com.github.hiwepy.aisql.exception;

public class SqlGenerationException extends RuntimeException {
    public SqlGenerationException(String response) {
        super(response);
    }
}
