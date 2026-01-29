package cn.yang.nebula.agent.aop.lock;

import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 锁 Key 解析器（SpEL）。
 * <p>
 * 负责将注解中的 SpEL key 表达式解析成实际值，并与 prefix 组合成最终 lockKey。
 *
 * <p>示例：
 * <pre>
 *   prefix="order:pay", key="#orderId"  => order:pay:123
 *   prefix="order:pay", key="#req.userId + ':' + #req.orderId" => order:pay:7:123
 * </pre>
 *
 * <p>支持变量：
 * <ul>
 *   <li>#p0/#a0：第0个参数</li>
 *   <li>#参数名：需要编译参数 -parameters 才能稳定工作</li>
 * </ul>
 *
 * @author : QingHai
 */
public class SpElLockKeyResolver {

    /**
     * SpEL 解析器
     */
    private final ExpressionParser parser = new SpelExpressionParser();

    /**
     * 参数名发现器：用于支持 #orderId 这种按参数名访问
     */
    private final ParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();

    /**
     * 解析最终锁 key。
     *
     * @param method 目标方法
     * @param args   方法入参
     * @param ann    注解对象
     * @return 最终 lockKey
     */
    public String resolveKey(Method method, Object[] args, DistributedLock ann) {
        // 1) 解析动态 key 部分
        String dynamicPart = evalSpel(method, args, ann.key());

        // 2) 规范化 prefix
        String prefix = normalizePrefix(ann.prefix());

        // 3) 拼接最终 key
        String key = prefix.isEmpty() ? dynamicPart : (prefix + ":" + dynamicPart);

        // 4) 防御：禁止空 key（避免错误导致全局串行化/锁冲突）
        if (key == null || key.isBlank()) {
            throw new IllegalArgumentException("@DistributedLock resolved blank key, SpEL=" + ann.key());
        }
        return key;
    }

    /**
     * 执行 SpEL 表达式求值。
     *
     * @param method 目标方法
     * @param args   方法入参
     * @param spel   SpEL 表达式字符串
     * @return 表达式计算结果（转为字符串）
     */
    private String evalSpel(Method method, Object[] args, String spel) {
        StandardEvaluationContext context = new StandardEvaluationContext();

        // 支持 #p0/#a0 访问参数
        for (int i = 0; i < args.length; i++) {
            context.setVariable("p" + i, args[i]);
            context.setVariable("a" + i, args[i]);
        }

        // 支持按参数名访问：#orderId / #req
        String[] paramNames = nameDiscoverer.getParameterNames(method);
        for (int i = 0; i < paramNames.length && i < args.length; i++) {
            context.setVariable(paramNames[i], args[i]);
        }

        // 解析并执行表达式
        Expression exp = parser.parseExpression(spel);
        Object value = exp.getValue(context);

        // 统一转字符串，null 变为空串
        return Objects.toString(value, "");
    }

    /**
     * 规范化 prefix：去空格、去掉末尾多余冒号。
     *
     * @param prefix 注解 prefix
     * @return 规范化后的 prefix
     */
    private String normalizePrefix(String prefix) {
        if (prefix == null) {
            return "";
        }
        String p = prefix.trim();
        // 避免 prefix="order:" 造成双冒号
        if (p.endsWith(":")) {
            p = p.substring(0, p.length() - 1);
        }
        return p;
    }
}
