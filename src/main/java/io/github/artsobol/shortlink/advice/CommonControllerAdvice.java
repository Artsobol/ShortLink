package io.github.artsobol.shortlink.advice;

import io.github.artsobol.shortlink.exception.BaseException;
import io.github.artsobol.shortlink.exception.ErrorResponse;
import io.github.artsobol.shortlink.exception.ValidationErrorResponse;
import io.github.artsobol.shortlink.exception.ValidationFieldError;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class CommonControllerAdvice {

    private final MessageSource messageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        List<ValidationFieldError> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> new ValidationFieldError(err.getField(), resolveValidationMessage(err)))
                .toList();

        String message = createMessage("validation.error", null);

        ValidationErrorResponse response = new ValidationErrorResponse(
                OffsetDateTime.now(ZoneOffset.UTC),
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI(),
                errors
        );

        log.info("Validation error: path={}, errors couns={}", request.getRequestURI(), errors.size());
        log.debug("Validation error: {}", response);

        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(BaseException ex, HttpServletRequest request) {
        HttpStatus status = ex.getStatus();
        String message = createMessage(ex.getMessageKey(), ex.getMessageArgs());

        ErrorResponse response = new ErrorResponse(
                OffsetDateTime.now(ZoneOffset.UTC),
                status.value(),
                status.getReasonPhrase(),
                ex.getErrorCode(),
                message,
                request.getRequestURI()
        );

        log.info("App error: status={}, error code={}, path={}", status, ex.getErrorCode(), request.getRequestURI());
        log.debug("App error: {}", response);

        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String message = createMessage("unexpected.error", null);

        log.error("Unexpected error: status={}, message={}", status, ex.getMessage(), ex);
        return ResponseEntity.status(status).body(new ErrorResponse(
                OffsetDateTime.now(ZoneOffset.UTC),
                status.value(),
                status.getReasonPhrase(),
                "INTERNAL_SERVER_ERROR",
                message,
                request.getRequestURI()
        ));
    }

    private String resolveValidationMessage(org.springframework.validation.FieldError err) {
        String defaultMessage = err.getDefaultMessage();
        if (defaultMessage == null || defaultMessage.isBlank()) {
            return "Validation error";
        }

        try {
            return messageSource.getMessage(defaultMessage, err.getArguments(), LocaleContextHolder.getLocale());
        } catch (Exception ignored) {
            return defaultMessage;
        }
    }

    private String createMessage(String key, Object[] args) {
        try {
            var locale = LocaleContextHolder.getLocale();
            log.debug("Resolving message: key={}, locale={}, args={}", key, locale, args);
            return messageSource.getMessage(key, args, locale);
        } catch (Exception e) {
            log.debug("Cannot resolve message key={}", key, e);
            return "Unexpected error";
        }
    }
}
