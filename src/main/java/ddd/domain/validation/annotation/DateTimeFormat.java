/*
 * Copyright(C) 2013 Sanyu Academy All rights reserved.
 *
 */
package ddd.domain.validation.annotation;

import java.lang.annotation.Documented;
import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = {ddd.domain.validation.annotation.DateTimeFormat.Validator.class})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
public @interface DateTimeFormat {

    String message() default "{ddd.domain.validation.annotation.DateTimeFormat.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
    @Retention(RUNTIME)
    @Documented
    public @interface List {

        DateTimeFormat[] value();
    }

    public class Validator implements ConstraintValidator<DateTimeFormat, String> {

        @Override
        public void initialize(DateTimeFormat value) {
        }

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            try {
                LocalDate.parse(value, DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT));
            } catch (DateTimeParseException ex) {
                return false;
            }
            return true;
        }
    }

}
