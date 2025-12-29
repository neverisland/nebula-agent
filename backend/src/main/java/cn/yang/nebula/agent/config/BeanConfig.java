package cn.yang.nebula.agent.config;

import cn.yang.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author : QingHai
 */
@Configuration
public class BeanConfig {

    @Bean
    @Lazy
    public PasswordEncoder passwordEncoder() {
        return new Pbkdf2PasswordEncoder();
    }
}
