package cn.yang.nebula.agent.aop.idempotent;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 幂等注解
 *
 * @author : QingHai
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Idempotent {

    /**
     * SpEL 表达式，用于获取幂等 Key
     * 示例：#req.idempotentKey
     */
    String key();

    /**
     * Key 前缀（可选）
     */
    String prefix() default "";

    /**
     * Key 过期时间（秒）
     */
    long expireSeconds() default 180;

    /**
     * 是否在异常时释放 Key
     */
    boolean deleteOnException() default true;
}
