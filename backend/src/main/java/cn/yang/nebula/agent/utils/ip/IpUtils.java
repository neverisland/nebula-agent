package cn.yang.nebula.agent.utils.ip;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

/**
 * IP地址工具类
 */
public class IpUtils {

    /**
     * 获取客户端真实IP（支持反向代理/负载均衡）
     */
    public static String getClientIp() {
        // 从当前请求上下文获取HttpServletRequest
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (Objects.isNull(requestAttributes)) {
            return "unknown"; // 非Web环境（如定时任务）返回unknown
        }
        HttpServletRequest request = requestAttributes.getRequest();
        return getClientIp(request);
    }

    /**
     * 从HttpServletRequest中获取真实IP
     */
    private static String getClientIp(HttpServletRequest request) {
        // 依次从反向代理头中获取真实IP
        String ip = request.getHeader("X-Forwarded-For");
        if (isInvalidIp(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (isInvalidIp(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (isInvalidIp(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (isInvalidIp(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        // 若以上都获取不到，直接取远程地址
        if (isInvalidIp(ip)) {
            ip = request.getRemoteAddr();
        }
        // 处理X-Forwarded-For多IP场景（第一个为真实IP）
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }

    /**
     * 判断IP是否无效（null/空字符串/unknown）
     */
    private static boolean isInvalidIp(String ip) {
        return ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip);
    }
}