package cn.yang.nebula.agent.aop.agent;

import cn.yang.nebula.agent.utils.json.JsonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * 切面日志记录
 */
@Slf4j
@Aspect
@Component
public class AgentLogAspect {

    public static final ThreadLocal<String> agentResponse = new ThreadLocal<>();
    private static final ThreadLocal<Long> startTime = new ThreadLocal<>();

    @Pointcut(value = "@annotation(cn.yang.nebula.agent.aop.agent.AgentLog)")
    private void pointcut() {
        // Do nothing because of pointcut expression
    }

    /**
     * 进入方法前需打印参数，记录开始时间
     */
    @Before(value = "pointcut()")
    public void before(JoinPoint joinPoint) throws JsonProcessingException {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String methodName = method.getName();
        log.info("进入方法[{}],参数[{}],当前时间[{}]", methodName, JsonUtils.objectToString(joinPoint.getArgs()), new Date());
        // 设置当前时间搓
        startTime.set(System.currentTimeMillis());
    }

    /**
     * 最后走此方法出，打印结果并记录耗时时间
     *
     * @return Object
     */
    @AfterReturning(value = "pointcut()", returning = "result")
    public Object afterReturn(JoinPoint joinPoint, Object result) throws JsonProcessingException {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String methodName = method.getName();
        long start = startTime.get();
        long now = System.currentTimeMillis();
        log.info("退出方法[{}],参数[{}],最终结果[{}],三方结果[{}],当前时间[{}],耗时[{}]", methodName,
                JsonUtils.objectToString(joinPoint.getArgs()),
                JsonUtils.objectToString(result),
                agentResponse.get(),
                new Date(), (now - start));
        // 移除
        startTime.remove();
        agentResponse.remove();
        return result;
    }

}
