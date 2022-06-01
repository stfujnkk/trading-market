package cn.lyf.common.valid.validator;

import cn.lyf.common.valid.EnumIntValid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;

public class EnumIntValidValidator implements ConstraintValidator<EnumIntValid, Integer> {
    private Set<Integer> set = new HashSet<>();

    @Override
    public void initialize(EnumIntValid constraintAnnotation) {
        for (int v : constraintAnnotation.value()) {
            set.add(v);
        }
    }

    @Override
    public boolean isValid(Integer integer, ConstraintValidatorContext constraintValidatorContext) {
        if (integer == null) return true;
        return set.contains(integer);
    }
}
