package cn.yang.nebula.agent.autoconfigure;

import cn.yang.nebula.agent.utils.id.generator.IdGenerator;
import cn.yang.nebula.agent.utils.id.generator.LocalIdGenerator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * id生成器自动装配
 *
 * @author : 未见清海
 */
@Configuration
public class IdGenerateAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public IdGenerator idGenerator() {
        return new LocalIdGenerator();
    }

}
