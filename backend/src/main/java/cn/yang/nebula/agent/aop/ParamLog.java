package cn.yang.nebula.agent.aop;

import java.lang.annotation.*;

/**
 * 日志开关
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Documented
public @interface ParamLog {

    boolean open() default true;

    boolean logResponse() default true;
}
