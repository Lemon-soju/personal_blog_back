package com.lemonSoju.blog.exception;

public class JwtTokenNullException extends CustomException {

    private static final String MESSAGE = "accessToken 값이 Null 입니다.";

    public JwtTokenNullException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 401;
    }
}
