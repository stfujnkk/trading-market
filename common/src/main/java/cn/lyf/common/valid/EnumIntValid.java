package cn.lyf.common.valid;

import cn.lyf.common.valid.validator.EnumIntValidValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Constraint(validatedBy = {EnumIntValidValidator.class})
@Retention(RUNTIME)
public @interface EnumIntValid {
    String message() default "{cn.lyf.common.valid.EnumIntValid.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int[] value();
}
