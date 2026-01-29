package cn.yang.nebula.agent.config;

import org.springframework.boot.cache.autoconfigure.CacheProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;
import java.util.Map;

/**
 * 使用spring cache 配置
 *
 * @author : QingHai
 */
@Configuration
@EnableCaching
public class CachingConfig {

    @Bean
    public CacheProperties cacheProperties() {
        return new CacheProperties();
    }

    @Primary
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory, CacheProperties cacheProperties) {

        RedisSerializer<?> jsonSerializer = RedisSerializer.json();

        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                // 设置过期时间
                .entryTtl(cacheProperties.getRedis().getTimeToLive() != null ? cacheProperties.getRedis().getTimeToLive() : Duration.ofHours(3))
                // 禁用缓存空值
                .disableCachingNullValues()
                // Key 依然建议使用 String 序列化
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.string()))
                // Value 使用最新的 JSON 序列化
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jsonSerializer))
                .disableCachingNullValues();

        return RedisCacheManager.builder(connectionFactory).cacheDefaults(config) // 应用默认配置
                .withInitialCacheConfigurations(Map.ofEntries(

//                        Map.entry(CacheSpaceEnum.SMS_VERIFICATION_CODE_SEND.getMark(), RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(3)))

                )).build();
    }
}
