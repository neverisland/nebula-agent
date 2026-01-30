package cn.yang.nebula.agent.config;

import cn.yang.nebula.agent.utils.id.generator.IdGenerator;
import cn.yang.nebula.agent.utils.id.generator.LocalIdGenerator;
import cn.yang.nebula.agent.utils.password.Pbkdf2PasswordEncoder;
import cn.yang.nebula.agent.utils.username.UsernameGenerator;
import cn.yang.nebula.agent.utils.username.UsernameGeneratorImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Bean 配置类
 *
 * @author : QingHai
 */
@Configuration
public class BeanConfig {

    /**
     * 密码加密
     *
     * @return PasswordEncoder
     */
    @Bean
    @Lazy
    public PasswordEncoder passwordEncoder() {
        return new Pbkdf2PasswordEncoder();
    }

    /**
     * id生成器
     *
     * @return IdGenerator
     */
    @Bean
    @ConditionalOnMissingBean
    public IdGenerator idGenerator() {
        return new LocalIdGenerator();
    }

    /**
     * 用户名生成器自动装配
     *
     * @return UsernameGenerator
     */
    @Bean
    @ConditionalOnMissingBean
    public UsernameGenerator usernameGenerator() {
        return new UsernameGeneratorImpl();
    }
}
