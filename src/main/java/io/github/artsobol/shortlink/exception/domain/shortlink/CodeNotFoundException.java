package io.github.artsobol.shortlink.exception.domain.shortlink;

import io.github.artsobol.shortlink.exception.base.BaseException;
import org.springframework.http.HttpStatus;

import java.util.Map;

public class CodeNotFoundException extends BaseException {

    public CodeNotFoundException(String code) {
        super("CODE_NOT_FOUND", "code.not.found", HttpStatus.NOT_FOUND, Map.of("code", code), null, code);
    }
}
