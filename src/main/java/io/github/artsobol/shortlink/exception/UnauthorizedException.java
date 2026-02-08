package io.github.artsobol.shortlink.exception;

import org.springframework.http.HttpStatus;

import java.util.Map;

public class UnauthorizedException extends BaseException {
    public UnauthorizedException(String messageKey, Object... args) {
        super(messageKey, messageKey, HttpStatus.UNAUTHORIZED, Map.of(), null, args);
    }
}
