package cn.yang.nebula.agent.aop.valid;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * 自定义权限注解
 *
 * @author : QingHai
 */
@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EnumValidValidator.class)
public @interface EnumValid {

    String message() default "枚举值不合法";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * 枚举类
     */
    Class<? extends Enum<?>> enumClass();

    /**
     * 是否允许为空
     */
    boolean allowNull() default false;

}
