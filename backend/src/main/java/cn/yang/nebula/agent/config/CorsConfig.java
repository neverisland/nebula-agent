package cn.yang.nebula.agent.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 跨域配置
 *
 * @author : QingHai
 */
@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        // 允许的源（生产环境应配置具体域名）
                        .allowedOriginPatterns("*")
                        // 允许的请求方法
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        // 允许的请求头
                        .allowedHeaders("*")
                        // 是否允许携带凭证（如cookies）
                        .allowCredentials(true)
                        // 预检请求缓存时间（秒）
                        .maxAge(3600);
            }
        };
    }
}