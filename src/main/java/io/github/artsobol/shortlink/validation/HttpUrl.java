package io.github.artsobol.shortlink.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.URL;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@URL(message = "{validation.url.invalid}")
@Pattern(regexp = "^https?://.*", message = "{validation.url.protocol}")
public @interface HttpUrl {

    String message() default "{validation.url.invalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
