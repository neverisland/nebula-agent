package cn.yang.nebula.agent.integrate.file.service;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : QingHai
 */
@Configuration
public class OssConfig {

    /**
     * OSS 访问端点
     */
    @Value("${file.oss.endpoint:https://oss-cn-hangzhou.aliyuncs.com}")
    private String endpoint;

    @Bean
    public OSS ossClient() {
        String accessKeyId = System.getenv("ALI_YUN_ACCESS_KEY_ID");
        String secretAccessKey = System.getenv("ALI_YUN_ACCESS_SECRET_KEY");
        return new OSSClientBuilder().build(endpoint, accessKeyId, secretAccessKey);
    }
}
