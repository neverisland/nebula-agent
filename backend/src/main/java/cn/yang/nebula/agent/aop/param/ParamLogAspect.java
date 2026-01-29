package cn.yang.nebula.agent.aop.param;

import cn.yang.nebula.agent.exception.BusinessException;
import cn.yang.nebula.agent.utils.ip.IpUtils;
import cn.yang.nebula.agent.utils.json.JsonUtils;
import cn.yang.nebula.agent.vo.result.ResultFactory;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 日志切面
 */
@Slf4j
@Aspect
@Component(value = "AssetParamLogAspect")
public class ParamLogAspect {

    @Around("@within(cn.yang.nebula.agent.aop.param.ParamLog)")
    public Object doRequestClass(ProceedingJoinPoint pjp) throws Throwable {
        return execWithLog(pjp);
    }

    @Around("@annotation(cn.yang.nebula.agent.aop.param.ParamLog)")
    public Object doRequestMethod(ProceedingJoinPoint pjp) throws Throwable {
        return execWithLog(pjp);
    }

    private Object execWithLog(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.currentTimeMillis();
        String name = pjp.getSignature().getName();
        Class<?> clazz = pjp.getTarget().getClass();
        String clientIp = IpUtils.getClientIp(); // 获取客户端IP
        ParamLog annotation = getParamLog(pjp);
        if (Objects.nonNull(annotation) && annotation.open()) {
            try {
                Object result = pjp.proceed();
                long end = System.currentTimeMillis();
                log.info("InvokeMonitor - 服务名称:[{}]\n访问IP:[{}]\n服务接口:[{}]\n方法:[{}]\n耗时:[{}]\n入参:[{}]\n出参:[{}]",
                        "xiaozhi-backend-web", clientIp, clazz.getCanonicalName(), name, end - start,
                        JsonUtils.objectToString(pjp.getArgs()),
                        JsonUtils.objectToString(result));
                return result;
            } catch (BusinessException e) {
                long end = System.currentTimeMillis();
                log.warn("InvokeMonitor - 抛出自定义异常 - 服务名称:[{}]\n访问IP:[{}]\n服务接口:[{}]\n方法:[{}]\n耗时:[{}]\n入参:[{}]\n堆栈:",
                        "xiaozhi-backend-web", clientIp, clazz.getCanonicalName(), name, end - start,
                        JsonUtils.objectToString(pjp.getArgs()), e);
                return ResultFactory.failure(e, e.getDetails());
            } catch (Exception e) {
                long end = System.currentTimeMillis();
                log.warn("InvokeMonitor - 抛出异常 - 服务名称:[{}]\n访问IP:[{}]\n服务接口:[{}]\n方法:[{}]\n耗时:[{}]\n入参:[{}]\n堆栈:",
                        "xiaozhi-backend-web", clientIp, clazz.getCanonicalName(), name, end - start,
                        JsonUtils.objectToString(pjp.getArgs()), e);
                throw e;
            }
        } else {
            return pjp.proceed();
        }

    }

    private ParamLog getParamLog(ProceedingJoinPoint pjp) {
        Class<?> clazz = pjp.getTarget().getClass();
        ParamLog classAnnotation = clazz.getAnnotation(ParamLog.class);
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        ParamLog methodAnnotation = methodSignature.getMethod().getAnnotation(ParamLog.class);
        return Objects.nonNull(methodAnnotation) ? methodAnnotation : classAnnotation;
    }
}
