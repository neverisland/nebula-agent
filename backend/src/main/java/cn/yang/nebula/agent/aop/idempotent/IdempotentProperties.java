package cn.yang.nebula.agent.aop.idempotent;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 幂等配置属性
 * <p>
 * 支持全局 Redis Key 前缀
 * 可通过 application.yml/application.properties 配置
 *
 * @author : QingHai
 */
@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "idempotent.redis")
public class IdempotentProperties {

    /**
     * 默认 Redis Key 前缀
     */
    private String prefix = "idempotent:item";

}
