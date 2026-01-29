package cn.yang.nebula.agent.aop.idempotent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * 幂等 Redis 服务
 * <p>
 * 提供 SET NX 加锁、查询、删除操作
 * 可用于幂等 Key 管理
 *
 * @author : QingHai
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class IdempotentService {

    private final StringRedisTemplate redisTemplate;

    /**
     * 尝试 SET NX 加锁
     *
     * @param key           Redis Key
     * @param value         值
     * @param expireSeconds 过期时间（秒）
     * @return true 成功 false 失败
     */
    public boolean tryLock(String key, String value, long expireSeconds) {
        // 可能会null, 异常.
        Boolean success = redisTemplate.opsForValue().setIfAbsent(key, value, Duration.ofSeconds(expireSeconds));
        return Boolean.TRUE.equals(success);
    }

    /**
     * 设置 Redis Key 值（覆盖已有）
     *
     * @param key           Redis Key
     * @param value         值
     * @param expireSeconds 过期时间（秒）
     */
    public void set(String key, String value, long expireSeconds) {
        try {
            redisTemplate.opsForValue().set(key, value, Duration.ofSeconds(expireSeconds));
        } catch (Exception e) {
            log.error("设置 Redis Key 失败: key={}, error={}", key, e.getMessage());
        }
    }


    /**
     * 获取 Redis Key 值
     *
     * @param key Redis Key
     * @return 对应值
     */
    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 删除 Redis Key
     *
     * @param key Redis Key
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }
}
