package cn.yang.nebula.agent.aop.agent;

import java.lang.annotation.*;

/**
 * 二方包方法日志注解
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AgentLog {

}
