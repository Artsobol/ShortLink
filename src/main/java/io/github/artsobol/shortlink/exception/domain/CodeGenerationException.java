package io.github.artsobol.shortlink.exception.domain;

import io.github.artsobol.shortlink.exception.base.BaseException;
import org.springframework.http.HttpStatus;

import java.util.Map;

public class CodeGenerationException extends BaseException {
    public CodeGenerationException(int retries) {
        super("FAIL_GENERATE_CODE", "code.fail.generate", HttpStatus.CONFLICT, Map.of("retries", retries), null);
    }
}
