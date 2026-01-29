package cn.yang.nebula.agent.aop.idempotent;

import cn.yang.nebula.agent.utils.json.JsonUtils;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.Ordered;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.annotation.Order;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 幂等切面
 * <p>
 * 通过 @Idempotent 注解实现接口幂等：
 * <p>
 * 1. 支持 Redis Key 前缀（默认 + 注解自定义）
 * <p>
 * 2. 支持 SpEL 解析幂等 Key
 * <p>
 * 3. 处理重复请求直接返回已有结果
 * <p>
 * 4. 异常自动释放 Redis Key
 *
 * @author : QingHai
 */
@Aspect
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 2)
@RequiredArgsConstructor
public class IdempotentAspect {

    private final IdempotentService idempotentService;

    private final IdempotentProperties properties;

    // SpEL 表达式解析器
    private final ExpressionParser parser = new SpelExpressionParser();

    private final ParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();

    // 幂等处理中
    private final static String PROCESSING = "PROCESSING";

    /**
     * 切面核心方法
     *
     * @param pjp 当前被切方法
     * @return 业务方法返回结果
     * @throws Throwable 执行异常抛出
     */
    @Around("@annotation(cn.yang.nebula.agent.aop.idempotent.Idempotent)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {

        Method method = ((MethodSignature) pjp.getSignature()).getMethod();

        Idempotent idempotent = method.getAnnotation(Idempotent.class);

        // 解析 SpEL Key
        String bizKey = parseKey(idempotent.key(), method, pjp.getArgs());
        if (bizKey == null || bizKey.isBlank()) {
            throw new IllegalStateException("幂等 Key 为空,请检查注解配置或请求参数");
        }

        // 2. 拼接最终 Redis Key（注解 prefix 优先，默认配置 fallback）
        String finalKey = buildFinalKey(idempotent.prefix(), bizKey);

        // 3. 尝试 Redis SET NX 加锁
        boolean locked = idempotentService.tryLock(finalKey, PROCESSING, idempotent.expireSeconds());

        // 加锁失败的时候
        if (!locked) {
            // 已存在 Key，直接返回已保存结果
            String cached = idempotentService.get(finalKey);
            if (cached != null && !PROCESSING.equals(cached)) {
                Class<?> returnClass = method.getReturnType();
                // 如果方法返回类型是 void，直接返回 null
                if (returnClass == Void.TYPE) {
                    return null;
                }

                return JsonUtils.stringToObject(cached, returnClass);
            }
            // 有可能是加锁失败了，暂时不考虑
            throw new IllegalStateException("请求正在处理,请稍后重试");
        }

        try {
            // 执行业务方法
            Object result = pjp.proceed();
            // 保存业务结果到 Redis，确保重复请求返回同样结果
            idempotentService.set(finalKey, JsonUtils.objectToString(result), idempotent.expireSeconds());

            return result;
        } catch (Throwable ex) {
            // 异常处理：删除 Redis Key
            if (idempotent.deleteOnException()) {
                idempotentService.delete(finalKey);
            }
            throw ex;
        }
    }

    /**
     * 构建最终 Redis Key
     *
     * @param annoPrefix 注解前缀
     * @param bizKey     业务 Key
     * @return Redis 最终 Key
     */
    private String buildFinalKey(String annoPrefix, String bizKey) {
        if (annoPrefix != null && !annoPrefix.isBlank()) {
            return annoPrefix + ":" + bizKey;
        }
        return properties.getPrefix() + ":" + bizKey;
    }

    /**
     * 解析 SpEL 表达式生成业务 Key
     *
     * @param spEl   SpEL 表达式
     * @param method 被切方法
     * @param args   方法参数
     * @return 解析后的 Key
     */
    private String parseKey(String spEl, Method method, Object[] args) {
        EvaluationContext context = new StandardEvaluationContext();
        String[] paramNames = nameDiscoverer.getParameterNames(method);
        for (int i = 0; i < paramNames.length; i++) {
            context.setVariable(paramNames[i], args[i]);
        }
        return parser.parseExpression(spEl).getValue(context, String.class);
    }

}
