package cn.yang.nebula.agent.aop;

import java.lang.annotation.*;

/**
 * 方法出入参日志打印标志
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Documented
public @interface MethodLog {

    /**
     * 是否需要脱敏
     */
    boolean isDesensitize() default false;
}
