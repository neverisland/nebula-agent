package cn.yang.nebula.agent.aop.valid;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 通用校验器
 *
 * @author : QingHai
 */
public class EnumValidValidator implements ConstraintValidator<EnumValid, Object> {

    private Set<Object> enumValues;
    private boolean allowNull;

    @Override
    public void initialize(EnumValid annotation) {
        this.allowNull = annotation.allowNull();

        Class<? extends Enum<?>> enumClass = annotation.enumClass();

        enumValues = Arrays.stream(enumClass.getEnumConstants())
                .map(e -> {
                    if (e instanceof JudgeEnum<?> judgeEnum) {
                        return judgeEnum.getValue();
                    }
                    return ((Enum<?>) e).name();
                })
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return allowNull;
        }
        return enumValues.contains(value);
    }
}
