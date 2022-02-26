package com.patika.paycore.entity.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IdentityNumberValidator.class)
public @interface IdentityNumber {
    String message() default "IdentityNumber is invalid.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
